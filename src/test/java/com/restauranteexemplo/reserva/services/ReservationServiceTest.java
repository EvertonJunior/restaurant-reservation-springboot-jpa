package com.restauranteexemplo.reserva.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
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
import com.restauranteexemplo.reserva.entities.Reservation;
import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.entities.enums.TableStatus;
import com.restauranteexemplo.reserva.repositories.ReservationRepository;
import com.restauranteexemplo.reserva.repositories.TableRepository;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
	
	
	@Mock
	private ReservationRepository repository;
	@Mock
	private TableRepository tableRepository;
	@InjectMocks
	private ReservationService services;


	private Reservation reservation;
	
	
	@BeforeEach
	void setup() {
		Client client = new Client(1L, "Everton", "everton@gmail.com", "everton1234");
		Table table = new Table(1L, 4);
		reservation = new Reservation(1L, client, LocalDateTime.parse("2024-08-30T14:35:45"),
				LocalDateTime.parse("2024-08-30T17:35:45"), table);
		table.setTableStatus(TableStatus.RESERVED);
	}
	
	@Test
	@DisplayName("JUnit test for given saved Reservation object when insert  then return saved Reservation object")
	void testGivenSavedReservation_WhenInsert_ThenReturnSavedReservation() {
		//GIVEN
		when(repository.save(reservation)).thenReturn(reservation);
		//WHEN
		Reservation savedReservation = services.insert(reservation);
		//THEN
		Assertions.assertNotNull(savedReservation);
	}
	
	@Test
	@DisplayName("JUnit test for Given list reservations when findAll then return list reservations")
	void testGivenListReservations_WhenFindAll_ThenReturnListReservations() {
		//GIVEN
		Client clientTwo = new Client(2L, "jose", "jose@gmail.com", "jose1234");
		Table tableTwo = new Table(2L, 6);
		Reservation reservationTwo = new Reservation(2L, clientTwo, LocalDateTime.parse("2024-08-17T14:35:45"),
				LocalDateTime.parse("2024-08-17T17:35:45"), tableTwo);
		when(services.findAll()).thenReturn(List.of(reservation, reservationTwo));
		//WHEN
		List<Reservation> reservationsSaved = services.findAll();
		//THEN
		Assertions.assertEquals(2, reservationsSaved.size());
	}
	
	@Test
	@DisplayName("JUnit test for given empty reservations when findAll then return empty reservations list")
	void testGivenEmptyReservations_WhenFindAll_ThenReturnEmptyReservationsList() {
		//GIVEN
		when(services.findAll()).thenReturn(Collections.emptyList());
		//WHEN
		List<Reservation> reservations = services.findAll();
		//THEN
		Assertions.assertTrue(reservations.isEmpty());
	}
	
	
	@Test
	@DisplayName("JUnit test for given Reservation Object when findById then return Reservation object")
	void testGivenReservationObject_WhenFindById_ThenReturnReservationObject() {
		//GIVEN
		when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
		//WHEN
		Reservation savedReservation = services.findById(1L);
		//THEN
		Assertions.assertNotNull(savedReservation);
		Assertions.assertEquals("Everton", savedReservation.getClient().getName());
	}
	
	@Test
	@DisplayName("JUnit test for given ResourceNotFound when findById then return ResourceNotFoundException")
	void testResourceNotFound_whenFindById_ThenReturnResourceNotFound() {
		//GIVEN
		when(repository.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
		//WHEN
		//THEN
		assertThrows(ResourceNotFoundException.class, () -> services.findById(1L));
	}
	
	@Test
	@DisplayName("JUnit test for given delete Reservation when deleteById then return deleted Reservation")
	void testDeleteReservation_WhenDeleteById_ThenReturnDeletedReservation() {
		//GIVEN
		when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
		doNothing().when(repository).deleteById(reservation.getId());
		//WHEN
		assertDoesNotThrow(() -> services.delete(reservation.getId()));
		//THEN
		verify(repository, times(1)).deleteById(reservation.getId());
	}
	
	@Test
	@DisplayName("JUnit test for given delete expired Reservation when cleanUpExpiredReservation then return deleted expired reservation")
	void testGivenDeleteExpiredReservation_WhencleanUpExpiredReservation_ThenReturnDeletedExpiredReservation() {
		//GIVEN
		Client clientTwo = new Client(2L, "jose", "jose@gmail.com", "jose1234");
		Table tableTwo = new Table(2L, 6);
		Reservation reservationTwo = new Reservation(2L, clientTwo, LocalDateTime.parse("2024-08-17T16:35:45"),
				LocalDateTime.parse("2024-08-17T17:55:45"), tableTwo);
		tableTwo.setTableStatus(TableStatus.RESERVED);
		when(repository.findAll()).thenReturn(List.of(reservation,reservationTwo));
		when(tableRepository.save(reservation.getTable())).thenReturn(reservation.getTable());
		//WHEN
		services.cleanUpExpiredReservation();
		//THEN
		verify(repository).deleteById(reservationTwo.getId());
		Assertions.assertEquals(TableStatus.FREE, tableTwo.getTableStatus());
		Assertions.assertEquals(TableStatus.RESERVED, reservation.getTable().getTableStatus());
	}
}
