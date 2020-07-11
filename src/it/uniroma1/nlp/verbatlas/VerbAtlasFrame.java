package it.uniroma1.nlp.verbatlas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import it.uniroma1.nlp.kb.*;

public class VerbAtlasFrame implements Frame, Iterable<BabelNetSynsetID>
{
	private String name;
	private VerbAtlasFrameID id;
	private ArrayList<Role> roles;
	HashSet<BabelNetSynsetID> bnIds = new HashSet<>();
	private SynsetFactory synsetFactory;
	
	
	VerbAtlasFrame(String name, VerbAtlasFrameID id, ArrayList<Role> roles, HashSet<BabelNetSynsetID> bnIds)
	{
		this.name = name; this.id = id; this.roles = roles; this.bnIds = bnIds;	this.synsetFactory = new SynsetFactory();
	}
	
	VerbAtlasFrame() 
	{
		this.name = ""; this.id = new VerbAtlasFrameID(""); this.roles = new ArrayList<>(); this.bnIds = new HashSet<>(); this.synsetFactory = new SynsetFactory();
	}
	
	public String name() { return name; }
	
	public static class Role
	{
		
		HashSet<BabelNetSynsetID> implicitArguments;
		HashSet<BabelNetSynsetID> shadowArguments;
		ArrayList<SelectionalPreference> sPrefs;
		private Type type;
		
		public enum Type
		{
			AGENT,ASSET,ATTRIBUTE,BENEFICIARY,CAUSE,CO_AGENT,CO_PATIENT,CO_THEME,DESTINATION,
			EXPERIENCER,EXTENT,GOAL,IDIOM,INSTRUMENT,LOCATION,MATERIAL,PATIENT,PRODUCT,PURPOSE,RECIPIENT,
			RESULT,SOURCE,STIMULUS,THEME,TIME,TOPIC,VALUE;
		}
		
		public Role(Type type, ArrayList<SelectionalPreference> sPrefs, HashSet<BabelNetSynsetID> implicitArguments, HashSet<BabelNetSynsetID> shadowArguments)
		{
			this.type = type; this.sPrefs = sPrefs; this.implicitArguments = implicitArguments; this.shadowArguments = shadowArguments;
		}
		
		public Role(Type type) { this.type = type; }
		
		public Type type() { return type; }
		
		public HashSet<BabelNetSynsetID> getImplicitArguments() {return implicitArguments;}
		
		public HashSet<BabelNetSynsetID> getShadowArguments(){return shadowArguments;}
		
		@Override
		public String toString() 
		{ 
			if (sPrefs != null) return type+": "+sPrefs; 
			return type+"";
		}
	}

	public VerbAtlasSynsetFrame toSynsetFrame(BabelNetSynsetID bId)
	{
		return synsetFactory.getSynset(bId, this);
	}
	
	public VerbAtlasSynsetFrame toSynsetFrame(WordNetSynsetID wId)
	{
		return toSynsetFrame(VerbAtlas.wn2bnMap.get(wId));
	}

	public ArrayList<Role> getRoles() { return roles; }

	public class Builder
	{
		private HashSet<BabelNetSynsetID> bnIds = new HashSet<>();
		private ArrayList<Role> roles = new ArrayList<>();
		
		public Builder addRole(Role r) {roles.add(r); return this;}
		public Builder addRole(Role... r) {roles.addAll(Arrays.asList(r)); return this;}
		
		public Builder addBabelNetSynsetID(BabelNetSynsetID bnId) {bnIds.add(bnId); return this;}
		public Builder addBabelNetSynsetID(BabelNetSynsetID... bnId) {bnIds.addAll(Arrays.asList(bnId)); return this;}
		
		public VerbAtlasFrame build() { return new VerbAtlasFrame(name, id, roles, bnIds);}
	}
	
	@Override
	public String getName() {return name;}

	@Override
	public VerbAtlasFrameID getId() {return id;}

	@Override
	public Iterator<BabelNetSynsetID> iterator() { return bnIds.iterator();	}
	
	@Override
	public String toString() { return name+": "+roles;}
}
