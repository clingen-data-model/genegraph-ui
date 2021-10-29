(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
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

(defmethod render-full "Statement" [statement]
  [:section.section
   [:div.box
    [:h5.title.is-5
     (:curie statement)]
    [:h6.subtitle.is-6
     [:div.level
      [:div.level-left
       (for [t (:type statement)]
         ^{:key t}
         [:level-item
          (render-link t)])]]]
    ;;(map render-link (:type statement))
    ]
   [:div.columns.is-multiline
    [:div.column.is-narrow
     (render-link (:subject statement))]
    [:div.column.is-narrow
     (render-link (:predicate statement))]
    [:div.column.is-narrow
     (render-link (:object statement))]
    (for [qualifier (:qualifier statement)]
      ^{:key qualifier}
      [:div.column.is-narrow
       (render-link qualifier)])]
   (for [contribution (:contributions statement)]
     [:div.columns
      [:div.column (render-link (:attributed_to contribution))]
      [:div.column (render-link (:realizes contribution))]
      [:div.column (:date contribution)]])
   [:div.block
    (:description statement)]
   (when (:score statement)
     [:h5.title.is-5 "score: " (:score statement)])
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


