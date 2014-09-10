package controles;

import java.awt.event.KeyEvent;

import vision.Orientation;

public enum TypeAction {
	GAUCHE(KeyEvent.VK_Q, true),
	DROITE(KeyEvent.VK_D, true),
	ESCALADER(KeyEvent.VK_Z),
	ACCROUPI(KeyEvent.VK_S, true),
	SAUT(KeyEvent.VK_SPACE),
	ROULADE(KeyEvent.VK_TAB),
	CHANGER_ARME(KeyEvent.VK_A),
	ATTAQUER(KeyEvent.VK_E);
	
	private final boolean aFin;
	private final int touche;
	
	
	private TypeAction(int touche, boolean aFin) {
		this.aFin = aFin;
		this.touche = touche;
	}
	
	private TypeAction(int touche) {
		this(touche, false);
	}


	public String getNom() {
		return toString().charAt(0) + toString().substring(1).toLowerCase().replace("_", " ");
	}

	public int getID() {
		return ordinal();
	}
	
	public int getToucheDefaut() throws IllegalArgumentException {
		return touche;
	}

	public String getDirection() {
		switch(this) {
		case GAUCHE: return "Ouest";
		case DROITE: return "Est";
		case ROULADE:
		case ACCROUPI: return "Sud";
		case SAUT:
		case ESCALADER: return "Nord";
		default: throw new IllegalArgumentException("Aucune correspondance de direction trouvee pour " + getNom());
		}
	}

	public Orientation getOrientation() {
		return this == DROITE ? Orientation.DROITE : Orientation.GAUCHE;
	}

	public boolean aFin() {
		return aFin;
	}
	
	public static String[] getNoms() {
		TypeAction[] actions = values();
		String[] noms = new String[actions.length];
		for(int i=0 ; i<noms.length ; i++)
			noms[i] = actions[i].getNom();
		return noms;
	}

	public static TypeAction get(int id) throws IllegalArgumentException {
		TypeAction t[] = values();
		if(id < 0 || id >= t.length)
			throw new IllegalArgumentException(id+" n'est pas un identifiant d'action valide");
		return t[id];
	}

}
