package com.restauranteexemplo.reserva.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.services.TableService;

@RestController
@RequestMapping(value = "/tables")
public class TableResource {

	@Autowired
	private TableService service;

	@GetMapping
	public ResponseEntity<List<Table>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@GetMapping(value = ("/{id}"))
	public ResponseEntity<Table> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));
	}

	@DeleteMapping(value = ("/{id}"))
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<Table> insert(@RequestBody Table table) {
		table = service.insert(table);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(table.getId()).toUri();
		return ResponseEntity.created(uri).body(table);
	}

	@PutMapping(value = ("/{id}"))
	public ResponseEntity<Table> update(@PathVariable Long id, @RequestBody Table table) {
		return ResponseEntity.ok().body(service.update(id, table));
	}
}
