package com.sjsu.physicsengine;

public class Constants 
{
	public static final Vector2D ZERO_VEC2D = new Vector2D(0, 0);
	public static final float EPSILON = 1.1920928955078125E-6f;
	public static final float PI = (float) Math.PI;
	public static final int INFINITY = 2147483645;
	
	public static final double NANO_TO_STEP = 1E-20; // for whatever reason, if doing this on PC put this at 1e-15, on mac have it at 1e-20
}
