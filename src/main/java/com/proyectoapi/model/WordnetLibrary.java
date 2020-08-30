package com.proyectoapi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
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

/**
 * Class WordnetLibrary: Esta clase gestiona la interacción con la base de datos de Wordnet.
 * @author Lucía Batista Flores
 * @version 1.0
 */

public class WordnetLibrary {

	static Logger logger = Logger.getLogger( WordnetLibrary.class.getName());
	private Dictionary dic; /*Variable que se utiliza para gestionar la conexión a Wordnet*/ 

	/**
	 * Constructor de la clase que interacciona con Wordnet
	 */
	public WordnetLibrary() {
		try {
		
			Resource resource = new ClassPathResource("file_properties.xml");

			JWNL.initialize(resource.getInputStream());
			dic = Dictionary.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este método permite obtener un Synset de la ontologia de Wordnet
	 * @param offSet - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve un Synset de la ontología
	 * @throws JWNLException
	 */
	public Synset getSynset(long offSet) throws JWNLException {
		return dic.getSynsetAt(POS.NOUN, offSet);
	}

	/**
	 * Este método permite obtener un Synset de la ontología de Wordnet a través de la palabra y su significado
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado del synset
	 * @return Devuelve un Synset de la ontologia
	 */
	public Synset getSynset(String word, POS pos, int sense) {
		Synset synset = null;
		try {
			IndexWord index = dic.lookupIndexWord(pos, word);
			synset = index.getSense(sense);
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return synset;
	}

	/**
	 * Este método permite obtener la descripción asociada al Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve un tipo String con la descripción
	 */
	public String getSense(Synset synset) {
		return synset.getGloss();
	}

	/**
	 * Este método permite obtener la descripción asociada al offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve un tipo String con la descripción
	 * @throws JWNLException
	 */
	public String getSense(long offset) throws JWNLException {
		Synset s = dic.getSynsetAt(POS.NOUN, offset);
		return s.getGloss();
	}

	/**
	 * Este método permite obtener la descripción asociada a la palabra
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return Devuelve un tipo String con la descripción
	 */
	public String getSense(String word, POS pos, int sense) {
		String description = null;
		String message = "No synsets found for '" + word + "/" + pos.getKey() + "'";
		try {
			// Obtenemos el índice de la palabra
			IndexWord index = dic.lookupIndexWord(pos, word);
			
			if (index == null)
				logger.log(Level.INFO, message);
			else {
				// Obtenemos todos los resultados de una palabra
				Synset[] senses = index.getSenses();
				Synset synset = senses[sense];
				description = synset.getGloss();

			}
		} catch (JWNLException e) {
			
			logger.log(Level.INFO, message);
			return message;
		}
		return description;
	}

	/**
	 * Este método permite obtener el número de significados de cada palabra
	 * @param word - Palabra que identifica al synset
	 * @return Devuelve el entero que identifica al significado
	 */
	public int getNumberSenses(String word) {
		IndexWord i = null;
		int num = 0;
		try {
			i = dic.getIndexWord(POS.NOUN, word);
			num =i.getSenseCount();
		} catch (JWNLException e) {

			e.printStackTrace();
		}
		return num;

	}
	
	/**
	 * Este método permite obtener el listado de objectos Words de un Synset
	 * @param s - Objeto Synset de la ontología
	 * @return Devuelve un vector de objectos Words
	 */
	public Word[] getLemma(Synset s) {
		return s.getWords();
	}

	/**
	 * Este método permite obtener el listado de objectos Words de un Synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve un vector de objectos Words
	 */
	public Word[] getLemma(long offset) {
		Synset s = null;
		Word[] w = null ;
			try {
				s = getSynset(offset);
				s.getWords();
			} catch (JWNLException e) {
				e.printStackTrace();
			}
			return w;
	
	}

	/**
	 * Este método permite transformar el listado de Synsets en un vector de Cadenas
	 * @param synonyms - Listados de Synsets
	 * @return Devuelve el listado de cadenas que se corresponden con el listado de lemmas
	 */
	/* getLemma: Las distintas formas de obtener una forma de una palabra */
	public List<String> getLemmas(List<Synset> synonyms) {
		HashSet<String> lemmaSet = new HashSet<>();
		ArrayList<String> lemmasaux = new ArrayList<>();

		for (Synset synset : synonyms) {
			Word[] words = synset.getWords();
			for (int i = 0; i < words.length; i++) {
				lemmasaux.add(words[i].getLemma());
			}
			for (String lemma : lemmasaux)
				lemmaSet.add(lemma);
		}

		return new ArrayList<>(lemmaSet);
	}


	/**
	 * Este método obtiene el listado de sinónimos de un Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el listado de objetos Synsets
	 */
	public List<Synset> getSynonyms(Synset synset) {
		ArrayList<Synset> synonyms = null;
		Synset[] synsets = null;
		try {
			IndexWord i = dic.lookupIndexWord(synset.getPOS(), synset.getWord(0).getLemma());
			synsets = i.getSenses();
			synonyms = new ArrayList<>(Arrays.asList(synsets));
			synonyms.remove(synset);

		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return synonyms;
		 
	}
	
	/**
	 * Este método obtiene el listado de sinónimos de un Synset dado su offset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getSynonyms(long offset) throws JWNLException {

		Synset synset = getSynset(offset);
		return getSynonyms(synset);
	}

	/**
	 * Este método obtiene el listado de sinónimos de un Synset dado su offset
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return Devuelve el listado de objetos Synsets
	 */
	public List<Synset> getSynonyms(String word, POS pos, int sense) {
		ArrayList<Synset> synonyms = null;
		Synset[] synsets = null;
		try {
			IndexWord i = dic.lookupIndexWord(pos, word);
			synsets = i.getSenses();
			synonyms = new ArrayList<>(Arrays.asList(synsets));
			synonyms.remove(sense);

		} catch (JWNLException e) {

			e.printStackTrace();
		}
		return synonyms;

	}

	/**
	 * Este método devuelve el listado de palabras sinónimas del Synset
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return Devuelve un listado de String con las palabras sinónimas
	 */
	public List<String> getSynonymsWords(String word, POS pos, int sense) {
		List<Synset> synonyms = getSynonyms(word, pos, sense);
		return getLemmas(synonyms);
	}

	/**
	 * Este método devuelve el listado de palabras sinónimas del Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve un listado de String con las palabras sinónimas
	 */
	public List<String> getSynonymsWords(Synset synset) {
		return getLemmas(getSynonyms(synset));
	}

	/**
	 * Este método devuelve el listado de palabras sinónimas del Synset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve un listado de String con las palabras sinónimas
	 * @throws JWNLException
	 */
	public List<String> getSynonymsWords(long offset) throws JWNLException {
		return getSynonymsWords(getSynset(offset));
	}

	/**
	 * Este método devuelve el listado de hiperónimos del Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getHypernyms(Synset synset) throws JWNLException {
		ArrayList<Synset> hyp = new ArrayList<>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHypernyms(synset);

		for (int it = 0; it < listhyponyms.size(); it++) {
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
			hyp.add(current);
		}
		return hyp;
	}

	/**
	 * Este método devuelve el listado de hiperónimos del Synset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getHypernyms(long offset) throws JWNLException {
		Synset synset = getSynset(offset);
		return getHypernyms(synset);
	}

	/**
	 * Este método devuelve el listado de hiperónimos del Synset
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return  Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getHypernyms(String word, POS pos, int sense) throws JWNLException {
		Synset synset = getSynset(word, pos, sense);
		return getHypernyms(synset);
	}

	/**
	 * Este método devuelve el listado de palabras hiperónimas del Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el listado de palabras hipéronimas
	 * @throws JWNLException
	 */
	public List<String> getHypernymsWords(Synset synset) throws JWNLException {
		return getLemmas(getHypernyms(synset));
	}

	/**
	 * Este método devuelve el listado de palabras hiperónimas del Synset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return  Devuelve el listado de palabras hipéronimas
	 * @throws JWNLException
	 */
	public List<String> getHypernymsWords(long offset) throws JWNLException {
		return getLemmas(getHypernyms(offset));
	}

	/**
	 * Este método devuelve el listado de palabras hiperónimas del Synset
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return Devuelve el listado de palabras hipéronimas
	 * @throws JWNLException
	 */
	public List<String> getHypernymsWords(String word, int sense, POS pos) throws JWNLException {
		Synset synset = getSynset(word, pos, sense);
		return getLemmas(getHypernyms(synset));
	}

	/**
	 * Este método devuelve el listado de hipónimos del Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getHyponyms(Synset synset) throws JWNLException {
		ArrayList<Synset> hyp = new ArrayList<>();
		PointerTargetNodeList listhyponyms = PointerUtils.getInstance().getDirectHyponyms(synset);

		for (int it = 0; it < listhyponyms.size(); it++) {
			PointerTargetNode node = (PointerTargetNode) listhyponyms.get(it);
			Synset current = node.getSynset();
			hyp.add(current);
		}
		return hyp;
	}

	/**
	 * Este método devuelve el listado de hipónimos del Synset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return  Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getHyponyms(long offset) throws JWNLException {
		Synset synset = getSynset(offset);
		return getHyponyms(synset);
	}

	/**
	 * Este método devuelve el listado de hipónimos del Synset
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return Devuelve el listado de objetos Synsets
	 * @throws JWNLException
	 */
	public List<Synset> getHyponyms(String word, int sense, POS pos) throws JWNLException {
		Synset synset = getSynset(word, pos, sense);
		return getHyponyms(synset);
	}

	/**
	 * Este método devuelve el listado de palabras hipónimas del Synset
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el listado de palabras hipónimas
	 * @throws JWNLException
	 */
	public List<String> getHyponymsWords(Synset synset) throws JWNLException {
		return getLemmas(getHyponyms(synset));
	}

	/**
	 * Este método devuelve el listado de palabras hipónimas del Synset
	 * @param offset - Número de tipo long que identifica de manera única a un Synset
	 * @return Devuelve el listado de palabras hipónimas
	 * @throws JWNLException
	 */
	public List<String> getHyponymsWords(long offset) throws JWNLException {
		Synset synset = getSynset(offset);
		return getLemmas(getHyponyms(synset));

	}

	/**
	 * Este método devuelve el listado de palabras hipónimas del Synset
	 * @param word - Palabra que identifica al synset
	 * @param pos - Tipo de Palabra (Nombre)
	 * @param sense - Número del significado
	 * @return Devuelve el listado de palabras hipónimas
	 * @throws JWNLException
	 */
	public List<String> getHyponymsWords(String word, int sense, POS pos) throws JWNLException {
		return getLemmas(getHyponyms(word, sense, pos));
	}

	
	public List<Synset> getCohyponyms(long offset){
		List<Synset> fatherList; 	
		List<Synset> childList = new ArrayList<>();	
		
		try {
			fatherList = getHypernyms(offset);
			for (Synset father: fatherList){
				childList.addAll(getHyponyms(father));
			}		
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return childList;
	}
	
	/**
	 * Este método obtiene el listado de Synsets del tipo PointerTargetNodeList
	 * @param list - Tipo que contiene el listado de Nodos de la ontología
	 * @param result 
	 * @return Devuelve el listado de Synsets
	 */
	public List<Synset> getListofSynsets(PointerTargetNodeList list, List<Synset> result) {

		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			PointerTargetNode listaux = (PointerTargetNode) it.next();
			if (!result.contains(listaux.getSynset())) {
				result.add(listaux.getSynset());
			}
		}
		return result;
	}

	/**
	 * Este método obtiene el de palabras dado un listado de Synsets
	 * @param result - Listado de synsets
	 * @return Devuelve el listado de palabras
	 */
	public List<String> getListofSynsetsToString(List<Synset> result) {
		ArrayList<String> listsynsets = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			listsynsets.add(result.get(i).toString());
		}
		return listsynsets;
	}

	/**
	 * Este método obtiene el listado de palabras del tipo PointerTargetNodeList
	 * @param list - Tipo que contiene el listado de Nodos de la ontología
	 * @param result -Listado de synsets
	 * @return Devuelve el listado de palabras
	 */
	public List<String> getListofWords(PointerTargetNodeList list, List<String> result) {
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			PointerTargetNode listaux = (PointerTargetNode) it.next();
			Word[] wArr = listaux.getSynset().getWords();
			for (Word w : wArr) {
				if (!result.contains(w.getLemma())) {
					result.add(w.getLemma());
				}
			}
		}
		return result;
	}

	/**
	 * Este método comprueba si un Synset es el nodo raíz de la ontología
	 * @param s - Objeto Synset de la ontología
	 * @return Devuelve true si es el nodo raíz y false si no lo es.
	 */
	public boolean isEntityNode(Synset s) {
		return ((s.getWord(0).getLemma().equals("entity")) ? true : false);
	}

	/**
	 * Este método obtiene el listado de Synsets desde un determinado nodo a la raíz
	 * @param s - Objeto Synset de la ontología
	 * @return Devuelve el listado de nodos hasta la raíz
	 */
	public List<Synset> getPathToEntity(Synset s) {
		ArrayList<Synset> nodepath = new ArrayList<>();
		try {
			if (isEntityNode(s)) {
				nodepath.add(s);
				return nodepath;
			}
			PointerTargetTree tree = PointerUtils.getInstance().getHypernymTree(s);
			PointerTargetNodeList l = (PointerTargetNodeList) tree.toList().get(0);
			for (int i = 0; i < l.size(); i++) {
				PointerTargetNode p = (PointerTargetNode) l.get(i);
				nodepath.add(p.getSynset());

			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return nodepath;
	}

	/**
	 * Este método obtiene el listado de synsets entre dos synsets de las ontologías
	 * @param synset1 - Objeto Synset de la ontología
	 * @param synset2 - Objeto Synset de la ontología
	 * @return Devuelve el listado de synsets
	 */
	public List<Synset> getPathBetweenSynsets(Synset synset1, Synset synset2) {
		List<Synset> pathsynsets = new ArrayList<>();
		try {
			if (isEntityNode(synset1) && isEntityNode(synset2)) {
				pathsynsets.add(synset1);
				return pathsynsets;
			}
			if (isEntityNode(synset1)) {
				pathsynsets = getPathToEntity(synset2);
				return pathsynsets;
			}
			if (isEntityNode(synset2)) {
				pathsynsets = getPathToEntity(synset1);
				return pathsynsets;
			}
			/* Calculamos la relacion */

			RelationshipList list = RelationshipFinder.getInstance().findRelationships(synset1, synset2,
					PointerType.HYPERNYM);
			PointerTargetNodeList l = ((Relationship) list.get(0)).getNodeList();

			for (int i = 0; i < l.size(); i++) {
				PointerTargetNode p = (PointerTargetNode) l.get(i);
				pathsynsets.add(p.getSynset());
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return pathsynsets;
	}

	/**
	 * Este método devuelve el número de enlaces entre dos synsets
	 * @param s1 - Objeto Synset de la ontología
	 * @param s2 - Objeto Synset de la ontología
	 * @return Devuelve el número de enlaces entre los synsets
	 */
	public int getNumLinksBetweenSynsets(Synset s1, Synset s2) {
		return getPathBetweenSynsets(s1, s2).size() - 1;
	}

	/**
	 * Este método devuelve la profundidad del Synset
	 * @param a - Objeto Synset de la ontología
	 * @return Devuelve el número de profundidad que tiene el synset
	 */
	public int depthOfSynset(Synset a) {

		return getPathToEntity(a).size() - 1;
	}

	/**
	 * Este método calcula la profundidad de un Synset dentro de un subárbol
	 * @param a  - Objeto Synset de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve el número de profundidad que tiene el synset dentro del subárbol
	 */
	public int depthOfSynset(Synset a, LinkedHashMap<Synset, List<Synset>> tree) {

		Map.Entry<Synset, List<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, List<Synset>>>(tree.entrySet())).get(0);
		if (!tree.containsKey(a))
			return -1;
		if (rootnode.getKey().equals(a))
			return 0;
		else
			return depthnodes(a, rootnode.getValue(), 1, tree);

	}

	/**
	 * Este método calcula la profundidad para los nodos recibidos como parámetros dentro del subárbol
	 * @param a - Objeto Synset de la ontología
	 * @param list - Vector de nodos de la ontología del nivel a examinar
	 * @param depth - Número que representa la profundidad del subárbol
	 * @param tree - Subárbol de wordndet  
	 * @return Devuelve el número que representa la profundidad dentro del subárbol
	 */
	public int depthnodes(Synset a, List<Synset> list, int depth,
			LinkedHashMap<Synset, List<Synset>> tree) {

		ArrayList<Synset> newlevelnodes = new ArrayList<>();
		if (list.isEmpty()) {
			for (Synset node : list) {
				List<Synset> hijos = tree.get(node);
				if (hijos != null)
					newlevelnodes.addAll(hijos);
			}
			if (!list.contains(a)) {
				return depthnodes(a, newlevelnodes, depth + 1, tree);
			}
		}
		return depth;
	}

	/**
	 * Este método calcula el número de nodos hasta el synset raíz de la ontología
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el número de nodos existentetes desde el nodo recibido como parámetro hasta la raíz de la ontología
	 */
	public int getNodesToEntity(Synset synset) {
		if (synset.getWord(0).toString().equalsIgnoreCase("entity"))
			return 1;
		try {
			PointerTargetTree pt = PointerUtils.getInstance().getHypernymTree(synset);
			PointerTargetNodeList l = (PointerTargetNodeList) pt.toList().get(0);
			return l.size();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Este método obtiene el synset que representa el LCS entre dos synsets
	 * @param a - Objeto Synset1 de la ontología
	 * @param b - Objeto Synset2 de la ontología
	 * @return Devuelve un objeto del tipo Synset que representa al LCS de ambos synsets
	 * @throws JWNLException
	 */
	public Synset getLeastCommonSubsumer(Synset a, Synset b){

		int depth;
		int depthMin;
		Synset lcssynset = null;
		List<Synset> nodepath = getPathBetweenSynsets(a, b);
		depthMin = 10000;

		for (Synset node : nodepath) {

			depth = getNodesToEntity(node);
			if (depth <= depthMin) {
				depthMin = depth;
				lcssynset = node;
			}
		}
		return lcssynset;
	}
	
	/**
	 * Este método devuelve el número de elementos del primer conjunto que no se encuentran en el segundo conjunto
	 * @param a - Conjunto de Synsets
	 * @param b - Conjunto de Synsets
	 * @return Devuelve el número de elementos del primer conjunto que no se encuentran en el segundo conjunto
	 */
	public int NotContainsFirstInSecond(HashSet<Synset> a, HashSet<Synset> b) {
		int num = 0;
		for (Synset syn : a) {

			if (!b.contains(syn)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Este método permite obtener el número de elementos en la intersección entre los dos conjuntos
	 * @param a - Conjunto de Synsets
	 * @param b - Conjunto de Synsets
	 * @return Devuelve el número de elementos en la intersección entre ambos conjuntos
	 */
	public int Intersection(HashSet<Synset> a, HashSet<Synset> b) {
		int num = 0;
		for (Synset syn : a) {

			if (b.contains(syn)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Este método permite obtener el subárbol de hiperónimos desde el Synset raíz hasta el Synset que se recibe como parámetros
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el subárbol de hiperónimos
	 */
	public HashSet<Synset> getHypernymTreeList(Synset synset) {

		HashSet<Synset> list = new HashSet<>();
		try {
			PointerTargetTree tree = PointerUtils.getInstance().getHypernymTree(synset);
			@SuppressWarnings("unchecked")
			List<PointerTargetNodeList> branchList = tree.toList();
			for (PointerTargetNodeList nodeList : branchList) {
				for (int i = 0; i < nodeList.size(); i++) {
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

	/**
	 * Este método permite obtener el subárbol de hipónimos desde el Synset raíz hasta el Synset que se recibe como parámetros
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el subárbol de hipónimos
	 */
	public HashSet<Synset> getHyponymsTreeList(Synset synset) {

		HashSet<Synset> list = new HashSet<>();
		try {
			PointerTargetTree tree = PointerUtils.getInstance().getHyponymTree(synset);
			@SuppressWarnings("unchecked")
			List<PointerTargetNodeList> branchList = tree.toList();
			for (PointerTargetNodeList nodeList : branchList) {
				for (int i = 0; i < nodeList.size(); i++) {
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

	/***
	 * Este método permite obtener el listado de Synsets de la siguiente altura
	 * @param pos - Nivel que se esta calculando 
	 * @param hijos - Listado de Synsets del nivel superior
	 * @param altura - Altura total que se desea calcular del subárbol
	 * @param treeResult - Map que representa al subárbol, donde cada elemento del map representa al Synset y a los hijos de dicho synset
	 * @return Devuelve el listado de Synsets del siguiente nivel
	 * @throws JWNLException
	 */
	private List<Synset> recorrerNodos(int pos, List<Synset> hijos, int altura,
			LinkedHashMap<Synset, List<Synset>> treeResult) throws JWNLException {

		if (pos < hijos.size() && altura >= 0) {

				List<Synset> hyponyms = getHyponyms(hijos.get(pos));
				if (altura == 0) {
					treeResult.put(hijos.get(pos), new ArrayList<>());
				} else {
					treeResult.put(hijos.get(pos), hyponyms);
					getSubarbolWordnet(hijos.get(pos), altura, treeResult);
				}

				recorrerNodos(pos + 1, hijos, altura, treeResult);
			

		}
		return hijos;
	}

	/**
	 * Este método permite obtener el subárbol para una determinada altura
	 * @param s - Objeto Synset de la ontología
	 * @param altura - Altura para la que se desea calcular el subárbol
	 * @param treeResult - Subárbol calculado dentro de Wordnet 
	 * @return Devuelve el Map que representa al subárbol dada dicha altura
	 * @throws JWNLException
	 */
	public LinkedHashMap<Synset, List<Synset>> getSubarbolWordnet(Synset s, int altura,
			LinkedHashMap<Synset, List<Synset>> treeResult) throws JWNLException {
		

		if (altura >= 0) {
			treeResult.put(s, getHyponyms(s));
			altura--;
			recorrerNodos(0, getHyponyms(s), altura, treeResult);
		}
		return treeResult;

	}
	
	/**
	 * Este método permite obtener todo el subárbol desde un Synset
	 * @param s - Objeto Synset de la ontología
	 * @return Devuelve el Map que representa al subárbol entero desde dicho Synset de la ontología
	 */
	public LinkedHashMap<Synset, List<Synset>> getSubarbolWordnet(Synset s){
		LinkedHashMap<Synset, List<Synset>> tree = new  LinkedHashMap<>();
		HashSet<Synset> synsets =getHyponymsTreeList(s);
		try {
		for(Synset s1:synsets) {
			
				tree.put(s1, getHyponyms(s1));
			
		}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return tree;
	}
	
	/**
	 * Este método nos permite pintar el subárbol de la ontología
	 * @param treeResult - Map que representa al subárbol
	 */
	public void printTree(Map<Synset, List<Synset>> treeResult) {
		treeResult.forEach((key, value) -> logger.info(key.getWord(0).getLemma() + ":" + value));
	}

	/**
	 * Este método permite hacer la conversión del objeto Tree al Map que representa al subárbol, este método se emplea para obtener el Subárbol que se recibe como entrada en las llamadas
	 * @param tree - Objeto de tipo Tree que permite obtener el subárbol desde la entrada
	 * @return Devuelve el Map que representa el subárbol
	 * @throws JWNLException
	 */
	public LinkedHashMap<Synset, List<Synset>> convertTreeToMap(TreeWordnet tree) throws JWNLException {
		LinkedHashMap<Synset, List<Synset>> treeResult = new LinkedHashMap<>();
		ArrayList<com.proyectoapi.model.NodeWordnet> nodes = tree.getNodes();
		for (int i = 0; i < nodes.size(); i++) {
	
			ArrayList<Long> hijos = nodes.get(i).getHijos();
			ArrayList<Synset> synsetSons = new ArrayList<>();
			for (int j = 0; j < hijos.size(); j++) {

				Synset s = getSynset(hijos.get(j).longValue());
				synsetSons.add(s);
			}
			treeResult.put(getSynset(nodes.get(i).getKeysynset()), synsetSons);

		}
		return treeResult;
	}

}
