package simulator.model;

import simulator.exceptions.ArgumentsException;
import simulator.exceptions.EventExecuteException;

public class NewCityRoadEvent extends NewRoadEvent {

	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	protected Road createRoadObject() throws EventExecuteException {
		try {
			return new CityRoad(id, srcJunc, destJunc, maxSpeed, co2Limit, length, weather);
		} catch (ArgumentsException e) {
			throw new EventExecuteException("Error executing the new city road event", e.getCause());
		}
	}

}
