package cz.cuni.mff.d3s.rosremote.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerRunner {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		System.out.println("RMI server runner");

		// Ensure RMI registry exists
		try {
			LocateRegistry.createRegistry(1099);
			System.out.println("Java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("Java RMI registry already exists.");
		}

		Server server = new Server();
		Naming.rebind("//0.0.0.0/ROSRemote", server);
	}
}
