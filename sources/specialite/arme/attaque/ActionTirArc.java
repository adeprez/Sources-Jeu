package specialite.arme.attaque;

import perso.AbstractPerso;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.Sequence;
import specialite.arme.Arc;
import specialite.arme.Arme;
import specialite.arme.projectile.Fleche;
import specialite.arme.projectile.Fleche.MembreFleche;

public class ActionTirArc extends ActionTir {
    private final Fleche fleche;
    private MembreFleche membre;
    private boolean clean;


    public ActionTirArc(AbstractPerso source) {
	super(source, 20, 21);
	fleche = new Fleche(source, this);
    }

    public void setEtatArc(int etat) {
	Arme a = getSource().getSpecialite().getArme();
	if(a instanceof Arc)
	    ((Arc) a).setEtat(etat);
    }

    @Override
    public void tirer() {
	super.tirer();
	seTermine();
    }

    @Override
    public Sequence getSequence() {
	Sequence s = Animations.getInstance().getSequence("tir arc", false);
	s.setSequenceur(this);
	return s;
    }

    @Override
    public void tourAction() {
	super.tourAction();
	if(getTour() == 13)
	    setEtatArc(1);
	else if(getTour() == 16)
	    setEtatArc(2);
    }

    @Override
    public void commence() {
	super.commence();
	AbstractPerso p = getSource();
	p.getAnimation().setVitesse(133);
	membre = fleche.addTo(p.getAnimation().getBras(false).getMain(), 0, 90, (float) (p.getAngleRad() + Math.PI/2));
	clean = false;
    }

    @Override
    public Fleche getProjectile() {
	return fleche;
    }

    @Override
    public void seTermine() {
	if(!clean) {
	    clean = true;
	    membre.fermer();
	    setEtatArc(0);
	}
    }

}
