package com.example.springbootswagger2.model;
import java.util.ArrayList;

import org.apache.catalina.filters.AddDefaultCharsetFilter.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springbootswagger2.wordnetcontroller.WordnetLibraryController;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

@ApiModel("Model Synset")
public class SynsetW {
	
	@ApiModelProperty(value = "offset", required = true)
	long offset;
	@ApiModelProperty(value = "sense", required = true)
	String sense;
	

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getSense() {
		return sense;
	}

	public void setSense(String sense) {
		this.sense = sense;
	}

	
	public Synset getSynset() throws JWNLException {
		WordnetLibrary wl = new WordnetLibrary();
		return wl.getSynset(this.offset);
		
	}
	
	public ArrayList<Synset> getSynsetList(ArrayList<SynsetW> nodos) throws JWNLException {
		WordnetLibrary wl = new WordnetLibrary();
		ArrayList<Synset> synsets = new ArrayList<Synset>();
		for(int i =0; i<nodos.size();i++) {
			SynsetW n = nodos.get(i);
			synsets.add(n.getSynset());
		}
		return null;
		
	}
}
