package com.proyectoapi.controllerwordnet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.battcn.boot.swagger.model.DataType;
import com.battcn.boot.swagger.model.ParamType;
import com.proyectoapi.model.TreeWordnet;
import com.proyectoapi.model.WordnetLibrary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;

/**
 * Class WordnetLibraryController: Esta clase es el controlador para Wordnet
 * @author Lucía Batista Flores
 * @version 1.0
 */
@Api(value = "WordnetRestController")
@RestController
public class WordnetLibraryController {

	static Logger logger = Logger.getLogger( WordnetLibraryController.class.getName());
	String jsonlabel = "synset";
	private WordnetLibrary wl = new WordnetLibrary();

	/**
	 * Este método obtiene el synset a través del offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve una cadena con el objeto synset de la ontología
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Synset by Offset", tags = "getSynset")
	@GetMapping(path = "/getSynset/{offset}")
	//@RequestMapping(value = "/getSynset/{offset}", method = RequestMethod.GET)
	public String getSynset(
			@ApiParam(value = "Offset", required = true, example = "4582285") @PathVariable("offset") long offset)
			throws JWNLException {
		Synset a = wl.getSynset(offset);
		return a.toString();
	}

	
	/**
	 * Este método obtiene el synset a través de la palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve una cadena con el objeto synset de la ontología
	 */
	@ApiOperation(value = "Get Synset by Word and sense", tags = "getSynset")
	@GetMapping(path = "/getSynset/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getSynset/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSynset(@PathVariable("word") String word, @PathVariable("sense") int sense) {
		JSONObject obj = new JSONObject();
		Synset a = wl.getSynset(word, POS.NOUN, sense);
		obj.append(jsonlabel, a);
		return obj.toString();
	}

	/**
	 * Este método obtiene el significado del synset recibiendo como parámetro el offset del synset 
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve la cadena del significado del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@GetMapping(path = "/getSense/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getSense/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSense(@PathVariable("offset") long offset) throws JWNLException {
		return wl.getSense(offset);
	}

	/**
	 * Este método obtiene el significado del synset recibiendo como parámetro la palabra y el número del significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve la cadena del significado del synset
	 */
	@ApiOperation(value = "Get Sense by Synset", tags = "getSense")
	@GetMapping(path = "/getSense/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getSense/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSense(@PathVariable("word") String word, @PathVariable("sense") int sense) {
		return wl.getSense(word, POS.NOUN, sense);
	}

	/**
	 * Este método obtiene el listado de sinónimos de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de sinónimos
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Synonyms", tags = "getSynonyms")
	@GetMapping(path = "/getSynonyms/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getSynonyms/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSynonyms(@PathVariable("offset") long offset) throws JWNLException {
		List<Synset> synonyms = wl.getSynonyms(offset);
		return synonyms.toString();
	}

	/**
	 * Este método obtiene el listado de sinónimos de un synset dada la palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de sinónimos
	 */
	@ApiOperation(value = "Get Synonyms", tags = "getSynonyms")
	@GetMapping(path = "/getSynonyms/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getSynonyms/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSynonyms(@PathVariable("word") String word, @PathVariable("sense") int sense) {
		List<Synset> synonyms = wl.getSynonyms(word, POS.NOUN, sense);
		return synonyms.toString();
	}

	/**
	 * Este método obtiene el listado de palabras sinónimas de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de palabras sinónimas del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Synonyms Words", tags = "getSynonyms")
	@GetMapping(path = "/getSynonymsWords/{offset}",  produces = { "text/plain" })
//	@RequestMapping(value = "/getSynonymsWords/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSynonymsWords(@PathVariable("offset") long offset) throws JWNLException {
		List<String> synonyms = wl.getSynonymsWords(offset);
		return synonyms.toString();
	}

	/**
	 * Este método obtiene el listado de palabras sinónimas de un synset dada la palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de palabras sinónimas del synset
	 */
	@ApiOperation(value = "Get Synonyms Words", tags = "getSynonyms")
	@GetMapping(path = "/getSynonymsWords/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getSynonymsWords/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getSynonymsWords(@PathVariable("word") String word, @PathVariable("sense") int sense) {
		List<String> synonyms = wl.getSynonymsWords(word, POS.NOUN, sense);
		return synonyms.toString();
	}

