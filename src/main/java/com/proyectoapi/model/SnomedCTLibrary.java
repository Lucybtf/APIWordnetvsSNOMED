package com.proyectoapi.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mays.snomed.ConceptBasic;
import com.mays.snomed.ConceptDescription;
import com.mays.snomed.SnomedIsa;
import com.mays.util.Sql;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Synset;

/**
 * Class SnomedCTLibrary: Esta clase gestiona la interacción con la base de datos de Wordnet.
 * @author Lucía Batista Flores
 * @version 1.0
 */
public class SnomedCTLibrary {

	private SnomedIsa snomedBD;

	/**
	 * Constructor de la clase que interacciona con Snomed CT
	 */
	public SnomedCTLibrary() {
		try {
			
			Connection sql = Sql.getConnection("./ddb", "snomed-test");
			snomedBD = new SnomedIsa();
			snomedBD.init(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este método permite obtener el concepto de la ontología de Snomed dado su id
	 * @param idConcept - Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve la cadena del nombre del concepto
	 */
	public String getConcept(long idConcept) {
		return snomedBD.getConcept(idConcept).toString();
	}

	/**
	 * Este método permite obtener el listado de palabras que representan a los conceptos sinónimos
	 * @param idConcept - Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve el listado de cadenas que representan a los sinónimos del concepto recibido como parámetro
	 */
	public List<String> getSynonymsConcepts(long idConcept) {
		ArrayList<String> synonyms = new ArrayList<>();
		ArrayList<ConceptDescription> descriptions;
		try {
			descriptions = snomedBD.getDescriptions(idConcept);
		
			for (ConceptDescription s : descriptions) {
				if (s.getDescriptionStatus() == 0)
					synonyms.add(s.getTerm());
			}
			return synonyms;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return synonyms;
	}

	/**
	 *  Este método permite obtener el listado de identificadores que representan a los conceptos sinónimos
	 * @param idConcept -  Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve el listado de identificadores de los conceptos sinónimos
	 * @throws Exception
	 */
	public List<Long> getSynonymsIdConcepts(long idConcept) throws Exception {
		ArrayList<Long> synonyms = new ArrayList<>();

		ArrayList<ConceptDescription> descriptions = snomedBD.getDescriptions(idConcept);
		for (ConceptDescription s : descriptions) {
			if (s.getDescriptionStatus() == 0)
				synonyms.add(s.getDescriptionId());
		}
		return synonyms;
	}

	/**
	 * Este método permite obtener el listado de palabras que representan a los conceptos hiperónimos
	 * @param idConcept - Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve el listado de cadenas que representan los conceptos hiperónimos
	 */
	public List<String> getHypernymsConcepts(long idConcept) {
		ArrayList<String> hypernymsconcepts = new ArrayList<>();
		ArrayList<Long> hypernyms = snomedBD.getParents(idConcept);
		for (int i = 0; i < hypernyms.size(); i++) {
			long currentid = hypernyms.get(i);
			hypernymsconcepts.add(snomedBD.getConcept(currentid).toString());
		}
		return hypernymsconcepts;
	}
	
	/**
	 * Este método permite obtener el listado de identificadores que representan a los conceptos hiperónimos
	 * @param idConcept - Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve el listado de identificadores de los conceptos hiperónimos
	 */
	public List<Long> getHypernymsIdConcepts(long idConcept) {
		return snomedBD.getParents(idConcept);
	}

	/**
	 * Este método permite obtener el listado de palabras que representan a los conceptos hipónimos
	 * @param idConcept - Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve el listado de cadenas que representan los conceptos hipónimos
	 */
	public List<String> getHyponymsConcepts(long idConcept) {
		ArrayList<String> hyponyms = new ArrayList<>();
		ArrayList<Long> hyponymsconcepts = snomedBD.getChildren(idConcept);
		for (int i = 0; i < hyponymsconcepts.size(); i++) {
			hyponyms.add(snomedBD.getConcept(hyponymsconcepts.get(i)).toString());
		}
		return hyponyms;
	}

	/**
	 * Este método permite obtener el listado de identificadores que representan a los conceptos hipónimos
	 * @param idConcept - Número de tipo long que identifica de manera única a un Concepto
	 * @return Devuelve el listado de identificadores de los conceptos hipónimos
	 */
	public List<Long> getHyponymsIdConcepts(long idConcept) {
		return snomedBD.getChildren(idConcept);
	}

	public List<Long> getCohyponyms(long offset){
		List<Long> fatherList;	
		List<Long> childList = new ArrayList<>();	
		
		fatherList = getHypernymsIdConcepts(offset);
		for (Long father: fatherList){
			childList.addAll(getHyponymsIdConcepts(father));
		}
		return childList;
	}
	
	/**
	 * Este método permite obtener el número de conceptos existentes entre dos conceptos de la ontología
	 * @param idConcept1 - Número de tipo long que identifica de manera única al Concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al Concepto2
	 * @return Devuelve el número de conceptos entre los dos conceptos proporcionados como parámetros
	 */
	public int getPathLength(long idConcept1, long idConcept2) {
		HashMap<Long, Integer> id1 = getAncestorsDistanciaMinima(idConcept1);
		HashMap<Long, Integer> id2 = getAncestorsDistanciaMinima(idConcept2);
		if (id1.size() > id2.size()) {
			HashMap<Long, Integer> ancestresTemporal = id1;
			id1 = id2;
			id2 = ancestresTemporal;
		}
		int distance = Integer.MAX_VALUE;
		Set<Long> setnodes = id1.keySet();
		for (Long i : setnodes) {
			if (id2.containsKey(i) && distance > id1.get(i) + id2.get(i)) {
				distance = id1.get(i) + id2.get(i);
			}
		}
		return distance;
	}
	
	/**
	 * Este método permite obtener el listado de ancestros del concepto
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @return Devuelve el listado de identificadores de los ancestros
	 */
	public List<Long> getAncestors(long idConcept) {
		return snomedBD.getAncestors(idConcept);
	}

	/**
	 * 	Este método permite obtener el listado de descendientes del concepto
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @return Devuelve el listado de identificadores de los descendientes
	 */
	public List<Long> getDescendants(long idConcept) {
		return snomedBD.getDescendants(idConcept);
	}
	
	/**
	 * Este método permite obtener los anscestros y la distancia mínima a cada ancestro de un concepto 
	 * @param idConcept  - Número de tipo long que identifica de manera única al Concepto
	 * @return Devuelve el listado de identificadores  de los ancestros, junto con la distancia mínima a dicho ancestro
	 */
	public HashMap<Long, Integer> getAncestorsDistanciaMinima(long idConcept) {

		int distancia = 0;
		HashMap<Long, Integer> result = new HashMap<>();
		Set<Long> parents = new HashSet<>();
		result.put(idConcept, 0);
		parents.addAll(snomedBD.getParents(idConcept));
		while (parents.isEmpty()) {
			distancia++;
			Set<Long> grandparents = new HashSet<>();
			for (Long idPare : parents) {
				if (!result.containsKey(idPare))
					result.put(idPare, distancia);
				grandparents.addAll(snomedBD.getParents(idPare));
			}
			parents = grandparents;
			grandparents.clear();
		}
		return result;
	}

	/**
	 * Este método obtiene el número de ancestros máximos hasta la raíz
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @return Devuelve el número de ancestros máximos hasta la raíz de Snomed
	 */
	public int getAncestorsMaxDistance(long idConcept) {

		if(snomedBD.getConcept(idConcept)!=null) {
		int caminoMaximo = 0;
		Set<Long> parents = new HashSet<>();
		Set<Long> grandparents = new HashSet<>();
		Set<Long> conjuntTemporal = null;
		parents.addAll(snomedBD.getParents(idConcept));

		while (parents.isEmpty()) {
			caminoMaximo++;
			for (Long idPare : parents)
				grandparents.addAll(snomedBD.getParents(idPare));
			conjuntTemporal = parents;
			parents = grandparents;
			grandparents = conjuntTemporal;
			grandparents.clear();
		}

		return caminoMaximo + 1;
		}
		else return -1;
	}
	
	/*Pendiente*/
	public List<Long> getPathEntityConcept(long id) {
		return snomedBD.getAncestors(id);
	}

	/**
	 * Este método obtiene el identificador del LCS de dos conceptos de la ontología
	 * @param idConcept1 - Número de tipo long que identifica de manera única al Concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al Concepto2
	 * @return Devuelve el identificador del concepto LCS
	 */
	public Long getLCS(long idConcept1, long idConcept2) {
		Map<Long, Integer> id1 = getAncestorsDistanciaMinima(idConcept1);
		Map<Long, Integer> id2 = getAncestorsDistanciaMinima(idConcept2);
		
		if (id1.size() > id2.size()) {
			Map<Long, Integer> ancestresTemporal = id1;
			id1 = id2;
			id2 = ancestresTemporal;
		}
		long idLCS = -1;
		int distanciaLCS = Integer.MAX_VALUE;
		Set<Long> set = id1.keySet();
		for (Long id : set)
			if (id2.containsKey(id) && (distanciaLCS > id1.get(id) + id2.get(id))) {
				idLCS = id;
				distanciaLCS = id1.get(id) + id2.get(id);
			}
		if (idLCS == -1)
			return null;
		return idLCS;
	}

	/**
	 * Este método calcula el número de enlaces entre 2 conceptos de la ontología
	 * @param idConcept1 - Número de tipo long que identifica de manera única al Concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al Concepto2
	 * @return Devuelve el número de enlaces entre 2 conceptos de la ontología
	 */
	public int getNumLinksBetweenConcepts(long idConcept1, long idConcept2) {
		return getPathLength(idConcept1, idConcept2) - 1;
	}
	
	/**
	 * Este método calcula el número de nodos hasta la entidad
	 * @param idConcept -  Número de tipo long que identifica de manera única al Concepto
	 * @return Devuelve el número de nodo hacia la entidad
	 */
	public int getNumNodestoEntityConcept(long idConcept) {
		long entity = 138875005;
		return getPathLength(entity, idConcept);
	}

	/**
	 * Este método permite obtener el subárbol completo desde un concepto de la ontología
	 * @param idConceptroot - Número de tipo long que identifica de manera única al Concepto raíz del subárbol
	 * @return Devuelve el Map que representa al subárbol
	 */
	public LinkedHashMap<Long, Integer> getSubTreeSnomed(long idConceptroot){

		List<Long> nodes = snomedBD.getDescendants(idConceptroot);
		LinkedHashMap<Long, Integer> subgraph = new LinkedHashMap<>();
		int depth = 0;
		subgraph.put(idConceptroot, 0);
		depth++;
		
		List<Long> nodeslevel = new ArrayList<>();
		nodeslevel.add(idConceptroot);
		while (nodes.isEmpty()) {
			nodeslevel= getSubnodes(nodeslevel);
			for (Long n : nodeslevel) {
				subgraph.put(n, depth);
				nodes.remove(n);
			}
			depth++;
		}
		return subgraph;
	}
	
	/**
	 * Este método obtiene los subnodos del subárbol
	 * @param nodeslevel - Listado de identificadores únicos de los conceptos hijos del subábol
	 * @param depth - Profundidad para la que se esta calculando
	 * @return Devuelve el listado de identificadores con los nodos de la profundidad correspondiente
	 */
	private List<Long> getSubnodes(List<Long> nodeslevel) {

		ArrayList<Long> list = new ArrayList<>();
		for (Long son : nodeslevel) {
			list.addAll(snomedBD.getChildren(son));
		}
		return list;
	}
	
	/**
	 * Este método permite obtener el subárbol para una determinada altura
	 * @param idConceptroot- Número de tipo long que identifica de manera única al Concepto raíz del subárbol
	 * @param level - Nivel de altura para el que se desea calcular el subárbol
	 * @return Devuelve el Map que representa al subárbol
	 */
	public LinkedHashMap<Long, Integer> getSubTreeSnomed(long idConceptroot, int level) {

		LinkedHashMap<Long, Integer> subgraph = new LinkedHashMap<Long, Integer>();
		int depth = 0;
		subgraph.put(idConceptroot, 0);
		depth++;
		level--;
		List<Long> nodes = new ArrayList<>();
		nodes.add(idConceptroot);
		while (level >= 0) {
			nodes = getSubnodes(nodes);
			for (Long n : nodes)
				subgraph.put(n, depth);
			depth++;
			level--;
		}
		return subgraph;
	}


	/**
	 * Este método permite realizar la conversión entre el tipo GraphSnomed al tipo LinkedHashMap
	 * @param graph - Objecto de tipo GraphSnomed
	 * @return Devuelve el LinkedHashMap que ofrece como resultado el subárbol
	 */
	public LinkedHashMap<Long, Integer> getSubTreeSnomedConversion(TreeSnomedCT graph) {
		LinkedHashMap<Long, Integer> graphlinkedlist = new LinkedHashMap<>();
		ArrayList<NodeSnomedCT> listanodos = graph.getNodes();
		for (int i = 0; i < listanodos.size(); i++) {
			graphlinkedlist.put(listanodos.get(i).getId(), listanodos.get(i).getLevel());
		}
		return graphlinkedlist;
	}


	/**
	 * Este método obtiene los ancestros del subárbol de Snomed CT
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuele el listado de identificadores de los conceptos anscestros del nodo del subárbol
	 */
	public List<Long> getAncestorsSubTreeSnomed(long idConcept, LinkedHashMap<Long, Integer> subtree) {
		ArrayList<Long> ancestors = new ArrayList<>();

			if (subtree.containsKey(idConcept)) {
				int level = subtree.get(idConcept);
				Set<Long> subtreenodes = subtree.keySet();
				for (Long a : subtreenodes) {
					if (subtree.get(a) <= level)
						ancestors.add(a);
				}
		}
		return ancestors;
	}

	/**
	 * Este método obtiene los descendientes del subárbol de Snomed CT
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuele el listado de identificadores de los conceptos anscestros del nodo del subárbol
	 */
	public List<Long> getDescendantsSubTreeSnomed(long idConcept, LinkedHashMap<Long, Integer> subtree) {
		List<Long> descendants = new ArrayList<>();
		if (subtree.containsKey(idConcept)) {
			int level = subtree.get(idConcept);
			Set<Long> subtreenodes = subtree.keySet();
			for (Long a : subtreenodes) {
				if (subtree.get(a) > level)
					descendants.add(a);
			}
		}
		return descendants;
	}

	/**
	 * Este método devuelve la distancia máxima hasta la raíz de Snomed CT
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el número de nodos de distancia hasta la raíz
	 */
	public int getMaxDistanceToEntitySubTreeSnomed(long idConcept, LinkedHashMap<Long, Integer> subtree) {

		int caminoMaximo = 0;
		Set<Long> parents = new HashSet<>();
		Set<Long> grandparents = new HashSet<>();
		Set<Long> conjuntTemporal = null;

		parents.addAll(getParentsSubTreeSnomed(idConcept, subtree));
		while (parents.isEmpty()) {
			caminoMaximo++;
			for (Long idparents : parents) {
				ArrayList<Long> abuelos = getParentsSubTreeSnomed(idparents, subtree);
				grandparents.addAll(abuelos);
			}	
			conjuntTemporal = parents;
			parents = grandparents;
			grandparents = conjuntTemporal;
			grandparents.clear();
		}

		return caminoMaximo; 
	}

	/**
	 * Este método permite obtener los padres del concepto del subárbol
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el listado de identificadores de los conceptos padres del concepto que recibe como parámetro
	 */
	private ArrayList<Long> getParentsSubTreeSnomed(long idConcept, LinkedHashMap<Long, Integer> subtree) {
		ArrayList<Long> parents = snomedBD.getParents(idConcept);
		ArrayList<Long> parentsSubTree = new ArrayList<>();

		if (parents.isEmpty()) {
			for (Long p : parents) {
				if (subtree.containsKey(p))
					parentsSubTree.add(p);
			}
		}
		return parentsSubTree;
	}

	/**
	 * Este método permite calcular los ancestros de un concepto y la distancia mínima a cada uno de ellos dentro del subárbol
	 * @param idConcept - Número de tipo long que identifica de manera única al Concepto
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve un Map con el listado de ancestros y la distancia mínima al concepto percibido como parámetro
	 */
	public HashMap<Long, Integer> getAncestrosDistanciaMinimaSubTreeSnomed(long idConcept,
			LinkedHashMap<Long, Integer> subtree) {
		HashMap<Long, Integer> ancestorsmin = new HashMap<>();
		HashMap<Long, Integer> ancestors =  getAncestorsDistanciaMinima(idConcept);
		Set<Long> ancestorsnode = ancestors.keySet();
		for(Long num: ancestorsnode) {
			if(subtree.containsKey(num))
				ancestorsmin.put(num, ancestors.get(num));
		}
		return ancestorsmin;
	}
	
	/**
	 * Este método devuelve el número de elementos del primer conjunto que no se encuentran en el segundo conjunto
	 * @param a - Conjunto de Synsets
	 * @param b - Conjunto de Synsets
	 * @return Devuelve el número de elementos del primer conjunto que no se encuentran en el segundo conjunto
	 */
	public int NotContainsFirstInSecond(Set<Long> a, Set<Long> b) {
		int num = 0;
		for (Long syn : a) {

			if (!b.contains(syn)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Este método permite obtener el número de elementos en la intersección entre los dos conjuntos
	 * @param a - Conjunto de Synsets
	 * @param b - Conjunto de Synsets
	 * @return Devuelve el número de elementos en la intersección entre ambos conjuntos
	 */
	public int Intersection(Set<Long> a, Set<Long> b) {
		int num = 0;
		for (Long syn : a) {

			if (b.contains(syn)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Este método permite obtener el objeto json resultante que representa el subárbol de Snomed CT
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el objeto json resultante
	 */
	public JSONObject getSubTreeSnomedJSON(Map<Long, Integer> subtree) {

		JSONArray jsonarray = new JSONArray();
		JSONObject jresult = new JSONObject();
		Set<Long> nodes = subtree.keySet();
		for (Long i : nodes) {
			JSONObject json = new JSONObject();
			json.put("id", i);
			json.put("level", subtree.get(i));
			jsonarray.put(json);
		}
		return jresult.put("nodes", jsonarray);
	}

}