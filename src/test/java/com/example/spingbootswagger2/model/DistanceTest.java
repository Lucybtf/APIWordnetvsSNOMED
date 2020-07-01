package com.example.spingbootswagger2.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import com.example.springbootswagger2.model.Distance;

import com.example.springbootswagger2.model.WordnetLibrary;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;

public class DistanceTest {

	
	WordnetLibrary wordnetTest;
	Distance distance;
	
	
	@Before
	public void initWordnetLibrary(){
		 wordnetTest = new WordnetLibrary();
		 distance = new Distance();
	}
	
	
	@Test
	public void WPSimilarityTest() throws JWNLException{
		Synset cat = wordnetTest.getSynset(2124272);
		Synset animal = wordnetTest.getSynset(15568);
		//double result =
		double result = 0.631578947368421;
		//System.out.print("DISTANCIA ARBOL"+distance.WPSimilarity(cat, animal,wordnetTest)+" rsult"+ result+"\n");
		
		assertEquals(result, distance.WPSimilarity(cat, animal,wordnetTest), 0.00);
	}
	
	@Test
	public void WPSimilaritySubTree() throws JWNLException{
		 LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
		 Synset cat = wordnetTest.getSynset(2124272);
		 Synset animal = wordnetTest.getSynset(15568);
		 Synset feline =  wordnetTest.getSynset(2123649);
	     tree = wordnetTest.getSubarbolWordnet(animal,7, tree); //Testado con cat
	//	System.out.print("DISTANCIA SUBARBOL"+distance.WPSimilaritySubTree(feline, cat, tree, wordnetTest));
	     double result =0.8571428571428571;
		assertEquals(result, distance.WPSimilaritySubTree(feline, cat, tree, wordnetTest), 0.00);
	}
	
	@Test
	public void Distance_WPTest() throws JWNLException{
		Synset cat = wordnetTest.getSynset(2124272);
		Synset animal = wordnetTest.getSynset(15568);
		//double result =
		double result = 1 - 0.631578947368421;
		//System.out.print("DISTANCIA ARBOL"+distance.WPSimilarity(cat, animal,wordnetTest)+" rsult"+ result+"\n");
		
		assertEquals(result, 1 -distance.WPSimilarity(cat, animal,wordnetTest), 0.00);
	}
	
	@Test
	public void Distance_WPSubTreeTest() throws JWNLException{
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
		Synset cat = wordnetTest.getSynset(2124272);
		Synset animal = wordnetTest.getSynset(15568);
		Synset feline =  wordnetTest.getSynset(2123649);
	    tree = wordnetTest.getSubarbolWordnet(animal,7, tree);
		//double result =
		double result = 1 - 0.8571428571428571;
		//System.out.print("DISTANCIA ARBOL"+distance.WPSimilarity(cat, animal,wordnetTest)+" rsult"+ result+"\n");

		assertEquals(result, 1 - distance.WPSimilaritySubTree(feline, cat, tree, wordnetTest), 0.00);
	}
	
	
	@Test
	public void Sanchez_DistanceSubtreeTest() throws JWNLException{
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
		Synset animal = wordnetTest.getSynset(15568);
		Synset cat = wordnetTest.getSynset(2124272);
		Synset kitten = wordnetTest.getSynset(2125600);
		
		double result = 0.932885804141463;
		tree = wordnetTest.getSubarbolWordnet(animal,7, tree); //Testado con cat
		//wordnetTest.printTree(tree);
		//System.out.print("\nDISTANCIA SUBARBOL SANCHEZ"+distance.Sanchez_DistanceSubtree(cat, kitten, tree, wordnetTest)+ "RESULT"+ result+"\n");
		assertEquals(result, distance.Sanchez_DistanceSubtree(cat, kitten, tree, wordnetTest), 0.0);
	}
	
	@Test
	public void Sanchez_DistanceTest() throws JWNLException{
		Synset cat = wordnetTest.getSynset(2124272);
		Synset kitten = wordnetTest.getSynset(2125600);
		double result = 0.6674246609131292;
		//System.out.print("DISTANCIA  SANCHEZ"+distance.Sanchez_Distance(cat, kitten, wordnetTest)+"\n");
		assertEquals(result,distance.Sanchez_Distance(cat, kitten, wordnetTest), 0.0);
	}
	/*
	@Test
	public void nodeLeafTest(){
		
	}
	*/
	@Test
	public void IC_measureTest() throws JWNLException, IOException{
		//Synset cat = wordnetTest.getSynset(2124272);
		Synset kitten = wordnetTest.getSynset(2125600);
		double result = 4.77173442538677;
		System.out.print("IC MEASURE"+distance.IC_measure(kitten, wordnetTest)+"\n");
		assertEquals(result,distance.IC_measure(kitten, wordnetTest), 0.0);
		
	}
	
	@Test
	public void IC_measureSubTreeTest() throws JWNLException, IOException{
		Synset cat = wordnetTest.getSynset(2124272);
		Synset wildcat = wordnetTest.getSynset(2125600);
		double result = 1.6020599913279623;
		LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
		tree = wordnetTest.getSubarbolWordnet(cat,7, tree); //Testado con cat
		System.out.print("IC MEASURE"+distance.IC_measureSubTree(wildcat, tree, wordnetTest)+"\n");
		assertEquals(result,distance.IC_measureSubTree(wildcat, tree, wordnetTest), 0.0);
		
	}
	
/*	@Test
	public void getLeafWordnetTest() throws JWNLException{
		Synset cat = wordnetTest.getSynset(2124272);
		Synset kitten = wordnetTest.getSynset(2125600);
		
	}
	*/
	
	@Test
	public void resnisk_DistanceTest() {
		
		try {
			Synset cat = wordnetTest.getSynset(2124272);
			Synset kitten = wordnetTest.getSynset(2125600);
			
			double result = 2.18620045285897;
			System.out.print("resnisk distance"+distance.resnisk_Distance(cat, kitten, wordnetTest)+"\n");
			assertEquals(result, distance.resnisk_Distance(cat, kitten,  wordnetTest), 0.0);
		} catch (JWNLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void lin_DistanceTest() {
		
		try {
		
			Synset cat = wordnetTest.getSynset(2124272);
			Synset kitten = wordnetTest.getSynset(2125600);
			double result = 0.48266399951749667;
			//System.out.print("Lin distance"+distance.lin_Distance(2124272, 2125600));
			assertEquals(result, distance.lin_Distance(cat, kitten,  wordnetTest), 0.0);
		} catch (JWNLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void jianConrath_DistanceTest(){
		try {
			
			Synset cat = wordnetTest.getSynset(2124272);
			Synset kitten = wordnetTest.getSynset(2125600);
			
			System.out.print("\n\nJian and Conrath"+distance.jianConrath_Distance(cat, kitten, wordnetTest)+"\n");
		
		} catch (JWNLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
