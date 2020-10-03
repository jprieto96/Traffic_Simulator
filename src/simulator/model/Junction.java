package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.AddIncomingRoadException;
import simulator.exceptions.AddOutGoingRoadException;
import simulator.exceptions.AdvanceJunctionException;
import simulator.exceptions.ArgumentsException;
import simulator.exceptions.MoveToNextRoadException;

public class Junction extends SimulatedObject {

	private List<Road> entryRoads;
	private Map<Junction,Road> exitRoads;
	private List<List<Vehicle>> queueList;
	private Map<Road,List<Vehicle>> roadQueueMap;
	private int greenLightInd;
	private int lastStepChangeLight;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor, yCoor;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws ArgumentsException {
		super(id);
		if(checkArguments(lsStrategy, dqStrategy, xCoor, yCoor)) {
			this.lsStrategy = lsStrategy;
			this.dqStrategy = dqStrategy;
			this.xCoor = xCoor;
			this.yCoor = yCoor;
			this.roadQueueMap = new HashMap<Road,List<Vehicle>>();
			this.queueList = new ArrayList<List<Vehicle>>();
			this.exitRoads = new HashMap<Junction,Road>();
			this.entryRoads = new ArrayList<Road>();
			this.greenLightInd = -1;
		}
		else throw new ArgumentsException("Incorrect Arguments.");
	}
	
	public int getxCoor() {
		return xCoor;
	}

	public int getyCoor() {
		return yCoor;
	}
	
	public int getGreenLightInd() {
		return greenLightInd;
	}
	
	public List<Road> getEntryRoads() {
		return Collections.unmodifiableList(entryRoads);
	}
	
	public Map<Road, List<Vehicle>> getRoadQueueMap() {
		return Collections.unmodifiableMap(roadQueueMap);
	}

	private boolean checkArguments(LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) {
		return lsStrategy != null && dqStrategy != null && xCoor >= 0 && yCoor >= 0 ;
	}

	void addIncommingRoad(Road r) throws AddIncomingRoadException {
		if(r.getDestJunc() != this)
			throw new AddIncomingRoadException("Destination junction different from actual junction");
		else {
			entryRoads.add(r);
			List<Vehicle> queue = new LinkedList<Vehicle>();
			queueList.add(queue);
			roadQueueMap.put(r, queue);
		}
	}
	
	void addOutGoingRoad(Road r) throws AddOutGoingRoadException {
		boolean correctJunction = true;
		for (Junction key : exitRoads.keySet()) {
			if(exitRoads.get(key).getDestJunc() == r.getDestJunc())
				correctJunction = false;
		}
		if(exitRoads.isEmpty() || (exitRoads.containsValue(r) && correctJunction))
			exitRoads.put(r.getDestJunc(), r);
		else
			throw new AddOutGoingRoadException("The road is not contained in exitsRoads or there is a road headin to the same junction");
	}

	void enter(Vehicle v) {
		for(List<Vehicle> lv : queueList) {
			if(lv == roadQueueMap.get(v.getRoad())) {
				lv.add(v);
			}
		}
	}

	Road roadTo(Junction j) {
		Junction key = null;
		for(Junction junc : exitRoads.keySet()) {
			if(this == exitRoads.get(junc).getSrcJunc() && j == exitRoads.get(junc).getDestJunc())
				key = junc;
		}
		return exitRoads.get(key);	
	}
	
	@Override 
	void advance(int time) throws AdvanceJunctionException {
		try {
			if(greenLightInd != -1) {
				List<Vehicle> listV = dqStrategy.dequeue(queueList.get(greenLightInd));
				for (Vehicle v : listV) {
					v.moveToNextRoad();
					queueList.get(greenLightInd).remove(v);
				}
			}	
			int ind = lsStrategy.chooseNextGreen(entryRoads, queueList, greenLightInd, lastStepChangeLight, time);
			if(ind != greenLightInd) {
				greenLightInd = ind;
				lastStepChangeLight = time;
			}
		}
		catch(MoveToNextRoadException ex) {
			throw new AdvanceJunctionException("The Junction can not advance", ex.getCause());
		}	
	}
	
	@Override
	public JSONObject report() {
		JSONObject j1 = new JSONObject();	//Junction
		JSONObject j2 = new JSONObject();	//Q
		JSONArray ja = new JSONArray();		//queues
		JSONArray ja2 = new JSONArray();	//vehicles

		j1.put("id", this.getId());
		j1.put("green", (greenLightInd != -1) ? this.entryRoads.get(greenLightInd).getId() : "none");
		
		int i = 0;
		for(Road key : roadQueueMap.keySet()) {
			j2.put("road", key.getId());
			for( Vehicle v : roadQueueMap.get(key))
				ja2.put(v.getId());
			j2.put("vehicles", ja2);
			ja.put(i, j2);
			j2 = new JSONObject();
			ja2 = new JSONArray();
			++i;
		}
			
		
		j1.put("queues", ja);
		return j1;
	}

}
