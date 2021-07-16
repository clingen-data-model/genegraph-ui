(ns genegraph-ui.common.events
  (:require ["firebase/app" :as firebase]
            [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]))

(def current-user-query
"{
  current_user {
    label
    is_admin
  }
}")

(re-frame/reg-event-fx
 :common/authenticate
 (fn [_ _]
   (-> (firebase/auth) 
       (.signInWithRedirect (firebase/auth.GoogleAuthProvider.)))))

(re-frame/reg-fx
 :common/firebase-sign-out
 (fn [_]
   (-> (firebase/auth) 
       (.signOut))))

(re-frame/reg-event-fx
 :common/sign-out
 (fn [{:keys [db]} _]
   {:fx [[:common/firebase-sign-out]]
    :db (assoc db
               :user-authorization nil
               :admin-user nil)}))

(re-frame/reg-event-db
 :common/recieve-user-query
 (fn [db [_ {:keys [data errors]}]]
   ;; (cljs.pprint/pprint errors)
   (if (:current_user data)
     (assoc db
            :user-authorization :authorized
            :admin-user (get-in data [:current_user :is_admin]))
     (assoc db :user-authorization :unauthorized))))

(re-frame/reg-event-fx
 :common/recieve-id-token
 (fn [cofx [_ token]]
   {:fx [[:dispatch 
          [::re-graph/re-init
           {:ws {:connection-init-payload {:token token}}
            :http {:impl {:headers {"Authorization"
                                    (str "Bearer "
                                         (:token token))}}}}]]
         [:dispatch-later
          {:ms 200
           :dispatch [::re-graph/query
                      current-user-query
                      {}
                      [:common/recieve-user-query]]}]]}))

(re-frame/reg-fx
 :common/retrieve-id-token
 (fn [_]
   (when-let [user (-> (firebase/auth) .-currentUser)]
     (-> user
         .getIdToken
         (.then #(re-frame/dispatch [:common/recieve-id-token %]))))))

(re-frame/reg-event-fx
 :common/auth-state-change
 (fn [cofx _]
   (if-let [user (-> (firebase/auth) .-currentUser)]
     {:db (assoc (:db cofx) :user {:email (.-email user)
                                   :avatar (.-photoURL user)})
      :fx [[:common/retrieve-id-token]]}
     {:db (assoc (:db cofx) :user nil)})))

(def search-query
  "query ($text: String, $type: Type) {
  find(text: $text, type: $type) {
    count
    results {
      curie
      label
    }
  }
}")

(re-frame/reg-event-db
 :common/recieve-search-result
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "recieved search result")
   (cljs.pprint/pprint data)
   (merge db data)))

(re-frame/reg-event-fx
 :common/search
 (fn [{:keys [db]} [_ search-text]]
   (js/console.log "submitted search result "
                   search-text
                   " "
                   (:common/search-option db))
   {:db (assoc db
               :common/page 1
               :common/search-text search-text)
    :fx [[:dispatch
          [::re-graph/query
           search-query
           {:text search-text :type (:common/search-option db :GENE)}
           [:common/recieve-search-result]]]]}))

(re-frame/reg-event-db
 :common/select-search-option
 (fn [db [_ option]]
   (assoc db :common/search-option option)))


(def resource-query
  "query ($iri: String) {
  resource(iri: $iri) {
    ...basicFields
    ... on ProbandEvidence {
      ...probandFields
    }
    subject_of {
      ...basicFields
      ...statementFields
    }
    ... on Statement {
      ...statementFields
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
}

fragment basicFields on Resource {
  __typename
  label
  curie
  description
  source {
    curie
    iri
    label
  }
  type {
    __typename
    label
    curie
  }
}

fragment statementFields on Statement {
  subject {
    ...basicFields
  }
  predicate {
    ...basicFields
  }
  object {
    ...basicFields
  }
  evidence {
    ...basicFields
    ... on Statement {
      score
    }
    ... on ProbandEvidence {
      ...probandFields
    }
  }
  score
}")

(re-frame/reg-event-db
 :common/recieve-value-object
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "recieve value object")
   (cljs.pprint/pprint errors)
   (-> db
       (merge data)
       (assoc :common/query-response data))))

(re-frame/reg-event-fx
 :common/select-value-object
 (fn [{:keys [db]} [_ curie]]
   (js/console.log "select value object " curie)
   (let [params {:iri curie}
         history (if (:value-object db)
                   (cons (:value-object db)
                         (:history db))
                   (:history db))]
     {:db (assoc
           db
           :common/last-query resource-query
           :common/last-params (str params)
           :history history)
      :fx [[:dispatch
            [::re-graph/query
             resource-query
             params
             [:common/recieve-value-object]]]]})))
