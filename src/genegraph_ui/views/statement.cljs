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

   [:p.block (render-link (:subject statement))]
   [:p.block (render-link (:predicate statement))]
   [:p.block (render-link (:object statement))]
   (when (:score statement)
     [:h5.title.is-5 "score: " (:score statement)])
   [:h5.title.is-5 "statements about"]
   [:div.block
    (for [referee (:subject_of statement)]
      (render-compact referee))]
   [:h5.title.is-5 "evidence"]
   [:div.block
    (for [evidence (:evidence statement)]
      (render-compact evidence))]])

(defmethod render-compact "Statement" [statement]
  ^{:key statement}
  [:div.columns
   [:div.column.is-narrow
    [:a.icon
     {:href (href :resource statement)}
     [:i.fas.fa-file]]]
   [:div.column.is-narrow
    [:h6.title.is-6 "score:"]
    (:score statement)]
   [:div.column.is-narrow
    (map render-link (:type statement))]
   [:div.column.is-narrow
    (render-link (:subject statement))]
   [:div.column.is-narrow
    (render-link (:object statement))]])


