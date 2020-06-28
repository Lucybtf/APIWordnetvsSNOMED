package com.example.springbootswagger2.distancecontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "DistanceRestController", description = "REST Api Wordnet")
@RestController
public class DistanceController {

	/*@ApiOperation(value = "Get WP Similarity", tags = "getWPSimilarity")
	@RequestMapping(value = "/getWPSimilarity", method = RequestMethod.GET)
	public String getWPSimilarity() {
		return "hola";
	}
	
	@ApiOperation(value = "Get WP Subtree Similarity", tags = "getWuandPamer")
	@RequestMapping(value = "/getWPSubtreeSimilarity", method = RequestMethod.GET)
	public String getWPSubTreeSimilarity() {
		return "hola";
	}*/
	
	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistance", method = RequestMethod.GET)
	public String getWPDistance() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Wu and Palmer distance in a Subtree of Wordnet", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistanceSubTree", method = RequestMethod.GET)
	public String getWPDistanceSubTree() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "getSanchezDistance")
	@RequestMapping(value = "/getSanchezDistance", method = RequestMethod.GET)
	public String getSanchezDistance() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Sanchez distance in a Subtree of Wordnet", tags = "getSanchezDistance")
	@RequestMapping(value = "/getSanchezDistanceSubTree", method = RequestMethod.GET)
	public String getSanchezDistanceSubTree() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getSanchezIC", method = RequestMethod.GET)
	public String getSanchezIC() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Resnisk Distance", tags = "getResniskDistance")
	@RequestMapping(value = "/getResniskDistance", method = RequestMethod.GET)
	public String getResniskDistance() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Lin Distance", tags = "getLinDistance")
	@RequestMapping(value = "/getLinDistance", method = RequestMethod.GET)
	public String getLinDistance() {
		return "hola";
	}
	
	@ApiOperation(value = "Get Jian and Conrath Distance", tags = "getJianConrathDistance")
	@RequestMapping(value = "/getJianConrathDistance", method = RequestMethod.GET)
	public String getJianConrathDistance() {
		return "hola";
	}
	
}
