package com.sjsu.physicsengine;

import java.awt.geom.RectangularShape;
import java.awt.geom.Ellipse2D.Float;


/* Generic object superclass */
public abstract class Object 
{
	/* Object types */
	public enum ObjectType 
	{
		WALL, CIRCLE;
	}
	
	private int id;
	private double mass;
	private double inverseMass;
	private boolean hasCollided;			// True if this object has collided this loop
	private Vector2D Vi;					// current(initial) velocity in this frame
	private Vector2D Vf;					// velocity of object in next frame (final velocity)
	private Vector2D center;
	private double e;						// Restitution.. a cheap way to represent this.. 0 (no bounce) -----> 1 (elastic)
	private double I;						// Rotation
	private boolean isFixed;
	private int process;
	private ObjectType type;
	
	public abstract void update(long timeStep);
	public abstract void setGeometry(RectangularShape s);
	public abstract RectangularShape getGeometry();
	
	
	
	/**************************
	 * ** GETTERS AND SETTERS
	 **************************/
	public void setId(int i)
	{
		id = i;
	}
	public void setHasCollided(boolean b)
	{
		this.hasCollided = b;
	}
	public void setProcess(int p)
	{
		process = p;
	}
	public void setVi(Vector2D v)
	{
		Vi = v;
	}
	public void setVf(Vector2D v)
	{
		Vf = v;
	}
	public void addToVf(Vector2D v)
	{
		// In the case that we have two objects colliding at once,
		// We need to add all velocities together for the next game loop
		// so that all resulting vectors are taken into account
		Vector2D tmpVec = this.getVf();
		this.setVf(tmpVec.add(v));
	}
	public void setMass(float m)
	{
		mass = m;
		inverseMass = 1 / m;
	}
	public void setRotation(float i)
	{
		I = i;
	}
	public void setFixed(boolean f)
	{
		isFixed = f;
	}
	public void setType(ObjectType t)
	{
		type = t;
	}
	public void setRestitution(double rest)
	{
		e = rest;
	}
	public void setCenter(Vector2D l)
	{
		center = l;
	}

	public int getId()
	{
		return id;
	}
	public ObjectType getType()
	{
		return type;
	}
	public int getProcess()
	{
		return process;
	}
	public Vector2D getVi()
	{
		return Vi;
	}
	public Vector2D getVf()
	{
		return Vf;
	}
	public Vector2D getCenter()
	{
		return center;
	}
	public double getMass()
	{
		return mass;
	}
	public double getInverseMass()
	{
		return inverseMass;
	}
	public double getRotation()
	{
		return I;
	}
	public double getRestitution()
	{
		return e;
	}
	public boolean isFixed()
	{
		return isFixed;
	}
	public boolean hasCollidedThisLoop()
	{
		return hasCollided;
	}
}
