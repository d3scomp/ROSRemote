package cz.cuni.mff.d3s.rosremote.server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements ServerIntf {
	private static final long serialVersionUID = 0L;

	private int launchCounter = 0;
	private Map<Integer, Process> runningSimulations = new HashMap<>();

	protected Server() throws RemoteException {
		super();
	}

	@Override
	public Config createConfig(String mapName) throws IOException {
		return new Config(mapName);
	}

	@Override
	public Config createConfig(String mapName, double resolution, long simIntervalMs) throws IOException {
		return new Config(mapName, resolution, simIntervalMs);
	}

	/**
	 * Starts simulation according to configuration object
	 * 
	 * FIXME: What if multiple simulations are starting at the same time
	 */
	@Override
	public synchronized int startSimulation(ConfigIntf config) throws IOException, InterruptedException {
		generateConfigurationFiles(config);
		return runROSSimulation();
	}

	private void generateConfigurationFiles(ConfigIntf config) {
		System.out.println("Generating configs");

		try {
			config.writeLaunch();
			config.writeWorld();
		} catch (IOException e) {
			System.out.println("Config generation failed");
			e.printStackTrace();
		}
	}

	private int runROSSimulation() throws IOException, InterruptedException {
		final String simulationDir = System.getProperty("user.dir") + File.separator + Config.SIM_FILES_PREFIX;

		ProcessBuilder launchBuilder = new ProcessBuilder();
		launchBuilder.command("/opt/ros/indigo/bin/roslaunch", "simulation.launch");
		System.out.println("Simulation dir:" + simulationDir);
		launchBuilder.environment().put("ROS_HOME", simulationDir);
		launchBuilder.directory(new File(simulationDir));

		launchBuilder.inheritIO();

		System.out.println("Launching simulation");
		Process launch = launchBuilder.start();

		// Assign id to running process and remember it
		int processId = launchCounter++;
		runningSimulations.put(processId, launch);

		return processId;
	}

	@Override
	public void stopSimulaiton(int id) throws InterruptedException {
//		runningSimulations.get(id).destroyForcibly();
		runningSimulations.get(id).destroy();
		waitForExit(id);
	}

	@Override
	public void waitForExit(int id) throws InterruptedException {
		runningSimulations.get(id).waitFor();
	}

	@Override
	public boolean isRunning(int id) {
		return runningSimulations.get(id).isAlive();
	}
}
