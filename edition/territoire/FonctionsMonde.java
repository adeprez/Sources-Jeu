package territoire;

import interfaces.Actualisable;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;

import layouts.LayoutLignes;
import ressources.Proprietes;
import ressources.compte.Compte;

import composants.panel.PanelImage;
import composants.styles.Bouton;
import composants.styles.fenetres.FenetrePopup;

import divers.Outil;

public class FonctionsMonde extends PanelImage implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton migrer, infos, retour, quitter;
	private final Actualisable l;
	private final Compte compte;


	public FonctionsMonde(Compte compte, Actualisable l) {
		super("fond/rouleau.png");
		setPreferredSize(new Dimension(150, 190));
		setLayout(new LayoutLignes(false));
		this.compte = compte;
		migrer = new Bouton("Migrer").large();
		infos = new Bouton("Infos").large();
		retour = new Bouton("Retour").large();
		quitter = new Bouton("Quitter").large();
		migrer.addActionListener(this);
		infos.addActionListener(this);
		add(Box.createRigidArea(new Dimension(1, 20)));
		add(new OrGraphique(compte.getRessources(), Proprietes.getInstance().estAdmin()));
		add(infos);
		add(migrer);
		add(retour);
		add(quitter);
		Dimension d = new Dimension(138, 20);
		infos.setPreferredSize(d);
		migrer.setPreferredSize(d);
		retour.setPreferredSize(d);
		quitter.setPreferredSize(d);
		this.l = l;
	}

	private void migrer() {
		compte.migrer();
		l.actualise();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == migrer) {
			if(Outil.confirmer("Migrer vers un autre territoire ?\nCette action est irreversible." +
					"\nTout ce que vous avez bati\nsera perdu."))
				migrer();
		} else if(e.getSource() == infos)
			new FenetrePopup("Informations").setContenu(new PanelInformationsMonde(compte, true)).afficher(new Dimension(400, 160));
	}

}
