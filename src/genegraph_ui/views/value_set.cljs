(ns genegraph-ui.views.value-set
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date type-tags]]
            [genegraph-ui.query :refer [defpage]]
            [markdown.core :as md :refer [md->html]]
            [reitit.frontend.easy :refer [href]]))


(defn- list-members [resource]
  (when (seq (:members resource))
    [:div.break.mt-4
     [:h6.title.is-6.mb-1 "members"]
     (for [member (:members resource)]
       (render-list-item member))]))

(defpage "ValueSet"
  "query ($iri: String) {
resource(iri: $iri) {
   __typename
   label
   curie
   description
   ...on ValueSet {
   members { ...list_item }
   }
}}"
  ([resource]
   [:div.columns
    [:div.column
     [:div.break
      [:h4.title.is-4 (:label resource)]
      [:h6.subtitle.is-6 (:curie resource)]
      [:p.is-family-secondary (:description resource)]]
     (list-members resource)]]))
