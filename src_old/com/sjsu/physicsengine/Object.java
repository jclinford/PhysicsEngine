package com.sjsu.physicsengine;

import java.awt.geom.RectangularShape;
import java.awt.geom.Ellipse2D.Float;


/* Generic object superclass */
public abstract class Object 
{
	/* Object types */
	public static enum ObjectType 
	{
		Wall, Body;
	}
	
	protected int id;
	protected float mass;
	protected Vector2D velocity;
	protected Vector2D location;
	protected float I;
	protected boolean isFixed;
	protected int curQuadrant;
	protected ObjectType type;
	
	public abstract void update();
	public abstract void setLocation(Vector2D l);
	public abstract void setGeometry(RectangularShape s);
	public abstract RectangularShape getGeometry();
	
	
	
	/**************************
	 * ** GETTERS AND SETTERS
	 **************************/
	public void setId(int i)
	{
		id = i;
	}
	public void setQuad(int q)
	{
		curQuadrant = q;
	}
	public void setVelocity(Vector2D v)
	{
		velocity = v;
	}
	public void setMass(float m)
	{
		mass = m;
	}
	public void setRotation(float i)
	{
		I = i;
	}
	public void setFixed(boolean f)
	{
		isFixed = f;
	}

	public int getId()
	{
		return id;
	}
	public ObjectType getType()
	{
		return type;
	}
	public int getQuad()
	{
		return curQuadrant;
	}
	public Vector2D getVelocity()
	{
		return velocity;
	}
	public Vector2D getLocation()
	{
		return location;
	}
	public float getMass()
	{
		return mass;
	}
	public float getRotation()
	{
		return I;
	}
	public boolean isFixed()
	{
		return isFixed;
	}
}
