package expensesheet

import groovy.transform.ToString

/**
 One expense. When saved, `beforeInsert` does the math: new balance = old balance - this amount. `usdValue` shows the ZAR amount in USD using `CurrencyConversionService`.
 */
@ToString(includeNames = true)
class Transaction {
    /**
     If you name a `Date` field `dateCreated` or `lastUpdated`. Grails does all that.
     */
    Date dateCreated

    /** User puts in whatever description: makeup, game, coffee etc. */
    String description

    /** Amount. positive values reduce balance */
    BigDecimal amount

    /** Running balance after this transaction has been made */
    BigDecimal runningBalance

    /** The user who recorded this transaction */
    ExpenseUser user

    static belongsTo = [user: ExpenseUser]

    /** Transient property used for USD conversion */
    static transients = ['usdValue']

    static constraints = {
        description blank: false
        // Only the `amount` field is required. Controller makes sure it’s not negative.
        amount nullable: false
        runningBalance nullable: true
        user nullable: false
    }

    /**
     Before saving, it calculates the running balance: grabs the user’s last transaction and subtracts this amount from that. If it’s their first time, it uses their starting balance instead.
     */
    def beforeInsert() {
        BigDecimal lastBalance = 0G
        if (user) {
            // Find the most recent transaction for the user ordered by date
            Transaction lastTransaction = Transaction.findByUser(user, [sort: 'dateCreated', order: 'desc'])
            if (lastTransaction) {
                lastBalance = lastTransaction.runningBalance ?: 0G
            } else {
                lastBalance = user?.startingBalance ?: 0G
            }
        }
        runningBalance = (lastBalance ?: 0G) - (amount ?: 0G)
    }

    /**
     Temporary getter that turns the ZAR amount into USD using `CurrencyConversionServiceHolder`, which hooks into the service set up when the app starts.
     *
     * @return amount in USD or null if the service is unavailable
     */
    BigDecimal getUsdValue() {
        def svc = CurrencyConversionServiceHolder?.currencyConversionService
        if (svc) {
            return svc.convertZarToUsd(amount)
        }
        return null
    }
}