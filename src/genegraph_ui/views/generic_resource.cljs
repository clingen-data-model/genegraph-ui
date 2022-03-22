(ns genegraph-ui.views.generic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :as helpers :refer [type-tags]]
            [reitit.frontend.easy :refer [href]]))

(defmethod render-full "GenericResource" [resource]
  [:div
   [:div.columns
    [:div.column.is-two-fifths
     [:h3.title.is-3 (or (:label resource)
                         (:curie resource))]
     [:h5.subtitle.is-5 (:curie resource)]
     (type-tags resource)
     [:p.block (:description resource)]]
    [:div.column
     (for [statement (:subject_of resource)]
       ^{:key (key statement)}
       (render-list-item statement))]]
   (for [value-set (:in_scheme resource)]
     [:div.columns
      [:div.column.is-two-fifths
       [:h5.title.is-5 "Value Set"]]
      [:div.column
       (render-compact value-set)]])])

(defmethod render-compact "GenericResource" [resource]
  ^{:key resource}
  [:div.columns
   [:div.column.is-two-fifths
    [:div.break (render-link resource) " (" (:curie resource) ")"]
    (type-tags resource)
    (when-let [source (:source resource)]
      [:div.break [:a.is-size-7
                   {:href (:iri source)
                    :title (:label source)}
                   (:short_citation source)]])]
   [:div.column
    [:p.block.is-family-secondary (:description resource)]
    (for [statement (:subject_of resource)]
      (render-list-item statement))]])

(defmethod render-list-item "GenericResource" [resource]
  [:div.columns.is-multiline.is-narrow
   [:div.column (render-link resource)]])
