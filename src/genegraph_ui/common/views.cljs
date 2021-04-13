(ns genegraph-ui.common.views
  (:require [genegraph-ui.common.events :as common-events]
            [genegraph-ui.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [reitit.frontend.easy :as rfe]))

(defn navbar []
  (let [user @(subscribe [::common-subs/user])
        admin-user @(subscribe [::common-subs/user-is-admin])]
    [:nav.navbar.is-fixed-top {:role "navigation"}
     [:div.navbar-brand
      [:a.navbar-item {:href (rfe/href :home)}
       [:img {:src "/images/logo.svg"}]]
      [:a.navbar-item {:href (rfe/href :home)}
       [:h5.title.is-6 "genegraph-ui"]]]
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
