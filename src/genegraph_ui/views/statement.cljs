(ns genegraph-ui.views.statement
  (:require [genegraph-ui.protocols
             :refer
             [render-full render-compact render-link render-list-item]]
            [genegraph-ui.common.helpers :refer [curie-label trim-iso-date type-tags]]
            [genegraph-ui.query :as query :refer [defpartial defpage]]
            [genegraph-ui.pages.resource.subs :as subs]
            [re-frame.core :refer [subscribe dispatch]]
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

#_(defpage "Statement"
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

(defn render-spo [statement]
  (when (:subject statement)
    [:div.block
     (render-list-item (:subject statement))
     (render-list-item (:predicate statement))
     (render-list-item (:object statement))]))

(defn render-contributions [statement]
  (let [contributions (:contributions statement)]
    (when (seq contributions)
      [:div.block
       [:h6.title.is-6 "contributions"]
       (for [contribution (:contributions statement)]
         [:div.columns
          [:div.column.is-narrow (some->  contribution :date (subs 0 10) )]
          [:div.column.is-narrow (render-link (:attributed_to contribution))]
          [:div.column (render-link (:realizes contribution))]])
       [:hr]])))

(defn render-subject-of [statement]
  (let [resources (:subject_of statement)]
    (when (seq resources)
      [:div.mb-6
       [:h6.title.is-6 "subject of"]
       (for [resource resources]
         (render-list-item resource))])))

(defn render-evidence [statement]
  (let [evidence-list (:evidence statement)]
    (when (seq evidence-list)
      [:div.mb-6
       [:h6.title.is-6 "evidence"]
       (doall
        (for [evidence evidence-list]
          (render-list-item evidence)))])))

(defn render-description [statement]
  (when (:description statement)
    [:div
     [:h6.title.is-6 "description"]
     [:div.columns
      [:div.column.is-family-secondary
       (:description statement)]]
     [:hr]]))

(defpage "Statement"
  "
query ($iri: String) {
  resource(iri: $iri) {
    __typename
    label
    curie
    description
    type {
      __typename
      curie
      label
    }
    subject_of {
       ...list_item
    }
    ... on Statement {
      subject {
        ...list_item
      }
      predicate {
        ...list_item
      }
      object {
        ...list_item
      }
      score
      calculated_score
      evidence {
        ...compact
      }
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
    }
  }
}
"
  ([statement]
   ^{:key statement}
   [:div
    [:div.p-4.mb-6.subject
     [:h3.title.is-3 (:curie statement)]
     [:h5.subtitle.is-5 (type-tags statement)]
     (when (:subject statement)
       [:hr]
       (render-spo statement))]
    (render-description statement)
    (render-contributions statement)
    (render-subject-of statement)
    (render-evidence statement)]))

(defpartial compact "Statement"
  "{__typename
    type {__typename curie label}
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
   [:div.columns.pb-4
    [:div.column
     [:div.columns
      [:div.column.is-narrow.is-size-5.has-text-weight-bold
       (render-link statement)]
      [:div.column.is-narrow (type-tags statement)]
      [:div.column.is-narrow (render-spo statement)]
      (when-let [description (:description statement)]
        (let [description-segments (re-seq #"(?:\S+\W+\n?){1,50}" description)]
          [:div.column.is-family-secondary
           (first description-segments)
           (when (< 1 (count description-segments))
             "...")]))]
     (for [evidence (:evidence statement)]
       ^{:key evidence}
       [:div.columns
        [:div.column.py-1 (render-list-item evidence)]])]]))



(defpartial list-item "Statement"
  "{__typename
    curie
    label
    type {__typename curie label}
    score
    subject {__typename type {curie label} curie label}
    predicate {__typename type {curie label}  curie label}
    object {__typename type {curie label} curie label}
    qualifier {__typename type {curie label} curie label}}"
  ([resource]
   (let [state @(subscribe [::subs/element-state (:curie resource)])]
     ^{:key resource}
     [:div.columns
      [:div.column.is-narrow.py-1
       [:span.icon
        {:on-click #(dispatch [:resource/expand-detail (:curie resource)])}
        [:ion-icon {:name "add-circle-outline"}]]]
      [:div.column.is-narrow.py-1 (render-link resource)]
      [:div.column.is-narrow.py-1 (type-tags resource)]
      (when (:subject resource)
        [:div.column.py-1
         [:div.columns.is-multiline.p-2
          [:div.column.is-narrow.py-1 (render-link (:subject resource))]
          [:div.column.is-narrow.py-1 (render-link (:predicate resource))]
          [:div.column.is-narrow.py-1 (render-link (:object resource))]
          (for [qualifier (:qualifier resource)]
            [:div.column.is-narrow.py-1 (render-link qualifier)])]])])))

(defpartial link "Statement"
  ([statement]
   ^{:key statement}
   [:a
    {:href (href :resource statement)
     :on-click #(dispatch [:common/navigate-to-resource
                           (select-keys statement
                                        [:curie :type :__typename])])}
    (cond (:label statement) (:label statement)
          (:score statement) (str "score: " (:score statement))
          
          :else [:span.icon [:ion-icon {:name "diamond-outline"}]])]))
