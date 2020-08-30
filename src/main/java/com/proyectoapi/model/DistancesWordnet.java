package com.proyectoapi.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;

/**
 * Class DistancesWordnet: Esta clase calcula las distancias semánticas para la ontología de Wordnet.
 * @author Lucía Batista Flores
 * @version 1.0
 */

public class DistancesWordnet {

	WordnetLibrary dictionary = new WordnetLibrary();

	/**
	 * Este método permite calcular la similitud semántica de Wu and Palmer para dos synsets de Wordnet
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor de la similitud semántica de Wu and Palmer entre dos synsets
	 */
	public double getWPSimilarity(Synset s1, Synset s2) {
		double distance;

		Synset lcs;
		/* Calculamos la profundidad de ambos synsets */
		int totallinks;
		int depthlcs;
		int linkss1tolcs;
		int linkss2tolcs;
		/* Calculamos el Least Common Synset y su profundidad */
		lcs = dictionary.getLeastCommonSubsumer(s1, s2);
		depthlcs = dictionary.depthOfSynset(lcs);

		linkss1tolcs = dictionary.getNumLinksBetweenSynsets(s1, lcs);
		linkss2tolcs = dictionary.getNumLinksBetweenSynsets(s2, lcs);

		totallinks = (2 * depthlcs) + linkss1tolcs + linkss2tolcs;

		/* Fórmula: 2*depth(LCS)/(depth(S1)+depth(S2) */
		distance = (double) (2 * depthlcs) / (double) totallinks;

		return distance;
	}

	/**
	 * Este método permite calcular la similitud semántica de Wu and Palmer para dos synsets de un subárbol de Wordnet
	 * @param s1- Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve el valor double que representa la similitud semántica de Wu and Palmer dentro del subárbol
	 * @throws JWNLException
	 */
	public double getWPSimilaritySubTree(Synset s1, Synset s2, LinkedHashMap<Synset, List<Synset>> tree) {
		
		double result = 0.0;
		Synset lcs = dictionary.getLeastCommonSubsumer(s1, s2);
		if (tree.containsKey(lcs)) {
			int depthlcs = dictionary.depthOfSynset(lcs, tree);
			int numlinkss1 = dictionary.getNumLinksBetweenSynsets(s1, lcs);
			int numlinkss2 = dictionary.getNumLinksBetweenSynsets(s2, lcs);
			result = (double) depthlcs / (double) (depthlcs + numlinkss1 + numlinkss2);
		}
		return result;
	}

	/**
	 * Este método permite calcular la similitud semántica de Wu and Palmer para dos synsets de un subárbol completo desde un Synset de Wordnet
	 * @param root - Objecto Synset raíz del subárbol 
	 * @param s1- Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor double que representa la similitud semántica de Wu and Palmer dentro del subárbol completo
	 * @throws JWNLException
	 */
	public double getWPSimilarityCompletedSubTree(Synset root, Synset s1, Synset s2) {
		LinkedHashMap<Synset, List<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return getWPSimilaritySubTree(s1, s2, tree);
	}
	
	/**
	 * Este método permite calcular la distancia semántica de Wu and Palmer para dos synsets de Wordnet
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor double que representa la distancia semántica de Wu and Palmer
	 */
	public double getDistanceWp(Synset s1, Synset s2) {
		return 1 - getWPSimilarity(s1, s2);
	}

