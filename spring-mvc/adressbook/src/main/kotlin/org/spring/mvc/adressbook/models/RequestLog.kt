package org.spring.mvc.adressbook.models

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

data class RequestLog(val login: String, val request: String){
}

//@Component
//class Logger (requestLog: RequestLog){
//
//    val loggerAddressBook = ConcurrentHashMap <LocalDateTime, RequestLog>()

//}