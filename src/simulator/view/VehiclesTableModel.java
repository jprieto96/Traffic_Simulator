package simulator.view;

import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 4466291948729394083L;
	
	private List<Vehicle> vehiclesList;
	private String[] columnsName = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance" };
	
	public VehiclesTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		vehiclesList = new Vector<Vehicle>();
	}

	@Override
	public int getRowCount() {
		return vehiclesList.size();
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
		if(rowIndex >= vehiclesList.size()) return null;
		Vehicle v = vehiclesList.get(rowIndex);
		
		if(columnIndex == 0) return v.getId();
		else if(columnIndex == 1) return getVehicleStatus(v);
		else if(columnIndex == 2) return getVehicleItinerary(v);
		else if(columnIndex == 3) return v.getContClass();
		else if(columnIndex == 4) return v.getMaxSpeed();
		else if(columnIndex == 5) return v.getActualSpeed();
		else if(columnIndex == 6) return v.getTotalContamination();
		else if(columnIndex == 7) return v.getTotalDistanceTraveled();
		else return null;
	}
	
	private Object getVehicleItinerary(Vehicle v) {
		String itinerary = "[";
		for(int i = 0; i < v.getItinerary().size(); ++i) {
			if(i == v.getItinerary().size() - 1)
				itinerary += v.getItinerary().get(i).getId() + "]";
			else
				itinerary += v.getItinerary().get(i).getId() + ", ";
		}
		return itinerary;
	}

	private String getVehicleStatus(Vehicle v) {
		if(v.getStatus() == VehicleStatus.PENDING)
			return "Pending";
		else if(v.getStatus() == VehicleStatus.TRAVELING)
			return v.getRoad() + ":" + v.getLocation();
		else if(v.getStatus() == VehicleStatus.WAITING)
			return "Waiting:" + v.getItinerary().get(v.getCurrentJunction()).getId();
		else 
			return "Arrived";
	}
	
	private void update(RoadMap map) {
		vehiclesList = map.getVehicleList();
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
