package it.uniroma1.nlp.verbatlas.internal;

public class Tuple<T, G>
{
	private T first;
	private G second;
	
	public Tuple(T first, G second) {this.first = first; this.second = second;}
	
	public T getFirst() {return first;}
	public G getSecond() {return second;}
	
	@Override
	public int hashCode() {return first.hashCode();}
	
	@Override
	@SuppressWarnings(value = { "unchecked" })
	public boolean equals(Object o) { return o.getClass()!=this.getClass()? false : ((Tuple<T, G>)o).hashCode() == this.hashCode() ? true : false; }
	
	@Override
	public String toString() { return "("+first+", "+second+")";}
}
