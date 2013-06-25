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
	private static final int TOP_RIGHT_QUAD = 0;
	private static final int TOP_LEFT_QUAD = 1;
	private static final int BOTTOM_LEFT_QUAD = 2;
	private static final int BOTTOM_RIGHT_QUAD = 3;
	
	
	private static final int MAX_OBJECTS = 5;
	private static final int MAX_LEVELS = 10;
	
	
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
	
	
	/* Methods */
	
	/* Retrieve all objects that *might* collid with the rectangle given */
	public ArrayList<Object> retrieve(ArrayList<Object> rObjects, Object o)
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
	public int getIndex(Object o)
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
				index = TOP_LEFT_QUAD;
			else if (bottomQuad)
				index = BOTTOM_LEFT_QUAD;
		}
		else if (r.getX() > vMidPoint)
		{
			if (topQuad)
				index = TOP_RIGHT_QUAD;
			else if (bottomQuad)
				index = BOTTOM_RIGHT_QUAD;
		}
		
		// assign it to a thread or update thread if its different
		if (level == 0 && index != o.getQuad() && index != -1)
		{
			World.updateObjectThread(o, index);
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
