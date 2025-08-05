<%@ page import="expensesheet.Transaction" %>
<%@ page import="expensesheet.ExpenseUser" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Transactions - ${user?.name}</title>
    </head>
    <body>
        <h1>Transactions for ${user?.name}</h1>
        <div class="actions" style="margin-bottom:1em;">
            <g:link class="button" action="create" params="[userId: user?.id]">Add Transaction</g:link>
            <g:link class="button" action="exportCsv" params="[userId: user?.id]">Export CSV</g:link>
        </div>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>ZAR</th>
                    <th>USD</th>
                    <th>Running Balance</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${transactionList}" var="transaction">
                    <tr>
                        <td><g:formatDate date="${transaction.dateCreated}" format="yyyy-MM-dd HH:mm"/></td>
                        <td>${transaction.description}</td>
                        <td>${g.formatNumber(number: transaction.amount, format: '###,##0.00')}</td>
                        <td>${g.formatNumber(number: transaction.getUsdValue(), format: '###,##0.00')}</td>
                        <td>${g.formatNumber(number: transaction.runningBalance, format: '###,##0.00')}</td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <div class="pagination">
            <g:paginate controller="transaction" action="index" params="[userId: user?.id]" total="${transactionCount ?: 0}"/>
        </div>
    </body>
</html>