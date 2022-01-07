(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date]]
            [markdown.core :as md :refer [md->html]]
            [reitit.frontend.easy :refer [href]]))

(defn render-compact-grouped-by-type [resources]
  (for [[type resources-with-type] (group-by #(-> % :type first) resources)]
    ^{:key type}
    [:div.box
     [:h6.title.is-6 (render-link type)]
     (for [resource resources-with-type]
       ^{:key resource}
       [:div.box
        (render-compact resource {:skip [:type]})])]))

;; (defn statement-definition [statement]
;;   [:div.columns.is-multiline
;;    [:div.column.is-narrow
;;     (render-link (:subject statement))]
;;    [:div.column.is-narrow
;;     (render-link (:predicate statement))]
;;    [:div.column.is-narrow
;;     (render-link (:object statement))]
;;    (for [qualifier (:qualifier statement)]
;;      ^{:key qualifier}
;;      [:div.column.is-narrow
;;       (render-link qualifier)])])


(defn statement-definition [statement]
  [:div.block
   [:div.block.mb-0 (render-link (:subject statement))]
   [:div.block.mb-0 (render-link (:predicate statement))]
   [:div.block.mb-0 (render-link (:object statement))]
   (for [qualifier (:qualifier statement)]
     [:div.block.mb-0 (render-link qualifier) " "])])

(defn statement-types [statement]
  [:div.level
   [:div.level-left
    (for [t (:type statement)]
      ^{:key t}
      [:level-item.tag
       (render-link t)])]])

(defn statement-provenance [statement]
  (for [contribution (:contributions statement)]
    [:div.block
     [:div.block.mb-0 (render-link (:attributed_to contribution))]
     [:div.block.mb-0 (render-link (:realizes contribution))]
     [:div.block.mb-0 (trim-iso-date (:date contribution))]]))

(defn statement-score [statement]
  (when (:score statement)
    [:h5.title.is-5 "score: " (:score statement)]))

(defmethod render-full "Statement" [statement]
  [:div
   [:div.columns
    [:div.column.is-two-fifths
     [:h3.title.is-3
      (curie-label statement)]
     (statement-types statement)
     (statement-definition statement)
     (statement-score statement)
     (statement-provenance statement)]
    [:div.column
     [:div.block.is-family-secondary
      (:description statement)]]]
   [:h5.title.is-5 "statements about"]
   [:div.block
    (render-compact-grouped-by-type (:subject_of statement))]
   [:h5.title.is-5 "used as evidence by"]
   [:h5.title.is-5 "direct evidence"]
   [:div.block
    (for [evidence (:direct_evidence statement)]
      (render-compact evidence))]
   [:h5.title.is-5 "genetic evidence"]
   [:div.block
    (render-compact-grouped-by-type (:genetic_evidence statement))]
   [:h5.title.is-5 "experimental evidence"]
   [:div.block
    (render-compact-grouped-by-type (:experimental_evidence statement))]])

(defmethod render-compact "Statement"
  ([statement]
   (render-compact statement {}))
  ([statement options]
   ^{:key statement}
   [:div.columns
    [:div.column
     [:div.columns
      [:div.column.is-one-third
       (cond
         (:score statement) [:a.break.has-text-weight-semibold
                             {:href (href :resource statement)}
                             "score: " (:score statement)]
         :else [:a.break.icon
                {:href (href :resource statement)}
                [:i.fas.fa-file]])
       (when-not (some #(= :type %) (:skip options))
         [:div.break
          (map render-link (:type statement))])
       (when-not (= (:source options) (get-in statement [:subject :curie]))
         [:div.break
          (render-link (:subject statement))])
       [:div.break
        (render-link (:predicate statement))]
       (when-not (= (:source options) (get-in statement [:object :curie]))
         [:div.break
          (render-link (:object statement))])
       (for [qualifier (:qualifier statement)]
         ^{:key qualifier}
         [:div.break
          (render-link qualifier)])]
      (when-let [description (:description statement)]
        (let [description-segments (re-seq #"(?:\S+\W+\n?){1,50}" description)]
          [:div.column
           (first description-segments)
           (when (< 1 (count description-segments))
             "...")]))]
     (for [evidence (:evidence statement)]
       ^{:key evidence}
       [:div.columns
        [:div.column (render-compact evidence)]])]]))

(defmethod render-link "Statement"
  [resource]
  ^{:key resource}
  [:a
   {:href (href :resource resource)}
   (cond (:label resource) (:label resource)
         
         ;; (and (get-in resource [:subject :label])
         ;;      (get-in resource [:predicate :label])
         ;;      (get-in resource [:object :label]))
         ;; [:div
         ;;  (get-in resource [:subject :label]) " "
         ;;  (get-in resource [:predicate :label]) " "
         ;;  (get-in resource [:object :label])]
         
         :else (curie-label resource))])
