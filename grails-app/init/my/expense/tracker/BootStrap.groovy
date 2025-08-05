package expensesheet

import grails.util.Environment

/**
 The `BootStrap` class runs when the app starts. It registers `CurrencyConversionService` in a static holder so domain classes can use it. You can also do setup stuff here, like adding default users or transactions.
 */
class BootStrap {

    CurrencyConversionService currencyConversionService

    def init = { servletContext ->
        // Expose the currency conversion service to domain classes
        CurrencyConversionServiceHolder.currencyConversionService = currencyConversionService
    }

    def destroy = {
    }
}