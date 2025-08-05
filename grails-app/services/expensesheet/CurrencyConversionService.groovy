package expensesheet

import groovy.json.JsonSlurper

/**
 Converts ZAR to USD using the Fixer.io API. API key lives in `application.yml` under `fixer.apiKey`.
 */
class CurrencyConversionService {

    /** `GrailsApplication` is injected so you can grab config properties easily.
     */
    def grailsApplication

    /**
     Fetches the ZAR→USD rate using Fixer.io. Since the free plan only uses EUR as the base, it grabs both USD and ZAR rates in EUR, then calculates USD/ZAR. If the API bails or returns junk, it just gives back 0.
     */
    BigDecimal getZarToUsdRate() {
        String apiKey = grailsApplication?.config?.getProperty('fixer.apiKey')
        if (!apiKey) {
            return 0G
        }
        try {
            // Construct the URL: base is EUR due to Fixer free
            String url = "http://data.fixer.io/api/latest?access_key=${apiKey}&symbols=USD,ZAR"
            String json = new URL(url).text
            def data = new JsonSlurper().parseText(json)
            if (!(data && data.success)) {
                return 0G
            }
            BigDecimal usdRate = data.rates?.USD ?: 0G
            BigDecimal zarRate = data.rates?.ZAR ?: 0G
            if (usdRate && zarRate) {
                // USD rate divided by ZAR rate yields ZAR→USD conversion
                return usdRate / zarRate
            }
        } catch (Exception e) {
            // Swallow exceptions and fall through
        }
        return 0G
    }

    /**
     Takes an amount in ZAR, converts it to USD using the latest exchange rate, and returns that.
     */
    BigDecimal convertZarToUsd(BigDecimal amount) {
        if (!amount) {
            return 0G
        }
        BigDecimal rate = getZarToUsdRate()
        return (amount * rate).setScale(2, BigDecimal.ROUND_HALF_UP)
    }
}