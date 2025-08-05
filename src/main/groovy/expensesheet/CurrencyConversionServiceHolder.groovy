package expensesheet

/**
 A basic static holder that lets domain classes use `CurrencyConversionService`, since they can’t get it via normal Spring injection. It’s set up at app startup so domain classes can safely call it.
 */
class CurrencyConversionServiceHolder {
    static CurrencyConversionService currencyConversionService
}