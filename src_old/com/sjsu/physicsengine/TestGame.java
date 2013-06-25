package com.sjsu.physicsengine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 *  simple game engine to test SimplePhys
 *  Adds a bunch of bodies with random velocity
 *  and takes physics steps and updates the graphics to 
 *  the new locations of the bodies
 */
public class TestGame
{
	private JFrame mainFrame;
	private static JPanel drawingPanel;
	
	public static final int MAX_LOC = 1000;
	public static final int CIRCLE_RADII = 4;
	
	private static final int BOX_LENGTH = 20;
	private static final int NUM_BODIES = 1000;
	private static final int MAX_VELOCITY = 15;
	private static final int MAX_MASS = 100;
	private Random generator;
	
	private static World world;

	public TestGame()
	{
		mainFrame = new JFrame("Simple Phys");
		drawingPanel = new DrawingPanel();
		drawingPanel.setPreferredSize(new Dimension(MAX_LOC, MAX_LOC));
		mainFrame.setContentPane(drawingPanel);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
		generator = new Random(System.currentTimeMillis());
		world = new World();
		
		//makeWalls();
		makeBox();
		makeFixedBox();
		makeRandomBodies();
		//makeHeadOn();
		//makeOffsetHeadOn();
		//makeNewtonCradle();
	}


	public static void main(String arg[])
	{	
		// create the window
		TestGame game = new TestGame();

		world.startThreads();

		// set timer to create constant callback
		Timer timer = new Timer("constantStep");
		PhysicsStep step = new PhysicsStep(world, drawingPanel);
		timer.schedule(step, 0, 1);
	}


	/* Make walls to constrain everything inside the box */
	public void makeWalls()
	{
		Wall leftWall = new Wall();
		Wall topWall = new Wall();
		Wall rightWall = new Wall();
		Wall bottomWall = new Wall();

		leftWall.setGeometry(new Rectangle2D.Float(2, 2, 1, MAX_LOC - 3));
		rightWall.setGeometry(new Rectangle2D.Float(1, MAX_LOC-3, 1, MAX_LOC - 3));
		topWall.setGeometry(new Rectangle2D.Float(2, 2, MAX_LOC - 3, 1));
		bottomWall.setGeometry(new Rectangle2D.Float(MAX_LOC - 3, 2, MAX_LOC - 4, 1));


		world.addObjectToWorld(leftWall);
		world.addObjectToWorld(rightWall);
		world.addObjectToWorld(topWall);
		world.addObjectToWorld(bottomWall);
	}
	
	/* create a box of bodies to act as a wall-bounding box fixed */
	public void makeFixedBox()
	{
		int centerX = MAX_LOC / 2;
		int centerY = MAX_LOC / 2;
		Vector2D zero = new Vector2D(0, 0);

		// horizontal walls
		for (int i = 0; i < BOX_LENGTH * 8; i++)
		{
			Body b1 = new Body();
			Body b2 = new Body();
			b1.setMass(Constants.INFINITY);
			b2.setMass(Constants.INFINITY);
	
			int xLoc = (centerX - BOX_LENGTH * CIRCLE_RADII * 4) + (i * CIRCLE_RADII); 
			int yLoc1 = (centerY - BOX_LENGTH * CIRCLE_RADII * 4);
			int yLoc2 = (centerY + BOX_LENGTH * CIRCLE_RADII * 4);
			Vector2D loc1 = new Vector2D(xLoc, yLoc1);
			Vector2D loc2 = new Vector2D(xLoc, yLoc2);
			b1.setLocation(loc1);
			b2.setLocation(loc2);

			world.addObjectToWorld(b1);
			world.addObjectToWorld(b2);
		}

		// vertical walls
		for (int i = 0; i < BOX_LENGTH * 8; i++)
		{
			Body b1 = new Body();
			Body b2 = new Body();
			b1.setMass(Constants.INFINITY);
			b2.setMass(Constants.INFINITY);

			int yLoc = (centerY - BOX_LENGTH * CIRCLE_RADII * 4) + (i * CIRCLE_RADII); 
			int xLoc1 = (centerY - BOX_LENGTH * CIRCLE_RADII * 4);
			int xLoc2 = (centerY + BOX_LENGTH * CIRCLE_RADII * 4);
			Vector2D loc1 = new Vector2D(xLoc1, yLoc);
			Vector2D loc2 = new Vector2D(xLoc2, yLoc);
			b1.setLocation(loc1);
			b2.setLocation(loc2);

			world.addObjectToWorld(b1);
			world.addObjectToWorld(b2);
		}
	}

