package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{
	
	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheRoad() {
		return new NewInterCityRoadEvent(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

}
