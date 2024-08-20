package com.restauranteexemplo.reserva.repositories.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.integrationtest.testcontainers.AbstractIntegrationTest;
import com.restauranteexemplo.reserva.repositories.TableRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TableRepositoryIntegrationTest extends AbstractIntegrationTest{

	@Autowired
	private TableRepository repository;
	
	private Table table;
	
	@BeforeEach
	void setup() {
		table = new Table(null, 4);
		repository.save(table);
	}

	@Test
	@DisplayName("JUnit test for given saved Table Object when save then return saved Table object")
	void testGivenTableObject_WhenSave_ThenReturnSavedTableObject() {
		//GIVEN
		//WHEN
		Table savedTable = repository.save(table);
		//THEN
		assertNotNull(savedTable);
		assertEquals(4, table.getSeats());
	}
	
	@Test
	@DisplayName("JUnit test for given tables list when findAll then return tables list")
	void testGivenTablesList_WhenFindAll_ThenReturnTablesList() {
		//GIVEN
		Table tableTwo = new Table(null,6);
		repository.save(tableTwo);
		//WHEN
		List<Table> tables = repository.findAll();
		//THEN
		assertNotNull(tables);
		assertEquals(2, tables.size());
	}
	
	@Test
	@DisplayName("JUnit test for given Table Object when findById then return Table object")
	void testGivenTableObject_WhenFindById_ThenReturnTableObject() {
		//GIVEN
		//WHEN
		Table savedTable = repository.findById(1L).get();
		//THEN
		assertNotNull(savedTable);
		assertEquals(4, table.getSeats());
	}
	
	@Test
	@DisplayName("JUnit test for given Updated Table when save then return updated Table Object")
	void testGivenUpdatedTableObject_WhenSave_ThenReturnUpdatedTable() {
		//GIVEN
		table.setId(1L);
		Table tableUpdated = repository.findById(1L).get();
		tableUpdated.setSeats(8);
		//WHEN
		Table savedTable = repository.save(tableUpdated);
		//THEN
		assertNotNull(savedTable);
		assertEquals(8, savedTable.getSeats());
	}
	
	@Test
	@DisplayName("JUnit test for given deleted Table when deleteById then return deleted table")
	void testDeletedTable_WhenDeleteById_ThenReturnDeletedTable() {
		//GIVEN
		//WHEN
		repository.deleteById(1L);
		Optional<Table> savedTable = repository.findById(1L);
		//THEN
		assertTrue(savedTable.isEmpty());
	}

}
