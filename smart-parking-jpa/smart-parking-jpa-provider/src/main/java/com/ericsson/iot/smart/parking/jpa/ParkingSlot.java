package com.ericsson.iot.smart.parking.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;;

@Entity
public class ParkingSlot {
	
	@Id
	private String id;
	private String lot;
	private Integer floor;
	private Integer row;
	private String slot;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLot() {
		return lot;
	}
	
	public void setLot(String lot) {
		this.lot = lot;
	}
	
	public Integer getFloor() {
		return floor;
	}
	
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	
	public Integer getRow() {
		return row;
	}
	
	public void setRow(Integer row) {
		this.row = row;
	}
	
	public String getSlot() {
		return slot;
	}
	
	public void setSlot(String slot) {
		this.slot = slot;
	}

}
