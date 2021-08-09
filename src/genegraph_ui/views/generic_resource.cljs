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
   (for [statements-by-type-map-entry (group-by #(-> % :type first) (:subject_of resource))]
     ^{:key (key statements-by-type-map-entry)}
     [:div
      [:h6.title.is-6 (render-link (key statements-by-type-map-entry))]
      (for [statement (val statements-by-type-map-entry)]
        (render-compact statement {:skip [:type] :source (:curie resource)}))])])

(defmethod render-compact "GenericResource" [resource]
  ^{:key resource}
  [:p.block (render-link resource)])

