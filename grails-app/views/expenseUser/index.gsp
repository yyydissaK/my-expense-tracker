<%@ page import="expensesheet.ExpenseUser" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Expense Users</title>
    </head>
    <body>
        <h1>Expense Users</h1>
        <div class="actions" style="margin-bottom:1em;">
            <g:link class="button" action="create">Create New User</g:link>
        </div>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Starting Balance (ZAR)</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${expenseUserList}" var="user">
                    <tr>
                        <td>${fieldValue(bean: user, field: 'name')}</td>
                        <td>${g.formatNumber(number: user.startingBalance, format: '###,##0.00')}</td>
                        <td>
                            <g:link controller="expenseUser" action="show" id="${user.id}" class="btn btn-sm btn-primary">View</g:link>
                            <g:link controller="expenseUser" action="edit" id="${user.id}" class="btn btn-sm btn-secondary">Edit</g:link>
                            <g:link controller="transaction" action="index" params="[userId: user.id]" class="btn btn-sm btn-info">Transactions</g:link>
                        </td>
                    </tr>
                </g:each>
            </tbody>
        </table>
        <div class="pagination">
            <g:paginate controller="expenseUser" action="index" total="${expenseUserCount ?: 0}" />
        </div>
    </body>
</html>
