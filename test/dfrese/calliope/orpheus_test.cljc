(ns dfrese.calliope.orpheus-test
  (:require #?@(:cljs [[cljs.test :refer-macros [deftest is testing]]
                       ;;[dfrese.orpheus.core :as core :include-macros true]
                       [dfrese.edomus.virtual :as vdom]
                       ])
            #?@(:clj [[clojure.test :refer [deftest is testing]]
                      [dfrese.edomus.virtual :as vdom]])
            [dfrese.edomus.core :as dom]
            [dfrese.calliope.orpheus :as o]
            [dfrese.calliope.core :as c]
            [dfrese.calliope.app :as app] ;; TODO: move all to core maybe?
            [dfrese.orpheus.html :as html]))

(deftest canvas-test
  (let [view (fn [model]
               (html/span model))
        c (o/canvas view)
        elem (dom/create-element (vdom/new-document) "div")]
    (is (= {"childNodes" []} (app/init-canvas! c elem)))
    (is (= 0 (count (dom/child-nodes elem))))
    (is (= {"childNodes" [(html/span "Hello")]} (app/update-canvas! c {"childNodes" []} elem "Hello" nil)))
    (is (= 1 (count (dom/child-nodes elem))))
    (is (= "Hello" (dom/text-node-value (first (dom/child-nodes (first (dom/child-nodes elem)))))))
    (is (= {"childNodes" [(html/span "World")]} (app/update-canvas! c {"childNodes" [(html/span "Hello")]} elem "World" nil)))
    (is (= 1 (count (dom/child-nodes elem))))
    (is (= "World" (dom/text-node-value (first (dom/child-nodes (first (dom/child-nodes elem)))))))
    (is (= {} (app/finish-canvas! c {"childNodes" [(html/span "Hello")]} elem)))
    (is (= 0 (count (dom/child-nodes elem))))))

;; TODO: check with msg-callback
