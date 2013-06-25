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
		// Start process flag. Will be false when all tasks are finished (when sort happens)
		this.isProcessing = true;
		ArrayList<Object> possibleCollisions = new ArrayList<Object>();
		
//		System.out.println("pNum: " + pNum + "  objects: " + objects.size());
		
		// First check thread's bodies against its own bodies for collisions
		// uses the quadtree to maximize fps
		for (int i = 0; i < objects.size(); i++)
		{
			possibleCollisions.clear();
			myWorld.getTree().retrieve(possibleCollisions, objects.get(i));
			
			for (int x = 0; x < possibleCollisions.size(); x++)
			{
				// if collision, resolve
				if (Collisions.hasCollision(objects.get(i), possibleCollisions.get(x)) == true)
				{
//					System.out.println(this.pNum + ":Found collision a: " + objects.get(bIndex).getId() + "  b:" + objects.get(subIndex).getId());
					Collisions.resolveElastic(objects.get(i), possibleCollisions.get(x));
				}
			}
		}
		
		// TODO any other calculations that need parallel checking
	}
	
	public void step()
	{
		for (int j = 0; j < objects.size(); j++)
		{
			objects.get(j).update();
		}
		
		// after we take all steps we are done processing
		this.isProcessing = false;
	}
	
	/* Insert all objects belonging to this thread to the 
	 * common quadTree
	 */
	public void insertToPublicTree()
	{
		for (int i = 0; i < objects.size(); i++)
			myWorld.getTree().insert(objects.get(i));
	}

	/* Adds a body to this threads ownership */
	public void addObject(Object o) 
	{
//		System.out.println("P" + this.pNum + ": Adding object type: " + o.getType());
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
