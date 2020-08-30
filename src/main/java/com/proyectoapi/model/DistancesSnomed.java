package com.proyectoapi.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Class DistancesSnomed: Esta clase calcula las distancias semánticas para la ontología de Snomed CT.
 * @author Lucía Batista Flores
 * @version 1.0
 */

public class DistancesSnomed {

	SnomedCTLibrary snomed = new SnomedCTLibrary();

	/**
	 * Este método calcula la similitud semántica de Wu and Palmer para dos conceptos de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la similitud semántica de Wu and Palmer entre dos conceptos
	 */
	public double getwpSimilarity(long idConcept1, long idConcept2) {

		HashMap<Long, Integer> id1 = snomed.getAncestorsDistanciaMinima(idConcept1);
		HashMap<Long, Integer> id2 = snomed.getAncestorsDistanciaMinima(idConcept2);

		if (id1.size() > id2.size()) {
			HashMap<Long, Integer> ancestresTemporal = id1;
			id1 = id2;
			id2 = ancestresTemporal;
		}
		long idLCS = -1;
		int distanciaLCS = Integer.MAX_VALUE;
		Set<Long> set = id1.keySet();
		for (Long id : set)
			if (id2.containsKey(id) && (distanciaLCS > id1.get(id) + id2.get(id))) {
				idLCS = id;
				distanciaLCS = id1.get(id) + id2.get(id);
			}
		if (idLCS == -1)
			return 0;
		int distancia = snomed.getAncestorsMaxDistance(idLCS);

		return ((double) 2 * distancia / (double) (distanciaLCS + 2 * distancia));

	}

	/**
	 * Este método calcula la similitud semántica de Wu and Palmer para dos conceptos dentro de un subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la similitud semántica de Wu and Palmer 
	 */
	public double getWuSimilaritySubTree(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subtree) {
		HashMap<Long, Integer> id1 = snomed.getAncestrosDistanciaMinimaSubTreeSnomed(idConcept1, subtree);
		HashMap<Long, Integer> id2 = snomed.getAncestrosDistanciaMinimaSubTreeSnomed(idConcept2, subtree);
		
		if (id1.size() > id2.size()) {
			HashMap<Long, Integer> ancestresTemporal = id1;
			id1 = id2;
			id2 = ancestresTemporal;
		}
		long idLCS = -1;
		int distanciaLCS = Integer.MAX_VALUE;
		Set<Long> set = id1.keySet();
		for(Long id:set) {
			if (id2.containsKey(id) && (distanciaLCS > id1.get(id) + id2.get(id))) {
				idLCS = id;
				distanciaLCS = id1.get(id) + id2.get(id);
			}
		}
		if (idLCS == -1)
			return 0;
		if (subtree.containsKey(idLCS)) {
			int distancia = snomed.getMaxDistanceToEntitySubTreeSnomed(idLCS, subtree);
			return ((double) 2 * distancia / (double) (distanciaLCS + 2 * distancia));
		} 
		return 0;
	}

	/**
	 * Este método calcula la similitud semántica de Wu and Palmer para dos conceptos dentro de un subárbol completo
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la similitud semántica de Wu and Palmer para el subárbol completo
	 */
	public  double getWuSimilaritySubTree(long idConceptroot, long idConcept1, long idConcept2) {
		LinkedHashMap<Long, Integer> subtree = snomed.getSubTreeSnomed(idConceptroot);
		return getWuSimilaritySubTree(idConcept1, idConcept2, subtree);
	}
	
	/**
	 * Este método calcula la distancia semántica de Wu and Palmer para dos conceptos
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos conceptos de Snomed CT
	 */
	public double getdistanceWP(long idConcept1, long idConcept2) {
		return 1 - getwpSimilarity(idConcept1, idConcept2);
	}
	
	/**
	 * Este método calcula la distancia semántica de Wu and Palmer para dos conceptos dentro de un subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos conceptos de Snomed CT de un subárbol
	 */
	public double getdistanceWP(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subtree) {
		return 1 - getWuSimilaritySubTree(idConcept1, idConcept2, subtree);
	}
	
	/**
	 * Este método calcula la distancia semántica de Wu and Palmer para dos conceptos dentro de un subárbol completo
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia semántica de Wu and Palmer entre dos conceptos de Snomed CT de un subárbol completo
	 */
	public double getdistanceWP(long idConceptroot, long idConcept1, long idConcept2) {
		return 1 - getWuSimilaritySubTree(idConceptroot, idConcept1, idConcept2);
	}
	
