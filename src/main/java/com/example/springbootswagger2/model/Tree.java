package com.example.springbootswagger2.model;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//@ApiModel("Model Tree")
public class Tree {

	@ApiModelProperty(value = "nodes", required = true)
	ArrayList<TreeNode> nodes = new ArrayList<>();

	Tree(long keysynset, ArrayList<Long> hijos){
	//  System.out.print("HOLA"+ keysynset);
	// Long key = new Long(keysynset);
	  TreeNode n = new TreeNode(keysynset,hijos);
	  nodes.add(n);
	}
	

	public ArrayList<TreeNode> getNode() {
		return nodes;
	}


	public void setNode(ArrayList<TreeNode> nodes) {
		this.nodes = nodes;
	}
	
	public int size() {
		return this.size();
	}
	
}
