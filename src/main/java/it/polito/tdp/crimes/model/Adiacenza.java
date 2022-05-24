package it.polito.tdp.crimes.model;

import java.util.Objects;

public class Adiacenza implements Comparable<Adiacenza>{
	private String v1;
	private String v2;
	private int peso;
	
	public Adiacenza(String v1, String v2, int peso) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.peso = peso;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		return Objects.hash(peso, v1, v2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		return peso == other.peso && Objects.equals(v1, other.v1) && Objects.equals(v2, other.v2);
	}

	@Override
	public String toString() {
		return v1 + ", " + v2 + ", " + peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		// TODO Auto-generated method stub
		return this.toString().compareTo(o.toString());
	}
	
}
