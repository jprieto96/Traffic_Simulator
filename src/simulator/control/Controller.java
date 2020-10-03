package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.exceptions.AdvanceTrafficSimulatorException;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator trafficSimulator;
	private Factory<Event> eventsFactory;

	// TODO terminar
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if (sim == null)
			throw new IllegalArgumentException("Argument exception (Traffic simulator)");
		else
			trafficSimulator = sim;
		if (eventsFactory == null)
			throw new IllegalArgumentException("Argument exception (Events Factory)");
		else
			this.eventsFactory = eventsFactory;
	}
	
	public void addObserver(TrafficSimObserver o) {
		trafficSimulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		trafficSimulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		trafficSimulator.addEvent(e);
	}

	public void loadEvents(InputStream in) {
		JSONObject jo = null;
		JSONArray events = null;
		if(in != null) {
			jo = new JSONObject(new JSONTokener(in));
			events= jo.getJSONArray("events");
			if (events == null)
				throw new JSONException("JSON format error");
			else {
				for (int i = 0; i < events.length(); i++) {
					JSONObject j = events.getJSONObject(i);
					if (j != null) {
						Event event = eventsFactory.createInstance(j);
						if (event != null)
							trafficSimulator.addEvent(event);
						else
							throw new JSONException("JSON format error");
					} 
					else throw new JSONException("JSON format error");
				}
			}
		}
		else throw new JSONException("JSON format error");
	}

	public void run(int n, OutputStream out) {
		try {
			if(out == null) {
				new OutputStream() {
					@Override
					public void write(int b) throws IOException {}
				};
			}
			PrintStream p = new PrintStream(out);
			p.println("{");
			p.println(" \"states\": [");
			// n - 1 pasos
			for (int i = 0; i < n - 1; i++) {
				trafficSimulator.advance();
				p.print(trafficSimulator.report()); p.println(",");
			}
			// Ultimo paso fuera del for para nop poner la coma
			trafficSimulator.advance();
			p.println(trafficSimulator.report());
			p.println("]");
			p.println("}");
		}
		catch (AdvanceTrafficSimulatorException ex) {
			System.err.format(ex.getMessage() + "\nCause of Exception:\n  " + ex.getCause() + "\n");
		}
	}

	public void reset() {
		trafficSimulator.reset();
	}

	public void run(int n) {
		try {
			for (int i = 0; i < n; i++)
				trafficSimulator.advance();
		}
		catch (AdvanceTrafficSimulatorException ex) {
			System.err.format(ex.getMessage() + "\nCause of Exception:\n  " + ex.getCause() + "\n");
		}
	}

}
