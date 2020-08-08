package com.proyectoapi.controllerwordnet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoapi.model.SynsetW;
import com.proyectoapi.model.Tree;
import com.proyectoapi.model.WordnetLibrary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
import springfox.documentation.annotations.ApiIgnore;


/*	getLemma by offset, synset 
 * getLemmas by ArrayList Synset
 * getNumberSenses
 * getListofWords
 * NOt ContainsFirstInSecond
 * Interesection
 * getHypernymTreeList(Synset)
 * getHyponymsTreeList(Synset)
 */


@Api(value = "WordnetRestController", description = "REST Api Wordnet")
@RestController
public class WordnetLibraryController {

	private WordnetLibrary wl = new WordnetLibrary();
	
	/*getSynset: Por offset y por palabra y sense*/
	@ApiOperation(value = "Get Synset by Offset", tags = "getSynset")
	@RequestMapping(value = "/getSynset/{offset}", method = RequestMethod.GET)
	public String getSynset(  @ApiParam(value = "Offset", required = true, example = "4582285") @PathVariable("offset")long offset) throws JWNLException {
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
		return  wl.getSense(offset);
	}


	
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
	@RequestMapping(value = "/isEntityNode/{offset}", method = RequestMethod.GET, produces={"application/json"})
	@ResponseBody
	public boolean isEntityNode(@PathVariable("offset")long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		return wl.isEntityNode(s);
	}
	
	@ApiOperation(value = "Check if the entity node", tags = "checkIfEntityNode")
	@RequestMapping(value = "/isEntityNode/{word}/{sense}", method = RequestMethod.GET, produces={"application/json"})
	@ResponseBody
	public boolean isEntityNode(@PathVariable("word")String word, @PathVariable("sense")int sense) {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		return wl.isEntityNode(s);
	}
	
	@ApiOperation(value = "Get List of Synsets until entity", tags = "getPathToEntity")
	@RequestMapping(value = "/getPathToEntity/{offset}", method = RequestMethod.GET, produces={"application/json"})
	public String getPathToEntity(@PathVariable("offset")long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		ArrayList<String> synsets =wl.getListofSynsetsToString( wl.getPathToEntity(s));
		JSONArray jsArray = new JSONArray(synsets);
		System.out.print(jsArray);
		return jsArray.toString();
	}
	
	@ApiOperation(value = "Get List of Synsets until entity", tags = "getPathToEntity")
	@RequestMapping(value = "/getPathToEntity/{word}/{sense}", method = RequestMethod.GET, produces={"application/json"})
	public String getPathToEntity(@PathVariable("word")String word, @PathVariable("sense")int sense) {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		ArrayList<String> synsets =wl.getListofSynsetsToString( wl.getPathToEntity(s));
		JSONArray jsArray = new JSONArray(synsets);
		System.out.print(jsArray);
		return jsArray.toString();
	}
	
	@ApiOperation(value = "Get number of nodes until entity", tags = "getNodesToEntity")
	@RequestMapping(value = "/getNodesToEntity/{offset}", method = RequestMethod.GET, produces={"application/json"})
	@ResponseBody
	public int getNodesToEntity(@PathVariable("offset")long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		System.out.print("NODES"+wl.getNodesToEntity(s));
		return wl.getNodesToEntity(s);
	}
	
	@ApiOperation(value = "Get number of nodes until entity", tags = "getNodesToEntity")
	@RequestMapping(value = "/getNodesToEntity/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getNodesToEntity(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		System.out.print("NODES"+wl.getNodesToEntity(s));
		return Integer.toString(wl.getNodesToEntity(s));
	}
	
	@ApiOperation(value = "Get path between two synsets", tags = "getPathBetweenSynsets")
	@RequestMapping(value = "/getPathBetweenSynsets/{offset}", method = RequestMethod.GET, produces={"application/json"})
	public String getPathBetweenSynsets(@PathVariable("offset")long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		ArrayList<String> synsets =wl.getListofSynsetsToString( wl.getPathToEntity(s));
		JSONArray jsArray = new JSONArray(synsets);
		System.out.print(jsArray);
		return jsArray.toString();
	}
	
	@ApiOperation(value = "Get path between two synsets", tags = "getPathBetweenSynsets")
	@RequestMapping(value = "/getPathBetweenSynsets/{word}/{sense}", method = RequestMethod.GET, produces={"application/json"})
	public String getPathBetweenSynsets(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		ArrayList<String> synsets =wl.getListofSynsetsToString( wl.getPathToEntity(s));
		JSONArray jsArray = new JSONArray(synsets);
		System.out.print(jsArray);
		return jsArray.toString();
	}
	
	@ApiOperation(value = "Get number of links between two synsets", tags = "getPathBetweenSynsets")
	@RequestMapping(value = "/getNumLinksBetweenSynsets/{offset}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getNumLinksBetweenSynsets(@PathVariable("offset")long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		System.out.print("NODES"+wl.getNodesToEntity(s));
		return Integer.toString(wl.getNodesToEntity(s));
	}
	
	
	@ApiOperation(value = "Get number of links between two synsets", tags = "getPathBetweenSynsets")
	@RequestMapping(value = "/getNumLinksBetweenSynsets/{word}/{sense}", method = RequestMethod.GET, produces= { "text/plain" })
	public String getNumLinksBetweenSynsets(@PathVariable("word")String word, @PathVariable("sense")int sense) {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		System.out.print("NODES"+wl.getNodesToEntity(s));
		return Integer.toString(wl.getNodesToEntity(s));
	}
	
