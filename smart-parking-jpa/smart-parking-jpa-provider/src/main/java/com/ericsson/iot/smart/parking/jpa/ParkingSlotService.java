package com.ericsson.iot.smart.parking.jpa;

import java.util.List;
import java.util.Map;

public interface ParkingSlotService {

    List<ParkingSlotStatus> list();

    ParkingSlotStatus get(String parkingSlotId);

    void add(ParkingSlotStatus parkingSlotStatus);

	void add(SlotEventLog slotEventLog);

	List<SlotEventLog> listEvents();

	Long getLotStatus(String lot);

	Long getFloorStatus(String lot, Integer floor);

	Map<String, Boolean> getLots();

	Map<Integer, Boolean> getFloors(String lot);

	Map<Integer, Boolean> getRows(String lotId, Integer floorId);

	Map<String, Boolean> getSlots(String lotId, Integer floorId, Integer rowId);

}
