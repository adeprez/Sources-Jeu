package jeu;

import interfaces.Actualisable;
import interfaces.Localise;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;

import layouts.LayoutPerso;
import layouts.PlaceurComposants;
import map.DessineurElementsMap3D;
import map.objets.Objet;
import partie.PartieClient;
import reseau.client.ControleOutReseau;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.paquets.session.PaquetPing;
import ressources.Proprietes;
import statique.Style;
import temps.EvenementTempsPeriodique;
import temps.Evenementiel;
import temps.GestionnaireEvenements;
import vision.Camera;
import vision.VisionJeu;
import base.Fenetre;

import composants.HorlogeProgression;

import controles.ControlesClavier;
import divers.Outil;
import ecrans.ContainerMap;


public class EcranJeu extends ContainerMap<Objet> implements Actualisable, Evenementiel, PlaceurComposants {
    private static final long serialVersionUID = 1L;
    private final ControleOutReseau controle;
    private final HorlogeProgression horloge;
    private final PartieClient partie;
    private final ControlesClavier c;
    private final EcranScores scores;
    private final VisionJeu vision;
    private final BarreJeu barre;
    private final JLabel ping;


    public EcranJeu(PartieClient partie) {
	super(new Camera());
	setLayout(new LayoutPerso(this));
	this.partie = partie;
	getCamera().setSource(vision = new VisionJeu(getCamera(), partie.getPerso()));

	setName("Jeu");
	setDoubleBuffered(true);
	setFocusable(true);
	setIgnoreRepaint(true);
	setMap(partie.getClient().getRessources().getMap());

	horloge = new HorlogeProgression(partie.getClient().getHorloge());
	controle = new ControleOutReseau(getCamera(), partie.getClient());
	scores = new EcranScores(partie);
	barre = new BarreJeu(partie.getPerso(), controle);
	c = new ControlesClavier("test");
	c.addActionControleListener(controle);

	add(ping = Outil.getTexte("", false));
	add(scores);
	add(horloge);
	add(barre);

	addMouseListener(controle);
	c.addControleListener(scores);

	partie.getClient().write(new PaquetPing());
	partie.getClient().addActualiseListener(this);
	partie.lancer();
	if(Proprietes.getInstance().est3D())
	    partie.getClient().getRessources().getMap().setDessineur(new DessineurElementsMap3D<Objet>());
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
	return partie.fermer() && super.fermer();
    }

    @Override
    public void actualise() {
	ping.setText("Ping : " + partie.getClient().getPing());
    }

    @Override
    public void evenement(EvenementTempsPeriodique source, GestionnaireEvenements p) {
	partie.getClient().write(new PaquetPing());
	partie.getClient().write(new Paquet(TypePaquet.TEMPS));
    }

    @Override
    public void layout(Container parent) {
	horloge.setBounds(0, 0, 75, 30);
	ping.setBounds(0, 30, 75, 30);
	int h = barre.getPreferredSize().height;
	barre.setBounds(0, getHeight() - h, getWidth(), h);
	Dimension d = scores.getPreferredSize();
	scores.setBounds(getWidth() - d.width, (getHeight() - d.height)/2, d.width, d.height);
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

}
