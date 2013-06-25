package com.sjsu.physicsengine;

import java.awt.Rectangle;
import java.util.ArrayList;

/* Takes care of threads and algorithms */

public class World 
{
	public static final int NUM_PROCESSORS = 1;
	private static ArrayList<PhysicsThread> threads;
	private static int objectCount;
	private static QuadTree quadTree;

	
	public World()
	{
		// Initialize our quadTree with our bounds of the screen
		quadTree = new QuadTree(0, new Rectangle(0, 0, TestGame.MAX_LOC, TestGame.MAX_LOC));
		
		objectCount = 0;
		threads = new ArrayList<PhysicsThread>();
		for (int i = 0; i < NUM_PROCESSORS; i++)
		{
			PhysicsThread thread = new PhysicsThread(this, i, NUM_PROCESSORS);
			thread.setPriority(1);
			threads.add(thread);
		}
	}
	
	// TODO find the correct thread it needs to be added to
	public void addObjectToWorld(Object b)
	{
		//int quadrant = quadTree.getIndex(b);
		//if (quadrant == -1)
		//	quadrant = 0;
		//int tIndex =  quadrant % NUM_PROCESSORS;
		int tIndex = objectCount % NUM_PROCESSORS;
		threads.get(tIndex).addObject(b);
		b.setId(objectCount);
		
		objectCount++;
	}
	
	public static void updateObjectThread(Object b, int newThread)
	{
		threads.get(b.getQuad()).getObjects().remove(b);
		
		b.setQuad(newThread);
		threads.get(newThread).getObjects().add(b);
	}
	
	// Get the bodies belonging to thread # n
	public static ArrayList<Object> getThreadObjects(int n)
	{
		return threads.get(n).getObjects();
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
		quadTree.clear();
		for (int i = 0; i < threads.size(); i++)
		{
			threads.get(i).insertToPublicTree();
		}
		
		// Run collisions for all objects
		for (int i = 0; i < threads.size(); i++)
		{
			threads.get(i).run();
		}
		
		// Take velocity steps
		for (int i = 0; i < threads.size(); i++)
		{
			threads.get(i).step();
		}

		
		// Wait for all threads to finish
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
	}
}
