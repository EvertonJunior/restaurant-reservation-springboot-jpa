package com.restauranteexemplo.reserva.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.restauranteexemplo.reserva.entities.Reservation;
import com.restauranteexemplo.reserva.services.ReservationService;

@RestController
@RequestMapping(value = "/reservations")
public class ReservationResource {

	@Autowired
	private ReservationService service;

	@GetMapping
	public ResponseEntity<List<Reservation>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Reservation> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}
	@GetMapping(value = "/client/{id}")
	public ResponseEntity<Reservation> findByClientId(@PathVariable Long id){
		return ResponseEntity.ok().body(service.findByClientId(id));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<Reservation> insert(Reservation reservation) {
		reservation = service.insert(reservation);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(reservation.getId())
				.toUri();
		return ResponseEntity.created(uri).body(reservation);
	}
}
