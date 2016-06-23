package uk.ac.nott.cs.g53dia.demo;
import uk.ac.nott.cs.g53dia.library.*;

/**
 * An example of how to simulate execution of a tanker agent in the sample (task) environment.
 * <p>
 * Creates a default {@link Environment}, a {@link DemoTanker} and a GUI window 
 * (a {@link TankerViewer}) and executes the Tanker for DURATION days in the environment. 
 * 
 * @author Julian Zappala
 */

/*
 * Copyright (c) 2005 Neil Madden.
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk)
 * 
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
 
 //To remove visualisation of the test environment, comment out any reference to tv

public class DemoSimulator {

    /**
     * Time for which execution pauses so that GUI can update.
     * Reducing this value causes the simulation to run faster.
     */
	private static int DELAY = 50;
	
	/**
	 * Number of timesteps to execute
	 */
	private static int DURATION = 10 * 10000;
	
	public static void main(String[] args) {
        // Create an environment
        Environment env = new Environment((Tanker.MAX_FUEL/2)-5);
        // Create our tanker
        Tanker t = new DemoTanker();
        // Create a GUI window to show our tanker
        TankerViewer tv = new TankerViewer(t);
        tv.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        // Start executing the Tanker
        while (env.getTimestep()<(DURATION)) {
            // Advance the environment timestep
            env.tick();
            // Update the GUI
            tv.tick(env);
            // Get the current view of the tanker
            Cell[][] view = env.getView(t.getPosition(), Tanker.VIEW_RANGE);
            // Let the tanker choose an action
            Action a = t.senseAndAct(view, env.getTimestep());
            // Try to execute the action
            try {
                a.execute(env, t);
            } catch (OutOfFuelException dte) {
                System.out.println("Tanker out of fuel!");
                break;
            } catch (ActionFailedException afe) {
                System.err.println("Failed: " + afe.getMessage());
            }
            try { Thread.sleep(DELAY);} catch (Exception e) { }
        }
    }

	
}
