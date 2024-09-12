package com.restauranteexemplo.reserva.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.repositories.TableRepository;
import com.restauranteexemplo.reserva.services.exceptions.DatabaseException;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@Service
public class TableService {

	@Autowired
	private TableRepository repository;

	public List<Table> findAll() {
		return repository.findAll();
	}

	public Table findById(Long id) {
		Optional<Table> table = repository.findById(id);
		return table.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public void deleteById(Long id) {
		repository.findById(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Table insert(Table table) {
		return repository.save(table);
	}

	public Table update(Long id, Table table) {
		repository.findById(id);
		Table entity = repository.getReferenceById(id);
		updateData(entity, table);
		return repository.save(entity);
	}

	private void updateData(Table entity, Table table) {
		entity.setSeats(table.getSeats());
	}

}
