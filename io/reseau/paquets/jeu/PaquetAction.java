package reseau.paquets.jeu;

import io.IO;
import perso.Vivant;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import controles.TypeAction;

public class PaquetAction extends Paquet {

	
	public PaquetAction(int id, Vivant source, TypeAction action, boolean appuie) {
		super(TypePaquet.ACTION);
		addBytePositif(id);
		addBytePositif(action.getID());
		if(action.aFin())
			add(appuie);
		PaquetPosition.ecrire(this, source);
		addByte(source.getAngle());
		add(source.estDroite());
	}
	
	public static Paquet getPaquet(TypeAction action, Vivant source, boolean appuie) {
		Paquet p = new Paquet(TypePaquet.ACTION);
		addData(action, source, p, appuie);
		return p;
	}
	
	public static void addData(TypeAction action, Vivant source, IO io, boolean appuie) {
		io.addBytePositif(action.getID());
		if(action.aFin())
			io.add(appuie);
		io.addByte(source.getAngle());
		io.add(source.estDroite());
	}
	
}
