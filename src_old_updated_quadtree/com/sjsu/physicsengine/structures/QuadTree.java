package com.sjsu.physicsengine.structures;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.sjsu.physicsengine.RigidBody;

/* 
 * Quad tree, like a binary tree but with four node children
 * used to simplify collision detection
 */

// Check out https://github.com/mikechambers/ExamplesByMesh/blob/master/JavaScript/QuadTree/src/QuadTree.js

public class QuadTree 
{
	private int MAX_CHILDREN = 5;					// Number of children a node has before splitting
	private int MAX_DEPTH = 8;						// Number of levels a quadtree can go down
					
	private Node root;
	
	
	public QuadTree(Rectangle grid)
	{
		root = new Node(grid, 0, MAX_CHILDREN, MAX_DEPTH);
	}
	
	
	public Node getRoot()
	{
		return root;
	}
	
	public void insertBody(RigidBody b)
	{
		root.insert(b);
	}
	
	public void insertBodies(ArrayList<RigidBody> b)
	{
		for (int i = 0; i < b.size(); i++)
		{
			root.insert(b.get(i));
		}
	}
	
	public void clearBodies()
	{
		root.clear();
	}
	
	/* 
	 * Retrieves all objects in the same node as the parameter b (ie all
	 * rigidbodies that are geographically near the parameter).
	 */
	public ArrayList<RigidBody> retrieveNeighbors(RigidBody b)
	{
		return root.retrieveNode(b);
	}
}
