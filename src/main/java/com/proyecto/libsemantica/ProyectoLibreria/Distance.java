package com.proyecto.libsemantica.ProyectoLibreria;

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
	 
	 public static void main(String[] arg) throws JWNLException{
	 
	
	 final String word = "cancer", word2 ="disease";
	 final POS pos = POS.NOUN;
	 
	 double wp = WPSimilarity(word, pos, word2, pos);
     System.out.print("\n\nLa similitud de Wu and Palmer es:"+wp+"\n");
     
	 }



}
