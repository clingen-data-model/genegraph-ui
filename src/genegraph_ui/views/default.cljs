(ns genegraph-ui.views.default
  (:require [genegraph-ui.protocols :as p :refer [render-full render-compact render-link render-list-item]]
            [reitit.frontend.easy :refer [href]]
            [genegraph-ui.common.helpers :as helpers :refer [type-tags]]
            [genegraph-ui.common.helpers :refer [curie-label]]))

(defmethod render-full :default
  [resource]
  [:section.section
   [:p "No renderer for resource"]
   [:pre resource]])

(defmethod render-compact :default [resource]
  ^{:key resource}
  [:div.columns
   [:div.column.is-two-fifths
    [:div.break (render-link resource)]
    (type-tags resource)
    (when-let [source (:source resource)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   (when-let [description (:description resource)]
     [:div.column description])])

(defmethod render-link :default
  [resource]
  ^{:key resource}
  [:a
   {:href (href :resource resource)}
   (or (:label resource)
       (curie-label resource))])


(defmethod render-list-item :default [resource]
  [:div.columns.is-multiline.is-narrow
   [:div.column (render-link resource)]])
