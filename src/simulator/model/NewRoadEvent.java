package simulator.model;

import simulator.exceptions.AddIncomingRoadException;
import simulator.exceptions.AddOutGoingRoadException;
import simulator.exceptions.ArgumentsException;
import simulator.exceptions.EventExecuteException;

public abstract class NewRoadEvent extends Event {
	
	protected String id;
	protected Junction srcJunc;
	protected Junction destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed;
	protected Weather weather;
	
	protected String id_srcJunc;
	protected String id_destJunc;
	

	public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);
		this.id = id;
		this.id_srcJunc = srcJunc;
		this.id_destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}
	
	@Override
	void execute(RoadMap map) throws EventExecuteException {
		try {
			srcJunc = map.getJunction(id_srcJunc);
			destJunc = map.getJunction(id_destJunc);
			map.addRoad(createRoadObject());
			srcJunc.addOutGoingRoad(map.getRoad(id));
			destJunc.addIncommingRoad(map.getRoad(id));
		}
		catch (EventExecuteException | ArgumentsException | AddIncomingRoadException | AddOutGoingRoadException e) {
			throw new EventExecuteException("Error executing the new city road event", e.getCause());
		}
	}
	
	@Override
	public String toString() {
		return "New Road '" + id + "'";
	}
	
	protected abstract Road createRoadObject() throws EventExecuteException;

}
