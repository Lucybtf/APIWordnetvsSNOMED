package com.example.springbootswagger2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import net.didion.jwnl.data.Synset;

public class DistanceSnomed {
	 
	SnomedCTLibrary snomed;
	 
	 
		public double wuSimilarity(long idConcept1, long idConcept2) {
			HashMap<Long, Integer> id1 = snomed.AncestrosDistanciaMinima(idConcept1);
			HashMap<Long, Integer> id2 = snomed.AncestrosDistanciaMinima(idConcept2);
			
			if(id1.size()>id2.size()){
				HashMap<Long, Integer> ancestresTemporal=id1;
				id1=id2;
				id2=ancestresTemporal;
			}
			long idLCS=-1;
			int distanciaLCS=Integer.MAX_VALUE;
			for(Long id : id1.keySet()) 
				if(id2.containsKey(id) && (distanciaLCS>id1.get(id)+id2.get(id))) {
					idLCS=id;
					distanciaLCS=id1.get(id)+id2.get(id);
				}
			if(idLCS==-1) return 0;
		//	int distancia = trobarDistanciaMaximaFinsArrel(idLCS);
			//return ((double)2*distancia/(double)(distanciaLCS+2*distancia));	
			return distanciaLCS;
		}
		
		public double wuSimilaritySubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph) {
			return idConcept2;
			
		}
		
		
		public double SanchezDistance(long idConcept1, long idConcept2) {
			Set<Long> id1 = snomed.AncestrosDistanciaMinima(idConcept1).keySet();
			Set<Long> id2 = snomed.AncestrosDistanciaMinima(idConcept2).keySet();
			
			int numinsideAinB = snomed.NotContainsFirstInSecond(id1, id2);
			int numinsideBinA = snomed.NotContainsFirstInSecond(id1, id2);
		    int intersection = snomed.Intersection(id1, id2);
				
			double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
			double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
			return distance_sanchez;	
		}
		
		public double SanchezDistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph) {
			return idConcept2;
			
		}
		
		
		public int getLeafNodes(ArrayList<Long> descendants) {
			int leafs=0;
			for(Long node:descendants) {
				if(snomed.getHyponymsIdConcepts(node).size()>0) {
					leafs++;
				}
			}
			return 0;
			
		}
		public double IC_measure(long idConcept) {
			ArrayList<Long> antecesorsnode =snomed.getAncestors(idConcept);
			ArrayList<Long> descendantsnode = snomed.getDescendants(idConcept);
			int leafs = getLeafNodes(descendantsnode);
			double icvalue = 0.0;
			double numer= ((double)leafs/(double)antecesorsnode.size())+1;
			long entity =  138875005;
			int denom = snomed.getDescendants(entity).size()+1;
			System.out.print("Entity 352567"+denom);
			double resultdiv = (double)numer/(double)denom;

			icvalue = -Math.log10(resultdiv);
		
			return icvalue;
			
		}
		
		public double IC_measureSubGraph(long idConcept, LinkedHashMap<Long, Integer> subgraph) {
			return idConcept;
		}
		
		public double resnisk_Distance(long idConcept1, long idConcept2) {
			long lcsConcept = snomed.getLCS(idConcept1, idConcept2);
			return IC_measure(lcsConcept);	
		}
		
		public double resnisk_DistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph ) {
			return idConcept2;
			
		}
		
		public double lin_Distance(long idConcept1, long idConcept2) {
			double resultlin = 0.0, num=0.0, div =0.0;
			num = 2* resnisk_Distance(idConcept1, idConcept2);
			div = IC_measure(idConcept1)+ IC_measure(idConcept2);
			resultlin= num/div;
			return resultlin;
		}
		
		public double lin_DistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph ) {
			return idConcept2;
			
		}
		
		public double jianConrath_Distance(long idConcept1, long idConcept2) {
			double resultjc = 0.0, num=0.0, div =0.0, res =0.0;
			div = IC_measure(idConcept1)+ IC_measure(idConcept2);
			res = 2*resnisk_Distance(idConcept1, idConcept2);
			resultjc= div-(2*resnisk_Distance(idConcept1, idConcept2));
			return resultjc;
		}
		
		public double jianConrath_Distance(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph ) {
			return idConcept2;
			
		}
}
