(ns genegraph-ui.views.value-set
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date type-tags]]
            [markdown.core :as md :refer [md->html]]
            [reitit.frontend.easy :refer [href]]))


(defmethod render-full "ValueSet" [resource]
  [:section.section
   [:div.columns
    [:div.column.is-two-fifths
     [:h3.title (:label resource)]
     [:h4.subtitle (:curie resource)]]]
   [:h4.title.is-4 "members"]
   (for [member (:members resource)]
     (render-compact member))])
