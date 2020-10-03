package simulator.factories;


import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder{
	
	public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	@Override
	protected Event createTheRoad() {
		return new NewCityRoadEvent(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

}
