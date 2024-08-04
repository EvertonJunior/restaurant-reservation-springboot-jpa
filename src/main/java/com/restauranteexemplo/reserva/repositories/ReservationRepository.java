package com.restauranteexemplo.reserva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restauranteexemplo.reserva.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
}
