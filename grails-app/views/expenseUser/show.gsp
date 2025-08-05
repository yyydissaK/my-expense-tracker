<%@ page import="expensesheet.ExpenseUser" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Show Expense User</title>
    </head>
    <body>
        <h1>User Details</h1>
        <table class="table table-bordered">
            <tr><th>Name</th><td>${expenseUser?.name}</td></tr>
            <tr><th>Starting Balance (ZAR)</th><td>${g.formatNumber(number: expenseUser?.startingBalance, format: '###,##0.00')}</td></tr>
        </table>
        <div class="actions" style="margin-top:1em;">
            <g:link class="btn btn-primary" controller="expenseUser" action="edit" id="${expenseUser.id}">Edit</g:link>
            <g:link class="btn btn-info" controller="transaction" action="index" params="[userId: expenseUser.id]">View Transactions</g:link>
            <g:link class="btn btn-secondary" controller="expenseUser" action="index">Back to Users</g:link>
        </div>
    </body>
</html>
