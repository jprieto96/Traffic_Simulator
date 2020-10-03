package simulator.model;

import java.util.ArrayList;
import java.util.List;
import simulator.exceptions.MoveToNextRoadException;
import simulator.exceptions.ArgumentsException;
import simulator.exceptions.EventExecuteException;

public class NewVehicleEvent extends Event {
	
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) throws EventExecuteException {
		try {
			List<Junction> it = new ArrayList<Junction>();
			for (String id : itinerary)
				it.add(map.getJunction(id));
			Vehicle v = new Vehicle(id, maxSpeed, contClass, it);
			map.addVehicle(v);
			v.moveToNextRoad();
		} catch (MoveToNextRoadException | ArgumentsException e) {
			throw new EventExecuteException("Error executing the new vehicle event", e.getCause());
		}
	}
	
	@Override
	public String toString() {
		return "New Vehicle '" + id + "'";
	}

}
