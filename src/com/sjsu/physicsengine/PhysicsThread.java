package com.sjsu.physicsengine;

import java.util.ArrayList;
import java.util.Collections;

import com.sjsu.physicsengine.Collisions;

public class PhysicsThread extends Thread
{
	private int pNum;						// processor number
	private int numThreads;					// total number of processors
	private ArrayList<Object> objects;		// all bodies belonging to this processor
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
		objects = new ArrayList<Object>();
	}

	@Override
	public void run() 
	{
		// Start process flag. Will be false when we finish a task
		this.isProcessing = true;
		ArrayList<Object> possibleCollisions = new ArrayList<Object>();
		
		// First check thread's bodies against its own bodies for collisions
		// uses the quadtree to maximize fps
		for (int i = 0; i < objects.size(); i++)
		{
			possibleCollisions.clear();
			myWorld.getTree().retrieve(possibleCollisions, objects.get(i));
			
			for (int x = 0; x < possibleCollisions.size(); x++)
			{
				// These two different lists both can contain the same objects, so we don't need to check for collisions
				// if we are looking at the same object obviously. Continue to next object if we have duplicate entries
				if (possibleCollisions.get(x).getId() == objects.get(i).getId())
						continue;
				
				// Check if we have a collision
				if (Collisions.hasCollision(objects.get(i), possibleCollisions.get(x)) == true)
				{
//					System.out.println(this.pNum + ":Found collision a: " + objects.get(bIndex).getId() + "  b:" + objects.get(subIndex).getId());
					// Resolve the collision
					Collisions.resolveElastic(objects.get(i), possibleCollisions.get(x), pNum);
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
		
		for (int j = 0; j < objects.size(); j++)
		{
			// Move each object forward by its Vf that
			// was calculated in run()
			objects.get(j).update(timeStep);
		}
		
		// Done processing steps
		this.isProcessing = false;
	}
	
	/* Insert all objects belonging to this thread to the common quadTree */
	public void insertToPublicTree()
	{
		for (int i = 0; i < objects.size(); i++)
			myWorld.getTree().insert(objects.get(i));
	}

	/* Adds a body to this threads ownership */
	public void addObject(Object o) 
	{
		System.out.println("P" + this.pNum + ": Adding object type: " + o.getType());
		objects.add(o);
	}

	/* removes a body from this thread */
	public boolean removeObject(Object o) 
	{
		return objects.remove(o);
	}
	
	/* returns the bodies belonging to this thread */
	public ArrayList<Object> getObjects()
	{
		return objects;
	}
	
	/* returns whether this thread is currently working */
	public boolean isProcessing()
	{
		return this.isProcessing;
	}
}
