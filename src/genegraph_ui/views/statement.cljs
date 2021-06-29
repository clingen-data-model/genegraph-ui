(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "Assertion" [statement]
  [:section.section
   [:h5.title.is-5 "definition"]
   [:p.block (render-link (:subject statement))]
   [:p.block (render-link (:predicate statement))]
   [:p.block (render-link (:object statement))]
   [:h5.title.is-5 "statements about"]
   [:div.block
    (for [referee (:subject_of statement)]
      (render-compact referee))]
   [:h5.title.is-5 "evidence for"]
   [:div.block
    (for [evidence (:evidence statement)]
      (render-compact evidence))]])

(defmethod render-compact "Assertion" [assertion]
  ^{:key assertion}
  [:div.columns
   [:div.column.is-narrow
    [:a.icon
     {:href (href :resource assertion)}
     [:i.fas.fa-file]]]
   [:div.column.is-narrow
    (map render-link (:type assertion))]
   [:div.column.is-narrow
    (render-link (:subject assertion))]
   [:div.column.is-narrow
    (render-link (:object assertion))]])


