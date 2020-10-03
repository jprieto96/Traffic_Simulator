package simulator.view;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel  extends AbstractTableModel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 9068611447830000689L;
	
	private List<Road> roadsList;
	private String[] columnsName = { "Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };
	
	public RoadsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return roadsList.size();
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
		if(rowIndex >= roadsList.size()) return null;
		Road r = roadsList.get(rowIndex);
		
		if(columnIndex == 0) return r.getId();
		else if(columnIndex == 1) return r.getLength();
		else if(columnIndex == 2) return r.getWeather();
		else if(columnIndex == 3) return r.getMaxSpeed();
		else if(columnIndex == 4) return r.getActualSpeedLimit();
		else if(columnIndex == 5) return r.getTotalContamination();
		else if(columnIndex == 6) return r.getContLimit();
		else return null;
	}
	
	private void update(RoadMap map) {
		roadsList = map.getRoadList();
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
