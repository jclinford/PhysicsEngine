package com.sjsu.physicsengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sjsu.physicsengine.RigidBody.BodyType;
import com.sjsu.physicsengine.structures.Node;

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
	
	private static final int BOX_LENGTH = 20;
	private static final int NUM_BODIES = 1000;
	private static final int MAX_VELOCITY = 35;
	private static final int MAX_MASS = 100;
	private Random generator;
	
	public static World world;

	public TestGame()
	{
		mainFrame = new JFrame("Simple Phys");
		drawingPanel = new DrawingPanel();
		drawingPanel.setPreferredSize(new Dimension(MAX_LOC, MAX_LOC));
		drawingPanel.addMouseListener(new ClickListener());
		mainFrame.setContentPane(drawingPanel);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		
		generator = new Random(System.currentTimeMillis());
		world = new World();
		
		//makeWalls();
		//makeBox();
		//makeFixedBox();
		//makeRandomBodies();
		//makeHeadOn();
		//makeOffsetHeadOn();
		makeNewtonCradle();
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


		world.addBodyToWorld(leftWall);
		world.addBodyToWorld(rightWall);
		world.addBodyToWorld(topWall);
		world.addBodyToWorld(bottomWall);
	}
	
	/* create a box of bodies to act as a wall-bounding box fixed */
	public void makeFixedBox()
	{
		int centerX = MAX_LOC / 2;
		int centerY = MAX_LOC / 2;
		Vector2D zero = new Vector2D(0, 0);

		// horizontal walls
		for (int i = 0; i < BOX_LENGTH * 4 + 1; i++)
		{
			Circle b1 = new Circle();
			Circle b2 = new Circle();
			b1.setMass(Constants.INFINITY);
			b2.setMass(Constants.INFINITY);
	
			int xLoc = (int) ((centerX - BOX_LENGTH * b1.getRadius() * 4) + (i * b1.getRadius() * 2)); 
			int yLoc1 = (int) (centerY - BOX_LENGTH * b1.getRadius() * 4);
			int yLoc2 = (int) (centerY + BOX_LENGTH * b1.getRadius() * 4);
			Vector2D loc1 = new Vector2D(xLoc, yLoc1);
			Vector2D loc2 = new Vector2D(xLoc, yLoc2);
			b1.setCenter(loc1);
			b2.setCenter(loc2);

			world.addBodyToWorld(b1);
			world.addBodyToWorld(b2);
		}

		// vertical walls
		for (int i = 0; i < BOX_LENGTH * 4; i++)
		{
			Circle b1 = new Circle();
			Circle b2 = new Circle();
			b1.setMass(Constants.INFINITY);
			b2.setMass(Constants.INFINITY);

			int yLoc = (int) ((centerY - BOX_LENGTH * b1.getRadius() * 4) + (i * b1.getRadius()) * 2); 
			int xLoc1 = (int) (centerY - BOX_LENGTH * b1.getRadius() * 4);
			int xLoc2 = (int) (centerY + BOX_LENGTH * b1.getRadius() * 4);
			Vector2D loc1 = new Vector2D(xLoc1, yLoc);
			Vector2D loc2 = new Vector2D(xLoc2, yLoc);
			b1.setCenter(loc1);
			b2.setCenter(loc2);

			world.addBodyToWorld(b1);
			world.addBodyToWorld(b2);
		}
	}

	/* create a box of bodies to act as a wall-bounding box */
	public void makeBox()
	{
		int centerX = MAX_LOC / 2;
		int centerY = MAX_LOC / 2;
		Vector2D zero = new Vector2D(0, 0);

		// horizontal walls
		for (int i = 0; i < BOX_LENGTH + 1; i++)
		{
			Circle b1 = new Circle();
			Circle b2 = new Circle();
			//b1.setMass(Constants.INFINITY);
			//b2.setMass(Constants.INFINITY);
			b1.setMass(MAX_MASS);
			b2.setMass(MAX_MASS);
			
			int xLoc = (int) ((centerX - BOX_LENGTH * b1.getRadius()) + (i * b1.getRadius() * 2)); 
			int yLoc1 = (int) (centerY - BOX_LENGTH * b1.getRadius());
			int yLoc2 = (int) (centerY + BOX_LENGTH * b1.getRadius());
			Vector2D loc1 = new Vector2D(xLoc, yLoc1);
			Vector2D loc2 = new Vector2D(xLoc, yLoc2);
			b1.setCenter(loc1);
			b2.setCenter(loc2);

			world.addBodyToWorld(b1);
			world.addBodyToWorld(b2);
		}

		// vertical walls
		for (int i = 0; i < BOX_LENGTH; i++)
		{
			Circle b1 = new Circle();
			Circle b2 = new Circle();
			//b1.setMass(Constants.INFINITY);
			//b2.setMass(Constants.INFINITY);
			b1.setMass(20);
			b2.setMass(20);

			int yLoc = (int) ((centerY - BOX_LENGTH * b1.getRadius()) + (i * b1.getRadius() * 2)); 
			int xLoc1 = (int) (centerY - BOX_LENGTH * b1.getRadius());
			int xLoc2 = (int) (centerY + BOX_LENGTH * b1.getRadius());
			Vector2D loc1 = new Vector2D(xLoc1, yLoc);
			Vector2D loc2 = new Vector2D(xLoc2, yLoc);
			b1.setCenter(loc1);
			b2.setCenter(loc2);

			world.addBodyToWorld(b1);
			world.addBodyToWorld(b2);
		}
	}
	
	/* 2D headon collision */
	public void makeOffsetHeadOn()
	{
		// // Head on collision
		Circle b1 = new Circle();
		b1.setCenter(new Vector2D(45, MAX_LOC / 2 + 2));
		b1.setVi(new Vector2D(MAX_VELOCITY, 0));
		b1.setMass(5);

		Circle b2 = new Circle();
		b2.setCenter(new Vector2D(MAX_LOC - 45, MAX_LOC / 2 - 8));
		b2.setVi(new Vector2D(-MAX_VELOCITY, 0));
		b2.setMass(10);
		
		Circle b3 = new Circle();
		b3.setCenter(new Vector2D(MAX_LOC / 2 - 40, MAX_LOC / 2));
		b3.setVi(new Vector2D(0, 0));
		b3.setMass(25);

		world.addBodyToWorld(b1);
		world.addBodyToWorld(b2);
		world.addBodyToWorld(b3);
	}

	/* A headon collision (1d) */
	public void makeHeadOn()
	{
		// // Head on collision
		Circle b1 = new Circle();
		b1.setCenter(new Vector2D(10, MAX_LOC / 2));
		b1.setVi(new Vector2D(MAX_VELOCITY, 0));
		b1.setMass(5);

		Circle b2 = new Circle();
		b2.setCenter(new Vector2D(MAX_LOC - 10, MAX_LOC / 2));
		b2.setVi(new Vector2D(-MAX_VELOCITY, 0));
		b2.setMass(10);
		
		Circle b3 = new Circle();
		b3.setCenter(new Vector2D(MAX_LOC / 2 - 40, MAX_LOC / 2));
		b3.setVi(new Vector2D(0, 0));
		b3.setMass(50);

		world.addBodyToWorld(b1);
		world.addBodyToWorld(b2);
		world.addBodyToWorld(b3);

	}

	public void makeNewtonCradle()
	{
		int centerX = MAX_LOC / 2;
		int centerY = MAX_LOC / 2;
		Vector2D zero = new Vector2D(0, 0);
		
		Circle b1 = new Circle();
		b1.setMass(MAX_MASS);

		int xLoc = (int) ((centerX - BOX_LENGTH * b1.getRadius()) + (b1.getRadius()));
		int yLoc1 = MAX_LOC / 2;
		Vector2D loc1 = new Vector2D(xLoc, yLoc1);
		b1.setCenter(loc1);

		world.addBodyToWorld(b1);
		
		Circle b2 = new Circle();
		Circle b3 = new Circle();
		Circle b4 = new Circle();
		b2.setMass(MAX_MASS);
		b3.setMass(Constants.INFINITY);
		b4.setMass(Constants.INFINITY);
		
		Vector2D loc2 = new Vector2D (50, MAX_LOC/2);
		b2.setCenter(loc2);
		b2.setVi(new Vector2D(10, 0));
		
		b3.setCenter(new Vector2D(40, MAX_LOC/2));
		b4.setCenter(new Vector2D(MAX_LOC - 40, MAX_LOC/2));
		
		world.addBodyToWorld(b2);
		world.addBodyToWorld(b3);
		world.addBodyToWorld(b4);
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

			Circle b = new Circle();

			System.out.println("Velcity:" + velocity.getMagnitude() + "  X:" + location.x + " Y:" + location.y);
			b.setCenter(location);
			b.setVi(velocity);
			b.setMass(mass);

			// add the body to physics engine
			world.addBodyToWorld(b);
		} 

		// A single body w/ random velocity in middle
		Vector2D location = new Vector2D(MAX_LOC / 2, MAX_LOC / 2);
		Vector2D velocity = new Vector2D(generator.nextInt(MAX_VELOCITY), generator.nextInt(MAX_VELOCITY));
		int mass = 10;

		Circle b = new Circle();

		b.setCenter(location);
		b.setVi(velocity);
		b.setMass(mass);

		// add the body to physics engine
		world.addBodyToWorld(b);
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
			ArrayList<RigidBody> bodies = World.getThreadBodies(i);
			for (int j = 0; j < bodies.size(); j++) 
			{
				if (bodies.get(j).getType() == BodyType.CIRCLE)
				{
					Circle c = (Circle) bodies.get(j);
					int processId = c.getProcess();
					int quad = c.Quadrant;
					int depth = c.Depth;
					
					//switch(processId)
					switch(quad)
					{
					case 0:
						g2.setColor(new Color(30, 30, 30));
						g2.fill(c.getGeometry());
						break;
					case 1:
						g2.setColor(new Color(25, 10, 100));
						g2.fill(c.getGeometry());
						break;
					case 2:
						g2.setColor(new Color(100, 10, 25));
						g2.fill(c.getGeometry());
						break;
					case 3:
						g2.setColor(new Color(10, 100, 25));
						g2.fill(c.getGeometry());
						break;
					case 4:
						g2.setColor(new Color(150, 100, 25));
						g2.fill(c.getGeometry());
						break;
					case 5:
						g2.setColor(new Color(0, 100, 125));
						g2.fill(c.getGeometry());
						break;
					default:
						g2.setColor(new Color(200, 255, 50));
						g2.fill(c.getGeometry());
						break;
					}
				}
			}
			g2.setColor(new Color(0, 0, 0));
		}
		
		// Draw the complete quadtree 
		//drawQuadTree(World.getTree().getRoot(), g2);

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

	public void drawQuadTree(Node n, Graphics2D g2)
	{
		if (n == null)
			return;
		
		// Draw the parent node
		g2.drawRect(n.getBounds().x, n.getBounds().y, n.getBounds().width, n.getBounds().height);
		
		// draw all children of parent node
		ArrayList<Node> subNodes = n.getSubNodes();
		for (int i = 0; i < subNodes.size(); i++)
		{
			if (subNodes.get(i) != null)
				drawQuadTree(subNodes.get(i), g2);
		}
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

class ClickListener implements MouseListener
{

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		// Add an object to the location
		int centerX = arg0.getX();
		int centerY = arg0.getY();
		Vector2D zero = new Vector2D(0, 0);
		
		Circle b1 = new Circle();
		b1.setMass(10);
		Vector2D loc = new Vector2D(centerX, centerY);
		b1.setCenter(loc);

		TestGame.world.addBodyToWorld(b1);
	}
	
}
