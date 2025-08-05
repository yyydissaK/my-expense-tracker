<!doctype html>
<html>
    <head>
        <!-- Simple global error page used for HTTP 500 responses -->
        <title>Error</title>
        <meta name="layout" content="main">
        <!-- Include the errors stylesheet in development for better styling -->
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <div class="col-12">
                    <g:if env="development">
                        <!-- Show detailed exception information in development -->
                        <g:if test="${Throwable.isInstance(exception)}">
                            <g:renderException exception="${exception}" />
                        </g:if>
                        <g:elseif test="${request.getAttribute('javax.servlet.error.exception')}">
                            <g:renderException exception="${request.getAttribute('javax.servlet.error.exception')}" />
                        </g:elseif>
                        <g:else>
                            <ul class="errors">
                                <li>An error has occurred</li>
                                <li>Exception: ${exception}</li>
                                <li>Message: ${message}</li>
                                <li>Path: ${path}</li>
                            </ul>
                        </g:else>
                    </g:if>
                    <g:else>
                        <!-- In production just show a generic error message -->
                        <ul class="errors">
                            <li>An internal server error has occurred. Please try again later.</li>
                        </ul>
                    </g:else>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
