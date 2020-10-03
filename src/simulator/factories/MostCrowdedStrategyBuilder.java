package simulator.factories;

import org.json.JSONObject;


import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		if(data == null) return null;
		else {		
			int time = (data.has("timeslot")) ? data.getInt("timeslot") : 1;
			return new MostCrowdedStrategy(time);
		}
	}

}
