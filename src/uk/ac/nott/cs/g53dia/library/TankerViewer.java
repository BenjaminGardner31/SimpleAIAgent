package uk.ac.nott.cs.g53dia.library;
import java.awt.*;
import javax.swing.*;
/**
 * A simple user interface for watching an individual Tanker.
 *
 * @author Neil Madden.
 */
/*
 * Copyright (c) 2003 Stuart Reeves
 * Copyright (c) 2003-2005 Neil Madden (nem@cs.nott.ac.uk).
 * Copyright (c) 2011 Julian Zappala (jxz@cs.nott.ac.uk).
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class TankerViewer extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2810783821678793885L;
	TankerViewerIconFactory iconfactory;
    JLabel[][] cells;
    JLabel tstep, fuel, pos, water,  completed, delivered, score;
    Tanker tank;
    final static int SIZE = Tanker.VIEW_RANGE * 2 + 1;

    public TankerViewer(Tanker Tanker) { this(Tanker, new DefaultTankerViewerIconFactory()); }
    public TankerViewer(Tanker Tanker, TankerViewerIconFactory fac) {
        this.tank = Tanker;
        this.iconfactory = fac;
        Container c     = getContentPane();
        c.setLayout(new BorderLayout());

        // Create the cell viewer
        cells           = new JLabel[SIZE][SIZE];
        JLayeredPane lp = new JLayeredPane();
        JPanel p        = new JPanel(new GridLayout(SIZE, SIZE));
        p.setBackground(Color.GREEN);

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                cells[x][y] = new JLabel();
                p.add(cells[x][y]);
            }
        }
        lp.add(p,new Integer(0));
        p.setBounds(0,0,600,600);
        // Create a Tanker label
        JLabel Tankerlabel = new JLabel(iconfactory.getIconForTanker(Tanker));
        lp.add(Tankerlabel,new Integer(1)); // Add above the background
        lp.setSize(new Dimension(600,600));
        Tankerlabel.setBounds(275,275,40,40);
        c.add(lp, BorderLayout.CENTER);

        // Create some labels to show info about the Tanker and environment
        JPanel infop    = new JPanel(new GridLayout(4,4));
        infop.add(new JLabel("Timestep:"));
        tstep = new JLabel("0");
        infop.add(tstep);
        infop.add(new JLabel("Fuel:"));
        fuel = new JLabel("200");
        infop.add(fuel);
        infop.add(new JLabel("Position:"));
        pos = new JLabel("(0,0)");
        infop.add(pos);
        infop.add(new JLabel("Water:"));
        water = new JLabel("0");
        infop.add(water);
        
        infop.add(new JLabel("Completed:"));
        completed = new JLabel("0");
        infop.add(completed);
        
        infop.add(new JLabel("Delivered:"));
        delivered = new JLabel("0");
        infop.add(delivered);
                
        infop.add(new JLabel("Score:"));
        score = new JLabel("0");
        infop.add(score);


        
        
        
        c.add(infop, BorderLayout.SOUTH);
        //infop.setPreferredSize(new Dimension(200,100));
        
        setSize(600,650);
        setTitle("Tanker Viewer");
        setVisible(true);
    }

    public void setTanker(Tanker t) {
        this.tank = t;
    }

    public void tick(Environment env) {
        Cell[][] view = env.getView(tank.getPosition(),Tanker.VIEW_RANGE);
        pos.setText(tank.getPosition().toString());
        tstep.setText(new String(""+env.getTimestep()));
        water.setText(new String(""+tank.waterLevel));
        fuel.setText(new String(""+tank.getFuelLevel()));
        completed.setText(new String("" + tank.getCompletedCount()));
        delivered.setText("" + tank.waterDelivered);
        score.setText("" + tank.getScore());
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Icon cur = iconfactory.getIconForCell(view[x][y]);
                cells[x][y].setIcon(cur);
            }
        }
    }
}

