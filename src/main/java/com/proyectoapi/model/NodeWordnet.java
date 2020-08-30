package com.proyectoapi.model;

import java.util.ArrayList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Class NodeSnomedCT: Esta clase representa los nodos del subárbol para Snomed CT, se emplea en la clase TreeWordnet para representar el subárbol
 * @author Lucía Batista Flores
 * @version 1.0
 */

@ApiModel("Model NodeWordnet")
public class NodeWordnet {

	@ApiModelProperty(value = "synset", position = 1, required = true)
	long keysynset;

	@ApiModelProperty(value = "hijos", position = 2, required = true)
	ArrayList<Long> hijos;

	NodeWordnet(long keyset, ArrayList<Long> hijos) {
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
