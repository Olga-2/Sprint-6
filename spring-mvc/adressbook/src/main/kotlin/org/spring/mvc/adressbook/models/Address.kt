package org.spring.mvc.adressbook.models

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

data class Address(val fullName: String?, val address: String?) {
}
//@Component
//class AddressBook (address: Address){
//
//    val addressList = MutableList() <LocalDateTime, RequestLog>()
//
//}
