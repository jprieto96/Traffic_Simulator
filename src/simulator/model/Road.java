package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.exceptions.AddContaminationException;
import simulator.exceptions.AdvanceRoadException;
import simulator.exceptions.AdvanceVehicleException;
import simulator.exceptions.ArgumentsException;
import simulator.exceptions.SetWeatherException;
import simulator.exceptions.VehicleEnterException;
import simulator.misc.SortVehicleByLocation;

public abstract class Road extends SimulatedObject {
	
	private Junction srcJunc;
	private Junction destJunc;
	private int length;
	protected int maxSpeed;
	protected int actualSpeedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int totalContamination;
	private List<Vehicle> vehicles;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws ArgumentsException {
		super(id);
		if(checkArguments(srcJunc, destJunc, maxSpeed, contLimit, length, weather)) {
			this.srcJunc = srcJunc;
			this.destJunc = destJunc;
			this.maxSpeed = maxSpeed;
			this.contLimit = contLimit;
			this.length = length;
			this.weather = weather;
			this.actualSpeedLimit = maxSpeed;
			this.vehicles = new ArrayList<Vehicle>();
		}
		else throw new ArgumentsException("Incorrect arguments.");
	}
	
	public int getLength() {
		return length;
	}
	
	public Weather getWeather() {
		return weather;
	}
	
	public Junction getDestJunc() {
		return destJunc;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getActualSpeedLimit() {
		return actualSpeedLimit;
	}
	
	public Junction getSrcJunc() {
		return srcJunc;
	}
	
	public int getTotalContamination() {
		return totalContamination;
	}
	
	public int getContLimit() {
		return contLimit;
	}
	
	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}

	private boolean checkArguments(Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		return weather != null && srcJunc != null && destJunc != null && length > 0 && maxSpeed > 0 && contLimit >= 0;
	}
	
	void enter(Vehicle v) throws VehicleEnterException {
		if(v.getLocation() == 0 && v.getActualSpeed() == 0)
			vehicles.add(v);
		else
			throw new VehicleEnterException("Vehicle can�t enter the road");
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather weather) throws SetWeatherException {
		if(weather != null) this.weather = weather;
		else throw new SetWeatherException("Invalid value of weather");
	}
	
	void addContamination(int totalContamination) throws AddContaminationException {
		if(totalContamination >= 0) this.totalContamination += totalContamination;
		else throw new AddContaminationException("Invalid value of total contamination");
	}

	@Override
	void advance(int time) throws AdvanceRoadException {
		try {
			reduceTotalContamination();
			updateSpeedLimit();
			for (Vehicle v : vehicles) {
				v.setActualSpeed(calculateVehicleSpeed(v));
				v.advance(time);
			}
			vehicles.sort(new SortVehicleByLocation());
		}
		catch(AdvanceVehicleException | ArgumentsException ex) {
			throw new AdvanceRoadException("Road can not advance", ex.getCause());
		}
	}
	
	@Override
	public JSONObject report() {
		JSONObject j1 = new JSONObject();
		JSONArray ja = new JSONArray();
		j1.put("id", this._id);
		j1.put("speedlimit", this.actualSpeedLimit);
		j1.put("weather", this.weather);
		j1.put("co2", this.totalContamination);
		for(Vehicle v : vehicles)
			ja.put(v.getId());
		j1.put("vehicles", ja);
		return j1;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);

}
