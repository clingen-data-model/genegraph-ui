(ns curator.common.views
  (:require [curator.common.events :as common-events]
            [curator.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [reitit.frontend.easy :as rfe]))

(defn navbar []
  (let [user @(subscribe [::common-subs/user])
        admin-user @(subscribe [::common-subs/user-is-admin])]
    [:nav.navbar {:role "navigation"}
     [:div.navbar-brand
      [:a.navbar-item {:href (rfe/href :home)}
       [:img {:src "/images/logo.svg"}]]]
     [:div.navbar-menu
      (when admin-user
        [:a.navbar-item {:href (rfe/href :admin)} "admin"])]
     (if user
       [:div.navbar-end
        [:div.navbar-item (:email user)]
        [:div.navbar-item [:figure.image.is-32x32 
                           [:img.is-rounded {:src (:avatar user)}]]]
        [:div.navbar-item
         [:div.buttons
          [:button.button
           {:on-click #(dispatch [:common/sign-out])}
           [:strong "log out"]]]]]
       [:div.navbar-end
        [:div.navbar-item
         [:div.buttons
          [:button.button
           {:on-click #(dispatch [:common/authenticate])}
           [:strong "log in"]]]]])]))
