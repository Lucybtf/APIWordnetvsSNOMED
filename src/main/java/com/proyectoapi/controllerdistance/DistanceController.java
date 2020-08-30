package com.proyectoapi.controllerdistance;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.proyectoapi.model.DistancesWordnet;
import com.proyectoapi.model.DistancesSnomed;
import com.proyectoapi.model.TreeSnomedCT;
import com.proyectoapi.model.SnomedCTLibrary;
import com.proyectoapi.model.TreeWordnet;
import com.proyectoapi.model.WordnetLibrary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;


/**
 * Class DistanceController:  Esta clase es el controlador de Distancias Semánticas
 * @author Lucía Batista Flores
 * @version 1.0
 */

@Api(value = "DistanceRestController")
@RestController
public class DistanceController {

	private WordnetLibrary wl = new WordnetLibrary();
	private SnomedCTLibrary sn = new SnomedCTLibrary();
	DistancesWordnet distance = new DistancesWordnet();
	DistancesSnomed distancesn = new DistancesSnomed();

	/**
	 * Este método es el controlador que permite calcular la similitud semántica de Wu and Palmer para dos synsets de Wordnet dados ambos offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos synsets
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "DistancesWordnet")
	//@RequestMapping(value = "/getWPDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@GetMapping(path = "/getWPDistance/{offset1}/{offset2}")
	@ResponseBody
	public double getWPDistance(@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2)
			throws JWNLException {
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getDistanceWp(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular la similitud semántica de Wu and Palmer dados las palabras y números de los significados de ambos synsets
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos synsets
	 */
	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "DistancesWordnet")
	//@RequestMapping(value = "/getWPDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@GetMapping(path = "/getWPDistance/{word1}/{sense1}/{word2}/{sense2}")
	@ResponseBody
	public double getWPDistance(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2) {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getDistanceWp(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Wu and Palmer para un subárbol de Wordnet dada una determinada altura
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos synsets de un subárbol
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Wu and Palmer distance in a Subtree of Wordnet by offsets", tags = "DistancesWordnetSubTree")
	@PostMapping(path = "/getWPDistanceSubTree/{offset1}/{offset2}", produces = { "application/json" })
	//@RequestMapping(value = "/getWPDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getWPDistanceSubTree(@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2,
			@RequestBody TreeWordnet treeResult) throws JWNLException {

		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getDistanceWPSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Wu and Palmer para un subárbol de Wordnet dada una determinada altura
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos synsets de un subárbol
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Wu and Palmer distance in a Subtree of Wordnet by offsets", tags = "DistancesWordnetSubTree")
	@PostMapping(path = "/getWPDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", produces = { "application/json" })
	//@RequestMapping(value = "/getWPDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getWPDistanceSubTree(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2, @RequestBody TreeWordnet treeResult)
			throws JWNLException {

		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getDistanceWPSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Wu and Palmer para un subárbol de Wordnet completo dado su synset raíz
	 * @param offsetroot - Número de tipo long que identifica de manera única al synset raíz del subárbol
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @return  Devuelve el valor de la distancia semántica de Wu and Palmer entre dos synsets de un subárbol completo desde un determinado synset raíz
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "DistancesWordnetSubTree")
	@GetMapping(path = "/getWPDistanceAllSubTree/{offsetroot}/{offset1}/{offset2}")
	//@RequestMapping(value = "/getWPDistanceAllSubTree/{offsetroot}/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getWPDistanceAllSubTree(@PathVariable("offset1") Long offsetroot,
			@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2) throws JWNLException {
		Synset root = wl.getSynset(offsetroot.longValue());
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getDistanceWPCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Wu and Palmer para un subárbol de Wordnet completo dado su synset raíz
	 * @param wordroot - Palabra que identifica al synset raíz del subárbol
	 * @param senseroot - Número del significado del synset raíz del subárbol
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos synsets de un subárbol completo desde un determinado synset raíz
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Wu and Palmer distance in Wordnet", tags = "DistancesWordnetSubTree")
	@GetMapping(path = "/getWPDistanceAllSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getWPDistanceAllSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getWPDistanceAllSubTree(@PathVariable("wordroot") String wordroot,
			@PathVariable("senseroot") int senseroot, @PathVariable("word1") String word1,
			@PathVariable("sense1") int sense1, @PathVariable("word2") String word2, @PathVariable("sense2") int sense2) {
		Synset root = wl.getSynset(wordroot, POS.NOUN, senseroot);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getDistanceWPCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos para dos synsets dados ambos offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos synsets 
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "DistancesWordnet")
	@GetMapping(path = "/getSanchezDistance/{offset1}/{offset2}")
	//	@RequestMapping(value = "/getSanchezDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistance(@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2)
			throws JWNLException {
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getSanchezDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos para dos synsets dadas las palabras y los números de significado de cada synset
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos synsets
	 */
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "DistancesWordnet")
	@GetMapping(path = "/getSanchezDistance/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getSanchezDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistance(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2) {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getSanchezDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos para dos synsets de un subárbol dado una altura
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos synsets del subárbol 
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Sanchez distance in a Subtree of Wordnet", tags = "DistancesWordnetSubTree")
	@PostMapping(path = "/getSanchezDistanceSubTree/{offset1}/{offset2}", produces = { "application/json" })
	//@RequestMapping(value = "/getSanchezDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getSanchezDistanceSubTree(@PathVariable("offset1") Long offset1,
			@PathVariable("offset2") Long offset2, @RequestBody TreeWordnet treeResult) throws JWNLException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getSanchezDistanceSubtree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos de un subárbol dadas las palabras y los números de significado de cada synset
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 - Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 - Número del significado del synset2
	 * @param treeResult - Subárbol de Wordnet 
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos synsets del subárbol 
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Sanchez distance in a Subtree of Wordnet", tags = "DistancesWordnetSubTree")
	@PostMapping(path = "/getSanchezDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", produces = { "application/json" })
	//	@RequestMapping(value = "/getSanchezDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getSanchezDistanceSubTree(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2, @RequestBody TreeWordnet treeResult)
			throws JWNLException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getSanchezDistanceSubtree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos de un subárbol de dos synsets de un subárbol completo
	 * @param offsetroot - Número de tipo long que identifica de manera única al synset raíz del subárbol
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos synsets del subárbol 
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getSanchezDistanceCompletedSubTree/{offsetroot}/{offset1}/{offset2}")
	//@RequestMapping(value = "/getSanchezDistanceAllSubTree/{offsetroot}/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistanceCompletedSubTree(@PathVariable("offsetroot") Long offsetroot,
			@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2) throws JWNLException {
		Synset root = wl.getSynset(offsetroot.longValue());
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getSanchezDistanceCompletedSubTree(root, s1, s2);
	}
	
	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos de un subárbol completo dadas las palabras y los números de significado de cada synset
	 * @param wordroot - Palabra que identifica al synset raíz del subárbol
	 * @param senseroot - Número del significado del synset raíz del subárbol
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 - Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 - Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos synsets del subárbol completo desde el nodo raíz
	 */
	@ApiOperation(value = "Get Sanchez distance in Wordnet", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getSanchezDistanceCompletedSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getSanchezDistanceAllSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistanceCompletedSubTree(@PathVariable("wordroot") String wordroot,
			@PathVariable("senseroot") int senseroot, @PathVariable("word1") String word1,
			@PathVariable("sense1") int sense1, @PathVariable("word2") String word2,
			@PathVariable("sense2") int sense2) {
		Synset root = wl.getSynset(wordroot, POS.NOUN, senseroot);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getSanchezDistanceCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única al Synset
	 * @return Devuelve el valor del IC de Sanchez para el synset
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "DistancesWordnet")
	@GetMapping(value = "/getSanchezIC/{offset}")
	//@RequestMapping(value = "/getSanchezIC/{offset}", method = RequestMethod.GET)
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public double getSanchezIC(@PathVariable("offset") Long offset) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset.longValue());
		return distance.getICMeasure(s1);
	}

	/**
	 * Este método es el controlador que permite calcula el valor del IC de Sanchez de un synset dada su palabra y el número de significado de este
	 * @param word1 - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el valor del IC de Sanchez para el synset
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Sanchez IC of a Word and  a Sense", tags = "DistancesWordnet")
	@GetMapping(value = "/getSanchezIC/{word}/{sense}")
	//	@RequestMapping(value = "/getSanchezIC/{word}/{sense}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezIC(@PathVariable("word") String word1, @PathVariable("sense") int sense)
			throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense);
		return distance.getICMeasure(s1);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez dentro de un subárbol para el synset dado su offset 
	 * @param offset - Número de tipo long que identifica de manera única al Synset
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor del IC de Sanchez para el synset de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "DistancesWordnetSubTree")
	@PostMapping(path = "/getSanchezICSubTree/{offset}", produces = { "application/json" })
	//@RequestMapping(value = "/getSanchezICSubTree/{offset}", method = RequestMethod.POST, consumes = {"application/json" })
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public double getSanchezICSubTree(@PathVariable("offset") Long offset, @RequestBody TreeWordnet treeResult)
			throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset.longValue());
		return distance.getICMeasureSubTree(s1, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez dentro de un subárbol para el synset dada su palabra y el número de significado de este
	 * @param word1 - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor del IC de Sanchez para el synset de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "DistancesWordnetSubTree")
	@PostMapping(path = "/getSanchezICSubTree/{word}/{sense}", produces = { "application/json" })
	//@RequestMapping(value = "/getSanchezICSubTree/{word}/{sense}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getSanchezICSubTree(@PathVariable("word") String word1, @PathVariable("sense") int sense,
			@RequestBody TreeWordnet treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense);
		return distance.getICMeasureSubTree(s1, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez de un subárbol completo dado el synset raíz
	 * @param offsetroot -  Número de tipo long que identifica de manera única al synset raíz del subárbol
	 * @param offset - Número de tipo long que identifica de manera única al Synset
	 * @return Devuelve el valor del IC de Sanchez para el synset del subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Sanchez IC of a Synset", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getSanchezICCompletedSubTree/{offsetroot}/{offset}")
	//@RequestMapping(value = "/getSanchezICAllSubTree/{offsetroot}/{offset}", method = RequestMethod.GET)
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public double getSanchezICCompletedSubTree(@PathVariable("offsetroot") Long offsetroot,
			@PathVariable("offset") Long offset) throws JWNLException, IOException {
		Synset root = wl.getSynset(offsetroot.longValue());
		Synset s1 = wl.getSynset(offset.longValue());
		return distance.getICMeasureCompletedSubTree(root, s1);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez de un subárbol completo dado el synset raíz
	 * @param wordroot - Palabra que identifica al synset raíz del subárbol
	 * @param senseroot - Número del significado del synset raíz del subárbol
	 * @param word1 - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el valor del IC de Sanchez para el synset del subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Sanchez IC of a Word and  a Sense", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getSanchezICCompletedSubTree/{wordroot}/{senseroot}/{word}/{sense}")
	//@RequestMapping(value = "/getSanchezICAllSubTree/{wordroot}/{senseroot}/{word}/{sense}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezICCompletedSubTree(@PathVariable("wordroot") String wordroot,
			@PathVariable("senseroot") int senseroot, @PathVariable("word") String word1,
			@PathVariable("sense") int sense) throws JWNLException, IOException {
		Synset root = wl.getSynset(wordroot, POS.NOUN, senseroot);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense);
		return distance.getICMeasureCompletedSubTree(root, s1);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Resnik para dos synsets dados sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos synsets
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Resnisk Distance", tags = "DistancesWordnet")
	@GetMapping(value = "/getResniskDistance/{offset1}/{offset2}")
	//@RequestMapping(value = "/getResniskDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResniskDistance(@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2)
			throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getResniskDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Resnik para dos synsets dadas sus palabras y sus números de significados respectivamente
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos synsets
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Resnisk Distance", tags = "DistancesWordnet")
	@GetMapping(value = "/getResniskDistance/{word1}/{sense1}/{word2}/{sense2}}")
	//@RequestMapping(value = "/getResniskDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResniskDistance(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getResniskDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Resnik para dos synsets de un subárbol dados sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos synsets de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Resnisk Distance in SubTree", tags = "DistancesWordnetSubTree")
	@PostMapping(value = "/getResniskDistance/{word1}/{sense1}/{word2}/{sense2}}", consumes = {"application/json" })
	//@RequestMapping(value = "/getResniskDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getResniskDistanceSubTree(@PathVariable("offset1") long offset1,
			@PathVariable("offset2") long offset2, @RequestBody TreeWordnet treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getResniskDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Resnik para dos synsets de un subárbol dadas sus palabras y números de significado respectivamente
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos synsets de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Resnisk Distance in SubTree", tags = "DistancesWordnetSubTree")
	@PostMapping(value = "/getResniskDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getResniskDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getResniskDistanceSubTree(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2, @RequestBody TreeWordnet treeResult)
			throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getResniskDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular Devuelve el valor de la distancia semántica de Resnik para dos synsets de un subárbol completo
	 * @param offsetroot - Número de tipo long que identifica de manera única al Synset raíz del subárbol
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos synsets de un subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Resnisk Distance", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getResniskDistanceCompletedSubTree/{offsetroot}/{offset1}/{offset2}")
	//@RequestMapping(value = "/getResniskDistanceAllSubTree/{offsetroot}/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResniskDistanceCompletedSubTree(@PathVariable("offsetroot") Long offsetroot,
			@PathVariable("offset1") Long offset1, @PathVariable("offset2") Long offset2)
			throws JWNLException, IOException {
		Synset root = wl.getSynset(offsetroot.longValue());
		Synset s1 = wl.getSynset(offset1.longValue());
		Synset s2 = wl.getSynset(offset2.longValue());
		return distance.getResniskDistanceCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Resnik para dos synsets de un subárbol completo
	 * @param wordroot - Palabra que identifica al synset raíz del subárbol
	 * @param senseroot - Número del significado del synset raíz del subárbol
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos synsets de un subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Resnisk Distance", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getResniskDistanceCompletedSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}")
	//	@RequestMapping(value = "/getResniskDistanceAllSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResniskDistanceCompletedSubTree(@PathVariable("wordroot") String wordroot,
			@PathVariable("senseroot") int senseroot, @PathVariable("word1") String word1,
			@PathVariable("sense1") int sense1, @PathVariable("word2") String word2, @PathVariable("sense2") int sense2)
			throws JWNLException, IOException {
		Synset root = wl.getSynset(wordroot, POS.NOUN, senseroot);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getResniskDistanceCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Lin para dos synsets dados sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el valor de la distancia semántica de Lin para dos synsets 
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Lin Distance", tags = "DistancesWordnet")
	@GetMapping(value = "/getLinDistance/{offset1}/{offset2}")
	//@RequestMapping(value = "/getLinDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistance(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2)
			throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getLinDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Lin para dos synsets dadas sus palabras y sus números de significado respectivamente
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Lin para dos synsets 
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Lin Distance", tags = "DistancesWordnet")
	@GetMapping(value = "/getLinDistance/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getLinDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistance(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getLinDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Lin para dos synsets de un subárbol dados sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Lin para dos synsets de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Lin Distance", tags = "DistancesWordnetSubTree")
	@PostMapping(value = "/getLinDistanceSubTree/{offset1}/{offset2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getLinDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getLinDistanceSubTree(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2,
			@RequestBody TreeWordnet treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getLinDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Lin para dos synsets de un subárbol dadas sus palabras y números de significados respectivamente
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Lin para dos synsets de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Lin Distance", tags = "DistancesWordnetSubTree")
	@PostMapping(value = "/getLinDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getLinDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getLinDistanceSubTree(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2, @RequestBody TreeWordnet treeResult)
			throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getLinDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Lin para dos synsets de un subárbol completo
	 * @param offsetroot - Número de tipo long que identifica de manera única al Synset raíz del subárbol
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el valor de la distancia semántica de Lin para dos synsets de un subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Lin Distance", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getLinDistanceCompletedSubTree/{offsetroot}/{offset1}/{offset2}")
	//@RequestMapping(value = "/getLinDistanceAllSubTree/{offsetroot}/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistanceCompletedSubTree(@PathVariable("offsetroot") long offsetroot,
			@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2)
			throws JWNLException, IOException {
		Synset root = wl.getSynset(offsetroot);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getLinDistanceCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Lin para dos synsets de un subárbol completo
	 * @param wordroot - Palabra que identifica al synset raíz del subárbol
	 * @param senseroot - Número del significado del synset raíz del subárbol
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Lin para dos synsets de un subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Lin Distance", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getLinDistanceCompletedSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getLinDistanceAllSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistanceCompletedSubTree(@PathVariable("wordroot") String wordroot,
			@PathVariable("senseroot") int senseroot, @PathVariable("word1") String word1,
			@PathVariable("sense1") int sense1, @PathVariable("word2") String word2, @PathVariable("sense2") int sense2)
			throws JWNLException, IOException {
		Synset root = wl.getSynset(wordroot, POS.NOUN, senseroot);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getLinDistanceCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Jiang and Conrath para dos synsets dados sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve  el valor de la distancia semántica de Jiang and Conrath para dos synsets
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance", tags = "DistancesWordnet")
	@GetMapping(value = "/getJiangConrathDistance/{offset1}/{offset2}")
	//@RequestMapping(value = "/getJianConrathDistance/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJiangConrathDistance(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2)
			throws JWNLException, IOException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getJiangConrathDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Jiang and Conrath para dos synsets dadas sus palabras y número de significado respectivamente
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 -  Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 -  Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath para dos synsets 
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance", tags = "DistancesWordnet")
	@GetMapping(value = "/getJiangConrathDistance/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getJianConrathDistance/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJiangConrathDistance(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2) throws JWNLException, IOException {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getJiangConrathDistance(s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Jian and Conrath Distance", tags = "DistancesWordnetSubTree")
	@PostMapping(value = "/getJiangConrathDistanceSubTree/{offset1}/{offset2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getJianConrathDistanceSubTree/{offset1}/{offset2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getJiangConrathDistanceSubTree(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2,
			@RequestBody TreeWordnet treeResult) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getJiangConrathDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 - Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 - Número del significado del synset2
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance", tags = "DistancesWordnetSubTree")
	@PostMapping(value = "/getJiangConrathDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getJianConrathDistanceSubTree/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getJiangConrathDistanceSubTree(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2, @RequestBody TreeWordnet treeResult)
			throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getJiangConrathDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol completo
	 * @param offsetroot - Número de tipo long que identifica de manera única al Synset raíz del subárbol
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getJiangConrathDistanceCompletedSubTree/{offsetroot}/{offset1}/{offset2}")
	//@RequestMapping(value = "/getJianConrathDistanceAllSubTree/{offsetroot}/{offset1}/{offset2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJiangConrathDistanceCompletedSubTree(@PathVariable("offsetroot") long offsetroot,
			@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2)
			throws JWNLException, IOException {
		Synset root = wl.getSynset(offsetroot);
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return distance.getjianConrathDistanceCompletedSubTree(root, s1, s2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol completo
	 * @param wordroot - Palabra que identifica al synset raíz del subárbol
	 * @param senseroot - Número del significado del synset raíz del subárbol
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 - Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 - Número del significado del synset2
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath para dos synsets de un subárbol completo desde el synset raíz
	 * @throws JWNLException
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance", tags = "DistancesWordnetSubTree")
	@GetMapping(value = "/getJiangConrathDistanceCompletedSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}")
	//@RequestMapping(value = "/getJianConrathDistanceAllSubTree/{wordroot}/{senseroot}/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJiangConrathDistanceCompletedSubTree(@PathVariable("wordroot") String wordroot,
			@PathVariable("senseroot") int senseroot, @PathVariable("word1") String word1,
			@PathVariable("sense1") int sense1, @PathVariable("word2") String word2, @PathVariable("sense2") int sense2)
			throws JWNLException, IOException {
		Synset root = wl.getSynset(wordroot, POS.NOUN, senseroot);
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return distance.getjianConrathDistanceCompletedSubTree(root, s1, s2);
	}

	/*---------------------------- SNOMED CT CONTROLLER DISTANCE----------------------------------------- */
	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Wu and Palmer para dos conceptos
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer para dos conceptos
	 */
	@ApiOperation(value = "Get Wu and Palmer distance By Id", tags = "DistancesSnomedCT")
	@GetMapping(value = "/getWPDistanceSnomed/{id1}/{id2}")
	//@RequestMapping(value = "/getWPDistanceSnomed/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getWPDistanceSnomed(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2) {
		return distancesn.getdistanceWP(id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Sanchez basada en conjuntos para dos conceptos
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Sanchez basada en conjuntos para dos conceptos
	 */
	@ApiOperation(value = "Get Sanchez distance By Id", tags = "DistancesSnomedCT")
	@GetMapping(value = "/getSanchezDistanceSnomed/{id1}/{id2}")
	//@RequestMapping(value = "/getSanchezDistanceSnomed/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistanceSnomed(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2) {
		return distancesn.SanchezDistance(id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez para dos conceptos
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @return Devuelve el valor del IC de Sanchez para dos conceptos
	 */
	@ApiOperation(value = "Get Sanchez IC By Id", tags = "DistancesSnomedCT")
	@GetMapping(value = "/getSanchezICSnomed/{id1}")
	//@RequestMapping(value = "/getSanchezICSnomed/{id1}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezICSnomed(@PathVariable("id1") long id1) {
		return distancesn.IC_measure(id1);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de Resnik para dos conceptos
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos conceptos
	 */
	@ApiOperation(value = "Get Resnisk Distance By Id", tags = "DistancesSnomedCT")
	@GetMapping(value = "/getResnikDistanceSnomed/{id1}/{id2}")
	//@RequestMapping(value = "/getResnikDistanceSnomed/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResnikDistanceSnomed(@PathVariable("id1") long id1, @PathVariable("id2") long id2) {
		return distancesn.resnik_Distance(id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de Lin para dos conceptos
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Lin para dos conceptos
	 */
	@ApiOperation(value = "Get Lin Distance By Id", tags = "DistancesSnomedCT")
	@GetMapping(value = "/getLinDistanceSnomed/{id1}/{id2}")
	//@RequestMapping(value = "/getLinDistanceSnomed/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistanceSnomed(@PathVariable("id1") long id1, @PathVariable("id2") long id2) {
		return distancesn.lin_Distance(id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de Jiang and Conrath para dos conceptos
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath para dos conceptos
	 */
	@ApiOperation(value = "Get Jian and Conrath Distance By Id", tags = "DistancesSnomedCT")
	@GetMapping(value = "/getJiangConrathDistanceSnomed/{id1}/{id2}")
	//@RequestMapping(value = "/getjianConrathDistanceSnomed/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJiangConrathDistanceSnomed(@PathVariable("id1") long id1, @PathVariable("id1") long id2) {
		return distancesn.jianConrath_Distance(id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Wu and Palmer para dos conceptos de un subárbol
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica Wu and Palmer para dos conceptos del subárbol
	 */
	@ApiOperation(value = "Get Wu and Palmer distance By Id", tags = "DistancesSnomedCTSubTree")
	@PostMapping(value = "/getWPDistanceSnomedSubTree/{id1}/{id2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getWPDistanceSubGraphSnomed/{id1}/{id2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getWPDistanceSnomedSubTree(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2,
			@RequestBody TreeSnomedCT subtree) {

		LinkedHashMap<Long, Integer> g = sn.getSubTreeSnomedConversion(subtree);
		return distancesn.getWuSimilaritySubTree(id1, id2, g);
	}

	/**
	 * Este método es el controlador que permite calcular el valor de la distancia semántica de Sanchez basada en conjuntos para dos conceptos de un subárbol
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica de Sanchez basada en conjuntos para dos conceptos del subárbol
	 */
	@ApiOperation(value = "Get Sanchez distance By Id", tags = "DistancesSnomedCTSubTree")
	@PostMapping(value = "/getSanchezDistanceSnomedSubTree/{id1}/{id2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getSanchezDistanceSnomedSubGraph/{id1}/{id2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getSanchezDistanceSnomedSubTree(@PathVariable("id1") Long id1, @PathVariable("id2") Long id2,
			@RequestBody TreeSnomedCT subtree) {
		LinkedHashMap<Long, Integer> g = sn.getSubTreeSnomedConversion(subtree);
		return distancesn.SanchezDistanceSubTree(id1, id2, g);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez para dos conceptos de un subárbol
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor del IC de Sanchez para un concepto del subárbol
	 */
	@ApiOperation(value = "Get Sanchez IC By Id", tags = "DistancesSnomedCTSubTree")
	@PostMapping(value = "/getICMeasureSnomedSubTree/{id1}", consumes = {"application/json" })
	//@RequestMapping(value = "/IC_measureSnomedSubTree/{id1}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getICMeasureSnomedSubTree(@PathVariable("id1") long id1, @RequestBody TreeSnomedCT subtree) {
		LinkedHashMap<Long, Integer> g = sn.getSubTreeSnomedConversion(subtree);
		return distancesn.IC_measureSubTree(id1, g);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Resnik para dos conceptos de un subárbol
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos conceptos del subárbol
	 */
	@ApiOperation(value = "Get Resnisk Distance By Id", tags = "DistancesSnomedCTSubTree")
	@PostMapping(value = "/getResnikDistanceSnomedSubTree/{id1}/{id2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getResnikDistanceSnomedSubGraph/{id1}/{id2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getResnikDistanceSnomedSubTree(@PathVariable("id1") long id1,
			@PathVariable("id2") long id2, @RequestBody TreeSnomedCT subtree) {
		LinkedHashMap<Long, Integer> g = sn.getSubTreeSnomedConversion(subtree);
		return distancesn.resnik_DistanceSubTree(id1, id2, g);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Lin para dos conceptos de un subárbol
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica de Lin para dos conceptos del subárbol
	 */
	@ApiOperation(value = "Get Lin Distance By Id", tags = "DistancesSnomedCTSubTree")
	@PostMapping(value = "/getLinDistanceSnomedSubTree/{id1}/{id2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getLinDistanceSnomedSubGraph/{id1}/{id2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getLinDistanceSnomedSubTree(@PathVariable("id1") long id1, @PathVariable("id2") long id2,
			@RequestBody TreeSnomedCT subtree) {
		LinkedHashMap<Long, Integer> g = sn.getSubTreeSnomedConversion(subtree);
		return distancesn.lin_DistanceSubTree(id1, id2, g);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Jiang and Conrath para dos conceptos de un subárbol
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	  * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica Jiang y Conrath para dos conceptos del subárbol
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance By Id", tags = "DistancesSnomedCTSubTree")
	@PostMapping(value = "/getJiangConrathDistanceSnomedSubTree/{id1}/{id2}", consumes = {"application/json" })
	//@RequestMapping(value = "/getjianConrathDistanceSnomedSubGraph/{id1}/{id2}", method = RequestMethod.POST, consumes = {"application/json" })
	@ResponseBody
	public double getJiangConrathDistanceSnomedSubTree(@PathVariable("id1") long id1, @PathVariable("id2") long id2,
			@RequestBody TreeSnomedCT subtree) {
		LinkedHashMap<Long, Integer> g = sn.getSubTreeSnomedConversion(subtree);
		return distancesn.jianConrath_DistanceSubGraph(id1, id2, g);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Wu and Palmer para dos conceptos de un subárbol completo
	 * @param rootid - Número de tipo long que identifica de manera única al concepto raíz del subárbol 
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer para dos conceptos del subárbol completo desde el concepto raíz
	 */
	@ApiOperation(value = "Get Wu and Palmer distance By Id", tags = "DistancesSnomedCTSubTree")
	@GetMapping(value = "/getWPDistanceSnomedCompletedSubTree/{rootid}/{id1}/{id2}")
	//@RequestMapping(value = "/getWPDistanceSnomedAllSubTree/{rootid}/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getWPDistanceSnomedCompletedSubTree(@PathVariable("rootid") Long rootid, @PathVariable("id1") Long id1,
			@PathVariable("id2") Long id2) {
		return distancesn.getWuSimilaritySubTree(rootid, id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Sanchez basada en conjuntos para dos conceptos de un subárbol completo
	 * @param rootid - Número de tipo long que identifica de manera única al concepto raíz del subárbol 
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Sanchez basada en conjuntos para dos conceptos del subárbol completo desde el concepto raíz
	 */
	@ApiOperation(value = "Get Sanchez distance By Id", tags = "DistancesSnomedCTSubTree")
	@GetMapping(value = "/getSanchezDistanceSnomedCompletedSubTree/{rootid}/{id1}/{id2}")
	//@RequestMapping(value = "/getSanchezDistanceSnomedAllSubTree/{rootid}/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getSanchezDistanceSnomedCompletedSubTree(@PathVariable("rootid") Long rootid, @PathVariable("id1") Long id1,
			@PathVariable("id2") Long id2) {
		return distancesn.getSanchezDistanceSubTree(rootid, id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular el valor del IC de Sanchez para un concepto del subárbol completo
	 * @param rootid - Número de tipo long que identifica de manera única al concepto raíz del subárbol 
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @return Devuelve el valor de la distancia semántica del IC de Sanchez para un concepto del subárbol completo desde el concepto raíz
	 */
	@ApiOperation(value = "Get Sanchez IC By Id", tags = "DistancesSnomedCTSubTree")
	@GetMapping(value = "/getICMeasureCompletedSubTree/{rootid}/{id1}")
	//@RequestMapping(value = "/getSanchezICSnomedAllSubTree/{rootid}/{id1}", method = RequestMethod.GET)
	@ResponseBody
	public double getICMeasureCompletedSubTree(@PathVariable("rootid") Long rootid, @PathVariable("id1") long id1) {
		return distancesn.getIC_measureSubTree(rootid, id1);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Resnik para dos conceptos de un subárbol completo
	 * @param rootid - Número de tipo long que identifica de manera única al concepto raíz del subárbol 
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Resnik para dos conceptos del subárbol completo desde el concepto raíz
	 */
	@ApiOperation(value = "Get Resnisk Distance By Id", tags = "DistancesSnomedCTSubTree")
	@GetMapping(value = "/getResnikDistanceCompletedSubTree/{rootid}/{id1}/{id2}")
	//@RequestMapping(value = "/getResnikDistanceSnomedAllSubTree/{rootid}/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getResnikDistanceCompletedSubTree(@PathVariable("rootid") Long rootid, @PathVariable("id1") long id1,
			@PathVariable("id2") long id2) {
		return distancesn.getResnik_DistanceSubTree(rootid, id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Lin para dos conceptos de un subárbol completo
	 * @param rootid - Número de tipo long que identifica de manera única al concepto raíz del subárbol 
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Lin para dos conceptos del subárbol completo desde el concepto raíz
	 */
	@ApiOperation(value = "Get Lin Distance By Id", tags = "DistancesSnomedCTSubTree")
	@GetMapping(value = "/getLinDistanceCompletedSubTree/{rootid}/{id1}/{id2}")
	//@RequestMapping(value = "/getLinDistanceSnomedAllSubTree/{rootid}/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getLinDistanceCompletedSubTree(@PathVariable("rootid") Long rootid, @PathVariable("id1") long id1,
			@PathVariable("id2") long id2) {
		return distancesn.getLin_DistanceSubTree(rootid, id1, id2);
	}

	/**
	 * Este método es el controlador que permite calcular la distancia semántica de Jiang and Conrath basada en conjuntos para dos conceptos de un subárbol completo
	 * @param rootid - Número de tipo long que identifica de manera única al concepto raíz del subárbol 
	 * @param id1 - Número de tipo long que identifica de manera única al concepto id1
	 * @param id2 - Número de tipo long que identifica de manera única al concepto id2
	 * @return Devuelve el valor de la distancia semántica de Jian and Conrath para dos conceptos del subárbol completo desde el concepto raíz
	 */
	@ApiOperation(value = "Get Jiang and Conrath Distance By Id", tags = "DistancesSnomedCTSubTree")
	@GetMapping(value = "/getJiangConrathDistanceCompletedSubTree/{rootid}/{id1}/{id2}")
	//@RequestMapping(value = "/getjianConrathDistanceSnomedAllSubTree/{rootid}/{id1}/{id2}", method = RequestMethod.GET)
	@ResponseBody
	public double getJiangConrathDistanceCompletedSubTree(@PathVariable("rootid") Long rootid, @PathVariable("id1") long id1,
			@PathVariable("id2") long id2) {
		return distancesn.getJianConrath_DistanceTree(rootid, id1, id2);
	}

}
