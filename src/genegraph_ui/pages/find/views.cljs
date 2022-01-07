(ns genegraph-ui.pages.find.views
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [genegraph-ui.common.views :as common-views]
            [genegraph-ui.common.subs :as common-subs]
            [genegraph-ui.protocols :as p :refer [render-compact]]
            [reitit.frontend.easy :as rfe]))



(defn find-page []
  (let [results @(subscribe [::common-subs/search-result])]
    [:div
     (common-views/navbar)
     [:section.section
      [:div.columns
       [:div.column
        (for [i (:results results)]
          ^{:key i}
          (render-compact i))]]]]))


