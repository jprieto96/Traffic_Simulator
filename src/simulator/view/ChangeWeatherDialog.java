package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private JComboBox<Road> roadSelection;
	private JComboBox<Weather> weatherSelection;
	private DefaultComboBoxModel<Road> roadsModel;
	private Weather[] weatherValues = { Weather.SUNNY, Weather.CLOUDY, Weather.RAINY, Weather.STORM, Weather.WINDY };
	private int status;
	private JSpinner ticksSpinner;

	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {
		
		setTitle("Change Road Weather");
		
		status = 0;
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		String welcome = "Schedule an event to change the weather"
				+ " of a road after a given number of simulation ticks from now.";
		JLabel welcomeLabel = new JLabel(welcome);
		JLabel roadLabel = new JLabel("Road: ");
		JLabel weatherLabel = new JLabel("Weather: ");
		JLabel ticksLabel = new JLabel("Ticks: ");
		ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("OK");
		
		weatherSelection = new JComboBox<Weather>(weatherValues);
		roadsModel = new DefaultComboBoxModel<Road>();
		roadSelection = new JComboBox<Road>(roadsModel);
		
		JPanel welcomePanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		welcomePanel.add(welcomeLabel);
		centerPanel.add(roadLabel);
		centerPanel.add(roadSelection);
		centerPanel.add(weatherLabel);
		centerPanel.add(weatherSelection);
		centerPanel.add(ticksLabel);
		centerPanel.add(ticksSpinner);
		southPanel.add(cancelButton);
		southPanel.add(okButton);
		
		mainPanel.add(welcomePanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(false);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (roadsModel.getSelectedItem() != null) {
					status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		
	}
	
	public int open(List<Road> roads) {
		roadsModel.removeAllElements();
		for (Road r : roads)
			roadsModel.addElement(r);

		setVisible(true);
		return status;
	}
	
	Road getRoad() {
		return (Road) roadsModel.getSelectedItem();
	}
	
	int getTicks() {
		return (int) ticksSpinner.getValue();
	}
	
	Weather getWaether() {
		return (Weather) weatherSelection.getSelectedItem();
	}

}
