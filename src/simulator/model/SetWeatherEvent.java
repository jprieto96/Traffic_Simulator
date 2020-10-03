package simulator.model;

import java.util.List;

import simulator.exceptions.EventExecuteException;
import simulator.exceptions.SetWeatherException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String, Weather>> ws;

	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		if(ws == null) throw new IllegalArgumentException("The argument of weatherEvent is null.");
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) throws EventExecuteException {
		try {
			Road road;
			for (Pair<String, Weather> w : ws) {
				road = map.getRoad(w.getFirst());
				if(road != null) road.setWeather(w.getSecond());
				else throw new IllegalArgumentException("Road with ID (" + w.getFirst() + ") does not exist in RoadMap.");
			}
		}
		catch(SetWeatherException e) {
			throw new EventExecuteException("Error executing the new vehicle event", e.getCause());
		}
	}
	
	@Override
	public String toString() {
		String data = "[";
		for (int i = 0; i < ws.size(); ++i) {
			if(i == ws.size() - 1)
				data += "(" + ws.get(i).getFirst() + "," + ws.get(i).getSecond() + ")]";
			else
				data += "(" + ws.get(i).getFirst() + "," + ws.get(i).getSecond() + "), ";
		}
		return "Change Weather: " + data;
	}

}
