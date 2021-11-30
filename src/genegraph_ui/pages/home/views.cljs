(ns genegraph-ui.pages.home.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.home.subs :as subs]
            [genegraph-ui.common.subs :as common-subs]
            [genegraph-ui.pages.home.events]
            [reitit.frontend.easy :as rfe]))

(defn query []
  (let [query @(subscribe [::common-subs/current-query])
        params @(subscribe [::common-subs/current-params])
        response @(subscribe [::common-subs/query-response])]
    [:div
     [:pre query]
     [:pre params]
     [:pre response]]))


(defn home []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-one-third
     (common-views/side-panel)]
    [:div.column.is-two-thirds
     (query)]]])


