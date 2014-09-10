package ressources;

import io.IO;
import statique.Style;
import divers.Taille;

public class Proprietes extends Enregistrable {
	private static Proprietes instance;
	private boolean debug, admin, dessin3D;
	private Taille taille;
	private String police;


	private Proprietes() {
		super("preferences/proprietes");
		IO io = lire();
		if(io.size() > 0) {
			admin = Secure.getInstance().estVrai(io.nextInt());
			dessin3D = io.nextBoolean();
			debug = io.nextBoolean();
			police = io.nextShortString();
			taille = new Taille(io.nextShortInt(), io.nextShortInt());
		} else {
			dessin3D = true;
			police = "Nyala";
			taille = new Taille(64);
			enregistrer();
		}
	}

	public static Proprietes getInstance() {
		synchronized(Proprietes.class) {
			if(instance == null)
				instance = new Proprietes();
			return instance;
		}
	}

	public void set3D(boolean dessin3D) {
		this.dessin3D = dessin3D;
		enregistrer();
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		enregistrer();
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
		enregistrer();
	}

	public void setPolice(String police) {
		this.police = police;
		Style.setPolice();
		enregistrer();
	}
	
	public void setTaille(Taille taille) {
		this.taille = taille;
		enregistrer();
	}
	
	public Taille getTaille() {
		return taille;
	}

	public boolean est3D() {
		return dessin3D;
	}

	public boolean estDebug() {
		return debug;
	}

	public boolean estAdmin() {
		return admin;
	}

	public String getPolice() {
		return police;
	}

	@Override
	public IO sauvegarder(IO io) {
		io.add(Secure.getInstance().code(admin)).add(dessin3D).add(debug).addShort(police);
		return io.addShort(taille.getLargeur()).addShort(taille.getHauteur());
	}

}
