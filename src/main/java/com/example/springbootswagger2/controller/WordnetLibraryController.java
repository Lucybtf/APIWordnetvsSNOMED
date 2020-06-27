package com.example.springbootswagger2.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.tree.TreeNode;
import javax.websocket.server.PathParam;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootswagger2.model.SynsetW;
import com.example.springbootswagger2.model.WordnetLibrary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jersey.repackaged.com.google.common.collect.Sets;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.util.TypeCheckingList;


@Api(value = "WordnetRestController", description = "REST Api Wordnet")
@RestController
public class WordnetLibraryController {

	private WordnetLibrary wl = new WordnetLibrary();
	
	/*@ApiOperation(value = "Get hola", tags = "getHola")
	@RequestMapping(value = "/Hola", method = RequestMethod.GET)
	public String getStudents() {
		return "hola";
	}*/
	
	/*getSynset: Por offset y por palabra y sense*/
	@ApiOperation(value = "Get Synset by Offset", tags = "getSynset")
	@RequestMapping(value = "/getSynset/{offset}", method = RequestMethod.GET, produces ="application/json")
	public String getSynset( @PathVariable("offset")long offset) throws JWNLException {
		JSONObject obj = new JSONObject();
		Synset a = wl.getSynset(offset);
		obj.append("synset",a);
		return  obj.toString();
	}
	
	@ApiOperation(value = "Get Synset by Word and sense", tags = "getSynset")
	@RequestMapping(value = "/getSynset/{word}/{sense}", method = RequestMethod.GET, produces ="application/json")
	public String getSynset( @PathVariable("word")String word,  @PathVariable("sense")int sense) throws JWNLException {
		JSONObject obj = new JSONObject();
		Synset a = wl.getSynset(word, POS.NOUN, sense);
		obj.append("synset",a);
		return  obj.toString();
	}
	
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@RequestMapping(value = "/getSense/{offset}", method = RequestMethod.GET)
	public String getSense(@PathVariable("offset")long offset) throws JWNLException {
		System.out.print(wl.getSense(offset));
		return  wl.getSense(offset);
	}
	
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@RequestMapping(value = "/getSense", method = RequestMethod.POST)
	public String getSense(@RequestBody Object s) {
		System.out.print(s);
		return "HOLA";	
	}
	
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@RequestMapping(value = "/getSense/{word}/{sense}", method = RequestMethod.GET)
	public String getSense(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		System.out.print(wl.getSense(word, POS.NOUN, sense));
		return  wl.getSense(word, POS.NOUN, sense);
	}
	
	@ApiOperation(value = "Get Synonyms", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonyms/{offset}", method = RequestMethod.GET)
	public String getSynonyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> synonyms = wl.getSynonyms(offset);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Synonyms", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonyms/{word}/{sense}", method = RequestMethod.GET)
	public String getSynonyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> synonyms = wl.getSynonyms(word, POS.NOUN, sense);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Synonyms Words", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonymsWords/{offset}", method = RequestMethod.GET)
	public String getSynonymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> synonyms = wl.getSynonymsWords(offset);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Synonyms Words", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonymsWords/{word}/{sense}", method = RequestMethod.GET)
	public String getSynonymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<String> synonyms = wl.getSynonymsWords(word, POS.NOUN, sense);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernyms/{offset}", method = RequestMethod.GET)
	public String getHypernyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHypernyms(offset);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernyms/{word}/{sense}", method = RequestMethod.GET)
	public String getHypernyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHypernyms(word, POS.NOUN, sense);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms Words", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsWors/{offset}", method = RequestMethod.GET)
	public String getHypernymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> hypernyms = wl.getHypernymsWords(offset);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms Words", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsWords/{word}/{sense}", method = RequestMethod.GET)
	public String getHypernymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<String> hypernyms = wl.getHypernymsWords(word, sense, POS.NOUN);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponyms/{offset}", method = RequestMethod.GET)
	public String getHyponyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHyponyms(offset);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponyms/{word}/{sense}", method = RequestMethod.GET)
	public String getHyponyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHyponyms(word, sense, POS.NOUN);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms Words", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsWors/{offset}", method = RequestMethod.GET)
	public String getHyponymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> hyponyms = wl.getHypernymsWords(offset);
		return hyponyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms Words", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsWords/{word}/{sense}", method = RequestMethod.GET)
	public String getHyponymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<String> hyponyms = wl.getHypernymsWords(word, sense, POS.NOUN);
		return hyponyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonyms/{offset}", method = RequestMethod.GET)
	public String getHolonyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> holonyms = wl.getHolonyms(offset);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonyms/{word}/{sense}", method = RequestMethod.GET)
	public String getHolonyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> holonyms = wl.getHolonyms(word, sense, POS.NOUN);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms Words", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonymsWords/{offset}", method = RequestMethod.GET)
	public String getHolonymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> holonyms = wl.getHolonymsWords(offset);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonymsWords/{word}/{sense}", method = RequestMethod.GET)
	public String getHolonymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> holonyms = wl.getHolonyms(word, sense, POS.NOUN);
		return holonyms.toString();
	}
	
	

}
