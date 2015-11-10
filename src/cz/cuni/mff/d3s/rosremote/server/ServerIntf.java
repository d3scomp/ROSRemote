package cz.cuni.mff.d3s.rosremote.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerIntf extends Remote {
	void sayHello() throws RemoteException; 
}
