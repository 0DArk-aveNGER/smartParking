package com.ericsson.iot.smart.parking.rest.client;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import com.ericsson.iot.smart.parking.EntityStatus;
import com.ericsson.iot.smart.parking.ParkingSlotDTO;

public class ParkingOperator {

    private DetailDisplay detailDisplay;
    
    private WebClient client;
    
    public void setClient(WebClient client) {
        this.client = client;
    }
    
    public void start() {
    	detailDisplay = new DetailDisplay();
    	detailDisplay.start();
    }

    public void stop() {
    	detailDisplay.terminate();
    }
    private class DetailDisplay extends Thread {
        
        private volatile boolean running = true;
        
        @Override
        public void run() {
            
            while (running) {
                try {
                	printParkingLotStatus();
               	 	ParkingSlotDTO updateDTO = updateDTO();
               	 	printFetchedDTO(updateDTO.getSlotId());
               	 	printLotStatus(updateDTO.getLot());
               	 	printAllFloorStatus(updateDTO.getLot());
               	 	printFloorStatus(updateDTO.getLot(), updateDTO.getFloor());
               	 	printRows(updateDTO.getLot(), updateDTO.getFloor());
               	 	printSlots(updateDTO.getLot(), updateDTO.getFloor(), updateDTO.getRow());

               
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException exception) {
                    // nothing to do
                }
            }
            
            client.close();
        }

        private void printSlots(String lot, Integer floor, Integer row) {
        	System.out.println("\n Call GET " + client.getBaseURI() + "/lot/" + lot + "/floor/" + floor + "/row/" + row + "/slots");
        	List<EntityStatus> rows = client.replacePath("/lot/" + lot + "/floor/" + floor + "/row/" + row + "/slots")
        										.accept(MediaType.APPLICATION_JSON)
        										.get(new GenericType<List<EntityStatus>>(){});
        	
        	if (rows != null) {
        		rows.forEach(e -> {System.out.println("Lot->" + lot + ", Floor-> " + floor 
        												+ ", Row->" + row + ", slot " + e.getId()  
        												+ " is " + (e.getStatus()  ? "vacant" : "occupied."));});
        		
        	} else {
        		System.out.println("Response is empty");
        	}
        	
        }

		private void printRows(String lot, Integer floor) {
        	System.out.println("\n Call GET " + client.getBaseURI() + "/lot/" + lot + "/floor/" + floor + "/rows");
        	List<EntityStatus> rows = client.replacePath("/lot/" + lot + "/floor/" + floor + "/rows")
        										.accept(MediaType.APPLICATION_JSON)
        										.get(new GenericType<List<EntityStatus>>(){});
        	
        	if (rows != null) {
        		rows.forEach(e -> {System.out.println("Lot->" + lot + ", Floor-> " + floor + ", Row " + e.getId() + " is " + (e.getStatus()  ? "vacant" : "occupied."));});
        		
        	} else {
        		System.out.println("Response is empty");
        	}
        	
        }

		private void printAllFloorStatus(String lot) {
        	System.out.println("\n Call GET " + client.getBaseURI() + "/lot/" + lot + "/floors");
        	List<EntityStatus> floors = client.replacePath("/lot/" + lot + "/floors").accept(MediaType.APPLICATION_JSON).get(new GenericType<List<EntityStatus>>(){});
        	
        	if (floors != null) {
        		floors.forEach(e -> {System.out.println("Lot->" + lot + ", Floor " + e.getId() + " is " + (e.getStatus()  ? "vacant" : "occupied."));});
        		
        	} else {
        		System.out.println("Response is empty");
        	}
        	
        }

		private void printParkingLotStatus() {
        	System.out.println("\n Call GET " + client.getBaseURI() + "/lots");
        	List<EntityStatus> lots = client.replacePath("/lots").accept(MediaType.APPLICATION_JSON).get(new GenericType<List<EntityStatus>>(){});
        	
        	if (lots != null) {
        		lots.forEach(e -> {System.out.println("Lot " + e.getId() + " is " + (e.getStatus()  ? "vacant" : "occupied."));});
        		
        	} else {
        		System.out.println("Response is empty");
        	}
        	
        }

