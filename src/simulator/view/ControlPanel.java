package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;
import simulator.view.images.Images;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	private RoadMap map;
	private int time;
	private boolean _stopped;
	private JToolBar toolBar;
	private JButton loadFilebutton;
	private JButton changeCo2Button;
	private JButton changeRoadWeatherButton;
	private JButton runButton;
	private JButton stopButton;
	private JButton exitButton;

	public ControlPanel(Controller controller) {
		controller.addObserver(this);
		this.controller = controller;
		_stopped = false;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		toolBar = new JToolBar();

		ImageIcon loadFileImage = Images.OPEN.image();
		loadFilebutton = new JButton(loadFileImage);
		loadFilebutton.setToolTipText("Load events file");

		ImageIcon changeCo2Image = Images.CO2CLASS.image();
		changeCo2Button = new JButton(changeCo2Image);
		changeCo2Button.setToolTipText("Change CO2 Class of a Vehicle");

		ImageIcon changeRoadWeatherImage = Images.WEATHER.image();
		changeRoadWeatherButton = new JButton(changeRoadWeatherImage);
		changeRoadWeatherButton.setToolTipText("Change Road Weather");

		ImageIcon runImage = Images.RUN.image();
		runButton = new JButton(runImage);
		runButton.setToolTipText("Run the simulator");

		ImageIcon stopImage = Images.STOP.image();
		stopButton = new JButton(stopImage);
		stopButton.setToolTipText("Stop the simulator");

		JLabel labelTicks = new JLabel("Ticks: ");
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		ticksSpinner.setMaximumSize(new Dimension(40, 40));
		ticksSpinner.setToolTipText("Simulation tick to run: 1-10000");

		ImageIcon exitImage = Images.EXIT.image();
		exitButton = new JButton(exitImage);
		exitButton.setToolTipText("Exit the simulator");

		toolBar.add(loadFilebutton);
		toolBar.add(Box.createRigidArea(new Dimension(10,  10)));
		toolBar.add(changeCo2Button);
		toolBar.add(changeRoadWeatherButton);
		toolBar.add(Box.createRigidArea(new Dimension(10, 10)));
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(Box.createRigidArea(new Dimension(10, 10)));
		toolBar.add(labelTicks);
		toolBar.add(ticksSpinner);
		toolBar.add(Box.createGlue());
		toolBar.add(exitButton);

		add(toolBar);

		loadFilebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileSelection();
			}
		});
		changeCo2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeCo2();
			}
		});
		changeRoadWeatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeRoadWeather();
			}
		});
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableToolBar(false);
				_stopped = false;
				run_sim((Integer) ticksSpinner.getValue());
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(null, "Do you want to exit the simulator?", "Exit",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == 0)
					System.exit(res);
			}
		});
	}
	
	protected void changeCo2() {
		ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog((Frame) SwingUtilities.getWindowAncestor(this));

		List<Vehicle> vehicles = map.getVehicleList();

		int status = dialog.open(vehicles);

		if (status == 1) {
			int timeUntilChangeCo2Class = time + (Integer) dialog.getTicks();
			Vehicle vehicle = dialog.getVehicle();
			Integer co2 = dialog.getCo2();
			if(vehicle == null || co2 == null)
				JOptionPane.showMessageDialog(null, "No information about the simulator yet.", "Error Message", JOptionPane.ERROR_MESSAGE);
			else {
				List<Pair<String, Integer>> cs = new ArrayList<Pair<String,Integer>>();
				cs.add(new Pair<String, Integer>(vehicle.getId(), co2));
				controller.addEvent(new SetContClassEvent(timeUntilChangeCo2Class, cs));
			}
		}
	}
	
	protected void changeRoadWeather() {
		ChangeWeatherDialog dialog = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));

		List<Road> roads = map.getRoadList();

		int status = dialog.open(roads);

		if (status == 1) {
			int timeUntilChangeWeather = time + dialog.getTicks();
			Road road = dialog.getRoad();
			Weather w = dialog.getWaether();
			if(road == null || w == null)
				JOptionPane.showMessageDialog(null, "The chosen data is incorrect.", "Error Message", JOptionPane.ERROR_MESSAGE);
			else {
				List<Pair<String, Weather>> ws = new ArrayList<Pair<String,Weather>>();
				ws.add(new Pair<String, Weather>(road.getId(), w));
				controller.addEvent(new SetWeatherEvent(timeUntilChangeWeather, ws));
			}
		}
	}

	private void fileSelection() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("resources/examples/"));
		int selection = fileChooser.showOpenDialog(this.getParent());
		if(selection == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedFile = fileChooser.getSelectedFile();
				InputStream in = new FileInputStream(selectedFile);
				controller.reset();
				controller.loadEvents(in);
			} catch (FileNotFoundException | JSONException ex) {
				JOptionPane.showMessageDialog(null, "Error loading the file", "Error message", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void update(RoadMap map, int time) {
		this.map = map;
		this.time = time;
		updateUI();
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				controller.run(1);
				Thread.sleep(100);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error running simulator", "Error message", JOptionPane.ERROR_MESSAGE);
				enableToolBar(true);
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void enableToolBar(boolean b) {
		loadFilebutton.setEnabled(b);
		changeCo2Button.setEnabled(b);
		changeRoadWeatherButton.setEnabled(b);
		runButton.setEnabled(b);
		exitButton.setEnabled(b);
		toolBar.updateUI();
	}

	private void stop() {
		_stopped = true;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map, time);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	@Override
	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "Error message", JOptionPane.ERROR_MESSAGE);
	}

}
