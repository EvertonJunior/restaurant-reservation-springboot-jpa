package com.restauranteexemplo.reserva.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.repositories.ClientRepository;
import com.restauranteexemplo.reserva.services.exceptions.DatabaseException;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	public List<Client> findAll() {
		return repository.findAll();
	}

	public Client findById(Long id) {
		Optional<Client> client = repository.findById(id);
		return client.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	public void deleteById(Long id) {
		findById(id);
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Client insert(Client client) {
		return repository.save(client);
	}

	public Client update(Long id, Client client) {
		findById(id);
		Client entity = repository.getReferenceById(id);
		updateData(entity, client);
		return repository.save(entity);
	}

	public void updateData(Client entity, Client client) {
		entity.setName(client.getName());
		entity.setEmail(client.getEmail());
	}
}
