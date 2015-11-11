package cz.cuni.mff.d3s.rosremote.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import cz.cuni.mff.d3s.rosremote.Config;

public class Server extends UnicastRemoteObject implements ServerIntf {
	private static final long serialVersionUID = 0L;

	protected Server() throws RemoteException {
		super();
	}

	@Override
	public void sayHello() {
		System.out.println("Hello");
	}

	@Override
	public void generateConfigurationFiles(Config config) {
		System.out.println("Generating configs");
		
		try {
			config.writeLaunch(System.out);
			config.writeWorld(System.out);
		} catch (IOException e) {
			System.out.println("Config generation failed");
			e.printStackTrace();
		}
	}
}
