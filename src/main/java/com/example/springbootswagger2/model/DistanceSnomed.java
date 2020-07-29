package com.example.springbootswagger2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import net.didion.jwnl.data.Synset;

public class DistanceSnomed {
	 
	SnomedCTLibrary snomed = new SnomedCTLibrary();
	 
	 
		public double wuSimilarity(long idConcept1, long idConcept2) {
			
			HashMap<Long, Integer> id1 = snomed.getAncestrosDistanciaMinima(idConcept1);
			HashMap<Long, Integer> id2 = snomed.getAncestrosDistanciaMinima(idConcept2);
			
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
			int distancia = snomed.getAncestorsMaxDistance(idLCS);
			
			return ((double)2*distancia/(double)(distanciaLCS+2*distancia));	
		
		}
		
		public double wuSimilaritySubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph) {
			HashMap<Long, Integer> id1 = snomed.getAncestrosDistanciaMinimaSubGraph(idConcept1, subgraph);
			HashMap<Long, Integer> id2 = snomed.getAncestrosDistanciaMinimaSubGraph(idConcept2, subgraph);
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
			if(subgraph.containsKey(idLCS)) {
				int distancia = snomed.getAncestorsMaxDistanceSubGraph(idLCS, subgraph);
				return ((double)2*distancia/(double)(distanciaLCS+2*distancia));
			}
			else return 0;
		}
		
		
		public double SanchezDistance(long idConcept1, long idConcept2) {
			Set<Long> id1 = snomed.getAncestrosDistanciaMinima(idConcept1).keySet();
			Set<Long> id2 = snomed.getAncestrosDistanciaMinima(idConcept2).keySet();
			
			int numinsideAinB = snomed.NotContainsFirstInSecond(id1, id2);
			int numinsideBinA = snomed.NotContainsFirstInSecond(id1, id2);
		    int intersection = snomed.Intersection(id1, id2);
				
			double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
			double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
			return distance_sanchez;	
		}
		
		public double SanchezDistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph) {
			Set<Long> id1 = snomed.getAncestrosDistanciaMinimaSubGraph(idConcept1, subgraph).keySet();
			System.out.print("\nID1"+id1.size());
			Set<Long> id2 = snomed.getAncestrosDistanciaMinimaSubGraph(idConcept2, subgraph).keySet();
			System.out.print("\nID1"+id1.size()+"ID2"+id2.size());
			
			int numinsideAinB = snomed.NotContainsFirstInSecond(id1, id2);
			int numinsideBinA = snomed.NotContainsFirstInSecond(id1, id2);
		    int intersection = snomed.Intersection(id1, id2);
				
			double num_frac =(double)(numinsideAinB+numinsideBinA)/(double)(numinsideAinB+numinsideBinA+intersection);
			double distance_sanchez = Math.log10(1+num_frac)/Math.log10(2);
			return distance_sanchez;
		}
		
		
		public int getLeafNodes(ArrayList<Long> descendants) {
			int leafs=0;
			for(Long node:descendants) {
				if(snomed.getHyponymsIdConcepts(node).size()==0) {
					leafs++;
				}
			}
			return leafs;
			
		}
		
		public int getLeafNodesSubGraph(ArrayList<Long> descendants, LinkedHashMap<Long, Integer> subgraph) {
			int leafs=0;
			for(Long node:descendants) {
				if(snomed.getHyponymsIdConcepts(node).size()==0) {
					leafs++;
				}
				if(snomed.getHyponymsIdConcepts(node).size()>0) {
					long idchild = snomed.getHyponymsIdConcepts(node).get(0);
					if(!subgraph.containsKey(idchild)) {
						leafs++;
					}
				}
			}
			return leafs;
			
		}
		
	
		public double IC_measure(long idConcept) {
			ArrayList<Long> ancestorsnode =snomed.getAncestors(idConcept);
			System.out.print("Antecesor->"+ ancestorsnode.size());
			ArrayList<Long> descendantsnode = snomed.getDescendants(idConcept);
			int leafs = getLeafNodes(descendantsnode);
			System.out.print("LEAFS"+leafs+"\n");
			double icvalue = 0.0;
			double numer= ((double)leafs/(double)ancestorsnode.size())+1;
			long entity =  138875005;
			int denom = snomed.getDescendants(entity).size()+1;
			System.out.print("Entity 352567->"+denom+"\n");
			double resultdiv = (double)numer/(double)denom;

			icvalue = -Math.log10(resultdiv);
		
			return icvalue;
			
		}
		
		public double IC_measureSubGraph(long idConcept, LinkedHashMap<Long, Integer> subGraph) {
			ArrayList<Long> ancestorsnode =snomed.getAncestorsSubGraph(idConcept, subGraph);
		//	System.out.print("Antecesor"+ancestorsnode.size());
			ArrayList<Long> descendantsnode = snomed.getDescendantsSubGraph(idConcept, subGraph);
			int leafs = getLeafNodesSubGraph(descendantsnode, subGraph);
			System.out.print("LEAFS"+leafs+"\n");
			double icvalue = 0.0;
			
			double numer= ((double)leafs/(double)ancestorsnode.size())+1;
			int denom = subGraph.size()+1;
			System.out.print("RES->"+numer+"/"+denom+"\n");
			double resultdiv = (double)numer/(double)denom;

			icvalue = -Math.log10(resultdiv);
		
			return icvalue;
		}
		
		public double resnisk_Distance(long idConcept1, long idConcept2) {
			long lcsConcept = snomed.getLCS(idConcept1, idConcept2);
			return IC_measure(lcsConcept);	
		}
		
		public double resnisk_DistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph ) {
			long lcsConcept = snomed.getLCS(idConcept1, idConcept2);
			System.out.print("\nLCS"+lcsConcept);
			if(subgraph.containsKey(lcsConcept))
				return IC_measureSubGraph(lcsConcept, subgraph);	
			return 0;
			
		}
		
		public double lin_Distance(long idConcept1, long idConcept2) {
			double resultlin = 0.0, num=0.0, div =0.0;
			num = 2* resnisk_Distance(idConcept1, idConcept2);
			div = IC_measure(idConcept1)+ IC_measure(idConcept2);
			resultlin= num/div;
			return resultlin;
		}
		
		public double lin_DistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph ) {
			double resultlin = 0.0, num=0.0, div =0.0;
			num = 2* resnisk_DistanceSubGraph(idConcept1, idConcept2, subgraph);
			div = IC_measureSubGraph(idConcept1, subgraph)+ IC_measureSubGraph(idConcept2, subgraph);
			resultlin= num/div;
			return resultlin;
			
		}
		
		public double jianConrath_Distance(long idConcept1, long idConcept2) {
			double resultjc = 0.0, num=0.0, div =0.0, res =0.0;
			div = IC_measure(idConcept1)+ IC_measure(idConcept2);
			res = 2*resnisk_Distance(idConcept1, idConcept2);
			resultjc= div-(2*resnisk_Distance(idConcept1, idConcept2));
			return resultjc;
		}
		
		public double jianConrath_DistanceSubGraph(long idConcept1, long idConcept2, LinkedHashMap<Long, Integer> subgraph ) {
			double resultjc = 0.0, num=0.0, div =0.0, res =0.0;
			div = IC_measureSubGraph(idConcept1, subgraph)+ IC_measureSubGraph(idConcept2, subgraph);
			System.out.print("DIV"+div+"\n");
			res = 2*resnisk_DistanceSubGraph(idConcept1, idConcept2, subgraph);
			System.out.print("RES"+res+"\n");
			resultjc= div-(2*resnisk_DistanceSubGraph(idConcept1, idConcept2, subgraph));
			return resultjc;
			
		}
		
		public static void main(String[] args) throws Exception{
			DistanceSnomed distancesct = new DistanceSnomed();
			long id = 123037004, id2 = 22298006, id3=80891009, clinical_finding = 404684003, id4 = 57809008, id5 =123397009, id6=5626501, id7=56265001, id8=25105200, id9=417163006, id10=123946008, id11=301095005, id12 =301857004;
			long id4nivel =92993003;
			
			System.out.print("DISTANCE"+distancesct.wuSimilarity(id2, id4));
		}
}
