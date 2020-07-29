package com.example.springbootswagger2.model;




public class GraphNode {

	//@ApiModelProperty(value = "id", position=1, required = true)
	Long id;
//	@ApiModelProperty(value = "level", position=1, required = true)
	int level;
	
	public GraphNode(long key, int lev) {
		// TODO Auto-generated constructor stub
		this.id = new Long(key);
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
