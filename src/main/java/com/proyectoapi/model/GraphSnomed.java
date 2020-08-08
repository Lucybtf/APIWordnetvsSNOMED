package com.proyectoapi.model;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Model Graph")
public class GraphSnomed {

	
	
	@ApiModelProperty(value = "nodes", required = true)
	ArrayList<GraphNode> nodes = new ArrayList<>();

	GraphSnomed(long key,int level){
	  GraphNode n = new GraphNode(key,level);
	  nodes.add(n);
	}
	public ArrayList<GraphNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<GraphNode> nodes) {
		this.nodes = nodes;
	}

	public int size() {
		return this.nodes.size();
	}
	/*
	public ArrayList<GraphNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<GraphNode> nodes) {
		this.nodes = nodes;
	}
	public void setNodeSn(GraphNode nodeSn) {
		this.nodes.add(nodeSn);
	}*/
}
