package com.restauranteexemplo.reserva.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.services.ClientService;
import com.restauranteexemplo.reserva.services.ReservationService;
import com.restauranteexemplo.reserva.services.TableService;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@WebMvcTest
class TableResourceTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private ClientService clientServic;
	@MockBean
	private TableService service;
	@MockBean
	private ReservationService reservationService;

	private Table table;

	@BeforeEach
	void setup() {
		table = new Table(1L, 4);
	}

	@Test
	@DisplayName("JUnit test for given Table object when insert then return insert Table Object")
	void testGivenTableObject_WhenInsert_ThenReturnTableObject() throws JsonProcessingException, Exception {
		// GIVEN
		when(service.insert(any(Table.class))).thenReturn(table);
		// WHEN
		ResultActions response = mockMvc.perform(post("/tables").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(table)));
		// THEN
		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.seats", is(table.getSeats())));
	}

	@Test
	@DisplayName("JUnit test for given tables list when findAll then return tables list")
	void testGivenTablesList_WhenFindAll_ThenReturnTablesList() throws JsonProcessingException, Exception {
		// GIVEN
		Table tableTwo = new Table(2L, 9);
		List<Table> tables = new ArrayList<>();
		tables.addAll(Arrays.asList(table, tableTwo));
		when(service.findAll()).thenReturn(tables);
		// WHEN
		ResultActions response = mockMvc.perform(get("/tables").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(tables)));
		// THEN
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(tables.size())));
	}

	@Test
	@DisplayName("JUnit test for given TableId when findById then return Table object")
	void testGivenTableId_WhenFindById_ThenReturnTableObject() throws JsonProcessingException, Exception {
		// GIVEN
		Long tableId = 1L;
		when(service.findById(tableId)).thenReturn(table);
		// WHEN
		ResultActions response = mockMvc.perform(get("/tables/{id}",tableId).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(table)));
		// THEN
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.seats", is(table.getSeats())));
	}

	@Test
	@DisplayName("JUnit test for given invalid id when findById then return ResourceNotFoundException")
	void testGivenInvalidId_WhenFindById_ThenReturnNotFoundException() throws Exception {
		// GIVEN
		Long tableId = 1L;
		when(service.findById(tableId)).thenThrow(ResourceNotFoundException.class);
		// WHEN
		ResultActions response = mockMvc.perform(get("/tables/{id}",tableId));
		// THEN
		response.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("JUnit test for given updated Table object when update then return Updated Table")
	void testGivenUpdatedTableObject_WhenUpdate_ThenReturnUpdatedTableObject() throws JsonProcessingException, Exception {
		// GIVEN
		Long tableId = 1L;
		Table tableUpdated = new Table(null, 4);
		tableUpdated.setSeats(8);
		when(service.findById(tableId)).thenReturn(table);
		when(service.update(tableId, tableUpdated)).thenReturn(table);
		// WHEN
		ResultActions response = mockMvc.perform(put("/tables/{id}", tableId).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(tableUpdated)));
		// THEN
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.seats", is(table.getSeats())));
	}

	@Test
	@DisplayName("JUnit test for given deleted Table when deleteById then return deleted table")
	void testDeletedTable_WhenDeleteById_ThenReturnDeletedTable() throws Exception {
		// GIVEN
		Long tableId = 1L;
		doNothing().when(service).deleteById(tableId);
		// WHEN
		ResultActions response = mockMvc.perform(delete("/tables/{id}", tableId));
		// THEN
		response.andExpect(status().isNoContent());
	}

}
