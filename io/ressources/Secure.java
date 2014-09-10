package ressources;

import io.IO;
import divers.Outil;

public final class Secure extends Enregistrable {
	private static final byte CLEF = 42;
	private static Secure instance;
	private final int[] ID;
	private int code;
	
	
	public static Secure getInstance() {
		synchronized(Secure.class) {
			if(instance == null)
				instance = new Secure();
			return instance;
		}
	}
	
	private Secure() {
		super("preferences/secure");
		ID = new int[6];
		IO io = lire();
		if(io.size() < 5) {
			code = Outil.r().nextInt();
			enregistrer();
		} else {
			io.nextShortString();
			ID[3] = io.next();
			ID[2] = io.nextInt();
			ID[5] = io.nextInt();
			if(io.next() != CLEF)
				throw new IllegalAccessError("Faille de securite : fichier secure corrompu");
			ID[0] = io.nextInt();
			code = io.nextInt();
			ID[4] = io.nextInt();
			ID[1] = io.nextInt();
			if(io.nextInt() != CLEF)
				throw new IllegalAccessError("Faille de securite : fichier secure corrompu");
		}
	}
	
	public int code(boolean estVrai) {
		if(estVrai)
			return code;
		int faux;
		do {
			faux = Outil.r().nextInt();
		} while(faux == code);
		return faux;
	}
	
	public int[] getID() {
		return ID;
	}
	
	public boolean estVrai(int code) {
		return this.code == code;
	}

	@Override
	public IO sauvegarder(IO io) {
		io.addShort("/!\\ Ne pas modifier /!\\  ");
		for(int i=0 ; i<ID.length ; i++)
			ID[i] = Outil.r().nextInt();
		io.add((byte) ID[3]).addInts(ID[2], ID[5]).add(CLEF).addInts(ID[0], code, ID[4], ID[1], CLEF);
		io.addShort("**Sans quoi le programme sera inutilisable.");
		return io;
	}
	
}
