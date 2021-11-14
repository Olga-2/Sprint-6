package org.spring.mvc.addressbook

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
class AddressbookApplication

fun main(args: Array<String>) {

	val a=
		runApplication<AddressbookApplication>()

//
	val beanNames: Array<String> = a.getBeanDefinitionNames()
//	Arrays.sort(beanNames)
//	for (beanName in beanNames) {
//		println(beanName)
//	}
}
