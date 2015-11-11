package cz.cuni.mff.d3s.rosremote.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import cz.cuni.mff.d3s.rosremote.Config;

public interface ServerIntf extends Remote {
	void sayHello() throws RemoteException;
	
	void generateConfigurationFiles(Config config) throws RemoteException;
}
