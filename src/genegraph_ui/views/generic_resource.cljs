(ns genegraph-ui.views.generic-resource
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :as helpers :refer [type-tags]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [reitit.frontend.easy :refer [href]]))


(defn- list-value-set-membership [resource]
  (when (seq (:in_scheme resource))
    [:div.break.mt-4
     [:h6.title.is-6.mb-1 "in value set"]
     (for [value-set (:in_scheme resource)]
       (render-list-item value-set))]))

(defpage "GenericResource"
  "query ($iri: String) {
  resource(iri: $iri) {
  __typename label curie description
  in_scheme { ...list_item }
  source { curie iri label short_citation } 
  type { __typename label curie }
}
}"
  ([resource]
   [:div.columns
    [:div.column
     [:div.break
      [:h4.title.is-4 (:label resource)]
      [:h6.subtitle.is-6 (:curie resource)]
      [:p.is-family-secondary (:description resource)]]
     (list-value-set-membership resource)]]
   ))

(defpartial compact "GenericResource"
  "{__typename
    label
    curie
    source { curie iri label short_citation } 
    type { __typename label curie }
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

;; (defpartial list-item "GenericResource"
;;   "{__typename
;;     label
;;     curie}"
;;   ([resource]
;;   ^{:key resource}
;;    [:div.break.pl-2 (render-link resource)]))
