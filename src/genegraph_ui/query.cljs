(ns genegraph-ui.query
  (:require [clojure.string :as s])
  (:require-macros [genegraph-ui.query]))

(defonce fragments (atom {}))

(defn- scopes []
  (set (map :scope (vals @fragments))))

(defn add-fragment [query-fragment]
  (swap! fragments assoc (select-keys query-fragment [:scope :type]) query-fragment))

(defn- normalize-name-for-gql [fragment-name]
  (s/replace fragment-name #"-" "_"))

(defn- normalize-name-for-clj [fragment-name]
  (s/replace fragment-name #"_" "-"))

(defn- fragment-name [fragment-map]
  (normalize-name-for-gql
   (str (name (:scope fragment-map))
        (:type fragment-map))))

(defn- fragment-map->str [fragment-map]
  (str "\nfragment "
       (fragment-name fragment-map)
       " on "
       (:type fragment-map)
       " "
       (:fragment fragment-map)
       "\n"))

(defn- construct-fragment-for-scope [scope]
  (let [fragments-within-scope (filter #(= scope (:scope %)) (vals @fragments))]
    (str "\nfragment "
         (normalize-name-for-gql (name scope))
         " on Resource {\n"
         (s/join (map #(str "... on "
                            (:type %)
                            " { ..."
                            (fragment-name %)
                            " }\n")
                      fragments-within-scope))
         "}\n")))

(defn- construct-fragments-for-scopes [scopes-in-query]
  (s/join (map construct-fragment-for-scope scopes-in-query)))

(defn- construct-fragments-for-query [scopes-in-query]
  (let [fragments-needed-for-query (filter #(contains? scopes-in-query (:scope %))
                                           (vals @fragments))]
    (s/join (map fragment-map->str fragments-needed-for-query))))

(defn- resolve-resource-type [resource]
  (js/console.log (str "resolve-resource-type " resource))
  (:__typename resource))

(defmulti query-for-resource
  "Return the graphql query needed to represent a single instance of a given resource."
  resolve-resource-type)

(def default-query
    "query ($iri: String, $genetic_evidence_type: String, $experimental_evidence_type: String) {
  resource(iri: $iri) {
    ... compact
    ... list_item
    in_scheme {
      ...basicFields
    }
    ...basicFields
    ... on ProbandEvidence {
      ...probandFields
    }
    ... on VariantEvidence {
      ...variantFields
    }
    ... on Segregation {
      ...segregationFields
    }
    ... on ValueSet {
      members {
        ...basicFields
      }
    }
    ... on Family {
      segregation {
        ... basicFields
        ... segregationFields
      }
      probands {
        ... basicFields
        ... probandFields 
      }
    }
    ... on Agent {
      contributions {
        artifact {
          __typename
          label
          curie
          ... on Statement {
            subject {
              __typename
              curie
              label
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
          }
        }
        date
        realizes {
          curie
          label
        }
      }
    }
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
      direct_evidence: evidence {
	...basicFields
	... on Statement {
	  score
	  calculated_score
	}
	... on ProbandEvidence {
	  ...probandFields
	}
	... on VariantEvidence {
	  ...variantFields
	}
	... on Segregation {
	  ...segregationFields
	}
      }
      genetic_evidence: evidence(transitive: true, class: $genetic_evidence_type) {
	...basicFields
	... on Statement {
	  score
	  calculated_score
	  evidence {
            ...basicFields
            ... on ProbandEvidence {
              ...probandFields
            }
            ... on Segregation {
              ...segregationFields
            }
            ... on VariantEvidence {
              ...variantFields
            }
	  }
	}
	... on ProbandEvidence {
	  ...probandFields
	}
	... on VariantEvidence {
	  ...variantFields
	}
	... on Segregation {
	  ...segregationFields
	}
      }
      experimental_evidence: evidence(transitive: true, class: $experimental_evidence_type) {
	...basicFields
	... on Statement {
	  score
	  calculated_score
	  evidence {
            ...basicFields
            ... on ProbandEvidence {
              ...probandFields
            }
	  }
	}
      }
    }
    __typename
    used_as_evidence_by {
      ...statementFields
      ...basicFields
    }
  }
}

fragment probandFields on ProbandEvidence {
  variants {
    curie
    label
    canonical_reference {
      curie
    }
  }
  variant_evidence {
    ...basicFields
    ...variantFields
  }
}

fragment variantFields on VariantEvidence {
  variant {
    curie
    label
    canonical_reference {
      curie
    }
  }
  zygosity {
    curie
    label
  }
  proband {
    curie
    label
  }
}

fragment segregationFields on Segregation {
  conditions {
    curie
    label
  }
  estimated_lod_score
  published_lod_score
  meets_inclusion_criteria
  phenotype_negative_allele_negative_count
  phenotype_positive_allele_positive_count
  sequencing_method {
    curie
    label
  }
  family {
    __typename
    curie
    label
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
}")

(defmethod query-for-resource :default [resource]
  default-query)

(defn identify-scopes-in-query
  "GraphQL will return an error if a fragment is declared but not used.
  This function identifies the scoped fragments used in a given query"
  [query]
  (let [scope-set (scopes)]
    (->> (re-seq #"\.\.\.\s*(\w+)" query)
         (map #(-> % second normalize-name-for-clj keyword))
         (filter #(contains? scope-set %))
         set)))

;; TODO Strip unused fragments, as these create an error in the GraphQL request
;; Strategy for now, make sure all fragments are always used
(defn uberquery
  "Append the query specific for a given resource to the fragments needed
  to render the various elements of the resource."
  [resource]
  (let [original-query-str (query-for-resource resource)]
    (loop [query-str original-query-str
           scopes-in-query #{}]
      (let [new-scopes-in-query (identify-scopes-in-query query-str)
            new-query-str (str
                           original-query-str
                           (construct-fragments-for-scopes new-scopes-in-query)
                           (construct-fragments-for-query new-scopes-in-query))]
        (js/console.log "in uberquery")
        ;; (js/console.log query-str)
        ;; (js/console.log new-query-str)
        ;; (js/console.log (str scopes-in-query))
        (if (= new-scopes-in-query scopes-in-query)
          new-query-str
          (recur new-query-str new-scopes-in-query))))))
