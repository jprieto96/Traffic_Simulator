package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data == null) return null;
		else {
			JSONArray array = data.getJSONArray("info");
			
			List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				ws.add(new Pair<String, Weather>(ob.getString("road"), Weather.valueOf(ob.getString("weather").toUpperCase())));
			}
			return new SetWeatherEvent(data.getInt("time"), ws);
		}
			
			
	}
}
