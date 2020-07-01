package com.example.springbootswagger2.model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springbootswagger2.wordnetcontroller.WordnetLibraryController;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;


public class SynsetW {
	
	Long offset;
	String Lemma;
	
	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getLemma() {
		return Lemma;
	}

	public void setLemma(String lemma) {
		Lemma = lemma;
	}
	
	public Synset getSynset() throws JWNLException {
		WordnetLibrary wl = new WordnetLibrary();
		return wl.getSynset(offset);
		
	}
}
