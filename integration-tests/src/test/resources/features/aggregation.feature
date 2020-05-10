Feature: Quotes aggregation

  Scenario: Produce 3 quotes and aggregate them

    Given mock producer is started on port 50000
    And aggregated quotes are produced at 'http://localhost:84/quotes?limit=1'

    When quote is produced in OHLC format [1.0, 1.5, 1.0, 1.3]
    And quote is produced in OHLC format [1.1, 1.7, 1.1, 1.5]
    And quote is produced in OHLC format [1.2, 2.0, 1.2, 1.7]

    Then it should be aggregated in 60 seconds
    And aggregated quote is [1.0, 2.0, 1.0, 1.7]
