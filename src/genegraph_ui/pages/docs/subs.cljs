(ns genegraph-ui.pages.docs.subs
  (:require [re-frame.core :as re-frame]
            [markdown.core :as markdown]
            [clojure.walk :as walk]
            [nextjournal.markdown :as md]
            [nextjournal.markdown.transform :as md.transform]
            [clojure.string :as str]))

(def bulma-style-substitutions
  {:h1 :h1.title.is-1
   :h2 :h1.title.is-2
   :h3 :h1.title.is-3
   :h4 :h1.title.is-4
   :h5 :h1.title.is-5
   :h6 :h1.title.is-6
   :p :p.is-family-secondary.block})

(defn add-bulma-style [hiccup]
  (walk/postwalk
   (fn [x]
     (if (vector? x)
       (let [tag (first x)]
         (assoc x 0 (bulma-style-substitutions tag tag)))
       x))
   hiccup))


(re-frame/reg-sub
 ::markdown
 (fn [db]
   (-> db
       :docs/page
       md/->hiccup
       add-bulma-style)
   #_(let [result (md/->hiccup (:docs/page db))
         #_(-> db
             :docs/page
             markdown/md->html
             (str/replace #"<h(\d)>" "<h$1 class=\"title is-$1\">"))]
     (js/console.log result)
     result)))
