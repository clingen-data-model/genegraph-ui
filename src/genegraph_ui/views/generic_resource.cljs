(ns genegraph-ui.views.generic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :as helpers :refer [type-tags]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [reitit.frontend.easy :refer [href]]))


(defpage "GenericResource"
  "query ($iri: String) {
  resource(iri: $iri) {
...compact
}
}"
  ([resource]
   (render-compact resource)))

(defpartial compact "GenericResource"
  "{__typename
    label
    curie
    source { curie iri label short_citation } 
    description}"
  ([resource]
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
      (render-list-item statement))]]))

(defpartial list-item "GenericResource"
  "{__typename
    label
    curie
    source { curie iri label short_citation } 
    description}"
  ([resource]
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
      (render-list-item statement))]]))

;; (defmethod render-compact "GenericResource" [resource]
;;   ^{:key resource}
;;   [:div.columns
;;    [:div.column.is-two-fifths
;;     [:div.break (render-link resource) " (" (:curie resource) ")"]
;;     (type-tags resource)
;;     (when-let [source (:source resource)]
;;       [:div.break [:a.is-size-7
;;                    {:href (:iri source)
;;                     :title (:label source)}
;;                    (:short_citation source)]])]
;;    [:div.column
;;     [:p.block.is-family-secondary (:description resource)]
;;     (for [statement (:subject_of resource)]
;;       (render-list-item statement))]])
