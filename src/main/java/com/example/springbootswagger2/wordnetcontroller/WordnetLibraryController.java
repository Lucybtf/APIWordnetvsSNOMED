package com.example.springbootswagger2.wordnetcontroller;

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

import javax.activation.MimeType;
import javax.swing.tree.TreeNode;
import javax.websocket.server.PathParam;
import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootswagger2.model.SynsetW;
import com.example.springbootswagger2.model.WordnetLibrary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
	@RequestMapping(value = "/getSynset/{offset}", method = RequestMethod.GET)
	public String getSynset(  @ApiParam(value = "Event type", required = true, example = "sent") @PathVariable("offset")long offset) throws JWNLException {
		Synset a = wl.getSynset(offset);
		return  a.toString();
	}
	
	@ApiOperation(value = "Get Synset by Word and sense", tags = "getSynset")
	@RequestMapping(value = "/getSynset/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSynset( @PathVariable("word")String word,  @PathVariable("sense")int sense) throws JWNLException {
		JSONObject obj = new JSONObject();
		Synset a = wl.getSynset(word, POS.NOUN, sense);
		obj.append("synset",a);
		return  obj.toString();
	}
	
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@RequestMapping(value = "/getSense/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSense(@PathVariable("offset")long offset) throws JWNLException {
		System.out.print(wl.getSense(offset));
		return  wl.getSense(offset);
	}
	
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@RequestMapping(value = "/getSense/{synset}", method = RequestMethod.GET,produces= { "text/plain" })
	public String getSense(@ApiParam(value = "Description of synset", example="{\"lemma\":\"feline mammal usually having thick soft fur and no ability to roar: domestic cats; wildcats\"\r\n}" ) @RequestParam("synset")String jsonString) throws JWNLException  {
		//String input = (String) inputJsonObj.get("input");
		SynsetW inputPojo  = null;
		System.out.print("CADENA"+jsonString);
		 try {
			inputPojo = new ObjectMapper().readValue(jsonString, SynsetW.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			System.out.print("MApping Error");
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			System.out.print("JSON processing");
			e.printStackTrace();
		}

	    return inputPojo.getLemma();
	}
	
	/*@POST
	  @Produces(MediaType.APPLICATION_JSON)
	  @Consumes(MediaType.APPLICATION_JSON)
	  public JSONObject sayPlainTextHello(JSONObject inputJsonObj) throws Exception {

	    String input = (String) inputJsonObj.get("input");
	    String output = "The input you sent is :" + input;
	    JSONObject outputJsonObj = new JSONObject();
	    outputJsonObj.put("output", output);

	    return outputJsonObj;
	  }
	*/
	
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@RequestMapping(value = "/getSense/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSense(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		System.out.print(wl.getSense(word, POS.NOUN, sense));
		return  wl.getSense(word, POS.NOUN, sense);
	}
	
	@ApiOperation(value = "Get Synonyms", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonyms/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSynonyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> synonyms = wl.getSynonyms(offset);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Synonyms", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonyms/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSynonyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> synonyms = wl.getSynonyms(word, POS.NOUN, sense);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Synonyms Words", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonymsWords/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSynonymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> synonyms = wl.getSynonymsWords(offset);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Synonyms Words", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonymsWords/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getSynonymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<String> synonyms = wl.getSynonymsWords(word, POS.NOUN, sense);
		return synonyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernyms/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHypernyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHypernyms(offset);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernyms/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHypernyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHypernyms(word, POS.NOUN, sense);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms Words", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsWors/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHypernymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> hypernyms = wl.getHypernymsWords(offset);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hypernyms Words", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsWords/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHypernymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<String> hypernyms = wl.getHypernymsWords(word, sense, POS.NOUN);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponyms/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHyponyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHyponyms(offset);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponyms/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHyponyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> hypernyms = wl.getHyponyms(word, sense, POS.NOUN);
		return hypernyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms Words", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsWors/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHyponymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> hyponyms = wl.getHypernymsWords(offset);
		return hyponyms.toString();
	}
	
	@ApiOperation(value = "Get Hyponyms Words", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsWords/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHyponymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<String> hyponyms = wl.getHypernymsWords(word, sense, POS.NOUN);
		return hyponyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonyms/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHolonyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> holonyms = wl.getHolonyms(offset);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonyms/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHolonyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> holonyms = wl.getHolonyms(word, sense, POS.NOUN);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms Words", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonymsWords/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHolonymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> holonyms = wl.getHolonymsWords(offset);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Holonyms Words", tags = "getHolonyms")
	@RequestMapping(value = "/getHolonymsWords/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getHolonymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> holonyms = wl.getHolonyms(word, sense, POS.NOUN);
		return holonyms.toString();
	}
	
	@ApiOperation(value = "Get Meronyms", tags = "getMeronyms")
	@RequestMapping(value = "/getMeronyms/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getMeronyms(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<Synset> meronyms = wl.getMeronyms(offset);
		return meronyms.toString();
	}
	
	@ApiOperation(value = "Get Meronyms", tags = "getMeronyms")
	@RequestMapping(value = "/getMeronyms/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getMeronyms(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> meronyms = wl.getMeronyms(word, sense, POS.NOUN);
		return meronyms.toString();
	}
	@ApiOperation(value = "Get Meronyms Words", tags = "getMeronyms")
	@RequestMapping(value = "/getMeronymsWords/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getMeronymsWords(@PathVariable("offset")long offset) throws JWNLException {
		ArrayList<String> meronyms = wl.getHolonymsWords(offset);
		return meronyms.toString();
	}
	
	@ApiOperation(value = "Get Meronyms Words", tags = "getMeronyms")
	@RequestMapping(value = "/getMeronymsWords/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getMeronymsWords(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		ArrayList<Synset> meronyms = wl.getHolonyms(word, sense, POS.NOUN);
		return meronyms.toString();
	}
	
	
	@ApiOperation(value = "Check if the entity node", tags = "checkIfEntityNode")
	@RequestMapping(value = "/isEntityNode", method = RequestMethod.GET, produces= { "text/plain" })
	public void isEntityNode() {
	
	}
	
	@ApiOperation(value = "Get List of Synsets until entity", tags = "getPathToEntity")
	@RequestMapping(value = "/getPathToEntity", method = RequestMethod.GET, produces= { "text/plain" })
	public void getPathToEntity() {
	
	}
	
	@ApiOperation(value = "Get number of nodes until entity", tags = "getNodesToEntity")
	@RequestMapping(value = "/getNodesToEntity", method = RequestMethod.GET, produces= { "text/plain" })
	public void getNodesToEntity() {
	
	}
	
	
	@ApiOperation(value = "Get path between two synsets", tags = "getPathBetweenSynsets")
	@RequestMapping(value = "/getPathBetweenSynsets", method = RequestMethod.GET, produces= { "text/plain" })
	public void getPathBetweenSynsets() {
	
	}
	
	
	
	@ApiOperation(value = "Get number of links between two synsets", tags = "getPathBetweenSynsets")
	@RequestMapping(value = "/getNumLinksBetweenSynsets", method = RequestMethod.GET, produces= { "text/plain" })
	public void getNumLinksBetweenSynsets() {
	
	}
	
	@ApiOperation(value = "Get depth of a synset", tags = "getdepthOfSynset")
	@RequestMapping(value = "/getdepthOfSynset", method = RequestMethod.GET, produces= { "text/plain" })
	public void getdepthOfSynset() {
	
	}
	
	@ApiOperation(value = "Get depth of a synset in a subtree", tags = "getdepthOfSynset")
	@RequestMapping(value = "/getdepthOfSynsetinSubtree", method = RequestMethod.GET, produces= { "text/plain" })
	public void getdepthOfSynsetinSubtree() {
	
	}

	@ApiOperation(value = "Get Least Common Subsummer", tags = "getLCS")
	@RequestMapping(value = "/getLCS", method = RequestMethod.GET, produces= { "text/plain" })
	public void getLCS() {
	
	}

	/*	getLemma by offset, synset 
	 * getLemmas by ArrayList Synset
	 * getNumberSenses
	 * getListofWords
	 * NOt ContainsFirstInSecond
	 * Interesection
	 * getHypernymTreeList(Synset)
	 * getHyponymsTreeList(Synset)
	 */
	
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@RequestMapping(value = "/getSubtree", method = RequestMethod.GET, produces= { "text/plain" })
	public void getSubtree() {
	
	}
	

}
