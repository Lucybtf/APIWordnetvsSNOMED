package com.proyecto.libsemantica.ProyectoLibreria;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;


public class WordnetLibraryTest {

	WordnetLibrary wordnetTest;
	
	@Before
	public void initWordnetLibrary(){
		 wordnetTest = new WordnetLibrary();
	}
	 
	@Test
	public void getSynsetByWordTest() throws JWNLException {
		
		long offset = 2961779;
        
        Synset s0 = wordnetTest.getSynset("car", POS.NOUN, 1);
        Synset soffset =  wordnetTest.getSynset(offset);
        assertEquals(s0,soffset);
	}
	
	@Test
	public void getSynsetByOffsetTest() throws JWNLException {
		
		long offset = 2961779;
        
        Synset s0 = wordnetTest.getSynset("car", POS.NOUN, 1);
        Synset soffset =  wordnetTest.getSynset(offset);
        assertEquals(s0,soffset);
	}
	
	@Test
	public void getSenseBySynsetTest(){
		String sense = "a worker who is hired to perform a job";
		try {
			Synset employee = wordnetTest.getSynset(10073616);
			String senseresult = wordnetTest.getSense(employee);
			assertEquals(sense,senseresult);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getSenseByOffsetTest(){
		String sense = "a worker who is hired to perform a job";
		try {
			long employee = 10073616;
			String senseresult = wordnetTest.getSense(employee);
			assertEquals(sense,senseresult);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getSenseByWordTest(){
		String sense = "a worker who is hired to perform a job";
		String employee = "employee";
		int i = 0;
		String senseresult = wordnetTest.getSense(employee, i);
		assertEquals(sense, senseresult);
	}
	
	

	@Test
	public void getSynonymsBySynsetTest(){
		Synset party1, party2, party3, party4, party5;
		ArrayList<Synset> synonyms = new ArrayList<Synset>(); 
		try {
			party1= wordnetTest.getSynset(8273889);
			party2= wordnetTest.getSynset(8269523);
			party3= wordnetTest.getSynset(8281818);
			party4= wordnetTest.getSynset(7462241);
			party5= wordnetTest.getSynset(10422569);
			
			synonyms.add(party2);
			synonyms.add(party3);
			synonyms.add(party4);
			synonyms.add(party5);
		
			
			assertEquals(synonyms, wordnetTest.getSynonyms(party1));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	@Test
	public void getSynonymsByOffsetTest(){
		Synset party2, party3, party4, party5;
		ArrayList<Synset> synonyms = new ArrayList<Synset>(); 
		try {
		//	party= wordnetTest.getSynset(8273889);
			party2= wordnetTest.getSynset(8269523);
			party3= wordnetTest.getSynset(8281818);
			party4= wordnetTest.getSynset(7462241);
			party5= wordnetTest.getSynset(10422569);
			
			synonyms.add(party2);
			synonyms.add(party3);
			synonyms.add(party4);
			synonyms.add(party5);
		
			
			assertEquals(synonyms, wordnetTest.getSynonyms(8273889));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Test
	public void getSynonymsWordsByWordSenseTest(){
		
		ArrayList<String> synonyms = new ArrayList<String>(); 
		synonyms.add("company");
		synonyms.add("party");
		synonyms.add("political_party");
		
		int sense = 1;
		
		assertEquals(synonyms, wordnetTest.getSynonymsWords("party", POS.NOUN, sense));
		
	}
	
	@Test
	public void getSynonymsWordsBySynsetTest(){
		ArrayList<String> synonyms = new ArrayList<String>(); 
		try {
			Synset party1= wordnetTest.getSynset(8269523);
			synonyms.add("company");
			synonyms.add("party");
			synonyms.add("political_party");
			
			assertEquals(synonyms, wordnetTest.getSynonymsWords(party1));
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void getSynonymsWordsByOffsetTest(){
	
		ArrayList<String> synonyms = new ArrayList<String>(); 
		synonyms.add("company");
		synonyms.add("party");
		synonyms.add("political_party");
		try {
			assertEquals(synonyms, wordnetTest.getSynonymsWords(8269523));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getHypernymsBySynsetTest(){
		try {
			ArrayList<Synset> result = new ArrayList<Synset>();
			Synset synset_cat = wordnetTest.getSynset(2124272);
			Synset synset_result = wordnetTest.getSynset(2123649);
			result.add(synset_result);
			assertEquals(result, wordnetTest.getHypernyms(synset_cat));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getHypernymsByOffsetTest(){
		try {
			ArrayList<Synset> result = new ArrayList<Synset>();
			//Synset synset_cat = wordnetTest.getSynset(2124272);
			Synset synset_result = wordnetTest.getSynset(2123649);
			result.add(synset_result);
			assertEquals(result, wordnetTest.getHypernyms(2124272));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getHypernymsByWordSenseTest(){
		try {
			ArrayList<Synset> result = new ArrayList<Synset>();
			//Synset synset_cat = wordnetTest.getSynset(2124272);
			Synset synset_result = wordnetTest.getSynset(2123649);
			result.add(synset_result);
			assertEquals(result, wordnetTest.getHypernyms("cat", POS.NOUN, 1));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getHypernymsWordsBySynsetTest(){

		ArrayList<String> hypernyms = new ArrayList<String>();
		hypernyms.add("feline");
		hypernyms.add("felid");
		try {
			Synset synset_cat = wordnetTest.getSynset(2124272);
			assertEquals(hypernyms, wordnetTest.getHypernymsWords(synset_cat));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getHypernymsWordsByOffsetTest(){

		ArrayList<String> hypernyms = new ArrayList<String>();
		hypernyms.add("feline");
		hypernyms.add("felid");
		try {
			assertEquals(hypernyms, wordnetTest.getHypernymsWords(2124272));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void getHypernymsWordsByWordSenseTest(){
		int i = 1;
		ArrayList<String> hypernyms = new ArrayList<String>();
		hypernyms.add("feline");
		hypernyms.add("felid");
		try {
			assertEquals(hypernyms, wordnetTest.getHypernymsWords("cat", i, POS.NOUN));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getHyponymsBySynsetTest() throws JWNLException{
		
		ArrayList<Synset> hyponyms = new ArrayList<Synset>();
		hyponyms.add(wordnetTest.getSynset(2126437));
		Synset synset = wordnetTest.getSynset(2126249);
		assertEquals(hyponyms, wordnetTest.getHyponyms(synset));
	}
	
	@Test
	public void getHyponymsByOffsetTest() throws JWNLException{
		ArrayList<Synset> hyponyms = new ArrayList<Synset>();
		hyponyms.add(wordnetTest.getSynset(2126437));
		assertEquals(hyponyms, wordnetTest.getHyponyms(2126249));
	}

	@Test
	public void getHyponymsByWordSenseTest() throws JWNLException{
		int i = 1;
		ArrayList<Synset> hyponyms = new ArrayList<Synset>();
		hyponyms.add(wordnetTest.getSynset(2126437));
		assertEquals(hyponyms, wordnetTest.getHyponyms("Siamese_cat", i, POS.NOUN));
	}
	
	
	@Test
	public void getHyponymsWordsBySynsetTest() throws JWNLException{
		int i = 1;
		ArrayList<String> hyponyms = new ArrayList<String>();
		hyponyms.add("blue_point_Siamese");
		
		assertEquals(hyponyms, wordnetTest.getHyponymsWords("Siamese_cat", i, POS.NOUN));
	}
	
	@Test
	public void getHyponymsWordsByOffsetTest() throws JWNLException{
		int i = 1;
		ArrayList<String> hyponyms = new ArrayList<String>();
		hyponyms.add("blue_point_Siamese");
	
		assertEquals(hyponyms, wordnetTest.getHyponymsWords(2126249));
	}

	@Test
	public void getHyponymsWordsByWordSenseTest() throws JWNLException{
		int i = 1;
		ArrayList<String> hyponyms = new ArrayList<String>();
		hyponyms.add("blue_point_Siamese");
	
		assertEquals(hyponyms, wordnetTest.getHyponymsWords("Siamese_cat", i, POS.NOUN));
	}
	
	@Test
	public void getHolonymsBySynsetTest() throws JWNLException{
		ArrayList<Synset> resultarray = new ArrayList<Synset>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		Synset sresult = wordnetTest.getSynset("wheeled_vehicle", POS.NOUN, 1);
		resultarray.add(sresult);
		assertEquals(resultarray, wordnetTest.getHolonyms(synset));
		
	}
	
	@Test
	public void getHolonymsByOffsetTest() throws JWNLException{
		ArrayList<Synset> resultarray = new ArrayList<Synset>();
		Synset sresult = wordnetTest.getSynset("wheeled_vehicle", POS.NOUN, 1);
		resultarray.add(sresult);
		assertEquals(resultarray, wordnetTest.getHolonyms(4582285));
	}
	
	@Test
	public void getHolonymsByWordSenseTest() throws JWNLException{
		ArrayList<Synset> resultarray = new ArrayList<Synset>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		Synset sresult = wordnetTest.getSynset("wheeled_vehicle", POS.NOUN, 1);
		resultarray.add(sresult);
		assertEquals(resultarray, wordnetTest.getHolonyms("wheel", 1, POS.NOUN));
	}
	
	
	@Test
	public void getHolonymsWordsBySynsetTest() throws JWNLException{
		ArrayList<String> resultarray = new ArrayList<String>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		resultarray.add("wheeled_vehicle");
		//System.out.print("WO"+wordnetTest.getHolonymsWords(synset));
		assertEquals(resultarray, wordnetTest.getHolonymsWords(synset));	
	}
	
	@Test
	public void getHolonymsWordsByOffsetTest() throws JWNLException{
		ArrayList<String> resultarray = new ArrayList<String>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		resultarray.add("wheeled_vehicle");
		assertEquals(resultarray, wordnetTest.getHolonymsWords(4582285));
	}
	
	@Test
	public void getHolonymsWordsByWordSenseTest() throws JWNLException{
		ArrayList<String> resultarray = new ArrayList<String>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		resultarray.add("wheeled_vehicle");
		assertEquals(resultarray, wordnetTest.getHolonymsWords("wheel", 1, POS.NOUN));
	}
	
	
	@Test
	public void getMeronymsBySynsetTest() throws JWNLException{
		ArrayList<Synset> result = new ArrayList<Synset>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		Synset felloe = wordnetTest.getSynset("felloe", POS.NOUN, 1);
		Synset rim = wordnetTest.getSynset("rim", POS.NOUN, 3);
		result.add(felloe);
		result.add(rim);
		//System.out.print("\n\nRESULTADO"+wordnetTest.getMeronyms(synset));
		assertEquals(result, wordnetTest.getMeronyms(synset));
		
	}
	
	@Test
	public void getMeronymsByOffsetTest() throws JWNLException{
		ArrayList<Synset> result = new ArrayList<Synset>();
		Synset felloe = wordnetTest.getSynset("felloe", POS.NOUN, 1);
		Synset rim = wordnetTest.getSynset("rim", POS.NOUN, 3);
		result.add(felloe);
		result.add(rim);
		//System.out.print("\n\nRESULTADO"+wordnetTest.getMeronyms(synset));
		assertEquals(result, wordnetTest.getMeronyms(4582285));
	}
	
	@Test
	public void getMeronymsByWordSenseTest() throws JWNLException{
		ArrayList<Synset> result = new ArrayList<Synset>();
		Synset felloe = wordnetTest.getSynset("felloe", POS.NOUN, 1);
		Synset rim = wordnetTest.getSynset("rim", POS.NOUN, 3);
		result.add(felloe);
		result.add(rim);
		//System.out.print("\n\nRESULTADO"+wordnetTest.getMeronyms(synset));
		assertEquals(result, wordnetTest.getMeronyms("wheel", 1, POS.NOUN));
	}
	@Test
	public void getMeronymsWordsBySynsetTest() throws JWNLException{
		ArrayList<String> result = new ArrayList<String>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		result.add("felloe");
		result.add("felly");
		result.add("rim");
		//System.out.print("\n\nRESULTADO"+wordnetTest.getMeronymsWords(synset));
		assertEquals(result, wordnetTest.getMeronymsWords(synset));
	}
	
	@Test
	public void getMeronymsWordsByOffsetTest() throws JWNLException{
		ArrayList<String> result = new ArrayList<String>();
		result.add("felloe");
		result.add("felly");
		result.add("rim");
		//System.out.print("\n\nRESULTADO"+wordnetTest.getMeronymsWords(synset));
		assertEquals(result, wordnetTest.getMeronymsWords(4582285));
	}
	
	@Test
	public void getMeronymsWordsByWordSenseTest() throws JWNLException{
		ArrayList<String> result = new ArrayList<String>();
		Synset synset = wordnetTest.getSynset("wheel", POS.NOUN, 1);
		result.add("felloe");
		result.add("felly");
		result.add("rim");
		//System.out.print("\n\nRESULTADO"+wordnetTest.getMeronymsWords(synset));
		assertEquals(result, wordnetTest.getMeronymsWords("wheel", 1, POS.NOUN));
	}
	
	@Test
	public void isNotEntityNodeTest(){
		Synset s1 = wordnetTest.getSynset("kitten", POS.NOUN, 1);
		assertEquals(false, wordnetTest.isEntityNode(s1));
	}
	
	@Test
	public void isEntityNodeTest(){
		Synset s1 = wordnetTest.getSynset("entity", POS.NOUN, 1);
		assertEquals(true, wordnetTest.isEntityNode(s1));
	}
	
	@Test
	public void getPathToEntityTest(){
		Synset synset = wordnetTest.getSynset("animal",POS.NOUN, 1);
		ArrayList<Synset> goodresult = new ArrayList<Synset>();
		try {
			goodresult.add(wordnetTest.getSynset(15568));/*animal*/
			goodresult.add(wordnetTest.getSynset(4475));/*organism*/
			goodresult.add(wordnetTest.getSynset(4258));/*living_thing*/
			goodresult.add(wordnetTest.getSynset(3553));/*whole*/
			goodresult.add(wordnetTest.getSynset(2684));/*object*/
			goodresult.add(wordnetTest.getSynset(1930));/*physical_entity*/
			goodresult.add(wordnetTest.getSynset(1740));/*entity*/
			assertEquals(goodresult,wordnetTest.getPathToEntity(synset));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getPathBetweenSynsetsTest(){
		ArrayList<Synset> goodresults = new ArrayList<Synset>();
		Synset s1 = wordnetTest.getSynset("animal", POS.NOUN, 1);
		Synset s2 = wordnetTest.getSynset("cat", POS.NOUN, 1);
		try {
			goodresults.add(wordnetTest.getSynset(15568));/*animal*/
			goodresults.add(wordnetTest.getSynset(1468898));/*chordate*/
			goodresults.add(wordnetTest.getSynset(1474323));/*vertebrate*/
			goodresults.add(wordnetTest.getSynset(1864419));/*mammal*/
			goodresults.add(wordnetTest.getSynset(1889397));/*placental*/
			goodresults.add(wordnetTest.getSynset(2077948));/*carnivore*/
			goodresults.add(wordnetTest.getSynset(2123649));/*feline*/
			goodresults.add(wordnetTest.getSynset(2124272));/*cat*/
			
			assertEquals(goodresults, wordnetTest.getPathBetweenSynsets(s1, s2));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getNumLinksBetweenSynsets(){
		ArrayList<Synset> goodresults = new ArrayList<Synset>();
		int result = 7;
		Synset s1 = wordnetTest.getSynset("animal", POS.NOUN, 1);
		Synset s2 = wordnetTest.getSynset("cat", POS.NOUN, 1);
		try {
			goodresults.add(wordnetTest.getSynset(15568));/*animal*/
			goodresults.add(wordnetTest.getSynset(1468898));/*chordate*/
			goodresults.add(wordnetTest.getSynset(1474323));/*vertebrate*/
			goodresults.add(wordnetTest.getSynset(1864419));/*mammal*/
			goodresults.add(wordnetTest.getSynset(1889397));/*placental*/
			goodresults.add(wordnetTest.getSynset(2077948));/*carnivore*/
			goodresults.add(wordnetTest.getSynset(2123649));/*feline*/
			goodresults.add(wordnetTest.getSynset(2124272));/*cat*/
		
			assertEquals(result, wordnetTest.getNumLinksBetweenSynsets(s1, s2));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void depthOfSynset() throws JWNLException{
		Synset synset = wordnetTest.getSynset("animal",POS.NOUN, 1);
	//	Synset entity = wordnetTest.getSynset(1740);
	
		ArrayList<Synset> goodresult = new ArrayList<Synset>();
		try {
			assertEquals(6,wordnetTest.depthOfSynset(synset));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void depthOfSynsetSubTreeTest(){
		long s1=2124272, s2=2128969;
        try {
			//Synset kitten= wordnetTest.getSynset(s0);
	        Synset cat = wordnetTest.getSynset(s1); 
	        Synset leopard = wordnetTest.getSynset(s2);
	       
	        LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
	        tree = wordnetTest.getSubarbolWordnet(cat,4, tree); //Testado con cat
	        
	        assertEquals(2, wordnetTest.depthOfSynset(leopard, tree));
	        
        } catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
	
	@Test
	public void depthnodesTest(){
		long s1=2124272, s2=2128969;
        try {
	        Synset cat = wordnetTest.getSynset(s1); 
	        Synset leopard = wordnetTest.getSynset(s2);
	       
	        LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
	        tree = wordnetTest.getSubarbolWordnet(cat,4, tree); //Testado con cat
	        ArrayList<Synset> nodeslevel = wordnetTest.getHyponyms(cat);
	        
	        assertEquals(3, wordnetTest.depthnodes(cat, nodeslevel, 0,tree));
	        
        } catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getNodesToEntityTest(){
		Synset synset = wordnetTest.getSynset("animal",POS.NOUN, 1);
		assertEquals(7,wordnetTest.getNodesToEntity(synset));
	}
	
	@Test
	public void getLeastCommonSubsumerTest(){
		Synset s1 = wordnetTest.getSynset("domestic_cat",POS.NOUN, 1);
        Synset s2 = wordnetTest.getSynset("wildcat", POS.NOUN, 3);
     	Synset s3 = wordnetTest.getSynset("cat", POS.NOUN, 1);
        try {
			Synset lcs = wordnetTest.getLeastCommonSubsumer(s1,s2);
	        assertEquals(s3, wordnetTest.getLeastCommonSubsumer(s1, s2));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	@Test
	public void NotContainsFirstInSecondTest(){
		Synset s1 = wordnetTest.getSynset("cat", POS.NOUN, 1);
		//System.out.print("SYNSET-"+s1+"\n");
		Synset s2 = wordnetTest.getSynset("kitten", POS.NOUN, 1);
		//System.out.print("SYNSET-"+s2+"\n");
	    HashSet<Synset> tree1 = wordnetTest.getHypernymTreeList(s1);
	    HashSet<Synset> tree2 = wordnetTest.getHypernymTreeList(s2);
	    
	    //System.out.print("TREE"+tree1+"\n");
	    //System.out.print("TREE2"+tree2+"\n");
	 //   System.out.print("RESULT"+wordnetTest.NotContainsFirstInSecond(tree1, tree2));
	  //  System.out.print("RESULT"+wordnetTest.NotContainsFirstInSecond(tree2, tree1));
	    assertEquals(7,wordnetTest.NotContainsFirstInSecond(tree1, tree2));
	    assertEquals(3,wordnetTest.NotContainsFirstInSecond(tree2, tree1));
	}
	
	@Test
	public void IntersectionTest(){
		Synset s1 = wordnetTest.getSynset("cat", POS.NOUN, 1);
		Synset s2 = wordnetTest.getSynset("kitten", POS.NOUN, 1);
	    HashSet<Synset> tree1 = wordnetTest.getHypernymTreeList(s1);
	    HashSet<Synset> tree2 = wordnetTest.getHypernymTreeList(s2);
	    
	    System.out.print("RESULT"+wordnetTest.Intersection(tree1, tree2));
	    assertEquals(7,wordnetTest.Intersection(tree1, tree2));
	    
	}
	
	@Test
	public void getHypernymTreeListTest(){
		Synset synset = wordnetTest.getSynset("animal",POS.NOUN, 1);
		HashSet<Synset> goodresult = new HashSet<Synset>();
		try {
			goodresult.add(wordnetTest.getSynset(15568));/*animal*/
			goodresult.add(wordnetTest.getSynset(4475));/*organism*/
			goodresult.add(wordnetTest.getSynset(4258));/*living_thing*/
			goodresult.add(wordnetTest.getSynset(3553));/*whole*/
			goodresult.add(wordnetTest.getSynset(2684));/*object*/
			goodresult.add(wordnetTest.getSynset(1930));/*physical_entity*/
			goodresult.add(wordnetTest.getSynset(1740));/*entity*/
			assertEquals(goodresult,wordnetTest.getHypernymTreeList(synset));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getHyponymsTreeListTest(){
		Synset synset = wordnetTest.getSynset("ball",POS.NOUN, 9);
		HashSet<Synset> goodresult = new HashSet<Synset>();
		try {
			//Calculo los hiponimos del un subarbol de nodos hojas
			ArrayList<Synset> synsets = wordnetTest.getHyponyms("ball",9, POS.NOUN);
			//Añado el nodo de inicio de busqueda
			synsets.add(0, synset);
			HashSet<Synset> hashSet1 = new HashSet<Synset>(synsets);

			assertEquals(hashSet1,wordnetTest.getHyponymsTreeList(synset));
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void RecorrerNodosTest(){
		
	}
	
	@Test
	public void getSubarbolWordnetTest(){
		
	}
}
