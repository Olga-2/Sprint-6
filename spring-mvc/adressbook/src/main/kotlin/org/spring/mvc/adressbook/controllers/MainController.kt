package org.spring.mvc.adressbook.controllers

import org.spring.mvc.adressbook.models.Address
import org.spring.mvc.adressbook.models.RequestLog
import org.spring.mvc.adressbook.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller("/app")
class MainController {

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

    @Value("\${error.message}")
    private val errorMessage: String? = null

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST, RequestMethod.GET])
    fun getAuth(
        model: Model, @ModelAttribute("User") user: User,
        request: HttpServletRequest, response: HttpServletResponse
    ): String {

        val user = users.find { it.login.equals(user.login, true) && it.password.equals(user.password) }

        if (user != null) {
            response.addCookie(Cookie("auth", "${user.login}_${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}"));
            allRequestLog.put(LocalDateTime.now(), RequestLog(user.login.orEmpty(), request.requestURL.toString()))
               return "redirect:/app/list?fullName"
        }
        return "authView"
    }

    @RequestMapping(value = ["/app/list"], method = [RequestMethod.GET, RequestMethod.POST])
    fun getAddressList(model: Model, @RequestParam fullName : String, request: HttpServletRequest): String {
        if (!accept(request))  return "authView"

       if  (request.method.equals(RequestMethod.POST.name, true))
        if (fullName != null)
            return "redirect:/app/list/${fullName}"
        model.addAttribute("addresses", addresses)
        model.addAttribute("fullName", "")

        return "addressList"
    }

    @RequestMapping(value = ["/app/add"], method = [RequestMethod.POST, RequestMethod.GET])
    fun addAddress(
        model: Model,
        @ModelAttribute("Address") address: Address,
        request: HttpServletRequest): String {

        if (!accept(request))  return "authView"

        val fio = address.fullName
        val addr = address.address
        if (fio != null && fio.length > 0
            && addr != null && addr.length > 0)
         {
            addresses.add(Address(fio, addr))
            return "redirect:/app/list"
        }
        model.addAttribute("errorMessage", errorMessage);
        return "addAddress";
    }

    @GetMapping(value = ["/app/{id}/view"])
    fun getAddress(model: Model, request: HttpServletRequest, @PathVariable id: Int): String {
        if (!accept(request))  return "authView"
        if (!(id in addresses.indices)) return "errorPage"
        val address = addresses[id]
        model.addAttribute("address", address)
        return "addressView"

    }

    @RequestMapping(value = ["/app/{id}/delete"], method = [RequestMethod.POST])
    fun deleteAddress(model: Model, request: HttpServletRequest, @PathVariable id: Int): String {
        if (!accept(request))  return "authView"
        if (!(id in addresses.indices)) return "errorPage"
        addresses.removeAt(id)
        model.addAttribute("addresses", addresses)

        return "redirect:/app/list?fullName"
    }

    @RequestMapping(value = ["/app/{id}/edit"], method = [RequestMethod.POST, RequestMethod.GET])
    fun editAddress(model: Model, request: HttpServletRequest,
                    @ModelAttribute("Address") address: Address,
                    @PathVariable id: Int): String {
        if (!accept(request))  return "authView"
        if (!(id in addresses.indices)) return "errorPage"

        if (address.fullName == null && address.address == null ) {
            model.addAttribute("address",  addresses[id])
            return "addressEdit"
        }
        val fio = address.fullName
        val addr = address.address

        if (fio != null && fio.length > 0
            && addr != null && addr.length > 0)
            addresses[id] = Address(fio, addr)
        model.addAttribute("addresses", addresses)
        return "redirect:/app/list?fullName"
    }

    @RequestMapping(value = ["/app/list/{fio}"],  method = [RequestMethod.GET])
    fun findAddress(model: Model, request: HttpServletRequest, @PathVariable fio: String): String {
        if (!accept(request))  return "authView"
        val address = addresses.find { it.fullName.equals(fio, true)}
        val id = if (address == null)  -1 else addresses.indexOf(address)
        if (!(id in addresses.indices)) return "errorPage"
        model.addAttribute("address", address)
        model.addAttribute("id", id)
        return "addressFind"
    }

    @RequestMapping(value = ["/errorPage"],  method = [RequestMethod.GET])
    fun errorPage(): String {
        return "errorPage"
    }

    private fun accept(request: HttpServletRequest): Boolean {
        val auth = request.cookies.find { it.name.equals("auth")}
        if (auth != null
            && (LocalDateTime.parse(auth.value.split("_")[1], ISO_LOCAL_DATE_TIME).isBefore(LocalDateTime.now()))) {
            allRequestLog.put(LocalDateTime.now(), RequestLog(auth.value.split("_")[0], request.requestURL.toString()))
            return true
        }
        else return false
    }
}