	/**
	 * Este método calcula la distancia de Sanchez basada en conjuntos entre dos conceptos de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos conceptos de Snomed CT
	 */
	public double SanchezDistance(long idConcept1, long idConcept2) {
		Set<Long> id1 = snomed.getAncestorsDistanciaMinima(idConcept1).keySet();
		Set<Long> id2 = snomed.getAncestorsDistanciaMinima(idConcept2).keySet();

		int numinsideAinB = snomed.NotContainsFirstInSecond(id1, id2);
		int numinsideBinA = snomed.NotContainsFirstInSecond(id2, id1);
		int intersection = snomed.Intersection(id1, id2);

		double numfrac = (double) (numinsideAinB + numinsideBinA)
				/ (double) (numinsideAinB + numinsideBinA + intersection);
	
		return Math.log10(1 + numfrac) / Math.log10(2);
	}

	/**
	 * Este método calcula la diatancia de Sanchez basada en conjuntos entre dos conceptos de Snomed CT de un subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos conceptos de Snomed CT de un subárbol
	 */
	public double SanchezDistanceSubTree(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subtree) {
		Set<Long> id1 = snomed.getAncestrosDistanciaMinimaSubTreeSnomed(idConcept1, subtree).keySet();
		Set<Long> id2 = snomed.getAncestrosDistanciaMinimaSubTreeSnomed(idConcept2, subtree).keySet();

		int numinsideAinB = snomed.NotContainsFirstInSecond(id1, id2);
		int numinsideBinA = snomed.NotContainsFirstInSecond(id2, id1);
		int intersection = snomed.Intersection(id1, id2);

		double numfrac = (double) (numinsideAinB + numinsideBinA)
				/ (double) (numinsideAinB + numinsideBinA + intersection);

		return Math.log10(1 + numfrac) / Math.log10(2);
	}

	/**
	 * Este método calcula la diatancia de Sanchez basada en conjuntos entre dos conceptos de Snomed CT de un subárbol completo
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia semántica de Sanchez entre dos conceptos de Snomed CT de un subárbol completo
	 */
	public  double getSanchezDistanceSubTree(long idConceptroot, long idConcept1, long idConcept2) {
		LinkedHashMap<Long, Integer> subtree = snomed.getSubTreeSnomed(idConceptroot);
		return SanchezDistanceSubTree(idConcept1, idConcept2, subtree);
	}
	
	/**
	 * Este método calcula el número de conceptos que son nodos hoja del subárbol del descendientes
	 * @param descendantsnode - Listado de descendientes
	 * @return Devuelve el número de nodos hojas del subárbol
	 */
	public int getLeafNodes(List<Long> descendantsnode) {
		int leafs = 0;
		for (Long node : descendantsnode) {
			if (snomed.getHyponymsIdConcepts(node).isEmpty()) {
				leafs++;
			}
		}
		return leafs;

	}



	/**
	 * Este método devuelve el valor del IC de Sanchez para un concepto de Snomed CT
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @return Devuelve el valor del IC de Sanchez para un concepto de Snomed CT
	 */
	public double IC_measure(long idConcept) {
		List<Long> ancestorsnode = snomed.getAncestors(idConcept);
		List<Long> descendantsnode = snomed.getDescendants(idConcept);
		int leafs = getLeafNodes(descendantsnode);

		double icvalue;
		double numer = ((double) leafs / (double) ancestorsnode.size() + 1);
		long entity = 138875005;
		int denom = snomed.getDescendants(entity).size() + 1;
		
		double resultdiv = (double) numer / (double) denom;
		icvalue = -Math.log10(resultdiv);

		return icvalue;

	}

	/**
	 * Este método devuelve el valor del IC de Sanchez para un concepto de un subárbol de Snomed CT
	 * @param idConcept - Número de tipo long que identifica de manera única al concepto
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor del IC de un concepto de Snomed perteneciente a un subárbol
	 */
	public double IC_measureSubTree(long idConcept, LinkedHashMap<Long, Integer> subtree) {
		List<Long> ancestorsnode = snomed.getAncestorsSubTreeSnomed(idConcept, subtree); //Mirar porque no coincide
		List<Long> descendantsnode = snomed.getDescendantsSubTreeSnomed(idConcept, subtree);
	
		int leafs = getLeafNodes(descendantsnode);
		double icvalue;

		double numer = ((double) leafs / (double) ancestorsnode.size()+1);
		int denom = subtree.size() + 1;
		double resultdiv = (double) numer / (double) denom;

		icvalue = -Math.log10(resultdiv);

		return icvalue;
	}

	/**
	 * Este método devuelve el valor del IC de Sanchez para un concepto de un subárbol completo de Snomed CT
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @return Devuelve el valor del IC de un concepto de Snomed perteneciente a un subárbol
	 */
	public  double getIC_measureSubTree(long idConceptroot, long idConcept1) {
		LinkedHashMap<Long, Integer> subtree = snomed.getSubTreeSnomed(idConceptroot);
		return IC_measureSubTree(idConcept1, subtree);
	}
	
