package cz.cuni.mff.d3s.rosremote.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import cz.cuni.mff.d3s.rosremote.server.ServerIntf;

public class Client {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		System.out.println("RMI hello wordl client");
		
		ServerIntf server = (ServerIntf)Naming.lookup("//localhost/ROSRemote");
        server.sayHello();
	}
}
