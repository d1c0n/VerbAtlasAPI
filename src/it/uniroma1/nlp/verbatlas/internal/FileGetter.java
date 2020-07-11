package it.uniroma1.nlp.verbatlas.internal;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileGetter 
{
	private static String root = "VerbAtlas-1.0.3";
	
	public static Path getFile(String fileName) throws URISyntaxException
	{
		String localPath = root+"/"+fileName;
		return Paths.get(ClassLoader.getSystemResource(localPath).toURI());
	}

}
