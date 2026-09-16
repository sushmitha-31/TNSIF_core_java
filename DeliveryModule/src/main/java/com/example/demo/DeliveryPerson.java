package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DeliveryPerson {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int deliveryPersonId;
	private String name;
	private long contactNo;

	public int getDeliveryPersonId() {
		return deliveryPersonId;
	}
	public void setDeliveryPersonId(int deliveryPersonId) {
		this.deliveryPersonId = deliveryPersonId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getContactNo() {
		return contactNo;
	}
	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	public DeliveryPerson(int deliveryPersonId, String name, long contactNo) {
		this.deliveryPersonId = deliveryPersonId;
		this.name = name;
		this.contactNo = contactNo;
	}

	public DeliveryPerson() {

	}

	@Override
	public String toString() {
		return "DeliveryPerson [deliveryPersonId=" + deliveryPersonId + ", name=" + name + ", contactNo=" + contactNo + "]";
	}

}