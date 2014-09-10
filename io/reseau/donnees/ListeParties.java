package reseau.donnees;

import interfaces.Executable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import reseau.AbstractClient;
import reseau.client.RechercheServeur;
import reseau.listeners.DeconnexionListener;
import reseau.objets.InfoServeur;
import statique.Style;
import divers.Liste;
import divers.Outil;
import exceptions.AnnulationException;

public class ListeParties extends Liste<InfoServeur> implements Executable, Runnable {
	private static final long serialVersionUID = 1L;
	private final List<AdressePartie> adresses;
	private final JProgressBar avancement;


	public ListeParties(List<AdressePartie> adresses) {
		this.adresses = adresses;
		avancement = new JProgressBar();
		avancement.setStringPainted(true);
		avancement.setFont(Style.POLICE);
		avancement.setVisible(false);
	}

	public ListeParties() {
		this(ListeAdresseParties.getInstance());
	}

	public JProgressBar getAvancement() {
		return avancement;
	}

	@Override
	public void executer() {
		if(!avancement.isVisible())
			new Thread(this).start();
	}

	@Override
	public void run() {
		clear();
		List<RechercheServeur> recherches = new ArrayList<RechercheServeur>();
		avancement.setVisible(true);
		avancement.setMaximum(adresses.size());
		for(int i=0 ; i<adresses.size() ; i++) try {
			AdressePartie a = adresses.get(i);
			avancement.setValue(i + 1);
			avancement.setString(Outil.getPourcentage(i + 1, adresses.size()) + "% - Connexion au serveur " + a.getMemo()
					+ " (" + a.getNomAdresse() + ")");
			final RechercheServeur r = new RechercheServeur(a.getAdresse());
			r.addDeconnexionListener(new DeconnexionListener<AbstractClient>() {
				@Override
				public void deconnexion(AbstractClient client) {
					try {
						add(r.getInfoServeur());
					} catch(AnnulationException e) {}}});
			r.lancer();
		} catch(Exception e) {}
		Outil.wait(RechercheServeur.TEMPS_LIMITE_RECEPTION);
		for(final RechercheServeur r : recherches)
			r.fermer();
		avancement.setVisible(false);
	}
	
}
