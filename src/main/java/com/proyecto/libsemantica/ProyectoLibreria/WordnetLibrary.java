package com.proyecto.libsemantica.ProyectoLibreria;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.tree.TreeNode;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jersey.repackaged.com.google.common.collect.Sets;
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
import net.didion.jwnl.util.TypeCheckingList;

public class WordnetLibrary {

	private static final Logger logdescription = LoggerFactory.getLogger(WordnetLibrary.class.getName());
	private String propertiesFile = "C:\\Users\\67382523\\workspace_tfm\\ProyectoLibreria\\config\\file_properties.xml";
	private Dictionary dic;
	
	/* WordnetLibrary(): Constructor de la libreria*/
	WordnetLibrary(){
		  try{
	          JWNL.initialize(new FileInputStream(propertiesFile));
	          dic = Dictionary.getInstance();
	        }catch(Exception e){
	          logdescription.error(e.toString());
	        }
	}
	
	
	public Synset getSynset(long offSet) throws JWNLException{
		
		return dic.getSynsetAt(POS.NOUN, offSet);
	}

	public Synset getSynset(String word, POS pos) {
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
	
	
	public String  getSense(Synset synset){
		return synset.getGloss();
	}
	
	public String  getSense(long offset) throws JWNLException{
		System.out.print("SYNSET"+offset);
		Synset s =dic.getSynsetAt(POS.NOUN, offset);
		String sense =s.getGloss();
		System.out.print(sense);
		return sense;
	}
	
	
	 /* getSenses: Descripciones de un palabra(word) por tipo de categoría sintáctica(pos)*/
	 public void getSense(POS pos,String word, int sense){
		try {
			//Obtenemos el índice de la palabra
			final IndexWord index = dic.lookupIndexWord(pos, word);
			 System.out.print("index"+index);
			if(index == null) System.out.println("No synsets found for '" + word + "/" + pos.getKey() + "'");
			else{
				//Obtenemos todos los resultados de una palabra
				final Synset[] senses = index.getSenses();
				System.out.print("Senses Vector"+index.getSenses());
               int i = sense;
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


	
	/* getNumberSenses: Obtienes el número de significados de cada palabra*/
	public int getNumberSenses(POS pos, String word){
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
	public String getLemma(POS pos, String word){
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
	public  ArrayList<String> getLemmas(ArrayList<Synset> synsets){
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
	
	
	public ArrayList<String> getSynonyms(POS pos, String word){
		
		ArrayList<String> synonyms = new ArrayList<String>();
		Synset[] s = null;
		if(dic != null){
			try {
				IndexWord i=dic.lookupIndexWord(pos, word);
				if(i != null) 
				s = i.getSenses();
				for(Synset a:s){
					//System.out.print(a.getWords());
					Word[] words = a.getWords();
					for(Word w1: words){
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
	
	
	public ArrayList<String> getSynonyms(Synset synset){
		ArrayList<String> synonyms = new ArrayList<String>();
		Word[] words = synset.getWords();
		for(Word w1: words){
		//	System.out.print("Palabra: "+w1.getLemma()+"\n");
			String syn = w1.getLemma();
			if(!synonyms.contains(syn)){
				synonyms.add(syn);
			}
		}
		return synonyms;
	}
	
	public ArrayList<String> getSynonyms(long offset) throws JWNLException{
		
		ArrayList<String> synonyms = new ArrayList<String>();
		Synset synset = getSynset(offset);
		Word[] words = synset.getWords();
		for(Word w1: words){
		
			String syn = w1.getLemma();
			if(!synonyms.contains(syn)){
				synonyms.add(syn);
			}
		}
		return synonyms;
	}
	
	public ArrayList<String> getHypernyms(POS pos, String word){
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
	
	
	public ArrayList<Synset> getHypernyms(Synset synset) throws JWNLException{
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHypernyms(synset);
		//	System.out.print("TAM"+listhyponyms.size());
		for(int it = 0; it<listhyponyms.size();it++){
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
				//System.out.print("Current"+current+"\n");
			hyp.add(current);
				
		}
		return hyp;
	}
	
	public void getHypernyms(long offset){
		
		
	}
	
	public ArrayList<String> getHyponyms(POS pos, String word){
		ArrayList<String> hyponyms = new ArrayList<String>();
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		Synset[] synsets = null;
		try {
			IndexWord i = dic.getIndexWord(pos, word);
			//System.out.print("\n\n I"+i);
			if(i !=null){
			synsets = i.getSenses();
			for(Synset a:synsets){
				//System.out.print("SENTIDOS"+i.getSenseCount());
				//Synset s = i.getSense(1);
				if(a == null) return null;
			//	System.out.print("\n\nTHING"+a.toString());
				
				PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHyponyms(a);
			//	System.out.print("TAM"+listhyponyms.size());
				for(int it = 0; it<listhyponyms.size();it++){
					PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
					Synset current = node.getSynset();
					//System.out.print("Current"+current+"\n");
					hyp.add(current);
					
				}
			}
			
			}
			hyponyms = getLemmas(hyp);
		
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hyponyms;
		
	}
		
	public ArrayList<Synset> getHyponyms(Synset synset) throws JWNLException{
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHyponyms(synset);
		//	System.out.print("TAM"+listhyponyms.size());
		for(int it = 0; it<listhyponyms.size();it++){
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
				//System.out.print("Current"+current+"\n");
			hyp.add(current);
				
		}
		return hyp;
		
	}
	
	public ArrayList<Synset> getHyponyms(long offset) throws JWNLException{
		Synset synset =getSynset(offset);
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHyponyms(synset);
		//	System.out.print("TAM"+listhyponyms.size());
		for(int it = 0; it<listhyponyms.size();it++){
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
				//System.out.print("Current"+current+"\n");
			hyp.add(current);
				
		}
		return hyp;
	}
	
	public ArrayList<String> getListofWords(PointerTargetNodeList list){	
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
	
	public ArrayList<String> getHolonyms(POS pos, String word){
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

	public ArrayList<String> getHolonyms(Synset synset) throws JWNLException{
		
		ArrayList<String> holonyms = new ArrayList<String>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartHolonyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberHolonyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartHolonyms(synset);
	
		holonyms.addAll(getListofWords(listpartof));
		holonyms.addAll(getListofWords(listmemberof));
		holonyms.addAll(getListofWords(listsubtanceof));
		return holonyms;
	}
	
	public ArrayList<String> getHolonyms(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		ArrayList<String> holonyms = new ArrayList<String>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartHolonyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberHolonyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartHolonyms(synset);
	
		holonyms.addAll(getListofWords(listpartof));
		holonyms.addAll(getListofWords(listmemberof));
		holonyms.addAll(getListofWords(listsubtanceof));
		return holonyms;
		
	}

	public ArrayList<String> getMeronyms(POS pos, String word){
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
	
	public ArrayList<String> getMeronyms(Synset synset) throws JWNLException{
		ArrayList<String> meronyms = new ArrayList<String>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartMeronyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberMeronyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartMeronyms(synset);
	
		meronyms.addAll(getListofWords(listpartof));
		meronyms.addAll(getListofWords(listmemberof));
		meronyms.addAll(getListofWords(listsubtanceof));	
		return meronyms;
	}
	
	public void getMeronyms(long offset){
		
		
	}
	
	public boolean isEntityNode(Synset s){
		//if(s.getWord(0).getLemma().equals("entity")?true:false);
		return ((s.getWord(0).getLemma().equals("entity"))?true:false);
	}
	
	public ArrayList<Synset> getPathToEntity(Synset s){
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
	

	public ArrayList<Synset> getPathBetweenSynsets(Synset synset1, Synset synset2){
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
			System.out.print("LISTA"+list.size()+"LIST"+list.get(0));
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
	
	public int getNumLinksBetweenSynsets(Synset s1, Synset s2){
		return getPathBetweenSynsets(s1, s2).size()-1;
	}
	
	public int depthOfSynset(Synset a) throws JWNLException{
		
		PointerTargetTree tree;
		int max = -999;
		try {
			tree = PointerUtils.getInstance().getHypernymTree(a);
			List<PointerTargetNodeList> treeList = tree.toList();
			for(PointerTargetNodeList nodelist:treeList){
				System.out.print("NODELIST:"+nodelist.size()+"\n");
				max = Math.max(max, nodelist.size()+1);
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return max;
	}
	
	public int depthOfSynset(Synset a, LinkedHashMap<Synset, ArrayList<Synset>> tree) throws JWNLException{
		System.out.print("\n\nENTRA EN 1 function");
		 //Set<Entry<Synset, ArrayList<Synset>>> mapSet = tree.entrySet();
	    int depth = 0;
		Map.Entry<Synset, ArrayList<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, ArrayList<Synset>>>(tree.entrySet())).get(0);
		if(!tree.containsKey(a))
			return -1;
		if(rootnode.getKey().equals(a))
			return 0;
		else
		//System.out.print("\n\ndepth FINAL"+depthnodes(a, rootnode.getValue(), depth+1,tree)+"\n");
		return depthnodes(a, rootnode.getValue(), 1,tree);
		//	System.out.print("\n\nDEPTH"+depth);
		//}		
		//return depth;
	}
	
	

	private int depthnodes(Synset a, ArrayList<Synset> nodeslevel, int depth, LinkedHashMap<Synset, ArrayList<Synset>> tree) {
		// TODO Auto-generated method stub
		System.out.print("\nENTRA NODOS HIJOS->"+ nodeslevel.size()+" depth"+depth+" synset a "+ a.getWord(0).getLemma()+ a.getOffset()+"\n");
	   ArrayList<Synset> newlevelnodes = new ArrayList<Synset>();
	   if(nodeslevel.size()>0){
	   for(Synset node: nodeslevel){
		   ArrayList<Synset> hijos =tree.get(node);
		 //  System.out.print("HIJOS->"+tree.get(node));
		   if(hijos !=null)
			   newlevelnodes.addAll(hijos);
	   }
	 //  System.out.print("\nSIGUIENTE NIVEL->"+ newlevelnodes+"\n");
		   //System.out.print("Nood hijos llenos"+nodeslevel+"\n");
	   if(!nodeslevel.contains(a)){
		 
		   System.out.print("Not Contains"+depth);
		  
		    return depthnodes(a, newlevelnodes, depth+1,tree);
		  
	   }
	   }
	   
	   
	   
	  
	   System.out.print("DEpTHNODOS->"+depth);
	   return depth;
	}


	public int getNodesToEntity(Synset synset){
		if(synset.getWord(0).toString().equalsIgnoreCase("entity")) return 1;
		try {
			PointerTargetTree pt = PointerUtils.getInstance().getHypernymTree(synset);
			PointerTargetNodeList l = (PointerTargetNodeList)pt.toList().get(0);
			return l.size();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public Synset getLeastCommonSubsumer(Synset a, Synset b) throws JWNLException {
		
		int depth, depthMin;
		Synset lcssynset = null ;
		
		ArrayList<Synset> nodepath = getPathBetweenSynsets(a, b);
		//System.out.print("\n\n Calculamos el PATH DE A a B:"+path);
		depthMin = 10000;
	
		for (Synset node : nodepath){
			System.out.print("\nSYNSET:"+node.getWord(0).getLemma()+"----->"+depthOfSynset(node)+"\n");
			//depth = depthOfSynset(node);
			depth = getNodesToEntity(node);
			if(depth <= depthMin){
				depthMin = depth;
				lcssynset = node;
			}
			
		}
		return lcssynset;
	}
	
	public int NotContainsFirstInSecond(HashSet<Synset> a, HashSet<Synset> b) {
		// TODO Auto-generated method stub
		int num = 0;
		for(Synset syn:a){
			
			if(!b.contains(syn)){
				num++;
			}
		}
		return num;
	}

	public int Intersection(HashSet<Synset> a, HashSet<Synset> b){
		int num = 0;
		for(Synset syn:a){
			
			if(b.contains(syn)){
				num++;
			}
		}
		return num;
	}
	
	/*Repasar valores del árbol*/
	public HashMap<Long, Synset> getHypernymTree(Synset synset){
		
		HashMap<Long, Synset> list = new HashMap<Long, Synset>();
		try {	
			PointerTargetTree tree = PointerUtils.getInstance().getHypernymTree(synset);
			@SuppressWarnings("unchecked")
			List<PointerTargetNodeList> branchList = tree.toList();			
			for ( PointerTargetNodeList nodeList : branchList ){				
				for ( int i = 0; i < nodeList.size(); i++ ){
					 PointerTargetNode node = (PointerTargetNode) nodeList.get(i);
					 Synset s = node.getSynset();
					// System.out.println("WORD: " + s.getWord(0).getLemma());
					 //System.out.println("KEY: " + s.getKey() +" " + list.get(s.getKey())+"\n");
					 if (list.get(s.getKey())==null){
						 list.put((Long)s.getKey(), s);				 
						 //System.out.println("**: " + s + "\n");
						
					 }
				 }
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return list;
	}


	/*Repasar valores del árbol*/
	public  HashSet<Synset> getHypernymTreeList(Synset synset){
		
		HashSet<Synset> list = new HashSet<Synset>();
		try {	
			PointerTargetTree tree = PointerUtils.getInstance().getHypernymTree(synset);
			@SuppressWarnings("unchecked")
			List<PointerTargetNodeList> branchList = tree.toList();			
			for ( PointerTargetNodeList nodeList : branchList ){				
				for ( int i = 0; i < nodeList.size(); i++ ){
					 PointerTargetNode node = (PointerTargetNode) nodeList.get(i);
					 Synset s = node.getSynset();
						 list.add(s); 
				 }
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/*Repasar valores del árbol*/
	public HashSet<Synset> getHyponymsTreeList(Synset synset){
		
		HashSet<Synset> list = new HashSet<Synset>();
		try {	
			PointerTargetTree tree = PointerUtils.getInstance().getHyponymTree(synset);
			@SuppressWarnings("unchecked")
			List<PointerTargetNodeList> branchList = tree.toList();			
			for ( PointerTargetNodeList nodeList : branchList ){				
				for ( int i = 0; i < nodeList.size(); i++ ){
					 PointerTargetNode node = (PointerTargetNode) nodeList.get(i);
					 Synset s = node.getSynset();
					 list.add(s);
				 }
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<Synset> RecorrerNodos(int pos, ArrayList<Synset> hijos,int altura, LinkedHashMap<Synset, ArrayList<Synset>> treeResult) throws JWNLException{
		
		if (pos < hijos.size()) { 
			//System.out.print("\nNODO->"+hijos.get(pos).getWord(0).getLemma()+ "  HIJOS"+hijos.size()+" ALTURA->"+altura);
			
			if( altura>=0){
				treeResult.put(hijos.get(pos), getHyponyms(hijos.get(pos)));
				//System.out.print("\nALTURA DENTRO DE RECORRER NODOS->"+altura+"\n");
				getSubarbolWordnet(hijos.get(pos),altura, treeResult);
				RecorrerNodos(pos+1,hijos,altura,treeResult);
			}
           
        }
		//if(pos == hijos.size()) System.out.print("FIN ALTURA\n\n");//printTree(treeResult);
		
		return hijos;
    }
	
	public  LinkedHashMap<Synset, ArrayList<Synset>> getSubarbolWordnet(Synset s,  int altura, LinkedHashMap<Synset, ArrayList<Synset>> treeResult) throws JWNLException{
		ArrayList<Synset> hijos = new ArrayList <Synset>();

		if( altura>=0){
			hijos = getHyponyms(s);
			treeResult.put(s, hijos);
			altura--;
			//System.out.print("ALTURA EN SUBARBOL->"+altura+"\n\n");
			hijos=RecorrerNodos(0, hijos, altura, treeResult);
		}
		return treeResult;
		
	}
	
	public void printTree(Map<Synset, ArrayList<Synset>> treeResult){
		 treeResult.forEach((key, value) -> System.out.println(key.getWord(0).getLemma() + ":" + value));
	}
	
	 public static void main(String[] arg) throws JWNLException{
	        WordnetLibrary w = new WordnetLibrary();
	        logdescription.info("HOLA CREADO WORDNET");
	        
	        final String word = "canis familiaris";
            final POS pos = POS.NOUN;
            
            long offset = 2961779;
           
            /*Synset s1 = getSynset(offset);
            System.out.print("SENSES SYNSET\n");
            getSense(s1);
            System.out.print("\n\nSENSES OFFSET\n");
            getSense(offset);
            System.out.print("\n\n\nObtener el synset del offset:"+s1 );*/
            long s0 =2125600, s1=2124272, s3=6422547;
            Synset kitten=  w.getSynset(s0);
            Synset cat = w.getSynset(s1); 
            Synset book =w.getSynset(s3);
           /* 
            Synset lcs = w.getLeastCommonSubsumer(kitten,cat);
            System.out.print("LCS->"+lcs);
            ArrayList<Synset> nodoskitten = w.getPathBetweenSynsets(kitten, lcs);
            ArrayList<Synset> nodoscat = w.getPathBetweenSynsets(cat, lcs);
            
            int numlinks_kitten= w.getNumLinksBetweenSynsets(kitten, lcs);
            int numlinks_cat = w.getNumLinksBetweenSynsets(cat, lcs);*/
          //  System.out.print("NUMERO DE ENLACES a KITTEN->"+numlinks_kitten+"\n"+"NUMERO DE ENLACES a KITTEN->"+numlinks_cat);
            LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
            tree = w.getSubarbolWordnet(cat,4, tree); //Testado con cat
         //   w.printTree(tree);
           //System.out.print("\nSUBARBOL:"+ getSubarbolWordnet(cat,2));
            /*LinkedHashMap<Synset,ArrayList<Synset>> treeResult= new LinkedHashMap<Synset,ArrayList<Synset>>();
            treeResult =  w.getSubarbolWordnet(cat,3, treeResult);
            System.out.print("\n\nPINTAR ARBOL RESULTADO\n\n");
            treeResult.forEach((key, value) -> System.out.println(key.getWord(0).getLemma() + ":" + value));
		*/
	 }
	



}
