package simulator.view.images;

import java.net.URL;
import javax.swing.ImageIcon;

public enum Images {
	
	OPEN {
		@Override
		public ImageIcon image() {
			ImageIcon imageIcon = null;
			URL url = Images.class.getResource("open.png");
			if(url != null)
				imageIcon = new ImageIcon(url);
			return imageIcon;
		}
	},
	CO2CLASS() {
		@Override
		public ImageIcon image() {
			ImageIcon imageIcon = null;
			URL url = Images.class.getResource("co2class.png");
			if(url != null)
				imageIcon = new ImageIcon(url);
			return imageIcon;
		}
	},
	WEATHER() {
		@Override
		public ImageIcon image() {
			ImageIcon imageIcon = null;
			URL url = Images.class.getResource("weather.png");
			if(url != null)
				imageIcon = new ImageIcon(url);
			return imageIcon;
		}
	},
	RUN {
		@Override
		public ImageIcon image() {
			ImageIcon imageIcon = null;
			URL url = Images.class.getResource("run.png");
			if(url != null)
				imageIcon = new ImageIcon(url);
			return imageIcon;
		}
	},
	STOP {
		@Override
		public ImageIcon image() {
			ImageIcon imageIcon = null;
			URL url = Images.class.getResource("stop.png");
			if(url != null)
				imageIcon = new ImageIcon(url);
			return imageIcon;
		}
	},
	EXIT {
		@Override
		public ImageIcon image() {
			ImageIcon imageIcon = null;
			URL url = Images.class.getResource("exit.png");
			if(url != null)
				imageIcon = new ImageIcon(url);
			return imageIcon;
		}
	};
	
	public abstract ImageIcon image();

}
