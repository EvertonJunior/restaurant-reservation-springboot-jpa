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
import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.services.ClientService;
import com.restauranteexemplo.reserva.services.ReservationService;
import com.restauranteexemplo.reserva.services.TableService;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@WebMvcTest
class ClientResourceTest {
	
	@Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
	@MockBean
	private ReservationService reservationService;;
	@MockBean
	private ClientService clientService;
	@MockBean
	private TableService tableService;
	
	private Client client;
	
	@BeforeEach
	void setup() {
		// Given / Arrange
		client = new Client(1L, "Everton", "everton@gmail.com", "everton1234");
	}
	
	@Test
	@DisplayName("Test JUnit for given Client object when Insert then return saved Client")
	void testGivenClientObject_WhenInsert_ThenReturnSavedClient() throws JsonProcessingException, Exception {
		//GIVEN
		when(clientService.insert(any(Client.class))).thenReturn(client);
		//WHEN
		 ResultActions response = mockMvc.perform(post("/clients")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(mapper.writeValueAsString(client)));
		//THEN
		 response.andDo(print()).
         andExpect(status().isCreated())
         .andExpect(jsonPath("$.name", is(client.getName())))
         .andExpect(jsonPath("$.email", is(client.getEmail())));

	}
	
	@Test
	@DisplayName("Test JUnit for given clients list when findAll then return clients list")
	void testGivenClientsList_WhenFindAll_ThenReturnClientsList() throws JsonProcessingException, Exception {
		//GIVEN
		List<Client> clients = new ArrayList<>();
		Client clientTwo = new Client(2L, "Jose", "jose@gmail.com", "jose1234");
		clients.addAll(Arrays.asList(client,clientTwo));
		when(clientService.findAll()).thenReturn(clients);
		//WHEN
		 ResultActions response = mockMvc.perform(get("/clients")
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(mapper.writeValueAsString(clients)));
		//THEN
	        response.
            andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.size()", is(clients.size())));
		 
	}
	
	@Test
	@DisplayName("Test JUnit for given Client Object when findById then return Client Object")
	void testGivenClientObject_WhenFindById_ThenReturnClientObject() throws JsonProcessingException, Exception {
		// GIVEN
		Long clientsId = 1L;
		when(clientService.findById(clientsId)).thenReturn(client);
		// WHEN
		ResultActions response = mockMvc.perform(get("/clients/{id}", clientsId));
		// THEN
		 response.
         andExpect(status().isOk())
         .andDo(print())
         .andExpect(jsonPath("$.name", is(client.getName())));
	}

	@Test
	@DisplayName("JUnit test for Given invalid ClientId when findById then return NotFound")
	void testGivenInvalidClientId_WhenFindById_ThenReturnNotFound() throws Exception {
		//GIVEN
		Long clientsId = 1L;
		when(clientService.findById(clientsId)).thenThrow(ResourceNotFoundException.class);
		//WHEN
		ResultActions response = mockMvc.perform(get("/clients/{id}", clientsId));
		//THEN
		response.andExpect(status().isNotFound()).andDo(print());
	}
	
	@Test
	@DisplayName("JUnit test for Given updated Client Object when update then return updated Client")
	void testGivenUpdatedClientObject_When_Update_ThenReturnUpdatedClient() throws JsonProcessingException, Exception {
		//GIVEN
		Client clientUpdated = client;
		clientUpdated.setName("Jose");
		clientUpdated.setEmail("jose@gmail.com");
		Long clientsId = 1L;
		when(clientService.findById(clientsId)).thenReturn(client);
		when(clientService.update(clientsId, clientUpdated)).thenReturn(clientUpdated);
		//WHEN
        ResultActions response = mockMvc.perform(put("/clients/{id}", clientsId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientUpdated)));

		//THEN
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.name", is(clientUpdated.getName())));
		
	}
	
	@Test
	@DisplayName("JUnit test for given ClientId when delete then return not content")
	void testGivenClientId_WhenDeleteById_ThenReturnNotContent() throws Exception {
		//GIVEN
		Long clientsId = 1L;
		doNothing().when(clientService).deleteById(clientsId);
		//WHEN
		ResultActions response = mockMvc.perform(delete("/clients/{id}", clientsId));
		//THEN
		response.andExpect(status().isNoContent());
	}
	
	
}
