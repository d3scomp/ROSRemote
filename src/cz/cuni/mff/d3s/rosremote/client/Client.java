package cz.cuni.mff.d3s.rosremote.client;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import cz.cuni.mff.d3s.rosremote.server.ConfigInf;
import cz.cuni.mff.d3s.rosremote.server.ServerIntf;

public class Client {
	public static void main(String[] args) throws NotBoundException, IOException {
		System.out.println("RMI hello wordl client");

		ServerIntf server = (ServerIntf) Naming.lookup("//192.168.56.101/ROSRemote");
		server.sayHello();

		ConfigInf conf = server.createConfig("corridor", 0.02, 100);
		conf.addTurtlebot(12, 12);
		conf.addTurtlebot(25, 12);
		conf.setStageWindow(640, 480, 15, 8, 0, 0, 15);

		server.generateConfigurationFiles(conf);
	}
}
