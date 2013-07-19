package com.sjsu.physicsengine;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/* A wall is a rectangular object with infinite mass
 * and fixed in place.
 */
public class Wall extends RigidBody
{	
	private Rectangle2D.Double geometry;
	
	public Wall()
	{
//		type = ObjectType.WALL;
//		geometry = new Rectangle2D.Double();
//		this.setMass(Constants.INFINITY);
//		isFixed = true;
//		Vi = new Vector2D(0, 0);
//		Vf = new Vector2D(0, 0);
//		center = new Vector2D(0, 0);
	}
	
	
	/*************************
	 ****** OVERIDES**********
	 ************************/
	
	@Override
	public void update(long timeStep) 
	{
		// Walls can't move
		return;
	}

	@Override
	public void setCenter(Vector2D l) 
	{
		//this.center.set(l);
		this.geometry.x = l.x;
		this.geometry.y = l.y;
	}

	@Override
	public void setGeometry(RectangularShape s)
	{
		this.geometry = (Rectangle2D.Double) s;
		this.setCenter(new Vector2D((double)s.getX(), (double)s.getY()));
	}

	@Override
	public RectangularShape getGeometry() 
	{
		return ((RectangularShape) this.geometry);
	}

}
