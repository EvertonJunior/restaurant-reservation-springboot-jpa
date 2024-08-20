package com.restauranteexemplo.reserva.resources.integrationtests;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restauranteexemplo.reserva.config.TestConfig;
import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.integrationtest.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClientResourceIntegrationTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static Client client;

	@BeforeAll
	static void setup() {
		// Given / Arrange
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		specification = new RequestSpecBuilder().setBasePath("/clients").setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		client = new Client(null, "Everton", "everton@gmail.com", "everton1234");

	}

	@Test
	@Order(1)
	void testGivenClientObject_WhenInsertClient_ThenReturnAClientObject()
			throws JsonMappingException, JsonProcessingException {
		// GIVEN
		var content = given().spec(specification).contentType(TestConfig.CONTENT_TYPE_JSON).body(client).when().post()
				.then().statusCode(201).extract().body().asString();
		// WHEN
		Client insertClient = objectMapper.readValue(content, Client.class);
		client = insertClient;
		// THEN
		assertNotNull(insertClient);
		assertNotNull(insertClient.getName());
		assertEquals("Everton", client.getName());
	}

	

	@Test
	@Order(2)
	void testGivenClientObject_WhenFindById_ThenReturnClientObject()
			throws JsonMappingException, JsonProcessingException {
		// GIVEN
		var content = given().spec(specification).pathParam("id", client.getId()).when().get("{id}").then()
				.statusCode(200).extract().body().asString();
		// WHEN
		Client foundClient = objectMapper.readValue(content, Client.class);
		// THEN
		assertNotNull(foundClient);
		assertEquals("Everton", foundClient.getName());
	}
	
	

	@Test
	@Order(3)
	void testGivenClientsList_WhenFindAll_ThenReturnClientsList() throws JsonMappingException, JsonProcessingException {
		// GIVEN
		Client clientTwo = new Client(null, "Eduardo", "eduardo@gmail.com", "eduardo1234");
		given().spec(specification).contentType(TestConfig.CONTENT_TYPE_JSON).body(clientTwo).when().post().then()
				.statusCode(201);
		var content = given().spec(specification).when().get().then().statusCode(200).extract().body().asString();
		// WHEN
		Client[] myArray = objectMapper.readValue(content, Client[].class);
		List<Client> clients = Arrays.asList(myArray);
		Client client1 = clients.get(1);
		// THEN
		assertEquals(2, clients.size());
		assertEquals("Eduardo", client1.getName());
	}
	
	@Test
	@Order(4)
	void testGivenClientObject_WhenUpdatedClient_ThenReturnUpdatedClientObject()
			throws JsonMappingException, JsonProcessingException {
		// GIVEN
		client.setName("Jose");
		client.setEmail("jose@gmai.com");
		var content = given().spec(specification).pathParam("id", client.getId()).contentType(TestConfig.CONTENT_TYPE_JSON).body(client).when().put("{id}")
				.then().statusCode(200).extract().body().asString();
		// WHEN
		Client updatedClient = objectMapper.readValue(content, Client.class);
		client = updatedClient;
		// THEN
		assertNotNull(updatedClient);
		assertEquals("Jose", updatedClient.getName());
	}
	
	@Test
	@Order(5)
	void testGivenClientObject_WhenDeleteById_ThenReturnNoContent() {
		//GIVEN
		given().spec(specification).pathParam("id", client.getId()).when().delete("{id}").then().statusCode(204);
		//WHEN
		//THEN
	}
}
