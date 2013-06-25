package com.sjsu.physicsengine;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.awt.geom.RectangularShape;

import com.sjsu.physicsengine.Vector2D;


/* A Standard Rigid body object */
public class Body extends Object
{
	private static final float WIDTH = 5;
	private static final float HEIGHT = 5;
	
	private Ellipse2D.Float geometry;
	private static long lastUpdate;
	private static long timeStep;
	
	/* new default data */
	public Body()
	{
		id = 0;			// id will be set when adding into the world
		type = ObjectType.Body;
		geometry = new Ellipse2D.Float(0, 0, WIDTH, HEIGHT);
		velocity = new Vector2D(0, 0);
		location = new Vector2D(0, 0);
		isFixed = false;
		lastUpdate = System.nanoTime();
	}
	
	
	
	/* Steps the object forward in time
	 * by a 1/TIME_STEPth of time
	 */
	public void update()
	{
		float newX = this.geometry.x;
		float newY = this.geometry.y;
		long curTime = System.nanoTime();
		timeStep = curTime - lastUpdate;
				
		// TODO instead step forward by time elapsed by having a timer
		// and a last_updated time
		newX += this.velocity.x * timeStep * Constants.NANO_TO_STEP;
		newY += this.velocity.y * timeStep * Constants.NANO_TO_STEP;
		
		//TODO update based on accelerations applied to body
		
		this.location.set(newX, newY);
		this.geometry.setFrame(newX, newY, WIDTH, HEIGHT);
		
//		System.out.println("" + newX + ", " + newY + "  " + this.getVelocity() + "   ts: " +timeStep * Constants.NANO_TO_STEP);
		
		lastUpdate = System.currentTimeMillis();
	}


	/*************************
	 ****** OVERIDES**********
	 ************************/
	@Override
	public void setLocation(Vector2D l) 
	{
		this.location.set(l);
		this.geometry.setFrame(l.x, l.y, WIDTH, HEIGHT);
	}

	@Override
	public void setGeometry(RectangularShape s)
	{
		this.geometry = (Ellipse2D.Float) s;
	}

	@Override
	public RectangularShape getGeometry() 
	{
		return ((RectangularShape) this.geometry);
	}
}
