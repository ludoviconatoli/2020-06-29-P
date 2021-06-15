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
	
	private List<Match> percorsoMigliore;
	private double pesoMassimo = 0.0;
	private List<Match> partite; 
	private double pesoTemporaneo;
	
	public List<Match> calcolaPercorsoMigliore(Match partenza, Match arrivo) {
		partite = new ArrayList<>();
		percorsoMigliore = new ArrayList<>();
		pesoTemporaneo = 0.0;
		
		partite.add(partenza);
		cerca(partite, pesoTemporaneo, arrivo);
		
		return percorsoMigliore;
	}
	
	public void cerca(List<Match> partite, double peso, Match arrivo) {
		
		if(partite.get(partite.size()-1).equals(arrivo)) {
			if(peso > pesoMassimo) {
				percorsoMigliore = new ArrayList<>(partite);
				pesoMassimo = peso;
			}
			
			return;
		}
		
		for(Match m: Graphs.neighborListOf(this.grafo, partite.get(partite.size()-1))) {
			
			DefaultWeightedEdge de = grafo.getEdge(partite.get(partite.size()-1), m);
			if(!partite.contains(m) && de != null && diverso(m, partite.get(partite.size()-1))) {
				
				partite.add(m);
				peso += this.grafo.getEdgeWeight(de);
				
				cerca(partite, peso, arrivo);
				
				partite.remove(partite.get(partite.size()-1));
				peso -= this.grafo.getEdgeWeight(de);
			}
		}
	}
	
	public boolean diverso(Match m1, Match m2) {
		if(!m1.getTeamHomeID().equals(m2.teamHomeID) &&
				!m1.teamAwayID.equals(m2.teamHomeID) &&
				!m1.getTeamHomeID().equals(m2.teamAwayID) &&
				!m1.teamAwayID.equals(m2.teamAwayID)) {
			
			return true;
		}
		
		return false;
	}
	
	public double getPesoMassimo() {
		return this.pesoMassimo;
	}
}
