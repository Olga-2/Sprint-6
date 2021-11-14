package org.spring.mvc.addressbook.servlet

import org.spring.mvc.addressbook.models.InitialData
import org.spring.mvc.addressbook.models.RequestLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggerFilter(@Autowired initialData: InitialData): HttpFilter() {

    val logRequest = initialData.allRequestLog

    @Order(2)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        var auth: String? = null
        if ((request as HttpServletRequest ).cookies != null) {
            val c = request.cookies.filter { it.name.equals("auth") }.firstOrNull()
            auth = if ( c != null) c.value else null
        }
        else
            if ((response as HttpServletResponse).containsHeader("Set_Cookie")) response.getHeaders("Set_Cookie").find {"auth".equals(it) }
        if (auth != null)
            logRequest.put(
                LocalDateTime.now(), RequestLog(auth, request.requestURI))
        chain!!.doFilter(request, response)
    }

}
