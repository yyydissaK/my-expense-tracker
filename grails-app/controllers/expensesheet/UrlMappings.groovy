package expensesheet

/**
 * URL mappings for the system.
 * The base URL makes sure to redirect to the list of users so that a visitor can immediately select or create a user.
 */
class UrlMappings {

    static mappings = {
        "/"(controller: 'expenseUser', action: 'index')
        "/transactions"(controller: 'transaction', action: 'index')
        "/$controller/$action?/$id?"(){
            constraints {
                // apply constraints here if needed
            }
        }
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}