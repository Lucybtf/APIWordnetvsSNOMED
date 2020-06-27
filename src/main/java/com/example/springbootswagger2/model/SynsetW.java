package com.example.springbootswagger2.model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springbootswagger2.controller.WordnetLibraryController;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

public class SynsetW {

	Synset s;

	public String toString() {
		return s.toString();
	}
}
