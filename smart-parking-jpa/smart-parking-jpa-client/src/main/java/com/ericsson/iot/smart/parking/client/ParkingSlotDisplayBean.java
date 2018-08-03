package com.ericsson.iot.smart.parking.client;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.StringJoiner;

import com.ericsson.iot.smart.parking.jpa.ParkingSlot;
import com.ericsson.iot.smart.parking.jpa.ParkingSlotService;
import com.ericsson.iot.smart.parking.jpa.ParkingSlotStatus;
import com.ericsson.iot.smart.parking.jpa.SlotEventLog;

public class ParkingSlotDisplayBean {

    private ParkingSlotService parkingSlotService;
    private ShowDetail thread;

    public void setParkingSlotService(ParkingSlotService parkingSlotService) {
        this.parkingSlotService = parkingSlotService;
    }

    public void start() {
        thread = new ShowDetail(parkingSlotService);
        thread.start();
    }

    public void stop() {
        thread.terminate();
    }

    private class ShowDetail extends Thread {

        private ParkingSlotService parkingSlotService;
        private volatile boolean running = true;

        public ShowDetail(ParkingSlotService parkingSlotService) {
			this.parkingSlotService = parkingSlotService;
		}

		@Override
        public void run() {
            while (running) {
                try {
                    if (parkingSlotService != null) {

                        ParkingSlotStatus parkingSlotStatus = new ParkingSlotStatus();
                        ParkingSlot parkingSlot = new ParkingSlot();
                        int floor = getRandomNumberInRange(1, 2);
                        int row = getRandomNumberInRange(1, 10);
                        int slot = getRandomNumberInRange(1, 15);
                        parkingSlot.setId("IN01:F"+floor+":R"+row+":S"+slot);
						parkingSlotStatus.setParkingSlot(parkingSlot);
						parkingSlotStatus.setStatus(true);
						System.out.println("Before Update.");
						parkingSlotService.add(parkingSlotStatus);
						
                        System.out.println(displaySlotStatus());
                        
                        
                        SlotEventLog eventLog = new SlotEventLog();
                        Timestamp eventTime = Timestamp.from(Instant.now());
						eventLog.setEventTimeStart(eventTime);
						eventLog.setStatus(getRandomNumberInRange(0, 1) == 1);
						eventLog.setParkingSlot(parkingSlot);
						parkingSlotService.add(eventLog);
						displayEventLog();
                    }
                } catch (Exception e) {
                	System.out.println(e.toString());
                	System.out.println(e.getMessage());
                	System.out.println(e.getStackTrace().toString());
                    System.err.println(e);
                }
                
                try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }

        private void displayEventLog() {
        	 if (parkingSlotService != null) {
                 StringJoiner joiner = new StringJoiner("|");
                 for (SlotEventLog slotEventLog : parkingSlotService.listEvents()) {
                     joiner.add(slotEventLog.getParkingSlot().getId()).add(slotEventLog.getEventTimeStart().toString()).add(slotEventLog.getStatus().toString()).add("\n");
                 }
                 System.out.println("Event History:");
                 System.out.println(joiner.toString());
             } else {
                 System.out.println("There is no event!");
             }
		}

		private String displaySlotStatus() {
            if (parkingSlotService != null) {
                StringBuilder builder = new StringBuilder();
                for (ParkingSlotStatus parkingSlotStatus : parkingSlotService.list()) {
                    builder.append(parkingSlotStatus.getParkingSlot().getId()).append(" | ").append(parkingSlotStatus.getStatus()).append("\n");
                }
                return builder.toString();
            } else {
                return "nothing!";
            }
        }

        public void terminate() {
            running = false;
        }

    }

    private static int getRandomNumberInRange(int min, int max) {
    	
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();

	}
}
