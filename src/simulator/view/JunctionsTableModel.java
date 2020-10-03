package simulator.view;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 691175981606948320L;
	
	private List<Junction> junctionsList;
	private String[] columnsName = { "Id", "Green", "Queues" };
	
	public JunctionsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return junctionsList.size();
	}

	@Override
	public int getColumnCount() {
		return columnsName.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnsName[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= junctionsList.size()) return null;
		Junction j = junctionsList.get(rowIndex);
		
		if(columnIndex == 0) return j.getId();
		else if(columnIndex == 1) return getGreenOfJunction(j);
		else if(columnIndex == 2) return getJunctionQueues(j);
		else return null;
	}
	
	private String getJunctionQueues(Junction j) {
		String junctionQueue = "";
		List<Vehicle> vehicles;
		for (Road r : j.getRoadQueueMap().keySet()) {
			junctionQueue += r.getId() + ":[";
			vehicles = j.getRoadQueueMap().get(r);
			for (int i = 0; i < vehicles.size(); ++i) {
				if(i == vehicles.size() - 1) junctionQueue += vehicles.get(i).getId();
				else junctionQueue += vehicles.get(i).getId() + ", ";
			}
			junctionQueue += "]";
		}
		return junctionQueue;
	}

	private String getGreenOfJunction(Junction j) {
		return (j.getGreenLightInd() != -1) ? j.getEntryRoads().get(j.getGreenLightInd()).getId() : "NONE";
	}

	private void update(RoadMap map) {
		junctionsList = map.getJunctionList();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error message", JOptionPane.ERROR_MESSAGE);
	}

	
}
