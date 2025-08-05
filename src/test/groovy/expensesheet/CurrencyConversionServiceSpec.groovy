package expensesheet

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

/**
 Unit tests for `CurrencyConversionService`. Instead of hitting the real Fixer.io API (because that’d be slow and chaotic), `getZarToUsdRate` is faked to return a fixed rate. The test checks that conversion math is right and rounds to two decimals.
 */
class CurrencyConversionServiceSpec extends Specification implements ServiceUnitTest<CurrencyConversionService> {

    void "convertZarToUsd multiplies by rate"() {
        given: "a stubbed exchange rate"
        service.metaClass.getZarToUsdRate = { -> 0.1G }

        when: "converting 100 ZAR at 0.1 rate"
        BigDecimal usd = service.convertZarToUsd(100G)

        then: "we expect 10 USD with two decimal precision"
        usd == 10.00G
    }
}