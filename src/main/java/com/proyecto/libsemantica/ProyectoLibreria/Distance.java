package com.proyecto.libsemantica.ProyectoLibreria;

import net.didion.jwnl.data.POS;

public class Distance {

	
	/* Similitud de Wu and Palmer*/
	 public double wordSimilarity(String word1, POS posWord1, String word2, POS posWord2) {
		 double distance = 0;
	 /*   double maxScore = 0 D;
	    try {
	        WS4JConfiguration.getInstance().setMFS(true);
	        List < Concept > synsets1 = (List < Concept > ) db.getAllConcepts(word1, posWord1.name());
	        List < Concept > synsets2 = (List < Concept > ) db.getAllConcepts(word2, posWord2.name());
	        for (Concept synset1: synsets1) {
	            for (Concept synset2: synsets2) {
	                Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
	                double score = relatedness.getScore();
	                if (score > maxScore) {
	                    maxScore = score;
	                }
	            }
	        }
	        System.out.println("Similarity score of " + word1 + " & " + word2 + " : " + maxScore);
	    } catch (Exception e) {
	        logger.error("Exception : ", e);
	    }*/
		 return distance;
	}

}
