package com.sjsu.physicsengine;

import java.awt.Rectangle;

/* Collision checks */
public class Collisions
{
	/* checks if a collision is occuring */
	public static boolean hasCollision(Object a, Object b)
	{
		if (a == null || b == null)
		{
			System.out.println("null yo");
			return false;
		}
		
		Rectangle rec1 = a.getGeometry().getBounds();
		Rectangle rec2 = b.getGeometry().getBounds();
		
		//System.out.println("Atype: " + a.getType() + "   Btype: " + b.getType());
		/*if (a.getType() == Object.ObjectType.Wall)
			System.out.println(rec1);
		if (b.getType() == Object.ObjectType.Wall)
			System.out.println(rec2);*/
		
		if (rec1.intersects(rec2))
		{
			//if (a.getType() == Object.ObjectType.Wall || b.getType() == Object.ObjectType.Wall)
			//	System.out.println("Collision" + rec1.x + "" + rec1.y);
			return true;
		}
		
		return false;
	}
	
	/* calculates the results of an 2d elastic collision */
	public static void resolveElastic(Object a, Object b)
	{
		float phi;														// angle of collision
		float thetaA;													// angle of motion for body A
		float thetaB;													// angle of motion for body B
		float Ma = a.getMass();											// Mass of body A
		float Mb = b.getMass();											// Mass of B
		Vector2D Va = new Vector2D();									// Velocity of A
		Vector2D Vb = new Vector2D();									// Velocity of B
		Vector2D Vaf = new Vector2D();									// Final Velocity of A
		Vector2D Vbf = new Vector2D();									// Final Velocity of B
		float MagA = (float) a.getVelocity().getMagnitude();			// Magnitude of Va
		float MagB = (float) b.getVelocity().getMagnitude();			// Magnitude of Vb
		
		// phi will be the angle along x that a line between the two centers make
		float dx = (float) (b.getGeometry().getCenterX() - a.getGeometry().getCenterX());
		float dy = (float) (b.getGeometry().getCenterY() - a.getGeometry().getCenterY());
		
		phi = findAngle(dx, dy);
		thetaA = findAngle(a.getVelocity().x, a.getVelocity().y);
		thetaB = findAngle(b.getVelocity().x, b.getVelocity().y);
		
//		System.out.println("ObjectA:" + a.getId() + "    ObjectB:" + b.getId());
//		System.out.println("phi:" + phi + "  thetaA:" + thetaA + "  massA:" + Ma + "  magA:" + MagA +
//				"  thetaB:" + thetaB + "  massB:" + Mb + "  magB:" + MagB);
		
		// rotate our frame of reference to match angle of collision
		Va.set(MagA * (float)Math.cos(thetaA - phi), MagA * (float)Math.sin(thetaA - phi));
		Vb.set(MagB * (float)Math.cos(thetaB - phi), MagB * (float)Math.sin(thetaB - phi));
		
		
//		System.out.println("VaRotate:" + Va + "  VbRotate:" + Vb);
		
		// conservation of momentum for x components in new frame
		// y components we disregard since we rotated
		Vaf.set( ((Ma - Mb) * Va.x + 2 * Mb * Vb.x) / (Ma + Mb), Va.y);
		Vbf.set( ((Mb - Ma) * Vb.x + 2 * Ma * Va.x) / (Ma + Mb), Vb.y);
		
//		System.out.println("VafRotate:" + Vaf + "  VbfRotate:" + Vbf);
		
		// Unrotate the velocities back to original frame
		Vaf.set((float)Math.cos(phi) * Vaf.x + (float)Math.cos(phi + Math.PI / 2) * Vaf.y,
				(float)Math.sin(phi) * Vaf.x + (float)Math.sin(phi + Math.PI / 2) * Vaf.y);
		Vbf.set((float)Math.cos(phi) * Vbf.x + (float)Math.cos(phi + Math.PI / 2) * Vbf.y,
				(float)Math.sin(phi) * Vbf.x + (float)Math.sin(phi + Math.PI / 2) * Vbf.y);
		
//		System.out.println("Vaf:" + Vaf + "  Vbf:" + Vbf);
		
		// set the bodies new velocity if they're not fixed bodies
		// Only set velocity if its owned by process number
		if (!a.isFixed())
			a.getVelocity().set(Vaf);
		if (!b.isFixed())
			b.getVelocity().set(Vbf);
		
//		if (a.getQuad() != b.getQuad())
//		{
			//System.out.println(" " + a.getQuad() + "     " + b.getQuad() + "    pid: " + processId);
//		}

		
		// If magnitude is so small just set it to zero velocity
		if (Vaf.getMagnitude() < Constants.EPSILON)
			a.getVelocity().set(Constants.ZERO_VEC2D);
		if (Vbf.getMagnitude() < Constants.EPSILON)
			b.getVelocity().set(Constants.ZERO_VEC2D);
	}
	
	
	
	/* find an angle between a line and x-axis */ 
	private static float findAngle(float dx, float dy)
	{
		float theta;
		
		if (dx < 0)
			theta = (float) (Math.PI + Math.atan(dy / dx));
		else if (dx > 0 && dy >= 0)
			theta = (float) Math.atan(dy / dx);
		else if (dx > 0 && dy < 0)
			theta = (float) (2 * Math.PI + Math.atan(dy / dx));
		else if (dx == 0 && dy == 0)
			theta = 0;
		else if (dx == 0 && dy >= 0)
			theta = (float) (Math.PI / 2);
		else
			theta = (float) (3 * Math.PI / 2);
		
		return theta;
	}
}
