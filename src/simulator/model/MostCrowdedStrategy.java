package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	private int getIndexOfRoad(List<List<Vehicle>> qs) {
		int index = 0, max = -1;
		for (int i = 0; i < qs.size(); i++) {
			if(qs.get(i).size() > max) {
				max = qs.get(i).size();
				index = i;
			}
		}
		return index;
	}
	
	private int getIndexOfRoadCircular(List<List<Vehicle>> qs, int currGreen) {
		int index = 0, max = -1, init = (currGreen + 1) % qs.size();
		for(int i = 0; i < qs.size(); i++) {
			int aux = (i + init) % qs.size();
			if(qs.get(aux).size() > max) {
				max = qs.get(aux).size();
				index = aux;
			}
		}

		return index;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if(roads.isEmpty()) return -1;
		if(currGreen == -1) return getIndexOfRoad(qs);
		if(currTime - lastSwitchingTime < timeSlot) return currGreen;
		return getIndexOfRoadCircular(qs, currGreen);
	}

}
