package cz.cuni.mff.d3s.rosremote.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to ROSRemote server
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface ServerIntf extends Remote {
	ConfigIntf createConfig(String mapName) throws RemoteException, IOException;

	ConfigIntf createConfig(String mapName, double resolution, long simIntervalMs) throws RemoteException, IOException;

	/**
	 * Starts simulation
	 * 
	 * Starts simulation according to provided configuration
	 * 
	 * @param config
	 *            Simulation configuration object
	 * @return Simulation identification
	 * @throws RemoteException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	int startSimulation(ConfigIntf config) throws RemoteException, IOException, InterruptedException;

	void stopSimulaiton(int id) throws RemoteException, InterruptedException;

	void waitForExit(int id) throws RemoteException, InterruptedException;

	boolean isRunning(int id) throws RemoteException;
}
