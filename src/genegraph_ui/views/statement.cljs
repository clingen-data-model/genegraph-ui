(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date type-tags]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [markdown.core :as md :refer [md->html]]
            [reitit.frontend.easy :refer [href]]))

(defn render-compact-grouped-by-type [resources]
  (for [[type resources-with-type] (group-by #(-> % :type first) resources)]
    ^{:key type}
    [:div.box
     [:h6.title.is-6 (render-link type)]
     (for [resource resources-with-type]
       ^{:key resource}
       [:div.box
        (render-compact resource {:skip [:type]})])]))

(defn statement-definition [statement]
  [:div.block
   [:div.block.mb-0 (render-list-item (:subject statement))]
   [:div.block.mb-0 (render-link (:predicate statement))]
   [:div.block.mb-0 (render-link (:object statement))]
   (for [qualifier (:qualifier statement)]
     [:div.block.mb-0 (render-link qualifier) " "])])

(defn statement-types [statement]
  [:div.level
   [:div.level-left
    (for [t (:type statement)]
      ^{:key t}
      [:level-item.tag
       (render-link t)])]])

(defn statement-provenance [statement]
  (for [contribution (:contributions statement)]
    [:div.block
     [:div.block.mb-0 (render-link (:attributed_to contribution))]
     [:div.block.mb-0 (render-link (:realizes contribution))]
     [:div.block.mb-0 (trim-iso-date (:date contribution))]]))

(defn statement-score [statement]
  (when (:score statement)
    [:h5.title.is-5 "score: " (:score statement)]))

(defpage "Statement"
  "query ($iri: String, $genetic_evidence_type: String, $experimental_evidence_type: String) {
  resource(iri: $iri) {
    ... compact
    ... list_item
    ...basicFields
    subject_of {
      ...basicFields
      ...statementFields
    }
    ... on Statement {
      ...statementFields
      contributions {
        attributed_to {
          curie
          label
        }
        date
        realizes {
          curie
          label
        }
      }
      direct_evidence: evidence { ...compact }
      genetic_evidence: evidence(transitive: true, class: $genetic_evidence_type) { ...compact }
      experimental_evidence: evidence(transitive: true, class: $experimental_evidence_type) { ...compact }
    }
    __typename
    used_as_evidence_by {
      ...statementFields
      ...basicFields
    }
  }
}

fragment basicFields on Resource {
  __typename
  label
  curie
  description
  source {
    __typename
    curie
    iri
    label
    short_citation
  }
  type {
    __typename
    label
    curie
  }
}

fragment statementFields on Statement {
  score
  calculated_score
  subject {
    ...basicFields
    ... on Statement {
      subject {
        curie
        label
      }
      predicate {
        curie
        label
      }
      object {
        curie
        label
      }
    }
  }
  predicate {
    ...basicFields
  }
  object {
    ...basicFields
  }
  qualifier {
    ...basicFields
  }
  specified_by {
    curie
    label
  }
}"
  ([statement]
   [:div
    [:div.columns
     [:div.column.is-two-fifths
      [:h3.title.is-3
       (curie-label statement)]
      (statement-types statement)
      (statement-definition statement)
      (statement-score statement)
      (statement-provenance statement)]
     [:div.column
      [:div.block.is-family-secondary
       (:description statement)]
      (for [linked-statement (:subject_of statement)]
        (render-list-item linked-statement))]]
    [:div.block
     (for [evidence (:direct_evidence statement)]
       (render-compact evidence))]
    [:h5.title.is-5 "genetic evidence"]
    [:div.block
     (render-compact-grouped-by-type (:genetic_evidence statement))]
    [:h5.title.is-5 "experimental evidence"]
    [:div.block
     (render-compact-grouped-by-type (:experimental_evidence statement))]]))

(defpartial compact "Statement"
  "{__typename
    label
    curie
    subject {curie label type {curie label}}
    predicate {curie label type {curie label}}
    object {curie label type {curie label}}
    score
    calculated_score
    evidence { ...list_item }
}"
  ([statement]
   ^{:key statement}
   [:div.columns
    [:div.column
     [:div.columns
      [:div.column.is-one-third
       (cond
         (:score statement) [:div.break
                             [:a.has-text-weight-semibold
                              {:href (href :resource statement)}
                              "score: " (:score statement)]
                             (when (:calculated_score statement)
                               (str " (default " (:calculated_score statement) ")"))]
         :else [:a.break.icon
                {:href (href :resource statement)}
                [:i.fas.fa-file]])
       [:div.break
        (map render-link (:type statement))]
       [:div.break
        (render-link (:subject statement))]
       [:div.break
        (render-link (:predicate statement))]
       [:div.break
        (render-link (:object statement))]
       (for [qualifier (:qualifier statement)]
         ^{:key qualifier}
         [:div.break
          (render-link qualifier)])]
      (when-let [description (:description statement)]
        (let [description-segments (re-seq #"(?:\S+\W+\n?){1,50}" description)]
          [:div.column.is-family-secondary
           (first description-segments)
           (when (< 1 (count description-segments))
             "...")]))]
     (for [evidence (:evidence statement)]
       ^{:key evidence}
       [:div.columns
        [:div.column (render-list-item evidence)]])]]))



(defpartial list-item "Statement"
  "{__typename
    curie
    label
    subject {__typename type {curie label} curie label}
    predicate {__typename type {curie label}  curie label}
    object {__typename type {curie label} curie label}
    qualifier {__typename type {curie label} curie label}}"
  ([resource]
   ^{:key resource}
   [:div.columns
    [:div.column.is-narrow
     [:div.break (render-link resource)]
     (type-tags resource)]
    [:div.column
     [:div.columns.is-multiline.p-2
      [:div.column.is-narrow.p-1 (render-list-item (:subject resource))]
      [:div.column.is-narrow.p-1 (render-link (:predicate resource))]
      [:div.column.is-narrow.p-1 (render-link (:object resource))]
      (for [qualifier (:qualifier resource)]
        [:div.column.is-narrow.p-1 (render-link qualifier)])]]]))