	/**
	 * Este método obtiene el listado de hiperónimos de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de hiperónimos del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hypernyms", tags = "getHypernyms")
	@GetMapping(path = "/getHypernyms/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHypernyms/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHypernyms(@PathVariable("offset") long offset) throws JWNLException {
		List<Synset> hypernyms = wl.getHypernyms(offset);
		return hypernyms.toString();
	}

	/**
	 * Este método obtiene el listado de hiperónimos de un synset dada la palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de hiperónimos del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hypernyms", tags = "getHypernyms")
	@GetMapping(path = "/getHypernyms/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHypernyms/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHypernyms(@PathVariable("word") String word, @PathVariable("sense") int sense)
			throws JWNLException {
		List<Synset> hypernyms = wl.getHypernyms(word, POS.NOUN, sense);
		return hypernyms.toString();
	}

	/**
	 * Este método obtiene el listado de palabras hiperónimos de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de palabras hiperónimas del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hypernyms Words", tags = "getHypernyms")
	@GetMapping(path = "/getHypernymsWors/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHypernymsWors/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHypernymsWords(@PathVariable("offset") long offset) throws JWNLException {
		List<String> hypernyms = wl.getHypernymsWords(offset);
		return hypernyms.toString();
	}

	/**
	 * Este método obtiene el listado de palabras hiperónimas de un synset dada la palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de palabras hiperónimas del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hypernyms Words", tags = "getHypernyms")
	@GetMapping(path = "/getHypernymsWords/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHypernymsWords/{word}/{sense}", method = RequestMethod.GET, produces = {"text/plain" })
	public String getHypernymsWords(@PathVariable("word") String word, @PathVariable("sense") int sense)
			throws JWNLException {
		List<String> hypernyms = wl.getHypernymsWords(word, sense, POS.NOUN);
		return hypernyms.toString();
	}

	/**
	 * 
	 * Este método obtiene el listado de hipónimos de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de hipónimos del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hyponyms", tags = "getHyponyms")
	@GetMapping(path = "/getHyponyms/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHyponyms/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHyponyms(@PathVariable("offset") long offset) throws JWNLException {
		List<Synset> hypernyms = wl.getHyponyms(offset);
		return hypernyms.toString();
	}

	/**
	 * Este método obtiene el listado de hipónimos de un synset dado su palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de hipónimos del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hyponyms", tags = "getHyponyms")
	@GetMapping(path = "/getHyponyms/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHyponyms/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHyponyms(@PathVariable("word") String word, @PathVariable("sense") int sense)
			throws JWNLException {
		List<Synset> hypernyms = wl.getHyponyms(word, sense, POS.NOUN);
		return hypernyms.toString();
	}

	/**
	 * Este método obtiene el listado de palabras hipónimas de un synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de palabras hipónimas del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hyponyms Words", tags = "getHyponyms")
	@GetMapping(path = "/getHyponymsWors/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHyponymsWors/{offset}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHyponymsWords(@PathVariable("offset") long offset) throws JWNLException {
		List<String> hyponyms = wl.getHypernymsWords(offset);
		return hyponyms.toString();
	}

	/**
	 * Este método obtiene el listado de palabras hipónimas de un synset dado su palabra y el número de significado que lo identifica
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de palabras hipónimas del synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Hyponyms Words", tags = "getHyponyms")
	@GetMapping(path = "/getHyponymsWords/{word}/{sense}",  produces = { "text/plain" })
	//@RequestMapping(value = "/getHyponymsWords/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getHyponymsWords(@PathVariable("word") String word, @PathVariable("sense") int sense)
			throws JWNLException {
		List<String> hyponyms = wl.getHypernymsWords(word, sense, POS.NOUN);
		return hyponyms.toString();
	}

	/**
	 * Este método comprueba si el synset es el nodo raíz de la ontología de Wordnet
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve true, si es el synset raíz, y false si no lo es
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Check if the entity node", tags = "checkIfEntityNode")
	@GetMapping(path = "/isEntityNode/{offset}",  produces = { "text/plain" })
	//@RequestMapping(value = "/isEntityNode/{offset}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public boolean isEntityNode(@PathVariable("offset") long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		return wl.isEntityNode(s);
	}

	/**
	 * Este método comprueba si el synset es el nodo raíz de la ontología de Wordnet
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve true, si es el synset raíz, y false si no lo es
	 */
	@ApiOperation(value = "Check if the entity node", tags = "checkIfEntityNode")
	@GetMapping(path = "/isEntityNode/{word}/{sense}",  produces = {"application/json" })
	//@RequestMapping(value = "/isEntityNode/{word}/{sense}", method = RequestMethod.GET, produces = {"application/json" })
	@ResponseBody
	public boolean isEntityNode(@PathVariable("word") String word, @PathVariable("sense") int sense) {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		return wl.isEntityNode(s);
	}

