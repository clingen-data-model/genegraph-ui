(ns genegraph-ui.views.agent
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date type-tags]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [markdown.core :as md :refer [md->html]]
            [reitit.frontend.easy :refer [href]]))


(def agent-fragment
  "")

(defmethod render-full "Agent" [resource]
  [:section.section
   [:div.columns
    [:div.column.is-two-fifths
     [:h3.title (:label resource)]]
    [:div.column
     (for [contribution (:contributions resource)]
       (render-list-item (:artifact contribution)))]]])

#_(defpartial list-item "Agent" [agent]
  "{__typename
    curie
    label
    "
  [])
