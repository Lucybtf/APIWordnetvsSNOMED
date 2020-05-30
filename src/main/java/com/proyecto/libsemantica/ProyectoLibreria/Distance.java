package com.proyecto.libsemantica.ProyectoLibreria;

import java.util.HashSet;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;

public class Distance {

	/// WordnetLibrary dictionary;
	/* Similitud de Wu and Palmer*/
	 public static double WPSimilarity(String word1, POS pos, String word2, POS pos2) {
		 double distance = 0;
		 
		 /*Obtenemos los synsets de Wordnet*/
		 WordnetLibrary dictionary = new WordnetLibrary();
		 Synset s1 = dictionary.getSynset(word1,pos);
		 Synset s2 = dictionary.getSynset(word2,pos2);
		 Synset lcs;
		 /*Calculamos la profundidad de ambos synsets*/
	     int depth, depth2, depthtotal, depthlcs;
		try {
			depth = dictionary.depthOfSynset(s1);
			depth2 = dictionary.depthOfSynset(s2);
			depthtotal = depth+depth2;
			
			 /*Calculamos el Least Common Synset y su profundida*/
			lcs = dictionary.getLeastCommonSubsumer(s1,s2);
		    depthlcs =dictionary.depthOfSynset(lcs);
		    
		    /*Fórmula: 2*depth(LCS)/(depth(S1)+depth(S2)*/
		    distance = (2*depthlcs)/(double)depthtotal;
		   // System.out.print("\nLCS->"+lcs+"DEPTH:"+depthlcs);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		 return distance;
	}
	 
	 
	public static double Sanchez_Similarity(String word1, POS pos, String word2, POS pos2){
		
		  
		 WordnetLibrary dictionary = new WordnetLibrary();
		 Synset s1 = dictionary.getSynset(word1,pos);
		 Synset s2 = dictionary.getSynset(word2,pos2);
		 
		 HashSet<Synset> S1synsets =  dictionary.getHypernymTreeList(s1);
		 HashSet<Synset> S2synsets =  dictionary.getHypernymTreeList(s2);
		
	      
		 int numinsideAinB = dictionary.NotContainsFirstInSecond(S1synsets, S2synsets);
		 int numinsideBinA = dictionary.NotContainsFirstInSecond(S2synsets, S1synsets);;
	     int intersection = dictionary.Intersection(S1synsets, S2synsets);
		 System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		 System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		 System.out.print("\n\nInteresection "+ intersection);
			
		double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
		double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
	//	System.out.print("Distancia de Sanchez:"+distance_sanchez);
	         
		
		return distance_sanchez;
		
	}
	 public static void main(String[] arg) throws JWNLException{
	 
	
	 final String word = "cancer", word2 ="disease";
	 final POS pos = POS.NOUN;
	 
	 double wp = WPSimilarity(word, pos, word2, pos);
     System.out.print("\n\nLa similitud de Wu and Palmer es:"+wp+"\n");
     
     double sanchez = Sanchez_Similarity(word, pos, word2, pos);
     System.out.print("\n\nLa similitud de Sanchez es:"+sanchez+"\n");
     
	 }



}
