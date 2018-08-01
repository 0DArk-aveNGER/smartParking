package com.ericsson.iot.smart.parking;

import java.beans.Transient;
import java.util.Date;
import java.util.StringJoiner;

public class ParkingSlotDTO {
	
	private String lot;
	private Integer floor;
	private Integer row;
	private String slot;
	private Date timestamp;
	private Boolean status;
	
	/**
	 * @return the lotType
	 */
	public String getLot() {
		return lot;
	}
	/**
	 * @param lotType the lotType to set
	 */
	public void setLot(String lot) {
		this.lot = lot;
	}
	/**
	 * @return the floor
	 */
	public Integer getFloor() {
		return floor;
	}
	/**
	 * @param floor the floor to set
	 */
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	/**
	 * @return the row
	 */
	public Integer getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}
	/**
	 * @return the slot
	 */
	public String getSlot() {
		return slot;
	}
	/**
	 * @param slot the slot to set
	 */
	public void setSlot(String slot) {
		this.slot = slot;
	}
	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Transient
	public String getSlotId()
	{
		StringJoiner sj = new StringJoiner(":");
		sj.add(lot).add(floor.toString()).add(row.toString()).add(slot);
		return sj.toString();
	}
	
	public void init(String slotId, Boolean status, Date timestamp)
	{
		String[] fields = slotId.split(":");
		lot = fields[0];
		floor = Integer.valueOf(fields[1]);
		row = Integer.valueOf(fields[2]);
		slot = fields[3];
		this.status = status;
		this.timestamp = timestamp;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
