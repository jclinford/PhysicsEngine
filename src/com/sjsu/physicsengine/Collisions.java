package com.sjsu.physicsengine;
import java.awt.geom.Rectangle2D;

import com.sjsu.physicsengine.Object;
import com.sjsu.physicsengine.Object.ObjectType;

/* Collision checks */
public class Collisions
{
	/* 
	 * Check to see if an object is colliding with another object
	 * Returns true if we have a collision, false if we do not
	 */
	public static boolean hasCollision(Object a, Object b)
	{
		boolean hasCollision = false;
		
		//System.out.println("Atype: " + a.getType() + "   Btype: " + b.getType());
		
		// Need to calculate the collisions differently based on what objects we have
		switch(a.getType())
		{
			case CIRCLE:
				if (b.getType() == ObjectType.CIRCLE)
				{
					// Circle on Circle penetration calc
					Circle circleA = (Circle) a;
					Circle circleB = (Circle) b;
				
					// Check to see if we are colliding. If ||A-B||^2 < (r1 + r2)^2 then we have two circles penetrating each other
					// (ie if the distance between the two circles is smaller than their radii we have a collision)
					// I squared this formula because ||A-B||^2 is faster to calculate than ||A-B|| due to sqrt function
					if (circleA.getCenter().subtract(circleB.getCenter()).getMagnitudeSquared() < 
							( (circleA.getRadius() + circleB.getRadius()) * circleA.getRadius() + circleB.getRadius()) )
						hasCollision = true;
					else
						hasCollision = false;
				}
			break;
			default:
				hasCollision = false;
		}
		
		// If we have a collision set the flag
		if (hasCollision)
		{
			a.setHasCollided(true);
			b.setHasCollided(true);
		}
		
//		System.out.println(hasCollision);
		return hasCollision;
	}
	
	/* calculates the results of an 2d elastic collision */
	public static void resolveElastic(Object a, Object b, int processId)
	{
		switch (a.getType())
		{
			case CIRCLE:
				if (b.getType() == ObjectType.CIRCLE)
				{
					// Circle on Circle collision
					resolveElasticCircleOnCircle((Circle) a, (Circle) b, processId);
				}
				break;
			default:
				System.out.println("Collision type not known...");	
		}
	}
	
	
	/* Resolve a circle vs circle collision */
	private static void resolveElasticCircleOnCircle(Circle a, Circle b, int processId)
	{			
		Vector2D Ia = new Vector2D();								// Linear Impulse of a
		Vector2D Ib = new Vector2D();								// Linear Impulse of b
		Vector2D Vr = (a.getVi().subtract(b.getVi()));				// The velocity of A relative to B
		Vector2D AB = a.getCenter().subtract(b.getCenter());		// Distance between A and B center's


		// See also http://www.wildbunny.co.uk/blog/2011/04/06/physics-engines-for-dummies/
		
		// taken from http://stackoverflow.com/questions/345838/ball-to-ball-collision-detection-and-handling
		// If our objects are colliding, they are most likely overlapping. We need to correct the overlap/penetration
		// before we resolve the actual collision so it is most accurate, aka MTD (Minimum Translation Distance)
		double d = AB.getMagnitude();
		Vector2D MTD = AB.multiply(((a.getRadius() + b.getRadius()) - d) / d);
		
		// Push the two objects apart
		Vector2D newCa = a.getCenter();
		Vector2D newCb = b.getCenter();
		newCa = newCa.add(MTD.multiply(a.getInverseMass() / (a.getInverseMass() + b.getInverseMass())));
		newCb = newCb.subtract(MTD.multiply(b.getInverseMass() / (a.getInverseMass() + b.getInverseMass())));
		a.setCenter(newCa);
		b.setCenter(newCb);
		
		// Calculate the impact speed
		double V = Vr.dot(MTD.normalize());
		
		// If we have collision but they are moving away from each other we don't need to resolve
		if (V > 0)
			return;
		
		// Calc the collision impulse
		double ia = (- (1 + a.getRestitution() * V) / (a.getInverseMass() + b.getInverseMass()));
		double ib = ( (1 + b.getRestitution() * V) / (a.getInverseMass() + b.getInverseMass()));
		Ia = MTD.normalize().multiply(ia);
		Ib = MTD.normalize().multiply(ib);
		
		// Set the new velocities if the object belongs to the process
		if (processId == a.getProcess())
		{
			System.out.println("Setting a: " + a + "  from: " + a.getVi() + "  to: "  + Ia.multiply(a.getInverseMass()));
			a.addToVf(Ia.multiply(a.getInverseMass()));
		}
		if (processId == b.getProcess())
		{
			System.out.println("Setting b: " + b + "  from: " + b.getVi() + "  to: "  + Ib.multiply(b.getInverseMass()));
			b.addToVf(Ib.multiply(b.getInverseMass()));
		}
	}
}
