package it.uniroma1.nlp.kb;

import java.util.List;
import it.uniroma1.nlp.verbatlas.VerbAtlasFrame.Role;

public interface Frame
{
	
	public String getName();
	
	public VerbAtlasFrameID getId();
	
	public List<Role> getRoles();
	
}
