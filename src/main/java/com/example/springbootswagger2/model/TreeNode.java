package com.example.springbootswagger2.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.didion.jwnl.data.Synset;

@ApiModel("Model TreeNode")
public class TreeNode {


	@ApiModelProperty(value = "synset", position=1, required = true)
	long keysynset;


	@ApiModelProperty(value = "hijos", position = 2, required = true)
	ArrayList<Long> hijos;


	TreeNode(long keyset, ArrayList<Long> hijos){
		this.keysynset = keyset;
		this.hijos = hijos;
	}
	
	public long getKeysynset() {
		return keysynset;
	}

	public void setKeysynset(long keysynset) {
		this.keysynset = keysynset;
	}

	public ArrayList<Long> getHijos() {
		return hijos;
	}

	public void setHijos(ArrayList<Long> hijos) {
		this.hijos = hijos;
	}
		
}
