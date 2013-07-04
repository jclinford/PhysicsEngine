package com.sjsu.physicsengine;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.sjsu.physicsengine.structures.QuadTree;

/* Takes care of threads and algorithms */

public class World 
{
	public static final int NUM_PROCESSORS = 4;
	private static ArrayList<PhysicsThread> threads;
	private static int bodyCount;
	private static QuadTree quadTree;
	private static long lastUpdate;
	private static long timeStep;

	
	public World()
	{
		// Initialize our quadTree with our bounds of the screen
		quadTree = new QuadTree(new Rectangle(0, 0, TestGame.MAX_LOC, TestGame.MAX_LOC));
		
		bodyCount = 0;
		threads = new ArrayList<PhysicsThread>();
		for (int i = 0; i < NUM_PROCESSORS; i++)
		{
			PhysicsThread thread = new PhysicsThread(this, i, NUM_PROCESSORS);
			thread.setPriority(1);
			threads.add(thread);
		}
		
		lastUpdate = System.nanoTime();
	}
	
	
	public void addBodyToWorld(RigidBody b)
	{
		int tIndex = bodyCount % threads.size();
		b.setProcess(tIndex);
		b.setId(bodyCount);

		threads.get(tIndex).addBody(b);
		
		bodyCount++;
	}
	
	// Get the bodies belonging to thread # n
	public static ArrayList<RigidBody> getThreadBodies(int n)
	{
		return threads.get(n).getBodies();
	}
	
	// Start all threads
	public void startThreads()
	{
		for (int i = 0; i < threads.size(); i++)
		{
			threads.get(i).start();
		}
	}
	
	
	// return the QuadTree
	public QuadTree getTree()
	{
		return quadTree;
	}
	
	
	// Physics loop, runs all collisions checks, takes velocity steps
	public void physicsStep()
	{	
		// Clear all objects from the quadtree every loop,
		// and reinsert all the objects
		// TODO maybe parallelize this?
		quadTree.clearBodies();
		for (int i = 0; i < threads.size(); i++)
		{
			threads.get(i).insertAllToPublicTree();
		}
		
		// Run collisions for all objects
		for (int i = 0; i < threads.size(); i++)
		{
			threads.get(i).run();
		}
		
		// Wait for all threads to finish calculations
		int threadFinishCount = 0;
		while (threadFinishCount < threads.size())
		{
			threadFinishCount = 0;
			for (int i = 0; i < threads.size(); i++)
			{
				if (threads.get(i).isProcessing() == false)
					threadFinishCount++;
			}
		}
		
		// Calculate the timeStep used for velocity steps
		// This will just be the time that has passed since last update
		long curTime = System.nanoTime();
		timeStep = curTime - lastUpdate;
		for (int i = 0; i < threads.size(); i++)
		{
			// Take velocity steps on each thread's objects
			threads.get(i).step(timeStep);
		}
		lastUpdate = System.currentTimeMillis();

		
		// Wait for all threads to finish
		while (threadFinishCount < threads.size())
		{
			threadFinishCount = 0;
			for (int i = 0; i < threads.size(); i++)
			{
				if (threads.get(i).isProcessing() == false)
					threadFinishCount++;
			}
		}
	}
}
