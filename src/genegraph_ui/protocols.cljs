(ns genegraph-ui.protocols)

(defmulti render-full
  "Return a hiccup representation of the resource suitable for presentation
  as the main element on a page."
  :__typename)

(defmulti render-compact
  "Return a hiccup representation of the resource suitable for presentation
  as a component of a larger page, or list of similar entities."
  :__typename)

(defmulti render-link
  "Return a hiccup representation of a link to the resource,
  with a label appropriate for the resource type."
  :__typename)

(defmulti render-list-item
  "return a definitional view of the item, generally intended
  for a list of similar items"
  :__typename)

