package com.sjsu.physicsengine.structures;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.sjsu.physicsengine.RigidBody;

/* A node of a quad-tree */

public class Node 
{	
	private Rectangle bounds;
	private int depth;
	private int maxChildren;
	private int maxDepth;
	
	private static final int TOP_LEFT = 0;
	private static final int TOP_RIGHT = 1;
	private static final int BOTTOM_LEFT = 2;
	private static final int BOTTOM_RIGHT = 3;
	
	private ArrayList<RigidBody> myBodies;			// Bodies that belong to this node
	private Node[] subNodes;						// Children nodes of this node
	private int numSubNodes = 0;					// Number of subnodes we currently have
	
	public Node()
	{
		// Defaults
		bounds = new Rectangle(0, 0);
		depth = 0;
		maxChildren = 5;
		maxDepth = 5;
		
		myBodies = new ArrayList<RigidBody>();
		subNodes = new Node[4];
	}
	
	public Node(Rectangle grid, int d, int mC, int mD)
	{
		bounds = grid;
		depth = d;
		maxChildren = mC;
		maxDepth = mD;
		
		myBodies = new ArrayList<RigidBody>();
		subNodes = new Node[4];
	}
	
	/* Insert a body to our node. If it's full split our node */
	public void insert(RigidBody body)
	{
		/* If we have subnodes, find the index our body belongs to and insert it there */
		if (numSubNodes > 0)
		{
			int index = findIndex(body);
			subNodes[index].insert(body);
			return;
		}
		
		/* If we are at the base node (no subnodes) then we insert here */
		myBodies.add(body);
		int curCharCount = myBodies.size();
		
		/* Check to see if we have too many children at this node, split if we are allowed to */
		if ( (depth <= maxDepth) && (curCharCount > maxChildren) )
		{
			split();
			
			for (int i = 0; i < curCharCount; i++)
			{
				insert(myBodies.get(i));
			}
			
			myBodies.clear();
		}
	}
	
	/* Retrieve the node that the rigidBody belongs to. If not this node, search myBodies */
	public ArrayList<RigidBody> retrieveNode(RigidBody body)
	{
		// If we have subnodes, find the index
		if (numSubNodes > 0)
		{
			int index = findIndex(body);
			return subNodes[index].retrieveNode(body);
		}
		
		// if we are at the base node already, return this node's children
		return myBodies;
	}
	
	/* Finds the index that a body belongs to */
	public int findIndex(RigidBody body)
	{
		int index;
		boolean left = (body.getCenter().x > (bounds.x + bounds.width / 2)) ? false : true;
		boolean top = (body.getCenter().y > (bounds.y + bounds.height / 2)) ? false : true;
		
		if (left)
		{
			if (top)
				index = TOP_LEFT;
			else
				index = BOTTOM_LEFT;
		}
		else
		{
			if (top)
				index = TOP_RIGHT;
			else
				index = BOTTOM_RIGHT;
		}
		
		return index;
	}
	
	/* Subdivide a node into four subnodes */
	public void split()
	{
		int newDepth = depth + 1;
		int bX = bounds.x;
		int bY = bounds.y;
		
		int bwh = (int) Math.floor((bounds.width / 2));
		int bhh = (int) Math.floor((bounds.height / 2));
		int bXbwh = bX + bwh;
		int bYbhh = bY + bhh;
		
		/* Rect's for new node bounds */
		Rectangle topLeftBounds = new Rectangle(bX, bY, bwh, bhh);
		Rectangle topRightBounds = new Rectangle(bXbwh, bY, bwh, bhh);
		Rectangle bottomLeftBounds = new Rectangle(bX, bYbhh, bwh, bhh);
		Rectangle bottomRightBounds = new Rectangle(bXbwh, bYbhh, bwh, bhh);
		
		/* Create the new Nodes */
		subNodes[TOP_LEFT] = new Node(topLeftBounds, newDepth, maxChildren, maxDepth);
		subNodes[TOP_RIGHT] = new Node(topRightBounds, newDepth, maxChildren, maxDepth);
		subNodes[BOTTOM_RIGHT] = new Node(bottomRightBounds, newDepth, maxChildren, maxDepth);
		subNodes[BOTTOM_LEFT] = new Node(bottomLeftBounds, newDepth, maxChildren, maxDepth);
		
		numSubNodes = 4;
	}
	
	/* Clear this node and all subNodes */
	public void clear()
	{
		myBodies.clear();
		
		/* Clear all subnodes */
		for (int i = 0; i < 4; i++)
		{
			if (subNodes[i] != null)
				subNodes[i].clear();
			subNodes[i] = null;
		}
		numSubNodes = 0;
	}
}
