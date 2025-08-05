<%@ page import="expensesheet.Transaction" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Create Transaction</title>
    </head>
    <body>
        <h1>Create Transaction for ${user?.name}</h1>
        <g:hasErrors bean="${transaction}">
            <div class="alert alert-danger">
                <ul class="errors">
                    <g:eachError bean="${transaction}" var="error">
                        <li><g:message error="${error}" /></li>
                    </g:eachError>
                </ul>
            </div>
        </g:hasErrors>
        <g:form controller="transaction" action="saveTransaction">
            <g:hiddenField name="userId" value="${user?.id}"/>
            <div class="form-group">
                <label for="description">Description</label>
                <g:textField name="description" value="${transaction?.description}" class="form-control" required="required"/>
            </div>
            <div class="form-group">
                <label for="amount">Amount (ZAR)</label>
                <g:textField name="amount" value="${transaction?.amount}" class="form-control" required="required"/>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </g:form>
    </body>
</html>
