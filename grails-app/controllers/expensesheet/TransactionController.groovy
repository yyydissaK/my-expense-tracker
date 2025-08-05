package expensesheet

import grails.web.servlet.mvc.GrailsParameterMap
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import grails.gorm.transactions.Transactional

/**
 * Controller for managing Transactions.  It uses the scaffolding plugin for CRUD operations.
 * An `exportCsv` action
 * generates a CSV file containing all transactions for a user.
 */
class TransactionController {
    static scaffold = Transaction

    CurrencyConversionService currencyConversionService

    /**
     * Displays a paginated list of transactions for the current user.
     * The user is determined by the `userId` request parameter or a session attribute.
     * If no user is found the browser is redirected to the ExpenseUser index page.
     */
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        ExpenseUser user
        if (params.userId) {
            user = ExpenseUser.get(params.long('userId'))
        } else if (session.userId) {
            user = ExpenseUser.get(session.userId)
        }
        if (!user) {
            flash.message = 'Please select or create a user first.'
            redirect(controller: 'expenseUser', action: 'index')
            return
        }
        // Persist the user ID in the session for subsequent requests
        session.userId = user.id
        def transactionList = Transaction.findAllByUser(user, [sort: 'dateCreated', order: 'desc', max: params.max, offset: params.offset ?: 0])
        def transactionCount = Transaction.countByUser(user)
        // Render our custom index view and pass user information along
        render view: 'index', model: [user: user, transactionList: transactionList, transactionCount: transactionCount]
    }

    /**
     * Export all transactions for a particular user to CSV.  The
     * resulting file contains the date, description, amount in ZAR, amount in USD (converted on demand) and the running balance.
     */
    def exportCsv() {
        ExpenseUser user = ExpenseUser.get(params.long('userId'))
        if (!user) {
            flash.message = 'User not found'
            redirect(controller: 'expenseUser', action: 'index')
            return
        }
        def transactions = Transaction.findAllByUser(user, [sort: 'dateCreated', order: 'asc'])
        response.setHeader('Content-disposition', "attachment; filename=${user.name?.replaceAll(' ', '_')}-transactions.csv")
        response.contentType = 'text/csv'
        def writer = new BufferedWriter(new OutputStreamWriter(response.outputStream))
        writer.write('Date,Description,ZAR,USD,Running Balance\n')
        // Prepare a formatter for timestamps
        def sdf = new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
        transactions.each { t ->
            BigDecimal usd = currencyConversionService.convertZarToUsd(t.amount)
            String formatted = ''
            if (t.dateCreated) {
                // Format the timestamp safely using SimpleDateFormat
                formatted = sdf.format(t.dateCreated)
            }
            writer.write("${formatted},${t.description},${t.amount},${usd},${t.runningBalance}\n")
        }
        writer.flush()
        writer.close()
        // Prevent Grails from trying to render a view
        return
    }

    /**
     * Override save to ensure the transaction is associated with the correct user.
     * The user is taken from the `userId` parameter or the session.
     */
    def save() {
        // If the GSP did not bind a user instance we bind it here
        if (!params.user?.id) {
            if (params.userId) {
                params.user = ExpenseUser.get(params.long('userId'))
            } else if (session.userId) {
                params.user = ExpenseUser.get(session.userId)
            }
        }
        super.save()
    }

    /**
     * Render a form to create a new transaction for a given user.
     * If no user Id is supplied the request is redirected back to the user list.
     */
    def create() {
        ExpenseUser user = null
        if (params.userId) {
            user = ExpenseUser.get(params.long('userId'))
        } else if (session.userId) {
            user = ExpenseUser.get(session.userId)
        }
        if (!user) {
            flash.message = 'Please select or create a user first.'
            redirect(controller: 'expenseUser', action: 'index')
            return
        }
        render view: 'create', model: [transaction: new Transaction(user: user), user: user]
    }

    /**
     * Persist a new transaction.
     * Parses the amount and validates that it is non-negative.
     * Updates the session userId so other actions continue to refer to this user.
     */
    @Transactional
    def saveTransaction() {
        ExpenseUser user = null
        if (params.userId) {
            user = ExpenseUser.get(params.long('userId'))
        } else if (session.userId) {
            user = ExpenseUser.get(session.userId)
        }
        if (!user) {
            flash.message = 'Please select or create a user first.'
            redirect(controller: 'expenseUser', action: 'index')
            return
        }
        BigDecimal amount = null
        if (params.amount) {
            try {
                amount = new BigDecimal(params.amount)
            } catch (Exception e) {
                // leave null; validation will catch below
            }
        }
        Transaction t = new Transaction(user: user, description: params.description, amount: amount)
        // Validate non-negative amount
        if (amount == null) {
            t.errors.rejectValue('amount', 'amount.null', 'Amount is required')
        } else if (amount < 0) {
            t.errors.rejectValue('amount', 'amount.min', 'Amount must be non-negative')
        }
        if (!t.validate() || t.hasErrors()) {
            render view: 'create', model: [transaction: t, user: user]
            return
        }
        t.save(flush: true)
        flash.message = 'Transaction created successfully'
        // Persist the user ID in the session for subsequent requests
        session.userId = user.id
        redirect action: 'index', params: [userId: user.id]
    }
}