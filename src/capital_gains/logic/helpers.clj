(ns capital-gains.logic.helpers
	(:require [schema.core :as s
						 :include-macros true]
						[capital-gains.models :as m]
						))

(s/defn is-buy-operation? [operation :- m/Operation]
	(if (= (:operation operation) "buy") true false))

(s/defn calculate-profit [acc :- m/Accumulator operation :- m/Operation]
	(let [dif (- (:unit-cost operation) (:weighted-average-price acc))]
		(if (< 0 dif)
			(* dif (:quantity operation))
			0)))

(s/defn calculate-new-quantity [acc operation]
	(let [quantityAcc (:quantity acc)
				quantityOp  (:quantity operation)]
		(if (is-buy-operation? operation)
			(+ quantityAcc quantityOp)
			(- quantityAcc quantityOp))
		)
	)


(s/defn create-new-accumulator [] :- m/Accumulator
	{:weighted-average-price 0
	 :quantity               0
	 :tax                    []
	 :loss                   0})