package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for DeliveryPersonService business logic, written per the
 * Plan slide's TDD approach: identify test cases for the module (assign,
 * getDeliveryDetails, CRUD), write them, then confirm they pass.
 *
 * The repository is mocked with Mockito so these run instantly with no
 * database needed - only DeliveryPersonService's own logic is under test.
 */
@ExtendWith(MockitoExtension.class)
class DeliveryPersonServiceTest {

	@Mock
	private DeliveryPersonRepository repo;

	@InjectMocks
	private DeliveryPersonService service;

	private DeliveryPerson ravi;
	private DeliveryPerson anita;

	@BeforeEach
	void setUp() {
		ravi = new DeliveryPerson(1, "Ravi Kumar", 9876543210L);
		anita = new DeliveryPerson(2, "Anita Rao", 9123456780L);
	}

	// ---------- listAll ----------

	@Test
	void listAll_returnsEveryPersonFromRepository() {
		when(repo.findAll()).thenReturn(List.of(ravi, anita));

		List<DeliveryPerson> result = service.listAll();

		assertEquals(2, result.size());
		assertTrue(result.contains(ravi));
	}

	// ---------- get ----------

	@Test
	void get_returnsPerson_whenFound() {
		when(repo.findById(1)).thenReturn(Optional.of(ravi));

		DeliveryPerson result = service.get(1);

		assertEquals("Ravi Kumar", result.getName());
	}

	@Test
	void get_throwsNoSuchElementException_whenNotFound() {
		when(repo.findById(99)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.get(99));
	}

	// ---------- save ----------

	@Test
	void save_delegatesToRepository() {
		service.save(ravi);

		verify(repo, times(1)).save(ravi);
	}

	// ---------- delete ----------

	@Test
	void delete_delegatesToRepository() {
		service.delete(1);

		verify(repo, times(1)).deleteById(1);
	}

	// ---------- assign ----------

	@Test
	void assign_succeeds_whenPersonExistsAndIsFree() {
		when(repo.findById(1)).thenReturn(Optional.of(ravi));

		DeliveryPerson result = service.assign(1001L, 1);

		assertEquals(ravi, result);
		assertEquals(ravi, service.getDeliveryDetails(1001L));
	}

	@Test
	void assign_throwsNoSuchElementException_whenPersonDoesNotExist() {
		when(repo.findById(999)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> service.assign(1001L, 999));
	}

	@Test
	void assign_throwsIllegalStateException_whenPersonAlreadyBusy() {
		when(repo.findById(1)).thenReturn(Optional.of(ravi));
		service.assign(1001L, 1); // first assignment succeeds

		assertThrows(IllegalStateException.class, () -> service.assign(1002L, 1));
	}

	// ---------- getDeliveryDetails ----------

	@Test
	void getDeliveryDetails_returnsAssignedPerson() {
		when(repo.findById(1)).thenReturn(Optional.of(ravi));
		service.assign(1001L, 1);

		DeliveryPerson result = service.getDeliveryDetails(1001L);

		assertEquals("Ravi Kumar", result.getName());
	}

	@Test
	void getDeliveryDetails_throwsNoSuchElementException_whenOrderNotAssigned() {
		assertThrows(NoSuchElementException.class, () -> service.getDeliveryDetails(404L));
	}

	// ---------- getAvailable ----------

	@Test
	void getAvailable_excludesAssignedPersons() {
		when(repo.findById(1)).thenReturn(Optional.of(ravi));
		when(repo.findAll()).thenReturn(List.of(ravi, anita));
		service.assign(1001L, 1); // Ravi becomes busy

		List<DeliveryPerson> available = service.getAvailable();

		assertEquals(1, available.size());
		assertEquals("Anita Rao", available.get(0).getName());
	}

	@Test
	void getAvailable_returnsEveryone_whenNoneAssigned() {
		when(repo.findAll()).thenReturn(List.of(ravi, anita));

		List<DeliveryPerson> available = service.getAvailable();

		assertEquals(2, available.size());
	}
}
