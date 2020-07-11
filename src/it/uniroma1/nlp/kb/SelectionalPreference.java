package it.uniroma1.nlp.kb;


public class SelectionalPreference
{
	private VerbAtlasPreferenceID Id;
	private String name;
	private BabelNetSynsetID bnId;
	
	public SelectionalPreference(String name, VerbAtlasPreferenceID Id, BabelNetSynsetID bnId)
	{
		this.name = name;  this.bnId = bnId; this.Id = Id;
	}
	
	public String getName() { return name; }
	public BabelNetSynsetID getBabelNetSynsetId() { return bnId; }
	
	@Override
	public String toString() { return name; }
	
	public VerbAtlasPreferenceID getId() { return Id; }
}
