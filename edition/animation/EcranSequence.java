package animation;

import io.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import listeners.ChangeEtapeListener;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.Membre;
import ressources.sprites.animation.sequence.Sequence;
import statique.Style;
import base.Ecran;

import composants.styles.Bouton;
import composants.styles.ScrollPaneTransparent;

import divers.Outil;
import exceptions.AnnulationException;

public class EcranSequence extends Ecran implements ChangeEtapeListener, MouseWheelListener, MouseListener, 
ChangeListener, MouseMotionListener, ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	private final AbstractButton suppr, add;
	private final JSpinner dX, dY, angle;
	private final JList<Membre> membres;
	private final BarreNumEtape barre;
	private final Sequence sequence;
	private final JCheckBox inverse;
	private final Animation anim;
	private int taille, etape;
	private Membre selection;
	private Point p;


	public EcranSequence(Animation anim, String nom, Sequence sequence) {
		this.anim = anim;
		taille = 75;
		setName(nom);
		setLayout(new BorderLayout());
		this.sequence = sequence;

		barre = new BarreNumEtape(0, this, sequence);
		dX = new JSpinner(new SpinnerNumberModel(0, IO.LIMITE_BYTE_MIN * 2, IO.LIMITE_BYTE_MAX * 2, 5));
		dY = new JSpinner(new SpinnerNumberModel(0, IO.LIMITE_BYTE_MIN * 2, IO.LIMITE_BYTE_MAX * 2, 5));
		angle = new JSpinner(new SpinnerNumberModel(0, -5, 365, 5));
		inverse = new JCheckBox("Inverse");
		suppr = new Bouton("Supprimer").large();
		add = new Bouton("Ajouter").large();
		membres = new JList<Membre>(anim.getMembres());
		membres.setFont(Style.POLICE.deriveFont(15f));

		JPanel haut = new JPanel();
		haut.setBorder(BorderFactory.createRaisedBevelBorder());
		haut.add(inverse);
		haut.add(Outil.getTexte("|  Decalage x", false));
		haut.add(dX);
		haut.add(Outil.getTexte("|  Decalage y", false));
		haut.add(dY);

		angle.setToolTipText("Angle");
		JPanel droite = new JPanel(new BorderLayout());
		JPanel db = new JPanel(new GridLayout(2, 1));
		db.setOpaque(false);
		droite.setOpaque(false);
		db.add(add);
		db.add(suppr);
		droite.add(db, BorderLayout.SOUTH);
		droite.add(angle, BorderLayout.NORTH);
		droite.add(new ScrollPaneTransparent(membres), BorderLayout.CENTER);

		add(droite, BorderLayout.EAST);
		add(haut, BorderLayout.NORTH);
		add(barre, BorderLayout.WEST);

		dX.addChangeListener(this);
		dY.addChangeListener(this);
		angle.addChangeListener(this);
		inverse.addActionListener(this);
		membres.addListSelectionListener(this);
		suppr.addActionListener(this);
		add.addActionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		getFenetre().addKeyListener(barre);
		change();
	}

	public Sequence getSequence() {
		return sequence;
	}

	public Rectangle getRect() {
		return new Rectangle((getWidth() - taille)/2, (int) ((getHeight() - taille)/1.1), taille, taille);
	}

	public void dessineGrille(Graphics g) {
		g.setColor(Color.GRAY);
		for(int x=0 ; x<getWidth() ; x += 20)
			g.drawLine(x, 0, x, getHeight());
		for(int y=0 ; y<getHeight() ; y += 20)
			g.drawLine(0, y, getWidth(), y);
	}

	public Membre getMembre(Point p, boolean up) {
		Rectangle zone = getRect();
		Membre[] membres = anim.getMembres();
		for(int i = up ? 0 : membres.length - 1 ; up ? i < membres.length : i >= 0 ; i += up ? 1 : -1)
			if(membres[i].contient(zone, true, p))
				return membres[i];
		return null;
	}

	public void setSelection(Membre membre) {
		selection = membre;
		if(selection == null)
			membres.clearSelection();
		else membres.setSelectedValue(selection, true);
		selectionChange();
	}

	public void incrAngle(int val) {
		try {
			sequence.getModele().getEtape(etape).incrAngle(anim.getRang(selection), val);
			change();
		} catch(AnnulationException e) {
			Outil.erreur(e.getMessage());
		}
	}

	public void setAngle(int a) {
		try {
			sequence.getModele().getEtape(etape).setAngle(anim.getRang(selection), a);
			change();
		} catch(AnnulationException e) {
			Outil.erreur(e.getMessage());
		}
	}

	public void selectionChange() {
		angleChange();
		repaint();
	}

	public void angleChange() {
		if(selection != null) try {
			if(!angle.isEnabled())
				angle.setEnabled(true);
			angle.setValue((int) Math.toDegrees(sequence.getModele().getEtape(etape).getAngles()[anim.getRang(selection)]));
		} catch(AnnulationException e) {
			Outil.erreur(e.getMessage());
		}
		else angle.setEnabled(false);
	}

	public void change() {
		sequence.getModele().getEtape(etape).effet(sequence, anim);
		angleChange();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		dessineGrille(g);
		anim.dessiner((Graphics2D) g, getRect(), true);
		super.paintComponent(g);
		String s;
		g.setColor(Color.BLACK);
		if(selection != null) {
			Rectangle r = selection.getRect(getRect(), true);
			g.drawRect(r.x, r.y, r.width, r.height);
			String angle;
			try {
				angle = sequence.getModele().getEtape(etape).getAngles()[anim.getRang(selection)] + "";
			} catch(AnnulationException e) {
				angle = "erreur";
			}
			s = selection.toString() + " (angle=" + angle + ")";
		} else s = "Shift pour selectionner derriere";
		g.drawString(s, 60, getHeight() - 5);
	}

	@Override
	public void changeEtape(int etape) {
		this.etape = etape;
		dX.setValue(sequence.getModele().getEtape(etape).getdX());
		dY.setValue(sequence.getModele().getEtape(etape).getdY());
		inverse.setSelected(sequence.getModele().getEtape(etape).inverseSens());
		change();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(selection == null) {
			taille = Math.max(20, taille + e.getUnitsToScroll());
			repaint();
		} else incrAngle(e.getUnitsToScroll());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == angle)
			setAngle((int) angle.getValue());
		else {
			if(e.getSource() == dX)
				sequence.getModele().getEtape(etape).setdX((int) dX.getValue());
			else sequence.getModele().getEtape(etape).setdY((int) dY.getValue());
			change();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(selection != null) {
			if(p != null)
				incrAngle(e.getPoint().y - p.y - e.getPoint().x + p.x);
			p = e.getPoint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		setSelection(getMembre(e.getPoint(), e.isShiftDown()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		p = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == suppr) {
			changeEtape(sequence.getModele().removeEtape(etape));
			barre.setEtape(etape);
		} else if(e.getSource() == add) {
			etape++;
			sequence.getModele().addEtape(etape);
			changeEtape(etape);
			barre.setEtape(etape);
		} else {
			sequence.getModele().getEtape(etape).setInverseSens(inverse.isSelected());
			change();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			selection = membres.getSelectedValue();
			selectionChange();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}



}
