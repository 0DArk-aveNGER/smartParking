package com.ericsson.iot.smart.parking.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Sensor {

    @Id
    @GeneratedValue
    private Long id;

    private String brand;
    private String model;
    private String serialNumber;
    
    @OneToOne
    @JoinColumn(name="parkingSlotId")
    private ParkingSlot parkingSlot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomer() {
        return brand;
    }

    public void setCustomer(String customer) {
        this.brand = customer;
    }

    public String getFlight() {
        return model;
    }

    public void setFlight(String flight) {
        this.model = flight;
    }

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}

	public void setParkingSlot(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}
}
