package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data == null) return null;
		else {
			List<String> itinerary = new ArrayList<String>();
			JSONArray array = data.getJSONArray("itinerary");
			for (int i = 0; i < array.length(); i++)
				itinerary.add(array.getString(i));
			return new NewVehicleEvent(	data.getInt("time"),
										data.getString("id"),
										data.getInt("maxspeed"),
										data.getInt("class"),
										itinerary);
		}
	}

}
