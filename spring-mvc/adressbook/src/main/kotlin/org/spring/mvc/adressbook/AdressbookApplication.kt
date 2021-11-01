package org.spring.mvc.adressbook

import org.spring.mvc.adressbook.models.RequestLog
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

@SpringBootApplication
@ComponentScan
class AdressbookApplication

fun main(args: Array<String>) {
	runApplication<AdressbookApplication>()
}
