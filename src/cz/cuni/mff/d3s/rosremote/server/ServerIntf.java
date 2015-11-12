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
	void sayHello() throws RemoteException;
	
	void generateConfigurationFiles(ConfigInf config) throws RemoteException;
	
	ConfigInf createConfig(String mapName) throws RemoteException, IOException;
	ConfigInf createConfig(String mapName, double resolution, long simIntervalMs) throws RemoteException, IOException;
}
