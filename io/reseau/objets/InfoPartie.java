package reseau.objets;

import interfaces.Sauvegardable;
import io.IO;
import partie.modeJeu.TypeJeu;

public class InfoPartie implements Sauvegardable {
	private TypeJeu type;
	private int temps;

	
	public InfoPartie(TypeJeu type, int temps) {
		this.type = type;
		this.temps = temps;
	}
	
	public InfoPartie(IO io) {
		type = TypeJeu.get(io.nextPositif());
		temps = io.nextShortInt();
	}
	
	public int getTemps() {
		return temps;
	}

	public void setTemps(int temps) {
		this.temps = temps;
	}

	public TypeJeu getTypeJeu() {
		return type;
	}
	
	public void setTypeJeu(TypeJeu type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type.getNom() + " de " + temps + " secondes";
	}
	
	@Override
	public IO sauvegarder(IO io) {
		return io.addBytePositif(type.getID()).addShort(temps);
	}

}
