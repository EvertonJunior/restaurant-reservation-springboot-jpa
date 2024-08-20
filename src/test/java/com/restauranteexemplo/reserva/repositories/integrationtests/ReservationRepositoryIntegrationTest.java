package com.restauranteexemplo.reserva.repositories.integrationtests;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.entities.Reservation;
import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.integrationtest.testcontainers.AbstractIntegrationTest;
import com.restauranteexemplo.reserva.repositories.ClientRepository;
import com.restauranteexemplo.reserva.repositories.ReservationRepository;
import com.restauranteexemplo.reserva.repositories.TableRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryIntegrationTest extends AbstractIntegrationTest{

	@Autowired
	private ReservationRepository repository;
	
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private TableRepository tableRepository;
	
	private Reservation reservation;

	private Client client;

	private Table table;
	
	@BeforeEach
	void setup() {
		client = new Client(null, "Everton", "everton@gmail.com", "everton1234");
		table = new Table(null, 4);
		clientRepository.save(client);
		tableRepository.save(table);
		reservation = new Reservation(null, client, LocalDateTime.parse("2024-08-16T14:35:45"),
				LocalDateTime.parse("2024-08-16T17:35:45"), table);
		repository.save(reservation);
	}

	@Test
	@DisplayName("JUnit test for given Reservation Object when save then return Saved Reservation object")
	void testGivenClientObject_WhenSave_ThenReturnSavedClientObject() {
		//GIVEN
		//WHEN
		Reservation savedReservation = repository.save(reservation);
		//THEN
		Assertions.assertNotNull(savedReservation);
	}
	
	@Test
	@DisplayName("JUnit test for given list Reservations when findAll then return list Reservations")
	void testGivenListReservations_WhenFindAll_ThenReturnListReservations() {
		//GIVEN
		Client clientTwo = new Client(null, "jose", "jose@gmail.com", "jose1234");
		Table tableTwo = new Table(null, 7);
		clientRepository.save(clientTwo);
		tableRepository.save(tableTwo);
		Reservation reservationTwo = new Reservation(null, clientTwo, LocalDateTime.parse("2024-08-16T22:35:45"),
				LocalDateTime.parse("2024-08-16T23:35:45"), tableTwo);
		repository.save(reservationTwo);
		//WHEN
		List<Reservation> re = repository.findAll();
		//THEN
		Assertions.assertEquals(2, re.size());
		Assertions.assertNotNull(re);
	}
	
	@Test
	@DisplayName("JUnit test for given Reservation Object when FindById then return Reservation object")
	void testGivenReservationObject_WhenFindById_ThenReturnReservationObject() {
		//GIVEN
		//WHEN
		Reservation savedReservation = repository.findById(reservation.getId()).get();
		//THEN
		Assertions.assertEquals("Everton", savedReservation.getClient().getName());
	}
	
	@Test
	@DisplayName("JUnit test for given Reservation object when FindByClientId then return reservation object")
	void testGivenReservationObject_WhenFindByClientId_thenReturnReservationObject() {
		//GIVEN
		//WHEN
		Reservation savedReservation = repository.findByClientId(reservation.getClient().getId());
		//THEN
		Assertions.assertEquals("Everton", savedReservation.getClient().getName());
	}
	

	@Test
	@DisplayName("JUnit test for given deleted Reservation object when deleteById then return deleted Reservation")
	void testGivenDeletedReservation_WhenDeleteById_ThenReturnDeletedObject() {
		//GIVEN
		//WHEN
		repository.deleteById(reservation.getId());
		Optional<Reservation> deletedReservation = repository.findById(reservation.getId());
		//THEN
		Assertions.assertTrue(deletedReservation.isEmpty());
	}
	
}
