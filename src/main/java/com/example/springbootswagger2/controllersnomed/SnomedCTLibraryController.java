package com.example.springbootswagger2.controllersnomed;

import java.util.ArrayList;

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
	public ArrayList<String> getHypernymsConcepts(long idConcept){
		return snomedc.getHypernymsConcepts(idConcept);
	}
	
	@ApiOperation(value = "Get Hypernyms Ids by Id", tags = "getHypernyms")
	@RequestMapping(value = "/getHypernymsIdConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<Long> getHypernymsIdConcepts(long idConcept){
		return snomedc.getHypernymsIdConcepts(idConcept);
	}
	
	@ApiOperation(value = "Get Hyponyms Strings by Id", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<String> gettHyponymsConcept(long idConcept) throws Exception {
		return snomedc.getHyponymsConcepts(idConcept);
	}

	@ApiOperation(value = "Get Hyponyms Ids by Id", tags = "getHyponyms")
	@RequestMapping(value = "/getHyponymsConcepts/{id}", method = RequestMethod.GET)
	public ArrayList<Long> gettHyponymsIdConcept(long idConcept) throws Exception {
		return snomedc.getHyponymsIdConcepts(idConcept);
		
	}
}
