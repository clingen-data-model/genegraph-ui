(ns genegraph-ui.view)

(defn resolve-type [resource]
  (:__typename resource))

(defmulti full [resource] resolve-type)
