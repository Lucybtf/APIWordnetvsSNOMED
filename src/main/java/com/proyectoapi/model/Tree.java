package com.proyectoapi.model;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Model Tree")
public class Tree {

	@ApiModelProperty(value = "nodes", required = true)
	ArrayList<TreeNode> nodes = new ArrayList<>();

	Tree(long keysynset, ArrayList<Long> hijos){
	  TreeNode n = new TreeNode(keysynset,hijos);
	  nodes.add(n);
	}
	

	public ArrayList<TreeNode> getNodes() {
		return nodes;
	}


	public void setNodes(ArrayList<TreeNode> nodes) {
		this.nodes = nodes;
	}
	
	public int size() {
		return this.size();
	}
	
}