	/**
	 * Este método calcula la distancia semántica de Wu and Palmer para el subárbol que se calcula dada una determinada altura
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return  Devuelve el valor double que representa la distancia semántica de Wu and Palmer del subárbol de Wordnet
	 * @throws JWNLException
	 */
	public double getDistanceWPSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, List<Synset>> tree){
		return 1 - getWPSimilaritySubTree(s1, s2, tree);
	}

	/**
	 * Este método calcula la distancia semántica de Wu and Palmer para el subárbol completo desde un Synset de la ontología
	 * @param root - Objecto Synset raíz del subárbol 
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor double que representa la distancia semántica de Wu and Palmer del subárbol de Wordnet
	 * @throws JWNLException
	 */
	public double getDistanceWPCompletedSubTree(Synset root, Synset s1, Synset s2){
		return 1 - getWPSimilarityCompletedSubTree(root, s1, s2);
	}
	

	/**
	 * Este método calcula la distancia semántica de Sanchez basada en conjuntos
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor double que representa la distancia semántica de Sanchez basada en conjuntos
	 */
	public double getSanchezDistance(Synset s1, Synset s2) {

		HashSet<Synset> s1Synsets = dictionary.getHypernymTreeList(s1);
		HashSet<Synset> s2Synsets = dictionary.getHypernymTreeList(s2);

		int numinsideAinB = dictionary.NotContainsFirstInSecond(s1Synsets, s2Synsets);
		int numinsideBinA = dictionary.NotContainsFirstInSecond(s2Synsets, s1Synsets);
		int intersection = dictionary.Intersection(s1Synsets, s2Synsets);

		double numfrac = (double) (numinsideAinB + numinsideBinA)
				/ (double) (numinsideAinB + numinsideBinA + intersection);
		
		return Math.log10(1 + numfrac) / Math.log10(2);
	}
	
	/**
	 * Este método calcula  la distancia semántica de Sanchez para conjunto de un subárbol de Wordnet
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @param tree -  Subárbol de Wordnet
	 * @return Devuelve el valor double que representa la distancia semántica de Sanchez basada en conjuntos para el subárbol
	 */
	public double getSanchezDistanceSubtree(Synset s1, Synset s2, LinkedHashMap<Synset, List<Synset>> tree) {

		Map.Entry<Synset, List<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, List<Synset>>>(
				tree.entrySet())).get(0);
		Synset root = rootnode.getKey();
		HashSet<Synset> s1Synsets = new HashSet<>(dictionary.getPathBetweenSynsets(s1, root));
		HashSet<Synset> s2Synsets = new HashSet<>(dictionary.getPathBetweenSynsets(s2, root));

		int numinsideAinB = dictionary.NotContainsFirstInSecond(s1Synsets, s2Synsets);
		int numinsideBinA = dictionary.NotContainsFirstInSecond(s2Synsets, s1Synsets);
		int intersection = dictionary.Intersection(s1Synsets, s2Synsets);

		double numfrac = (double) (numinsideAinB + numinsideBinA)
				/ (double) (numinsideAinB + numinsideBinA + intersection);

		return Math.log10(1 + numfrac) / Math.log10(2);
	}

	/**
	 * Este método calcula la distancia semántica de Sanchez para conjuntos de un subárbol completo
	 * @param root - Objecto Synset raíz del subárbol 
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor double que representa la distancia semántica de Sanchez basada en conjuntos para el subárbol completo desde un Synset
	 */
	public double getSanchezDistanceCompletedSubTree(Synset root, Synset s1, Synset s2) {
		LinkedHashMap<Synset, List<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return getSanchezDistanceSubtree(s1, s2, tree);
	}

	/**
	 * Este método calcula el número de nodos hojas de la ontología Wordnet
	 * @param synset - Objeto Synset de la ontología
	 * @return Devuelve el número de nodos hojas en el subárbol de descendientes del synset
	 * @throws JWNLException
	 */
	public int getnodeLeafs(Synset synset) throws JWNLException {

		HashSet<Synset> hyponymsynsets = dictionary.getHyponymsTreeList(synset);

		int leaves = 0;
		for (Synset hypo : hyponymsynsets) {
			List<Synset> hijos = dictionary.getHyponyms(hypo);

			if (hijos.isEmpty()) {
				leaves = leaves + 1;
			}
		}
		return leaves;
	}

	/**
	 * Este método calcula el valor IC de Sanchez 
	 * @param synset - Objeto Synset de la ontología
	 * @return  Devuelve el valor del IC de Sanchez para el Synset proporcionado como parámetro
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getICMeasure(Synset synset) throws JWNLException, IOException {
		/* IC Sanchez(c) =-log( (|leaves(c)|/|hypernyms(c)|+1)/max_leaves+1)) */
		int leaves = 0;
		double icvalue;
		
		leaves = getnodeLeafs(synset);
		HashSet<Synset> hypernymssynsets = dictionary.getHypernymTreeList(synset);

		double numer = ((double) leaves / (double) hypernymssynsets.size() + 1);
		int denom = 65031 + 1;

		double resultdiv = (double) numer / (double) denom;
		icvalue = -Math.log10(resultdiv);

		return icvalue;
	}

	/**
	 * Este método calcula la profundidad del Synset del subárbol recibido como parámetro
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve la profundidad del Synset dentro del subárbol
	 */
	/*public int getdepthSubTree(LinkedHashMap<Synset, List<Synset>> tree){
		int max = -999;
		for (Synset s : tree.keySet()) {
			int depthcurrentnode = dictionary.depthOfSynset(s, tree);
			max = Math.max(depthcurrentnode, max);
		}
		return max;

	}*/

	/**
	 * Este método calcula el número de nodos hoja dentro del subárbol de Wordnet que percibe como parámetros
	 * @param s - Objeto Synset de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve el número de Synsets hojas dentro del subárbol de Wordnet
	 * @throws JWNLException
	 */
	public int getleavesSubTree(Synset s, LinkedHashMap<Synset, List<Synset>> tree)
			throws JWNLException {
		HashSet<Synset> hyponymsfull = dictionary.getHyponymsTreeList(s);
		int leaves = 0;
	
		for (Synset current : hyponymsfull) {
			if (tree.containsKey(current) && dictionary.getHyponyms(current).isEmpty())
			{

					leaves++;
			}
		}
		return leaves;
	}

	/**
	 *  Este método calcula el valor IC de Sanchez para el synset que recibe como parámetro dentro del subárbol de Wordnet 
	 * @param s - Objeto Synset de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return  Devuelve el valor del IC de Sanchez para Synset proporcionado como parámetro que se encuentra dentro del subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getICMeasureSubTree(Synset synset, LinkedHashMap<Synset, List<Synset>> tree) throws JWNLException, IOException {
		double icvalue = 0;

		if (tree.containsKey(synset)) {
			Map.Entry<Synset, List<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, List<Synset>>>(
					tree.entrySet())).get(0);
			int numleaves = getleavesSubTree(synset, tree);
			List<Synset> hypernyms = dictionary.getPathBetweenSynsets(synset, rootnode.getKey());
			int numhypernyms = hypernyms.size();

			double numer = ((double) numleaves / (double) numhypernyms + 1);
			int denom = tree.size() + 1;
			
			double resultdiv = (double) numer / (double) denom;
			icvalue = -Math.log10(resultdiv);
		}
		return icvalue;

	}
	
	/**
	 * Este método devuelve el valor IC de Sanchez para un subárbol completo desde el Synset proporcionado como parámetro
	 * @param root  -  Objeto Synset raíz del subárbol
	 * @param s1 -  Objeto Synset de la ontología para el que se desea calcular el valor
	 * @return Devuelve el valor del IC de Sanchez para el Synset proporcionado como parámetro que se encuentra dentro del subárbol completo desde el synset raíz 
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getICMeasureCompletedSubTree(Synset root, Synset s1) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return getICMeasureSubTree(s1, tree);
	}
	
	/**
	 * Este método calcula el número de nodos hojas en toda la ontología de Wordnet
	 * @return Devuelve el número de nodos hojas de la ontología de Wordnet
	 * @throws IOException
	 * @throws JWNLException
	 */
	public int getLeafsWordnet() throws JWNLException {
		
		Synset s1 = dictionary.getSynset("entity", POS.NOUN, 1);
		HashSet<Synset> hyp = dictionary.getHyponymsTreeList(s1);
		int leavesW = 0;
		
		for (Synset a : hyp) {
			List<Synset> hijos = dictionary.getHyponyms(a);
			if (hijos.isEmpty())
				leavesW++;
		}
		return leavesW;
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Resnik entre los dos synsets de la ontología
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor de la distancia semántica de Resnik entre los dos synsets proporcionados como parámetros
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getResniskDistance(Synset s1, Synset s2) throws JWNLException, IOException {
		Synset lcs = dictionary.getLeastCommonSubsumer(s1, s2);
		return getICMeasure(lcs);
	}

	/**
	 * Este método calcula la distancia semántica de Resnik entre los dos synsets proporcionados como parámetros dentro del subárbol
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Resnik entre los dos synsets proporcionados como parámetros que se encuentran dentro del subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getResniskDistanceSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, List<Synset>> tree) throws JWNLException, IOException {

		Synset lcs = dictionary.getLeastCommonSubsumer(s1, s2);
		if (tree.containsKey(lcs)) {
			return getICMeasureSubTree(lcs, tree);
		}
		return 0.0;
	}
	
	/**
	 * Este método calcula el valor de la distancia semántica de Resnik entre los dos synsets proprocionados como parámetros que se encuentran dentro del subárbol
	 * @param root - Objeto Synset raíz del subárbol
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor de la distancia semántica de Resnik entre los dos synsets proporcionados como parámetros que se encuentran dentro del subárbol completo
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getResniskDistanceCompletedSubTree(Synset root, Synset s1, Synset s2) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return getResniskDistanceSubTree(s1, s2, tree);
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Lin entre los dos synsets proporcionados como parámetros que se encuentran dentro del subárbol
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor de la distancia semántica de Lin entre los dos synsets proporcionados como parámetros
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getLinDistance(Synset s1, Synset s2) throws JWNLException, IOException {
		double resultlin;
		double num;
		double div;
		num = 2 * getResniskDistance(s1, s2);
		div = getICMeasure(s1) + getICMeasure(s2);
		resultlin = num / div;
		return resultlin;
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Lin entre los dos synsets proporcionados como parámetros 
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Lin entre los dos synsets proporcionados como parámetros dentro del subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getLinDistanceSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, List<Synset>> tree) throws JWNLException, IOException {
		double resultlin;
		double div;

		if (tree.containsKey(s1) && tree.containsKey(s2)) {

			div = getICMeasureSubTree(s1, tree) + getICMeasureSubTree(s2, tree);

			resultlin =  (2 * getResniskDistanceSubTree(s1, s2, tree)) / div;
			return resultlin;
		} 
		return 0.0;
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Lin entre los dos synsets proporcionados como parámetros dentro del subárbol completo
	 * @param root - Objeto Synset raíz del subárbol
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return  Devuelve el valor de la distancia semántica de Lin entre los dos synsets proporcionados como parámetros dentro del subárbol completo
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getLinDistanceCompletedSubTree(Synset root, Synset s1, Synset s2) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return getLinDistanceSubTree(s1, s2, tree);
	}
	
	/**
	 * Este método calcula el valor de la distancia semántica de Jiang and Conrath entre los dos synsets proporcionados como parámetros
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath entre los dos synsets proporcionados como parámetros
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getJiangConrathDistance(Synset s1, Synset s2)
			throws JWNLException, IOException {
		double resultjc;
		double div;
		
		div = getICMeasure(s1) + getICMeasure(s2);
		resultjc = div - (2 * getResniskDistance(s1, s2));
		return resultjc;
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Jiang and Corath entre los dos synsets proporcionados como parámetros del subárbol
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @param tree - Subárbol de Wordnet
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath entre los dos synsts proporcionados como parámetros del subárbol
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getJiangConrathDistanceSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, List<Synset>> tree) throws JWNLException, IOException {
		double resultjc = 0;
		double sum;
		
		if (tree.containsKey(s1) && tree.containsKey(s2)) {
			sum = getICMeasureSubTree(s1, tree) + getICMeasureSubTree(s2, tree);
			resultjc = sum - (2 * getResniskDistanceSubTree(s1, s2, tree));
		}
		return resultjc;
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Jiang and Corath entre los dos synsets proporcionados como parámetros del subárbol completo
	 * @param root - Objeto Synset raíz del subárbol
	 * @param s1 - Objeto Synset1 de la ontología
	 * @param s2 - Objeto Synset2 de la ontología
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath entre los dos synsts proporcionados como parámetros del subárbol completo
	 * @throws JWNLException
	 * @throws IOException
	 */
	public double getjianConrathDistanceCompletedSubTree(Synset root, Synset s1, Synset s2) throws JWNLException, IOException {
		LinkedHashMap<Synset, List<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return getJiangConrathDistanceSubTree(s1, s2, tree);
	}

}
