package com.ericsson.iot.smart.parking.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class ParkingSlotServiceImpl implements ParkingSlotService {

    @PersistenceContext(unitName = "smartParking")
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public void add(ParkingSlotStatus parkingSlotStatus) {
    	System.out.println("Inside Update.");
        ParkingSlotStatus slotStatus = entityManager.find(ParkingSlotStatus.class, parkingSlotStatus.getParkingSlot().getId());
        System.out.println("Entity Found =" + (slotStatus != null));
        if (slotStatus != null) {
        	System.out.println("Entity will be updated.");
        	slotStatus.setStatus(parkingSlotStatus.getStatus());
        	entityManager.merge(slotStatus);
        	System.out.println("Entity has been updated.");
		}else
		{
			System.out.println("Entity will be inserted.");
			entityManager.persist(parkingSlotStatus);
			System.out.println("Entity has been inserted.");
		}
    }
    
    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public void add(SlotEventLog slotEventLog) {
    	System.out.println("Inside SlotEventLog Update.");
        ParkingSlot slot= entityManager.find(ParkingSlot.class, slotEventLog.getParkingSlot().getId());
        System.out.println("ParkingSlotStatus Found =" + (slot != null));
        if (slot != null) {
			slotEventLog.setParkingSlot(slot);
		}
		System.out.println("slotEventLog will be inserted.");
		entityManager.persist(slotEventLog);
		System.out.println("slotEventLog has been inserted.");
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<ParkingSlotStatus> list() {
    	System.out.println("Entity Manager is open :" + entityManager.isOpen());
        TypedQuery<ParkingSlotStatus> query = entityManager.createQuery("SELECT p FROM ParkingSlotStatus p", ParkingSlotStatus.class);
        System.out.println("Query Object :" + query.toString());
        return query.getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public List<SlotEventLog> listEvents() {
    	TypedQuery<SlotEventLog> query = entityManager.createQuery("SELECT s FROM SlotEventLog s", SlotEventLog.class);
    	return query.getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public ParkingSlotStatus get(String id) {
        TypedQuery<ParkingSlotStatus> query = entityManager.createQuery("SELECT p FROM ParkingSlotStatus p WHERE p.id=:id", ParkingSlotStatus.class);
        query.setParameter("id", id);
        ParkingSlotStatus parkingSlotStatus = null;
        try {
            parkingSlotStatus = query.getSingleResult();
        } catch (NoResultException e) {
            System.err.println(e);
        }
        return parkingSlotStatus;
    }
    
    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public Long getLotStatus(String lot) {
    	TypedQuery<Long> query = entityManager.createQuery("SELECT count(1) FROM ParkingSlotStatus p, ParkingSlot s "
    																	+ "WHERE s.lot=:lot and p.id = s.id and p.status = :status",
    																	Long.class);
    	Long parkingSlotCount = null;
    	query.setParameter("lot", lot);
    	query.setParameter("status", Boolean.TRUE);
		try {
			parkingSlotCount = query.getSingleResult();
    	} catch (NoResultException e) {
    		System.err.println(e);
    	}
    	return parkingSlotCount;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    @Override
    public Long getFloorStatus(String lot, Integer floor) {
    	TypedQuery<Long> query = entityManager.createQuery("SELECT count(1) FROM ParkingSlotStatus p, ParkingSlot s "
    			+ "WHERE s.lot=:lot and p.id = s.id and p.status = :status and s.floor = :floor",
    			Long.class);
    	Long floorStatus = null;
    	query.setParameter("lot", lot);
    	query.setParameter("status", Boolean.TRUE);
    	query.setParameter("floor", floor);
    	try {
    		floorStatus = query.getSingleResult();
    	} catch (NoResultException e) {
    		System.err.println(e);
    	}
    	return floorStatus;
    }

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Boolean> getLots() {
		 Query query = entityManager.createQuery("SELECT s.lot, max(p.status) FROM ParkingSlotStatus p, ParkingSlot s "
    			+ "WHERE p.id = s.id group by s.lot");
		 List<Object[]> resultList = query.getResultList();
		 
		 Map<String, Boolean> lots = new HashMap<>();
		 for (Object[] item : resultList) 
		 {
			 lots.put((String)item[0], (Boolean)item[1]);
		 }
		
		return lots;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Boolean> getFloors(String lot)
	{
		 Query query = entityManager.createQuery("SELECT s.floor, max(p.status) FROM ParkingSlotStatus p, ParkingSlot s "
	    			+ "WHERE p.id = s.id and s.lot = :lot group by s.floor");
		 
		 query.setParameter("lot", lot);
		 List<Object[]> resultList = query.getResultList();
		 
		 Map<Integer, Boolean> floors = new HashMap<>();
		 for (Object[] item : resultList) 
		 {
			 floors.put((Integer)item[0], (Boolean)item[1]);
		 }
		
		return floors;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, Boolean> getRows(String lot, Integer floor)
	{
		Query query = entityManager.createQuery("SELECT s.row, max(p.status) FROM ParkingSlotStatus p, ParkingSlot s "
												+ "WHERE p.id = s.id and s.lot = :lot and s.floor = :floor group by s.row");
		
		query.setParameter("lot", lot);
		query.setParameter("floor", floor);
		List<Object[]> resultList = query.getResultList();
		
		Map<Integer, Boolean> rows = new HashMap<>();
		for (Object[] item : resultList) 
		{
			rows.put((Integer)item[0], (Boolean)item[1]);
		}
		
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Boolean> getSlots(String lot, Integer floor, Integer row)
	{
		Query query = entityManager.createQuery("SELECT s.slot, p.status FROM ParkingSlotStatus p, ParkingSlot s "
												+ "WHERE p.id = s.id and s.lot = :lot and s.floor = :floor and s.row = :row");
		
		query.setParameter("lot", lot);
		query.setParameter("floor", floor);
		query.setParameter("row", row);
		List<Object[]> resultList = query.getResultList();
		
		Map<String, Boolean> slots = new HashMap<>();
		for (Object[] item : resultList) 
		{
			slots.put((String)item[0], (Boolean)item[1]);
		}
		
		return slots;
	}
	
	
}
