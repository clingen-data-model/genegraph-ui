(ns genegraph-ui.pages.documentation.views
  (:require [genegraph-ui.common.views :as common-views]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [clojure.string :as s]
            [reitit.frontend.easy :as rfe]))

(defn side-menu []
     [:aside.menu
      [:p.menu-label "get started"]
      [:ul.menu-list
       [:li [:a {:href (rfe/href :documentation)}"introduction"]]
       [:li [:a {:href (rfe/href :sepio-overview)}"SEPIO overview"]]
       [:li [:a {:href (rfe/href :graphql-mapping)}"GraphQL mapping"]]
       [:li [:a {:href (rfe/href :examples)}"query examples"]]]
      [:p.menu-label "concepts"]
      [:ul.menu-list
       [:li [:a
             {:href "https://github.com/monarch-initiative/SEPIO-ontology"}
             "SEPIO" [:span.icon [:i.fas.fa-link]]]]
       [:li [:a
             {:href "https://graphql.org/learn/"}
             "GraphQL" [:span.icon [:i.fas.fa-link]]]]]
      [:p.menu-label "clingen sepio profiles"]
      [:ul.menu-list
       [:li [:a {:href (rfe/href :gene-validity)}"gene validity"]]]
      [:p.menu-label "explore"]
      [:ul.menu-list
       [:li [:a {:href (s/replace genegraph-ui.BACKEND_HTTP "api" "ide")  } "Genegraph GraphiQL" [:span.icon [:i.fas.fa-link]]]]]])

(defn home []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-narrow
     (side-menu)]
    [:div.column.is-two-thirds
     [:h2.title.is-2 "Introduction"]
     [:p "Genegraph provides access to ClinGen data modeled in "
      [:a "SEPIO"]
      " presented using a GraphQL API. There are a few different paths to understanding the data and how to find what you're looking for; this site offers the ability to view concrete examples for queries, as well as explore the data directly, both through an example user interface as well as through the GraphiQL IDE. "]]]])



(defn sepio-overview []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-narrow
     (side-menu)]
    [:div.column.is-two-thirds
     [:h2.title.is-2 "SEPIO Overview"]
     [:p "This page includes selected examples of entities in SEPIO, with specific examples on how they relate to the SEPIO profiles used within ClinGen."]
     [:h4.title.is-4 "Proposition"]
     [:p "A proposition is analogous to a scientific hypothesis, for example: \"Variants within Gene X are causative of disease Y, given mode of inheritance Z\". A proposition will usually sit near the top of any given SEPIO-based representation of a curation. Distinct from other kinds of statements in SEPIO, a proposition has no provenance associated with it (i.e. no date or attribution), as it is intended to be the subject of statements made by individuals that present the evidence in support or against the proposition."]
     [:h6.title.is-6 "examples"]
     [:p [:a {:href (rfe/href :resource {:curie "CGGV:a414c8fb-8c9d-4707-b2b8-f08c21b8cda6"})} "CALM3 contains causal mutations for Long QT Syndrome with Autosomal Dominant Inheritance"]]]]])

(defn graphql-mapping []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-narrow
     (side-menu)]
    [:div.column.is-two-thirds
     [:h2.title.is-2 "GraphQL mapping"]
     [:div.is-family-secondary
      [:p.block "The data model is based on SEPIO, an OWL ontology based on the Resource Description Framework (RDF). While it is not required to use RDF-based technologies to take advantage of SEPIO, there are advantages in doing so. As such, the backend database of Genegraph uses an RDF Triplestore (Apache Jena)"]
      [:p.block "We present a GraphQL API for the front-end service of Genegraph. This offers substantially more flexibility than a traditional REST API, while using a technology more familiar to developers than those based on the RDF stack (such as a SPARQL endpoint). This means that a user can declare exactly how much data one would like to retrieve at a time. A full description of GraphQL is out of scope for this documentation (GraphQL has excellent documentation of its own)."]
      [:p.block "When creating a GraphQL schema for Genegraph, we look to reflect the structure and semantics of the SEPIO model, while allowing "]]]]])

(defn examples []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-narrow
     (side-menu)]
    [:div.column.is-two-thirds
     [:h2.title.is-2 "examples"]
     [:p "The objects within SEPIO do not map 1-1 with types in SEPIO. Within SEPIO, there is a diversity of types used liberally to suggest the purpose of an entity, whereas type differences in GraphQL are chosen to reflect the structure of data returned by a resolver. Therefore types with similar structure, such as Propositions and Assertions, are returned as the GraphQL Statement type. For example: "]
     [:pre "query ($iri: String) {
  resource(iri: $iri) {
    __typename
    type {
      curie
      label
    }
  }
}"]
     [:pre "{
  \"iri\": \"CGGV:a414c8fb-8c9d-4707-b2b8-f08c21b8cda6\"
}"]
     [:p "returns"]
     [:pre "{
  \"data\": {
    \"resource\": {
      \"typename\": \"Statement\",
      \"type\": [
        {
          \"curie\": \"SEPIO:0004001\",
          \"label\": \"gene validity proposition\"
        }
      ]
    }
  }
}"]]]])

(defn gene-validity []
  [:section.section
   (common-views/navbar)
   [:div.columns
    [:div.column.is-narrow
     (side-menu)]
    [:div.column.is-two-thirds
     [:h2.title.is-2 "Gene Validity SEPIO profile"]]]])
