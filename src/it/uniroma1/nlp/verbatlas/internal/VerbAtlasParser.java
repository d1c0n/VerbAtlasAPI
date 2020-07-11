package it.uniroma1.nlp.verbatlas.internal;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.uniroma1.nlp.kb.*;

public class VerbAtlasParser 
{
		
	public static HashMap<VerbAtlasPreferenceID, Tuple<BabelNetSynsetID, String>> parsePrefIDs(String position)
	{
		try (Stream<String> s = Files.lines(FileGetter.getFile(position)))
		{
			return (HashMap<VerbAtlasPreferenceID, Tuple<BabelNetSynsetID, String>>)s.skip(1).parallel().
														   map(x -> x.split("\t")).
								   						   collect(Collectors.toMap(x -> new VerbAtlasPreferenceID(x[0]),
								   													x -> new Tuple<BabelNetSynsetID, String>(new BabelNetSynsetID(x[1]), x[2])));
		}
		catch(Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	
	public static HashMap<BabelNetSynsetID, ArrayList<Tuple<String, ArrayList<VerbAtlasPreferenceID>>>> parseBN2SP(String position)
	{
		HashMap<BabelNetSynsetID, ArrayList<Tuple<String, ArrayList<VerbAtlasPreferenceID>>>> bn2spMap = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(FileGetter.getFile(position)))
		{
			br.readLine();
			while (br.ready())
			{
				ArrayList<Tuple<String, ArrayList<VerbAtlasPreferenceID>>> preferences = new ArrayList<>();
				String[] line = br.readLine().split("\t");
				for (int i = 1; i < line.length; i+=2)
				{
					ArrayList<VerbAtlasPreferenceID> vapId = new ArrayList<>();
					for (String s : line[i+1].split("\\|"))
						vapId.add(new VerbAtlasPreferenceID(s));
					preferences.add(new Tuple<String, ArrayList<VerbAtlasPreferenceID>>(line[i], vapId));
				}
				bn2spMap.put(new BabelNetSynsetID(line[0]), preferences);
			}
			return bn2spMap;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	public static HashMap<String, ArrayList<String>> parseVA2SP(String position)
	{
		HashMap<String, ArrayList<String>> va2spMap = new HashMap<>();
		try (BufferedReader sp = Files.newBufferedReader(FileGetter.getFile(position)))
		{
			sp.readLine();
			while(sp.ready())
			{
				String[] t = sp.readLine().replace("\t", " ").split(" ");
				String key = t[0];
				ArrayList<String> values = new ArrayList<>();
				for (int i=1; i<t.length ; i+=2) values.add(t[i]+" "+t[i+1]);
				va2spMap.put(key, values);
			}
			return va2spMap;
		}
		catch(Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	public static HashMap<WordNetSynsetID, BabelNetSynsetID> parseWN2BN(String position)
	{
		try (Stream<String> s = Files.lines(FileGetter.getFile(position)))
		{
			return (HashMap<WordNetSynsetID, BabelNetSynsetID>)s.skip(1).parallel().map(x -> x.split("\t")).
																					collect(Collectors.toMap(x -> new WordNetSynsetID(x[1]),
																						 				     y -> new BabelNetSynsetID(y[0])));
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	public static HashMap<PropBankPredicateID, VerbAtlasFrameID> parsePB2VA(String position)
	{
		try (Stream<String> s = Files.lines(FileGetter.getFile(position)))
		{
			return (HashMap<PropBankPredicateID, VerbAtlasFrameID>)s.skip(1).parallel().map(x -> x.split("\t")[0].split(">")).
																						collect(Collectors.toMap(x -> new PropBankPredicateID(x[0]),
																						y -> new VerbAtlasFrameID(y[1])));
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}

	public static HashMap<String, ArrayList<WordNetSynsetID>> parseLemma2WN(String position)
	{
		HashMap<String, ArrayList<WordNetSynsetID>> lemma2wnMap = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(FileGetter.getFile(position)))
		{
			while (br.ready())
			{
				String[] info = br.readLine().split("\t");
				ArrayList<WordNetSynsetID> l = lemma2wnMap.getOrDefault(info[1], new ArrayList<>());
				l.add(new WordNetSynsetID(info[0]));
				lemma2wnMap.put(info[1], l);
			}
			return lemma2wnMap;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	public static HashMap<BabelNetSynsetID, ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>>> parseBN2Shadow(String position)
	{
		HashMap<BabelNetSynsetID, ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>>> bn2shadowMap = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(FileGetter.getFile(position)))
		{
			br.readLine();
			while (br.ready())
			{
				ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>> shadows = new ArrayList<>();
				String[] line = br.readLine().split("\t");
				for (int i = 1; i < line.length; i+=2)
				{
					HashSet<BabelNetSynsetID> bnIds = new HashSet<>();
					for (String s : line[i+1].split("\\|"))
						bnIds.add(new BabelNetSynsetID(s));
					shadows.add(new Tuple<String, HashSet<BabelNetSynsetID>>(line[i], bnIds));
				}
				bn2shadowMap.put(new BabelNetSynsetID(line[0]), shadows);
			}
			return bn2shadowMap;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	public static HashMap<BabelNetSynsetID, ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>>> parseBN2Implicit(String position)
	{
		HashMap<BabelNetSynsetID, ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>>> bn2implicitMap = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(FileGetter.getFile(position)))
		{
			br.readLine();
			while (br.ready())
			{
				ArrayList<Tuple<String, HashSet<BabelNetSynsetID>>> implicits = new ArrayList<>();
				String[] line = br.readLine().split("\t");
				for (int i = 1; i < line.length; i+=2)
				{
					HashSet<BabelNetSynsetID> bnIds = new HashSet<>();
					for (String s : line[i+1].split("\\|"))
						bnIds.add(new BabelNetSynsetID(s));
					implicits.add(new Tuple<String, HashSet<BabelNetSynsetID>>(line[i], bnIds));
				}
				bn2implicitMap.put(new BabelNetSynsetID(line[0]), implicits);
			}
			return bn2implicitMap;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
	
	public static HashMap<BabelNetSynsetID, VerbAtlasFrameID> parseBN2VA(String position)
	{
		try (Stream<String> s = Files.lines(FileGetter.getFile(position)))
		{
			return (HashMap<BabelNetSynsetID, VerbAtlasFrameID>)s.skip(1).parallel().map(x -> x.split("\t")).
																						collect(Collectors.toMap(x -> new BabelNetSynsetID(x[0]),
																						y -> new VerbAtlasFrameID(y[1])));
		}
		catch (Exception e)
		{
			throw new RuntimeException("Impossibile trovare percorso specificato");
		}
	}
}

