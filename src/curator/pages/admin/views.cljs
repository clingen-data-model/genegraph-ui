(ns curator.pages.admin.views
  (:require [curator.common.views :as common-views]
            [curator.pages.admin.events :as events]
            [curator.pages.admin.subs :as subs]
            [re-frame.core :refer [dispatch subscribe]]
            [clojure.browser.dom :as dom]))

(defn menu []
  (let [section @(subscribe [::subs/current-section])]
    [:aside.menu
     [:p.menu-label "users"]
     [:ul.menu-list
      [:li [:a {:class (when (= :new-user section) "is-active")
                :on-click #(dispatch [:admin/set-section :new-user])}
            "add new user"]]
      [:li [:a {:class (when (= :find-user section) "is-active")
                :on-click #(dispatch [:admin/set-section :find-user])}
            "find user"]]
      [:li [:a {:class (when (= :list-users section) "is-active")
                :on-click #(dispatch [:admin/set-section :list-users])}
            "list users"]]]]))


(defn user-result []
  (let [user @(subscribe [::subs/user-result])]
    [:div
     [:div.field
      [:div.label "name"]
      [:div.control
       [:input.input {:type "text"
                      :id "user-name"
                      :placeholder "name"
                      :defaultValue (:label user)}]]]
     [:div.field
      [:div.label "email"]
      [:div.control
       [:input.input {:type "text"
                      :id "user-email"
                      :placeholder "email"
                      :defaultValue (:email user)}]]]]))

(defn find-user-form []
  [:div
   [:div.field
    [:label.label "find by email"]
    [:input.input {:type "text" :id "email-search" :placeholder "email"}] ]
   [:div.field
    [:div.control
     [:button.button
      {:on-click #(dispatch [:admin/send-find-user-query
                             (.-value (dom/get-element "email-search"))])}
      "find user"]]]])

(defn find-user []
  [:div.column
   (find-user-form)
   (user-result)])

(defn new-user []
  (let [groups @(subscribe [::subs/groups])]
    [:div.column
     [:div.field
      [:div.label "name"]
      [:div.control
       [:input.input {:type "text"
                      :id "user-name"
                      :placeholder "name"}]]]
     [:div.field
      [:div.label "email"]
      [:div.control
       [:input.input {:type "text"
                      :id "user-email"
                      :placeholder "email"}]]]
     
     [:div.field
      [:div.label "groups"]]
     (for [group groups]
       ^{:key (:curie group)}
       [:div.field
        [:label.checkbox
         [:input {:type "checkbox" :id (:curie group)}]
         (str " " (:label group))]])
     [:div.field
      [:div.control
       [:button.button.is-primary
        {:on-click #(dispatch [:admin/create-user
                               {:name (.-value (dom/get-element "user-name"))
                                :email (.-value (dom/get-element "user-email"))
                                :groups (->> groups
                                             (map :curie)
                                             (filter (fn [id]
                                                       (.-checked (dom/get-element id)))))}])}
        "create"]]]]))

(defn list-users []
  [:div])

(defn admin-content []
  (let [section @(subscribe [::subs/current-section])]
    [:section.section
     [:div.columns
      [:div.column.is-one-fifth
       (menu)]
      (case section
        :find-user (find-user)
        :new-user (new-user)
        :list-users (list-users)
        [:div])
       
      ]]))

(defn admin []
  [:div
   [:section.section (common-views/navbar)]
   (admin-content)])
