package temps;


public class Evenement implements EvenementPeriodique {
	private final Evenementiel evt;
	private int delai;

	
	public Evenement(int delai, Evenementiel evt) {
		this.delai = delai;
		this.evt = evt;
	}

	@Override
	public int getTemps() {
		return delai;
	}

	@Override
	public void evenement(EvenementTempsPeriodique e, GestionnaireEvenements p) {
		evt.evenement(e, p);
	}

	@Override
	public void setTemps(int temps) {
		delai = temps;
	}

	@Override
	public int compareTo(EvenementTempsPeriodique e) {
		return new Integer(delai).compareTo(e.getTemps());
	}

}
