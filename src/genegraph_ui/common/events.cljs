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

(def search-queries
  {:gene   "query($text: String) {
  genes(text: $text) {
    count
    gene_list {
      curie
      label
      curation_activities
      gene_validity_assertions {
        curie
        disease {
          curie
          label
        }
      }
    }
  }
}"
   :affiliation
   "query ($text: String, $offset: Int = 0) {
  affiliations(text: $text)
  {
    agent_list {
      curie
      label
      gene_validity_assertions(offset: $offset,
        sort: {field: GENE_LABEL}) {
        count
        curation_list {
          curie
          gene {
            label
            curie
          }
          disease {
            label
            curie
          }
        }
      }
    }
  }
}"
   :disease
   "query ($text: String) {
  diseases(text: $text) {
    disease_list {
      curie
      label
    }
  }
}"})

(re-frame/reg-event-db
 :common/recieve-search-result
 (fn [db [_ {:keys [data errors]}]]
   (js/console.log "recieved search result")
   (cljs.pprint/pprint data)
   (merge db data)))

(re-frame/reg-event-fx
 :common/search
 (fn [{:keys [db]} [_ search-text]]
   (js/console.log "submitted search result " search-text)
   {:db (assoc db
               :common/page 1
               :common/search-text search-text)
    :fx [[:dispatch
          [::re-graph/query
           (get search-queries (:common/search-option db))
           {:text search-text}
           [:common/recieve-search-result]]]]}))

(re-frame/reg-event-db
 :common/select-search-option
 (fn [db [_ option]]
   (assoc db :common/search-option option)))

(re-frame/reg-event-fx
 :common/select-page
 (fn [{:keys [db]} [_ page]]
   {:db (assoc db :common/page page)
    :fx [[:dispatch
          [::re-graph/query
           (get search-queries (:common/search-option db))
           {:text (:common/search-text db)
            :offset (* 10 (- page 1))}
           [:common/recieve-search-result]]]]}))