	@ApiOperation(value = "Get depth of a synset", tags = "getdepthOfSynset")
	@RequestMapping(value = "/getdepthOfSynset/{offset}", method = RequestMethod.GET, produces={"application/json"})
	@ResponseBody
	public int getdepthOfSynset(@PathVariable("offset")long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		System.out.print("NODES"+wl.depthOfSynset(s));
		return wl.depthOfSynset(s);
	}
	
	
	@ApiOperation(value = "Get depth of a synset", tags = "getdepthOfSynset")
	@RequestMapping(value = "/getdepthOfSynset/{word}/{sense}", method = RequestMethod.GET, produces={"application/json"})
	@ResponseBody
	public int getdepthOfSynset(@PathVariable("word")String word, @PathVariable("sense")int sense) throws JWNLException {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		System.out.print("NODES"+wl.depthOfSynset(s));
		return wl.depthOfSynset(s);
	}


	@ApiOperation(value = "Get Least Common Subsummer", tags = "getLCS")
	@RequestMapping(value = "/getLCS/{offset1}/{offset2}", method = RequestMethod.GET, produces={"application/json"})
	public String getLCS(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2) throws JWNLException {
		JSONObject json = new JSONObject();
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		
		Synset lcs = wl.getLeastCommonSubsumer(s1, s2);
		System.out.print("LCS"+lcs.toString());
		json.put("synset", lcs);
		return json.toString();
	}
	
	
	@ApiOperation(value = "Get Least Common Subsummer", tags = "getLCS")
	@RequestMapping(value = "/getLCS/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET, produces={"application/json"})
	public String getLCS(@PathVariable("word1")String word1, @PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2")int sense2) throws JWNLException {
		JSONObject json = new JSONObject();
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		
		Synset lcs = wl.getLeastCommonSubsumer(s1, s2);
		System.out.print("LCS"+lcs.toString());
		json.put("synset", lcs);
		return json.toString();
	}

	
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@RequestMapping(value = "/getSubtreeByOffset/{offset}/{depth}", method = RequestMethod.POST, produces={"application/json"})
	public String getSubtreeByOffset(@PathVariable("offset")long offset, @PathVariable("depth") int depth) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset);
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
		tree = wl.getSubarbolWordnet(s1, depth, tree);
		JSONArray jsonarray = new JSONArray();

		
		for(Synset s: tree.keySet()) {
			
			ArrayList<Synset> hijos = tree.get(s);
			JSONObject jsonnode = new JSONObject();
			JSONArray hijosjson = new JSONArray();
			for(int i=0; i<hijos.size();i++) {
				hijosjson.put(hijos.get(i).getOffset());
				System.out.print("s"+s.getWord(0)+"hijos"+hijos.get(i)+ "\n");
			}

			jsonnode.put("keysynset", s.getOffset());
			jsonnode.put("hijos", hijosjson);
			jsonarray.put(jsonnode);

		}
	    return jsonarray.toString();
	}
	
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@RequestMapping(value = "/getSubtree/{offset}/{depth}", method = RequestMethod.POST, produces={"application/json"})
	public String getSubtree(@PathVariable("offset")long offset, @PathVariable("depth") int depth) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset);
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
		tree = wl.getSubarbolWordnet(s1, depth, tree);
		JSONArray jsonarray = new JSONArray();
		JSONObject json = new JSONObject();
		for(Synset s: tree.keySet()) {
			JSONObject synsetpair = new JSONObject();
			ArrayList<Synset> hijos = tree.get(s);
			JSONObject jsonhijos = new JSONObject();
			
			for(int i=0; i<hijos.size();i++) {
				jsonhijos.put(Integer.toString(i), hijos.get(i));
			}
			System.out.print(jsonhijos);
			synsetpair.put(s.toString(), jsonhijos);
			jsonarray.put(synsetpair);

		}
	    return jsonarray.toString();
	}
	
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@RequestMapping(value = "/getSubtree/{word}/{sense}/{depth}", method = RequestMethod.POST, produces={"application/json"})
	public String getSubtree(@PathVariable("word")String word, @PathVariable("sense")int sense, @PathVariable("depth") int depth) throws JWNLException, IOException {
		long offset = wl.getSynset(word, POS.NOUN, sense).getOffset();
	    return getSubtree(offset, depth);
	}
	

	@ApiOperation(value = "Get depth of a synset in a subtree", tags = "getdepthOfSynset")
	@RequestMapping(value = "/getdepthOfSynsetinSubtree/{offset}", method = RequestMethod.POST, consumes={"application/json"})
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public int getdepthOfSynsetinSubtree(@PathVariable("offset")Long offset, @RequestBody Tree treeResult) throws JWNLException {
	
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
	
		tree = wl.convertTreeToMap(treeResult);
		Synset n = wl.getSynset(offset.longValue());
	
		return wl.depthOfSynset(n, tree);
	}

}
