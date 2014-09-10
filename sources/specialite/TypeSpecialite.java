package specialite;

import perso.AbstractPerso;
import specialite.competence.Competence;
import specialite.competence.CompetenceCharge;
import specialite.competence.CompetenceCoupEpee;
import specialite.competence.CompetenceTirArc;

public enum TypeSpecialite {
	GUERRIER, ARCHER, INGENIEUR;

	
	public Competence[] getCompetences(AbstractPerso source) {
		switch(this) {
		case GUERRIER: return new Competence[] {new CompetenceCoupEpee(source), new CompetenceCharge(source)};
		case ARCHER: return new Competence[] {new CompetenceTirArc(source)};
		case INGENIEUR: return new Competence[] {};
		default: throw new IllegalAccessError("Non implemente");
		}
	}
}
