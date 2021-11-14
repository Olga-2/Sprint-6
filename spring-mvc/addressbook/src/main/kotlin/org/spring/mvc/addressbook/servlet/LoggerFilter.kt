package org.spring.mvc.addressbook.servlet

import org.spring.mvc.addressbook.models.InitialData
import org.spring.mvc.addressbook.models.RequestLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.*
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest

@Component
class LoggerFilter(@Autowired initialData: InitialData): HttpFilter() {

    val logRequest = initialData.allRequestLog

    @Order(2)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val auth = (request as HttpServletRequest ).cookies.find { it.name.equals("auth")}

        if (auth != null)
            logRequest.put(
                LocalDateTime.now(), RequestLog(auth.value, request.requestURI))
        chain!!.doFilter(request, response)
    }

}
