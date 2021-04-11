(ns genegraph-ui.pages.home.events
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [cuerdas.core :as s]
            ;[cheshire.core :as json]
            ))

(def suggest-query "
query ($text: String, $suggester: Suggester) {
  suggest(suggest: $suggester, text: $text) {
    iri
    curie
    text
  }
}")

(re-frame/reg-event-db
  :home/set-search-topic
  (fn [db [_ topic]]
    (assoc db :home/search-topic topic)))

(re-frame/reg-event-fx
  :home/request-suggestions
  (fn [{:keys [db]} [_ search-text]]
    (.log js/console (str "request suggestion for " search-text))
    (let [topic (:home/search-topic db :gene)]
      (case topic
        :gene {:dispatch [::re-graph/query
                          suggest-query
                          {:text search-text
                           :suggester "GENE"}
                          [:home/recieve-suggestions]]}
        (.log js/console "Not requesting suggestions for topic: " topic)))))

(re-frame/reg-event-db
  :home/recieve-suggestions
  (fn [db [_ {:keys [data errors] :as payload}]]
    (.log js/console (str data))
    (.log js/console (str errors))
    (assoc db
      :home/suggested-genes (:suggest data)
      :errors (:message errors))))


;(def variant-query-str "
;query ($subject: String) {
;  clinical_assertions(subject: $subject) {
;    iri
;    id
;    subject {name}
;    predicate
;    version
;    review_status
;    date_updated
;    release_date
;    collection_methods
;    allele_origins
;    contribution {
;      activity_date
;      agent
;      agent_role
;    }}}")
(def variant-query-str "
query ($subject: String, $timeframe: String) {
  aggregate_assertions(subject: $subject, timeframe: $timeframe) {
    iri
    id
    subject {name}
    predicate
    object
    review_status
    release_date
    members {
      id
      subject {name}
      predicate
      object
      review_status
      allele_origins
      collection_methods
      version
      contribution {
        activity_date
        agent
        agent_role
      }
    }
  }
}")

(re-frame/reg-event-fx
  :home/request-search
  (fn [cofx [_ topic search-text]]
    (let [db (:db cofx)]
      (.log js/console (s/format "cofx: %s, topic: %s, search-text: %s"
                                 cofx topic search-text))
      (case topic
        :variation (let [fx {:dispatch [::re-graph/query
                                        variant-query-str
                                        {:subject search-text}
                                        [:home/receive-search-results]
                                        ]}]
                     (.log js/console fx)
                     fx)
        (assoc db :errors (str "Unknown search topic: " topic))
        )
      )))

(re-frame/reg-event-db
  :home/receive-search-results
  (fn [db [_ payload]]
    (let [{data :data
           errors :errors} payload]
      (.log js/console (s/format "data: %s" (str data)))
      (assoc db :home/search-results data
                :errors (:message errors))
      )))
