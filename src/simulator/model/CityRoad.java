package simulator.model;

import simulator.exceptions.ArgumentsException;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws ArgumentsException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		totalContamination -= weather.getxCityRoads();
		if(totalContamination < 0) totalContamination = 0;
	}

	@Override
	void updateSpeedLimit() {
		actualSpeedLimit = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (int) (((11.0 - v.getContClass())/11.0) * actualSpeedLimit);
	}

}
