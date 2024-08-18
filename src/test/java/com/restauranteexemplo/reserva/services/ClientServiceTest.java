package com.restauranteexemplo.reserva.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.repositories.ClientRepository;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
	
	@Mock
	private ClientRepository repository;
	
	@InjectMocks
	private ClientService services;
	
	private Client client;

	@BeforeEach
	void setup() {
		//GIVEN
		client = new Client(1L, "Everton", "everton@gmail.com", "everton1234");
	}
	
	@Test
	@DisplayName("JUnit test for given Client object when Save client then return Client object")
	void testGivenClientObject_WhenInsert_ThenReturnClientObject() {
		//GIVEN
		when(repository.save(client)).thenReturn(client);
		//WHEN
		Client savedClient = services.insert(client);
		//THEN
		Assertions.assertNotNull(savedClient);
		Assertions.assertEquals("Everton", savedClient.getName());
	}
	
	@Test
	@DisplayName("JUnit test for given Clients List when findAll then return Clients list")
	void testGivenClientList_WhenFindAll_ThenReturnClientsList() {
		//GIVEN
		Client clientTwo = new Client(2L, "Maria", "maria@gmail.com", "maria1234");
		when(repository.findAll()).thenReturn(List.of(client,clientTwo));
		//WHEN
		List<Client> clients = services.findAll();
		//THEN
		Assertions.assertNotNull(clients);
		Assertions.assertEquals(2, clients.size());
	}
	
	@Test
	@DisplayName("Display name")
	void testGivenEmptyClientList_WhenFindAll_thenReturnEmptyClientList() {
		//GIVEN
		when(repository.findAll()).thenReturn(List.of());
		//WHEN
		List<Client> clients = services.findAll();
		//THEN
		Assertions.assertTrue(clients.isEmpty());
		Assertions.assertEquals(0, clients.size());
	}
	
	@Test
	@DisplayName("JUnit test for given Client Object when findById then return Client Object")
	void testGivenClientObject_WhenFindById_ThenReturnClientObject() {
		//GIVEN
		when(repository.findById(anyLong())).thenReturn(Optional.of(client));
		//WHEN
		Client savedClient = services.findById(1L);
		//THEN
		Assertions.assertNotNull(savedClient);
		Assertions.assertEquals("Everton", savedClient.getName());
	}
	
	@Test
	@DisplayName("JUnit test for given not found Client object  when findById then return ExceptionNotFound")
	void testGivenNotFoundClient_WhenFindById_ThenReturnExceptionNotFound() {
		// GIVEN
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		// WHEN //THEN
		assertThrows(ResourceNotFoundException.class, () -> {
			services.findById(1L);
		});
	}
	
	@Test
	@DisplayName("JUnit test for given Update Client object when Update then return Updated Client")
	void testGivenUpdateClientObject_WhenUpdate_ThenReturnUpdatedClient() {
		//GIVEN
		Client clientUpdate = client;
		clientUpdate.setName("Jose");
		clientUpdate.setEmail("jose@gmail.com");
		when(repository.findById(anyLong())).thenReturn(Optional.of(client));
		when(repository.getReferenceById(1L)).thenReturn(client);
		when(repository.save(client)).thenReturn(clientUpdate);
		//WHEN
		services.update(1L, clientUpdate);
		//THEN
		Assertions.assertEquals("Jose", client.getName());
		Assertions.assertEquals("jose@gmail.com", client.getEmail());
	}
	
	@Test
	@DisplayName("JUnit test for given Client when delete then deleted client")
	void testGivenClient_WhenDeleteByid_ThenReturnDeletedClient() {
		//GIVEN
		when(repository.findById(anyLong())).thenReturn(Optional.of(client));
		doNothing().when(repository).deleteById(client.getId());
		//WHEN
		assertDoesNotThrow(() -> services.deleteById(client.getId()));
		//THEN
		verify(repository, times(1)).deleteById(client.getId());
	}
	
	
}
