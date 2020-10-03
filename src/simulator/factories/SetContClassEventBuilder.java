package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if(data == null) return null;
		else {
			JSONArray array = data.getJSONArray("info");
			List<Pair<String,Integer>> cs = new ArrayList<Pair<String,Integer>>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				cs.add(new Pair<String,Integer>(ob.getString("vehicle"), ob.getInt("class")));
			}
			return new SetContClassEvent(data.getInt("time"), cs);
		}
	}

}
