package simulator.model;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.exceptions.AddContaminationException;
import simulator.exceptions.AdvanceVehicleException;
import simulator.exceptions.ArgumentsException;
import simulator.exceptions.MoveToNextRoadException;
import simulator.exceptions.VehicleEnterException;
import simulator.exceptions.VehicleStatusException;

public class Vehicle extends SimulatedObject {

	private static final String ERROR_SPEED = "maxSpeed has to be a positive number";
	private static final String ERROR_CONTCLASS = "contClass has to be a number between 0 and 10, both included";
	private static final String ERROR_ITINERARY = "itinerary`s size has to be at least 2";

	private List<Junction> itinerary;
	private int maxSpeed;
	private int actualSpeed;
	private VehicleStatus status;
	private Road road;
	private int location;
	private int contClass;
	private int currentJunction;
	private int totalContamination;
	private int totalDistanceTraveled;
	private int distance;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws ArgumentsException {
		super(id);
		if (checkArguments(maxSpeed, contClass, itinerary)) {
			this.maxSpeed = maxSpeed;
			this.contClass = contClass;
			this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
			this.currentJunction = 0;
			this.status = VehicleStatus.PENDING;
		}
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public int getActualSpeed() {
		return actualSpeed;
	}
	
	public int getTotalDistanceTraveled() {
		return totalDistanceTraveled;
	}
	
	public int getCurrentJunction() {
		return currentJunction;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getTotalContamination() {
		return totalContamination;
	}
	
	void setActualSpeed(int actualSpeed) throws ArgumentsException {
		if (checkSpeed(actualSpeed))
			this.actualSpeed = Math.min(actualSpeed, maxSpeed);
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return location;
	}

	public int getContClass() {
		return contClass;
	}

	void setContClass(int contClass) throws ArgumentsException {
		if (checkContClass(contClass))
			this.contClass = contClass;
	}

	@Override
	void advance(int time) throws AdvanceVehicleException {
		try {
			if (status == VehicleStatus.TRAVELING) {
				int pastLocation = location;
				location = Math.min(location + actualSpeed, road.getLength());
				int c = contClass * (location - pastLocation);
				totalContamination += c;
				road.addContamination(c);
				if (location == road.getLength()) {
					itinerary.get(currentJunction).enter(this);
					status = VehicleStatus.WAITING;
					actualSpeed = 0;
				}
			}
			else actualSpeed = 0;
			totalDistanceTraveled = location + distance;
		}
		catch(AddContaminationException ex) {
			throw new AdvanceVehicleException("Vehicle can not advance", ex.getCause());
		}
	}

	// TODO 
	void moveToNextRoad() throws MoveToNextRoadException {
		try {
			if(status == VehicleStatus.PENDING) {
				actualSpeed = 0;
				road = itinerary.get(currentJunction).roadTo(itinerary.get(++currentJunction));
				location = 0;
				road.enter(this);
				status = VehicleStatus.TRAVELING;
			}	
			else if(status == VehicleStatus.WAITING) {
				actualSpeed = 0;
				if(currentJunction < itinerary.size() - 1) {
					road.exit(this);
					road = itinerary.get(currentJunction).roadTo(itinerary.get(++currentJunction));
					location = 0;
					road.enter(this);
					status = VehicleStatus.TRAVELING;
					distance = totalDistanceTraveled;
				}
				else {
					status = VehicleStatus.ARRIVED;
					road.exit(this);
				}
			}
			else throw new VehicleStatusException("Vehicle status is not PENDING or WAITING.");
		}
		catch(VehicleEnterException | VehicleStatusException ex) {
			throw new MoveToNextRoadException("Vehicle can not move to next road.", ex.getCause());
		}
	}
	
	@Override
	public JSONObject report() {
		
		JSONObject j1 = new JSONObject();
		j1.put("id", this._id);
		j1.put("speed", this.actualSpeed);
		j1.put("distance", this.totalDistanceTraveled );
		j1.put("co2", this.totalContamination);
		j1.put("class", this.contClass);
		j1.put("status", this.status);
		if(status != VehicleStatus.PENDING && status != VehicleStatus.ARRIVED) {
			j1.put("road", this.road);
			j1.put("location", this.location);
		}
		return j1;
	}

	private boolean checkArguments(int maxSpeed, int contClass, List<Junction> itinerary) throws ArgumentsException {
		return checkSpeed(maxSpeed) && checkContClass(contClass) && checkItinerary(itinerary);
	}

	private boolean checkItinerary(List<Junction> itinerary) throws ArgumentsException {
		if (itinerary.size() >= 2)
			return true;
		else
			throw new ArgumentsException(ERROR_ITINERARY);
	}

	private boolean checkContClass(int contClass) throws ArgumentsException {
		if (contClass >= 0 && contClass <= 10)
			return true;
		else
			throw new ArgumentsException(ERROR_CONTCLASS);
	}

	private boolean checkSpeed(int speed) throws ArgumentsException {
		if (speed >= 0)
			return true;
		else
			throw new ArgumentsException(ERROR_SPEED);
	}
	@Override
	public String toString() {
		return _id;
	}

}