	/**
	 * Este método obtiene el listado de synsets desde el synset, identificado a través del offset que se recibe como parámetro, hasta la raíz
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de synsets que componen el camino hasta la raíz
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get List of Synsets to entity", tags = "getPathToEntity")
	@GetMapping(path = "/getPathToEntity/{offset}",  produces = {"application/json" })
	//@RequestMapping(value = "/getPathToEntity/{offset}", method = RequestMethod.GET, produces = { "application/json" })
	public String getPathToEntity(@PathVariable("offset") long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		List<String> synsets = wl.getListofSynsetsToString(wl.getPathToEntity(s));
		JSONArray jsArray = new JSONArray(synsets);
		return jsArray.toString();
	}

	/**
	 * Este método obtiene el listado de synsets desde el synset, identificado a través de la plabra y el número de significado que se reciben como parámetro y lo identifica, hasta la raíz
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el listado de synsets que componen el camino hasta la raíz
	 */
	@ApiOperation(value = "Get List of Synsets to entity", tags = "getPathToEntity")
	@GetMapping(path = "/getPathToEntity/{word}/{sense}",  produces = {"application/json" })
	//@RequestMapping(value = "/getPathToEntity/{word}/{sense}", method = RequestMethod.GET, produces = {"application/json" })
	public String getPathToEntity(@PathVariable("word") String word, @PathVariable("sense") int sense) {
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		List<String> synsets = wl.getListofSynsetsToString(wl.getPathToEntity(s));
		JSONArray jsArray = new JSONArray(synsets);
		return jsArray.toString();
	}

	/**
	 * Este método calcula el número de nodos desde el synset, identificado por el offset, hasta el synset raíz
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el número de nodos desde el synset hasta la raíz
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get number of nodes until entity", tags = "getNodesToEntity")
	@GetMapping(path = "/getNodesToEntity/{offset}",  produces = {"application/json" })
	//@RequestMapping(value = "/getNodesToEntity/{offset}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public int getNodesToEntity(@PathVariable("offset") long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		return wl.getNodesToEntity(s);
	}

	/**
	 * Este método calcula el número de nodos desde el synset, identificado por la palabra y el número de significado que lo identifica, hasta el synset raíz
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el número de nodos desde el synset hasta la raíz
	 */
	@ApiOperation(value = "Get number of nodes until entity", tags = "getNodesToEntity")
	@GetMapping(path = "/getNodesToEntity/{word}/{sense}",  produces = {"application/json" })
//	@RequestMapping(value = "/getNodesToEntity/{word}/{sense}", method = RequestMethod.GET, produces = { "text/plain" })
	public String getNodesToEntity(@PathVariable("word") String word, @PathVariable("sense") int sense){
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		return Integer.toString(wl.getNodesToEntity(s));
	}

