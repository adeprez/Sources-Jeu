package xp;

import javax.swing.JPanel;

import layouts.LayoutCarreHorizontal;
import listeners.CliquePanelCompetenceListener;
import listeners.CliquePanelCompetencesListener;
import specialite.Specialite;
import specialite.competence.Competence;

public class PanelCompetences extends JPanel implements CliquePanelCompetenceListener {
	private static final long serialVersionUID = 1L;
	private final CliquePanelCompetencesListener l;
	private final PanelCompetence[] comp;
	private final Specialite spe;

	
	public PanelCompetences(CliquePanelCompetencesListener l, Specialite spe) {
		setLayout(new LayoutCarreHorizontal(25, 0));
		setOpaque(false);
		this.l = l;
		this.spe = spe;
		Competence[] tc = spe.getCompetences();
		comp = new PanelCompetence[tc.length];
		for(int i=0 ; i<tc.length ; i++) {
			PanelCompetence p = new PanelCompetence(this, tc[i]);
			comp[i] = p;
			add(p);
		}
	}
	
	public Specialite getSpecialite() {
		return spe;
	}
	
	public PanelCompetence[] getPanelsCompetences() {
		return comp;
	}
	
	public int indexOf(PanelCompetence p) {
		for(int i=0 ; i<comp.length ; i++)
			if(p == comp[i])
				return i;
		return -1;
	}
	
	public void setXP(int xp) {
		for(int i=0; i<comp.length ; i++)
			comp[i].setXPManquant(Specialite.getXPPalier(i + 1) - xp);
	}

	@Override
	public void clique(PanelCompetence p) {
		l.clique(this, p);
	}
	
}
