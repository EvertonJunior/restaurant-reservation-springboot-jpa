package com.restauranteexemplo.reserva.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.restauranteexemplo.reserva.entities.Reservation;
import com.restauranteexemplo.reserva.entities.enums.TableStatus;
import com.restauranteexemplo.reserva.repositories.ReservationRepository;
import com.restauranteexemplo.reserva.repositories.TableRepository;
import com.restauranteexemplo.reserva.services.exceptions.DatabaseException;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository repository;

	@Autowired
	private TableRepository tableRepository;

	public List<Reservation> findAll() {
		return repository.findAll();
	}

	public Reservation findById(Long id) {
		Optional<Reservation> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public void delete(Long id) {
		repository.findById(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Reservation insert(Reservation reservation) {
		return repository.save(reservation);
	}
	
	public Reservation findByClientId(Long id) {
		return repository.findByClientId(id);
	}

	@Scheduled(fixedRate = 60000) //
	public void cleanUpExpiredReservation() {
		List<Reservation> reservations = findAll();
		for (Reservation x : reservations) {
			if (x.getFinalDateTime().isBefore(LocalDateTime.now())) {
				x.getTable().setTableStatus(TableStatus.FREE);
				delete(x.getId());
			} else {
				x.getTable().setTableStatus(TableStatus.RESERVED);
			}
			tableRepository.save(x.getTable());
		}

	}

}
