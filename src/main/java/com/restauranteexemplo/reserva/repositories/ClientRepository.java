package com.restauranteexemplo.reserva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restauranteexemplo.reserva.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
