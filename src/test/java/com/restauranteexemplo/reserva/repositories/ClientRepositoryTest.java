package com.restauranteexemplo.reserva.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.restauranteexemplo.reserva.entities.Client;

@DataJpaTest
class ClientRepositoryTest {

	@Autowired
	private ClientRepository repository;
	
	private Client client;
	
	@BeforeEach
	void setup() {
		// Given / Arrange
		client = new Client(null, "Everton", "everton@gmail.com", "everton1234");
		repository.save(client);
	}
	
	@Test
	@DisplayName("JUnit test for given Client Object when save then return saved Client")
	void testGivenClientObject_WhenSave_ThenReturnSavedClient() {
		//GIVEN
		//WHEN
		Client savedClient = repository.save(client);
		//THEN
		Assertions.assertNotNull(savedClient);
		Assertions.assertTrue(client.getId() > 0);
	}
	
	@Test
	@DisplayName("JUnit test for given Client list when findAll then return Client list")
	void testGivenClientList_WhenFindAll_thenReturnListClient() {
		//GIVEN
		Client clientTwo = new Client(2L, "Maria", "maria@gmail.com", "maria1234");
		repository.save(clientTwo);
		//WHEN
		List<Client> clients = repository.findAll();
		//THEN
		Assertions.assertNotNull(clients);
		Assertions.assertEquals(2, clients.size());
		
	}
	
	@Test
	@DisplayName("JUnit test for given Client object when findById then return Client object")
	void testGivenClientObject_WhenFindById_ThenReturnClientObject() {
		//GIVEN
		//WHEN
		Client savedClient = repository.findById(client.getId()).get();
		//THEN
		Assertions.assertNotNull(savedClient);
		Assertions.assertTrue(savedClient.getId() > 0);
		Assertions.assertEquals(client.getId(), savedClient.getId());
	}
	
	@Test
	@DisplayName("JUnit test for given Client object when update then return updated Client object")
	void testGivenClientObject_WhenUpdateClient_ThenReturnUpdatedClientObjetct() {
		//GIVEN
		
		//WHEN
		Client clientUpdate = repository.findById(client.getId()).get();
		clientUpdate.setName("Jose");
		clientUpdate.setEmail("jose@gmail.com");
		
		Client savedClient= repository.save(clientUpdate);
		//THEN
		Assertions.assertEquals("Jose", savedClient.getName());
		Assertions.assertEquals("jose@gmail.com", savedClient.getEmail());
	}

	
	@Test
	@DisplayName("JUnit test for given Client object when delete then remove Client")
	void testGivenClientObject_WhenDeleteById_thenReturnRemoveClient() {
		//GIVEN
		//WHEN
		repository.deleteById(client.getId());
		Optional<Client> removeClient = repository.findById(client.getId());
		//THEN
		Assertions.assertTrue(removeClient.isEmpty());
	}
}
