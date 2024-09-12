package com.restauranteexemplo.reserva.resources.integrationtests;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDateTime;
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
import com.restauranteexemplo.reserva.entities.Reservation;
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
class ReservationResourceIntegrationTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;

	private static ObjectMapper mapper;

	private static Reservation reservation;
;

	@BeforeAll
	static void setup() {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		specification = new RequestSpecBuilder().setBasePath("/reservations").setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		Client client = new Client(null, "Everton", "everton@gmail.com", "everton1234");
		Table table = new Table(null, 6);
		reservation = new Reservation(null, client, LocalDateTime.parse("2024-08-30T14:35:45"),
				LocalDateTime.parse("2024-08-30T17:35:45"), table);
	}

	@Test
	@Order(1)
	void testGivenSavedReservation_WhenInsert_ThenReturnSavedReservationObject() throws JsonMappingException, JsonProcessingException{
		// GIVEN

		var content = given().spec(specification).contentType(TestConfig.CONTENT_TYPE_JSON)
				.body(reservation).when().post().then().statusCode(201).extract().body().asString();
		// WHEN
		Reservation savedReservation = mapper.readValue(content, Reservation.class);
		reservation = savedReservation;
		// THEN
		assertNotNull(savedReservation);
	}

	@Test
	@Order(2)
	void testGivenReservationsList_WhenFindAll_ThenReturnReservationsList() throws JsonMappingException, JsonProcessingException {
		// GIVEN
		Client clientTwo = new Client(null, "Jose", "jose@gmail.com", "jose1234");
		Table tableTwo = new Table(null, 10);
		Reservation reservationTwo = new Reservation(null, clientTwo, LocalDateTime.parse("2024-08-30T22:35:45"),
				LocalDateTime.parse("2024-08-30T20:35:45"), tableTwo);
		given().spec(specification).contentType(TestConfig.CONTENT_TYPE_JSON).body(reservationTwo).when().post().then().statusCode(201);
		var content = given().spec(specification).when().get().then().statusCode(200)
				.extract().body().asString();
		// WHEN
		Reservation[] reservationArray = mapper.readValue(content, Reservation[].class);
		List<Reservation> reservations = Arrays.asList(reservationArray);
		Reservation reservationOne = reservations.get(0);
		// THEN
		assertNotNull(reservations);
		assertEquals(2, reservations.size());
		assertNotNull(reservationOne.getId());
	}

	@Test
	@Order(3)
	void testGivenReservationObject_WhenFindById_ThenReturnReservationObject() throws JsonMappingException, JsonProcessingException{
		// GIVEN
		var content = given().spec(specification).pathParam("id", reservation.getId())
				.body(reservation).when().get("{id}").then().statusCode(200).extract().body().asString();
		// WHEN
		Reservation foundReservation = mapper.readValue(content, Reservation.class);
		reservation = foundReservation;
		// THEN
		assertNotNull(foundReservation);
		assertNotNull(foundReservation.getId());
	}

	@Test
	@Order(4)
	void testGivenInvalidReservationId_WhenFindById_ThenReturnResourceNotFoundException() {
		// GIVEN
		given().spec(specification).pathParam("id", anyLong())
				.body(ResourceNotFoundException.class).when().get("{id}").then().statusCode(404);
		// WHEN
		// THEN
	}

	@Test
	@Order(5)
	void testDeletedReservation_WhenDelete_ThenReturnNoContent() {
		// GIVEN
		given().spec(specification).pathParam("id", reservation.getId()).when().delete("{id}")
				.then().statusCode(204);
		// WHEN
		// THEN
	}

}
