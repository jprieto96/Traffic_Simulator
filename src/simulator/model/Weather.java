package simulator.model;

public enum Weather {
	SUNNY(2, 2),
	CLOUDY(3, 2),
	RAINY(10, 2),
	WINDY(15, 10),
	STORM(20, 10);
	
	private int xInterCityRoads;
	private int xCityRoads;
	
	private Weather(int xInterCityRoads, int xCityRoads) {
		this.xInterCityRoads = xInterCityRoads;
		this.xCityRoads = xCityRoads;
	}

	public int getXInterCityRoads() {
		return xInterCityRoads;
	}
	
	public int getxCityRoads() {
		return xCityRoads;
	}
}
