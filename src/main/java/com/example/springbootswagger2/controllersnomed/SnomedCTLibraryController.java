package com.example.springbootswagger2.controllersnomed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootswagger2.model.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Api(value = "SnomedRestController", description = "REST Api SnomedCT")
@RestController
public class SnomedCTLibraryController {

	private SnomedCTLibrary snomedc = new SnomedCTLibrary();
	
	@ApiOperation(value = "Get Concept by Id", tags = "getConcept")
	@RequestMapping(value = "/getConcept/{id}", method = RequestMethod.GET)
	public String getConcept(@PathVariable("id")long idConcept) {
		return snomedc.getConcept(idConcept);
	}
	
	@ApiOperation(value = "Get Synonyms Strings by Id", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonymsConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<String> getSynonymsConcepts(@PathVariable("id")long idConcept) throws Exception {
		return snomedc.getSynonymsConcepts(idConcept);
	}
	
	@ApiOperation(value = "Get Synonyms Ids by Id", tags = "getSynonyms")
	@RequestMapping(value = "/getSynonymsIdConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<Long> getSynonymsIdConcepts(@PathVariable("id")long idConcept) throws Exception{
		return snomedc.getSynonymsIdConcepts(idConcept);
	}
	
	@ApiOperation(value = "Get Hypernyms Strings by Id", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<String> getHypernymsConcepts(@PathVariable("id")long idConcept){
		return snomedc.getHypernymsConcepts(idConcept);
	}
	
	@ApiOperation(value = "Get Hypernyms Ids by Id", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsIdConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<Long> getHypernymsIdConcepts(@PathVariable("id")long idConcept){
		return snomedc.getHypernymsIdConcepts(idConcept);
	}
	
	@ApiOperation(value = "Get Hyponyms Strings by Id", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<String> gettHyponymsConcept(@PathVariable("id")long idConcept) throws Exception {
		return snomedc.getHyponymsConcepts(idConcept);
	}

	@ApiOperation(value = "Get Hyponyms Ids by Id", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsIdConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<Long> gettHyponymsIdConcept(@PathVariable("id")long idConcept) throws Exception {
		return snomedc.getHyponymsIdConcepts(idConcept);
		
	}
	
	@ApiOperation(value = "Get Path to Entity Concept", tags = "getPathToEntity")
	@RequestMapping(value = "/getPathEntityConcept/{id}", method = RequestMethod.GET)
	public ArrayList<Long> getPathEntityConcept(@PathVariable("id")long idConcept){
		return snomedc.getPathEntityConcept(idConcept);
	}
	
	@ApiOperation(value = "Get Ancestros Distancia Minima Concept", tags = "getAncestrosDistanciaMinima")
	@RequestMapping(value = "/getAncestrosDistanciaMinima/{id}", method = RequestMethod.GET)
	public HashMap<Long, Integer> getAncestrosDistanciaMinima(long idConcept){
		return snomedc.getAncestrosDistanciaMinima(idConcept);
	}
	
	@ApiOperation(value = "Get Num Links Between Concepts", tags = "getNumLinksBetweenConcepts")
	@RequestMapping(value = "/getNumLinksBetweenConcepts/{id}/{id2}", method = RequestMethod.GET)
	public int getNumLinksBetweenConcepts(@PathVariable("id")long idConcept1, @PathVariable("id2")long idConcept2) {
		return snomedc.getNumLinksBetweenConceptos(idConcept1, idConcept2);
	}
	
	@ApiOperation(value = "Get Ancestors of a Concept", tags = "getAncestors")
	@RequestMapping(value = "/getAncestors/{id}", method = RequestMethod.GET)
	public ArrayList<Long> getAncestors(long idConcept){
		return snomedc.getAncestors(idConcept);
	}
	
	@ApiOperation(value = "Get Descendants of a Concept", tags = "getDescendants")
	@RequestMapping(value = "/getDescendants/{id}", method = RequestMethod.GET)
	public ArrayList<Long> getDescendants(long idConcept){
		return snomedc.getDescendants(idConcept);
	}
	
	@ApiOperation(value = "Get LCS Concept", tags = "getLCSConcept")
	@RequestMapping(value = "/getLCSConcept/{id}/{id2}", method = RequestMethod.GET)
	public Long getLCS(long idConcept1, long idConcept2) {
		return snomedc.getLCS(idConcept1, idConcept2);		
	}
	
	@ApiOperation(value = "Get SubGraph of Snomed CT", tags = "getSubGraph")
	@RequestMapping(value = "/getSubGraph/{idroot}/{level}", method = RequestMethod.GET)
	public String getSubGraph(long idConceptroot, int level){

		Map<Long, Integer> graph = new HashMap <Long, Integer>();
		graph =snomedc.getSubGraph(idConceptroot, level);
		System.out.print("\n\nGRAFO"+graph+ "JSON"+snomedc.getSubGraphJSON(graph));
		return snomedc.getSubGraphJSON(graph).toString();
	}
}
