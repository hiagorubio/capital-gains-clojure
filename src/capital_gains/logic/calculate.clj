(ns capital-gains.logic.calculate
	(:require [capital-gains.logic.helpers :as h]
																						 [capital-gains.models :as m]
																						 [schema.core :as s]))

(s/defn have-loss? [unit-cost, actual-weighted-average-price]
	(< unit-cost actual-weighted-average-price))

(s/defn calculate-loss [acc :- m/Accumulator operation :- m/Operation]
	(if (h/is-buy-operation? operation)
		(:loss acc)

		(let [
					actual-weighted-average-price (float (:weighted-average-price acc))
					unit-cost                     (float (:unit-cost operation))
					quantity                      (float (:quantity operation))
					]
			(if (have-loss? unit-cost actual-weighted-average-price)
				(* (- unit-cost actual-weighted-average-price) quantity)
				(+ (:loss acc) (h/calculate-profit acc operation))
				))))

(s/defn return-empty-tax [] {:tax 0.00})

(s/defn calculate-operation-value [operation :- m/Operation]
	(* (:quantity operation) (:unit-cost operation)))

(s/defn operation-value-exceeds-tax-limit? [operation]
	(< 20000.00 (calculate-operation-value operation)))

(s/defn apply-tax [profit operation]
	(if (operation-value-exceeds-tax-limit? operation)
		{:tax (* profit 0.2)}
		(return-empty-tax)))

(s/defn hass-loss? [loss] (> 0 loss))
(s/defn hass-profit? [profit-with-loss-deducted] (< 0 profit-with-loss-deducted))

(s/defn calculate-tax-deducting-loss-if-it-exists
	[acc :- m/Accumulator operation :- m/Operation]
	(let [profit (h/calculate-profit acc operation)
				loss   (:loss acc)]
		(if (hass-loss? loss)
			(let [profit-with-loss-deducted (+ profit loss)]
				(if (hass-profit? profit-with-loss-deducted)
					 (apply-tax profit-with-loss-deducted operation)
					(return-empty-tax)
					)
				)
			(apply-tax profit operation)
			)))

(s/defn calculate-tax-if-needed :- m/Tax
	[acc :- m/Accumulator operation :- m/Operation]
	(if (h/is-buy-operation? operation)
		(return-empty-tax)
		(let [profit (h/calculate-profit acc operation)]
			(if (hass-profit? profit)
				(calculate-tax-deducting-loss-if-it-exists acc operation)
				(return-empty-tax)))))

(s/defn calculate-weighted-average-price
	[acc :- m/Accumulator operation :- m/Operation]
	(if (h/is-buy-operation? operation)
		(if (= 0 (:weighted-average-price acc))
			(:unit-cost operation)
			(let [actual-stocks-quantity        (:quantity acc)
						actual-weighted-average-price (:weighted-average-price acc)
						new-quantity                  (:quantity operation)
						unit-cost                     (:unit-cost operation)
						]
				(/
					(+
						(* actual-stocks-quantity actual-weighted-average-price)
						(* new-quantity unit-cost))
					(+ actual-stocks-quantity new-quantity))))
		(:weighted-average-price acc)
		)
	)