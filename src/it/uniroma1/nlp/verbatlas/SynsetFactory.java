package it.uniroma1.nlp.verbatlas;

import java.util.ArrayList;
import java.util.HashSet;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.SelectionalPreference;
import it.uniroma1.nlp.kb.VerbAtlasPreferenceID;
import it.uniroma1.nlp.verbatlas.VerbAtlasFrame.Role;
import it.uniroma1.nlp.verbatlas.internal.Tuple;

public class SynsetFactory 
{
	VerbAtlasSynsetFrame getSynset(BabelNetSynsetID bnId, VerbAtlasFrame f)
	{
		HashSet<BabelNetSynsetID> bnIds = new HashSet<>();
		bnIds.add(bnId);
		
		ArrayList<Role> roles = new ArrayList<>();
		
		
		
		ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>> shadows = VerbAtlas.bn2shadowMap.getOrDefault(bnId,  new ArrayList<>());
		ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>> implicit = VerbAtlas.bn2implicitMap.getOrDefault(bnId,  new ArrayList<>());
		
		for (Tuple<String, ArrayList<VerbAtlasPreferenceID>> sp : VerbAtlas.bn2spMap.get(bnId))
		{
			HashSet<BabelNetSynsetID> implicitArguments = new HashSet<>();
			HashSet<BabelNetSynsetID> shadowArguments = new HashSet<>();
			
			if (shadows.contains(sp)) shadowArguments = shadows.get(shadows.indexOf(sp)).getSecond();
			if (implicit.contains(sp)) implicitArguments = implicit.get(implicit.indexOf(sp)).getSecond();
			Role.Type t = Role.Type.valueOf(sp.getFirst().toUpperCase().replace("-","_"));
			ArrayList<SelectionalPreference> sPs = new ArrayList<>();
			for (VerbAtlasPreferenceID pId : sp.getSecond()) 
			{
				sPs.add(new SelectionalPreference(VerbAtlas.prefIDs.get(pId).getSecond(),  pId, 	VerbAtlas.prefIDs.get(pId).getFirst()));	
			}
			roles.add(new Role(t, sPs, implicitArguments, shadowArguments));
		}
		VerbAtlasSynsetFrame v= new VerbAtlasSynsetFrame(f.getName(), f.getId(), roles, bnIds);
		return v;
	}
}
