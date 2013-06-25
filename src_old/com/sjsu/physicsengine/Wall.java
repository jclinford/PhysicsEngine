package com.sjsu.physicsengine;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/* A wall is a rectangular object with infinite mass
 * and fixed in place.
 */
public class Wall extends Object
{	
	private Rectangle2D.Float geometry;
	
	public Wall()
	{
		type = ObjectType.Wall;
		geometry = new Rectangle2D.Float();
		mass = Constants.INFINITY;
		isFixed = true;
		velocity = new Vector2D(0, 0);
		location = new Vector2D(0, 0);
	}
	
	
	/*************************
	 ****** OVERIDES**********
	 ************************/
	
	@Override
	public void update() 
	{
		// Walls can't move
		return;
	}

	@Override
	public void setLocation(Vector2D l) 
	{
		this.location.set(l);
		this.geometry.x = l.x;
		this.geometry.y = l.y;
	}

	@Override
	public void setGeometry(RectangularShape s)
	{
		this.geometry = (Rectangle2D.Float) s;
		this.setLocation(new Vector2D((float)s.getX(), (float)s.getY()));
	}

	@Override
	public RectangularShape getGeometry() 
	{
		return ((RectangularShape) this.geometry);
	}

}
