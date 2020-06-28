package com.example.springbootswagger2.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
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
	 public static double WPSimilarity(Synset s1, Synset s2, WordnetLibrary dictionary) {
		 double distance = 0;
		 
		 Synset lcs;
		 
		 /*Calculamos la profundidad de ambos synsets*/
	     int depth, depth2, totallinks, depthlcs, linkss1tolcs, linkss2tolcs;
	     try {
			depth = dictionary.depthOfSynset(s1);
			depth2 = dictionary.depthOfSynset(s2);
			
	
			 /*Calculamos el Least Common Synset y su profundidad*/
			lcs = dictionary.getLeastCommonSubsumer(s1,s2);
		    depthlcs =dictionary.depthOfSynset(lcs);
			
			linkss1tolcs= dictionary.getNumLinksBetweenSynsets(s1, lcs);
	        linkss2tolcs = dictionary.getNumLinksBetweenSynsets(s2, lcs);
			
	        totallinks = (2*depthlcs) + linkss1tolcs + linkss2tolcs;
		    
		    /*Fórmula: 2*depth(LCS)/(depth(S1)+depth(S2)*/
		    distance = (2*depthlcs)/(double) totallinks;
		   // System.out.print("\nLCS->"+lcs+"DEPTH:"+depthlcs);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		 return distance;
	}
	
	 
	public static double WPSimilaritySubTree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree, WordnetLibrary dictionary) throws JWNLException{
		System.out.print("ENTRA EN SUBTREE");
		double result =0.0;
		Synset lcs = dictionary.getLeastCommonSubsumer(s1,s2);//Y si en el subarbol no estuviera el LCS?
		System.out.print("\nLCS->"+lcs);
		if(tree.containsKey(lcs))
		{
			int depthlcs = dictionary.depthOfSynset(lcs, tree);
			int numlinkss1 = dictionary.getNumLinksBetweenSynsets(s1, lcs);
			int numlinkss2 = dictionary.getNumLinksBetweenSynsets(s2, lcs);
			System.out.print("\n\nDEPTH lcs->"+lcs.getWord(0).getLemma()+" "+dictionary.depthOfSynset(lcs, tree)+ " NUMLINKSS1->"+numlinkss1+" NUMLINKSS2->"+numlinkss2);
			result = (double)depthlcs/ (double)(depthlcs+numlinkss1+numlinkss2);
		}
		return result;
		
	}
	
	public static double Distance_WP(Synset s1, Synset s2, WordnetLibrary dictionary){
		return 1-WPSimilarity(s1, s2, dictionary);
	}
	
	public static double Sanchez_DistanceSubtree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree, WordnetLibrary dictionary){
		
	
		Map.Entry<Synset, ArrayList<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, ArrayList<Synset>>>(tree.entrySet())).get(0);
		Synset root = rootnode.getKey();
		HashSet<Synset> S1synsets = new HashSet(dictionary.getPathBetweenSynsets(s1, root));
		System.out.print("\n\nCAMINO DE S1("+s1.getWord(0).getLemma()+") A ROOT("+root.getWord(0).getLemma()+"):"+S1synsets);
		HashSet<Synset> S2synsets =  new HashSet(dictionary.getPathBetweenSynsets(s2, root));
		System.out.print("\n\nCAMINO DE S2("+s2.getWord(0).getLemma()+") A ROOT("+root.getWord(0).getLemma()+"):"+S2synsets);
		
		int numinsideAinB = dictionary.NotContainsFirstInSecond(S1synsets, S2synsets);
		int numinsideBinA = dictionary.NotContainsFirstInSecond(S2synsets, S1synsets);
	    int intersection = dictionary.Intersection(S1synsets, S2synsets);
		
	    System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		System.out.print("\n\nInteresection "+ intersection);
	    
		double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
		double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
		
		return distance_sanchez;
	}
	
	public static double Sanchez_Distance(Synset s1, Synset s2, WordnetLibrary dictionary){
		
		  
	/*	 WordnetLibrary dictionary = new WordnetLibrary();
		 Synset s1 = dictionary.getSynset(word1, pos, sense1);
		 Synset s2 = dictionary.getSynset(word2, pos2, sense2);*/
		 
		 HashSet<Synset> S1synsets =  dictionary.getHypernymTreeList(s1);
		 HashSet<Synset> S2synsets =  dictionary.getHypernymTreeList(s2);
		
	      
		 int numinsideAinB = dictionary.NotContainsFirstInSecond(S1synsets, S2synsets);
		 int numinsideBinA = dictionary.NotContainsFirstInSecond(S2synsets, S1synsets);
	     int intersection = dictionary.Intersection(S1synsets, S2synsets);
		// System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		 //System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		 //System.out.print("\n\nInteresection "+ intersection);
			
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
		Synset s1 = dictionary.getSynset("entity", POS.NOUN, 1);
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
	 
	 WordnetLibrary dictionarymain = new WordnetLibrary();
	 final String word = "kitten", word2 ="cat";
	 final POS pos = POS.NOUN;
	 
	/* double wp = WPSimilarity(word, pos, word2, pos);
	 double distance_wp = Distance_WP(word, pos, word2, pos);
     System.out.print("\n\nLa similitud de Wu and Palmer es:"+wp+"\n");
     System.out.print("\n\nLa similitud de Wu and Palmer es:"+distance_wp+"\n");*/
	 
	
     
    // double sanchez = Sanchez_Similarity(word, pos, word2, pos);
     //System.out.print("\n\nLa similitud de Sanchez es:"+sanchez+"\n");
     
	 
	 long offset =2124272, kitty_offset=2124950, sand_cat_offset=2127662, cat_offset=2124272, party_offset=7462241, chordate_offset =1468898, animal_offset=15568, feline_offset=2123649;
	 Synset cat = dictionarymain.getSynset(cat_offset);
	 Synset sand_cat = dictionarymain.getSynset(sand_cat_offset);
	 Synset kitty = dictionarymain.getSynset(kitty_offset);
	 Synset party = dictionarymain.getSynset(party_offset);
	 Synset chordate = dictionarymain.getSynset(chordate_offset);
	 Synset animal = dictionarymain.getSynset(animal_offset);
	 Synset feline = dictionarymain.getSynset(feline_offset);
	 
	 LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
	 LinkedHashMap<Synset, ArrayList<Synset>> treecat = new LinkedHashMap<Synset, ArrayList<Synset>>();
	 LinkedHashMap<Synset, ArrayList<Synset>> treeanimal = new LinkedHashMap<Synset, ArrayList<Synset>>();
	 
     treecat = dictionarymain.getSubarbolWordnet(feline,4, treecat); //Testado con cat
     //tree = dictionarymain.getSubarbolWordnet(party,4, tree); //Testado con cat
     //treeanimal = dictionarymain.getSubarbolWordnet(animal,9, treeanimal);
    // dictionarymain.printTree(treecat);
    // double wptree = WPSimilaritySubTree(cat,kitty,treecat);
    // System.out.print("\nRES"+wptree);
     
     double wptree =Sanchez_DistanceSubtree(cat,kitty,treecat, dictionarymain);
     System.out.print("\n\nSUBTREE->"+wptree);
   //  System.out.print("\n\nDEPTH SUBARBOL cotillion"+dictionarymain.depthOfSynset(dictionarymain.getSynset(7463637), tree));
    /*System.out.print("\n\nDEPTH SUBARBOL CAT ENCONTRAR FELINE(NO EN EL ARBOL)->"+dictionarymain.depthOfSynset(dictionarymain.getSynset(2123649), treecat));
    System.out.print("\n\nDEPTH SUBARBOL CAT(KITTY)->"+dictionarymain.depthOfSynset(dictionarymain.getSynset(kitty_offset), treecat));
    System.out.print("\n\nDEPTH SUBARBOL CAT(ES LA RAIZ)->"+dictionarymain.depthOfSynset(dictionarymain.getSynset(cat_offset), treecat));*/
     //System.out.print("\n\nDEPTH SUBARBOL ANIMAL"+dictionarymain.depthOfSynset(dictionarymain.getSynset(2127662), treeanimal));
     
    // dictionarymain.printTree(tree);
	// dictionarymain.getSubarbolWordnet(cat,2);
	// Synset s=wordnet
	 //getNodesToEntity(Synset synset)
	//System.out.print("IC cat->"+IC_measure(offset));
	/* System.out.print("\n\n");
	 double res = resnisk_Distance(kitty,sand_cat);
	 double lin = lin_Distance(kitty,sand_cat);
	 double jc = jianCorath_Distance(kitty,sand_cat);
	 System.out.print("\nMedida RES:"+ res+ "\nMedida Lin:"+lin+ "\nMedida jc:"+jc);
	 */
	 }





}
