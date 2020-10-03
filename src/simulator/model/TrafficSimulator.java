package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.AdvanceJunctionException;
import simulator.exceptions.AdvanceRoadException;
import simulator.exceptions.AdvanceTrafficSimulatorException;
import simulator.exceptions.EventExecuteException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
	private RoadMap roadMap;
	private List<Event> eventList;
	private int simulationTime;	
	private List<TrafficSimObserver> observerList;
	
	//TODO hay que llamar a los observadores OnError cuando llegue una excepcion
	public TrafficSimulator() {
		roadMap = new RoadMap(new ArrayList<Junction>(), new ArrayList<Road>(), new ArrayList<Vehicle>(),
				new HashMap<String, Junction>(), new HashMap<String, Road>(), new HashMap<String, Vehicle>());
		eventList = new SortedArrayList<Event>();
		observerList = new ArrayList<TrafficSimObserver>();
		simulationTime = 0;
	}
	
	public void addEvent(Event e) {
		eventList.add(e);
		for (TrafficSimObserver observer : observerList)
			observer.onEventAdded(roadMap, eventList, e, simulationTime);
	}

	public void advance() throws AdvanceTrafficSimulatorException {
		try {
			simulationTime++;
			
			for (TrafficSimObserver observer : observerList)
				observer.onAdvanceStart(roadMap, eventList, simulationTime);
			
			for(int i = 0; i < eventList.size(); i++) {
				if(eventList.get(i)._time == this.simulationTime) {
					eventList.get(i).execute(roadMap);
					eventList.remove(i);
					--i;
				}
			}
			for (Junction j : roadMap.getJunctionList())
				j.advance(simulationTime);
			for(Road r : roadMap.getRoadList())
				r.advance(simulationTime);
			
			for (TrafficSimObserver observer : observerList)
				observer.onAdvanceEnd(roadMap, eventList, simulationTime);
		}
		catch(EventExecuteException | AdvanceJunctionException | AdvanceRoadException ex) {
			for (TrafficSimObserver observer : observerList)
				observer.onError(ex.getMessage());
			throw new AdvanceTrafficSimulatorException("Traffic simulator can not advance", ex.getCause());
		}
	}
	
	public void reset() {
		roadMap.reset();
		eventList.clear();
		simulationTime = 0;
		for (TrafficSimObserver observer : observerList)
			observer.onReset(roadMap, eventList, simulationTime);
	}

	public JSONObject report() {
		
		JSONObject j1 = new JSONObject();
		
		j1.put("time", this.simulationTime);
		j1.put("state", this.roadMap.report());
		
		return j1;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observerList.add(o);
		for (TrafficSimObserver observer : observerList)
			observer.onRegister(roadMap, eventList, simulationTime);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observerList.remove(o);
	}
}