	/**
	 * Este método obtiene el listado de nodos entre dos synsets identificados a través de sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @return Devuelve el listado de synsets que pertencen al camino entre ambos synsets 
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get path between two synsets", tags = "getPathBetweenSynsets")
	@GetMapping(path = "/getPathBetweenSynsets/{offset1}/{offset2}",  produces = {"application/json" })
	//@RequestMapping(value = "/getPathBetweenSynsets/{offset1}/{offset2}", method = RequestMethod.GET, produces = {"application/json" })
	public String getPathBetweenSynsets(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2) throws JWNLException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset1);
		List<String> synsets = wl.getListofSynsetsToString(wl.getPathBetweenSynsets(s1, s2));
		JSONArray jsArray = new JSONArray(synsets);
		return jsArray.toString();
	}

	/**
	 *  Este método obtiene el listado de nodos entre dos synsets identificados a través de sus palabras y sus números de significado respectivamente
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 - Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense1 - Número del significado del synset2
	 * @return  Devuelve el listado de synsets que pertencen al camino entre ambos synsets 
	 */
	@ApiOperation(value = "Get path between two synsets", tags = "getPathBetweenSynsets")
	@GetMapping(path = "/getPathBetweenSynsets/{word1}/{sense1}/{word2}/{sense2}",  produces = {"application/json" })
	//	@RequestMapping(value = "/getPathBetweenSynsets/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET, produces = {"application/json" })
	public String getPathBetweenSynsets(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1, @PathVariable("word2") String word2, @PathVariable("sense2") int sense2) {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		List<String> synsets = wl.getListofSynsetsToString(wl.getPathBetweenSynsets(s1, s2));
		JSONArray jsArray = new JSONArray(synsets);
		return jsArray.toString();
	}

	/**
	 * Este método calcula el número de enlaces entre synsets dados sus offsets
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única al Synset2
	 * @return Devuelve el número de enlaces entre los dos synsets
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get number of links between two synsets", tags = "getPathBetweenSynsets")
	@GetMapping(path = "/getNumLinksBetweenSynsets/{offset1}/{offset2}",  produces = {"text/plain" })
	//@RequestMapping(value = "/getNumLinksBetweenSynsets/{offset1}/{offset2}", method = RequestMethod.GET, produces = {"text/plain" })
	public String getNumLinksBetweenSynsets(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2) throws JWNLException {
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		return Integer.toString(wl.getNumLinksBetweenSynsets(s1, s2));
	}

	/**
	 * Este método calcula el número de enlaces entre synsets dados sus palabras y número de significados que los identifican
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return  Devuelve el número de enlaces entre los dos synsets
	 */
	@ApiOperation(value = "Get number of links between two synsets", tags = "getPathBetweenSynsets")
	@GetMapping(path = "/getNumLinksBetweenSynsets/{word1}/{sense1}/{word2}/{sense2}",  produces = {"text/plain" })
	//	@RequestMapping(value = "/getNumLinksBetweenSynsets/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET, produces = {"text/plain" })
	public String getNumLinksBetweenSynsets(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1, @PathVariable("word2") String word2, @PathVariable("sense2") int sense2) {
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		return Integer.toString(wl.getNumLinksBetweenSynsets(s1, s2));
	}

