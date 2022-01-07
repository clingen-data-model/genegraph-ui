(ns genegraph-ui.protocols
  (:require [reitit.frontend.easy :refer [href]]
            [genegraph-ui.common.helpers :refer [curie-label]]))

(defmulti render-full
  "Return a hiccup representation of the resource suitable for presentation
  as the main element on a page."
  :__typename)

(defmethod render-full :default
  [resource]
  [:section.section
   [:p "No renderer for resource"]
   [:pre resource]])

(defmulti render-compact
  "Return a hiccup representation of the resource suitable for presentation
  as a component of a larger page, or list of similar entities."
  :__typename)

(defmulti render-link
  "Return a hiccup representation of a link to the resource,
  with a label appropriate for the resource type."
  :__typename)

(defmethod render-compact :default
  [resource]
  [:div.columns
   [:div.column.is-one-third
    [:h5.title.is-5 (render-link resource)]]
   [:div.column
    [:p (:description resource)]]])

(defmethod render-link :default
  [resource]
  ^{:key resource}
  [:a
   {:href (href :resource resource)}
   (or (:label resource)
       (curie-label resource))])
