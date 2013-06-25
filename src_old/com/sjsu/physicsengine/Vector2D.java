package com.sjsu.physicsengine;
import com.sjsu.physicsengine.Constants;

/* 
 * A 2-dimensional (column) general purpose vector
 */
public class Vector2D
{
	public float x;
	public float y;
	
	public Vector2D()
	{
		this.x = 0f;
		this.y = 0f;
	}
	
	public Vector2D(float initX, float initY)
	{
		this.x = initX;
		this.y = initY;
	}
	
	public Vector2D(Vector2D copy)
	{
		this(copy.x, copy.y);
	}
	
	public final void zeroVector()
	{
		this.x = 0.0f;
		this.y = 0.0f;
	}
	
	public final Vector2D set(float newX, float newY)
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
	public final Vector2D multiply(float scalar)
	{
		Vector2D resultVec;
		
		resultVec = new Vector2D(this.x * scalar, this.y * scalar); 
		
		return resultVec;
	}
	
	
	public final double getMagnitude()
	{
		double length;
		
		length = Math.sqrt(x * x + y * y);
		
		return length;
	}
	
	
	public final double normalize() 
	{
		double magnitude = getMagnitude();
		
		// if almost zero, just round down
		if (magnitude < Constants.EPSILON)
			return 0f;
		
		double invertedLength = 1.0f / magnitude;
		x *= invertedLength;
		y *= invertedLength;
		
		return magnitude;
	}
	
	
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
	
	public final static Vector2D abs(Vector2D v)
	{
		return new Vector2D(Math.abs(v.x), Math.abs(v.y));
	}
	public final static float dot(Vector2D a, Vector2D b)
	{
		return (a.x * b.x + a.y * b.y);
	}
	public final static float cross(Vector2D a, Vector2D b)
	{
		return (a.x * b.y - a.y * b.x);
	}
	public final static void cross(Vector2D a, float s, Vector2D out)
	{
		float tmp = -s * a.x;
		out.x = s * a.y;
		out.y = tmp;
	}
	public final static void cross(float s, Vector2D a, Vector2D out)
	{
		float tmp = s * a.x;
		out.x = -s * a.y;
		out.y = tmp;
	}
	public final static Vector2D cross(Vector2D a, float s)
	{
		return new Vector2D(s * a.y, -s * a.x);
	}
	
	public final static void min(Vector2D a, Vector2D b, Vector2D out)
	{
		out.x = a.x < b.x ? a.x : b.x;
		out.y = a.y < b.y ? a.y : b.y;
	}
	public final static void max(Vector2D a, Vector2D b, Vector2D out)
	{
		out.x = a.x > b.x ? a.x : b.x;
		out.y = a.y > b.y ? a.y : b.y;
	}
}
