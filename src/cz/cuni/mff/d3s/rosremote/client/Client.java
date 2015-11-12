package cz.cuni.mff.d3s.rosremote.client;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import cz.cuni.mff.d3s.rosremote.server.ConfigIntf;
import cz.cuni.mff.d3s.rosremote.server.ServerIntf;

public class Client {
	public static void main(String[] args) throws NotBoundException, IOException, InterruptedException {
		System.out.println("RMI hello wordl client");

		ServerIntf server = (ServerIntf) Naming.lookup("//192.168.56.101/ROSRemote");
	
		ConfigIntf conf = server.createConfig("corridor", 0.02, 100);
		conf.addTurtlebot(12, 12);
		conf.addTurtlebot(25, 12);
		conf.setStageWindow(640, 480, 15, 8, 0, 0, 15);

		System.out.println("Running simulation");
		int simId = server.startSimulation(conf);
		
		System.out.println("Waiting 10s");
		Thread.sleep(10000);
		
		System.out.println("Killing simulaiton");
		server.stopSimulaiton(simId);
		
		System.out.println("Waiting for simulation end");
		server.waitForExit(simId);
		
		System.out.println("Done");
	}
}
