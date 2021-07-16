(ns genegraph-ui.views.evidence-line
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "EvidenceLine" [evidence-line]
  [:section.section
   [:div.box
    [:h5.title.is-5
     (:curie evidence-line)]
    [:h6.subtitle.is-6
     (map render-link (:type evidence-line))]]
   (when (:score evidence-line)
     [:h5.title.is-5 "score: " (:score evidence-line)])
   [:h5.title.is-5 "statements about"]
   [:div.block
    (for [referee (:subject_of evidence-line)]
      (render-compact referee))]
   [:h5.title.is-5 "evidence"]
   [:div.block
    (for [evidence (:evidence evidence-line)]
      (render-compact evidence))]])

(defmethod render-compact "EvidenceLine" [evidence-line]
  ^{:key evidence-line}
  [:div.columns
   [:div.column.is-narrow
    [:a.icon
     {:href (href :resource evidence-line)}
     [:i.fas.fa-file]]]
   [:div.column.is-narrow
    (map render-link (:type evidence-line))]])
