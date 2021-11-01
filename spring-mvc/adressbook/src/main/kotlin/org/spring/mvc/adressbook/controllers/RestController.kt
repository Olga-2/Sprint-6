package org.spring.mvc.adressbook.controllers

import org.spring.mvc.adressbook.models.Address
import org.spring.mvc.adressbook.models.RequestLog
import org.spring.mvc.adressbook.models.User
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class RestController {

        val allRequestLog = ConcurrentHashMap<LocalDateTime, RequestLog>()

        val users = listOf(
            User("bob", "Bob123"),
            User("miki", "Mouse1")
        )

        val addresses = mutableListOf(
            Address("Ivanov Ivan", "Matrosova,  45"),
            Address("Petrov Ivan", "Nikitina,  14"),
            Address("Sidorov Vladimir", "Orechovajа,  26"),
            Address("Ohlabyistin Ivan", "Petrovaka, 38")
        )


        private fun accept(request: HttpServletRequest): Boolean {
            val auth = request.cookies.find { it.name.equals("auth")}
            if (auth != null
                && (LocalDateTime.parse(auth.value.split("_")[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME).isBefore(
                    LocalDateTime.now()))) {
                allRequestLog.put(LocalDateTime.now(), RequestLog(auth.value.split("_")[0], request.requestURL.toString()))
                return true
            }
            else return false
        }



}