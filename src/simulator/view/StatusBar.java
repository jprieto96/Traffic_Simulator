package simulator.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = -7231663420645969226L;
	
	private JLabel timeLabel, eventLabel;
	
	public StatusBar(Controller ctrl) {
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		timeLabel = new JLabel("Time: " + 0);
		eventLabel = new JLabel("Welcome!");
		add(timeLabel);
		add(Box.createRigidArea(new Dimension(15, 15)));
		add(eventLabel);
	}
	
	private void update(int time, Event e) {
		timeLabel.setText("Time: " + time);
		if(e != null)
			eventLabel.setText("Event added (" + e.toString() + ")");
		else 
			eventLabel.setText("");
		updateUI();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(time, null);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(time, null);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(time, e);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(time, null);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error message", JOptionPane.ERROR_MESSAGE);
	}

}
