package com.sjsu.physicsengine;
import com.sjsu.physicsengine.Constants;

/* 
 * A 2-dimensional (column) general purpose vector
 */
public class Vector2D
{
	public double x;
	public double y;
	
	public Vector2D()
	{
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Vector2D(double initX, double initY)
	{
		this.x = initX;
		this.y = initY;
	}
	
	public Vector2D(Vector2D copy)
	{
		this(copy.x, copy.y);
	}
	
	
	
	/*
	 * SELF METHODS THAT EFFECT THIS VECTOR
	 */
	public final void zeroVector()
	{
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public final Vector2D set(double newX, double newY)
	{
		this.x = newX;
		this.y = newY;
		return this;
	}
	
	public final Vector2D set(Vector2D newVec)
	{
		this.x = newVec.x;
		this.y = newVec.y;
		return this;
	}
	
	
	
	/*
	 * METHODS THAT RETURN A NEW VECTOR (DOESNT CHANGE OUR VECTOR ATTRIBUTES)
	 */
	
	/* sum vec2 with this vector and return result */
	public final Vector2D add(Vector2D vec2)
	{
		Vector2D resultVec;
		
		resultVec = new Vector2D(this.x + vec2.x, this.y + vec2.y);
		
		return resultVec;
	}
	
	/* subtract vec2 with this vector and return the result */
	public final Vector2D subtract(Vector2D vec2)
	{
		Vector2D resultVec;
		
		resultVec = new Vector2D(this.x - vec2.x, this.y - vec2.y);
		
		return resultVec;
	}
	
	/* multiply a scalar with this vector and return the result */
	public final Vector2D multiply(double scalar)
	{
		Vector2D resultVec;
		
		resultVec = new Vector2D(this.x * scalar, this.y * scalar); 
		
		return resultVec;
	}
	
	/* divide a scalar with this vector and return the result */
	public final Vector2D divide(double scalar)
	{
		Vector2D resultVec;
		
		resultVec = new Vector2D(this.x / scalar, this.y / scalar);
		
		return resultVec;
	}
	
	
	public final double getMagnitude()
	{
		double length;
		
		length = Math.sqrt(x * x + y * y);
		
		return length;
	}
	
	// Quicker than getMagnitured since it doesnt use sqrt
	// returns x^2 + y^2
	public final double getMagnitudeSquared()
	{
		double lengthSquare;
		
		lengthSquare = x * x + y * y;
		
		return lengthSquare;
	}
	
	public final static Vector2D abs(Vector2D v)
	{
		return new Vector2D(Math.abs(v.x), Math.abs(v.y));
	}
	
	// Dot this.b
	public final double dot(Vector2D b)
	{
		return (this.x * b.x + this.y * b.y);
	}
	
	// a.b
	public final static double dot(Vector2D a, Vector2D b)
	{
		return (a.x * b.x + a.y * b.y);
	}
	
	public final Vector2D normalize() 
	{
		Vector2D resultVec;
		double magnitude = getMagnitude();
		double invertedLength = 1.0 / magnitude;
		
		resultVec = new Vector2D(this.x * invertedLength, this.y * invertedLength);
		
		return resultVec;
	}
	
	
	
	/*
	 * OVERRIDES
	 * 
	 */
	
	@Override
	public final String toString() 
	{
		String text = "(" + x + "," + y + ")";
		return text;
	}
	
	@Override
	public final Vector2D clone()
	{
		return new Vector2D(x, y);
	}
}
