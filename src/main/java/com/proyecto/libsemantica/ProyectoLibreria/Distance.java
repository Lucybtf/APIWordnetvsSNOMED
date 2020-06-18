package com.proyecto.libsemantica.ProyectoLibreria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;

import jersey.repackaged.com.google.common.collect.Sets;

public class Distance {

	 WordnetLibrary dictionary;
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
			
			 /*Calculamos el Least Common Synset y su profundidad*/
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
	 
	public static double Distance_WP(String word1, POS pos, String word2, POS pos2){
		return 1-WPSimilarity(word1, pos, word2, pos2);
	}
	
	
	public static double Sanchez_Distance(String word1, POS pos, String word2, POS pos2){
		
		  
		 WordnetLibrary dictionary = new WordnetLibrary();
		 Synset s1 = dictionary.getSynset(word1,pos);
		 Synset s2 = dictionary.getSynset(word2,pos2);
		 
		 HashSet<Synset> S1synsets =  dictionary.getHypernymTreeList(s1);
		 HashSet<Synset> S2synsets =  dictionary.getHypernymTreeList(s2);
		
	      
		 int numinsideAinB = dictionary.NotContainsFirstInSecond(S1synsets, S2synsets);
		 int numinsideBinA = dictionary.NotContainsFirstInSecond(S2synsets, S1synsets);
	     int intersection = dictionary.Intersection(S1synsets, S2synsets);
		 System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		 System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		 System.out.print("\n\nInteresection "+ intersection);
			
		double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
		double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
	//	System.out.print("Distancia de Sanchez:"+distance_sanchez);
	         
		
		return distance_sanchez;
		
	}
	
	public static int nodeLeafs(Synset synset) throws JWNLException{
		
		WordnetLibrary dictionary = new WordnetLibrary();
		HashSet<Synset> hyponymsynsets = dictionary.getHyponymsTreeList(synset);
	
		int leaves = 0; 
		for(Synset hypo:hyponymsynsets){
				System.out.print("\nHOJAS"+leaves+"\n");
		    	ArrayList<Synset> hijos = dictionary.getHyponyms(hypo);
		    	
		    	if(hijos.size()==0){
		    	//	System.out.print(leaves+"LISTA HYPONYMS->"+ hypo.getWord(0).getLemma()+" ");
		    		//System.out.print("HIJOS->"+hijos.size()+" ");
		    		leaves=leaves+1;
		    		//System.out.print("HOJAS"+leaves+"\n");
		    	}
		    	
		}
		//System.out.print("HOJAS"+leaves+"\n");
		return leaves;
	}
	
	
	public static double IC_measure(long offset) throws JWNLException, IOException{
		/*IC Sanchez(c) =-log( (|leaves(c)|/|hypernyms(c)|+1)/max_leaves+1))*/
		int leaves = 0;
		WordnetLibrary dictionary = new WordnetLibrary();
		Synset synset = dictionary.getSynset(offset);
		
		HashSet<Synset> hyponymsynsets = dictionary.getHyponymsTreeList(synset);
		leaves = nodeLeafs(synset);
		System.out.print("\nLEAVES FINALES->"+leaves);
		
		HashSet<Synset> hypernymssynsets = dictionary.getHypernymTreeList(synset);
		Set<Synset> alltaxonomy = Sets.union(hyponymsynsets, hypernymssynsets);
	
		
		System.out.print("HIPONIMOS "+hyponymsynsets.size()+"HIPERNOMIMOS "+ hypernymssynsets.size()+"TAM TOTAL "+ alltaxonomy.size());
		int max_leaves =0;
		for(Synset h:alltaxonomy){
	    //	System.out.print("\n\nLISTA HYPONYMS->"+ h.getWord(0).getLemma()+"\t");
	    	ArrayList<Synset> hijosmax = dictionary.getHyponyms(h);
	    	
	    	//System.out.print("HIJOS->"+hijosmax.size()+"\n");
	    	if(hijosmax.size()==0){
	    		max_leaves++;
	    	}
	    	
		}
		
		System.out.print("\nNUMERO MAX DE HOJAS EN LA TAXONOMIA->"+ max_leaves+" NUMERO DE HOJAS->"+leaves+" "+"NUMERO DE SUBSUBMER->"+hypernymssynsets.size());
		double icvalue = 0.0;
		double numer= ((double) leaves/(double)hypernymssynsets.size())+1;
		int denom = 65031+1;
		System.out.print("Numerador->"+numer);//+ "Denominador->"+getLeafsWordnet());
		
		
		double resultdiv = numer/(double)denom;
		System.out.print("RESULTDIV->"+resultdiv);
		icvalue = -Math.log10(resultdiv);
		
		// ArrayList<String> testhyponyms2 = getHyponyms(pos, "thi");
         //System.out.print("\nLista hyponyms"+":"+testhyponyms2+"\n");
		return icvalue;
       
	}
	
