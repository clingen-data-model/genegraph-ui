(ns genegraph-ui.common.views
  (:require [genegraph-ui.common.events :as common-events]
            [genegraph-ui.common.subs :as common-subs]
            [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [clojure.string :as s]
            [reitit.frontend.easy :as rfe]))

(defn signin-widget []
  (let [user @(subscribe [::common-subs/user])]
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
          [:strong "log in"]]]]])))

(defn brand []
  [:div.navbar-brand
   [:a.navbar-item {:href (rfe/href :home)}
    [:img {:src "/images/logo.svg"}]]
   [:a.navbar-item {:href (rfe/href :home)}
    [:h5.title.is-6 "Genegraph"]]])

(defn menu []
  [:div.navbar-menu
   [:div.navbar-start
    [:a.navbar-item {:href "#"}
     ]]])

(defn navbar []
  [:nav.navbar.is-fixed-top {:role "navigation"}
   (brand)])

(defn panel-tabs []
  (let [search-option @(subscribe [::common-subs/current-search-option])
        options [:GENE :DISEASE :AFFILIATION]]
    [:p.panel-tabs
     (for [option options]
       (if (= search-option option)
         ^{:key option}
         [:a.is-active
          {:on-click #(dispatch [:common/select-search-option option])}
          (s/lower-case (name option))]
         ^{:key option}
         [:a
          {:on-click #(dispatch [:common/select-search-option option])}
          (s/lower-case (name option))]))]))

(defn panel-search []
  (let [search-option @(subscribe [::common-subs/current-search-option])]
    [:div.panel-block
     [:form.control.has-icons-left
      {:on-submit
       #(do (dispatch [:common/search
                       (.-value (.getElementById js/document "search"))])
            (.preventDefault %)
            false)}
      [:input.input
       {:id "search"
        :type :search
        :placeholder (s/lower-case (name search-option))}]
      [:span.icon.is-left
       [:i.fas.fa-search {:aria-hidden :true}]]]]))

(defn gene-results []
  (let [genes @(subscribe [::common-subs/gene-list])]
    [:div
     (for [gene genes]
       ^{:key gene}
       [:div
        [:a.panel-block
         {:href (rfe/href :gene gene)}
         (:label gene)]
        (for [assertion (:gene_validity_assertions gene)]
          ^{:key assertion} [:div.panel-block
                             [:a.icon
                              {:href (rfe/href :gene-validity assertion)}
                              [:i.fas.fa-file]]
                             [:a {:href (rfe/href :disease (:disease assertion))}
                              (get-in assertion [:disease :label])]])])]))
(defn disease-results []
  [:div])

(defn pagination [current-page total-count]
  (let [max-page (-> total-count (/ 10) Math/ceil)
        page-seq (range 1 (+ 1 max-page))
        displayed-pages #{1
                          max-page
                          current-page
                          (- current-page 1)
                          (+ current-page 1)}]
    [:ul.pagination-list
     (for [page (filter displayed-pages page-seq)]
       (if (= current-page page)
         ^{:key page}
         [:li
          {:on-click #(dispatch [:common/select-page page])}
          [:a.pagination-link.is-current page]]
         ^{:key page}
         [:li
          {:on-click #(dispatch [:common/select-page page])}
          [:a.pagination-link page]]))]))

(defn affiliation-results []
  (let [current-page @(subscribe [::common-subs/current-page])]
    [:div
     (for [affiliation @(subscribe [::common-subs/affiliation-list])]
       ^{:key affiliation}
       [:div
        [:a.panel-block
         (:label affiliation)]
        (for [assertion (get-in affiliation
                                [:gene_validity_assertions :curation_list])]
          ^{:key assertion}
          [:div.panel-block
           {:title (get-in assertion [:disease :label])}
           [:a.icon
            {:href (rfe/href :gene-validity assertion)}
            [:i.fas.fa-file]]
           [:a {:href (rfe/href :gene (:gene assertion))}
            (get-in assertion [:gene :label])]])
        [:nav.panel-block.pagination.is-small
         {:role "navigation"
          :aria-label "pagination"}
         (pagination current-page
                     (get-in affiliation [:gene_validity_assertions
                                          :count]))]])]))

(defn panel-results []
  (let [result @(subscribe [::common-subs/search-result])]
    [:div 
     (for [i (:results result)]
       ^{:key i}
       [:a.panel-block
        {:on-click #(dispatch [:common/select-value-object (:curie i)])}
        (:label i)])]))


(defn panel-breadcrumb [object]
  [:nav.breadcrumb.panel-block {:aria-label "breadcrumbs"}
   [:ul
    [:li [:a.icon
          {:on-click #(dispatch [:common/clear-value-object])}
          [:i.fas.fa-search]]]
    (when object
      [:li [:a (:label object)]])]])

(defn search []
  [:div
   (panel-tabs)
   (panel-search)
   (panel-results)])

(defn gene-sidepanel [gene]
  [:div
   (for [assertion (:subject_of gene)]
     ^{:key assertion}
     [:div.panel-block
      [:a
       (get-in assertion [:object :label])]])])

(defn affiliation-sidepanel [affiliation]
  [:div
   (for [contribution (:contributions affiliation)]
     ^{:key contribution}
     [:div.panel-block
      {:title (get-in contribution
                      [:artifact :subject :object :label])}
      [:a.icon [:i.fas.fa-file]]
      [:a
       (get-in contribution
               [:artifact :subject :subject :label])]])])

(defn value-object [object]
  (let [type @(subscribe [::common-subs/value-object-type])]
    (cond
      (type :so/Gene) (gene-sidepanel object)
      (type :cg/Affiliation) (affiliation-sidepanel object)
      :else [:div])))

(defn side-panel []
  (let [result @(subscribe [::common-subs/current-value-object])]
    [:nav.panel
     ;; [:box.panel-heading "Genegraph"]
     (panel-breadcrumb result)
     (if result
       (value-object result)
       (search))]))
