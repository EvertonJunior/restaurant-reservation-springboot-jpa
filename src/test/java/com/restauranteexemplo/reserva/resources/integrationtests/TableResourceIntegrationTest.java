package com.restauranteexemplo.reserva.resources.integrationtests;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParseException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import com.restauranteexemplo.reserva.config.TestConfig;
import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.integrationtest.testcontainers.AbstractIntegrationTest;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TableResourceIntegrationTest extends AbstractIntegrationTest{

	private static RequestSpecification specification;
	private static ObjectMapper mapper;
	
	private static Table table;
	
	@BeforeAll
	static void setup() {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		specification = new RequestSpecBuilder().setBasePath("/tables").setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		table = new Table(null,6);
	}
	
	@Test
	@Order(1)
	void testGivenTableObject_WhenInsert_ThenReturnTableObject() throws JsonParseException, JsonMappingException, IOException {
		// GIVEN
		var content = given().spec(specification).contentType(TestConfig.CONTENT_TYPE_JSON).body(table).when().post().then().statusCode(201).extract().body().asString();
		// WHEN
		Table savedTable = mapper.readValue(content, Table.class);
		table = savedTable;
		// THEN
		assertNotNull(savedTable);
		assertEquals(6, savedTable.getSeats());
	}

	@Test
	@Order(2)
	void testGivenTablesList_WhenFindAll_ThenReturnTablesList() throws JsonParseException, JsonMappingException, IOException {
		// GIVEN
		Table tableTwo = new Table(null, 8);
		given().spec(specification).contentType(TestConfig.CONTENT_TYPE_JSON).body(tableTwo).when().post().then().statusCode(201);
		var content = given().spec(specification).when().get().then().statusCode(200).extract().body().asString();
		// WHEN
		Table[] tableArray = mapper.readValue(content, Table[].class);
		List<Table> tables = Arrays.asList(tableArray);
		Table tableOne = tables.get(0);
		// THEN
		assertNotNull(tables);
		assertNotNull(tableOne);
		assertEquals(6, tableOne.getSeats());
	}

	@Test
	@Order(3)
	void testGivenTableId_WhenFindById_ThenReturnTableObject() throws JsonParseException, JsonMappingException, IOException {
		// GIVEN
		var content = given().spec(specification).pathParam("id", table.getId()).body(table).when().get("{id}").then().statusCode(200).extract().body().asString();
		// WHEN
		Table foundTable = mapper.readValue(content, Table.class);
		table = foundTable;
		// THEN
		assertNotNull(foundTable);
	}

	@Test
	@Order(4)
	void testGivenInvalidId_WhenFindById_ThenReturnNotFoundException() {
		// GIVEN
		given().spec(specification).pathParam("id", anyLong()).body(ResourceNotFoundException.class).when().get("{id}").then().statusCode(404);
		// WHEN
		// THEN
	}

	@Test
	@Order(5)
	void testGivenUpdatedTableObject_WhenUpdate_ThenReturnUpdatedTableObject() throws JsonParseException, JsonMappingException, IOException {
		// GIVEN
		table.setSeats(10);
		var content = given().spec(specification).pathParam("id", table.getId()).contentType(TestConfig.CONTENT_TYPE_JSON).body(table).when().put("{id}").then().statusCode(200).extract().body().asString();
		// WHEN
		Table savedTable = mapper.readValue(content, Table.class);
		table = savedTable;
		// THEN
		assertNotNull(savedTable);
		assertEquals(10, table.getSeats());
	}

	@Test
	@Order(6)
	void testDeletedTable_WhenDeleteById_ThenReturnDeletedTable() {
		// GIVEN
		given().spec(specification).pathParam("id", table.getId()).when().delete("{id}").then().statusCode(204);
		// WHEN
		// THEN
	}

}
