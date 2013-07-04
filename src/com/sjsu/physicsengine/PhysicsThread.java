package com.sjsu.physicsengine;

import java.util.ArrayList;
import java.util.Collections;

import com.sjsu.physicsengine.Collisions;

public class PhysicsThread extends Thread
{
	private int pNum;						// processor number
	private int numThreads;					// total number of processors
	private ArrayList<RigidBody> bodies;		// all bodies belonging to this processor
	private boolean isProcessing;			// flag to denote if this thread is processing
	private World myWorld;
	

	/* Start a physics thread to carry out all parallel processes
	 * p is the processor number
	 * numTs is total number of threads
	 */
	public PhysicsThread(World w, int p, int numTs) 
	{
		this.myWorld = w;
		this.isProcessing = false;
		this.pNum = p;
		this.numThreads = numTs;
		bodies = new ArrayList<RigidBody>();
	}

	@Override
	public void run() 
	{
		// Start process flag. Will be false when we finish a task
		this.isProcessing = true;
		ArrayList<RigidBody> possibleCollisions;
		
		// First check thread's bodies against its own bodies for collisions
		// uses the quadtree to maximize fps
		for (int i = 0; i < bodies.size(); i++)
		{
			possibleCollisions = myWorld.getTree().retrieveNeighbors(bodies.get(i));
			
//			System.out.println("Possible collisions: " + possibleCollisions);
//			System.out.println("Our objects: " + objects);
			for (int x = 0; x < possibleCollisions.size(); x++)
			{
				// These two different lists both can contain the same objects, so we don't need to check for collisions
				// if we are looking at the same object obviously. Continue to next object if we have duplicate entries
				if (possibleCollisions.get(x).getId() == bodies.get(i).getId())
						continue;
				
				// Check if we have a collision
				if (Collisions.hasCollision(bodies.get(i), possibleCollisions.get(x)) == true)
				{
//					System.out.println(this.pNum + ":Found collision a: " + objects.get(bIndex).getId() + "  b:" + objects.get(subIndex).getId());
					// Resolve the collision
					Collisions.resolveElastic(bodies.get(i), possibleCollisions.get(x), pNum);
				}
			}
		}
		
		// TODO any other calculations that need parallel checking
		
		// Done processing collision detection
		this.isProcessing = false;
	}
	
	public void step(long timeStep)
	{
		// Processing steps
		this.isProcessing = true;
		
		for (int j = 0; j < bodies.size(); j++)
		{
			// Move each object forward by its Vf that
			// was calculated in run()
			bodies.get(j).update(timeStep);
		}
		
		// Done processing steps
		this.isProcessing = false;
	}
	
	/* Insert all objects belonging to this thread to the common quadTree */
	public void insertAllToPublicTree()
	{
		myWorld.getTree().insertBodies(bodies);
	}

	/* Adds a body to this threads ownership */
	public void addBody(RigidBody o) 
	{
		System.out.println("P" + this.pNum + ": Adding rigidBody type: " + o.getType());
		bodies.add(o);
	}

	/* removes a body from this thread */
	public boolean removeBody(RigidBody o) 
	{
		return bodies.remove(o);
	}
	
	/* returns the bodies belonging to this thread */
	public ArrayList<RigidBody> getBodies()
	{
		return bodies;
	}
	
	/* returns whether this thread is currently working */
	public boolean isProcessing()
	{
		return this.isProcessing;
	}
}
