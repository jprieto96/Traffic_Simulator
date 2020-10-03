package simulator.model;

import simulator.exceptions.ArgumentsException;

public class InterCityRoad extends Road{

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws ArgumentsException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}

	@Override
	void reduceTotalContamination() {
		totalContamination = (int) (((100.0 - weather.getXInterCityRoads())/100.0) * totalContamination);
	}

	@Override
	void updateSpeedLimit() {
		if(totalContamination > contLimit) actualSpeedLimit = (int)(maxSpeed*0.5);
		else actualSpeedLimit = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (weather == Weather.STORM) ? (int) (actualSpeedLimit * 0.8) : actualSpeedLimit;
	}

}
