package com.proyectoapi.controllersnomed;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.proyectoapi.model.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Class SnomedCTLibraryController: Esta clase es el controlador para Snomed CT
 * @author Lucía Batista Flores
 * @version 1.0
 */

@Api(value = "SnomedRestController")
@RestController
public class SnomedCTLibraryController {

	private SnomedCTLibrary snomedc = new SnomedCTLibrary();

	/**
	 * Este método obtiene la cadena que identifica al concepto de Snomed CT dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve la cadena que identifica al concepto con ese id
	 */
	@ApiOperation(value = "Get Concept by Id", tags = "getConcept")
	@GetMapping(path = "/getConcept/{id}")
//	@RequestMapping(value = "/getConcept/{id}", method = RequestMethod.GET)
	public String getConcept(@PathVariable("id") long idConcept) {
		return snomedc.getConcept(idConcept);
	}

	/**
	 * Este método contiene el listado de palabras sinónimas de un concepto en Snomed Ct dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de palabras sinónimas del concepto
	 */
	@ApiOperation(value = "Get Synonyms Strings by Id", tags = "getSynonyms")
	@GetMapping(path = "/getSynonymsConcepts/{id}")
//	@RequestMapping(value = "/getSynonymsConcepts/{id}", method = RequestMethod.GET)
	public List<String> getSynonymsConcepts(@PathVariable("id") long idConcept) {
		return snomedc.getSynonymsConcepts(idConcept);
	}

	/**
	 * Este método contiene el listado de identificadores de los sinónimos de un concepto en Snomed Ct dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de identificadores de conceptos sinónimos del concepto
	 * @throws Exception
	 */
	@ApiOperation(value = "Get Synonyms Ids by Id", tags = "getSynonyms")
	@GetMapping(path = "/getSynonymsIdConcepts/{id}")
	//@RequestMapping(value = "/getSynonymsIdConcepts/{id}", method = RequestMethod.GET)
	public List<Long> getSynonymsIdConcepts(@PathVariable("id") long idConcept) throws Exception {
		return snomedc.getSynonymsIdConcepts(idConcept);
	}

	/**
	 * Este método contiene el listado de palabras hiperónimas de un concepto en Snomed Ct dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de palabras hiperónimas del concepto
	 */
	@ApiOperation(value = "Get Hypernyms Strings by Id", tags = "getHypernyms")
	@GetMapping(path = "/getHypernymsConcepts/{id}")
	//@RequestMapping(value = "/getHypernymsConcepts/{id}", method = RequestMethod.GET)
	public List<String> getHypernymsConcepts(@PathVariable("id") long idConcept) {
		return snomedc.getHypernymsConcepts(idConcept);
	}

	/**
	 * Este método contiene el listado de identificadores de los hiperónimos de un concepto en Snomed CT dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de identificadores de los sinónimos del concepto
	 */
	@ApiOperation(value = "Get Hypernyms Ids by Id", tags = "getHypernyms")
	@GetMapping(path = "/getHypernymsIdConcepts/{id}")
	//@RequestMapping(value = "/getHypernymsIdConcepts/{id}", method = RequestMethod.GET)
	public List<Long> getHypernymsIdConcepts(@PathVariable("id") long idConcept) {
		return snomedc.getHypernymsIdConcepts(idConcept);
	}

	/**
	 * Este método  contiene el listado de palabras hipónimas de un concepto en Snomed CT dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de palabras hipónimas de un concepto
	 * @throws Exception
	 */
	@ApiOperation(value = "Get Hyponyms Strings by Id", tags = "getHyponyms")
	@GetMapping(path = "/getHyponymsConcepts/{id}")
	//@RequestMapping(value = "/getHyponymsConcepts/{id}", method = RequestMethod.GET)
	public List<String> gettHyponymsConcept(@PathVariable("id") long idConcept){
		return snomedc.getHyponymsConcepts(idConcept);
	}

	/**
	 * Este método contiene el listado de identificadores de los hipónimos de un concepto en Snomed CT
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de identificadores de los hipónimos del concepto
	 * @throws Exception
	 */
	@ApiOperation(value = "Get Hyponyms Ids by Id", tags = "getHyponyms")
	@GetMapping(path = "/getHyponymsIdConcepts/{id}")
	//@RequestMapping(value = "/getHyponymsIdConcepts/{id}", method = RequestMethod.GET)
	public List<Long> gettHyponymsIdConcept(@PathVariable("id") long idConcept) {
		return snomedc.getHyponymsIdConcepts(idConcept);

	}

