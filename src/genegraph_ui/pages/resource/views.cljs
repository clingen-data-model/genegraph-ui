(ns genegraph-ui.pages.resource.views
  (:require [genegraph-ui.protocols :refer [render-full]]
            [genegraph-ui.views.generic-resource]
            [genegraph-ui.views.statement]
            [genegraph-ui.views.evidence-line]
            [genegraph-ui.views.proband-evidence]
            [genegraph-ui.common.events :as common-events]
            [genegraph-ui.common.subs :as common-subs]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.pages.resource.subs :as subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [clojure.string :as s]
            [reitit.frontend.easy :as rfe]))

(defn query []
  (let [query @(subscribe [::common-subs/current-query])
        params @(subscribe [::common-subs/current-params])
        response @(subscribe [::common-subs/query-response])]
    [:div
     [:pre query]
     [:pre params]
     [:pre response]]))

(defn resource []
  (let [resource @(subscribe [::subs/resource])]
    [:section.section
     [:div.columns
      [:div.column.is-one-third
       (common-views/side-panel)]
      [:div.column.is-two-thirds
       (render-full resource)
       (query)
       ]]]))