		public void terminate() {
            running = false;
        }
        
        private ParkingSlotDTO updateDTO()
        {
        	// update status
            ParkingSlotDTO parkingSlotDTO = new ParkingSlotDTO();
            parkingSlotDTO.setLot(getRandomNumberInRange(0, 1) == 0 ? "IN" :"OUT");
            if (parkingSlotDTO.getLot() == "OUT") {
            	parkingSlotDTO.setFloor(0);
            	parkingSlotDTO.setRow(getRandomNumberInRange(0, 20));
            	parkingSlotDTO.setSlot(getRandomNumberInRange(0, 10)+(getRandomNumberInRange(0, 1)==0?"A":"B"));
			}else{
				parkingSlotDTO.setFloor(getRandomNumberInRange(0, 1));
				parkingSlotDTO.setRow(getRandomNumberInRange(0, 10));
				parkingSlotDTO.setSlot(getRandomNumberInRange(0, 15)+(getRandomNumberInRange(0, 1)==0?"A":"B"));
			}
            parkingSlotDTO.setStatus(getRandomNumberInRange(0, 1) == 1);
            parkingSlotDTO.setTimestamp(Date.from(Instant.now()));
            System.out.println("\n Call PUT id= " + parkingSlotDTO.getSlotId() + client.getBaseURI());
            
            client.header("Content-Type", "application/json");
            client.replacePath("/").accept(MediaType.APPLICATION_JSON).put(parkingSlotDTO,ParkingSlotDTO.class);
            
            return parkingSlotDTO;
        }
        
        private void printFetchedDTO(String id)
        {
        	 // get parkingSlot with id='@id'
            System.out.println("\n Call GET " + client.getBaseURI() + "/" + id);
            ParkingSlotDTO updatedParkingSlotDTO = client.replacePath("/"+id).accept(MediaType.APPLICATION_JSON)
                    .get(ParkingSlotDTO.class);
            
            if (updatedParkingSlotDTO != null) {
                System.out.println("Parking Slot " + updatedParkingSlotDTO.getSlotId() + " is " + (updatedParkingSlotDTO.getStatus()? "vacant" : "occupied.") + " at " + 
                			(updatedParkingSlotDTO.getTimestamp() == null ? "N/A" : updatedParkingSlotDTO.getTimestamp().toString()));
            } else {
                System.out.println("Response is empty");
            }
            
        }
        
        private void printLotStatus(String id)
        {
        	System.out.println("\n Call GET " + client.getBaseURI() + "/lot/" + id);
        	Long lotStatus = client.replacePath("/lot/"+id).accept(MediaType.APPLICATION_JSON).get(Long.class);
        	
        	if (lotStatus != null) {
        		System.out.println("Lot " + id + " is " + (lotStatus.compareTo(0L) != 0  ? "vacant" : "occupied."));
        	} else {
        		System.out.println("Response is empty");
        	}
        	
        }
        
        private void printFloorStatus(String lotId, Integer floorId)
        {
        	System.out.println("\n Call GET " + client.getBaseURI() + "/lot/" + lotId+ "/floor/" + floorId);
        	Long floorStatus = client.replacePath("/lot/" + lotId+ "/floor/"+floorId).accept(MediaType.APPLICATION_JSON).get(Long.class);
        	
        	if (floorStatus != null) {
        		System.out.println("Lot " + lotId +  ", floor " + floorId + " is " + (floorStatus.compareTo(0L) != 0  ? "vacant" : "occupied."));
        	} else {
        		System.out.println("Response is empty");
        	}
        	
        }

    }
    
    private static int getRandomNumberInRange(int min, int max) {
    	
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}

}
