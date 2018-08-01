package com.ericsson.iot.smart.parking.rest.provider;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import com.ericsson.iot.smart.parking.EntityStatus;
import com.ericsson.iot.smart.parking.ParkingSlotDTO;
import com.ericsson.iot.smart.parking.ParkingSlotManagement;
import com.ericsson.iot.smart.parking.jpa.ParkingSlotService;
import com.ericsson.iot.smart.parking.rest.util.ParkingSlotDTOUtil;

@Path("/smartParking")
@CrossOriginResourceSharing(allowAllOrigins = true)
public class ParkingSlotManagementRestProvider implements ParkingSlotManagement {
    
    private final Map<String, ParkingSlotDTO> parkingSlotDTOs = new HashMap<>();
    private ParkingSlotService parkingSlotService;
    

    @Override
    @Path("/")
    @Produces("application/json")
    @GET
    public Collection<ParkingSlotDTO> list() {
    	return parkingSlotDTOs.values();
    }

    @Override
    @Path("/{id}")
    @Produces("application/json")
    @GET
    public ParkingSlotDTO get(@PathParam("id") String id) {
        return ParkingSlotDTOUtil.getDTO(parkingSlotService.get(id));
    }

    @Override
    @Path("/lot/{id}")
    @Produces("application/json")
    @GET
    public Long getLotStatus(@PathParam("id") String id) {
    	return parkingSlotService.getLotStatus(id);
    }

    @Override
    @Path("/lots")
    @Produces("application/json")
    @GET
    public List<EntityStatus> getLots() {
    	return ParkingSlotDTOUtil.getEntityStatus(parkingSlotService.getLots());
    }

    @Override
    @Path("lot/{lotId}/floors")
    @Produces("application/json")
    @GET
    public List<EntityStatus> getFloors(@PathParam("lotId") String lotId) {
    	return ParkingSlotDTOUtil.getEntityStatus(parkingSlotService.getFloors(lotId));
    }

    @Override
    @Path("lot/{lotId}/floor/{floorId}/rows")
    @Produces("application/json")
    @GET
    public List<EntityStatus> getRows(@PathParam("lotId") String lotId, @PathParam("floorId") Integer floorId) {
    	return ParkingSlotDTOUtil.getEntityStatus(parkingSlotService.getRows(lotId, floorId));
    }

    @Override
    @Path("lot/{lotId}/floor/{floorId}/row/{rowId}/slots")
    @Produces("application/json")
    @GET
    public List<EntityStatus> getSlots(@PathParam("lotId") String lotId, @PathParam("floorId") Integer floorId, @PathParam("rowId") Integer rowId) {
    	return ParkingSlotDTOUtil.getEntityStatus(parkingSlotService.getSlots(lotId, floorId, rowId));
    }

	@Override
    @Path("/lot/{lotId}/floor/{floorId}")
    @Produces("application/json")
    @GET
    public Long getFloorStatus(@PathParam("lotId") String lotId, @PathParam("floorId") Integer floorId) {
    	return parkingSlotService.getFloorStatus(lotId, floorId);
    }
    
    @Override
    @Path("/")
    @Consumes("application/json")
    @PUT
    public void updateStatus(ParkingSlotDTO parkingSlotDTO) {
//        parkingSlotDTOs.put(parkingSlotDTO.getSlotId(), parkingSlotDTO);
        parkingSlotService.add(ParkingSlotDTOUtil.getParkingSlotStatus(parkingSlotDTO));
        parkingSlotService.add(ParkingSlotDTOUtil.getEventLog(parkingSlotDTO));
    }

    @Override
    @Path("/byId")
    @Consumes("application/json")
    @PUT
    public void updateStatus(String parkingSlotId, Boolean status, Date timestamp) {
    	ParkingSlotDTO parkingSlotDTO = new ParkingSlotDTO();
    	parkingSlotDTO.init(parkingSlotId, status, timestamp);
		updateStatus(parkingSlotDTO);
    }

	public void setParkingSlotService(ParkingSlotService parkingSlotService) {
		this.parkingSlotService = parkingSlotService;
	}
	
}
