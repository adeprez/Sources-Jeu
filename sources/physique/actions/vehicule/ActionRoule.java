package physique.actions.vehicule;

import physique.Mobile;
import physique.actions.AbstractAction;
import physique.vehicule.Vehicule;
import physique.vehicule.VehiculeVolant;


public class ActionRoule extends ActionVehicule<Vehicule> {
    private int tentatives, vitesse;


    public ActionRoule(Vehicule source) {
	super(source);
    }

    public int getVitesse() {
	return getSource().getVitesse();
    }

    @Override
    public void commence() {
    }

    @Override
    public void seTermine() {
    }

    @Override
    public void tourAction() {
	Mobile m = getSource();
	vitesse = Math.min(getVitesse(), vitesse + 5);
	m.setVitesseInstantanee(vitesse);
	if(m.getDernierDeplacement() == 0) {
	    tentatives++;
	    vitesse /= 2;
	    if(tentatives >= 5)
		stopAction();
	} else tentatives = 0;
    }

    @Override
    public boolean peutArret() {
	return true;
    }

    @Override
    public boolean peutFaire(AbstractAction<?> courante) {
	return getSource().getTempsVol() < 5 || getSource() instanceof VehiculeVolant;
    }

}
