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

