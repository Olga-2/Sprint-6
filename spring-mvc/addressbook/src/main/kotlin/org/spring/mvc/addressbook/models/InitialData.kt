package org.spring.mvc.addressbook.models

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Component
class InitialData {
    public val allRequestLog = ConcurrentHashMap<LocalDateTime, RequestLog>()

    val users = setOf<User>(
        User("bob", "Bob123"),
        User("miki", "Mouse1")
    )

    val addresses = mutableListOf(
        Address("Ivanov Ivan", "Matrosova,  45"),
        Address("Petrov Ivan", "Nikitina,  14"),
        Address("Sidorov Vladimir", "Orechovajа,  26"),
        Address("Ohlabyistin Ivan", "Petrovaka, 38")
    )

}