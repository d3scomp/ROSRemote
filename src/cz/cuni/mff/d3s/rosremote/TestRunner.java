package cz.cuni.mff.d3s.rosremote;

import java.io.IOException;

import cz.cuni.mff.d3s.rosremote.server.Config;

public class TestRunner {

	public static void main(String[] args) throws IOException {
		Config conf = new Config("corridor", 0.02, 100);
		conf.new Turtlebot(12, 12);
		conf.new Turtlebot(25, 12);
		conf.new StageWindow(640, 480, 15, 8, 0, 0, 15);
				
		conf.writeLaunch();
		conf.writeWorld();
	}

}
