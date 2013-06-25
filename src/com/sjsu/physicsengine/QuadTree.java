package com.sjsu.physicsengine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/* 
 * Quad tree, like a binary tree but with four node children
 * used to simplify collision detection
 */
public class QuadTree 
{
	private int MAX_OBJECTS = 10;
	private int MAX_LEVELS = 10;
	
	private int level;
	private ArrayList<Object> objects;
	private Rectangle bounds;
	private QuadTree[] nodes;
	
	
	public QuadTree(int l, Rectangle b)
	{
		level = l;
		objects = new ArrayList<Object>();
		bounds = b;
		nodes = new QuadTree[4];
	}
	
	
	
	/* Retrieve all objects that *might* collide with the rectangle given */
	public List retrieve(ArrayList<Object> rObjects, Object o)
	{
		if (nodes[0] != null)
		{
			int index = getIndex(o);
			if (index != -1)
				nodes[index].retrieve(rObjects, o);
		}
		
		rObjects.addAll(objects);
		return rObjects;
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
	
	/* clear the entire tree */
	public void clear()
	{
		// let the garbage collector clean up
		if (level == 0)
		{
			objects = new ArrayList<Object>();
			nodes = new QuadTree[4];
		}
		
		// below code is clear if we dont want garbage collector
		// doing all the heavy lifting
//		objects.clear();
//		
//		for (int i = 0; i < nodes.length; i++)
//		{
//			if (nodes[i] != null)
//			{
//				nodes[i].clear();
//				nodes[i] = null;
//			}
//		}
	}
	
	/* Gets the index for the node the object should belong to.
	 * If -1 then object will not be part of any child nodes and must
	 * fit into the parent node
	 */
	private int getIndex(Object o)
	{
		int index = -1;
		double vMidPoint = bounds.getX() + (bounds.getWidth() / 2);
		double hMidPoint = bounds.getY() + (bounds.getHeight() / 2);
		Rectangle r = o.getGeometry().getBounds();
		
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
	
	
	/* 
	 * Insert an object into the tree. If we exceed MAX_OBJECTS then
	 * split the node into subnodes
	 */
	public void insert(Object o)
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
		
		objects.add(o);
		
		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS)
		{
			if (nodes[0] == null)
				split();
			
			int i = 0;
			while (i < objects.size())
			{
				int index = getIndex(objects.get(i));
				
				if (index != -1)
				{
					nodes[index].insert(objects.get(i));
					objects.remove(i);
				}
				else
					i++;
			}
		}
	}
}
