package com.sjsu.physicsengine;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;

import com.sjsu.physicsengine.Vector2D;


/* A rigid body circle */
public class Circle extends RigidBody
{
	private static final double DEFAULT_RADIUS = 4;
	private static final double DEFAULT_RESTITUTION = 1;
	private Ellipse2D.Double geometry;
	private static double radius;
	
	/* new default data */
	public Circle()
	{
		setId(0);			// id will be set when adding into the world
		setType(BodyType.CIRCLE);
		setVf(Constants.ZERO_VEC2D);
		setVi(Constants.ZERO_VEC2D);
		setFixed(false);
		setRestitution(DEFAULT_RESTITUTION);
		radius = DEFAULT_RADIUS;
		geometry = new Ellipse2D.Double(0, 0, radius * 2, radius * 2);
	}
	
	
	
	/* Steps the object forward in time
	 * by a 1/TIME_STEPth of time
	 */
	public void update(long timeStep)
	{
//		System.out.println("Vi : " + this.getVi() + "  Vf: " + this.getVf());
		Vector2D newLoc = new Vector2D();
		double newX = getCenter().x;
		double newY = getCenter().y;
		
		// If our object has collided the Vf is already calculated.
		// If there was no collision, then Vf is just Vi
		if (hasCollidedThisLoop() == false)
		{
//			System.out.println("No collision this loop: " + this);
			setVf(getVi());
		}
		
		// and a last_updated time
		newX += getVf().x * timeStep * Constants.NANO_TO_STEP;
		newY += getVf().y * timeStep * Constants.NANO_TO_STEP;
		
		//TODO update based on accelerations applied to body
		newLoc.set(newX, newY);
		this.setCenter(newLoc);
		
//		System.out.println("" + newX + ", " + newY + "  " + this.getVelocity() + "   ts: " +timeStep * Constants.NANO_TO_STEP);
		
		// New loop, reset our flag, Vi becomes Vf, and Vf resets
		setHasCollided(false);
		setVi(getVf());
		setVf(Constants.ZERO_VEC2D);
	}


	/*************************
	 ****** OVERIDES**********
	 ************************/
	@Override
	public void setCenter(Vector2D l) 
	{
		super.setCenter(l);
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
