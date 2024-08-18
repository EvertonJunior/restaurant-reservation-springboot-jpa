package com.restauranteexemplo.reserva.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.entities.Reservation;
import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.services.ClientService;
import com.restauranteexemplo.reserva.services.ReservationService;
import com.restauranteexemplo.reserva.services.TableService;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@WebMvcTest
class ReservationResourceTest {

	@Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
	@MockBean
	private ReservationService service;;
	@MockBean
	private ClientService clientService;
	@MockBean
	private TableService tableService;
	
	private Reservation reservation;
	
	@BeforeEach
	void setup() {
		Client client = new Client(1L, "Everton", "everton@gmail.com", "everton1234");
		Table table = new Table(1L, 4);
		reservation = new Reservation(1L, client, LocalDateTime.parse("2024-08-30T14:35:45"),
				LocalDateTime.parse("2024-08-30T17:35:45"), table);
	}

	@Test
	@DisplayName("JUnit test for given saved Reservation object when insert reservation then return saved Reservation object")
	void testGivenSavedReservation_WhenInsert_ThenReturnSavedReservationObject() throws JsonProcessingException, Exception {
		//GIVEN
		when(service.insert(any(Reservation.class))).thenReturn(reservation);
		//WHEN
		ResultActions response = mockMvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(reservation)));
		//THEN
	      response.andDo(print()).
          andExpect(status().isCreated())
          .andExpect(jsonPath("$.client.name", is(reservation.getClient().getName())))
          .andExpect(jsonPath("$.table.seats", is(reservation.getTable().getSeats())));

	}
	
	@Test
	@DisplayName("JUnit test for given Reservations list when findAll then return reservations list")
	void testGivenReservationsList_WhenFindAll_ThenReturnReservationsList() throws JsonProcessingException, Exception {
		// GIVEN
		Client clientTwo = new Client(2L, "Everton", "everton@gmail.com", "everton1234");
		Table tableTwo = new Table(2L, 4);
		Reservation reservationTwo = new Reservation(2L, clientTwo, LocalDateTime.parse("2024-08-30T14:35:45"),
				LocalDateTime.parse("2024-08-30T17:35:45"), tableTwo);
		List<Reservation> reservations = new ArrayList<>();
		reservations.addAll(Arrays.asList(reservation, reservationTwo));
		when(service.findAll()).thenReturn(reservations);
		// WHEN
		ResultActions response = mockMvc.perform(get("/reservations").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reservations)));
		// THEN
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(reservations.size())));
	}
	
	@Test
	@DisplayName("JUnit test for given Reservation Object when findById then return Reservation Object")
	void testGivenReservationObject_WhenFindById_ThenReturnReservationObject()
			throws JsonProcessingException, Exception {
		// GIVEN
		Long reservationId = 1L;
		when(service.findById(reservationId)).thenReturn(reservation);
		// WHEN
		ResultActions response = mockMvc.perform(get("/reservations/{id}", reservationId).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(reservation)));
		// THEN
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.client.name", is(reservation.getClient().getName())));
	}
	
	@Test
	@DisplayName("JUnit test for given invalid reservationId when FindByIDd then Return Resource not found exception")
	void testGivenInvalidReservationId_WhenFindById_ThenReturnResourceNotFoundException() throws JsonProcessingException, Exception {
		//GIVEN
		Long reservationId = 1L;
		when(service.findById(reservationId)).thenThrow(ResourceNotFoundException.class);
		//WHEN
		ResultActions response = mockMvc.perform(get("/reservations/{id}", reservationId));
		//THEN
		response.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("JUnit test for given deleted Reservation object when delete then return deleted reservation and no content")
	void testDeletedReservation_WhenDelete_ThenReturnNoContent() throws Exception {
		//GIVEN
		Long reservationId = 1L;
		doNothing().when(service).delete(reservationId);
		//WHEN
		ResultActions response = mockMvc.perform(delete("/reservations/{id}", reservationId));
		//THEN
		response.andExpect(status().isNoContent());
	}
}
