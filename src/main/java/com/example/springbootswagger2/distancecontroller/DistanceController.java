package com.example.springbootswagger2.distancecontroller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.springbootswagger2.model.Distance;
import com.example.springbootswagger2.model.WordnetLibrary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Synset;
import springfox.documentation.annotations.ApiIgnore;


@Api(value = "DistanceRestController", description = "REST Api Wordnet")
@RestController
public class DistanceController {

	private WordnetLibrary wl = new WordnetLibrary();
	Distance distance = new Distance();

	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistance/{offset1}/{offset2}", method = RequestMethod.GET)
//	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public double getWPDistance(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2) throws JWNLException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.Distance_WP(s1, s2, wl);
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
	
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "getICMeasure")
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
	