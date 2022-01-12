(ns genegraph-ui.common.helpers
  (:require [genegraph-ui.protocols :as p :refer [render-link]]))

(defn curie-label [resource]
  (if (string? (:curie resource))
    (re-find #"\w+:\w{8}" (:curie resource))
    ""))

(defn trim-iso-date [date]
  (if (string? date)
    (re-find #"\d{4}-\d{2}-\d{2}" date)))

(defn type-tags [resource]
  [:div.columns.is-multiline
   (for [t (:type resource)]
      ^{:key t}
      [:div.column.is-narrow.px-1
       [:span.tag (render-link t)]])])