	/**
	 * Este método obtiene el listado de identificadores hasta la raíz
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de identicadores ancestros hasta la raíz
	 */
	@ApiOperation(value = "Get Path to Entity Concept", tags = "getPathToEntity")
	@GetMapping(path = "/getPathEntityConcept/{id}")
	//@RequestMapping(value = "/getPathEntityConcept/{id}", method = RequestMethod.GET)
	public List<Long> getPathEntityConcept(@PathVariable("id") long idConcept) {
		return snomedc.getPathEntityConcept(idConcept);
	}

	/**
	 * Este método obtiene el listado de conceptos ancestros a dicho concepto y la distancia mínima del concepto a cada anscetro
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de pares de ancestros, cada par contiene un concepto ancestro y la distancia mínima del concepto a dicho ancestro
	 */
	@ApiOperation(value = "Get Ancestros Distancia Minima Concept", tags = "getAncestrosDistanciaMinima")
	@GetMapping(path = "/getAncestrosDistanciaMinima/{id}")
	//@RequestMapping(value = "/getAncestrosDistanciaMinima/{id}", method = RequestMethod.GET)
	public HashMap<Long, Integer> getAncestrosDistanciaMinima(long idConcept) {
		return snomedc.getAncestorsDistanciaMinima(idConcept);
	}

	/**
	 * Este método contiene el número de enlaces entre conceptos
	 * @param idConcept1 -  Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 -  Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el número de enlaces entre ambos conceptos
	 */
	@ApiOperation(value = "Get Num Links Between Concepts", tags = "getNumLinksBetweenConcepts")
	@GetMapping(path = "/getNumLinksBetweenConcepts/{id}/{id2}")
//	@RequestMapping(value = "/getNumLinksBetweenConcepts/{id}/{id2}", method = RequestMethod.GET)
	public int getNumLinksBetweenConcepts(@PathVariable("id") long idConcept1, @PathVariable("id2") long idConcept2) {
		return snomedc.getNumLinksBetweenConcepts(idConcept1, idConcept2);
	}

	/**
	 * Este método obtiene el listado de ancestros de un concepto
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de ancestros del concepto que recibe como parámetro
	 */
	@ApiOperation(value = "Get Ancestors of a Concept", tags = "getAncestors")
	@GetMapping(path = "/getAncestors/{id}")
//	@RequestMapping(value = "/getAncestors/{id}", method = RequestMethod.GET)
	public List<Long> getAncestors(long idConcept) {
		return snomedc.getAncestors(idConcept);
	}

	/**
	 * Este método obtiene el listado de descendientes de un concepto
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el listado de descendientes del concepto que recibe como parámetro
	 */
	@ApiOperation(value = "Get Descendants of a Concept", tags = "getDescendants")
	@GetMapping(path = "/getDescendants/{id}")
	//@RequestMapping(value = "/getDescendants/{id}", method = RequestMethod.GET)
	public List<Long> getDescendants(long idConcept) {
		return snomedc.getDescendants(idConcept);
	}

	/**
	 * Este método devuelve el identificador del concepto LCS de dos conceptos de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor del identificador del concepto LCS
	 */
	@ApiOperation(value = "Get LCS Concept", tags = "getLCSConcept")
	@GetMapping(path = "/getLCSConcept/{id}/{id2}")
	//@RequestMapping(value = "/getLCSConcept/{id}/{id2}", method = RequestMethod.GET)
	public Long getLCS(long idConcept1, long idConcept2) {
		return snomedc.getLCS(idConcept1, idConcept2);
	}

	/**
	 * 
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param level - Nivel de profundidad hasta el que se desea calcular el subárbol
	 * @return Devuelve el subárbol de Snomed hasta dicha profundidad si el subárbol cuenta con esa profunidad
	 */
	@ApiOperation(value = "Get SubTree of Snomed CT", tags = "getSubTree")
	@GetMapping(path = "/getSubTree/{idroot}/{level}")
	//@RequestMapping(value = "/getSubTree/{idroot}/{level}", method = RequestMethod.GET)
	public String getSubTree(long idConceptroot, int level) {

		Map<Long, Integer> subtree= snomedc.getSubTreeSnomed(idConceptroot, level);
		return snomedc.getSubTreeSnomedJSON(subtree).toString();
	}
}
