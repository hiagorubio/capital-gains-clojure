(ns capital-gains.logic.helpers-test
	(:require [capital-gains.logic.helpers :as h]
						[clojure.test :refer (deftest is testing)]
						[capital-gains.fixtures :as fixtures]
						))


(deftest is-buy-operation?-test
	(testing "if its a buy operation"
		(is (true? (h/is-buy-operation? {:operation "buy"}))))

	(testing "if isn't a buy operation"
		(is (false? (h/is-buy-operation? {:operation "sell"}))))

	(testing "if isn't a buy operation"
		(is (false? (h/is-buy-operation? {:operation nil}))))

	(testing "if isn't a buy operation"
		(is (false? (h/is-buy-operation? {:foo nil}))))

	)




(deftest calculate-profit-test
	(testing "calc profit"
		(is
			(=
				(h/calculate-profit
					(fixtures/create-accumulator 10 10 [] 0)
					(fixtures/create-operation "sell" 15 10))
			50)
			)

		(is
			(=
				(h/calculate-profit
					(fixtures/create-accumulator 0 0 [] 0)
					(fixtures/create-operation "sell" 15 10))
				150)
			)

		(is
			(=
				(h/calculate-profit
					(fixtures/create-accumulator 10 100 [] 100)
					(fixtures/create-operation "sell" 5 100))
				0)
			)


		))

(deftest calculate-new-quantity-test
	(testing "new quantity on buy operation"
		(is (h/calculate-new-quantity
					(fixtures/create-accumulator 0 0 [] 0)
					(fixtures/create-operation "buy" 30 100)
					) 100)


		(is (h/calculate-new-quantity
					(fixtures/create-accumulator 0 50 [] 0)
					(fixtures/create-operation "buy" 30 39)
					) 89)

		(is (h/calculate-new-quantity
					(fixtures/create-accumulator 0 0 [] 0)
					(fixtures/create-operation "buy" 0 0)
					) 0)
		)
	(testing "new quantity on sell operation"
		(is (h/calculate-new-quantity
					(fixtures/create-accumulator 0 50 [] 0)
					(fixtures/create-operation "sell" 30 39)
					) 11)

		(is (h/calculate-new-quantity
					(fixtures/create-accumulator 0 0 [] 0)
					(fixtures/create-operation "sell" 30 39)
					) -39)

		(is (h/calculate-new-quantity
					(fixtures/create-accumulator 0 0 [] 0)
					(fixtures/create-operation "sell" 0 0)
					) 0))
	)