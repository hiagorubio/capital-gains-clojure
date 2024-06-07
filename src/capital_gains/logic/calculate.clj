(ns capital-gains.logic.calculate
  (:require [capital-gains.logic.helpers :as h]
            [capital-gains.models :as m]
            [schema.core :as s]))

(s/def empty-tax {:tax 0.00})
(s/def tax 0.2)
(s/def operation-limit 20000.00)


(s/defn operation-has-loss? [unit-cost, actual-weighted-average-price]
  (< unit-cost actual-weighted-average-price))

(s/defn calculate-loss [acc :- m/Accumulator operation :- m/Operation]
  (if (h/is-buy-operation? operation)
    (:loss acc)

    (let [
          actual-weighted-average-price (float (:weighted-average-price acc))
          unit-cost                     (float (:unit-cost operation))
          quantity                      (float (:quantity operation))
          ]
      (if (operation-has-loss? unit-cost actual-weighted-average-price)
        (* (- unit-cost actual-weighted-average-price) quantity)
        (+ (:loss acc) (h/calculate-profit acc operation))
        ))))

(s/defn calculate-operation-value [operation :- m/Operation]
  (* (:quantity operation) (:unit-cost operation)))

(s/defn operation-value-exceeds-tax-limit? [operation]
  (< operation-limit (calculate-operation-value operation)))

(s/defn apply-tax [profit operation]
  (if (operation-value-exceeds-tax-limit? operation)
    {:tax (* profit tax)}
    empty-tax))

(s/defn has-loss? [loss] (> 0 loss))
(s/defn has-profit? [profit-with-loss-deducted] (< 0 profit-with-loss-deducted))

(s/defn calculate-tax-deducting-loss-if-it-exists
  [acc :- m/Accumulator operation :- m/Operation]
  (let [profit (h/calculate-profit acc operation)
        {loss :loss} acc]
    (if (has-loss? loss)
      (let [profit-with-loss-deducted (+ profit loss)]
        (if (has-profit? profit-with-loss-deducted)
          (apply-tax profit-with-loss-deducted operation)
          empty-tax
          )
        )
      (apply-tax profit operation)
      )))

(s/defn calculate-tax-if-needed :- m/Tax
  [acc :- m/Accumulator operation :- m/Operation]
  (if (h/is-buy-operation? operation)
    empty-tax
    (let [profit (h/calculate-profit acc operation)]
      (if (has-profit? profit)
        (calculate-tax-deducting-loss-if-it-exists acc operation)
        empty-tax))))

(s/defn havent-weighted-average-price? [avg] (= avg 0))

(s/defn calculate-weighted-average-price
  [{actual-quantity        :quantity
    weighted-average-price :weighted-average-price} :- m/Accumulator
   {unit-cost :unit-cost quantity :quantity :as operation} :- m/Operation]
  (if (h/is-buy-operation? operation)
    (if (havent-weighted-average-price? weighted-average-price)
      unit-cost
       (-> (* actual-quantity weighted-average-price)
          (+ (* quantity unit-cost))
          (/ (+ actual-quantity quantity)))
      )
    weighted-average-price))
