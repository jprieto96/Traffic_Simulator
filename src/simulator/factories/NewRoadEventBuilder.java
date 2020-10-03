package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {
	
	protected String id;
	protected int time;
	protected String srcJunc;
	protected String destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;

	NewRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data == null) return null;
		this.time = data.getInt("time");
		this.id = data.getString("id");
		this.srcJunc = data.getString("src");
		this.destJunc = data.getString("dest");
		this.length = data.getInt("length");
		this.co2Limit = data.getInt("co2limit");
		this.maxSpeed = data.getInt("maxspeed");
		this.weather = Weather.valueOf(data.getString("weather").toUpperCase());
		return createTheRoad();
	}
	
	protected abstract Event createTheRoad();
}
