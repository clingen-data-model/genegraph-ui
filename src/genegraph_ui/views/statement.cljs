(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date type-tags]]
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

(defn statement-definition [statement]
  [:div.block
   [:div.block.mb-0 (render-list-item (:subject statement))]
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
      (:description statement)]
     (for [linked-statement (:subject_of statement)]
       (render-list-item linked-statement))]]
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
         (:score statement) [:div.break
                             [:a.has-text-weight-semibold
                              {:href (href :resource statement)}
                              "score: " (:score statement)]
                             (when (:calculated_score statement)
                               (str " (default " (:calculated_score statement) ")"))]
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
          [:div.column.is-family-secondary
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
         :else (curie-label resource))])

(defmethod render-list-item "Statement" [resource]
  [:div.columns
   [:div.column.is-narrow
    [:div.break (render-link resource)]
    (type-tags resource)]
   [:div.column
    [:div.columns.is-multiline.p-2
     [:div.column.is-narrow.p-1 (render-list-item (:subject resource))]
     [:div.column.is-narrow.p-1 (render-link (:predicate resource))]
     [:div.column.is-narrow.p-1 (render-link (:object resource))]
     (for [qualifier (:qualifier resource)]
       [:div.column.is-narrow.p-1 (render-link qualifier)])]]])
