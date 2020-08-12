package com.proyectoapi.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SnomedCTLibraryTest {

	SnomedCTLibrary sn;

	@Before
	public void initWordnetLibrary() {
		sn = new SnomedCTLibrary();
	}

	@Test
	public void getConceptTest() {

		long conceptid = 404684003;
		String conceptname = sn.getConcept(conceptid);
		assertEquals(conceptname, "Clinical finding (finding)");
	}

	@Test
	public void getSynonymsConceptsTest() {
		long conceptid = 22298006;
		ArrayList<String> correctsynonyms = new ArrayList<String>();
		correctsynonyms.add("Myocardial infarction (disorder)");
		correctsynonyms.add("Cardiac infarction");
		correctsynonyms.add("Heart attack");
		correctsynonyms.add("MI - Myocardial infarction");
		correctsynonyms.add("Myocardial infarction");
		correctsynonyms.add("Infarction of heart");
		correctsynonyms.add("Myocardial infarct");
		ArrayList<String> synonyms = sn.getSynonymsConcepts(conceptid);
	//	System.out.print(synonyms+"\n"+correctsynonyms);
		assertEquals(synonyms, correctsynonyms);
	}

	@Test
	public void getSynonymsIdConceptsTest() throws Exception {
		long conceptid = 22298006, id1=751689013, id2=37442013, id3=37443015, id4=1784872019, id5=37436014, id6=37441018, id7=1784873012;
		ArrayList<Long> correctsynonyms = new ArrayList<Long>();
		correctsynonyms.add(id1);
		correctsynonyms.add(id2);
		correctsynonyms.add(id3);
		correctsynonyms.add(id4);
		correctsynonyms.add(id5);
		correctsynonyms.add(id6);
		correctsynonyms.add(id7);
		ArrayList<Long> synonyms = sn.getSynonymsIdConcepts(conceptid);
	//	System.out.print("\nSINONIMOS"+synonyms+"\n");
		assertEquals(synonyms, correctsynonyms);
	}

	@Test
	public void getHypernymsConceptsTest() {
		long conceptid = 22298006;
		ArrayList<String> correcthypernyms = new ArrayList<String>();
		correcthypernyms.add("Injury of anatomical site (disorder)");
		correcthypernyms.add("Myocardial disease (disorder)");
		correcthypernyms.add("Structural disorder of heart (disorder)");
		ArrayList<String> hypernyms = sn.getHypernymsConcepts(conceptid);
		//System.out.print("\n"+hypernyms);
		assertEquals(hypernyms, correcthypernyms);
	}

	@Test
	public void getHypernymsIdConceptsTest() {
		long conceptid = 22298006, id1=123397009, id2=57809008, id3=128599005;
		ArrayList<Long> correcthypernyms = new ArrayList<Long>();
		correcthypernyms.add(id1);
		correcthypernyms.add(id2);
		correcthypernyms.add(id3);
		ArrayList<Long> hypernyms = sn.getHypernymsIdConcepts(conceptid);
		//System.out.print("\n"+hypernyms);
		assertEquals(hypernyms, correcthypernyms);
	}

	@Test
	public void getHyponymsConceptsTest() {
		long conceptid = 22298006;
		ArrayList<String> correcthyponyms = new ArrayList<String>();
		correcthyponyms.add("Microinfarct of heart (disorder)");
		correcthyponyms.add("Myocardial infarction with complication (disorder)");
		correcthyponyms.add("Mixed myocardial ischemia and infarction (disorder)");
		correcthyponyms.add("Non-Q wave myocardial infarction (disorder)");
		correcthyponyms.add("Acute myocardial infarction (disorder)");
		correcthyponyms.add("True posterior myocardial infarction (disorder)");
		correcthyponyms.add("Old myocardial infarction (disorder)");
		correcthyponyms.add("Silent myocardial infarction (disorder)");
		correcthyponyms.add("Myocardial infarction in recovery phase (disorder)");
		correcthyponyms.add("Postoperative myocardial infarction (disorder)");
		correcthyponyms.add("Subsequent myocardial infarction (disorder)");
		correcthyponyms.add("First myocardial infarction (disorder)");
		ArrayList<String> hyponyms = sn.getHyponymsConcepts(conceptid);
	//	System.out.print("\nHIPONIMOS"+hyponyms);
		assertEquals(hyponyms, correcthyponyms);
	}

	@Test
	public void getHyponymsIdConceptsTest() {
		long conceptid = 22298006, id1=42531007, id2=371068009, id3=428196007, id4=314207007, id5=57054005, id6=194802003, id7=1755008, id8=233843008, id9=418044006, id10=129574000, id11=194856005, id12=394710008;
		ArrayList<Long> correcthyponyms = new ArrayList<>();
		correcthyponyms.add(id1);
		correcthyponyms.add(id2);
		correcthyponyms.add(id3);
		correcthyponyms.add(id4);
		correcthyponyms.add(id5);
		correcthyponyms.add(id6);
		correcthyponyms.add(id7);
		correcthyponyms.add(id8);
		correcthyponyms.add(id9);
		correcthyponyms.add(id10);
		correcthyponyms.add(id11);
		correcthyponyms.add(id12);
		ArrayList<Long> hyponyms = sn.getHyponymsIdConcepts(conceptid);
	//	System.out.print("\nHIPONIMOS"+hyponyms);
		assertEquals(hyponyms, correcthyponyms);
	}

	@Test
	public void getPathLengthTest() {
		long conceptid1 =138875005, conceptid2=56265001;
		int pathlength = sn.getPathLength(conceptid1, conceptid2);
		assertEquals(pathlength, 5);
	}

	@Test
	public void getNumLinksBetweenConceptsTest() {
		long conceptid1 =138875005, conceptid2=56265001;
		int numlinks = sn.getNumLinksBetweenConcepts(conceptid1, conceptid2);
		assertEquals(numlinks, 4);
	}
	
	@Test
	public void getAncestorsTest() {
		long conceptid1 =267070008;
		//ArrayList<Long> correctancestors = new ArrayList<>();
		ArrayList<Long> tree = sn.getDescendants(conceptid1);
		//System.out.print("\nARBOL ANCESTROS"+tree.size()+"\n"+tree);
		/*for(int i=0; i<tree.size(); i++) {
			System.out.print("\n"+sn.getConcept(tree.get(i)));
		}*/
		assertEquals(tree.size(), 4);
	}
	
	@Test
	public void getDescendantsTest() {
		long conceptid1 =267070008;
		//ArrayList<Long> correctdescendants = new ArrayList<>();
		ArrayList<Long> tree = sn.getDescendants(conceptid1);
		/*System.out.print("\nARBOL"+tree.size()+"\n"+tree);
		for(int i=0; i<tree.size(); i++) {
			System.out.print("\n"+sn.getConcept(tree.get(i)));
		}*/
		assertEquals(tree.size(), 4);
	}
	
	@Test
	public void getAncestorsDistanciaMinimaTest() {
		long conceptid = 118234003;
		HashMap<Long, Integer> ancestors = sn.getAncestorsDistanciaMinima(conceptid);
		HashMap<Long, Integer> correctancestors = new HashMap<Long, Integer>();
		correctancestors.put(new Long(138875005), 2);
		correctancestors.put(new Long(404684003), 1);
		correctancestors.put(new Long(118234003), 0);
//		System.out.print("\nANSCENTROS CON DISTANCIA MINIMA"+ancestors+"\n"+correctancestors);
		assertEquals(ancestors, correctancestors);
	}
	
	@Test
	public void getNumNodestoEntityConceptTest() {
		long idconcept =56265001;
		int numnodes = sn.getNumNodestoEntityConcept(idconcept);
		assertEquals(numnodes, 5);
	}
	
	@Test
	public void getLCSTest() {
		long conceptid1=301095005, conceptid2=49601007, correctlcs =106063007;
		long conceptlcs = sn.getLCS(conceptid1, conceptid2);
	//	System.out.print("\nLCS"+conceptlcs+"\n");
		assertEquals(conceptlcs, correctlcs);
	}

	@Test
	public void getMaxDistanceToEntityConceptTest() {
		long conceptid1 =56265001;
		int maxdistance = sn.getAncestorsMaxDistance(conceptid1);
	///	System.out.print("\nMAX DISTANCE ANCESTORS->"+maxdistance+"\n");
		assertEquals(maxdistance, 9);
	}
	
	@Test
	public void getSubGraphTest() {
		long conceptid1 = 424017009;
		Map<Long, Integer> tree =sn.getSubGraph(conceptid1);
		System.out.print("\ntree ID->"+tree);
	}
	
	@Test
	public void getSubGraphWithLevelTest() {
		long conceptid1 = 424017009;
		int level = 3;
		Map<Long, Integer> tree =sn.getSubGraph(conceptid1, level);
		System.out.print("\ntree LEVEL->"+tree);
	}
	
	@Test
	public void getAncestorsSubGraphTest() {
		long conceptid1 = 127357005, node = 124044002, id1=127357005, id2=127359008, id3=127360003;
		ArrayList<Long> correctancestors = new ArrayList<Long>();
		correctancestors.add(id1);
		correctancestors.add(id2);
		correctancestors.add(id3);
		LinkedHashMap<Long, Integer> tree =sn.getSubGraph(conceptid1);
		ArrayList<Long> ancestors= sn.getAncestorsSubGraph(node, tree);
		//System.out.print("TREE ANCESTORS"+tree+"\n"+"ANCESTOR"+ancestors);
		assertEquals(ancestors, correctancestors);
		
	}
	
	@Test
	public void getDescendantsSubGraphTest() {
		long conceptid1 = 127357005, node = 124044002, id1=124024008, id2=124021000;
		ArrayList<Long> correctdescendants = new ArrayList<Long>();
		correctdescendants.add(id1);
		correctdescendants.add(id2);
		LinkedHashMap<Long, Integer> tree =sn.getSubGraph(conceptid1);
		ArrayList<Long> descendants= sn.getDescendantsSubGraph(node, tree);
	//	System.out.print("TREE ANCESTORS"+tree+"\n"+"DESCENDANTS"+descendants);
		assertEquals(descendants, correctdescendants);
		
	}
	
	
	@Test
	public void getAncestorsMaxDistanceSubGraphTest() {
		long conceptid1 = 127357005, node = 124021000;
		
		LinkedHashMap<Long, Integer> tree =sn.getSubGraph(conceptid1);
		int maxpath = sn.getAncestorsMaxDistanceSubGraph(node, tree);
		System.out.print("\nPATH"+maxpath);
		assertEquals(maxpath, 3);
		
	}
	
	@Test
	public void getAncestrosDistanciaMinimaSubGraph() {
		long conceptid1 = 127357005, node = 124044002, id1=127359008, id2=127357005;
		LinkedHashMap<Long, Integer> correctancestorsmin = new LinkedHashMap<Long, Integer>();
		correctancestorsmin.put(id1, 1);
		correctancestorsmin.put(id2, 2);
		LinkedHashMap<Long, Integer> tree =sn.getSubGraph(conceptid1);
		HashMap<Long, Integer> ancestorsmin= sn.getAncestrosDistanciaMinimaSubGraph(node, tree);
		System.out.print("TREE ANCESTORS"+tree+"\n"+"ANCESTOR"+ancestorsmin);
		assertEquals(ancestorsmin, correctancestorsmin);
	}
	
	/*@Test
	public void getPathEntityConcept() {
		long conceptid = 417163006;
		ArrayList<Long> pathancestors= sn.getPathEntityConcept(conceptid);
		System.out.print("\nPATH TO RAIZ"+sn.getDescendants(conceptid));
		
	}*/


}
