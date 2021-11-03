package org.spring.mvc.adressbook

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.spring.mvc.adressbook.models.Address
import org.spring.mvc.adressbook.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.xml.ws.Response
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class AdressbookApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@Test
	fun testLogin() {
		mockMvc.perform(
			get("/app/login")
				.flashAttr("User", User("Bob", "Bob123"))
		)
			.andDo { println() }
			.andExpect ( status().is3xxRedirection )
			.andExpect ( redirectedUrl("/app/list?fullName") )
			.andExpect ( cookie().exists("auth") )

	}

	@Test
	fun testAddandOneRecordView() {

		val cookie = mockMvc.perform(
			MockMvcRequestBuilders.post("/app/login")
				.flashAttr("User", User("Bob", "Bob123")))
		 .andReturn().response.getCookie("auth")


		mockMvc.perform(
			MockMvcRequestBuilders.post("/app/add")
				.cookie(cookie)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.flashAttr("Address", Address("Oscar", "Tisovaya st.")))
			.andDo { println() }
			.andExpect ( status().is3xxRedirection )
			.andExpect ( redirectedUrl("/app/list?fullName") )

		mockMvc.perform(
			get("/app/list/Oscar")
				.cookie(cookie)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo { println() }
			.andExpect ( status().isOk )
			.andExpect ( content().string(containsString("Oscar")) )
			.andExpect ( content().string(containsString("Tisovaya")) )
	}

	@Test
	fun testList() {

		val cookie = mockMvc.perform(
			MockMvcRequestBuilders.post("/app/login")
				.flashAttr("User", User("Bob", "Bob123")))
		.andReturn().response.getCookie("auth")

		mockMvc.perform(
			get("/app/list")
				.param("fullName","")
				.cookie(cookie)
		)
			.andDo { println() }
			.andExpect ( status().isOk() )
			.andExpect ( view().name("addressList") )
			.andExpect ( content().string(containsString("Name")) )
			.andExpect ( content().string(containsString("Address")) )
	}

	@Test
	fun testView() {

		val cookie = mockMvc.perform(
			MockMvcRequestBuilders.post("/app/login")
				.flashAttr("User", User("Bob", "Bob123")))
			.andReturn().response.getCookie("auth")

		mockMvc.perform(
			get("/app/5/view")
				.cookie(cookie)
		)
			.andDo { println() }
			.andExpect ( status().isOk() )
			.andExpect ( content().string(containsString("Address not exist!")) )

	}

	@Test
	fun testFind() {

		val cookie = mockMvc.perform(
			MockMvcRequestBuilders.post("/app/login")
				.flashAttr("User", User("Bob", "Bob123"))
				.content("")
		).andReturn().response.getCookie("auth")

		mockMvc.perform(
				get("/app/list/Ivanov Ivan")
				.cookie(cookie)
		)
			.andDo { println() }
			.andExpect (status().isOk() )
			.andExpect ( view().name("addressFind") )
			.andExpect ( content().string(containsString("Edit")) )
			.andExpect ( content().string(containsString("View")) )
			.andExpect ( content().string(containsString("Delete")) )
		}
	}




