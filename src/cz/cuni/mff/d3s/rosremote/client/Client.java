package cz.cuni.mff.d3s.rosremote.client;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import cz.cuni.mff.d3s.rosremote.Config;
import cz.cuni.mff.d3s.rosremote.server.ServerIntf;

public class Client {
	public static void main(String[] args) throws NotBoundException, IOException {
		System.out.println("RMI hello wordl client");
		
		ServerIntf server = (ServerIntf)Naming.lookup("//192.168.56.101/ROSRemote");
        server.sayHello();
        
        
        
    	Config conf = new Config("corridor", 0.02, 100);
		conf.new Turtlebot(12, 12);
		conf.new Turtlebot(25, 12);
		conf.new StageWindow(640, 480, 15, 8, 0, 0, 15);
        
        server.generateConfigurationFiles(conf);
	}
}
