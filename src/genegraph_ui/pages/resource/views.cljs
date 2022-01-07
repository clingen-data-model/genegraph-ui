(ns genegraph-ui.pages.resource.views
  (:require [genegraph-ui.protocols :refer [render-full]]
            [genegraph-ui.views.generic-resource]
            [genegraph-ui.views.statement]
            [genegraph-ui.views.proband-evidence]
            [genegraph-ui.views.variant-evidence]
            [genegraph-ui.views.bibliographic-resource]
            [genegraph-ui.views.segregation]
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
        response @(subscribe [::common-subs/query-response])
        show-query @(subscribe [::common-subs/show-query])]
    [:div
     [:button.button
      {:on-click #(dispatch [:common/toggle-show-query])}
      (if show-query "hide query" "show query")]
     (when show-query
       [:div.box
        [:h6.title.is-6 "query"]
        [:pre query]
        [:h6.title.is-6 "parameters"]
        [:pre params]
        [:h6.title.is-6 "response"]
        [:pre response]])]))

(defn loader []
  [:div.column.is-two-thirds.has-text-centered
   [:div.icon.loader.is-loading.is-size-1.mt-6
    [:i.fas.fa-circle-notch]]])

(defn resource []
  (let [resource @(subscribe [::subs/resource])
        is-loading @(subscribe [::common-subs/is-loading])
        menu-hidden @(subscribe [::common-subs/menu-hidden])]
    [:div
     (common-views/navbar)
     [:section.section
      [:div.columns
       (if is-loading
         (loader)
         [:div.column
          (render-full resource)
          (query)])]]]))
