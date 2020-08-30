package com.proyectoapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Class NodeSnomedCT: Esta clase representa los nodos del subárbol para Snomed CT, se emplea en la clase TreeSnomedCT para representar el subárbo
 * @author Lucía Batista Flores
 * @version 1.0
 */

@ApiModel("Model NodeSnomedCT")
public class NodeSnomedCT {

	@ApiModelProperty(value = "id", position=1, required = true)
	Long id;
	@ApiModelProperty(value = "level", position=1, required = true)
	int level;

	public NodeSnomedCT(long key, int lev) {
		this.id = Long.valueOf(key);
		this.level = lev;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
