(ns capital-gains.logic.calculate-test
  (:require [capital-gains.fixtures :as fixtures]
            [clojure.test :refer [deftest testing is]]
            [capital-gains.logic.calculate :as calculate]))

(deftest operation-has-loss-test
  (testing "operation-has-loss?"

    (is (true? (calculate/operation-has-loss? 1 2)))

    (is (false? (calculate/operation-has-loss? 3 2)))

    (is (true? (calculate/operation-has-loss? 0 2)))

    (is (false? (calculate/operation-has-loss? 1 0)))

    (is (false? (calculate/operation-has-loss? 0 0)))
    ))

(deftest calculate-loss-test
  (testing "buy operation" (is (=
                                (calculate/calculate-loss
                                 (fixtures/create-accumulator 30 0 [] 0)
                                 (fixtures/create-operation "buy" 10 10))
                                0)))

  (testing "sell with loss"
    (is (=
         (calculate/calculate-loss
          (fixtures/create-accumulator 100 10 [] 0)
          (fixtures/create-operation "sell" 10 10))
         -900.0)))

  (testing "sell with profit"
    (is (=
         (calculate/calculate-loss
          (fixtures/create-accumulator 1 10 [] 0)
          (fixtures/create-operation "sell" 10 10))
         90)))


  (testing "sell with same weighted-average-price"
    (is (=
         (calculate/calculate-loss
          (fixtures/create-accumulator 10 10 [] 0)
          (fixtures/create-operation "sell" 10 10))
         0)))


  (testing "sell with loss and having loss on accumulator"
    (is (=
         (calculate/calculate-loss
          (fixtures/create-accumulator 100 10 [] -900)
          (fixtures/create-operation "sell" 10 10))
         -900.0)))

  )

(deftest calc-operation-value-test
  (is (= (calculate/calculate-operation-value (fixtures/create-operation "buy" 10 10))
         100))

  (is (= (calculate/calculate-operation-value (fixtures/create-operation "buy" 0 0))
         0))

  (is (= (calculate/calculate-operation-value (fixtures/create-operation "buy" -10 100))
         -1000))

  )

(deftest operation-value-exceeds-tax-limit?-test
  (testing "operation value exceeds limit"
    (is (true? (calculate/operation-value-exceeds-tax-limit? (fixtures/create-operation "sell" 20000.0 10)))))


  (testing "operation value exceeds limit by 0.1"
    (is (true? (calculate/operation-value-exceeds-tax-limit? (fixtures/create-operation "sell" 20000.1 1)))))

  (testing "operation value is the same of the limit"
    (is (false? (calculate/operation-value-exceeds-tax-limit? (fixtures/create-operation "sell" 20000.0 1)))))

  (testing "operation value is under the limit"
    (is (false? (calculate/operation-value-exceeds-tax-limit? (fixtures/create-operation "sell" 2 1)))))

  (testing "operation value is zero"
    (is (false? (calculate/operation-value-exceeds-tax-limit? (fixtures/create-operation "sell" 2 0))))))

(deftest apply-tax-test
  (testing "apply tax"
    (is (= (calculate/apply-tax 100 (fixtures/create-operation "sell" 20000.0 10))
           {:tax 20.0})))

  (testing "apply tax with profit under limit"
    (is (= (calculate/apply-tax 100 (fixtures/create-operation "sell" 20000.0 1))
           {:tax 0.0})))

  (testing "apply tax with profit under limit"
    (is (= (calculate/apply-tax 100 (fixtures/create-operation "sell" 2 1))
           {:tax 0.0})))  )

(deftest has-loss?-test
  (testing "has loss"
    (is (true? (calculate/has-loss? -1))))

  (testing "has no loss"
    (is (false? (calculate/has-loss? 0))))

  (testing "has no loss"
    (is (false? (calculate/has-loss? 1))))
  )

(deftest has-profit?-test
  (testing "has profit"
    (is (true? (calculate/has-profit? 1))))

  (testing "has no profit"
    (is (false? (calculate/has-profit? 0))))

  (testing "has no profit"
    (is (false? (calculate/has-profit? -1))))
  )

(deftest calculate-tax-deducting-loss-if-it-exists-test
  (testing "has loss"
    (is (= (calculate/calculate-tax-deducting-loss-if-it-exists
            (fixtures/create-accumulator 100 10 [] 0)
            (fixtures/create-operation "sell" 10 10))
           {:tax 0.0})))


  (testing "has profit"
    (is (= (calculate/calculate-tax-deducting-loss-if-it-exists
            (fixtures/create-accumulator 1000 10000 [] 0)
            (fixtures/create-operation "sell" 5000 1000))
           {:tax 800000.0})))

  (testing "has profit but operation is under 20000"
    (is (= (calculate/calculate-tax-deducting-loss-if-it-exists
            (fixtures/create-accumulator 10 10000 [] 0)
            (fixtures/create-operation "sell" 50 10))
           {:tax 0.0})))

  )

(deftest calculate-tax-if-needed-test
  (testing "buy operation"
    (is (= (calculate/calculate-tax-if-needed
            (fixtures/create-accumulator 100 10 [] 0)
            (fixtures/create-operation "buy" 10 10))
           {:tax 0.0})))

  (testing "sell operation with loss"
    (is (= (calculate/calculate-tax-if-needed
            (fixtures/create-accumulator 100 10 [] 0)
            (fixtures/create-operation "sell" 10 10))
           {:tax 0.0})))

  (testing "sell operation with profit but operation under 20000"
    (is (= (calculate/calculate-tax-if-needed
            (fixtures/create-accumulator 100 10 [] 0)
            (fixtures/create-operation "sell" 10 10))
           {:tax 0.0})))

  (testing "sell operation with profit and operation over 20000"
    (is (= (calculate/calculate-tax-if-needed
            (fixtures/create-accumulator 100 10 [] 0)
            (fixtures/create-operation "sell" 20000 10))
           {:tax 39800.0})))
  )

(deftest calculate-weighted-average-price-test
  (testing "sell operation"
    (is (= (calculate/calculate-weighted-average-price
            (fixtures/create-accumulator 100.0 10 [] 0)
            (fixtures/create-operation "sell" 10 10))
           100.0)))

  (testing "buy operation when unit-cost is the same of the weighted-average-price"
    (is (= (calculate/calculate-weighted-average-price
            (fixtures/create-accumulator 100.0 10 [] 0)
            (fixtures/create-operation "buy" 100.0 10))
           100.0)))

  (testing "buy operation when unit-cost is under the weighted-average-price"
    (is (= (calculate/calculate-weighted-average-price
            (fixtures/create-accumulator 100.0 10 [] 0)
            (fixtures/create-operation "buy" 50.0 10))
           75.0)))

  (testing "buy operation when unit-cost is over the weighted-average-price"
    (is (= (calculate/calculate-weighted-average-price
            (fixtures/create-accumulator 100.0 10 [] 0)
            (fixtures/create-operation "buy" 150.0 10))
           125.0)))
  )