	/* create a box of bodies to act as a wall-bounding box */
	public void makeBox()
	{
		int centerX = MAX_LOC / 2;
		int centerY = MAX_LOC / 2;
		Vector2D zero = new Vector2D(0, 0);

		// horizontal walls
		for (int i = 0; i < BOX_LENGTH * 2; i++)
		{
			Body b1 = new Body();
			Body b2 = new Body();
			//b1.setMass(Constants.INFINITY);
			//b2.setMass(Constants.INFINITY);
			b1.setMass(MAX_MASS);
			b2.setMass(MAX_MASS);
			
			int xLoc = (centerX - BOX_LENGTH * CIRCLE_RADII) + (i * CIRCLE_RADII); 
			int yLoc1 = (centerY - BOX_LENGTH * CIRCLE_RADII);
			int yLoc2 = (centerY + BOX_LENGTH * CIRCLE_RADII);
			Vector2D loc1 = new Vector2D(xLoc, yLoc1);
			Vector2D loc2 = new Vector2D(xLoc, yLoc2);
			b1.setLocation(loc1);
			b2.setLocation(loc2);

			world.addObjectToWorld(b1);
			world.addObjectToWorld(b2);
		}

		// vertical walls
		for (int i = 0; i < BOX_LENGTH * 2; i++)
		{
			Body b1 = new Body();
			Body b2 = new Body();
			//b1.setMass(Constants.INFINITY);
			//b2.setMass(Constants.INFINITY);
			b1.setMass(20);
			b2.setMass(20);

			int yLoc = (centerY - BOX_LENGTH * CIRCLE_RADII) + (i * CIRCLE_RADII); 
			int xLoc1 = (centerY - BOX_LENGTH * CIRCLE_RADII);
			int xLoc2 = (centerY + BOX_LENGTH * CIRCLE_RADII);
			Vector2D loc1 = new Vector2D(xLoc1, yLoc);
			Vector2D loc2 = new Vector2D(xLoc2, yLoc);
			b1.setLocation(loc1);
			b2.setLocation(loc2);

			world.addObjectToWorld(b1);
			world.addObjectToWorld(b2);
		}
	}
	
	/* 2D headon collision */
	public void makeOffsetHeadOn()
	{
		// // Head on collision
		Body b1 = new Body();
		b1.setLocation(new Vector2D(15, MAX_LOC / 2 + 32));
		b1.setVelocity(new Vector2D(MAX_VELOCITY, 0));
		b1.setMass(5);

		Body b2 = new Body();
		b2.setLocation(new Vector2D(MAX_LOC - 10, MAX_LOC / 2 + 22));
		b2.setVelocity(new Vector2D(-MAX_VELOCITY, 0));
		b2.setMass(10);
		
		Body b3 = new Body();
		b3.setLocation(new Vector2D(MAX_LOC / 2 - 40, MAX_LOC / 2 + 30));
		b3.setVelocity(new Vector2D(0, 0));
		b3.setMass(50);

		world.addObjectToWorld(b1);
		world.addObjectToWorld(b2);
		world.addObjectToWorld(b3);
	}

	/* A headon collision (1d) */
	public void makeHeadOn()
	{
		// // Head on collision
		Body b1 = new Body();
		b1.setLocation(new Vector2D(10, MAX_LOC / 2 + 10));
		b1.setVelocity(new Vector2D(MAX_VELOCITY, 0));
		b1.setMass(5);

		Body b2 = new Body();
		b2.setLocation(new Vector2D(MAX_LOC - 10, MAX_LOC / 2 + 10));
		b2.setVelocity(new Vector2D(-MAX_VELOCITY, 0));
		b2.setMass(10);
		
		Body b3 = new Body();
		b3.setLocation(new Vector2D(MAX_LOC / 2 - 40, MAX_LOC / 2 + 10));
		b3.setVelocity(new Vector2D(0, 0));
		b3.setMass(50);

		world.addObjectToWorld(b1);
		world.addObjectToWorld(b2);
		world.addObjectToWorld(b3);

	}

	public void makeNewtonCradle()
	{
		int centerX = MAX_LOC / 2;
		int centerY = MAX_LOC / 2;
		Vector2D zero = new Vector2D(0, 0);
		
		Body b1 = new Body();
		b1.setMass(MAX_MASS);

		int xLoc = (centerX - BOX_LENGTH * CIRCLE_RADII) + (CIRCLE_RADII);
		int yLoc1 = MAX_LOC / 2;
		Vector2D loc1 = new Vector2D(xLoc, yLoc1);
		b1.setLocation(loc1);

		world.addObjectToWorld(b1);
		
		Body b2 = new Body();
		Body b3 = new Body();
		Body b4 = new Body();
		b2.setMass(MAX_MASS);
		b3.setMass(Constants.INFINITY);
		b4.setMass(Constants.INFINITY);
		
		Vector2D loc2 = new Vector2D (50, MAX_LOC/2);
		b2.setLocation(loc2);
		b2.setVelocity(new Vector2D(10, 0));
		
		b3.setLocation(new Vector2D(40, MAX_LOC/2));
		b4.setLocation(new Vector2D(MAX_LOC - 40, MAX_LOC/2));
		
		world.addObjectToWorld(b2);
		world.addObjectToWorld(b3);
		world.addObjectToWorld(b4);
	}
	
