package animation;

import java.awt.BorderLayout;
import java.util.Map.Entry;

import listeners.ChangeDiapoListener;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.sequence.Animations;
import ressources.sprites.animation.sequence.ModeleSequence;
import ressources.sprites.animation.sequence.Sequence;

import composants.diaporama.DiaporamaModifiable;

import divers.Outil;
import exceptions.AnnulationException;

public class EcranAnimation extends DiaporamaModifiable<EcranSequence> implements Runnable, ChangeDiapoListener {
	private static final long serialVersionUID = 1L;
	private final Animation animation;
	private final ApercuAnim anim;
	private boolean run;


	public EcranAnimation(Animation animation) {
		this.animation = animation;
		anim = new ApercuAnim(animation.dupliquer(), true);
		setName("Animation");
		for(final Entry<String, ModeleSequence> e : Animations.getInstance().getSequences().entrySet())
			getDiapo().getModele().ajouter(new EcranSequence(animation, e.getKey(), new Sequence(e.getValue())));
		add(anim, BorderLayout.WEST);
		getDiapo().getModele().addChangeDiapoListener(this);
		lancer();
		changeDiapo(null);
	}

	public void ajout(String nom) {
		ModeleSequence s = new ModeleSequence(anim.getAnim().getMembres().length);
		s.addEtape();
		Animations.getInstance().ajout(nom, s);
		getDiapo().getModele().ajouter(new EcranSequence(animation, nom, new Sequence(s)));
	}
	
	@Override
	public void creer() {
		try {
			ajout(Outil.demander("Nom de l'animation ?"));
		} catch(AnnulationException e) {}
	}

	@Override
	public void changeDiapo(Object diapo) {
		if(getDiapo().getModele().aElement()) {
			EcranSequence e = getDiapo().getModele().getElement();
			e.change();
			anim.getAnim().setSequence(e.getSequence());
		}
	}

	@Override
	public void enregistrer() {
		EcranSequence e = getDiapo().getSelection();
		if(e != null)
			e.getSequence().getModele().enregistrer(e.getName());
		else Outil.erreur("Rien a enregistrer");
	}

	@Override
	public boolean lancer() {
		if(run)
			return false;
		new Thread(this).start();
		run = true;
		return true;
	}

	@Override
	public boolean fermer() {
		run = false;
		return true;
	}

	@Override
	public void run() {
		while(run) {
			Outil.wait(50);
			if(getDiapo().getModele().aElement()) {
				try {
					anim.getAnim().bouge();
				} catch(Exception e) {
					e.printStackTrace();
				}
				anim.repaint();
			}
		}
	}

}
