package com.proyecto.libsemantica.ProyectoLibreria;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
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
	          logdescription.error(e.toString());
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
			System.out.print("Lemmaaux:"+synsets.size());
			
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
	
	
	private static String[] getLemma(Synset synset) {
		// TODO Auto-generated method stub
		return null;
	}

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
	/*private static void demonstrateTreeOperation(IndexWord word) throws JWNLException {
        // Get all the hyponyms (children) of the first sense of <var>word</var>
		Synset s = word.getSense(1);
        PointerTargetTree hyponyms = PointerUtils.getInstance().getHyponymTree(s);
        System.out.println("Hyponyms of \"" + word.getLemma() + "\":");
        for(Iterator it = hyponyms.toList().iterator();it.hasNext();){
        	if(it.hasNext()){
        		 for (Object o : ((PointerTargetNodeList) it.next())) {
                     Synset t = ((PointerTargetNode) o).getSynset();
                   
                     System.out.print(t.getWords().length);
                 }
        	}
        }
    }*/

	
	 public static void main(String[] arg) throws JWNLException{
	        WordnetLibrary w = new WordnetLibrary();
	        logdescription.debug("HOLA CREADO WORDNET");
	        
	        final String word = "capacity";
            final POS pos = POS.NOUN;
         /*   getSenses(pos, word);
           System.out.print("LEMMA"+getLemma(pos, word));
           IndexWord index = dic.lookupIndexWord(pos, word);
           System.out.print("index"+index);
           System.out.print("numero"+getNumberSenses(pos, word));
           Synset[] s = getSynonyms(pos, word);
           System.out.print("lista synsets synonyms---------"+s.length+"\n");
           for (final Synset synset : s) {
        	   Word[] words = synset.getWords();
        	   
        	   for (final Word w1 : words) {
        	   System.out.print(w1.getLemma()+"\n");
        	   }
           }*/
            
           /* SYNONIMOS 
           ArrayList<String> synonyms = getSynonyms(pos, word);
           System.out.print("lista synsets synonyms---------"+synonyms.size()+"\n");
           for (final String w1 : synonyms) {
        	   System.out.print(w1+"\n");
           }*/
            
            
           /* HIPERNYMS*/
           ArrayList<String> teshypernyms = getHyperyms(pos, word);
           System.out.print(teshypernyms);
          // demonstrateTreeOperation(test);

	 }
}
