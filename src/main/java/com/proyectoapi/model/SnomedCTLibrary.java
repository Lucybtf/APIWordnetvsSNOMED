package com.proyectoapi.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mays.snomed.ConceptBasic;
import com.mays.snomed.ConceptDescription;
import com.mays.snomed.SnomedIsa;
import com.mays.util.Sql;

public class SnomedCTLibrary {

	private SnomedIsa snomedBD;

	public SnomedCTLibrary() {
		try {
			// Resource resource = new ClassPathResource("ddb");
			// System.out.print("PATH"+resource.getURL()+"\n"+resource.getURI().getPath()+"\n");
			Connection sql = Sql.getConnection("./ddb", "snomed-test");
			// Connection sql =Sql.getConnection("jdbc:derby:/snomed-test");
			snomedBD = new SnomedIsa();
			snomedBD.init(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getConcept(long idConcept) {
		return snomedBD.getConcept(idConcept).toString();
	}

	public ArrayList<String> getSynonymsConcepts(long idConcept) throws Exception {
		ArrayList<String> synonyms = new ArrayList<String>();

		ArrayList<ConceptDescription> descriptions = snomedBD.getDescriptions(idConcept);
		for (ConceptDescription s : descriptions) {
			if (s.getDescriptionStatus() == 0)
				// System.out.print("RELATION->"+s.getTerm()+""+s.getDescriptionStatus()+"\n");
				synonyms.add(s.getTerm());
			// System.out.print("SYNONYMS ID"+s.getConceptId());
		}
		return synonyms;
	}

	public ArrayList<Long> getSynonymsIdConcepts(long idConcept) throws Exception {
		ArrayList<Long> synonyms = new ArrayList<Long>();

		ArrayList<ConceptDescription> descriptions = snomedBD.getDescriptions(idConcept);
		for (ConceptDescription s : descriptions) {
			if (s.getDescriptionStatus() == 0)
				// System.out.print("RELATION->"+s.getTerm()+""+s.getDescriptionStatus()+"\n");
				synonyms.add(s.getConceptId());
			System.out.print("SYNONYMS ID" + s.getConceptId());
		}
		return synonyms;
	}

	public ArrayList<String> getHypernymsConcepts(long idConcept) {
		ArrayList<String> hypernymsconcepts = new ArrayList<String>();
		ArrayList<Long> hypernyms = new ArrayList<Long>();
		hypernyms = snomedBD.getParents(idConcept);
		for (int i = 0; i < hypernyms.size(); i++) {
			long currentid = hypernyms.get(i);
			ConceptBasic currentC = snomedBD.getConcept(currentid);
			System.out.print("\n\nCONCEPT" + snomedBD.getConcept(currentid) + "\n");
			hypernymsconcepts.add(snomedBD.getConcept(currentid).toString());
		}
		return hypernymsconcepts;
	}

	public ArrayList<Long> getHypernymsIdConcepts(long idConcept) {
		return snomedBD.getParents(idConcept);
	}

	public ArrayList<String> getHyponymsConcepts(long idConcept) {
		ArrayList<String> hyponyms = new ArrayList<String>();
		ArrayList<Long> hyponymsconcepts = snomedBD.getChildren(idConcept);
		for (int i = 0; i < hyponymsconcepts.size(); i++) {
			hyponyms.add(snomedBD.getConcept(hyponymsconcepts.get(i)).toString());
		}
		return hyponyms;
	}

	public ArrayList<Long> getHyponymsIdConcepts(long idConcept) {
		return snomedBD.getChildren(idConcept);

	}

	/*
	 * public ArrayList<String> getMeronymsConcept(long idConcept) throws Exception{
	 * ArrayList<String> meronyms = new ArrayList<String>();
	 * ArrayList<ConceptRelationship> relationships =
	 * snomedBD.getRelationships(idConcept); System.out.print("RELATIONS"
	 * +relationships); for(ConceptRelationship rel:relationships) {
	 * System.out.print("\n\nRelation:"+rel.getRelationshipId()+" "+rel.
	 * getRelationshipName()+rel.getRelationshipType()+" "+rel.getConceptId2()); }
	 * return meronyms; }
	 * 
	 * public ArrayList<String> getHolonymsConcept(long idConcept){
	 * ArrayList<String> holonyms = new ArrayList<String>(); return holonyms; }
	 */

	/*
	 * public ArrayList<String> pathToEntityConcept(long idConcept){
	 * ArrayList<String> path = new ArrayList<String>(); ArrayList<Long> parentnodes
	 * = new ArrayList<Long>(); parentnodes.addAll(snomedBD.getParents(idConcept));
	 * if(parentnodes.size()>0) { for(Long id:parentnodes) { //parentnodes.addAll()
	 * } }
	 * 
	 * return path; }
	 */
	public ArrayList<Long> getPathEntityConcept(long id) {
		// System.out.print(snomedBD.getAncestors(id).size());
		return snomedBD.getAncestors(id);
	}

	public HashMap<Long, Integer> getAncestrosDistanciaMinima(long idConcept) {

		int distancia = 0;
		HashMap<Long, Integer> result = new HashMap<Long, Integer>();
		Set<Long> parents = new HashSet<Long>();
		Set<Long> conjuntTemporal;

		result.put(idConcept, 0);
		parents.addAll(snomedBD.getParents(idConcept));
		while (parents.size() > 0) {
			distancia++;
			Set<Long> grandparents = new HashSet<Long>();
			for (Long idPare : parents) {
				if (!result.containsKey(idPare))
					result.put(idPare, distancia);
				grandparents.addAll(snomedBD.getParents(idPare));
			}
			conjuntTemporal = parents;
			parents = grandparents;
			grandparents = conjuntTemporal;
		}
		return result;
	}

	public ArrayList<Long> getParentsSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph) {
		ArrayList<Long> parents = snomedBD.getParents(idConcept);
		if (parents.size() > 0) {
			for (Long p : parents) {
				if (!subGraph.containsKey(p))
					parents.remove(p);
			}
		}
		return parents;
	}

	public HashMap<Long, Integer> getAncestrosDistanciaMinimaSubGraph(long idConcept,
			LinkedHashMap<Long, Integer> subGraph) {

		int distancia = 0;
		HashMap<Long, Integer> result = new HashMap<Long, Integer>();
		Set<Long> parents = new HashSet<Long>();
		Set<Long> conjuntTemporal;

		result.put(idConcept, 0);
		parents.addAll(getParentsSubGraph(idConcept, subGraph));
		while (parents.size() > 0) {
			distancia++;
			Set<Long> grandparents = new HashSet<Long>();
			for (Long idPare : parents) {
				if (!result.containsKey(idPare) && subGraph.containsKey(idPare))
					result.put(idPare, distancia);
				grandparents.addAll(getParentsSubGraph(idConcept, subGraph));
			}
			conjuntTemporal = parents;
			parents = grandparents;
			grandparents = conjuntTemporal;
		}
		return result;
	}

	public int getPathLength(long idConcept1, long idConcept2) {
		HashMap<Long, Integer> id1 = getAncestrosDistanciaMinima(idConcept1);
		HashMap<Long, Integer> id2 = getAncestrosDistanciaMinima(idConcept2);
		if (id1.size() > id2.size()) {
			HashMap<Long, Integer> ancestresTemporal = id1;
			id1 = id2;
			id2 = ancestresTemporal;
		}
		int distance = Integer.MAX_VALUE;
		for (Long i : id1.keySet()) {
			// System.out.print("\n+ LONG LENGTH"+i+ id2.containsKey(i));
			if (id2.containsKey(i) && distance > id1.get(i) + id2.get(i)) {
				distance = id1.get(i) + id2.get(i);
				// System.out.print("MAXIMO"+i+":"+distance+"id1"+id1.get(i)+"id2"+id2.get(i)+"\n");
			}
		}
		return distance;
	}

	public LinkedList<Long> getpathBetweenConcepts(long idConcept1, long idConcept2) {
		LinkedList<Long> path = new LinkedList<Long>();
		HashMap<Long, Integer> id1 = getAncestrosDistanciaMinima(idConcept1);
		System.out.print("\nANCESTROS" + id1 + "\n");
		HashMap<Long, Integer> id2 = getAncestrosDistanciaMinima(idConcept2);
		int caminomin = getPathLength(idConcept1, idConcept2);
		ArrayList<Long> children = snomedBD.getChildren(idConcept2);
		int camino = 1;
		if (id1.containsKey(idConcept2)) {
			path = getHijosPath(children, path, id1, camino + 1, caminomin);
		}
		for (Long p : path) {
			System.out.print("\n" + p + ":" + snomedBD.getParents(p));
		}
		return path;
	}

	public LinkedList<Long> getHijosPath(ArrayList<Long> children, LinkedList<Long> path, HashMap<Long, Integer> id1,
			int camino, int caminomin) {

		System.out.print("\nHIJOS" + children + "\n");
		ArrayList<Long> abuelos = new ArrayList<Long>();
		for (Long c : children) {
			if (id1.containsKey(c) && !path.contains(c)) {
				System.out.print("contains");
				path.add(c);

			}
			abuelos.addAll(snomedBD.getChildren(c));
		}
		System.out.print("ABUELOS" + abuelos);
		if (camino < caminomin)
			path = getHijosPath(abuelos, path, id1, camino + 1, caminomin);
		return path;
	}

	public int getNumLinksBetweenConceptos(long idConcept1, long idConcept2) {
		return getPathLength(idConcept1, idConcept2) - 1;
	}

	/*
	 * NO TIENE SENTIDO PORQUE CADA NODO TIENE UNA PROFUNDIDAD ASOCIADA public int
	 * depthConcept(long idConcept) { return 0; }
	 */
	public ArrayList<Long> getAncestors(long idConcept) {
		return snomedBD.getAncestors(idConcept);
	}

	public ArrayList<Long> getDescendants(long idConcept) {
		return snomedBD.getDescendants(idConcept);
	}

	public int numNodestoEntityConcept(long idConcept) {
		return snomedBD.getAncestors(idConcept).size();
	}

	public Long getLCS(long idConcept1, long idConcept2) {
		Map<Long, Integer> id1 = getAncestrosDistanciaMinima(idConcept1);
		System.out.print("\nANCESTROS DISTANCIA MINIMA" + id1 + "\n");
		System.out.print("\nCAMINO" + snomedBD.getAncestors(idConcept1) + "\n");
		Map<Long, Integer> id2 = getAncestrosDistanciaMinima(idConcept2);
		System.out.print("\nANCESTROS DISTANCIA MINIMA" + id2 + "\n");
		if (id1.size() > id2.size()) {
			Map<Long, Integer> ancestresTemporal = id1;
			id1 = id2;
			id2 = ancestresTemporal;
		}
		long idLCS = -1;
		int distanciaLCS = Integer.MAX_VALUE;
		for (Long id : id1.keySet())
			if (id2.containsKey(id) && (distanciaLCS > id1.get(id) + id2.get(id))) {
				idLCS = id;
				distanciaLCS = id1.get(id) + id2.get(id);
			}
		if (idLCS == -1)
			return null;
		return idLCS;
	}

	public int getAncestorsMaxDistance(long idConcept) {

		int caminoMaximo = 0;
		Set<Long> conjuntPares = new HashSet<Long>();
		Set<Long> conjuntAvis = new HashSet<Long>();
		Set<Long> conjuntTemporal = null;
		conjuntPares.addAll(snomedBD.getParents(idConcept));

		while (conjuntPares.size() > 0) {
			caminoMaximo++;
			System.out.print("\n\nCAMINOMAXIMO PROP" + caminoMaximo + ":" + conjuntPares + "TOTAL PARENTS"
					+ conjuntPares.size());
			for (Long idPare : conjuntPares)
				conjuntAvis.addAll(snomedBD.getParents(idPare));
			conjuntTemporal = conjuntPares;
			conjuntPares = conjuntAvis;
			conjuntAvis = conjuntTemporal;
			conjuntAvis.clear();
		}

		return caminoMaximo + 1; // Convierto a nodos VER
	}

	/*
	 * public ArrayList<Long> getParentsFromSubGraph(long idConcept,
	 * LinkedHashMap<Long,Integer> subgraph){ ArrayList<Long> sons=
	 * snomedBD.getParents(idConcept); for(Long s:sons) {
	 * if(!subgraph.containsKey(s)) subgraph.remove(s); } return sons; }
	 */

	public ArrayList<Long> getAncestorsSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph) {
		ArrayList<Long> ancestors = new ArrayList<Long>();
		if (subGraph.containsKey(idConcept)) {
			int level = subGraph.get(idConcept);
			for (Long a : subGraph.keySet()) {
				if (subGraph.get(a) <= level)
					ancestors.add(a);
			}

		}
		return ancestors;
	}

	public ArrayList<Long> getDescendantsSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph) {
		ArrayList<Long> descendants = new ArrayList<Long>();
		if (subGraph.containsKey(idConcept)) {
			int level = subGraph.get(idConcept);
			for (Long a : subGraph.keySet()) {
				if (subGraph.get(a) > level)
					descendants.add(a);
			}

		}
		return descendants;
	}

	public int getAncestorsMaxDistanceSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph) {

		int caminoMaximo = 0;
		Set<Long> conjuntPares = new HashSet<Long>();
		Set<Long> conjuntAvis = new HashSet<Long>();
		Set<Long> conjuntTemporal = null;

		conjuntPares.addAll(getParentsSubGraph(idConcept, subGraph));

		while (conjuntPares.size() > 0) {
			caminoMaximo++;
			System.out.print("\n\nCAMINOMAXIMO PROP" + caminoMaximo + ":" + conjuntPares + "TOTAL PARENTS"
					+ conjuntPares.size());
			for (Long idPare : conjuntPares)
				conjuntAvis.addAll(getParentsSubGraph(idConcept, subGraph));
			conjuntTemporal = conjuntPares;
			conjuntPares = conjuntAvis;
			conjuntAvis = conjuntTemporal;
			conjuntAvis.clear();
		}

		return caminoMaximo + 1; // Convierto a nodos VER
	}

	public int NotContainsFirstInSecond(Set<Long> a, Set<Long> b) {
		// TODO Auto-generated method stub
		int num = 0;
		for (Long syn : a) {

			if (!b.contains(syn)) {
				num++;
			}
		}
		return num;
	}

	public int Intersection(Set<Long> a, Set<Long> b) {
		int num = 0;
		for (Long syn : a) {

			if (b.contains(syn)) {
				num++;
			}
		}
		return num;
	}

	public JSONObject getSubGraphJSON(Map<Long, Integer> graph) {
		// System.out.print("GRAPH DENTRO JSON"+graph+"\n");
		JSONArray jsonarray = new JSONArray();
		JSONObject jresult = new JSONObject();
		Set<Long> nodes = new HashSet();
		nodes = graph.keySet();
		for (Long i : nodes) {
			JSONObject json = new JSONObject();

			json.put("id", i);
			json.put("level", graph.get(i));

			jsonarray.put(json);
			// System.out.print("JSON"+json+jsonarray+"\n");
		}
		return jresult.put("nodes", jsonarray);
	}

	public Map<Long, Integer> getSubGraph(long idConceptroot, int level) {

		Map<Long, Integer> subgraph = new LinkedHashMap<Long, Integer>();
		int depth = 0;
		subgraph.put(idConceptroot, 0);
		depth++;
		level--;
		ArrayList<Long> nodes = new ArrayList<Long>();
		nodes.add(idConceptroot);
		while (level >= 0) {
			System.out.print("LEVEL" + level + "NODES RAIZ" + nodes + "\n");
			nodes = getSubnodes(nodes, depth);
			System.out.print("\n" + level + "NODOS HIJOS TRAS SALIDA" + nodes + "\n");
			for (Long n : nodes)
				subgraph.put(n, depth);
			depth++;
			level--;
		}
		return subgraph;
	}

	public ArrayList<Long> getSubnodes(ArrayList<Long> children, int depth) {
		System.out.print("NODES" + children + "\n");
		HashMap<Long, Integer> subgraphlevel = new HashMap<Long, Integer>();
		ArrayList<Long> list = new ArrayList<>();
		for (Long son : children) {
			System.out.print("CHILDREN" + snomedBD.getChildren(son));
			list.addAll(snomedBD.getChildren(son));
		}
		return list;
	}

	public LinkedHashMap<Long, Integer> getSubGraph(GraphSnomed graph) {
		LinkedHashMap<Long, Integer> graphlinkedlist = new LinkedHashMap<Long, Integer>();
		ArrayList<GraphNode> listanodos = graph.getNodes();
		for (int i = 0; i < listanodos.size(); i++) {
			System.out.print("LONG" + listanodos.get(i).getId());
			System.out.print("LEVEL" + listanodos.get(i).getLevel());
			graphlinkedlist.put(listanodos.get(i).getId(), listanodos.get(i).getLevel());
		}
		return graphlinkedlist;
	}

	public static void main(String[] args) throws Exception {
		System.out.print("SNOMED CT\n");
		SnomedCTLibrary sct = new SnomedCTLibrary();
		long id = 123037004, id2 = 22298006, id3 = 80891009, clinical_finding = 404684003, id4 = 57809008,
				id5 = 123397009, id6 = 5626501, id7 = 56265001, id8 = 25105200, id9 = 417163006, id10 = 123946008,
				id11 = 301095005, id12 = 301857004;
		long id4nivel = 92993003;
		String description = sct.getConcept(id2);
		/*
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id4));
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id5));
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id6));
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id7));
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id8));
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id9));
		 * System.out.print("\n"+sct.getHypernymsIdConcepts(id10));
		 */

		// System.out.print("LCS"+sct.getLCS(id2, id11)+"camino
		// minimo"+sct.getPathLength(id2, id11));
		System.out.print("\nCAMINO MINIMO" + sct.getPathLength(id2, id12));
		// System.out.print("\nPATH"+sct.getpathBetweenConcepts(id2, id12));
		Map<Long, Integer> subgraph = sct.getSubGraph(id12, 7);
		System.out.print("\n\nSUBGRAPH" + subgraph);
		System.out.print("\n" + sct.getHypernymsIdConcepts(id4nivel));
		// System.out.print("PARENTS"+sct.getParentsFromSubGraph(id12, subgraph));
		// System.out.print("SUBGRAPH
		// MINIMA"+sct.getAncestrosDistanciaMinimaSubGraph(id2,subgraph));
		// System.out.print("\nANCESTROS"+sct.getAncestrosDistanciaMinima(id2));
		// System.out.print("\nMAXIMA"+sct.getAncestorsMaxDistance(id2));
		/*
		 * System.out.print("\n\nDESCRIPTION OF CONCEPT->"+description); ArrayList<Long>
		 * synonymsids = sct.getSynonymsIdConcepts(id2);
		 * System.out.print("\n\nSYNONYMS OF CONCEPT->"+synonymsids+"\n");
		 * ArrayList<String> synonyms = sct.getSynonymsConcepts(id2);
		 * System.out.print("\n\nSYNONYMS OF CONCEPT->"+synonyms);
		 * System.out.print("\n\nANCESTROS"+sct.gettHypernymsConcepts(id2));
		 * System.out.print("\n\nANCESTROS"+sct.gettHypernymsIdConcepts(id2));
		 * System.out.print("\n\nHYPONYMS"+sct.gettHyponymsConcept(id2)); //
		 * HashMap<Long, Integer> list = sct.trobarAncestresAmbDistanciaMinima(id2);
		 * HashMap<Long, Integer> ancestor = sct.AncestrosDistanciaMinima(id2);
		 * System.out.print("\n\npath->"+ancestor+"\n");
		 */
		// System.out.print("PATHTOENTITY"+ sct.getPathEntityConcept(id2));
		/*
		 * System.out.print("\n\nPATHTBETWEEN"+ sct.getpathBetweenConcepts(id2, id3));
		 * System.out.print("\nCALCULAR PATH LENGHT"+sct.calcularPathLength(id2, id3));
		 * System.out.print("\nDescendants"+sct.getDescendants(id3)+"\n");
		 * System.out.print("\nChildren"+sct.getHyponymsIdConcepts(id3)+"\n");
		 * System.out.print("GRAPH FINAL"+sct.getSubGraph(id3, 7));
		 */
		// System.out.print("MERONYMS"+sct.getMeronymsConcept(id2));
		// System.out.print("\n\nLCS"+sct.getLCS(id2, id4));
		// System.out.print("CAMINO MAXIMO A LA RAIZ"+sct.getMaxPathToEntity(id2));

	}
}
