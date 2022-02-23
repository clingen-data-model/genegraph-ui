(ns genegraph-ui.pages.find.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.common.subs :as common-subs]
            [genegraph-ui.protocols :as p :refer [render-compact]]
            [reitit.frontend.easy :as rfe]))


(defn loader []
  [:div.column.is-two-thirds.has-text-centered
   [:div.icon.loader.is-loading.is-size-1.mt-6
    [:i.fas.fa-circle-notch]]])

(defn find-page []
  (let [is-loading @(subscribe [::common-subs/is-loading])
        results @(subscribe [::common-subs/search-result])]
    [:div
     (common-views/navbar)
     (if is-loading
       (loader)
       [:section.section
        (for [i (:results results)]
          ^{:key i}
          (render-compact i))])]))


