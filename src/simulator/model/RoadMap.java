package simulator.model;

import java.util.Collections;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.ArgumentsException;


public class RoadMap {
	
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String, Junction> junctionMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;
	
	RoadMap(List<Junction> junctionList, List<Road> roadList, List<Vehicle> vehicleList, Map<String, Junction> junctionMap,
	Map<String, Road> roadMap, Map<String, Vehicle> vehicleMap) {
		this.junctionList = junctionList;
		this.roadList = roadList;
		this.vehicleList = vehicleList;
		this.junctionMap = junctionMap;
		this.roadMap = roadMap;
		this.vehicleMap = vehicleMap;
	}
	
	void addJunction(Junction j) {
		if(junctionMap.get(j.getId()) == null) {
			junctionList.add(j);
			junctionMap.put(j.getId(), j);
		}
	}
	
	void addRoad(Road r) throws ArgumentsException {
		if(roadMap.get(r.getSrcJunc().getId()) == null &&
				roadMap.get(r.getDestJunc().getId()) == null &&
				roadMap.get(r.getId()) == null) {
			roadList.add(r);
			roadMap.put(r.getId(), r);
		}
		else throw new ArgumentsException("There is a road with the same ID or junctions of the road doesn�t exist in the road map");
	}
	
	void addVehicle(Vehicle v) throws ArgumentsException {
		if(vehicleMap.get(v.getId()) == null && v.getItinerary().size() > 0) {
			vehicleList.add(v);
			vehicleMap.put(v.getId(), v);
		}
		else throw new ArgumentsException("There is a vehicle with the same ID or the itinerary is not valid");
	}
	
	public Junction getJunction(String id) {
		return junctionMap.get(id);
	}
	
	public Road getRoad(String id) {
		return roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehicleMap.get(id);
	}
	
	public List<Junction> getJunctionList() {
		return Collections.unmodifiableList(junctionList);
	}
	
	public List<Road> getRoadList() {
		return Collections.unmodifiableList(roadList);
	}
	
	public List<Vehicle> getVehicleList() {
		return Collections.unmodifiableList(vehicleList);
	}
	
	void reset() {
		junctionList.clear();
		roadList.clear();
		vehicleList.clear();
		junctionMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}
	
	public JSONObject report() {
		JSONObject j1 = new JSONObject();
		JSONArray ja = new JSONArray();
		JSONArray ja2 = new JSONArray();
		JSONArray ja3 = new JSONArray();
		
		for(int i = 0; i < this.junctionList.size(); i++) {
			ja.put(i, this.junctionList.get(i).report());
		}
		for(int i = 0; i < this.roadList.size(); i++) {
			ja2.put(i, this.roadList.get(i).report());
		}
		for(int i = 0; i < this.vehicleList.size(); i++) {
			ja3.put(i, this.vehicleList.get(i).report());
		}
		
		j1.put("roads", ja2);
		j1.put("vehicles", ja3);
		j1.put("junctions", ja);

		return j1;
	}
	
}
