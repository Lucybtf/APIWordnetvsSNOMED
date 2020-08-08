package com.example.spingbootswagger2.model;

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
import org.junit.Before;

import com.mays.snomed.ConceptBasic;
import com.mays.snomed.ConceptDescription;
import com.mays.snomed.SnomedIsa;
import com.mays.util.Sql;
import com.proyectoapi.model.GraphNode;
import com.proyectoapi.model.GraphSnomed;
import com.proyectoapi.model.SnomedCTLibrary;
import com.proyectoapi.model.WordnetLibrary;

public class SnomedCTLibraryTest {

	SnomedCTLibrary sn;
	
	
	@Before
	public void initWordnetLibrary(){
		 sn = new SnomedCTLibrary();
	}
	
	public void getConceptTest(long idConcept) {

	}
	
	public void getSynonymsConceptsTest(long idConcept) throws Exception{

	}
	
	
	public void getSynonymsIdConcepts(long idConcept) throws Exception{

	}
	
	public void getHypernymsConcepts(long idConcept){

	}
	
	public void getHypernymsIdConcepts(long idConcept){
	
	}
	
	public void getHyponymsConcepts(long idConcept){

	}
	
	public void getHyponymsIdConcepts(long idConcept){
		
	}
	
	public void getPathEntityConcept(long id){
	
	}
	
	public void getAncestrosDistanciaMinima(long idConcept){

	}
	
	public void getParentsSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph){

	}
	
	public void getAncestrosDistanciaMinimaSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph){

	}
	
	public void getPathLength(long idConcept1, long idConcept2) {

	}
	public void getpathBetweenConcepts(long idConcept1, long idConcept2){

	}
	
	public void getHijosPath(ArrayList<Long> children, LinkedList<Long> path, HashMap<Long, Integer> id1, int camino, int caminomin) {
		
	}
	
	public void getNumLinksBetweenConceptos(long idConcept1, long idConcept2) {
	
	}
	

	public void getAncestors(long idConcept){
	
	}
	
	
	public void getDescendants(long idConcept){
		
	}
	
	
	public void numNodestoEntityConcept(long idConcept) {
		
	}
	
	public void getLCS(long idConcept1, long idConcept2) {

	}
	
	public void getAncestorsMaxDistance(long idConcept){

	}
	

	public void getAncestorsSubGraph(long idConcept, LinkedHashMap<Long,Integer> subGraph){

	}
	
	public void getDescendantsSubGraph(long idConcept, LinkedHashMap<Long,Integer> subGraph){

	}
	
	public void getAncestorsMaxDistanceSubGraph(long idConcept, LinkedHashMap<Long,Integer> subGraph){

	}
	
	
	public void getSubGraphJSON(Map<Long, Integer> graph) {

	}
	
	
	public void getSubGraph(long idConceptroot, int level){


	}
	
	public void getSubnodes(ArrayList<Long> children, int depth){

	}
	
	public void getSubGraph(GraphSnomed graph){

	}

}
