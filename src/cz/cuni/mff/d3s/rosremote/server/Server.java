package cz.cuni.mff.d3s.rosremote.server;

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
}
