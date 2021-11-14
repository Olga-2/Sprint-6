package org.spring.mvc.addressbook.servlet

import org.spring.mvc.addressbook.models.InitialData
import org.spring.mvc.addressbook.models.RequestLog
import org.spring.mvc.addressbook.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.Cookie
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class  AccessFilter(@Autowired initialData: InitialData): HttpFilter() {

    val users = initialData.users

    @Order(1)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val resp = (response as HttpServletResponse)

        if ("/login".equals((request as HttpServletRequest).requestURI)) {
            if ("POST".equals(request.method)) {
                val name = request.getParameter("login")
                val pass = request.getParameter("password")

                if (users.contains(User(name, pass))) {
                    resp.addCookie(
                        Cookie(
                            "auth",
                            "${name!!}_${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}"
                        )
                    )
                } else {
                    resp.sendError(403, "Access deny")

                }
            }
        } else {
            val auth = request.cookies.find { it.name.equals("auth") }
            if (auth == null
                || !(LocalDateTime.parse(auth.value.split("_")[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .isBefore(LocalDateTime.now())
                        && users.toList().map { it.login }.filter { it.equals(auth.value.split("_")[0]) }
                    .count() == 1)
            ) {
                resp.sendError(403, "Access deny")
            }
        }
        chain!!.doFilter(request, response)
    }
}
