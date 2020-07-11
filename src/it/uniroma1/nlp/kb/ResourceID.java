package it.uniroma1.nlp.kb;

public abstract class ResourceID
{
	private String id;
	
	public ResourceID(String id) { this.id = id; }
	
	public String getId() { return id; }
	
	@Override
	public int hashCode() { return id.hashCode(); }
	
	@Override
	public String toString() { return id.toString(); }
	
	@Override
	public boolean equals(Object o) { return o.getClass() != this.getClass() ? false : ((ResourceID)o).getId().equals(this.id); }
	
}
