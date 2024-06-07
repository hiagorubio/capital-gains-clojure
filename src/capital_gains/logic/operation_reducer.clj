(ns capital-gains.logic.operation-reducer
	(:require
		[schema.core :as s]
		[capital-gains.logic.helpers :as helpers]
		[capital-gains.logic.calculate :as calculate]
		))


(s/defn operation-reducer
	[acc operation]
	(when operation
		{
		 :weighted-average-price (calculate/calculate-weighted-average-price acc operation)
		 :quantity               (helpers/calculate-new-quantity acc operation)
		 :tax                    (conj (:tax acc) (calculate/calculate-tax-if-needed acc operation))
		 :loss                   (calculate/calculate-loss acc operation)
		 }
		))