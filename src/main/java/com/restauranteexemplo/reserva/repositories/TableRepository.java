package com.restauranteexemplo.reserva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restauranteexemplo.reserva.entities.Table;

public interface TableRepository extends JpaRepository<Table, Long> {

}
