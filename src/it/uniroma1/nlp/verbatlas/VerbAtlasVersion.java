package it.uniroma1.nlp.verbatlas;

import java.time.LocalDate;

public class VerbAtlasVersion 
{
	public final String VERSION = "V1_0_3";
	private final int year = 2020;
	private final int month = 7;
	private final int dayOfMonth = 10;
	
	public LocalDate getReleaseDate() { return LocalDate.of(year, month, dayOfMonth);}
}
