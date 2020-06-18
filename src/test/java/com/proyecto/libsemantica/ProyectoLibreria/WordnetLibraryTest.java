package com.proyecto.libsemantica.ProyectoLibreria;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;

public class WordnetLibraryTest {

	WordnetLibrary wordnetTest;
	
	@Before
	public void initWordnetLibrary(){
		 wordnetTest = new WordnetLibrary();
	}
	 
	@Test
	public void getSynsetByOffsetTest() throws JWNLException {
		
		long offset = 2961779;
        
        Synset s0 = wordnetTest.getSynset("car", POS.NOUN);
       // System.out.print("S0"+s0);
        Synset soffset =  wordnetTest.getSynset(offset);
        //System.out.print("S0"+soffset);
        assertEquals(s0,soffset);
	}
	 
	@Test
	public void test() throws JWNLException {
		//fail("Not yet implemented");
		
		 final String word = "canis familiaris";
         final POS pos = POS.NOUN;
         
         
         /*getSynset*/
         Synset s1 =wordnetTest.getSynset("cancer",pos);
         
          
         Synset s2 =wordnetTest.getSynset("disease",pos);
         
         /*SYNONIMOS*/ 
         ArrayList<String> testsynonyms = wordnetTest.getSynonyms(pos, word);
         System.out.print("\nLista synonyms"+testsynonyms+"\n");
         
         /* HIPERNYMS*/
         ArrayList<String> testhypernyms = wordnetTest.getHypernyms(pos, word);
         System.out.print("\nLista hypernyms"+":"+testhypernyms+"\n");
        
         /* HIPONYMS*/
         ArrayList<String> testhyponyms = wordnetTest.getHyponyms(pos, "thing");
         System.out.print("\nLista hyponyms"+":"+testhyponyms+"\n");
        
         /*HOLONYMS*/
         ArrayList<String> testholonyms = wordnetTest.getHolonyms(pos, word);
         System.out.print("\nLista Holonyms"+":"+ testholonyms+"\n");
         
         /*MERONYMS*/
         ArrayList<String> testmeronyms = wordnetTest.getMeronyms(pos, word);
         System.out.print("\nLista Meronyms"+":"+ testmeronyms+"\n");
         
         /*DepthtoEntity*/
         try {
			int depth = wordnetTest.depthOfSynset(s1);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /*Least Common Subsumer*/
        try {
			Synset lcs = wordnetTest.getLeastCommonSubsumer(s1,s2);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        HashSet<Synset> tree1 = wordnetTest.getHypernymTreeList(s1);
        HashSet<Synset> tree2 = wordnetTest.getHypernymTreeList(s2);
        
        int numinsideAinB = wordnetTest.NotContainsFirstInSecond(tree1, tree2);
		int numinsideBinA = wordnetTest.NotContainsFirstInSecond(tree1, tree2);
		int intersection = wordnetTest.Intersection(tree1, tree2);
		
		System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		System.out.print("\n\nInteresection "+ intersection);
		
		double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
		double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
		System.out.print("\nDistancia de Sanchez:"+distance_sanchez);
	}

}
