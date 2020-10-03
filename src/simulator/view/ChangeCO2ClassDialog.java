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
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private JComboBox<Vehicle> vehicles;
	private JComboBox<Integer> co2ClassNumbers;
	private DefaultComboBoxModel<Vehicle> vehiclesModel;
	private Integer[] co2Values = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private int status;
	private JSpinner ticksSpinner;

	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {
		
		setTitle("Change CO2 Class");
		
		status = 0;
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		String welcome = "Schedule an event to change the CO2 class"
				+ " of a vehicle after a given number of simulation ticks from now.";
		JLabel welcomeLabel = new JLabel(welcome);
		JLabel vehicleLabel = new JLabel("Vehicle: ");
		JLabel co2ClassLabel = new JLabel("CO2 Class: ");
		JLabel ticksLabel = new JLabel("Ticks: ");
		ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("OK");
		
		co2ClassNumbers = new JComboBox<Integer>(co2Values);
		vehiclesModel = new DefaultComboBoxModel<Vehicle>();
		vehicles = new JComboBox<Vehicle>(vehiclesModel);
		
		
		JPanel welcomePanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		welcomePanel.add(welcomeLabel);
		centerPanel.add(vehicleLabel);
		centerPanel.add(vehicles);
		centerPanel.add(co2ClassLabel);
		centerPanel.add(co2ClassNumbers);
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
				if (vehiclesModel.getSelectedItem() != null) {
					status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		
	}
	
	public int open(List<Vehicle> vehicles) {
		vehiclesModel.removeAllElements();
		for (Vehicle v : vehicles)
			vehiclesModel.addElement(v);

		setVisible(true);
		return status;
	}
	
	Vehicle getVehicle() {
		return (Vehicle) vehiclesModel.getSelectedItem();
	}
	
	int getTicks() {
		return (int) ticksSpinner.getValue();
	}
	
	int getCo2() {
		return (int) co2ClassNumbers.getSelectedItem();
	}

}
