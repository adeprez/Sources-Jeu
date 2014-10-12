package jeu;

import interfaces.Localise;
import io.IO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import layouts.LayoutPerso;
import layouts.PlaceurComposants;
import listeners.PartieListener;
import map.DessineurElementsMap3D;
import map.objets.Objet;
import partie.Partie;
import partie.PartieClient;
import perso.Perso;
import reseau.client.ControleOutReseau;
import reseau.listeners.MessageListener;
import reseau.paquets.session.PaquetPing;
import ressources.Proprietes;
import statique.Style;
import vision.Camera;
import vision.VisionJeu;
import base.Fenetre;
import chat.EcranChat;

import composants.HorlogeProgression;
import composants.Redimensionneur;

import controles.ControlesClavier;
import divers.Outil;
import ecrans.ContainerMap;


public class EcranJeu extends ContainerMap<Objet> implements PlaceurComposants, ActionListener, MessageListener, PartieListener {
    private static final Dimension TAILLE_MINI_CHAT = new Dimension(200, 60);
    private static final long serialVersionUID = 1L;
    private final ControleOutReseau controle;
    private final HorlogeProgression horloge;
    private final JButton toggle, suivant;
    private final Redimensionneur chat;
    private final PartieClient partie;
    private final ControlesClavier c;
    private final EcranScores scores;
    private final VisionJeu vision;
    private final BarreJeu barre;
    private Dimension dChat;
    private int cinematique;


    public EcranJeu(PartieClient partie) {
	super(new Camera());
	setLayout(new LayoutPerso(this));
	this.partie = partie;
	getCamera().setSource(vision = new VisionJeu(getCamera(), partie.getPerso()));

	setName("Jeu");
	setDoubleBuffered(true);
	setIgnoreRepaint(true);
	setMap(partie.getClient().getRessources().getMap());

	horloge = new HorlogeProgression();
	controle = new ControleOutReseau(getCamera(), partie.getClient());
	scores = new EcranScores(partie);
	barre = new BarreJeu(partie.getPerso(), controle);
	toggle = new JButton("+");
	suivant = new JButton("Continuer");
	EcranChat ec = new EcranChat(partie.getClient());
	c = new ControlesClavier("test");
	c.addActionControleListener(controle);
	chat = new Redimensionneur(ec);
	suivant.setFont(Style.POLICE);
	suivant.setFocusable(false);
	suivant.setBackground(Color.BLACK);
	suivant.setForeground(Color.ORANGE);

	add(scores);
	add(horloge);
	add(barre);
	add(toggle);
	add(chat);

	addMouseListener(controle);
	c.addControleListener(scores);
	suivant.addActionListener(this);
	scores.setVisible(false);
	partie.addPartieListener(this);
	partie.getClient().write(new PaquetPing());
	partie.getClient().addMessageListener(this);
	partie.getClient().getHorloge().addHorlogeListener(horloge);
	partie.lancer();
	if(Proprietes.getInstance().est3D())
	    partie.getClient().getRessources().getMap().setDessineur(new DessineurElementsMap3D<Objet>());
	toggle.addActionListener(this);
	toggle.setMargin(new Insets(0, 0, 0, 0));
	toggle.setFont(toggle.getFont().deriveFont(14f).deriveFont(Font.BOLD));
	toggle.setBorderPainted(false);
	toggle.setBorder(null);
	toggle.setFocusable(false);
	dChat = new Dimension(300, 300);
	chat.setPreferredSize(TAILLE_MINI_CHAT);
    }

    public VisionJeu getVision() {
	return vision;
    }

