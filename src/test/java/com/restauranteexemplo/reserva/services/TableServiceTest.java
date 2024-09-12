package com.restauranteexemplo.reserva.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.repositories.TableRepository;
import com.restauranteexemplo.reserva.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private TableRepository repository;
	@InjectMocks
	private TableService service;
	
	private Table table;
	
	@BeforeEach
	void setup() {
		table = new Table(1L , 4);
	}

	
	@Test
	@DisplayName("JUnit test for given Table object when insert then return insert Table Object")
	void testGivenTableObject_WhenInsert_ThenReturnTableObject() {
		//GIVEN
		when(repository.save(table)).thenReturn(table);
		//WHEN
		Table savedTable = service.insert(table);
		//THEN
		assertNotNull(savedTable);
		assertEquals(4, savedTable.getSeats());
	}
	
	@Test
	@DisplayName("JUnit test for given tables list when findAll then return tables list")
	void testGivenTablesList_WhenFindAll_ThenReturnTablesList() {
		//GIVEN
		Table tableTwo = new Table(2L,7);
		when(repository.findAll()).thenReturn(List.of(table,tableTwo));
		//WHEN
		List<Table> tables = service.findAll();
		//THEN
		assertNotNull(tables);
		assertEquals(2, tables.size());
	}

	
	@Test
	@DisplayName("JUnit test for given empty tables list when findAll then return empty list ")
	void testGivenEmptyTablesList_WhenFindAll_ThenReturnEmptyList() {
		//GIVEN
		when(repository.findAll()).thenReturn(Collections.emptyList());
		//WHEN
		List<Table> tables = service.findAll();
		//THEN
		assertTrue(tables.isEmpty());
	}
	
	@Test
	@DisplayName("JUnit test for given Table object when findById then return Table object")
	void testGivenTableObject_WhenFindById_ThenReturnTableObject() {
		//GIVEN
		when(repository.findById(1L)).thenReturn(Optional.of(table));
		//WHEN
		Table savedTable = service.findById(1L);
		//THEN
		assertNotNull(savedTable);
		assertEquals(4, table.getSeats());
	}
	
	@Test
	@DisplayName("JUnit test for given invalid id when findById then return ResourceNotFoundException")
	void testGivenInvalidId_WhenFindById_ThenReturnNotFoundException() {
		//GIVEN
		when(repository.findById(1L)).thenThrow(ResourceNotFoundException.class);
		//WHEN
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
		//THEN
	}
	
	@Test
	@DisplayName("JUnit test for given updated Table object when update then return Updated Table")
	void testGivenUpdatedTableObject_WhenUpdate_ThenReturnUpdatedTableObject() {
		//GIVEN
		Table tableTwo = new Table(null,  7);
		when(repository.findById(1L)).thenReturn(Optional.of(table));
		when(repository.getReferenceById(1L)).thenReturn(table);
		//WHEN
		service.update(1L, tableTwo);
		//THEN
		assertEquals(7, table.getSeats());
	}
	
	@Test
	@DisplayName("JUnit test for given deleted Table when deleteById then return deleted table")
	void testDeletedTable_WhenDeleteById_ThenReturnDeletedTable() {
		//GIVEN
		when(repository.findById(anyLong())).thenReturn(Optional.of(table));
		doNothing().when(repository).deleteById(table.getId());
		//WHEN
		assertDoesNotThrow(() -> service.deleteById(table.getId()));
		//THEN
		verify(repository, times(1)).deleteById(table.getId());
	}
	
}
