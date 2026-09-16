package com.example.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DeliveryPersonService {
	@Autowired
	private DeliveryPersonRepository repo;

	private final Map<Long, Integer> orderAssignments = new HashMap<>();
	private final Set<Integer> busyDeliveryPersonIds = new HashSet<>();

	public List<DeliveryPerson> listAll() {

		return repo.findAll();
	}

	public DeliveryPerson get(int id) {

		return repo.findById(id).get();
	}
	public void save(DeliveryPerson deliveryPerson)
	{
	repo.save(deliveryPerson);
	}
	public void delete(Integer id)
	{
	busyDeliveryPersonIds.remove(id);
	repo.deleteById(id);
	}

	public DeliveryPerson assign(long orderId, int deliveryPersonId) {
		DeliveryPerson deliveryPerson = repo.findById(deliveryPersonId).get(); // 
		if (busyDeliveryPersonIds.contains(deliveryPersonId)) {
			throw new IllegalStateException("Delivery person " + deliveryPersonId + " is already assigned");
		}
		busyDeliveryPersonIds.add(deliveryPersonId);
		orderAssignments.put(orderId, deliveryPersonId);
		return deliveryPerson;
	}

	public DeliveryPerson getDeliveryDetails(long orderId) {
		Integer deliveryPersonId = orderAssignments.get(orderId);
		if (deliveryPersonId == null) {
			throw new java.util.NoSuchElementException("No delivery person assigned to order " + orderId);
		}
		return repo.findById(deliveryPersonId).get();
	}

	public List<DeliveryPerson> getAvailable() {
		return repo.findAll().stream()
				.filter(dp -> !busyDeliveryPersonIds.contains(dp.getDeliveryPersonId()))
				.collect(Collectors.toList());
	}

}

