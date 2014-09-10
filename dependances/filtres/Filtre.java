package filtres;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class Filtre extends FileFilter {
	private final String[] extensions;
	private final String description;


	public Filtre(String description, String... extensions) {
		this.description = description;
		this.extensions = extensions;
	}

	@Override 
	public String getDescription() {
		return description;
	}

	@Override 
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		for(final String ext : extensions)
			if(f.getName().toLowerCase().endsWith(ext))
				return true;
		return false;
	}

}
