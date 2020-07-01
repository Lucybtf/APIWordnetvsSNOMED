package com.example.springbootswagger2.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.TreeNode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.example.springbootswagger2.model.Student;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

	//private static final Logger logdescription = LoggerFactory.getLogger(WordnetLibrary.class.getName());
	private String propertiesFile = "C:\\Users\\67382523\\workspace_tfm\\ProyectoLibreria\\config\\file_properties.xml";
	private Dictionary dic;
		
	
	/* WordnetLibrary(): Constructor de la libreria*/
	public WordnetLibrary(){
		  try{
	          JWNL.initialize(new FileInputStream(propertiesFile));
	          dic = Dictionary.getInstance();
	        }catch(Exception e){
	  //        logdescription.error(e.toString());
	        }
	}
	
	/*getSynset: Por offset y por palabra y sense*/
	public Synset getSynset(long offSet) throws JWNLException {
		return dic.getSynsetAt(POS.NOUN, offSet);
	}

	public Synset getSynset(String word, POS pos, int sense) {
		Synset synset = null;
		try {
			IndexWord index = dic.lookupIndexWord(pos, word);
			synset =index.getSense(sense);
		} catch (JWNLException e) {
			//logdescription.error("Error al crear el Synset",e);
		}
		return synset;
	}
	
	public Synset getSynset(Object s) {
		//Pattern phone = Pattern.compile("Offset:");
		//Matcher m = phone.matcher(s);
	//	m.find();
		//System.out.print("CADENA"+s+m.groupCount());
		return null;
	}
	public String  getSense(Synset synset){
		return synset.getGloss();
	}
	
	public String  getSense(long offset) throws JWNLException{
		Synset s = dic.getSynsetAt(POS.NOUN, offset);
		return s.getGloss();
	}
	
	
	 /* getSenses: Descripciones de un palabra(word) */
	 public String getSense(String word, POS pos, int sense){
		String description = null;
		try {
			//Obtenemos el índice de la palabra
			IndexWord index = dic.lookupIndexWord(pos, word);
			if(index == null) System.out.println("No synsets found for '" + word + "/" + pos.getKey() + "'");
			else{
				//Obtenemos todos los resultados de una palabra
				Synset[] senses = index.getSenses();
				Synset synset = senses[sense];
				description = synset.getGloss();
				
			}
		} catch (JWNLException e) {
		//	logdescription.error(e.toString());
		}
		return description;
	}


	
	/* getNumberSenses: Obtienes el número de significados de cada palabra*/
	public int getNumberSenses(String word){
		IndexWord i = null;
		try {
		 i = dic.getIndexWord(POS.NOUN, word);
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i.getSenseCount();
		
	}
	
	/*getLemma: Las distintas formas de obtener una forma de una palabra*/
	/*public String getLemma(String word){
		if (dic == null) return null;
		
		IndexWord i = null;
		try {
			i = dic.lookupIndexWord(POS.NOUN, word);
		} catch (JWNLException e) {}
		if (i == null) return null;
		
		String lemma = i.getLemma();
		//lemma = lemma.replace("_", " ");
		
		return lemma;
	}*/
	
	public Word[] getLemma(Synset s){
		return s.getWords();
	}
	
	public Word[] getLemma(long offset){
		Synset s = null;
		try {
			s = getSynset(offset);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s.getWords();
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
		}
		
		return new ArrayList<String>(lemmaSet);
	}
	
	
	/*getSynset: Obtiene el synset de la palabra*/
	
	
	/*Relaciones entre Synsets:
	 * 1. Sinonimos
	 * 2. Antonimos
	 * 3. Hiponimos/hiperonimos Ex.perro/animal
	 * 4. Holonimo/Meronimo
	 * 5. Cohiponimia*/
	
	public ArrayList<Synset> getSynonyms(Synset synset){
		ArrayList<Synset> synonyms = null;
		Synset [] synsets = null;
		try {
			IndexWord i=dic.lookupIndexWord(synset.getPOS(), synset.getWord(0).getLemma());
			synsets = i.getSenses();
			synonyms = new ArrayList<Synset>(Arrays.asList(synsets));
			synonyms.remove(synset);
	
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return synonyms;
		//return getSynonyms(synset.getWord(0).getLemma(), synset.getPOS(), 0);
	}
	
	public ArrayList<Synset> getSynonyms(long offset) throws JWNLException{
		
		Synset synset = getSynset(offset);
		return getSynonyms(synset);
	}
	
	public ArrayList<Synset> getSynonyms(String word, POS pos, int sense){
		ArrayList<Synset> synonyms = null;
		Synset [] synsets = null;
		try {
			IndexWord i=dic.lookupIndexWord(pos, word);
			synsets = i.getSenses();
			synonyms = new ArrayList<Synset>(Arrays.asList(synsets));
			synonyms.remove(sense);
			
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return synonyms;
		
	}
	
	public ArrayList<String> getSynonymsWords(String word, POS pos, int sense){
		ArrayList<Synset> synonyms = getSynonyms(word, pos,sense);
		return getLemmas(synonyms);
	}
	
	public ArrayList<String> getSynonymsWords(Synset synset){
		return getLemmas(getSynonyms(synset));
	}
	
	public ArrayList<String> getSynonymsWords(long offset) throws JWNLException{
		return getSynonymsWords(getSynset(offset));
	}
	
	public ArrayList<Synset> getHypernyms(Synset synset) throws JWNLException{
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHypernyms(synset);

		for(int it = 0; it<listhyponyms.size();it++){
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
			hyp.add(current);
				
		}
		return hyp;
	}
	
	public ArrayList<Synset> getHypernyms(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		return getHypernyms(synset);
	}
	
	public ArrayList<Synset> getHypernyms(String word, POS pos, int sense) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return getHypernyms(synset);
	}
	
	public ArrayList<String> getHypernymsWords(Synset synset) throws JWNLException{
		return getLemmas(getHypernyms(synset));
	}
	
	public ArrayList<String> getHypernymsWords(long offset) throws JWNLException{
		return getLemmas(getHypernyms(offset));
	}

	public ArrayList<String> getHypernymsWords(String word, int sense, POS pos) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return getLemmas(getHypernyms(synset));
	}

	public ArrayList<Synset> getHyponyms(Synset synset) throws JWNLException{
		ArrayList<Synset> hyp =new ArrayList<Synset>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHyponyms(synset);

		for(int it = 0; it<listhyponyms.size();it++){
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
			hyp.add(current);
				
		}
		return hyp;
	}
	
	public ArrayList<Synset> getHyponyms(long offset) throws JWNLException{
		Synset synset =getSynset(offset);
		return getHyponyms(synset);
	}
	
	public ArrayList<Synset> getHyponyms(String word, int sense, POS pos) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return getHyponyms(synset);
	}
	
	public ArrayList<String> getHyponymsWords(Synset synset) throws JWNLException{
		return getLemmas(getHyponyms(synset));
	}
	
	public ArrayList<String> getHyponymsWords(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		return getLemmas(getHyponyms(synset));
		
	}
	
	public ArrayList<String> getHyponymsWords(String word, int sense, POS pos) throws JWNLException{
		return getLemmas(getHyponyms(word, sense, pos));
	}
		

	public ArrayList<Synset> getListofSynsets(PointerTargetNodeList list, ArrayList<Synset> result){	
	
		Iterator it = list.iterator();
		while(it.hasNext()){
			PointerTargetNode listaux =(PointerTargetNode)it.next();
			if(!result.contains(listaux.getSynset())){
				//System.out.print("SYNSET"+listaux.getSynset()+"\n");
				result.add(listaux.getSynset());
			}
		}
		return result;
	}

	public ArrayList<String> getListofWords(PointerTargetNodeList list, ArrayList<String> result){	
		
		Iterator it = list.iterator();
		while(it.hasNext()){
			PointerTargetNode listaux =(PointerTargetNode)it.next();
			//System.out.print("\nLISTA:\n"+listaux.getSynset());
			Word[] wArr = listaux.getSynset().getWords();
			for(Word w:wArr){
				// System.out.print("\t" + w.getLemma().replace("_", " "));
				if(!result.contains(w.getLemma())){
					result.add(w.getLemma());
				}
			}
		}
		return result;
	}
	
	public ArrayList<Synset> getHolonyms(Synset synset) throws JWNLException{
		
		ArrayList<Synset> holonyms = new ArrayList<Synset>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartHolonyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberHolonyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartHolonyms(synset);
	
		if(!holonyms.containsAll(getListofSynsets(listpartof, holonyms)))
			holonyms.addAll(getListofSynsets(listpartof, holonyms));
		if(!holonyms.containsAll(getListofSynsets(listmemberof, holonyms)))
			holonyms.addAll(getListofSynsets(listmemberof, holonyms));
		if(!holonyms.containsAll(getListofSynsets(listsubtanceof, holonyms)))
			holonyms.addAll(getListofSynsets(listsubtanceof, holonyms));
		return holonyms;
	}
	
	public ArrayList<Synset> getHolonyms(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		return  getHolonyms(synset); 
	}
	
	public ArrayList<Synset> getHolonyms(String word, int sense, POS pos) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return  getHolonyms(synset);
	}

	public ArrayList<String> getHolonymsWords(Synset synset) throws JWNLException{
		
		ArrayList<String> holonyms = new ArrayList<String>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartHolonyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberHolonyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartHolonyms(synset);
	
		if(!holonyms.containsAll(getListofWords(listpartof, holonyms)))
			holonyms.addAll(getListofWords(listpartof, holonyms));
		if(!holonyms.containsAll(getListofWords(listmemberof, holonyms)))
			holonyms.addAll(getListofWords(listmemberof, holonyms));
		if(!holonyms.containsAll(getListofWords(listsubtanceof, holonyms)))
			holonyms.addAll(getListofWords(listsubtanceof, holonyms));
		return holonyms;
	}
	
	
	public ArrayList<String> getHolonymsWords(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		return getHolonymsWords(synset);
		
	}
	
	public ArrayList<String> getHolonymsWords(String word, int sense, POS pos) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return getHolonymsWords(synset);
	}

	public ArrayList<Synset> getMeronyms(Synset synset) throws JWNLException{
		ArrayList<Synset> meronyms = new ArrayList<Synset>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartMeronyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberMeronyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartMeronyms(synset);
	
		if(!meronyms.containsAll(getListofSynsets(listpartof, meronyms)))
			meronyms.addAll(getListofSynsets(listpartof, meronyms));
		if(!meronyms.containsAll(getListofSynsets(listmemberof, meronyms)))
			meronyms.addAll(getListofSynsets(listmemberof, meronyms));
		if(!meronyms.containsAll(getListofSynsets(listsubtanceof, meronyms)))
			meronyms.addAll(getListofSynsets(listsubtanceof, meronyms));
		return meronyms;
	}
	
	public ArrayList<Synset> getMeronyms(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		return  getMeronyms(synset);
	}
	
	public ArrayList<Synset> getMeronyms(String word, int sense, POS pos) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return  getMeronyms(synset);
	}

	
	public ArrayList<String> getMeronymsWords(Synset synset) throws JWNLException{
		ArrayList<String> meronyms = new ArrayList<String>();
		PointerTargetNodeList listpartof = PointerUtils.getInstance().getPartMeronyms(synset);
		PointerTargetNodeList listmemberof = PointerUtils.getInstance().getMemberMeronyms(synset);
		PointerTargetNodeList listsubtanceof = PointerUtils.getInstance().getPartMeronyms(synset);
	
		if(!meronyms.containsAll(getListofWords(listpartof, meronyms)))
			meronyms.addAll(getListofWords(listpartof, meronyms));
		if(!meronyms.containsAll(getListofWords(listmemberof, meronyms)))
			meronyms.addAll(getListofWords(listmemberof, meronyms));
		if(!meronyms.containsAll(getListofWords(listsubtanceof, meronyms)))
			meronyms.addAll(getListofWords(listsubtanceof, meronyms));
		return meronyms;
	}
	
	public ArrayList<String> getMeronymsWords(long offset) throws JWNLException{
		Synset synset = getSynset(offset);
		return  getMeronymsWords(synset);
	}
	
	public ArrayList<String> getMeronymsWords(String word, int sense, POS pos) throws JWNLException{
		Synset synset = getSynset(word, pos, sense);
		return  getMeronymsWords(synset);
	}
	
	public boolean isEntityNode(Synset s){
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
				return pathsynsets;
			}
			if(isEntityNode(synset1)){
				pathsynsets = getPathToEntity(synset2);
				return pathsynsets;
			}
			if(isEntityNode(synset2)){
				pathsynsets = getPathToEntity(synset1);
				return pathsynsets;
			}
			/*Calculamos la relacion*/
	
			RelationshipList list = RelationshipFinder.getInstance().findRelationships(synset1, synset2, PointerType.HYPERNYM);
			PointerTargetNodeList l = ((Relationship) list.get(0)).getNodeList();
			
			for(int i= 0; i<l.size(); i++){
				PointerTargetNode p = (PointerTargetNode)l.get(i);
				//System.out.print("\nCURRENT:"+ p.getSynset()+"\n");
				pathsynsets.add(p.getSynset());
			//	System.out.print("\nNODE"+i+":"+p.getSynset().getWord(0).getLemma());
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
	
		return getPathToEntity(a).size()-1;
	}
	
	public int depthOfSynset(Synset a, LinkedHashMap<Synset, ArrayList<Synset>> tree) throws JWNLException{
	
	    int depth = 0;
		Map.Entry<Synset, ArrayList<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, ArrayList<Synset>>>(tree.entrySet())).get(0);
		if(!tree.containsKey(a))
			return -1;
		if(rootnode.getKey().equals(a))
			return 0;
		else
		return depthnodes(a, rootnode.getValue(), 1,tree);

	}
	
	
	

	public int depthnodes(Synset a, ArrayList<Synset> nodeslevel, int depth, LinkedHashMap<Synset, ArrayList<Synset>> tree) {
		
	//	System.out.print("\nENTRA NODOS HIJOS->"+ nodeslevel.size()+" depth"+depth+" synset a "+ a.getWord(0).getLemma()+ a.getOffset()+"\n");
		ArrayList<Synset> newlevelnodes = new ArrayList<Synset>();
		if(nodeslevel.size()>0){
			for(Synset node: nodeslevel){
				ArrayList<Synset> hijos =tree.get(node);
				if(hijos !=null)
					newlevelnodes.addAll(hijos);
			}
			if(!nodeslevel.contains(a)){
				//   System.out.print("Not Contains"+depth);
				return depthnodes(a, newlevelnodes, depth+1,tree);
			}
	   }
	   //System.out.print("DEpTHNODOS->"+depth);
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
			//System.out.print("\nSYNSET:"+node.getWord(0).getLemma()+"----->"+depthOfSynset(node)+"\n");
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
	/*public HashMap<Long, Synset> getHypernymTree(Synset synset){
		
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
	}*/


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
				//	 System.out.print("S->"+s+"\n");
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
			
			
			if( altura>=0){
				//System.out.print("\nNODO->"+hijos.get(pos).getWord(0).getLemma()+ hijos.get(0).getOffset()+ "  HIJOS"+hijos.size()+" ALTURA->"+altura);
					//treeResult.put(hijos.get(pos), new ArrayList<Synset>());
			
					ArrayList<Synset> hyponyms = getHyponyms(hijos.get(pos));
					if(altura == 0) {
						//System.out.print(" 0 HIJOS");
						treeResult.put(hijos.get(pos), new ArrayList<Synset>());
					}
					else
					{
					//	System.out.print("NODO->"+hijos.get(pos).getWord(0).getLemma()+ hijos.get(0).getOffset()+"\n");
						treeResult.put(hijos.get(pos), hyponyms);
						getSubarbolWordnet(hijos.get(pos),altura, treeResult);
						//System.out.print("\n"+treeResult+"\n");
					}
				
					RecorrerNodos(pos+1,hijos,altura,treeResult);
					//System.out.print("*****ALTURA" +altura+hijos.get(pos));
					
				
				
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
			hijos=RecorrerNodos(0, hijos, altura, treeResult);
		}
		return treeResult;
		
	}
	
	public void printTree(Map<Synset, ArrayList<Synset>> treeResult){
		 treeResult.forEach((key, value) -> System.out.println(key.getWord(0).getLemma() + ":" + value));
	}
	
	 public static void main(String[] arg) throws JWNLException{
	        WordnetLibrary w = new WordnetLibrary();
	       // logdescription.info("HOLA CREADO WORDNET");
	        
	        final String word = "canis familiaris";
            final POS pos = POS.NOUN;
            
            long offset = 2961779;
           
            String synsetstr = "[Synset: [Offset: 7462241] [POS: noun] Words: party -- (an occasion on which people can assemble for social interaction and entertainment; \"he planned a party to celebrate Bastille Day\")]";
            
            w.getSynset(synsetstr);
            /*Synset s1 = getSynset(offset);
            System.out.print("SENSES SYNSET\n");
            getSense(s1);
            System.out.print("\n\nSENSES OFFSET\n");
            getSense(offset);
            System.out.print("\n\n\nObtener el synset del offset:"+s1 );*/
           /* System.out.print("TEST"+w.getHypernymsWords("party", 1, POS.NOUN));
            long s0 =2125600, s1=2124272, s3=6422547;
            Synset kitten=  w.getSynset(s0);
            Synset cat = w.getSynset(s1); 
            Synset book =w.getSynset(s3);
           *//* 
            Synset lcs = w.getLeastCommonSubsumer(kitten,cat);
            System.out.print("LCS->"+lcs);
            ArrayList<Synset> nodoskitten = w.getPathBetweenSynsets(kitten, lcs);
            ArrayList<Synset> nodoscat = w.getPathBetweenSynsets(cat, lcs);
            
            int numlinks_kitten= w.getNumLinksBetweenSynsets(kitten, lcs);
            int numlinks_cat = w.getNumLinksBetweenSynsets(cat, lcs);*/
          //  System.out.print("NUMERO DE ENLACES a KITTEN->"+numlinks_kitten+"\n"+"NUMERO DE ENLACES a KITTEN->"+numlinks_cat);
      //      LinkedHashMap<Synset, ArrayList<Synset>> tree = new LinkedHashMap<Synset, ArrayList<Synset>>();
        //    tree = w.getSubarbolWordnet(cat,4, tree); //Testado con cat
         //   w.printTree(tree);
           //System.out.print("\nSUBARBOL:"+ getSubarbolWordnet(cat,2));
            /*LinkedHashMap<Synset,ArrayList<Synset>> treeResult= new LinkedHashMap<Synset,ArrayList<Synset>>();
            treeResult =  w.getSubarbolWordnet(cat,3, treeResult);
            System.out.print("\n\nPINTAR ARBOL RESULTADO\n\n");
            treeResult.forEach((key, value) -> System.out.println(key.getWord(0).getLemma() + ":" + value));
		*/
	 }
	



}
