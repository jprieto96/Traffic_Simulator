package simulator.misc;

import java.util.Comparator;

import simulator.model.Vehicle;

public class SortVehicleByLocation implements Comparator <Vehicle> {

	@Override
	public int compare(Vehicle arg0, Vehicle arg1) {
		if(arg0.getLocation() > arg1.getLocation()) return -1;
		else if(arg0.getLocation() < arg1.getLocation()) return 1;
		else return 0;
	}
}
