<%@ page import="expensesheet.ExpenseUser" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Create User</title>
    </head>
    <body>
        <h1>Create Expense User</h1>
        <g:if test="${flash.message}">
            <div class="alert alert-info">${flash.message}</div>
        </g:if>
        <g:hasErrors bean="${expenseUser}">
            <div class="alert alert-danger">
                <ul class="errors">
                    <g:eachError bean="${expenseUser}" var="error">
                        <li><g:message error="${error}" /></li>
                    </g:eachError>
                </ul>
            </div>
        </g:hasErrors>
        <g:form controller="expenseUser" action="save">
            <div class="form-group">
                <label for="name">Name</label>
                <g:textField name="name" value="${expenseUser?.name}" class="form-control" required="required"/>
            </div>
            <div class="form-group">
                <label for="startingBalance">Starting Balance (ZAR)</label>
                <g:textField name="startingBalance" value="${expenseUser?.startingBalance}" class="form-control" required="required"/>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </g:form>
    </body>
</html>
