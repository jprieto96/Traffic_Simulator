package simulator.view;

import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1842787689590022282L;
	
	private List<Event> eventList;
	private String[] columns = { "Time", "Desc." };
	
	public EventsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		eventList = new Vector<Event>();
	}
	
	private void update(List<Event> events) {
		eventList = events;
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(events);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(events);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(events);
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error message", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public int getRowCount() {
		return eventList.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= eventList.size()) return null;
		Event e = eventList.get(rowIndex);
		if(columnIndex == 0) return e.getTime();
		else if(columnIndex == 1) return e.toString();
		else return null;
	}
}
