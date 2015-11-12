package cz.cuni.mff.d3s.rosremote.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
	public void generateConfigurationFiles(ConfigInf config) {
		System.out.println("Generating configs");
		
		try {
			config.writeLaunch();
			config.writeWorld();
		} catch (IOException e) {
			System.out.println("Config generation failed");
			e.printStackTrace();
		}
	}

	@Override
	public Config createConfig(String mapName) throws IOException {
		return new Config(mapName); 
	}

	@Override
	public Config createConfig(String mapName, double resolution, long simIntervalMs) throws IOException {
		return new Config(mapName, resolution, simIntervalMs);
	}
}
