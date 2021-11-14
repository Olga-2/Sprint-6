package org.spring.mvc.addressbook

import com.fasterxml.jackson.databind.ObjectMapper
import net.minidev.json.JSONObject
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.spring.mvc.addressbook.models.Address
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.mock.http.client.MockClientHttpRequest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import kotlin.test.assertEquals


@SpringBootTest
@AutoConfigureMockMvc
class AdressbookApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@Test
	fun testLoginPost() {
		mockMvc.perform(
			MockMvcRequestBuilders.post("/login")
				.param("login","bob")
				.param("password", "Bob123"))
			.andDo { println() }
			.andExpect(cookie().exists("auth"))

	}
	@Test
	fun testAddandOneRecordView() {

		val cookie = getCookie()

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
			MockMvcRequestBuilders.get("/app/list/Oscar")
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

		val cookie = getCookie()

		mockMvc.perform(
			MockMvcRequestBuilders.get("/app/list")
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

		val cookie = getCookie()

		mockMvc.perform(
			MockMvcRequestBuilders.get("/app/5/view")
				.cookie(cookie)
		)
			.andDo { println() }
			.andExpect ( status().isOk() )
			.andExpect ( content().string(containsString("Address not exist!")) )

	}

	@Test
	fun testFind() {

		val cookie = getCookie()

		mockMvc.perform(
			MockMvcRequestBuilders.get("/app/list/Ivanov Ivan")
				.cookie(cookie)
		)
			.andDo { println() }
			.andExpect (status().isOk() )
			.andExpect ( view().name("addressFind") )
			.andExpect ( content().string(containsString("Edit")) )
			.andExpect ( content().string(containsString("View")) )
			.andExpect ( content().string(containsString("Delete")) )
		}
	fun getCookie() =
		mockMvc.perform(
			MockMvcRequestBuilders.post("/login")
				.param("login","bob")
				.param("password", "Bob123"))
			.andReturn().response.getCookie("auth")
	}

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class RestApplicationTest{
//
//	@LocalServerPort
//	private var port: Int = 0
//
//	@Autowired
//	private lateinit var restTemplate: TestRestTemplate
//
//	private fun url(s: String): String{
//		return "http://localhost:${port}/${s}"
//	}
//
//	@Test
//	fun getLogin() {
//		val params = HashMap<String, String>()
//		params.put("login", "bob")
//		params.put("password", "Bob123")
//		val entity = HttpEntity<Any?>(params)
//
//
//		val resp = restTemplate.exchange()
//		postForEntity(url("/login"), entity, String::class.java, params)
//
//		assertEquals(HttpStatus.OK, resp.statusCode)
//		assertThat(resp.headers.get("Set-Cookie").toString(), containsString("auth"))
//	}
//

//	@Test
//	fun getLogin() {
////		val resp = restTemplate.postForEntity(
////			url("/api/login"),
////			HttpEntity<Any>(User("Bob", "Bob123")), User::class.java
////		)
////		assertEquals(HttpStatus.OK, resp.statusCode)
////		assertThat(resp.headers.get("Set-Cookie").toString(), containsString("auth"))
////	}
//

//	@Test
//	fun getList() {
//
//		var resp = restTemplate.postForEntity(
//			url("/api/login"),
//			HttpEntity<Any>(User("Bob", "Bob123")), User::class.java)
//
//		val headers = HttpHeaders()
//		headers.add("Cookie", resp.headers.getFirst("Set-Cookie"))
//		class ListOfAddress : ParameterizedTypeReference<List<Address>>(){}
//
//		val resp1 = restTemplate.exchange(url("/api/list"), HttpMethod.GET,
//			HttpEntity<String>(headers), ListOfAddress())
//		assertNotNull(resp1)
//		assertEquals(200, resp1.statusCodeValue)
//		assertTrue( resp1.body!!.size == 4 )
//	}
//	@Test
//	fun getViewId() {
//
//		var resp = restTemplate.postForEntity(
//			url("/api/login"),
//			HttpEntity<Any>(User("Bob", "Bob123")), User::class.java)
//
//		val headers = HttpHeaders()
//		headers.add("Cookie", resp.headers.getFirst("Set-Cookie"))
//		class ListOfAddress : ParameterizedTypeReference<List<Address>>(){}
//
//		val resp1 = restTemplate.exchange(url("/api/1/view"), HttpMethod.GET,
//			HttpEntity<String>(headers), Address::class.java)
//		assertNotNull(resp1)
//		assertEquals(200, resp1.statusCodeValue)
//		assertEquals( "Petrov Ivan", resp1.body!!.fullName )
//		assertEquals( "Nikitina,  14", resp1.body!!.address )
//	}



//}