	/**
	 * Este método calcula el valor de la distancia de Resnik entre dos conceptos de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia de Resnik entre dos conceptos de Snomed CT
	 */
	public double resnik_Distance(long idConcept1, long idConcept2) {
		long lcsConcept = snomed.getLCS(idConcept1, idConcept2);
		return IC_measure(lcsConcept);
	}

	/**
	 * Este método calcula el valor de la distancia de Resnik entre dos conceptos pertenecientes a un subárbol de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia de Resnik entre dos conceptos de Snomed CT de un subárbol 
	 */
	public double resnik_DistanceSubTree(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subtree) {
		long lcsConcept = snomed.getLCS(idConcept1, idConcept2);
		if (subtree.containsKey(lcsConcept))
			return IC_measureSubTree(lcsConcept, subtree);
		return 0;
	}
	
	/**
	 * Este método calcula el valor de la distancia de Resnik entre dos conceptos de Snomed pertenecientes a un subárbol completo
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia de Resnik entre dos conceptos de Snomed CT de un subárbol completo
	 */
	public  double getResnik_DistanceSubTree(long idConceptroot, long idConcept1, long idConcept2) {
		LinkedHashMap<Long, Integer> subtree = snomed.getSubTreeSnomed(idConceptroot);
		return resnik_DistanceSubTree(idConcept1, idConcept2, subtree);
	}

	/**
	 * Este método calcula el valor de la distancia semántica de Lin entre dos conceptos de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return  Devuelve el valor de la distancia de Resnik entre dos conceptos de Snomed CT 
	 */
	public double lin_Distance(long idConcept1, long idConcept2) {
		double resultlin;
		double div;
		
		div = IC_measure(idConcept1) + IC_measure(idConcept2);
		resultlin = (2 * resnik_Distance(idConcept1, idConcept2)) / div;
		return resultlin;
	}

	/**
	 * Este método calcula la distancia semántica de Lin entre dos conceptos pertenecientes a un subárbol de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia de Lin entre los dos conceptos de Snomed CT de un subárbol 
	 */
	public double lin_DistanceSubTree(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subtree) {
		double resultlin;
		double num;
		double div;
		num = 2 * resnik_DistanceSubTree(idConcept1, idConcept2, subtree);
		div = IC_measureSubTree(idConcept1, subtree) + IC_measureSubTree(idConcept2, subtree);
		resultlin = num / div;
		return resultlin;

	}
	
	/**
	 * Este método calcula la distancia semántica de Lin entre dos conceptos pertenecientes a un subárbol completo de Snomed CT
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia de Lin entre dos conceptos de Snomed CT de un subárbol completo
	 */
	public  double getLin_DistanceSubTree(long idConceptroot, long idConcept1, long idConcept2) {
		LinkedHashMap<Long, Integer> subtree = snomed.getSubTreeSnomed(idConceptroot);
		return lin_DistanceSubTree(idConcept1, idConcept2, subtree);
	}

	/**
	 * Este método calcula la distancia semántica de Jiang and Conrath entre dos conceptos de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return  Devuelve el valor de la distancia de Jiang and Conrath entre dos conceptos de Snomed CT
	 */
	public double jianConrath_Distance(long idConcept1, long idConcept2) {
		double div;
		div = IC_measure(idConcept1) + IC_measure(idConcept2);
		return div - (2 * resnik_Distance(idConcept1, idConcept2));
	}

	/**
	 * Este método calcula la distancia semántica de Jiang and Conrath pertenecientes a un subárbol de Snomed CT
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @param subtree - Subárbol de Snomed CT
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath entre los dos conceptos de Snomed CT de un subárbol 
	 */
	public double jianConrath_DistanceSubGraph(long idConcept1, long idConcept2,
			LinkedHashMap<Long, Integer> subtree) {
		double div;
		div = IC_measureSubTree(idConcept1, subtree) + IC_measureSubTree(idConcept2, subtree);
		return div - (2 * resnik_DistanceSubTree(idConcept1, idConcept2, subtree));

	}

	/**
	 * Este método calcula la distancia semántica de Jiang and Conrath pertenecientes a un subárbol completo de Snomed CT
	 * @param idConceptroot - Número de tipo long que identifica de manera única al concepto raíz del subárbol
	 * @param idConcept1 - Número de tipo long que identifica de manera única al concepto1
	 * @param idConcept2 - Número de tipo long que identifica de manera única al concepto2
	 * @return Devuelve el valor de la distancia semántica de Jiang and Conrath entre los dos conceptos de Snomed CT de un subárbol completo
	 */
	public  double getJianConrath_DistanceTree(long idConceptroot, long idConcept1, long idConcept2) {
		LinkedHashMap<Long, Integer> subtree = snomed.getSubTreeSnomed(idConceptroot);
		return jianConrath_DistanceSubGraph(idConcept1, idConcept2, subtree);
	}

}