    @Override
    public void paintComponent(Graphics g) {
	long t = System.currentTimeMillis();
	super.paintComponent(g);
	int fps = (int) (1000/Math.max(1, System.currentTimeMillis() - t));
	g.setColor(Color.ORANGE);
	g.setFont(Style.POLICE);
	g.drawString("fps:" + fps, getWidth() - 50, 25);
	g.fillRect(getWidth() - 1000/fps, 0, 1000/fps, 10);
	if(cinematique > 0) {
	    g.setColor(Color.BLACK);
	    int h = getHeight() * Math.min(20, cinematique)/133;
	    g.fillRect(0, 0, getWidth(), h);
	    g.fillRect(0, getHeight() - h, getWidth(), h);
	    if(cinematique > 20) {
		if(cinematique == 21)
		    add(suivant);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1, (cinematique - 20)/100f)));
		String s = getName();
		g.setFont(Style.TITRE.deriveFont(40f));
		int w = g.getFontMetrics().stringWidth(s), w2 = (int) (w * 1.5 + 30);
		h = g.getFontMetrics().getHeight() * 2;
		g2.setPaint(new GradientPaint(getWidth()/2,  getHeight()/2, Color.BLACK,
			getWidth()/2, getHeight()/2 + h/2, new Color(0, 0, 0, 0), true));
		g.fillOval((getWidth() - w2)/2, getHeight()/2 - h/2, w2, h);
		g.setColor(Color.WHITE);
		g.drawString(s, (getWidth() - w)/2, getHeight()/2 + g.getFontMetrics().getHeight()/4);
		if(cinematique > 200)
		    stop();
	    }
	    cinematique++;
	}
    }

    @Override
    public void centrer() {}

    @Override
    public void afficher(Fenetre fenetre) {
	fenetre.addKeyListener(c);
    }

    @Override
    public boolean fermer() {
	getFenetre().removeKeyListener(c);
	partie.getClient().getHorloge().removeHorlogeListener(horloge);
	return super.fermer() && partie.fermer();
    }

    @Override
    public void layout(Container parent) {
	horloge.setBounds(0, 0, 75, 30);
	int h = barre.getPreferredSize().height;
	barre.setBounds(0, getHeight() - h, getWidth(), h);
	Dimension d = scores.getPreferredSize();
	scores.setBounds(getWidth() - d.width, (getHeight() - d.height)/2, d.width, d.height);
	chat.setBounds(0, getHeight() - chat.getPreferredSize().height, chat.getPreferredSize().width, chat.getPreferredSize().height);
	toggle.setBounds(chat.getWidth() - 15, getHeight() - 21, 22, 22);
	d = suivant.getPreferredSize();
	suivant.setBounds(getWidth() - d.width - 10, getHeight() - d.height - 10, d.width, d.height);
    }

    public static int getAngle(Camera cam, Localise source, int x, int y, boolean droite) {
	return getAngle(cam, source, x, y, droite, 7);
    }

    public static int getAngle(Camera cam, Localise source, int x, int y, boolean droite, int coefPortion) {
	Rectangle r = cam.getZone(source, 0);
	int angle = (int) Math.toDegrees(Math.atan2(x - r.x - (droite ? 0 : r.width), r.y + r.height/coefPortion - y))
		+ (droite ? -90 : 90);
	return Outil.entre(angle, -60, 60);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == suivant)
	    changer(new EcranResultatPartie(partie, getName()));
	else if("+".equals(toggle.getText())) {
	    chat.setPreferredSize(dChat);
	    toggle.setText("-");
	} else {
	    dChat = chat.getPreferredSize();
	    chat.setPreferredSize(TAILLE_MINI_CHAT);
	    toggle.setText("+");
	}
    }

    @Override
    public void message(int type, String message, Perso expediteur, IO io) {
	if(expediteur != null)
	    expediteur.message(message);
    }

    @Override
    public void finPartie(boolean equipe, int gagnant, Perso source) {
	if(gagnant == Partie.EGALITE)
	    setName("Egalité");
	else if(equipe ? partie.getPerso().getEquipe() == gagnant : partie.getClient().getID() == gagnant)
	    setName("Victoire !");
	else setName("Défaite. " + (equipe ? "L'équipe " + gagnant : partie.getPerso(gagnant).getNom()) + " gagne");
	if(source != null)
	    getCamera().setSource(new VisionJeu(getCamera(), source));
	removeAll();
	cinematique++;
    }

}
