package simulator.model;

import simulator.exceptions.ArgumentsException;
import simulator.exceptions.EventExecuteException;

public class NewJunctionEvent extends Event {
	
	private String id;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy,
			int xCoor, int yCoor) {
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
	}

	@Override
	void execute(RoadMap map) throws EventExecuteException {
		try {
			map.addJunction(new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor));
		} catch (ArgumentsException e) {
			throw new EventExecuteException("Error executing the new junction event", e.getCause());
		}
	}
	
	@Override
	public String toString() {
		return "New Junction '" + id + "'";
	}

}