	/**
	 * Este método calcula la profundidad de un synset, identificado por su offset, dentro de la ontología de Wordnet
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el número de profundidad al que se encuentra el Synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get depth of a synset", tags = "getdepthOfSynset")
	@GetMapping(path = "/getdepthOfSynset/{offset}", produces = { "application/json" })
	//@RequestMapping(value = "/getdepthOfSynset/{offset}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	public int getdepthOfSynset(@PathVariable("offset") long offset) throws JWNLException {
		Synset s = wl.getSynset(offset);
		return wl.depthOfSynset(s);
	}

	/**
	 * Este método calcula la profundidad de un synset, identificado por su palabra y su número de significado, dentro de la ontología de Wordnet
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @return Devuelve el número de profundidad al que se encuentra el Synset
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get depth of a synset", tags = "getdepthOfSynset")
	@GetMapping(path = "/getdepthOfSynset/{word}/{sense}", produces = { "application/json" })
	//@RequestMapping(value = "/getdepthOfSynset/{word}/{sense}", method = RequestMethod.GET, produces = {"application/json" })
	@ResponseBody
	public int getdepthOfSynset(@PathVariable("word") String word, @PathVariable("sense") int sense){
		Synset s = wl.getSynset(word, POS.NOUN, sense);
		return wl.depthOfSynset(s);
	}

	/**
	 * Este método calcula el synset LCS entre dos synsets, identificados cada uno por su offset
	 * @param offset1 - Número de tipo long que identifica de manera única al Synset1
	 * @param offset2 - Número de tipo long que identifica de manera única a un Synset2
	 * @return Devuelve el offset del synset que es LCS de ambos
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get Least Common Subsummer", tags = "getLCS")
	@GetMapping(path = "/getLCS/{offset1}/{offset2}", produces = { "application/json" })
	//@RequestMapping(value = "/getLCS/{offset1}/{offset2}", method = RequestMethod.GET, produces = {"application/json" })
	public String getLCS(@PathVariable("offset1") long offset1, @PathVariable("offset2") long offset2)
			throws JWNLException {
		JSONObject json = new JSONObject();
		
		Synset s1 = wl.getSynset(offset1);
		Synset s2 = wl.getSynset(offset2);
		Synset lcs = wl.getLeastCommonSubsumer(s1, s2);
		
		json.put(jsonlabel, lcs);
		return json.toString();
	}

	/**
	 * Este método calcula el synset LCS entre dos synsets, identificados cada uno por su palabra y el número de su significado
	 * @param word1 - Palabra que identifica al synset1
	 * @param sense1 - Número del significado del synset1
	 * @param word2 - Palabra que identifica al synset2
	 * @param sense2 - Número del significado del synset2
	 * @return Devuelve el offset del synset que es LCS de ambos
	 */
	@ApiOperation(value = "Get Least Common Subsummer", tags = "getLCS")
	@GetMapping(path = "getLCS/{word1}/{sense1}/{word2}/{sense2}", produces = { "application/json" })
	//@RequestMapping(value = "/getLCS/{word1}/{sense1}/{word2}/{sense2}", method = RequestMethod.GET, produces = {"application/json" })
	public String getLCS(@PathVariable("word1") String word1, @PathVariable("sense1") int sense1,
			@PathVariable("word2") String word2, @PathVariable("sense2") int sense2) {
		JSONObject json = new JSONObject();
		
		Synset s1 = wl.getSynset(word1, POS.NOUN, sense1);
		Synset s2 = wl.getSynset(word2, POS.NOUN, sense2);
		Synset lcs = wl.getLeastCommonSubsumer(s1, s2);
		
		json.put(jsonlabel, lcs);
		return json.toString();
	}

