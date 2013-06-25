package com.sjsu.physicsengine;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.awt.geom.RectangularShape;

import com.sjsu.physicsengine.Vector2D;


/* A Standard Rigid body object */
public class Circle extends Object
{
	private static final double DEFAULT_RADIUS = 5;
	private Ellipse2D.Double geometry;
	private static double radius;
	
	/* new default data */
	public Circle()
	{
		this.setId(0);			// id will be set when adding into the world
		type = ObjectType.CIRCLE;
		radius = DEFAULT_RADIUS;
		geometry = new Ellipse2D.Double(0, 0, radius * 2, radius * 2);
		Vi = new Vector2D(0, 0);
		Vf = new Vector2D(0, 0);
		center = new Vector2D(0, 0);
		isFixed = false;
	}
	
	
	
	/* Steps the object forward in time
	 * by a 1/TIME_STEPth of time
	 */
	public void update(long timeStep)
	{
//		System.out.println("Vi : " + this.getVi() + "  Vf: " + this.getVf());
		Vector2D newLoc = new Vector2D();
		double newX = this.center.x;
		double newY = this.center.y;
		
		// If our object has collided the Vf is already calculated.
		// If there was no collision, then Vf is just Vi
		if (this.hasCollidedThisLoop() == false)
			this.setVf(this.getVi());
		
		
		// TODO instead step forward by time elapsed by having a timer
		// and a last_updated time
		newX += this.getVf().x * timeStep * Constants.NANO_TO_STEP;
		newY += this.getVf().y * timeStep * Constants.NANO_TO_STEP;
		
		//TODO update based on accelerations applied to body
		newLoc.set(newX, newY);
		this.setCenter(newLoc);
		
//		System.out.println("" + newX + ", " + newY + "  " + this.getVelocity() + "   ts: " +timeStep * Constants.NANO_TO_STEP);
		
		// New loop, reset our flags and Vf
		this.setHasCollided(false);
		this.setVf(Constants.ZERO_VEC2D);
	}


	/*************************
	 ****** OVERIDES**********
	 ************************/
	@Override
	public void setCenter(Vector2D l) 
	{
		this.center.set(l);
		this.geometry.setFrame(l.x - this.getRadius(), l.y - this.getRadius(), 
				this.getRadius() * 2, this.getRadius() * 2);
	}
	
	

	@Override
	public void setGeometry(RectangularShape s)
	{
		this.geometry = (Ellipse2D.Double) s;
	}

	@Override
	public RectangularShape getGeometry() 
	{
		return ((RectangularShape) this.geometry);
	}
	
	
	
	
	public void setRadius(double r)
	{
		radius = r;
	}
	public double getRadius()
	{
		return radius;
	}
}
