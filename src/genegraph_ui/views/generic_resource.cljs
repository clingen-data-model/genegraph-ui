(ns genegraph-ui.views.generic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "GenericResource" [resource]
  [:section.section
   [:h3.title.is-3 (or (:label resource)
                       (:curie resource))]
   [:h6.subtitle.is-6
    [:div.level
      [:div.level-right
       (for [t (:type resource)]
         ^{:key t}
         [:div.level-item
          (render-link t)])]]]
   [:p.block (:description resource)]
   [:h5.title.is-5 "Statements"]
   (for [assertion (:subject_of resource)]
     (render-compact assertion))])

(defmethod render-compact "GenericResource" [resource]
  ^{:key resource}
  [:p.block (render-link resource)])

