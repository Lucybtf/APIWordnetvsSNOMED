package com.proyectoapi.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;

public class DistancesWordnet {

	WordnetLibrary dictionary = new WordnetLibrary();

	/* Similitud de Wu and Palmer */
	public double WPSimilarity(Synset s1, Synset s2, WordnetLibrary dictionary) {
		double distance = 0.0d;

		Synset lcs;

		/* Calculamos la profundidad de ambos synsets */
		int depth, depth2, totallinks, depthlcs, linkss1tolcs, linkss2tolcs;
		try {
			depth = dictionary.depthOfSynset(s1);
			depth2 = dictionary.depthOfSynset(s2);

			/* Calculamos el Least Common Synset y su profundidad */
			lcs = dictionary.getLeastCommonSubsumer(s1, s2);
			depthlcs = dictionary.depthOfSynset(lcs);

			linkss1tolcs = dictionary.getNumLinksBetweenSynsets(s1, lcs);
			linkss2tolcs = dictionary.getNumLinksBetweenSynsets(s2, lcs);

			totallinks = (2 * depthlcs) + linkss1tolcs + linkss2tolcs;

			/* Fórmula: 2*depth(LCS)/(depth(S1)+depth(S2) */
			distance = (double) (2 * depthlcs) / (double) totallinks;

		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return distance;
	}

	public double WPSimilaritySubTree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree) throws JWNLException {
		// System.out.print("ENTRA EN SUBTREE");
		double result = 0.0;
		Synset lcs = dictionary.getLeastCommonSubsumer(s1, s2);// Y si en el subarbol no estuviera el LCS?
		// System.out.print("\nLCS->"+lcs);
		if (tree.containsKey(lcs)) {
			int depthlcs = dictionary.depthOfSynset(lcs, tree);
			int numlinkss1 = dictionary.getNumLinksBetweenSynsets(s1, lcs);
			int numlinkss2 = dictionary.getNumLinksBetweenSynsets(s2, lcs);
			// System.out.print("\n\nDEPTH lcs->"+lcs.getWord(0).getLemma()+"
			// "+dictionary.depthOfSynset(lcs, tree)+ " NUMLINKSS1->"+numlinkss1+"
			// NUMLINKSS2->"+numlinkss2+"\n");
			result = (double) depthlcs / (double) (depthlcs + numlinkss1 + numlinkss2);
		}
		return result;
	}