	public static int getLeafsWordnet() throws IOException, JWNLException{
		WordnetLibrary dictionary = new WordnetLibrary();
		Synset s1 = dictionary.getSynset("entity", POS.NOUN);
		System.out.print("\nNODOS DE ENTITY");
		
		HashSet<Synset> hyp = dictionary.getHyponymsTreeList(s1);
		System.out.print("TAMAÑO NODOS"+hyp.size()+"\n");
		
		int leavesW = 0;
		//FileWriter myWriter = new FileWriter("./rdf/filename.txt");
		for(Synset a:hyp){
			ArrayList<Synset> hijos = dictionary.getHyponyms(a);
			if(hijos.size()==0)
				leavesW++;
			//myWriter.write("Synset->"+a.getWord(0)+"\n");
		//	ArrayList<String> hypnonymssons = dictionary.getHyponyms(pos, word)
		}
	    //myWriter.close();*/
	    //System.out.println("Successfully wrote to the file.");
		return leavesW;
	}
	
	public static double resnisk_Distance(long offsets1, long offsets2) throws JWNLException, IOException{
		
		WordnetLibrary dictionary = new WordnetLibrary();
		
		Synset s1 = dictionary.getSynset(offsets1);
		Synset s2 = dictionary.getSynset(offsets2);
		//System.out.print("\nSynset s1"+s1+"\n Synset s2"+s2+"\n");
		
		Synset lcs =dictionary.getLeastCommonSubsumer(s1, s2);
		//System.out.print("Synset LCS->"+lcs);
		return IC_measure(lcs.getOffset());
	}
	
	public static double lin_Distance(long offsets1, long offsets2) throws JWNLException, IOException{
		double resultlin = 0.0, num=0.0, div =0.0;
		num = 2* resnisk_Distance(offsets1, offsets2);
		div = IC_measure(offsets1)+ IC_measure(offsets2);
		resultlin= num/div;
		return resultlin;
	}
	
	public static double jianCorath_Distance(long offsets1, long offsets2) throws JWNLException, IOException{
		double resultjc = 0.0, num=0.0, div =0.0;
		div = IC_measure(offsets1)+ IC_measure(offsets2);
		resultjc= div-2*resnisk_Distance(offsets1, offsets2);
		return resultjc;
	}
	
	public static void main(String[] arg) throws JWNLException, IOException{
	 
	
	 final String word = "cancer", word2 ="disease";
	 final POS pos = POS.NOUN;
	 
	/* double wp = WPSimilarity(word, pos, word2, pos);
	 double distance_wp = Distance_WP(word, pos, word2, pos);
     System.out.print("\n\nLa similitud de Wu and Palmer es:"+wp+"\n");
     System.out.print("\n\nLa similitud de Wu and Palmer es:"+distance_wp+"\n");
     
     double sanchez = Sanchez_Similarity(word, pos, word2, pos);
     System.out.print("\n\nLa similitud de Sanchez es:"+sanchez+"\n");
     */
	 
	 long offset =2124272, kitty=2124950, sand_cat=2127662; 
	// System.out.print("IC cat->"+IC_measure(offset));
	 System.out.print("\n\n");
	 double res = resnisk_Distance(kitty,sand_cat);
	 double lin = lin_Distance(kitty,sand_cat);
	 double jc = jianCorath_Distance(kitty,sand_cat);
	 System.out.print("\nMedida RES:"+ res+ "\nMedida Lin:"+lin+ "\nMedida jc:"+jc);
	 
	 }



}
