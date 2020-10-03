package simulator.model;

import java.util.List;

import simulator.exceptions.ArgumentsException;
import simulator.exceptions.EventExecuteException;
import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> cs;

	public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		if(cs == null) throw new IllegalArgumentException("The argument of const class event is null");
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) throws EventExecuteException {
		try {
			Vehicle vehicle;
			for (Pair<String, Integer> c : cs) {
				vehicle = map.getVehicle(c.getFirst());
				if(vehicle != null) vehicle.setContClass(c.getSecond());
				else throw new ArgumentsException("Vehicle with ID (" + c.getFirst() + ") does not exist in RoadMap.");
			}
		}
		catch(ArgumentsException ex) {
			throw new EventExecuteException("Error executing the set cont class event", ex.getCause());
		}
	}
	
	@Override
	public String toString() {
		String data = "[";
		for (int i = 0; i < cs.size(); ++i) {
			if(i == cs.size() - 1)
				data += "(" + cs.get(i).getFirst() + "," + cs.get(i).getSecond() + ")]";
			else
				data += "(" + cs.get(i).getFirst() + "," + cs.get(i).getSecond() + "), ";
		}
		return "Change CO2 class: " + data;
	}

}
