package com.example.demo;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for DeliveryController using MockMvc. The service is
 * mocked so only routing, status codes, and request/response handling
 * are under test - not the business logic itself (covered separately
 * in DeliveryPersonServiceTest).
 */
@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DeliveryPersonService service;

	private static final String RAVI_JSON =
			"{\"deliveryPersonId\":1,\"name\":\"Ravi Kumar\",\"contactNo\":9876543210}";

	@Test
	void listDeliveryPersons_returnsOkWithList() throws Exception {
		DeliveryPerson ravi = new DeliveryPerson(1, "Ravi Kumar", 9876543210L);
		when(service.listAll()).thenReturn(List.of(ravi));

		mockMvc.perform(get("/delivery-persons"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is("Ravi Kumar")));
	}

	@Test
	void getById_returnsOk_whenFound() throws Exception {
		DeliveryPerson ravi = new DeliveryPerson(1, "Ravi Kumar", 9876543210L);
		when(service.get(1)).thenReturn(ravi);

		mockMvc.perform(get("/delivery-persons/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Ravi Kumar")));
	}

	@Test
	void getById_returns404_whenNotFound() throws Exception {
		when(service.get(99)).thenThrow(new NoSuchElementException());

		mockMvc.perform(get("/delivery-persons/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void addDeliveryPerson_callsServiceSave() throws Exception {
		mockMvc.perform(post("/delivery-persons")
						.contentType(MediaType.APPLICATION_JSON)
						.content(RAVI_JSON))
				.andExpect(status().isOk());

		verify(service, times(1)).save(any(DeliveryPerson.class));
	}

	@Test
	void updateDeliveryPerson_returnsOk_whenExists() throws Exception {
		when(service.get(1)).thenReturn(new DeliveryPerson(1, "Ravi Kumar", 9876543210L));

		mockMvc.perform(put("/delivery-persons/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(RAVI_JSON))
				.andExpect(status().isOk());

		verify(service, times(1)).save(any(DeliveryPerson.class));
	}

	@Test
	void updateDeliveryPerson_returns404_whenNotFound() throws Exception {
		when(service.get(99)).thenThrow(new NoSuchElementException());

		mockMvc.perform(put("/delivery-persons/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content(RAVI_JSON))
				.andExpect(status().isNotFound());

		verify(service, never()).save(any(DeliveryPerson.class));
	}

	@Test
	void deleteDeliveryPerson_callsServiceDelete() throws Exception {
		mockMvc.perform(delete("/delivery-persons/1"))
				.andExpect(status().isOk());

		verify(service, times(1)).delete(1);
	}

	@Test
	void assign_returns200_withAssignedPerson() throws Exception {
		DeliveryPerson ravi = new DeliveryPerson(1, "Ravi Kumar", 9876543210L);
		when(service.assign(1001L, 1)).thenReturn(ravi);

		mockMvc.perform(put("/orders/1001/assign/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Ravi Kumar")));
	}

	@Test
	void assign_returns404_whenPersonNotFound() throws Exception {
		when(service.assign(1001L, 999)).thenThrow(new NoSuchElementException());

		mockMvc.perform(put("/orders/1001/assign/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void assign_returns409_whenPersonAlreadyBusy() throws Exception {
		when(service.assign(1002L, 1)).thenThrow(new IllegalStateException());

		mockMvc.perform(put("/orders/1002/assign/1"))
				.andExpect(status().isConflict());
	}

	@Test
	void getDeliveryDetails_returns200_whenAssigned() throws Exception {
		DeliveryPerson ravi = new DeliveryPerson(1, "Ravi Kumar", 9876543210L);
		when(service.getDeliveryDetails(1001L)).thenReturn(ravi);

		mockMvc.perform(get("/orders/1001/delivery"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Ravi Kumar")));
	}

	@Test
	void getDeliveryDetails_returns404_whenNotAssigned() throws Exception {
		when(service.getDeliveryDetails(404L)).thenThrow(new NoSuchElementException());

		mockMvc.perform(get("/orders/404/delivery"))
				.andExpect(status().isNotFound());
	}

	@Test
	void getAvailable_returnsOkWithList() throws Exception {
		DeliveryPerson anita = new DeliveryPerson(2, "Anita Rao", 9123456780L);
		when(service.getAvailable()).thenReturn(List.of(anita));

		mockMvc.perform(get("/delivery-persons/available"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is("Anita Rao")));
	}
}

