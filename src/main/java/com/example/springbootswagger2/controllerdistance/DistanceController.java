package com.example.springbootswagger2.controllerdistance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.ws.rs.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.example.springbootswagger2.model.Distance;
import com.example.springbootswagger2.model.Tree;
import com.example.springbootswagger2.model.WordnetLibrary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import springfox.documentation.annotations.ApiIgnore;


@Api(value = "DistanceRestController", description = "REST Api Wordnet Distance")
@RestController
public class DistanceController {

	private WordnetLibrary wl = new WordnetLibrary();
	Distance distance = new Distance();

	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getWPDistance(@PathVariable("offset1")Long offset1, @PathVariable("offset2")Long offset2) throws JWNLException {
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.Distance_WP(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getWPDistance(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2") int sense2) throws JWNLException {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.Distance_WP(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Wu and Palmer distance in a Subtree of Wordnet by offsets", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getWPDistanceSubTree(@PathVariable("offset1")Long offset1, @PathVariable("offset2")Long offset2, @RequestBody Tree treeResult) throws JWNLException {
		
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
	
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.Distance_WPSubTree(s1, s2, tree, wl);
	}
	
	@ApiOperation(value = "Get Wu and Palmer distance in a Subtree of Wordnet by offsets", tags = "getWuandPamerDistance")
	@RequestMapping(value = "/getWPDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getWPDistanceSubTree(@PathVariable("word1")String word1, @PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2")int sense2, @RequestBody Tree treeResult) throws JWNLException {
		
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
	
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.Distance_WPSubTree(s1, s2, tree, wl);
	}
	
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "getSanchezDistance")
	@RequestMapping(value = "/getSanchezDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistance(@PathVariable("offset1")Long offset1, @PathVariable("offset2")Long offset2) throws JWNLException {
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.Sanchez_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "getSanchezDistance")
	@RequestMapping(value = "/getSanchezDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistance(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2,@PathVariable("sense2") int sense2) throws JWNLException {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.Sanchez_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Sanchez distance in a Subtree of Wordnet", tags = "getSanchezDistance")
	@RequestMapping(value = "/getSanchezDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getSanchezDistanceSubTree(@PathVariable("offset1")Long offset1, @PathVariable("offset2")Long offset2, @RequestBody Tree treeResult) throws JWNLException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.Sanchez_DistanceSubtree(s1, s2, tree, wl);
	}
	
	@ApiOperation(value = "Get Sanchez distance in a Subtree of Wordnet", tags = "getSanchezDistance")
	@RequestMapping(value = "/getSanchezDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getSanchezDistanceSubTree(@PathVariable("word1")String word1, @PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2") int sense2, @RequestBody Tree treeResult) throws JWNLException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.Sanchez_DistanceSubtree(s1, s2, tree, wl);
	}
	
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "getICMeasure")
	@RequestMapping(value = "/getSanchezIC/{offset}", method = RequestMethod.GET)
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public double getSanchezIC(@PathVariable("offset")Long offset) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset.longValue());
		return distance.IC_measure(s1, wl);
	}
	
	@ApiOperation(value = "Get Sanchez IC of a Word and  a Sense", tags = "getICMeasure")
	@RequestMapping(value = "/getSanchezIC/{word}/{sense}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezIC(@PathVariable("word")String word1, @PathVariable("sense")int sense) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense);
		return distance.IC_measure(s1, wl);
	}
	
	
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "getICMeasure")
	@RequestMapping(value = "/getSanchezICSubTree/{offset}", method = RequestMethod.POST, consumes={"application/json"})
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public double getSanchezICSubTree(@PathVariable("offset")Long offset, @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset.longValue());
		return distance.IC_measureSubTree(s1, tree, wl);
	}
	
	
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "getICMeasure")
	@RequestMapping(value = "/getSanchezICSubTree/{word}/{sense}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getSanchezICSubTree(@PathVariable("word")String word1, @PathVariable("sense")int sense, @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense);
		return distance.IC_measureSubTree(s1, tree, wl);
	}
	
	@ApiOperation(value = "Get Resnisk Distance", tags = "getResniskDistance")
	@RequestMapping(value = "/getResniskDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResniskDistance(@PathVariable("offset1")Long offset1, @PathVariable("offset2")Long offset2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.resnisk_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Resnisk Distance", tags = "getResniskDistance")
	@RequestMapping(value = "/getResniskDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResniskDistance(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2,@PathVariable("sense2")int sense2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2,POS.NOUN, sense2);
		return distance.resnisk_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Resnisk Distance in SubTree", tags = "getResniskDistance")
	@RequestMapping(value = "/getResniskDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getResniskDistanceSubTree(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2,  @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.resnisk_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Resnisk Distance in SubTree", tags = "getResniskDistance")
	@RequestMapping(value = "/getResniskDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getResniskDistanceSubTree(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2,@PathVariable("sense2")int sense2, @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2,POS.NOUN, sense2);
		return distance.resnisk_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Lin Distance", tags = "getLinDistance")
	@RequestMapping(value = "/getLinDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistance(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.lin_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Lin Distance", tags = "getLinDistance")
	@RequestMapping(value = "/getLinDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistance(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2")int sense2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2,POS.NOUN, sense2);
		return distance.lin_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Lin Distance", tags = "getLinDistance")
	@RequestMapping(value = "/getLinDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getLinDistance(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2,  @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.lin_DistanceSubTree(s1, s2, tree, wl);
	}
	
	@ApiOperation(value = "Get Lin Distance", tags = "getLinDistance")
	@RequestMapping(value = "/getLinDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getLinDistance(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2")int sense2,  @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2,POS.NOUN, sense2);
		return distance.lin_DistanceSubTree(s1, s2, tree, wl);
	}
	
	
	@ApiOperation(value = "Get Jian and Conrath Distance", tags = "getJianConrathDistance")
	@RequestMapping(value = "/getJianConrathDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJianConrathDistance(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.jianConrath_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Jian and Conrath Distance", tags = "getJianConrathDistance")
	@RequestMapping(value = "/getJianConrathDistance/{word1}/{sense1}/{word2}/{sense2}",  method = RequestMethod.GET)
	@ResponseBody
	public double getJianConrathDistance(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2")int sense2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2,POS.NOUN, sense2);
		return distance.jianConrath_Distance(s1, s2, wl);
	}
	
	@ApiOperation(value = "Get Jian and Conrath Distance", tags = "getJianConrathDistance")
	@RequestMapping(value = "/getJianConrathDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getJianConrathDistance(@PathVariable("offset1")long offset1, @PathVariable("offset2")long offset2, @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.jianConrath_DistanceSubTree(s1, s2, tree, wl);
	}
	
	@ApiOperation(value = "Get Jian and Conrath Distance", tags = "getJianConrathDistance")
	@RequestMapping(value = "/getJianConrathDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes={"application/json"})
	@ResponseBody
	public double getJianConrathDistanceSubTree(@PathVariable("word1")String word1,@PathVariable("sense1")int sense1, @PathVariable("word2")String word2, @PathVariable("sense2")int sense2, @RequestBody Tree treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<>();
		
		tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1,POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2,POS.NOUN, sense2);
		return distance.jianConrath_DistanceSubTree(s1, s2, tree, wl);
	}


}
	