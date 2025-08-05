<!doctype html>
<html>
    <head>
        <!-- Global 404 page shown when no controller/action matches the request -->
        <title>Page Not Found</title>
        <meta name="layout" content="main">
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <ul class="col-12 errors">
                    <li>Error: Page Not Found (404)</li>
                    <!-- Display the forward URI if available for easier debugging -->
                    <li>Path: ${request.forwardURI}</li>
                </ul>
            </section>
        </div>
    </div>
    </body>
</html>
