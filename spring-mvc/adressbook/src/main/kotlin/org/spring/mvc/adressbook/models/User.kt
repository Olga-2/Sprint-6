package org.spring.mvc.adressbook.models

import org.springframework.stereotype.Component

data class User(
    var login: String?,
    var password: String?,
) {
}

@Component
class Users
{
    val users = listOf(
        User("bob", "Bob123"),
        User("miki", "Mouse1")
    )

    public fun geLoginUsers() = users
}