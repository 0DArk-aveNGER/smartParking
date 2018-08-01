package com.ericsson.iot.smart.parking;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ParkingSlotManagement {

    Collection<ParkingSlotDTO> list();

    ParkingSlotDTO get(String parkingSlotId);

    void updateStatus(ParkingSlotDTO parkingSlotDTO);
    
	void updateStatus(String parkingSlotId, Boolean status, Date timestamp);

	Long getLotStatus(String id);

	Long getFloorStatus(String lotId, Integer floorId);

	List<EntityStatus> getLots();

	List<EntityStatus> getFloors(String lotId);

	List<EntityStatus> getRows(String lotId, Integer floorId);

	List<EntityStatus> getSlots(String lotId, Integer floorId, Integer rowId);
    
}
