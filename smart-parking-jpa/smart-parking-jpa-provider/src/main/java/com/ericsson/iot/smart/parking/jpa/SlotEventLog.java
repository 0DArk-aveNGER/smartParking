package com.ericsson.iot.smart.parking.jpa;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SlotEventLog {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="parkingSlotId",referencedColumnName="id")
	private ParkingSlot parkingSlot;
	
	private Timestamp eventTimeStart;
	private Timestamp eventTimeEnd;
	private Boolean status;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}
	
	public void setParkingSlot(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}
	
	public Timestamp getEventTimeStart() {
		return eventTimeStart;
	}
	
	public void setEventTimeStart(Timestamp eventTimeStart) {
		this.eventTimeStart = eventTimeStart;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Timestamp getEventTimeEnd() {
		return eventTimeEnd;
	}

	public void setEventTimeEnd(Timestamp eventTimeEnd) {
		this.eventTimeEnd = eventTimeEnd;
	}
}
