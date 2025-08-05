package expensesheet

/**
 * Represents the user of all their transactions.
 * Each user has a name and a starting balance.
 * All transactions recorded by the user will minus from balance from starting number.
 */
class ExpenseUser {
    /** The user’s display name */
    String name

    /** Starting ZAR balance provided when the user is created */
    BigDecimal startingBalance

    /** A user can have many transactions */
    static hasMany = [transactions: Transaction]

    static constraints = {
        name blank: false
        // Only require the starting balance field; validation of non neg values handled in the controller to avoid issues.
        startingBalance nullable: false
    }
}