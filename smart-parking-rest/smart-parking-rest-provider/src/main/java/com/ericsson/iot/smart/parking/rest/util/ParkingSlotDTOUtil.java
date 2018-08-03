package com.ericsson.iot.smart.parking.rest.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ericsson.iot.smart.parking.EntityStatus;
import com.ericsson.iot.smart.parking.ParkingSlotDTO;
import com.ericsson.iot.smart.parking.jpa.ParkingSlot;
import com.ericsson.iot.smart.parking.jpa.ParkingSlotStatus;
import com.ericsson.iot.smart.parking.jpa.SlotEventLog;

public class ParkingSlotDTOUtil {
	public static SlotEventLog getEventLog(ParkingSlotDTO parkingSlotDTO)
	{
		SlotEventLog slotEventLog = new SlotEventLog();
		
		if (parkingSlotDTO != null) {
			
			ParkingSlot parkingSlot = new ParkingSlot();
			parkingSlot.setId(parkingSlotDTO.getSlotId());
			
			slotEventLog.setParkingSlot(parkingSlot);
			slotEventLog.setStatus(parkingSlotDTO.getStatus());
			slotEventLog.setEventTimeStart(Timestamp.from(parkingSlotDTO.getTimestamp().toInstant()));
		}
		
		return slotEventLog;
	}

	public static ParkingSlotStatus getParkingSlotStatus(ParkingSlotDTO parkingSlotDTO)
	{
		
		ParkingSlotStatus parkingSlotStatus = new ParkingSlotStatus();
		
		if (parkingSlotDTO != null) {
			ParkingSlot parkingSlot = new ParkingSlot();
			parkingSlot.setId(parkingSlotDTO.getSlotId());
			parkingSlot.setFloor(parkingSlotDTO.getFloor());
			parkingSlot.setLot(parkingSlotDTO.getLot());
			parkingSlot.setRow(parkingSlotDTO.getRow());
			parkingSlot.setSlot(parkingSlotDTO.getSlot());
			
			parkingSlotStatus.setParkingSlot(parkingSlot);
			parkingSlotStatus.setStatus(parkingSlotDTO.getStatus());
		}
		
		return parkingSlotStatus;
	}
	
	public static ParkingSlotDTO getDTO(ParkingSlotStatus parkingSlotStatus)
	{
		ParkingSlotDTO parkingSlotDTO = new ParkingSlotDTO();

		if (parkingSlotStatus != null) {
			parkingSlotDTO.setFloor(parkingSlotStatus.getParkingSlot().getFloor());
			parkingSlotDTO.setLot(parkingSlotStatus.getParkingSlot().getLot());
			parkingSlotDTO.setRow(parkingSlotStatus.getParkingSlot().getRow());
			parkingSlotDTO.setSlot(parkingSlotStatus.getParkingSlot().getSlot());
			parkingSlotDTO.setStatus(parkingSlotStatus.getStatus());
		}
		
		return parkingSlotDTO;
	}
	
	public static List<EntityStatus> getEntityStatus(Map<?, Boolean> items) {
		List<EntityStatus> entityStatus = new ArrayList<>();
		for (Entry<?, Boolean> entry : items.entrySet()) {
			EntityStatus e = new EntityStatus();
			e.setId(entry.getKey().toString());
			e.setStatus(entry.getValue());
			entityStatus.add(e );
		}
		return entityStatus;
	}
}
