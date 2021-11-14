package org.spring.mvc.addressbook.models

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

data class User(
    var login: String?,
    var password: String?,
) {
}

@Component
class Users(@Autowired private val initialData: InitialData)
{
    fun geLoginUsers() = initialData.users
}