package temps;

public class EvenementRecursif extends Evenement {
	private final int delai;

	
	public EvenementRecursif(int delai, Evenementiel evt) {
		super(delai, evt);
		this.delai = delai;
	}

	@Override
	public void evenement(EvenementTempsPeriodique e, GestionnaireEvenements p) {
		super.evenement(e, p);
		setTemps(delai);
		p.addEvenement(this);
	}
	
}
