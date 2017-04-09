(ns dfrese.calliope.orpheus-test
  (:require #?@(:cljs [[cljs.test :refer-macros [deftest is testing]]])
            #?@(:clj [[clojure.test :refer [deftest is testing]]])
            [dfrese.edomus.virtual :as vdom]
            [dfrese.edomus.core :as dom]
            [dfrese.calliope.orpheus :as o]
            [dfrese.calliope.ext :as ext]
            [dfrese.orpheus.html :as html]))

(deftest canvas-test
  (let [view (fn [model]
               {"childNodes" [(html/span model)]})
        c (o/canvas view)
        elem (dom/create-element (vdom/new-document) "div")]
    (let [st (ext/-init-canvas! c elem)]
      (is (= 0 (count (dom/child-nodes elem))))
      (let [st (ext/-update-canvas! c st elem "Hello" nil)]
        (is (= {"childNodes" [(html/span "Hello")]} ))
        (is (= 1 (count (dom/child-nodes elem))))
        (is (= "Hello" (dom/text-node-value (first (dom/child-nodes (first (dom/child-nodes elem)))))))
        (let [st (ext/-update-canvas! c st elem "World" nil)]
          (is (= 1 (count (dom/child-nodes elem))))
          (is (= "World" (dom/text-node-value (first (dom/child-nodes (first (dom/child-nodes elem)))))))

          (ext/-finish-canvas! c st elem)
          (is (= 0 (count (dom/child-nodes elem)))))))))

;; TODO: check with msg-callback
