package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	Graph<Match, DefaultWeightedEdge> grafo ;
	PremierLeagueDAO dao;
	Map<Integer, Match> idMap;
	
	public Model(){
		this.dao = new PremierLeagueDAO();
		
	}
	
	public void creaGrafo(int mese, int min) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		idMap = new HashMap<>();
		dao.listAllMatchesMese(idMap, mese);
		for(Match m: idMap.values()) {
			this.grafo.addVertex(m);
		}
		
		List<Adiacenza> archi = dao.getArchi(idMap, min);
		if(archi != null) {
			for(Adiacenza a: archi) {
				Graphs.addEdge(grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getMassimi(){
		if(grafo != null) {
			Adiacenza massimo = new Adiacenza(null, null, 0);
			
			for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
				if(this.grafo.getEdgeWeight(e) > massimo.getPeso()) {
					massimo = new Adiacenza(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), grafo.getEdgeWeight(e));
				}
			}
			
			List<Adiacenza> result = new ArrayList<>();
			for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
				if(massimo.getPeso() == this.grafo.getEdgeWeight(e)) {
					
					Adiacenza a = new Adiacenza(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), grafo.getEdgeWeight(e));
					result.add(a);
				}
			}
			
			return result;
		}
		
		return null;
	}
	
	public Set<Match> getVertici() {
		if(this.grafo != null) {
			return grafo.vertexSet();
		}
		
		return null;
	}
}
