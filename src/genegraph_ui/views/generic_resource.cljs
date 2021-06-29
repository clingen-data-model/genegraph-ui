(ns genegraph-ui.views.generic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "GenericResource" [resource]
  [:section.section
   [:h3.title (:label resource)]
   [:h5.title.is-5 "Statements"]
   (for [assertion (:subject_of resource)]
     (render-compact assertion))])

(defmethod render-compact "GenericResource" [resource]
  [:p.block (render-link resource)])

