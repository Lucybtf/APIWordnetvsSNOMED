package com.proyectoapi.model;

import java.util.ArrayList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Model TreeNode")
public class TreeNode {

	@ApiModelProperty(value = "synset", position = 1, required = true)
	long keysynset;

	@ApiModelProperty(value = "hijos", position = 2, required = true)
	ArrayList<Long> hijos;

	TreeNode(long keyset, ArrayList<Long> hijos) {
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