	/* Make some random circles to bounce around */
	public void makeRandomBodies()
	{	
		//RANDOM BODIES
		for (int i = 0; i < NUM_BODIES; i++)
		{
			Vector2D location = new Vector2D(generator.nextInt(MAX_LOC), generator.nextInt(MAX_LOC));
			Vector2D velocity = new Vector2D(generator.nextInt(MAX_VELOCITY), generator.nextInt(MAX_VELOCITY));
			int mass = generator.nextInt(MAX_MASS);
			mass = 10;

			Body b = new Body();

//			System.out.println("Velcity:" + velocity.getMagnitude() + "  X:" + location.x + " Y:" + location.y);
			b.setLocation(location);
			b.setVelocity(velocity);
			b.setMass(mass);

			// add the body to physics engine
			world.addObjectToWorld(b);
		} 

		// A single body w/ random velocity in middle
		Vector2D location = new Vector2D(MAX_LOC / 2, MAX_LOC / 2);
		Vector2D velocity = new Vector2D(generator.nextInt(MAX_VELOCITY), generator.nextInt(MAX_VELOCITY));
		int mass = 10;

		Body b = new Body();

		b.setLocation(location);
		b.setVelocity(velocity);
		b.setMass(mass);

		// add the body to physics engine
		world.addObjectToWorld(b);
	}

}



/* JPanel that does all the drawing to the mainFrame */
class DrawingPanel extends JPanel 
{
	private long nextSecond = System.currentTimeMillis() + 1000;
	private int framesInLastSecond = 0;
	private int framesInCurrentSecond = 0;
	
	public void paintComponent(Graphics g) 
	{

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// for every processor, paint all bodies associated with it
		for (int i = 0; i < World.NUM_PROCESSORS; i++) 
		{
			ArrayList<Object> objects = World.getThreadObjects(i);
			for (int j = 0; j < objects.size(); j++) 
			{
				if (objects.get(j).getType() == Object.ObjectType.Body)
				{
					Object b = objects.get(j);
					int quadId = objects.get(j).getQuad();

					if (quadId == 1)
						g2.drawOval((int) b.getLocation().x,
								(int) b.getLocation().y, TestGame.CIRCLE_RADII,
								TestGame.CIRCLE_RADII);
					else if (quadId == 0) 
					{
						g2.drawOval((int) b.getLocation().x,
								(int) b.getLocation().y, TestGame.CIRCLE_RADII,
								TestGame.CIRCLE_RADII);
						g2.fillOval((int) b.getLocation().x,
								(int) b.getLocation().y, TestGame.CIRCLE_RADII,
								TestGame.CIRCLE_RADII);
					}
					else if (quadId == 2)
					{
						g2.drawRect((int) b.getLocation().x,
								(int) b.getLocation().y, TestGame.CIRCLE_RADII,
								TestGame.CIRCLE_RADII);
					} 
					else
					{
						g2.drawRect((int) b.getLocation().x,
								(int) b.getLocation().y, TestGame.CIRCLE_RADII,
								TestGame.CIRCLE_RADII);
						g2.fillRect((int) b.getLocation().x,
								(int) b.getLocation().y, TestGame.CIRCLE_RADII,
								TestGame.CIRCLE_RADII);
					}
				}
			}
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime > nextSecond) 
		{
			nextSecond += 1000;
			framesInLastSecond = framesInCurrentSecond;
			framesInCurrentSecond = 0;
		}
		framesInCurrentSecond++;

		g2.drawString(framesInLastSecond + " FPS: ", 75, 75);
	}

}


/* Timer class to take one step every x seconds */
class PhysicsStep extends TimerTask 
{
	private World world;
	private DrawingPanel drawingPanel;

	public PhysicsStep(World w, JPanel dp)
	{
		drawingPanel = (DrawingPanel)dp;
		world = w;
	}

	@Override
	public void run() 
	{
		//System.out.println("Step");
		world.physicsStep();
		drawingPanel.repaint();

	}

}