	public double WPSimilaritySubTree(Synset root, Synset s1, Synset s2) throws JWNLException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return WPSimilaritySubTree(s1, s2, tree);
	}
	
	public double Distance_WP(Synset s1, Synset s2, WordnetLibrary dictionary) {
		return 1 - WPSimilarity(s1, s2, dictionary);
	}

	public double Distance_WPSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree,
			WordnetLibrary dictionary) throws JWNLException {
		return 1 - WPSimilaritySubTree(s1, s2, tree);
	}

	public double Distance_WPSubTree(Synset root, Synset s1, Synset s2) throws JWNLException {
		return 1 - WPSimilaritySubTree(root, s1, s2);
	}
	
	public double Sanchez_DistanceSubtree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree,
			WordnetLibrary dictionary) {

		Map.Entry<Synset, ArrayList<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, ArrayList<Synset>>>(
				tree.entrySet())).get(0);
		Synset root = rootnode.getKey();
		HashSet<Synset> S1synsets = new HashSet(dictionary.getPathBetweenSynsets(s1, root));
		// System.out.print("\n\nCAMINO DE S1("+s1.getWord(0).getLemma()+") A
		// ROOT("+root.getWord(0).getLemma()+"):"+S1synsets);
		HashSet<Synset> S2synsets = new HashSet(dictionary.getPathBetweenSynsets(s2, root));
		// System.out.print("\n\nCAMINO DE S2("+s2.getWord(0).getLemma()+") A
		// ROOT("+root.getWord(0).getLemma()+"):"+S2synsets);

		int numinsideAinB = dictionary.NotContainsFirstInSecond(S1synsets, S2synsets);
		int numinsideBinA = dictionary.NotContainsFirstInSecond(S2synsets, S1synsets);
		int intersection = dictionary.Intersection(S1synsets, S2synsets);

		/*
		 * System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		 * System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		 * System.out.print("\n\nInteresection "+ intersection+"\n");
		 */

		double num_frac = (double) (numinsideAinB + numinsideBinA)
				/ (double) (numinsideAinB + numinsideBinA + intersection);
		double distance_sanchez = Math.log10(1 + num_frac) / Math.log10(2);

		return distance_sanchez;
	}

	public double Sanchez_Distance(Synset s1, Synset s2, WordnetLibrary dictionary) {

		HashSet<Synset> S1synsets = dictionary.getHypernymTreeList(s1);
		HashSet<Synset> S2synsets = dictionary.getHypernymTreeList(s2);

		int numinsideAinB = dictionary.NotContainsFirstInSecond(S1synsets, S2synsets);
		int numinsideBinA = dictionary.NotContainsFirstInSecond(S2synsets, S1synsets);
		int intersection = dictionary.Intersection(S1synsets, S2synsets);
		// System.out.print("\n\nContiene "+numinsideAinB +" elementos de A en B");
		// System.out.print("\n\nContiene "+numinsideBinA +" elementos de B en A");
		// System.out.print("\n\nInteresection "+ intersection);

		double num_frac = (double) (numinsideAinB + numinsideBinA)
				/ (double) (numinsideAinB + numinsideBinA + intersection);
		double distance_sanchez = Math.log10(1 + num_frac) / Math.log10(2);
		// System.out.print("Distancia de Sanchez:"+distance_sanchez);

		return distance_sanchez;

	}
	
	public double Sanchez_Distance(Synset root, Synset s1, Synset s2) {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return Sanchez_Distance(s1, s2, dictionary);
	}

	public int nodeLeafs(Synset synset) throws JWNLException {

		WordnetLibrary dictionary = new WordnetLibrary();
		HashSet<Synset> hyponymsynsets = dictionary.getHyponymsTreeList(synset);

		int leaves = 0;
		for (Synset hypo : hyponymsynsets) {
			// System.out.print("\nHOJAS"+leaves+"\n");
			ArrayList<Synset> hijos = dictionary.getHyponyms(hypo);

			if (hijos.size() == 0) {
				// System.out.print(leaves+"LISTA HYPONYMS->"+ hypo.getWord(0).getLemma()+" ");
				// System.out.print("HIJOS->"+hijos.size()+" ");
				leaves = leaves + 1;
				// System.out.print("HOJAS"+leaves+"\n");
			}

		}
		// System.out.print("HOJAS"+leaves+"\n");
		return leaves;
	}



	public double IC_measure(Synset synset, WordnetLibrary dictionary) throws JWNLException, IOException {
		/* IC Sanchez(c) =-log( (|leaves(c)|/|hypernyms(c)|+1)/max_leaves+1)) */
		int leaves = 0;

		HashSet<Synset> hyponymsynsets = dictionary.getHyponymsTreeList(synset);
		leaves = nodeLeafs(synset);
		// System.out.print("\nLEAVES FINALES->"+leaves);

		HashSet<Synset> hypernymssynsets = dictionary.getHypernymTreeList(synset);
		// System.out.print("HIPERNOMIMOS "+ hypernymssynsets.size()+"\n");

		double icvalue = 0.0;
		double numer = ((double) leaves / (double) hypernymssynsets.size() + 1);
		int denom = 65031 + 1;
		// System.out.print("Numerador->"+numer);//+ "Denominador->"+getLeafsWordnet());

		double resultdiv = (double) numer / (double) denom;
		// System.out.print("RESULTDIV->"+resultdiv);
		icvalue = -Math.log10(resultdiv);

		return icvalue;
	}

	public int depthSubTree(LinkedHashMap<Synset, ArrayList<Synset>> tree, WordnetLibrary dictionary)
			throws JWNLException {
		int max = -999;
		for (Synset s : tree.keySet()) {
			// Set<Entry<Synset, ArrayList<Synset>>> pair = tree.entrySet();
			int depthcurrentnode = dictionary.depthOfSynset(s, tree);
			// System.out.print("SYNSET->"+s.getWord(0).getLemma()+"depth"+ depthcurrentnode
			// +"\n");
			max = Math.max(depthcurrentnode, max);
		}
		return max;

	}

	public int leavesSubTree(Synset s, LinkedHashMap<Synset, ArrayList<Synset>> tree, WordnetLibrary dictionary)
			throws JWNLException {
		ArrayList<Synset> hyponymstree = new ArrayList<Synset>();
		HashSet<Synset> hyponymsfull = dictionary.getHyponymsTreeList(s);
		int leaves = 0;
		// System.out.print("HYPONYMS FULL"+ hyponymsfull+"\n");
		for (Synset current : hyponymsfull) {
			if (tree.containsKey(current)) // && dictionary.getHyponyms(current).size()==0)
			{ // System.out.print("leaves"+leaves+"SYNSET"+current.getWord(0).getLemma()+"RES"+tree.containsKey(s)+"hijos"+dictionary.getHyponyms(current).size()
				// +"\n");
				if (dictionary.getHyponyms(current).size() == 0)
					leaves++;
				// hyponymstree.add(current);
			}
		}
		return leaves;
	}

	public double IC_measureSubTree(Synset synset, LinkedHashMap<Synset, ArrayList<Synset>> tree,
			WordnetLibrary dictionary) throws JWNLException, IOException {
		/* IC Sanchez(c) =-log( (|leaves(c)|/|hypernyms(c)|+1)/max_leaves+1)) */
		int leaves = 0;
		double icvalue = 0.0;

		if (tree.containsKey(synset)) {
			Map.Entry<Synset, ArrayList<Synset>> rootnode = (new ArrayList<Map.Entry<Synset, ArrayList<Synset>>>(
					tree.entrySet())).get(0);

			// dictionary.printTree(tree);
			int numleaves = leavesSubTree(synset, tree, dictionary);
			ArrayList<Synset> hypernyms = dictionary.getPathBetweenSynsets(synset, rootnode.getKey());
			// System.out.print("HIPER synset"+synset.getWord(0).getLemma()+hypernyms);
			int numhypernyms = hypernyms.size();

			double numer = ((double) numleaves / (double) numhypernyms + 1);
			int denom = tree.size() + 1;
			// System.out.print("num
			// leaves"+numleaves+"numhypernyms"+numhypernyms+"Numerador->"+numer+
			// "Denominador->"+denom+"\n");

			double resultdiv = (double) numer / (double) denom;
			// System.out.print("RESULTDIV->"+resultdiv);
			icvalue = -Math.log10(resultdiv);
		}
		return icvalue;

	}
	
	public double IC_measureSubTree(Synset root,Synset s1) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return IC_measureSubTree(s1, tree, dictionary);
	}
	

	public int getLeafsWordnet() throws IOException, JWNLException {
		// WordnetLibrary dictionary = new WordnetLibrary();
		Synset s1 = dictionary.getSynset("entity", POS.NOUN, 1);
		// System.out.print("\nNODOS DE ENTITY");

		HashSet<Synset> hyp = dictionary.getHyponymsTreeList(s1);
		// System.out.print("TAMAÑO NODOS"+hyp.size()+"\n");

		int leavesW = 0;
		// FileWriter myWriter = new FileWriter("./rdf/filename.txt");
		for (Synset a : hyp) {
			ArrayList<Synset> hijos = dictionary.getHyponyms(a);
			if (hijos.size() == 0)
				leavesW++;
		}
		return leavesW;
	}

	public double resnisk_Distance(Synset s1, Synset s2, WordnetLibrary dictionary) throws JWNLException, IOException {

		Synset lcs = dictionary.getLeastCommonSubsumer(s1, s2);
		// System.out.print("lcs->"+lcs);
		// System.out.print("Synset LCS->"+lcs);
		return IC_measure(lcs, dictionary);
	}

	public double resnisk_DistanceSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree,
			WordnetLibrary dictionary) throws JWNLException, IOException {

		Synset lcs = dictionary.getLeastCommonSubsumer(s1, s2);
		// System.out.print("lcs->"+lcs);
		// System.out.print("Synset LCS->"+lcs);
		if (tree.containsKey(lcs)) {
			// System.out.print("\nIC SUbtree->"+IC_measureSubTree(lcs, tree,
			// dictionary)+"\n");
			return IC_measureSubTree(lcs, tree, dictionary);
		}
		return 0.0;
	}
	
	public double resnisk_DistanceSubTree(Synset root, Synset s1, Synset s2) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return resnisk_DistanceSubTree(s1, s2, tree, dictionary);
	}

	public double lin_Distance(Synset s1, Synset s2, WordnetLibrary dictionary) throws JWNLException, IOException {
		double resultlin = 0.0, num = 0.0, div = 0.0;
		num = 2 * resnisk_Distance(s1, s2, dictionary);
		div = IC_measure(s1, dictionary) + IC_measure(s2, dictionary);
		resultlin = num / div;
		return resultlin;
	}

	public double lin_DistanceSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree,
			WordnetLibrary dictionary) throws JWNLException, IOException {
		double resultlin = 0.0, num = 0.0, div = 0.0;

		if (tree.containsKey(s1) && tree.containsKey(s2)) {
			num = 2 * resnisk_DistanceSubTree(s1, s2, tree, dictionary);
			div = IC_measureSubTree(s1, tree, dictionary) + IC_measureSubTree(s2, tree, dictionary);

			resultlin = num / div;
			// System.out.print("RESNISK SUBTREE->"+resnisk_DistanceSubTree(s1, s2, tree,
			// dictionary)+"num"+ num+"div"+div+"resultlin"+resultlin+"\n");
			return resultlin;
		} else
			return 0.0;
	}

	public double lin_DistanceSubTree(Synset root, Synset s1, Synset s2) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return lin_DistanceSubTree(s1, s2, tree, dictionary);
	}
	
	public double jianConrath_Distance(Synset s1, Synset s2, WordnetLibrary dictionary)
			throws JWNLException, IOException {
		double resultjc = 0.0, num = 0.0, div = 0.0, res = 0.0;
		System.out.print("IC Jian->" + IC_measure(s1, dictionary) + "\n");
		System.out.print("IC Jiang->" + IC_measure(s2, dictionary) + "\n");
		div = IC_measure(s1, dictionary) + IC_measure(s2, dictionary);
		res = 2 * resnisk_Distance(s1, s2, dictionary);
		resultjc = div - (2 * resnisk_Distance(s1, s2, dictionary));
		return resultjc;
	}

	public double jianConrath_DistanceSubTree(Synset s1, Synset s2, LinkedHashMap<Synset, ArrayList<Synset>> tree,
			WordnetLibrary dictionary) throws JWNLException, IOException {
		double resultjc = 0.0, num = 0.0, sum = 0.0;
		if (tree.containsKey(s1) && tree.containsKey(s2)) {
			// System.out.print("ICSUBTREE s1->"+IC_measureSubTree(s1, tree,
			// dictionary)+"\n");
			// System.out.print("ICSUBTREE s2->"+IC_measureSubTree(s2, tree,
			// dictionary)+"\n");
			sum = IC_measureSubTree(s1, tree, dictionary) + IC_measureSubTree(s2, tree, dictionary);
			// System.out.print("SUMA->"+sum+"\n");
			resultjc = sum - (2 * resnisk_DistanceSubTree(s1, s2, tree, dictionary));
			// System.out.print("JCresnisk->"+2*resnisk_DistanceSubTree(s1, s2, tree,
			// dictionary)+"div"+sum+"\n");
		}
		return resultjc;
	}

	public double jianConrath_DistanceSubTree(Synset root, Synset s1, Synset s2) throws JWNLException, IOException {
		LinkedHashMap<Synset, ArrayList<Synset>> tree = dictionary.getSubarbolWordnet(root);
		return jianConrath_DistanceSubTree(s1, s2, tree, dictionary);
	}
	
	/*
	 * public static void main(String[] arg) throws JWNLException, IOException{
	 * 
	 * WordnetLibrary dictionarymain = new WordnetLibrary(); final String word =
	 * "kitten", word2 ="cat"; final POS pos = POS.NOUN;
	 * 
	 * double wp = WPSimilarity(word, pos, word2, pos); double distance_wp =
	 * Distance_WP(word, pos, word2, pos);
	 * System.out.print("\n\nLa similitud de Wu and Palmer es:"+wp+"\n");
	 * System.out.print("\n\nLa similitud de Wu and Palmer es:"+distance_wp+"\n");
	 * 
	 * 
	 * 
	 * // double sanchez = Sanchez_Similarity(word, pos, word2, pos);
	 * //System.out.print("\n\nLa similitud de Sanchez es:"+sanchez+"\n");
	 * 
	 * 
	 * long offset =2124272, kitty_offset=2124950, sand_cat_offset=2127662,
	 * cat_offset=2124272, party_offset=7462241, chordate_offset =1468898,
	 * animal_offset=15568, feline_offset=2123649; Synset cat =
	 * dictionarymain.getSynset(cat_offset); Synset sand_cat =
	 * dictionarymain.getSynset(sand_cat_offset); Synset kitty =
	 * dictionarymain.getSynset(kitty_offset); Synset party =
	 * dictionarymain.getSynset(party_offset); Synset chordate =
	 * dictionarymain.getSynset(chordate_offset); Synset animal =
	 * dictionarymain.getSynset(animal_offset); Synset feline =
	 * dictionarymain.getSynset(feline_offset); Synset lynx =
	 * dictionarymain.getSynset(2129704); //LinkedHashMap<Synset, ArrayList<Synset>>
	 * tree = new LinkedHashMap<Synset, ArrayList<Synset>>(); LinkedHashMap<Synset,
	 * ArrayList<Synset>> treecat = new LinkedHashMap<Synset, ArrayList<Synset>>();
	 * //LinkedHashMap<Synset, ArrayList<Synset>> treeanimal = new
	 * LinkedHashMap<Synset, ArrayList<Synset>>();
	 * 
	 * treecat = dictionarymain.getSubarbolWordnet(cat, 3, treecat); //Testado con
	 * cat //tree = dictionarymain.getSubarbolWordnet(party,4, tree); //Testado con
	 * cat //treeanimal = dictionarymain.getSubarbolWordnet(animal,9, treeanimal);
	 * // dictionarymain.printTree(treecat); // double wptree =
	 * WPSimilaritySubTree(cat,kitty,treecat); // System.out.print("\nRES"+wptree);
	 * Distance distance = new Distance(); double wptree =
	 * distance.IC_measureSubTree(dictionarymain.getSynset(2127275),treecat,
	 * dictionarymain); System.out.print("\n\nSUBTREE->"+wptree); //
	 * System.out.print("\n\nDEPTH SUBARBOL cotillion"+dictionarymain.depthOfSynset(
	 * dictionarymain.getSynset(7463637), tree)); /*System.out.
	 * print("\n\nDEPTH SUBARBOL CAT ENCONTRAR FELINE(NO EN EL ARBOL)->"
	 * +dictionarymain.depthOfSynset(dictionarymain.getSynset(2123649), treecat));
	 * System.out.print("\n\nDEPTH SUBARBOL CAT(KITTY)->"+dictionarymain.
	 * depthOfSynset(dictionarymain.getSynset(kitty_offset), treecat));
	 * System.out.print("\n\nDEPTH SUBARBOL CAT(ES LA RAIZ)->"+dictionarymain.
	 * depthOfSynset(dictionarymain.getSynset(cat_offset), treecat));
	 * //System.out.print("\n\nDEPTH SUBARBOL ANIMAL"+dictionarymain.depthOfSynset(
	 * dictionarymain.getSynset(2127662), treeanimal));
	 * 
	 * // dictionarymain.printTree(tree); //
	 * dictionarymain.getSubarbolWordnet(cat,2); // Synset s=wordnet
	 * //getNodesToEntity(Synset synset)
	 * //System.out.print("IC cat->"+IC_measure(offset)); System.out.print("\n\n");
	 * double res = resnisk_Distance(kitty,sand_cat); double lin =
	 * lin_Distance(kitty,sand_cat); double jc =
	 * jianCorath_Distance(kitty,sand_cat); System.out.print("\nMedida RES:"+ res+
	 * "\nMedida Lin:"+lin+ "\nMedida jc:"+jc); }
	 */

}
