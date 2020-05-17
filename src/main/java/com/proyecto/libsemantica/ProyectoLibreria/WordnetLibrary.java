package com.proyecto.libsemantica.ProyectoLibreria;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;

public class WordnetLibrary {

	private static final Logger logdescription = LoggerFactory.getLogger(WordnetLibrary.class.getName());
	private String propertiesFile = "C:\\Users\\67382523\\workspace_tfm\\ProyectoLibreria\\config\\file_properties.xml";
	private static Dictionary dic;
	
	/* WordnetLibrary(): Constructor de la libreria*/
	WordnetLibrary(){
		  try{
	          JWNL.initialize(new FileInputStream(propertiesFile));
	          dic = Dictionary.getInstance();
	        }catch(Exception e){
	         // logdescription.error(e.toString());
	        }
	}
	
	 /* getSenses: Descripciones de un palabra(word) por tipo de categoría sintáctica(pos)*/
	static void getSenses(POS pos,String word){
		try {
			//Obtenemos el índice de la palabra
			final IndexWord index = dic.lookupIndexWord(pos, word);
			 System.out.print("index"+index);
			if(index == null) System.out.println("No synsets found for '" + word + "/" + pos.getKey() + "'");
			else{
				//Obtenemos todos los resultados de una palabra
				final Synset[] senses = index.getSenses();
				System.out.print("Senses Vector"+index.getSenses());
                int i = 1;
                for (final Synset synset : senses) {

                    final String gloss = synset.getGloss();
                    System.out.println("Senses:"+gloss);
                    final List<String> words = new ArrayList<String>();
                    for (final Word synonyn : synset.getWords())
                    {
                        words.add(synonyn.getLemma() + "(" + synonyn.getPOS().getKey()+ ")");
                    }
                  //  System.out.println(String.format("%2d Lemmas: %s Description: %s", i++, words, gloss));
                }
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			logdescription.error(e.toString());
		}
	}
	

	public static Synset getSynset(String word, POS pos) {
		// TODO Auto-generated method stub
		Synset synset = null;
		try {
			IndexWord index = dic.lookupIndexWord(pos, word);
			synset =index.getSense(1);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return synset;
	}
	
	/* getNumberSenses: Obtienes el número de significados de cada palabra*/
	public static int getNumberSenses(POS pos, String word){
		IndexWord i = null;
		try {
		 i = dic.getIndexWord(pos, word);
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i.getSenseCount();
		
	}
	
	/*getLemma: Las distintas formas de obtener una forma de una palabra*/
	public static String getLemma(POS pos, String word){
		if (dic == null) return null;
		
		IndexWord i = null;
		try {
			i = dic.lookupIndexWord(pos, word);
		} catch (JWNLException e) {}
		if (i == null) return null;
		
		String lemma = i.getLemma();
		//lemma = lemma.replace("_", " ");
		
		return lemma;
	}
	
	/*getLemma: Las distintas formas de obtener una forma de una palabra*/
	public static ArrayList<String> getLemmas(ArrayList<Synset> synsets){
		HashSet<String> lemmaSet = new HashSet<String>();
		ArrayList<String> lemmasaux = new ArrayList<String>();
		for (Synset synset : synsets) {

			Word[] words = synset.getWords();
			
			for(int i=0;i<words.length;i++){
				lemmasaux.add(words[i].getLemma());
			}
			
			for(String lemma:lemmasaux) lemmaSet.add(lemma);
			//System.out.print("Lemmaaux:"+synsets.size());
			
		}
		
		
		/*new String[lemmaSet.size()]*/
		return new ArrayList<String>(lemmaSet);
	}
	
	
	/*getSynset: Obtiene el synset de la palabra*/
	
	
	/*Relaciones entre Synsets:
	 * 1. Sinonimos
	 * 2. Antonimos
	 * 3. Hiponimos/hiperonimos Ex.perro/animal
	 * 4. Holonimo/Meronimo
	 * 5. Cohiponimia*/
	
	
	public static ArrayList<String> getSynonyms(POS pos, String word){
		
		ArrayList<String> synonyms = new ArrayList<String>();
		Synset[] s = null;
		if(dic != null){
			try {
				IndexWord i=dic.lookupIndexWord(pos, word);
				if(i != null) 
				s = i.getSenses();
				for(Synset a:s){
					//System.out.print(a.getWords());
					Word[] antonyms = a.getWords();
					for(Word w1: antonyms){
					//	System.out.print("Palabra: "+w1.getLemma()+"\n");
						String syn = w1.getLemma();
						if(!synonyms.contains(syn)){
							synonyms.add(syn);
						}
					}
				}
				
			} catch (JWNLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return synonyms;
		}
		else return null;
	}

	public static ArrayList<String> getHyperyms(POS pos, String word){
		ArrayList<String> hypernyms = null;
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		try {
			//Obtenemos el primer Synset
			IndexWord i = dic.getIndexWord(pos, word);
			Synset s = i.getSense(1);
			
			if(s == null) return null;
			
			PointerTargetNodeList listhypernyms = PointerUtils.getInstance().getDirectHypernyms(s);
			for(int it = 0; it<listhypernyms.size();it++){
				PointerTargetNode node = (PointerTargetNode) listhypernyms.get(it);
				Synset current = node.getSynset();
				hyp.add(current);
			}
			hypernyms = getLemmas(hyp);
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hypernyms;
	}
	
	public static ArrayList<String> getHyponyms(POS pos, String word){
		ArrayList<String> hyponyms = new ArrayList<String>();
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		try {
			IndexWord i = dic.getIndexWord(pos, word);
			Synset s = i.getSense(1);
			if(s == null) return null;
			
			PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHyponyms(s);
			//System.out.print("TAM"+listhyponyms.size());
			for(int it = 0; it<listhyponyms.size();it++){
				PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
				Synset current = node.getSynset();
				//System.out.print("Current"+current+"\n");
				hyp.add(current);
				
			}
			hyponyms = getLemmas(hyp);
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hyponyms;
		
	}

	public static ArrayList<String> getListofWords(PointerTargetNodeList list){	
		ArrayList<String> result = new ArrayList<String>();
		Iterator it = list.iterator();
		while(it.hasNext()){
			PointerTargetNode listaux =(PointerTargetNode)it.next();
			//System.out.print("\nLISTA:\n"+listaux.getSynset());
			Word[] wArr = listaux.getSynset().getWords();
			for(Word w:wArr){
				// System.out.print("\t" + w.getLemma().replace("_", " "));
				 result.add(w.getLemma().replace("_", " "));
			}
			
		}
		return result;
	}
	
	public static ArrayList<String> getHolonyms(POS pos, String word){
		ArrayList<String> holonyms = new ArrayList<String>();
		IndexWordSet i = null;
		
		try {
			i =dic.lookupAllIndexWords(word);
		//	System.out.print("IndexWordSet"+i+"\n");
			IndexWord[] indexwords = i.getIndexWordArray();
			for(int a=0;a < indexwords.length;a++){
				IndexWord indexWord = indexwords[a];
			//	System.out.print("\nLemmma:"+indexwords[a].getLemma()+"("+indexwords[a].getPOS()+") ->"+"num senses"+indexWord.getSenseCount()+"\n");
				for(int b=1; b<=indexWord.getSenseCount();b++){
					Synset syn = indexWord.getSense(b);
					PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartHolonyms(syn);
					PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberHolonyms(syn);
					PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartHolonyms(syn);
					
					/*System.out.print("PART OF"+listpartof.size()+"\n");
					System.out.print("MEMBER OF"+listmemberof.size()+"\n");
					System.out.print("SUBSTANCE OF"+listsubtanceof.size()+"\n");*/
					/*if(listpartof.size()>0){
						System.out.print("Glosa:"+syn.getGloss()+"\n");
					}*/
					holonyms.addAll(getListofWords(listpartof));
					holonyms.addAll(getListofWords(listmemberof));
					holonyms.addAll(getListofWords(listsubtanceof));	
				}
				
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return holonyms;
	}




	public static ArrayList<String> getMeronyms(POS pos, String word){
		ArrayList<String> meronyms = new ArrayList<String>();
		IndexWordSet i = null;
		
		try {
			i =dic.lookupAllIndexWords(word);
		//	System.out.print("IndexWordSet"+i+"\n");
			IndexWord[] indexwords = i.getIndexWordArray();
			for(int a=0;a < indexwords.length;a++){
				IndexWord indexWord = indexwords[a];
			//	System.out.print("\nLemmma:"+indexwords[a].getLemma()+"("+indexwords[a].getPOS()+") ->"+"num senses"+indexWord.getSenseCount()+"\n");
				for(int b=1; b<=indexWord.getSenseCount();b++){
					Synset syn = indexWord.getSense(b);
					PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartMeronyms(syn);
					PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberMeronyms(syn);
					PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartMeronyms(syn);
					
				/*	System.out.print("PART OF"+listpartof.size()+"\n");
					System.out.print("MEMBER OF"+listmemberof.size()+"\n");
					System.out.print("SUBSTANCE OF"+listsubtanceof.size()+"\n");*/
					/*if(listpartof.size()>0){
						System.out.print("Glosa:"+syn.getGloss()+"\n");
					}*/
					meronyms.addAll(getListofWords(listpartof));
					meronyms.addAll(getListofWords(listmemberof));
					meronyms.addAll(getListofWords(listsubtanceof));	
				}
				
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return meronyms;
	}
	
	public static boolean isEntityNode(Synset s){
		//if(s.getWord(0).getLemma().equals("entity")?true:false);
		return ((s.getWord(0).getLemma().equals("entity"))?true:false);
	}
	
	public static ArrayList<Synset> getPathToEntity(Synset s){
		ArrayList<Synset> nodepath = new ArrayList<Synset>();
		try {
			if(isEntityNode(s)){
				nodepath.add(s);
				return nodepath;
			}
			PointerTargetTree tree = PointerUtils.getInstance().getHypernymTree(s);
			PointerTargetNodeList l = (PointerTargetNodeList)tree.toList().get(0);
			for(int i= 0; i<l.size(); i++){
				PointerTargetNode p = (PointerTargetNode)l.get(i);
				nodepath.add(p.getSynset());
				
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return nodepath;
	}	
	

	public static ArrayList<Synset> getPathBetweenSynsets(Synset synset1, Synset synset2){
		ArrayList<Synset> pathsynsets = new ArrayList<Synset>();
		try {			
			if(isEntityNode(synset1) && isEntityNode(synset2)){
				pathsynsets.add(synset1);
				System.out.print("ENTITY LOS DOS");
				return pathsynsets;
			}
			if(isEntityNode(synset1)){
				pathsynsets = getPathToEntity(synset2);
				System.out.print("ENTITY DOS");
				return pathsynsets;
			}
			if(isEntityNode(synset2)){
				pathsynsets = getPathToEntity(synset1);
				System.out.print("ENTITY UNO");
				return pathsynsets;
			}
			/*Calculamos la relacion*/
			System.out.print("\n\nCalculamos la relación si no es la raíz:\n");
			RelationshipList list = RelationshipFinder.getInstance().findRelationships(synset1, synset2, PointerType.HYPERNYM);
			System.out.print("LISTA"+list.size());
			PointerTargetNodeList l = ((Relationship) list.get(0)).getNodeList();
			
			for(int i= 0; i<l.size(); i++){
				PointerTargetNode p = (PointerTargetNode)l.get(i);
				//System.out.print("\nCURRENT:"+ p.getSynset()+"\n");
				pathsynsets.add(p.getSynset());
				System.out.print("\nNODE"+i+":"+p.getSynset().getWord(0).getLemma());
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return pathsynsets;
	}
	
	
	public static int depthOfSynset(Synset a) throws JWNLException{
		
		PointerTargetTree tree;
		int max = -999;
		try {
			tree = PointerUtils.getInstance().getHypernymTree(a);
			List<PointerTargetNodeList> treeList = tree.toList();
			for(PointerTargetNodeList nodelist:treeList){
				//System.out.print("NODELIST:"+nodelist.size()+"\n");
				max = Math.max(max, nodelist.size()+1);
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return max;
	}
	
	public static Synset getLeastCommonSubsumer(Synset a, Synset b) throws JWNLException {
		
		int depth, depthMin;
		Synset lcssynset = null ;
		
		ArrayList<Synset> nodepath = getPathBetweenSynsets(a, b);
		//System.out.print("\n\n Calculamos el PATH DE A a B:"+path);
		depthMin = 10000;
	
		for (Synset node : nodepath){
			System.out.print("\nSYNSET:"+node.getWord(0).getLemma()+"----->"+depthOfSynset(node)+"\n");
			try {
				depth = depthOfSynset(node);
				if(depth <= depthMin){
					depthMin = depth;
					lcssynset = node;
				}
			} catch (JWNLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return lcssynset;
	}
	

	
	 public static void main(String[] arg) throws JWNLException{
	        WordnetLibrary w = new WordnetLibrary();
	        logdescription.debug("HOLA CREADO WORDNET");
	        
	        final String word = "canis familiaris";
            final POS pos = POS.NOUN;
    
           /*SYNONIMOS*/ 
           ArrayList<String> testsynonyms = getSynonyms(pos, word);
           System.out.print("\nLista synonyms"+testsynonyms+"\n");
      
            
           /* HIPERNYMS*/
            ArrayList<String> testhypernyms = getHyperyms(pos, word);
            System.out.print("\nLista hypernyms"+":"+testhypernyms+"\n");
         
           
           /* HIPONYMS*/
           ArrayList<String> testhyponyms = getHyponyms(pos, word);
           System.out.print("\nLista hyponyms"+":"+testhyponyms+"\n");
           
           /*HOLONYMS*/
           ArrayList<String> testholonyms = getHolonyms(pos, word);
          System.out.print("\nLista Holonyms"+":"+ testholonyms+"\n");

          ArrayList<String> testmeronyms = getMeronyms(pos, word);
          System.out.print("\nLista Meronyms"+":"+ testmeronyms+"\n");
          
          Synset s1 =getSynset("cancer",pos);
          //System.out.print("Synset\n"+boat);
           
          Synset s2 =getSynset("disease",pos);
          //System.out.print("Synset\n"+auto);
          
          
       //  Synset lcs = getLeastCommonSubsumer(boat,auto);
         //1. Calculamos el depth de ambos synsets
          //El ejemplo lo hemos sacamos de uno encontrado por internet
          //Fuente: https://blog.thedigitalgroup.com/words-similarityrelatedness-using-wupalmer-algorithm
         int depth = depthOfSynset(s1);
         int depth2 = depthOfSynset(s2);
          
         System.out.print("PROFUNDIDAD CANCER->:"+depth+"PROFUNDIDAD DISEASE->"+depth2);
         
         //2.Calculamos el LCS y calculamos el depth
         Synset lcs = getLeastCommonSubsumer(s1,s2);
         System.out.print("\nLCS->"+lcs.getWord(0).getLemma()+"DEPTH:"+depthOfSynset(lcs));
         
        
	 }



}