	/**
	 * 
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @param depth
	 * @return
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@PostMapping(path = "/getSubtreeByOffset/{offset}/{depth}", produces = { "application/json" })
	//@RequestMapping(value = "/getSubtreeByOffset/{offset}/{depth}", method = RequestMethod.POST, produces = {"application/json" })
	public String getSubtreeByOffset(@PathVariable("offset") long offset, @PathVariable("depth") int depth)
			throws JWNLException{
		Synset s1 = wl.getSynset(offset);
		LinkedHashMap<Synset, List<Synset>> tree = new LinkedHashMap<>();
		tree = wl.getSubarbolWordnet(s1, depth, tree);
		JSONArray jsonarray = new JSONArray();
		Set<Synset> set = tree.keySet();
		for (Synset s : set) {

			List<Synset> hijos = tree.get(s);
			JSONObject jsonnode = new JSONObject();
			JSONArray hijosjson = new JSONArray();
			for (int i = 0; i < hijos.size(); i++) {
				hijosjson.put(hijos.get(i).getOffset());
			
			}

			jsonnode.put("keysynset", s.getOffset());
			jsonnode.put("hijos", hijosjson);
			jsonarray.put(jsonnode);

		}
		return jsonarray.toString();
	}

	/**
	 * Este método calcula el subárbol para Wordnet dado el offset de la raíz y la profundidad hasta la que se desea calcular el subárbol
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @param depth - Profundidad  hasta la que se desea calcular el subárbol
	 * @return Devuelve el subárbol de Wordnet resultante
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@PostMapping(path = "/getSubtree/{offset}/{depth}", produces = { "application/json" })
	//	@RequestMapping(value = "/getSubtree/{offset}/{depth}", method = RequestMethod.POST, produces = {"application/json" })
	public String getSubtree(@PathVariable("offset") long offset, @PathVariable("depth") int depth)
			throws JWNLException{
		Synset s1 = wl.getSynset(offset);
		LinkedHashMap<Synset, List<Synset>> tree = new LinkedHashMap<>();
		tree = wl.getSubarbolWordnet(s1, depth, tree);
		JSONArray jsonarray = new JSONArray();
		Set<Synset> set = tree.keySet();
		for (Synset s : set) {
			JSONObject synsetpair = new JSONObject();
			List<Synset> hijos = tree.get(s);
			JSONObject jsonhijos = new JSONObject();

			for (int i = 0; i < hijos.size(); i++) {
				jsonhijos.put(Integer.toString(i), hijos.get(i));
			}
		
			synsetpair.put(s.toString(), jsonhijos);
			jsonarray.put(synsetpair);

		}
		return jsonarray.toString();
	}

	/**
	 * Este método calcula el subárbol para Wordnet dada la palabra y el número de significado del synset raíz y la profundidad hasta la que se desea calcular el subárbol
	 * @param word - Palabra que identifica al synset
	 * @param sense - Número del significado del synset
	 * @param depth - Profundidad  hasta la que se desea calcular el subárbol
	 * @return  Devuelve el subárbol de Wordnet resultante
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get SubTree of Wordnet", tags = "getSubtree")
	@PostMapping(path = "/getSubtree/{word}/{sense}/{depth}", produces = { "application/json" })
	//@RequestMapping(value = "/getSubtree/{word}/{sense}/{depth}", method = RequestMethod.POST, produces = {"application/json" })
	public String getSubtree(@PathVariable("word") String word, @PathVariable("sense") int sense,
			@PathVariable("depth") int depth) throws JWNLException{
		long offset = wl.getSynset(word, POS.NOUN, sense).getOffset();
		return getSubtree(offset, depth);
	}

	/**
	 * Este método calcula la profundidad del synset, identificado por el offset, dentro del subárbol de Wordnet
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @param treeResult - Subárbol de Wordnet
	 * @return Devuelve el valor de la profundida del synset dentro del subárbol
	 * @throws JWNLException
	 */
	@ApiOperation(value = "Get depth of a synset in a subtree", tags = "getdepthOfSynset")
	@PostMapping(path = "/getdepthOfSynsetinSubtree/{offset}", produces = { "application/json" })
	//@RequestMapping(value = "/getdepthOfSynsetinSubtree/{offset}", method = RequestMethod.POST, consumes = {"application/json" })
	@ApiImplicitParam(name = "offset", value = "offset", dataType = DataType.LONG, paramType = ParamType.PATH)
	@ResponseBody
	public int getdepthOfSynsetinSubtree(@PathVariable("offset") Long offset, @RequestBody TreeWordnet treeResult)
			throws JWNLException {

		LinkedHashMap<Synset, List<Synset>> tree = wl.convertTreeToMap(treeResult);
		Synset n = wl.getSynset(offset.longValue());
		return wl.depthOfSynset(n, tree);
	}

}
