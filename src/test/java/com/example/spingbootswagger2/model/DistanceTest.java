package com.example.spingbootswagger2.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.example.springbootswagger2.model.Distance;

import com.example.springbootswagger2.model.WordnetLibrary;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Synset;

public class DistanceTest {

	
	WordnetLibrary wordnetTest;
	Distance distance;
	
	
	@Before
	public void initWordnetLibrary(){
		 wordnetTest = new WordnetLibrary();
	}
	
	@Test
	public void WPSimilarityTest() throws JWNLException{
		Synset s1 = wordnetTest.getSynset(2124272);
		Synset s2 = wordnetTest.getSynset(15568);
		//double result =
		System.out.print(distance.WPSimilarity(s1,s2,wordnetTest)+"\n");
	}
	
	@Test
	public void WPSimilaritySubTree(){
		
	}
	
	@Test
	public void Distance_WPTest(){
		
	}
	
	@Test
	public void Sanchez_DistanceSubtreeTest(){
		
	}
	
	@Test
	public void Sanchez_DistanceTest(){
		
	}
	
	@Test
	public void nodeLeafTest(){
		
	}
	@Test
	public void IC_measureTest(){
		
	}
	
	@Test
	public void getLeafWordnetTest(){
		
	}
	
	@Test
	public void resnick_DistanceTest(){
		
	}
	
	@Test
	public void lin_DistanceTest(){
		
	}
	
	@Test
	public void jianCorath_DistanceTest(){
		
	}

}
