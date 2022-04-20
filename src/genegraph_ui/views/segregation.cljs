(ns genegraph-ui.views.segregation
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [reitit.frontend.easy :refer [href]]))


(defpage "Segregation"
  "query ($iri: String) {
  resource(iri: $iri) {
...compact
}
}"
  ([evidence]
   ^{:key evidence}
   [:section.section
    (render-compact evidence)]))

(defpartial compact "Segregation"
  "{__typename
    label
    curie
    published_lod_score
    estimated_lod_score
    meets_inclusion_criteria
    source { curie iri label short_citation } 
    sequencing_method { label curie }
    phenotype_positive_allele_positive_count 
    phenotype_negative_allele_negative_count
    description
}"
  ([evidence]
   ^{:key evidence}
   [:div.columns
    [:div.column.is-one-third (render-link evidence)
     [:div.break  "LOD score: "
      (or (:published_lod_score evidence) (:estimated_lod_score evidence))
      [:span.is-size-7.has-text-grey 
       (if (:published_lod_score evidence)
         " (published)"
         " (estimated)")]]
     [:div.break.is-size-7
      (if (:meets_inclusion_criteria evidence)
        [:p.has-text-success "included"]
        [:p.has-text-grey "not included"])]
     (when-let [source (:source evidence)]
       [:div.break [:a.is-size-7
                    {:href (:iri source)
                     :title (:label source)}
                    (:short_citation source)]])]
    [:div.column.is-two-thirds
     [:div.break (render-link (:sequencing_method evidence))]
     [:div.columns
      [:div.column "affected: " (:phenotype_positive_allele_positive_count evidence)]
      [:div.column "unaffected: " (:phenotype_negative_allele_negative_count evidence)]]
     [:p.break.is-family-secondary (:description evidence)]]]))

(defpartial list-item "Segregation"
  "{__typename
    label
    curie
    published_lod_score
    estimated_lod_score
    meets_inclusion_criteria
    source { curie iri label short_citation } 
    sequencing_method { label curie }
    phenotype_positive_allele_positive_count 
    phenotype_negative_allele_negative_count
    description
}"
  ([evidence]
   ^{:key evidence}
   [:div.columns
    [:div.column.is-one-third (render-link evidence)
     [:div.break  "LOD score: "
      (or (:published_lod_score evidence) (:estimated_lod_score evidence))
      [:span.is-size-7.has-text-grey 
       (if (:published_lod_score evidence)
         " (published)"
         " (estimated)")]]
     [:div.break.is-size-7
      (if (:meets_inclusion_criteria evidence)
        [:p.has-text-success "included"]
        [:p.has-text-grey "not included"])]
     (when-let [source (:source evidence)]
       [:div.break [:a.is-size-7
                    {:href (:iri source)
                     :title (:label source)}
                    (:short_citation source)]])]
    [:div.column.is-two-thirds
     [:div.break (render-link (:sequencing_method evidence))]
     [:div.columns
      [:div.column "affected: " (:phenotype_positive_allele_positive_count evidence)]
      [:div.column "unaffected: " (:phenotype_negative_allele_negative_count evidence)]]
     [:p.break.is-family-secondary (:description evidence)]]]))
