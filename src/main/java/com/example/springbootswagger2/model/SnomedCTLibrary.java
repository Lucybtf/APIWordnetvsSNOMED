package com.example.springbootswagger2.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.mays.snomed.ConceptBasic;
import com.mays.snomed.ConceptDescription;
import com.mays.snomed.ConceptRelationship;
import com.mays.snomed.SnomedDb;
import com.mays.snomed.SnomedIsa;
import com.mays.util.Sql;

import net.didion.jwnl.data.Synset;


public class SnomedCTLibrary {

	private SnomedIsa snomedBD;
	
	public SnomedCTLibrary(){
		try {
			Connection sql = Sql.getConnection("./ddb", "snomed-test");	
			snomedBD = new SnomedIsa();
			snomedBD.init(sql); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getConcept(long idConcept) {
		return snomedBD.getConcept(idConcept).toString();
	}
	
	public ArrayList<String> getSynonymsConcepts(long idConcept) throws Exception{
		ArrayList<String> synonyms = new ArrayList<String>();

		ArrayList<ConceptDescription> descriptions = snomedBD.getDescriptions(idConcept);
		for(ConceptDescription s: descriptions) {
			if(s.getDescriptionStatus() == 0)
				//System.out.print("RELATION->"+s.getTerm()+""+s.getDescriptionStatus()+"\n");
				synonyms.add(s.getTerm());
				//System.out.print("SYNONYMS ID"+s.getConceptId());
		}
		return synonyms;
	}
	
	
	public ArrayList<Long> getSynonymsIdConcepts(long idConcept) throws Exception{
		ArrayList<Long> synonyms = new ArrayList<Long>();

		ArrayList<ConceptDescription> descriptions = snomedBD.getDescriptions(idConcept);
		for(ConceptDescription s: descriptions) {
			if(s.getDescriptionStatus() == 0)
				//System.out.print("RELATION->"+s.getTerm()+""+s.getDescriptionStatus()+"\n");
				synonyms.add(s.getConceptId());
				System.out.print("SYNONYMS ID"+s.getConceptId());
		}
		return synonyms;
	}
	
	public ArrayList<String> getHypernymsConcepts(long idConcept){
		ArrayList<String> hypernymsconcepts = new ArrayList<String>();
		ArrayList<Long> hypernyms = new ArrayList<Long>();
		hypernyms = snomedBD.getParents(idConcept);
		for(int i=0; i< hypernyms.size();i++) {
			long currentid = hypernyms.get(i);
			ConceptBasic currentC = snomedBD.getConcept(currentid);
			System.out.print("\n\nCONCEPT"+snomedBD.getConcept(currentid)+"\n");
			hypernymsconcepts.add(snomedBD.getConcept(currentid).toString());
		}
		return hypernymsconcepts;
	}
	
	public ArrayList<Long> getHypernymsIdConcepts(long idConcept){
		return snomedBD.getParents(idConcept);
	}
	
	public ArrayList<String> getHyponymsConcepts(long idConcept) throws Exception{
		ArrayList<String> hyponyms = new ArrayList<String>();
		ArrayList<Long> hyponymsconcepts = snomedBD.getChildren(idConcept);
		for(int i =0;i< hyponymsconcepts.size();i++) {
			hyponyms.add(snomedBD.getConcept(hyponymsconcepts.get(i)).toString());
		}
		return hyponyms;
	}
	
	public ArrayList<Long> getHyponymsIdConcepts(long idConcept){
		return snomedBD.getChildren(idConcept);
		
	}
	
/*	CONSULTAR
 * 	public ArrayList<String> getMeronymsConcept(long idConcept){
		ArrayList<String> meronyms = new ArrayList<String>();
		return meronyms;
	}
	
	public ArrayList<String> getHolonymsConcept(long idConcept){
		ArrayList<String> holonyms = new ArrayList<String>();
		return holonyms;
	}*/
	
	/*public ArrayList<String> pathToEntityConcept(long idConcept){
		ArrayList<String> path = new ArrayList<String>();
		ArrayList<Long> parentnodes = new ArrayList<Long>();
		parentnodes.addAll(snomedBD.getParents(idConcept));
		if(parentnodes.size()>0) {
			for(Long id:parentnodes) {
				//parentnodes.addAll()
			}
		}
		
		return path;
	}*/
	public ArrayList<Long> getPathEntityConcept(long id){
	//	System.out.print(snomedBD.getAncestors(id).size());
		return snomedBD.getAncestors(id);
	}
	
	public HashMap<Long, Integer> AncestrosDistanciaMinima(long idConcept){
	
		int distancia=0;
		HashMap<Long, Integer> result = new HashMap<Long, Integer> ();
		Set<Long> parents = new HashSet<Long>();
		Set<Long> conjuntTemporal;
		
		result.put(idConcept, 0);
		parents.addAll(snomedBD.getParents(idConcept));	
		while(parents.size()>0){
			distancia++;
			Set<Long> grandparents= new HashSet<Long>();
			for (Long idPare : parents) {
				if(!result.containsKey(idPare)) 
					result.put(idPare, distancia);
				grandparents.addAll(snomedBD.getParents(idPare));
			}
			conjuntTemporal=parents;
			parents=grandparents;
			grandparents=conjuntTemporal;
		}
		return result;
	}
	
	public int calcularPathLength(long idConcept1, long idConcept2) {
		HashMap<Long, Integer> id1 = AncestrosDistanciaMinima(idConcept1);
		HashMap<Long, Integer> id2 = AncestrosDistanciaMinima(idConcept2);
		if(id1.size()>id2.size()) {
			HashMap<Long, Integer> ancestresTemporal=id1;
			id1=id2;
			id2=ancestresTemporal;
		}
		int distance=Integer.MAX_VALUE;
		for(Long i:id1.keySet()) {
			System.out.print("\n+ LONG LENGTH"+i+ id2.containsKey(i));
			if(id2.containsKey(i) && distance > id1.get(i)+id2.get(i)) {
				distance = id1.get(i)+id2.get(i);
				System.out.print("MAXIMO"+i+":"+distance+"id1"+id1.get(i)+"id2"+id2.get(i)+"\n");
			}
		}
		return distance;
	}
	public ArrayList<Long> getpathBetweenConcepts(long idConcept1, long idConcept2){
		HashMap<Long, Integer> id1 = AncestrosDistanciaMinima(idConcept1);
		System.out.print("ANCESTROS"+id1+"\n");
		HashMap<Long, Integer> id2 = AncestrosDistanciaMinima(idConcept2);
		System.out.print("ANCESTROS2"+id2+"\n");
		ArrayList<Long> path = new ArrayList<Long>();
		if(id1.size()>id2.size()) {
			HashMap<Long, Integer> ancestresTemporal=id1;
			id1=id2;
			id2=ancestresTemporal;
		}
		int distance=Integer.MAX_VALUE;
		int sum =0;
//		System.out.print(b);
		/*for(Long i:id1.keySet()) 
		{	System.out.print("Long"+i+"\n");
			//sum =id1.get(i)+id2.get(i);
			//System.out.print("COntiene"+id2.containsKey(i)+"distance"+distance+"suma"+(id1.get(i)+id2.get(i))+"\n");
			if(id2.containsKey(i) && distance > id1.get(i)+id2.get(i)) {
				distance = id1.get(i)+id2.get(i);
				System.out.print("HOLA ENTRA\n");
				path.add(i);
			}
		}*/
		ArrayList<Long> descendants = snomedBD.getChildren(idConcept2);
		System.out.print("DENCENDANTS"+descendants);
		return path;
	}
	
	public int NumLinksBetweenConceptos(long idConcept) {
		return 0;
	}
	
/*	NO TIENE SENTIDO PORQUE CADA NODO TIENE UNA PROFUNDIDAD ASOCIADA
 * 	public int depthConcept(long idConcept) {
		return 0;
	}*/
	public ArrayList<Long> getAncestors(long idConcept){
		return snomedBD.getAncestors(idConcept);
	}
	
	
	public ArrayList<Long> getDescendants(long idConcept){
		return snomedBD.getDescendants(idConcept);
	}
	
	
	public int numNodestoEntityConcept(long idConcept) {
		return snomedBD.getAncestors(idConcept).size();
	}
	
	public Long getLCS(long idConcept1, long idConcept2) {
		Map<Long, Integer> id1 = AncestrosDistanciaMinima(idConcept1);
		System.out.print("\nANCESTROS DISTANCIA MINIMA"+id1+"\n");
		System.out.print("\nCAMINO"+snomedBD.getAncestors(idConcept1)+"\n");
		Map<Long, Integer> id2 = AncestrosDistanciaMinima(idConcept2);
		System.out.print("\nANCESTROS DISTANCIA MINIMA"+id2+"\n");
		if(id1.size()>id2.size()) {
			Map<Long, Integer> ancestresTemporal=id1;
			id1=id2;
			id2=ancestresTemporal;
		}
		long idLCS=-1;
		int distanciaLCS=Integer.MAX_VALUE;
		for(Long id : id1.keySet()) 
			if(id2.containsKey(id) && (distanciaLCS>id1.get(id)+id2.get(id))) {
				idLCS=id;
				distanciaLCS=id1.get(id)+id2.get(id);
			}
		if(idLCS==-1) return null;
		return idLCS;
	}
	
	public int getMaxPathToEntity(long idConcept){
	
		int caminoMaximo=0;
		Set<Long> conjuntPares = new HashSet<Long>();
		Set<Long> conjuntAvis = new HashSet<Long>();
		Set<Long> conjuntTemporal = null;
		conjuntPares.addAll(snomedBD.getParents(idConcept));	
		while(conjuntPares.size()>0){
			caminoMaximo++;
			for (Long idPare : conjuntPares) 
				conjuntAvis.addAll(snomedBD.getParents(idPare));
			conjuntTemporal=conjuntPares;
			conjuntPares=conjuntAvis;
			conjuntAvis=conjuntTemporal;
			conjuntAvis.clear();
		}
		System.out.print("NODOS"+conjuntTemporal);
		return caminoMaximo+1;	//Convierto a nodos VER
	}
	
	public int NotContainsFirstInSecond(Set<Long> a, Set<Long> b) {
		// TODO Auto-generated method stub
		int num = 0;
		for(Long syn:a){
			
			if(!b.contains(syn)){
				num++;
			}
		}
		return num;
	}

	public int Intersection(Set<Long> a, Set<Long> b){
		int num = 0;
		for(Long syn:a){
			
			if(b.contains(syn)){
				num++;
			}
		}
		return num;
	}
	
	public LinkedHashMap<Long, Integer> getSubGraph(long idConceptroot, int level){
		LinkedHashMap<Long, Integer> subgraph = new LinkedHashMap<Long, Integer> ();
		int depth =0;
		subgraph.put(idConceptroot, 0);
		depth++;
		level--;
		ArrayList<Long> nodes = new ArrayList<Long>();
		nodes.add(idConceptroot);
		while(level>= 0) {
			//System.out.print("HOLA\n");
			nodes = getSubnodes(nodes, depth);
			System.out.print("GRAPH"+nodes+"\n");
			for(Long n:nodes)
				subgraph.put(n, depth);
			depth++;
			level--;
		}
		return subgraph;
	}
	
	public ArrayList<Long> getSubnodes(ArrayList<Long> children, int depth){
		HashMap<Long, Integer> subgraphlevel = new HashMap<Long, Integer> ();
		ArrayList<Long> list = new ArrayList<Long>();
		//System.out.print("CHILDREN"+children+"\n");
		for(Long son: children) {
			list.addAll(snomedBD.getChildren(son));
			//subgraphlevel.put(son, depth);
		}
		return list;
	}

	public static void main(String[] args) throws Exception{
		System.out.print("SNOMED CT\n");
		SnomedCTLibrary sct = new SnomedCTLibrary();
		long id = 123037004, id2 = 22298006, id3=80891009, clinical_finding = 404684003, id4 = 57809008;
		String description = sct.getConcept(id2);	
	/*	System.out.print("\n\nDESCRIPTION OF CONCEPT->"+description);
		ArrayList<Long> synonymsids = sct.getSynonymsIdConcepts(id2);
		System.out.print("\n\nSYNONYMS OF CONCEPT->"+synonymsids+"\n");
		ArrayList<String> synonyms = sct.getSynonymsConcepts(id2);
		System.out.print("\n\nSYNONYMS OF CONCEPT->"+synonyms);
		System.out.print("\n\nANCESTROS"+sct.gettHypernymsConcepts(id2));
		System.out.print("\n\nANCESTROS"+sct.gettHypernymsIdConcepts(id2));
		System.out.print("\n\nHYPONYMS"+sct.gettHyponymsConcept(id2));
	//	HashMap<Long, Integer> list = sct.trobarAncestresAmbDistanciaMinima(id2);
		HashMap<Long, Integer> ancestor = sct.AncestrosDistanciaMinima(id2);
		System.out.print("\n\npath->"+ancestor+"\n");*/
	//	System.out.print("PATHTOENTITY"+ sct.getPathEntityConcept(id2));
		System.out.print("\n\nPATHTBETWEEN"+ sct.getpathBetweenConcepts(id2, id3));
		System.out.print("\nCALCULAR PATH LENGHT"+sct.calcularPathLength(id2, id3));
		System.out.print("\nDescendants"+sct.getDescendants(id3)+"\n");
		System.out.print("\nChildren"+sct.getHyponymsIdConcepts(id3)+"\n");
		System.out.print("GRAPH FINAL"+sct.getSubGraph(id3, 7));
		//System.out.print("\n\nLCS"+sct.getLCS(id2, id4));
		//System.out.print("CAMINO MAXIMO A LA RAIZ"+sct.getMaxPathToEntity(id2));
		
	 }
}
