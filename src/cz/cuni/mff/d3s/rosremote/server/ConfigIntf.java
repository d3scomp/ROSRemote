package cz.cuni.mff.d3s.rosremote.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConfigIntf extends Remote {
	void addTurtlebot(double x, double y) throws RemoteException;

	void addTurtlebot(String color, double x, double y) throws RemoteException;

	void setStageWindow(double sizex, double sizey, double centerx, double centery, double rotatex, double rotatey,
			double scale) throws RemoteException;

	public void writeLaunch() throws IOException, RemoteException;

	public void writeWorld() throws IOException, RemoteException;
	
	public int getNumOfRobots() throws RemoteException;
	
	public String getMapName() throws RemoteException;
}
