(ns var-schema.core-test
  (:require [clojure.test :refer :all]
            [var-schema.core :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [schema.test]
            [schema.core :as s]))

(use-fixtures :once schema.test/validate-schemas)

(def starts-with-a ( fmap first (s/eq \a)))

(def var-containing-predicate (Var (s/=> s/Any s/Bool)))

(deftest fmap-schema-test
  (doseq [valid-value ["armadillo" "a" "ape" "actually"]]
    (is ( = valid-value (s/validate starts-with-a valid-value))))
  (doseq [invalid-value [ "bonobo" "poophead" ""]]
    (is (thrown? clojure.lang.ExceptionInfo (s/validate starts-with-a invalid-value)))))

(deftest function-throws-an-exception
  (let [boom (fn [_] (throw ( RuntimeException. "poo")))
        s (fmap boom s/Any)]
     (is (thrown? clojure.lang.ExceptionInfo (s/validate s "anything")))))

(def always-true (constantly true))

(deftest happy-path-var
  (is ( = (var always-true) (s/validate var-containing-predicate (var always-true)))))

(deftest not-a-var
  (is (thrown? Exception (s/validate var-containing-predicate always-true))))

(def foo "foo")
(deftest wrong-type-in-var
   (is (thrown? Exception (s/validate var-containing-predicate (var foo)))))

;; what happens if the function f throws an exception? this should probably be handled in a real version of FMap


