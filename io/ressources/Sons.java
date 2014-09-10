package ressources;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class Sons {
	private static Sons instance;
	private final Map<String, AudioClip> sons;
	
	public static final Sons getInstance() {
		synchronized(Sons.class) {
			if(instance == null)
				instance = new Sons();
			return instance;
		}
	}
	
	private Sons() {
		sons = new HashMap<String, AudioClip>();
	}
	
	public void boucle(String nom) {
		get(nom).loop();
	}
	
	public void stop(String nom) {
		get(nom).stop();
	}
	
	public void joue(String nom) {
		get(nom).play();
	}
	
	public AudioClip get(String nom) {
		if(!sons.containsKey(nom))
			try {
				sons.put(nom, charger(nom, "wav"));
			} catch(MalformedURLException e) {
				try {
					sons.put(nom, charger(nom, "WAV"));
				} catch (MalformedURLException err) {
					err.printStackTrace();
				}
			}
		return sons.get(nom);
	}
	
	private static AudioClip charger(String nom, String extension) throws MalformedURLException {
		return Applet.newAudioClip(RessourcesLoader.getFichier(("/sons/" + nom + "." + extension), false).toURI().toURL());
	}
	
}
