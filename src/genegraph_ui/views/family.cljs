(ns genegraph-ui.views.family
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [reitit.frontend.easy :refer [href]]))


(defmethod render-full "Family" [evidence]
  ^{:key evidence}
  [:section.section
   [:h5.title.is-5 (:label evidence)]
   [:div.block.box
    [:h6.title.is-6 "segregation"]
    (when-let [segregation (:segregation evidence)]
      (render-compact segregation))]
   [:div.block.box
    [:h6.title.is-6 "individuals"]
    (for [proband (:probands evidence)]
      (render-compact proband))]])
