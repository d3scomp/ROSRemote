package cz.cuni.mff.d3s.rosremote.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.yaml.snakeyaml.Yaml;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Holds ROS simulation configuration
 * 
 * This class also serves as source for mustache template engine, thus all important fields must be public.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Config extends UnicastRemoteObject implements ConfigIntf {
	private static final long serialVersionUID = 0L;

	public static final double DEFAULT_RESOLUTION = 0.02;
	public static final long DEFAULT_SIM_INTERVAL_MS = 100;
	public static final String SIM_FILES_PREFIX = "simulation/";

	public static final String LAUNCH_FILE_TEMPLATE = SIM_FILES_PREFIX + "simulation.launch.mustache";
	public static final String LAUNCH_FILE = SIM_FILES_PREFIX + "simulation.launch";
	public static final String WORLD_FILE_TEMPLATE = SIM_FILES_PREFIX + "simulation.world.mustache";
	public static final String WORLD_FILE = SIM_FILES_PREFIX + "simulation.world";

	/**
	 * Holds information about single Turtlebot in configuration
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	class Turtlebot extends UnicastRemoteObject {
		private static final long serialVersionUID = 0L;

		public final String name;
		public final String color;
		public final double x;
		public final double y;

		/**
		 * Creates Turtlebot configuration element
		 * 
		 * @param x
		 *            Initial position coordinate X
		 * @param y
		 *            Initial position coordinate Y
		 * @throws RemoteException
		 */
		public Turtlebot(double x, double y) throws RemoteException {
			this("black", x, y);
		}

		/**
		 * Creates Turtlebot configuration element
		 * 
		 * @param name
		 *            Robot name (internal to simulation)
		 * @param color
		 *            (internal to simulation)
		 * @param x
		 *            Initial position coordinate X
		 * @param y
		 *            Initial position coordinate Y
		 */
		public Turtlebot(String color, double x, double y) throws RemoteException {
			this.name = "robot_" + String.valueOf(turtleBotCounter++);
			this.color = color;
			this.x = x;
			this.y = y;

			turtlebots.add(this);
		}
	}

	/**
	 * Floor plan configuration
	 * 
	 * Floor plan can either be described by its parameters or parsed from ROS yaml map file
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	class FloorPlan extends UnicastRemoteObject {
		private static final long serialVersionUID = 0L;

		public String name;
		public String bitmap;
		public double sizex;
		public double sizey;
		public double posex;
		public double posey;

		/**
		 * Creates floor plan from parameters
		 * 
		 * @param name
		 *            Floor plan name
		 * @param bitmap
		 *            Floor bitmap
		 * @param sizex
		 *            Bitmap size in meters - width
		 * @param sizey
		 *            Bitmap size in meters - height
		 * @param posex
		 *            Bitmap offset in meters - x coordinate
		 * @param posey
		 *            Bitmap offset in meters - y coordinate
		 */
		public FloorPlan(String bitmap, double sizex, double sizey, double posex, double posey) throws RemoteException {
			this.bitmap = bitmap;
			this.sizex = sizex;
			this.sizey = sizey;
			this.posex = posex;
			this.posey = posey;
		}

		@SuppressWarnings("unchecked")
		public FloorPlan(String yamlMapFile) throws IOException {
			// Parse YAML map configuration file
			FileReader reader = new FileReader(SIM_FILES_PREFIX + yamlMapFile);
			Yaml map = new Yaml();
			Map<String, Object> mapValues = (Map<String, Object>) map.load(reader);

			// Retrieve data from parsed YAML
			List<Double> origin = (List<Double>) mapValues.get("origin");
			String bitmapFile = (String) mapValues.get("image");
			double resolution = (Double) mapValues.get("resolution");

			// We only support zero origin YAML files
			if (origin.size() != 3 || origin.get(0) != 0 || origin.get(1) != 0 || origin.get(2) != 0) {
				throw new UnsupportedOperationException(
						"YAML map file has nonzero origin (or origin.size != 3), this is unfortunately not suported");
			}

			// Open bitmap image in order to obtain dimensions
			BufferedImage bitmap = ImageIO.read(new File(SIM_FILES_PREFIX + bitmapFile));

			// Fill in FloorPlan data
			this.bitmap = bitmapFile;
			this.sizex = resolution * bitmap.getWidth();
			this.sizey = resolution * bitmap.getHeight();
			this.posex = resolution * bitmap.getWidth() / 2;
			this.posey = resolution * bitmap.getHeight() / 2;
		}
	}

	/**
	 * Holds information about Stage window
	 * 
	 * Used to configure stage to display particular part of the simulated world.
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 *
	 */
	class StageWindow extends UnicastRemoteObject {
		private static final long serialVersionUID = 0L;

		public final double sizex, sizey;
		public final double centerx, centery;
		public final double rotatex, rotatey;
		public final double scale;

		public StageWindow(double sizex, double sizey, double centerx, double centery, double rotatex, double rotatey,
				double scale) throws RemoteException {
			if (!Config.this.stageWindow.isEmpty()) {
				throw new UnsupportedOperationException("Stage window already configured");
			}

			this.sizex = sizex;
			this.sizey = sizey;
			this.centerx = centerx;
			this.centery = centery;
			this.rotatex = rotatex;
			this.rotatey = rotatey;
			this.scale = scale;

			// Register this stage window with top level configuration
			Config.this.stageWindow.add(this);
		}
	}

	/**
	 * Generator name
	 */
	public final String generatorName = getClass().getName();

	/**
	 * Simulation map name prefix
	 */
	public final String mapName;
	/**
	 * Simulation precision
	 */
	public final double resolution;
	/**
	 * Simulation time interval in milliseconds
	 */
	public final long simIntervalMs;

	/**
	 * Counts Turtlebots
	 * 
	 * This is needed in order to bind Stage robot instances to Turtlebot ROS runtime instances
	 */
	private int turtleBotCounter = 0;

	/**
	 * Turtlebots already added to configuration
	 */
	public Collection<Turtlebot> turtlebots = new LinkedList<>();

	/**
	 * World floor plan
	 */
	public final FloorPlan floorPlan;

	/**
	 * Stage window configuration
	 */
	public Collection<StageWindow> stageWindow = new LinkedList<>();

	/**
	 * Mustache factory for processing templates
	 */
	private static final MustacheFactory mf = new DefaultMustacheFactory();

	/**
	 * Simple configuration
	 * 
	 * @param mapName
	 *            Map name, used as prefix for map files
	 * @throws IOException
	 */
	Config(String mapName) throws IOException {
		this(mapName, DEFAULT_RESOLUTION, DEFAULT_SIM_INTERVAL_MS);
	}

	/**
	 * Advanced configuration
	 * 
	 * @param mapName
	 *            Map name, used as prefix for map files
	 * @throws IOException
	 */
	Config(String mapName, double resolution, long simIntervalMs) throws IOException {
		this.mapName = mapName;
		this.resolution = resolution;
		this.simIntervalMs = simIntervalMs;
		this.floorPlan = this.new FloorPlan(mapName + ".yaml");
	}

	public void writeLaunch() throws IOException {
		FileWriter launchWriter = new FileWriter(LAUNCH_FILE);

		// Generate launch file using template
		Mustache mustache = mf.compile(LAUNCH_FILE_TEMPLATE);
		mustache.execute(launchWriter, this);

		launchWriter.close();
	}

	public void writeWorld() throws IOException {
		FileWriter worldWriter = new FileWriter(WORLD_FILE);

		// Generate world file using template
		Mustache mustache = mf.compile(WORLD_FILE_TEMPLATE);
		mustache.execute(worldWriter, this);

		worldWriter.close();
	}

	@Override
	public void addTurtlebot(double x, double y) throws RemoteException {
		this.new Turtlebot(x, y);
	}

	@Override
	public void addTurtlebot(String color, double x, double y) throws RemoteException {
		this.new Turtlebot(color, x, y);
	}

	@Override
	public void setStageWindow(double sizex, double sizey, double centerx, double centery, double rotatex,
			double rotatey, double scale) throws RemoteException {
		this.new StageWindow(sizex, sizey, centerx, centery, rotatex, rotatey, scale);
	}
}
