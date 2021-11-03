package org.spring.mvc.adressbook.controllers

import org.spring.mvc.adressbook.models.Address
import org.spring.mvc.adressbook.models.Message
import org.spring.mvc.adressbook.models.RequestLog
import org.spring.mvc.adressbook.models.User
import org.spring.mvc.adressbook.service.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.StreamingHttpOutputMessage
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
@RequestMapping("/api")
class RestController() {

    @Autowired
    private lateinit var service: Service

    private var allRequestLog = ConcurrentHashMap<LocalDateTime, RequestLog>()

    @PostMapping("/login")
    fun login(@RequestBody user: User): ResponseEntity<Any> {

        allRequestLog.put(LocalDateTime.now(), RequestLog(user.login.orEmpty(), "/api/login"))

        if (service.getLogin(user) != null) {
            val headers = HttpHeaders()
            headers.add("Set-Cookie", "auth=${user.login.orEmpty()}_${LocalDateTime.now()}" )

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Message("Login success"))
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Message("Access denied"))
       }

    @GetMapping(value = ["/list"])
    fun getAddressList(@CookieValue("auth") cookie: Cookie): ResponseEntity<Any> {
        if (!accept(cookie)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Message("Access denied"))
        return ResponseEntity.status(HttpStatus.OK).body(service.getList())
     }

    @GetMapping(value = ["/list/{fio}"])
    fun getFindAddressByFio(@CookieValue("auth") cookie: Cookie, @PathVariable fio : String): ResponseEntity<Any> {
        if (!accept(cookie)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Message("Access denied"))
        var address = service.getAddressByFio(fio)
        if (address != null)
            return ResponseEntity.status(HttpStatus.OK).body(service.getAddressByFio(fio))
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("Not found"))
    }

    @PutMapping(value = ["/add"])
    fun addAddress(@CookieValue("auth") cookie: Cookie, @RequestBody address: Address): ResponseEntity<Any> {
        if (!accept(cookie)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Message("Access denied"))
        try {
            service.addAddress(address)
            return ResponseEntity.status(HttpStatus.OK).body(Message("Address added"))
        }
        catch (e: Exception)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message("Error add address"))
        }
    }
    @GetMapping(value = ["/{id}/view"])
    fun getAddress(@CookieValue("auth") cookie: Cookie, @PathVariable id: Int): ResponseEntity<Any> {
        if (!accept(cookie)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Message("Access denied"))

        try {
            var address = service.getAddressById(id)
            if (address != null)
                return ResponseEntity.status(HttpStatus.OK).body(address)
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("Not found"))
        }
        catch (e: IndexOutOfBoundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message("Error addresses id"))
        }
    }
    @DeleteMapping(value = ["/{id}/delete"])
    fun deleteAddress(@CookieValue("auth") cookie: Cookie, @PathVariable id: Int): ResponseEntity<Any> {
        if (!accept(cookie)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied")
        try {
            service.deleteAddress(id)
            return ResponseEntity.status(HttpStatus.OK).body(Message("Address removed"))
        } catch (e: IndexOutOfBoundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Message("Error addresses id"))
        }
    }

    @PutMapping(value = ["/{id}/edit"])
    fun editAddress(@CookieValue("auth") cookie: Cookie, @PathVariable id: Int, @RequestBody address: Address): ResponseEntity<Any> {
        if (!accept(cookie)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Message("Access denied"))
        var address = service.editAddress(id, address)
        try {
            if (address != null)
                return ResponseEntity.status(HttpStatus.OK).body(service.getAddressById(id))
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message("Not found"))
        } catch (e: IndexOutOfBoundsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message("Error addresses id"))
        }
    }


    fun accept(auth: Cookie): Boolean {
        if (auth != null
            && (LocalDateTime.parse(auth.value.split("_")[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME).isBefore(
                LocalDateTime.now()
            ))
        ) {
            allRequestLog.put(
                LocalDateTime.now(),
                RequestLog(auth.value.split("_")[0], "request.requestURL".toString())
            )
            return true
        } else return false
    }
}