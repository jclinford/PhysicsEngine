package com.sjsu.physicsengine.structures;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.sjsu.physicsengine.RigidBody;

/* 
 * Quad tree, like a binary tree but with four node children
 * used to simplify collision detection
 */

// Check out http://devmag.org.za/2011/02/23/quadtrees-implementation/

public class QuadTree 
{
	private int MAX_BODIES = 10;
	private int MAX_LEVELS = 1000;
	
	private int level;
	private ArrayList<RigidBody> myBodies;
	private Rectangle bounds;
	private QuadTree[] nodes;
	
	
	public QuadTree(int l, Rectangle b)
	{
		level = l;
		myBodies = new ArrayList<RigidBody>();
		bounds = b;
		nodes = new QuadTree[4];
	}
	
	/* Set bounds of this tree and reload its contents */
	public void setBounds(int x, int y, int width, int height)
	{
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
		clear();
		split();
	}
	
	/* clear the entire tree and all sub-trees */
	public void clear()
	{
//		// let the garbage collector clean up
//		if (level == 0)
//		{
//			objects = new ArrayList<Object>();
//			nodes = new QuadTree[4];
//		}
		
		// below code is clear if we dont want garbage collector
		// doing all the heavy lifting
		myBodies.clear();
		for (int i = 0; i < nodes.length; i++)
		{
			if (nodes[i] != null)
			{
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}
	
	/* split this quadTree into 4 sub-quadtrees */
	private void split()
	{
		int subW = (int) (bounds.getWidth() / 2);
		int subH = (int) (bounds.getHeight() / 2);
		int x = (int)bounds.getX();
		int y = (int)bounds.getY();
		
		nodes[0] = new QuadTree(level + 1, new Rectangle(x + subW, y, subW, subH));
		nodes[1] = new QuadTree(level + 1, new Rectangle(x, y, subW, subH));
		nodes[2] = new QuadTree(level + 1, new Rectangle(x, y + subH, subW, subH));
		nodes[3] = new QuadTree(level + 1, new Rectangle(x + subW, y + subH, subW, subH));
	}
	
	/* Gets the index for the node the object should belong to.
	 * If -1 then object will not be part of any child nodes and must
	 * fit into the parent node
	 */
	private int getIndex(RigidBody o)
	{
		int index = -1;
		double vMidPoint = bounds.getX() + (bounds.getWidth() / 2);
		double hMidPoint = bounds.getY() + (bounds.getHeight() / 2);
		Rectangle2D r = o.getGeometry().getBounds2D();
		
		// Check topQuadrants and bottomQuadrants
		boolean topQuad = (r.getY() < hMidPoint && r.getY() + r.getHeight() < hMidPoint);
		boolean bottomQuad = (r.getY() > hMidPoint);
		
		// Left quadrants
		if (r.getX() < vMidPoint && r.getX() + r.getWidth() < vMidPoint)
		{
			if (topQuad)
				index = 1;			// top left
			else if (bottomQuad)
				index = 2;			// bottom left
		}
		else if (r.getX() > vMidPoint)
		{
			if (topQuad)
				index = 0;			// top right
			else if (bottomQuad)
				index = 3;			//bottom right
		}
		
		return index;
	}
	
	/* Retrieve all objects that *might* collide with the rectangle given */
	public List retrieve(ArrayList<RigidBody> rBodies, RigidBody o)
	{
		if (nodes[0] != null)
		{
			int index = getIndex(o);
			if (index != -1)
				nodes[index].retrieve(rBodies, o);
		}
		
		rBodies.addAll(myBodies);
		return rBodies;
	}
	
	
	/* 
	 * Insert an object into the tree. If we exceed MAX_BODIES then
	 * split the node into subnodes
	 */
	public void insert(RigidBody o)
	{
		if (nodes[0] != null)
		{
			int index = getIndex(o);
			if (index != -1)
			{
				nodes[index].insert(o);
				return;
			}
		}
		
		myBodies.add(o);
		
		if (myBodies.size() > MAX_BODIES && level < MAX_LEVELS)
		{
			if (nodes[0] == null)
				split();
			
			int i = 0;
			while (i < myBodies.size())
			{
				int index = getIndex(myBodies.get(i));
				
				if (index != -1)
				{
					nodes[index].insert(myBodies.get(i));
					myBodies.remove(i);
				}
				else
					i++;
			}
		}
	}
}
