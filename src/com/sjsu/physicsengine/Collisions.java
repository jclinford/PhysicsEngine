package com.sjsu.physicsengine;
import java.awt.geom.Rectangle2D;

import com.sjsu.physicsengine.Object;
import com.sjsu.physicsengine.Object.ObjectType;

/* Collision checks */
public class Collisions
{
	/* checks if a collision is occuring */
	public static boolean hasCollision(Object a, Object b)
	{
		boolean hasCollision = false;
		
		//System.out.println("Atype: " + a.getType() + "   Btype: " + b.getType());
		/*if (a.getType() == Object.ObjectType.Wall)
			System.out.println(rec1);
		if (b.getType() == Object.ObjectType.Wall)
			System.out.println(rec2);*/
		
		switch(a.type)
		{
		case CIRCLE:
			if (b.type == ObjectType.CIRCLE)
			{
				// Circle on Circle collision check
				Circle circleA = (Circle) a;
				Circle circleB = (Circle) b;
				
				// We collide if the distance between two centers is less than the sum of their radii
				if (circleA.getCenter().euclideanDistance(circleB.getCenter()) <
						circleA.getRadius() + circleB.getRadius())
					hasCollision = true;
				else
					hasCollision = false;
			}
			break;
		default:
			hasCollision = false;
		}
		
//		System.out.println(hasCollision);
		return hasCollision;
	}
	
	/* calculates the results of an 2d elastic collision */
	public static void resolveElastic(Object a, Object b, int processId)
	{
		double phi;														// angle of collision
		double thetaA;													// angle of motion for body A
		double thetaB;													// angle of motion for body B
		double Ma = a.getMass();										// Mass of body A
		double Mb = b.getMass();										// Mass of B
		Vector2D Va = new Vector2D();									// Velocity of A
		Vector2D Vb = new Vector2D();									// Velocity of B
		Vector2D Vaf = new Vector2D();									// Final Velocity of A
		Vector2D Vbf = new Vector2D();									// Final Velocity of B
		double MagA = a.getVi().getMagnitude();					// Magnitude of Va
		double MagB = b.getVi().getMagnitude();					// Magnitude of Vb
		
		// phi will be the angle along x that a line between the two centers make
		double dx = b.getCenter().x - a.getCenter().x;
		double dy = b.getCenter().y - a.getCenter().y;
		
		phi = findAngle(dx, dy);
		thetaA = findAngle(a.getVi().x, a.getVi().y);
		thetaB = findAngle(b.getVi().x, b.getVi().y);
		
//		System.out.println("ProccessID: " + processId + "   ObjectA:" + a.getId() + "    ObjectB:" + b.getId());
//		System.out.println("phi:" + phi + "  thetaA:" + thetaA + "  massA:" + Ma + "  magA:" + MagA +
//				"  thetaB:" + thetaB + "  massB:" + Mb + "  magB:" + MagB);
		
		// rotate our frame of reference to match angle of collision
		Va.set(MagA * Math.cos(thetaA - phi), MagA * Math.sin(thetaA - phi));
		Vb.set(MagB * Math.cos(thetaB - phi), MagB * Math.sin(thetaB - phi));
		
		
//		System.out.println("VaRotate:" + Va + "  VbRotate:" + Vb);
		
		// conservation of momentum for x components in new frame
		// y components we disregard since we rotated
		Vaf.set( ((Ma - Mb) * Va.x + 2 * Mb * Vb.x) / (Ma + Mb), Va.y);
		Vbf.set( ((Mb - Ma) * Vb.x + 2 * Ma * Va.x) / (Ma + Mb), Vb.y);
		
//		System.out.println("VafRotate:" + Vaf + "  VbfRotate:" + Vbf);
		
		// Unrotate the velocities back to original frame
		Vaf.set(Math.cos(phi) * Vaf.x + Math.cos(phi + Math.PI / 2) * Vaf.y,
				Math.sin(phi) * Vaf.x + Math.sin(phi + Math.PI / 2) * Vaf.y);
		Vbf.set(Math.cos(phi) * Vbf.x + Math.cos(phi + Math.PI / 2) * Vbf.y,
				Math.sin(phi) * Vbf.x + Math.sin(phi + Math.PI / 2) * Vbf.y);
		
//		System.out.println("Vaf:" + Vaf + "  Vbf:" + Vbf);
		
		// set the bodies new velocity if they're not fixed bodies.
		// Only set velocity if its owned by process number.
		// Add the new velocity to the future velocity that will take place next game loop.
		// This avoids updating velocities in middle of parallel-calculations
		if (!a.isFixed() && processId == a.getProcess())
		{
			a.addToVf(Vaf);
//			System.out.println("set A: " + a + " to : " + Vaf);
		}
		if (!b.isFixed() && processId == b.getProcess())
		{
			b.addToVf(Vbf);
//			System.out.println("set B: " + b + " to : " + Vbf);
		}
	}
	
	
	
	/* find an angle between a line and x-axis */ 
	private static double findAngle(double dx, double dy)
	{
		double theta;
		
		if (dx < 0)
			theta = Math.PI + Math.atan(dy / dx);
		else if (dx > 0 && dy >= 0)
			theta = Math.atan(dy / dx);
		else if (dx > 0 && dy < 0)
			theta = 2 * Math.PI + Math.atan(dy / dx);
		else if (dx == 0 && dy == 0)
			theta = 0;
		else if (dx == 0 && dy >= 0)
			theta = Math.PI / 2;
		else
			theta = 3 * Math.PI / 2;
		
		return theta;
	}
}
