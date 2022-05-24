package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private double sommaPesi;
	private double pesoMedio;
	private List<String> best;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.sommaPesi = 0;
		this.pesoMedio = 0.0;
		for(Adiacenza a : this.dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
			// tengo traccia dei pesi per calcolarne la media
			this.sommaPesi += a.getPeso();
		}
		this.pesoMedio = this.sommaPesi/this.grafo.edgeSet().size();
		System.out.println("Grafo creato!");
		System.out.println("# vertici: " + this.grafo.vertexSet().size());
		System.out.println("# archi: " + this.grafo.edgeSet().size());
		System.out.println("peso medio: " + this.pesoMedio);
	}
	
	public List<Adiacenza> getArchiPesoMedio() {
		List<Adiacenza> result = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e) > this.pesoMedio) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e)));
			}
		}
		System.out.println("Archi peso medio: " + result.size());
		Collections.sort(result);
		return result;
	}
	
	public List<String> calcolaPercorso(String sorgente, String destinazione) {	// Adiacenza a
		this.best = new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		parziale.add(sorgente);
		this.cerca(parziale, destinazione);	// 1 perche' abbiamo gia' inserito il primo
											// vertice
		System.out.println("Cammino migliore: " + this.best.size());
		return this.best;
	}

	private void cerca(List<String> parziale, String destinazione) {
		// condizione di terminazione
		String ultimoInserito = parziale.get(parziale.size()-1);
		if(ultimoInserito.equals(destinazione)) {
			// l'ultimo elemento inserito e' la destinazione (soluz parziale = soluz totale)
			if(parziale.size() > this.best.size()) {
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		// passo ricorsivo: scorro i vicini dell'ultimo inserito e provo le varie strade
		for(String v : Graphs.neighborListOf(this.grafo, ultimoInserito)) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				this.cerca(parziale, destinazione);
				parziale.remove(ultimoInserito);
			}
		}
		
	} 
	
	public List<String> getCategorie() {
		List<String> categorie = this.dao.getCategorie();
		Collections.sort(categorie);
		return categorie;
	}
}
