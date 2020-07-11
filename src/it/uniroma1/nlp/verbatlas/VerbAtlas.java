package it.uniroma1.nlp.verbatlas;


import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.uniroma1.nlp.kb.*;
import it.uniroma1.nlp.verbatlas.VerbAtlasFrame.Role;
import it.uniroma1.nlp.verbatlas.internal.*;

public class VerbAtlas implements Iterable<VerbAtlasFrame>
{
	
	private static VerbAtlas instance;
	private ArrayList<VerbAtlasFrame> frameList = new ArrayList<VerbAtlasFrame>();
	
	private static final String frameIds = "VA_frame_ids.tsv";
	//private static final String va2sp = "VA_va2sp.tsv";
	private static final String spIds = "VA_preference_ids.tsv";
	private static final String bn2va = "VA_bn2va.tsv";
	private static final String va2pas = "VA_va2pas.tsv";
	private static final String bn2wn = "bn2wn.tsv";
	private static final String wn2lemma = "wn2lemma.tsv";
	private static final String pb2va = "pb2va.tsv";
	private static final String bn2sp = "VA_bn2sp.tsv";
	private static final String bn2shadow = "VA_bn2shadow.tsv";
	private static final String bn2implicit = "VA_bn2implicit.tsv";
	
	static HashMap<VerbAtlasPreferenceID, Tuple<BabelNetSynsetID, String>> prefIDs;
	static HashMap<BabelNetSynsetID, ArrayList<Tuple<String, ArrayList<VerbAtlasPreferenceID>>>> bn2spMap;
	//private HashMap<String, ArrayList<String>> va2spMap;
	static HashMap<WordNetSynsetID, BabelNetSynsetID> wn2bnMap;
	private HashMap<PropBankPredicateID, VerbAtlasFrameID> pb2vaMap;
	private HashMap<String, ArrayList<WordNetSynsetID>> lemma2wnMap;
	static HashMap<BabelNetSynsetID, ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>>> bn2shadowMap;
	static HashMap<BabelNetSynsetID, ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>>> bn2implicitMap;
	private static HashMap<BabelNetSynsetID, VerbAtlasFrameID> bn2vaMap;
	
	public static VerbAtlas getInstance()
	{
		if (instance == null) instance = new VerbAtlas();
		return instance;
	}
	
	private VerbAtlas()
	{
		prefIDs = VerbAtlasParser.parsePrefIDs(spIds);
		bn2spMap = VerbAtlasParser.parseBN2SP(bn2sp);
		wn2bnMap = VerbAtlasParser.parseWN2BN(bn2wn);
		//va2spMap = VerbAtlasParser.parseVA2SP(va2sp);
		pb2vaMap = VerbAtlasParser.parsePB2VA(pb2va);
		lemma2wnMap = VerbAtlasParser.parseLemma2WN(wn2lemma);
		bn2shadowMap = VerbAtlasParser.parseBN2Shadow(bn2shadow);
		bn2implicitMap = VerbAtlasParser.parseBN2Implicit(bn2implicit);
		bn2vaMap = VerbAtlasParser.parseBN2VA(bn2va);
		
		try (BufferedReader frames = Files.newBufferedReader(FileGetter.getFile(frameIds));
			 BufferedReader roles = Files.newBufferedReader(FileGetter.getFile(va2pas));
			 BufferedReader bn = Files.newBufferedReader(FileGetter.getFile(bn2va)))
		{
			frames.readLine();
			roles.readLine();
			bn.readLine();
			while(frames.ready() && roles.ready())
			{
				String[] t = frames.readLine().split("\t");
				List<String> rAssign = Stream.of(roles.readLine().replace("\t", " ").split(" ")).collect(Collectors.toList());
				rAssign.remove(0);
				ArrayList<VerbAtlasFrame.Role> frameRoles = new ArrayList<>();
				rAssign.forEach(x -> frameRoles.add(new Role(Role.Type.valueOf(x.toUpperCase().replace("-", "_")))));
				String[] bnId = bn.readLine().split("\t");
				HashSet<BabelNetSynsetID> bnSet = new HashSet<>();
				while (bn.ready() && bnId[1].equals(t[0]))	
				{
					bnSet.add(new BabelNetSynsetID(bnId[0]));
					bnId = bn.readLine().split("\t");
				}
				frameList.add(new VerbAtlasFrame(t[1], new VerbAtlasFrameID(t[0]), frameRoles, bnSet));
			}
		}
		catch(Exception e)
		{
			
		}
	} 

	
	public VerbAtlasFrame getFrame(String nomeFrame) 
	{ 
		String Frame = nomeFrame.replace("-", "_");
		return frameList.stream().filter(f->f.name().equals(Frame)).
								  findFirst().
								  orElseThrow(() -> new RuntimeException("Nessun frame con quel nome"));
	}
	
	public VerbAtlasFrame getFrame(ResourceID id)
	{
		return switch(id.getId().substring(0, 2))
				{
					case "va" -> getFrame((VerbAtlasFrameID)id);
					case "bn" -> getFrame((BabelNetSynsetID)id);
					case "wn" -> getFrame((WordNetSynsetID)id);
					default -> getFrame((PropBankPredicateID)id);
				};
	}
	
	private VerbAtlasFrame getFrame(VerbAtlasFrameID id)
	{
		return frameList.stream().filter(f -> f.getId().equals(id)).findAny().orElseThrow(() -> new RuntimeException("Nessun frame trovato con quell'ID"));
	}

	private VerbAtlasFrame getFrame(BabelNetSynsetID id)
	{
		return getFrame(bn2vaMap.getOrDefault(id, null)).toSynsetFrame(id);
	}
	
	private VerbAtlasFrame getFrame(WordNetSynsetID id)
	{
		return getFrame(wn2bnMap.get(id));
	}
	
	private VerbAtlasFrame getFrame(PropBankPredicateID id)
	{
		return getFrame(pb2vaMap.get(id));
	}
		
	public Set<VerbAtlasFrame> getFramesByVerb(String verb)
	{
		HashSet<VerbAtlasFrame> wnSynsetSet = new HashSet<>();
		for (WordNetSynsetID wnId : lemma2wnMap.get(verb)) wnSynsetSet.add(getFrame(wnId));
		return wnSynsetSet;
	}
	
	public VerbAtlasVersion getVersion() { return new VerbAtlasVersion(); }

	@Override
	public Iterator<VerbAtlasFrame> iterator() { return frameList.iterator(); }
}
