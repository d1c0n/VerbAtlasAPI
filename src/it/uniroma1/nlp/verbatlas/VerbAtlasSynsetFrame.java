package it.uniroma1.nlp.verbatlas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;

public class VerbAtlasSynsetFrame extends VerbAtlasFrame
{	
	
	VerbAtlasSynsetFrame(String name,VerbAtlasFrameID id,ArrayList<Role> roles, HashSet<BabelNetSynsetID> bnIds)
	{
		super(name, id, roles, bnIds);
	}

	public VerbAtlasFrame toFrame() { return VerbAtlas.getInstance().getFrame(getId());}
	
	@Override
	public Iterator<BabelNetSynsetID> iterator() { return null;	}
}
