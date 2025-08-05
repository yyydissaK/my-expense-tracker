package expensesheet

import grails.gorm.transactions.Transactional

/**
 * Controller that does CRUD operations on ExpenseUser via scaffolding.
 */
class ExpenseUserController {
    static scaffold = ExpenseUser

    /**
     * Display a paginated list of users.  This will override the defaul scaffold
     * An index to show a custom view
     */
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.offset = params.offset ?: 0
        def expenseUserList = ExpenseUser.list(params)
        def expenseUserCount = ExpenseUser.count()
        render view: 'index', model: [expenseUserList: expenseUserList, expenseUserCount: expenseUserCount]
    }

    /**
     * Shows info of a single user.  If the user does not exist, go back
     */
    def show(Long id) {
        ExpenseUser user = ExpenseUser.get(id)
        if (!user) {
            flash.message = 'User not found'
            redirect action: 'index'
            return
        }
        render view: 'show', model: [expenseUser: user]
    }

    /**
     * Form to edit an existing user.
     */
    def edit(Long id) {
        ExpenseUser user = ExpenseUser.get(id)
        if (!user) {
            flash.message = 'User not found'
            redirect action: 'index'
            return
        }
        render view: 'edit', model: [expenseUser: user]
    }

    /**
     * Update an existing user. Makes sure that there is non negative starting bal
     */
    @Transactional
    def update(Long id) {
        ExpenseUser user = ExpenseUser.get(id)
        if (!user) {
            flash.message = 'User not found'
            redirect action: 'index'
            return
        }
        BigDecimal balance = null
        if (params.startingBalance) {
            try {
                balance = new BigDecimal(params.startingBalance)
            } catch (Exception e) {
                // leave null
            }
        }
        user.name = params.name
        user.startingBalance = balance
        // clear previous errors
        user.clearErrors()
        if (balance == null) {
            user.errors.rejectValue('startingBalance', 'startingBalance.null', 'Starting balance is required')
        } else if (balance < 0) {
            user.errors.rejectValue('startingBalance', 'startingBalance.min', 'Starting balance must be non-negative')
        }
        if (!user.validate() || user.hasErrors()) {
            render view: 'edit', model: [expenseUser: user]
            return
        }
        user.save(flush: true)
        flash.message = 'User updated successfully'
        redirect action: 'show', id: user.id
    }

    /**
     * Form to create a new user.  Uses a custom GSP to help with errors formed by scaffolfing
     */
    def create() {
        // Pass an empty instance to the view for error handling
        render view: 'create', model: [expenseUser: new ExpenseUser()]
    }

    /**
     * Persist a new ExpenseUser.  Converts the startingBalance
     * parameter to BigDecimal and performs validation.
     */
    @Transactional
    def save() {
        BigDecimal balance = null
        if (params.startingBalance) {
            try {
                balance = new BigDecimal(params.startingBalance)
            } catch (Exception e) {
                // leave as null; validation will catch
            }
        }
        ExpenseUser user = new ExpenseUser(name: params.name, startingBalance: balance)
        // Check that the starting balance is provided and non-negative
        if (balance == null) {
            user.errors.rejectValue('startingBalance', 'startingBalance.null', 'Starting balance is required')
        } else if (balance < 0) {
            user.errors.rejectValue('startingBalance', 'startingBalance.min', 'Starting balance must be non-negative')
        }
        if (!user.validate() || user.hasErrors()) {
            // Re-render the form with validation errors
            render view: 'create', model: [expenseUser: user]
            return
        }
        user.save(flush: true)
        flash.message = 'User created successfully'
        redirect action: 'index'
    }
}