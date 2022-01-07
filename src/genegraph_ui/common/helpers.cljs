(ns genegraph-ui.common.helpers)

(defn curie-label [resource]
  (if (string? (:curie resource))
    (re-find #"\w+:\w{8}" (:curie resource))
    ""))

(defn trim-iso-date [date]
  (if (string? date)
    (re-find #"\d{4}-\d{2}-\d{2}" date)))
