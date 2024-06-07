(ns capital-gains.core-test
	(:require [clojure.test :refer [deftest testing is]]
						[capital-gains.core :as capital-gains]))

(deftest processar-entrada
	(testing "challenge scenarios"
		(is
			(=
				(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]")
				[[{:tax 0.00}, {:tax 10000.00}, {:tax 0.00}]]
				))

		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},\n{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50},\n{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50}]\n[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}")
					[[{:tax 0.00}, {:tax 0.00}, {:tax 0.00}]]
					))

		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]")
					[[{:tax 0.00},{:tax 0.00},{:tax 1000.00}]]
					))

		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]")
					[[{:tax 0.00},{:tax 0.00},{:tax 0.00}]]
					))

		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 5000}]")
					[[{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 10000.00}]]
					))
		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},\n{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]")
					[[{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 3000.00}]]
					))

		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},\n{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000},\n{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 4350},\n{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 650}]")
					[[{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 3000.00},{:tax 0.00},{:tax 0.00},{:tax 3700.00},{:tax 0.00}]]
					))

		(is (=
					(capital-gains/processar-entrada "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},\n{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},\n{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000},\n{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000},\n{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 5000},\n{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 4350},\n{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 650}]")
					[[{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 0.00},{:tax 3000.00},{:tax 0.00},{:tax 0.00},{:tax 3700.00},{:tax 0.00}]]
					))


		))