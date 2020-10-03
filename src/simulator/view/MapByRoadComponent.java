package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.view.images.Images;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{
	
	private static final long serialVersionUID = -6650748334497758422L;
	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;
	private Image _weather;
	private Image _co2;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		setPreferredSize(new Dimension (300, 200));
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getRoadList().size() == 0 ) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
	}

	private void drawRoads(Graphics g) {
		int i = 0;
		for (Road r : _map.getRoadList()) {

			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i + 1) * 50;

			int roadColorValue = 200 - (int) (200.0
					* Math.min(1.0, (double) r.getTotalContamination() / (1.0 + (double) r.getContLimit())));
			Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);

			g.setColor(Color.BLACK);
			g.drawString(r.getId(), x1 - 40, y + 4);
			drawLine(g, x1, y, x2, y, roadColor);
			
			drawSrcAndDestJunctionsOfRoad(g, x1, y, x2, y, r);
			drawVehiclesOfRoad(g, r, x1, x2, y);
			
			drawWeatherImage(g, x2 + 15, y - 20, r);
			drawCO2Image(g, x2 + 55, y - 20, r);
					
			++i;
		}

	}

	private void drawCO2Image(Graphics g, int x, int y, Road r) {
		int c = (int) Math.floor(Math.min((double) r.getTotalContamination()/(1.0 + (double) r.getContLimit()),1.0) / 0.19);
		_co2 = loadImage("cont_" + c + ".png");
		g.drawImage(_co2, x, y, 32, 32, this);
	}

	private void drawWeatherImage(Graphics g, int x, int y, Road r) {
		_weather = loadImage(r.getWeather().toString().toLowerCase() + ".png");
		g.drawImage(_weather, x, y, 32, 32, this);
	}

	private void drawVehiclesOfRoad(Graphics g, Road r, int x1, int x2, int y) {
		for (Vehicle v : r.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));
				
				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));

				// draw an image of a car (with circle as background) and it identifier
				g.drawImage(_car, x, y - 10, 16, 16, this);
				g.drawString(v.getId(), x, y - 10);
			}
		}
	}

	private void drawSrcAndDestJunctionsOfRoad(Graphics g, int x1, int y1, int x2, int y2, Road r) {

		// draw a circle with center at (x,y) with radius _JRADIUS
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y1 - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the srcJunction identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getSrcJunc().getId(), x1 - 5, y1 - 8);
		
		// Color of destJunction
		Color destJunctionColor = _RED_LIGHT_COLOR;
		int idx = r.getDestJunc().getGreenLightInd();
		if (idx != -1 && r.equals(r.getDestJunc().getEntryRoads().get(idx))) {
			destJunctionColor = _GREEN_LIGHT_COLOR;
		}
		
		// draw a circle with center at (x,y) with radius _JRADIUS
		g.setColor(destJunctionColor);
		g.fillOval(x2 - _JRADIUS / 2, y2 - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the destJunction identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getDestJunc().getId(), x2 - 5, y2 - 8);
	}

	// this method is used to update the preffered and actual size of the component,
	// so when we draw outside the visible area the scrollbars show up
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctionList()) {
			maxW = Math.max(maxW, j.getxCoor());
			maxH = Math.max(maxH, j.getyCoor());
		}
		maxW += 20;
		maxH += 20;
		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}

	private void drawLine(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			Color lineColor) {

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
	}

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(Images.class.getResource(img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
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
