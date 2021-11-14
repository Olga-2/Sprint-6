package org.spring.mvc.addressbook.models

data class RequestLog(val login: String, val request: String){
}

class Message(text: String){
    val message = text
}