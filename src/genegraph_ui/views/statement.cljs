(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

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
   [:div.block
    (:description statement)]
   (when (:score statement)
     [:h5.title.is-5 "score: " (:score statement)])
   [:h5.title.is-5 "statements about"]
   [:div.block
    (for [referee (:subject_of statement)]
      (render-compact referee))]
   [:h5.title.is-5 "used as evidence by"]
   [:h5.title.is-5 "direct evidence"]
   [:div.block
    (for [evidence (:direct_evidence statement)]
      (render-compact evidence))]
   [:h5.title.is-5 "genetic evidence"]
   [:div.block
    (for [evidence (:genetic_evidence statement)]
      (render-compact evidence))]
   [:h5.title.is-5 "experimental evidence"]
   [:div.block
    (for [evidence (:experimental_evidence statement)]
      (render-compact evidence))]])

(defmethod render-compact "Statement"
  ([statement]
   (render-compact statement {}))
  ([statement options]
   ^{:key statement}
   [:div.columns
    [:div.column.is-narrow
     [:a.icon
      {:href (href :resource statement)}
      [:i.fas.fa-file]]]
    (when (:score statement)
      [:div.column.is-narrow
       [:h6.title.is-6 "score:"]
       (:score statement)])
    (when-not (some #(= :type %) (:skip options))
      [:div.column.is-narrow
       (map render-link (:type statement))])
    (when-not (= (:source options) (get-in statement [:subject :curie]))
      [:div.column.is-narrow
       (render-link (:subject statement))])
    (when-not (= (:source options) (get-in statement [:object :curie]))
      [:div.column.is-narrow
       (render-link (:object statement))])]))


