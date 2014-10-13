package perso;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;

import listeners.ChangeSpecialiteListener;
import physique.Direction;
import physique.actions.ActionMeurt;
import physique.forme.CorpsPerso;
import ressources.Images;
import ressources.sprites.animation.AnimationPerso;
import specialite.Specialite;
import specialite.TypeSpecialite;
import statique.Style;

public abstract class AbstractPerso extends Vivant {
    private final Specialite[] specialites;
    private final InformationsPerso infos;
    private final Caracteristiques caract;
    private final AnimationPerso anim;
    private final CorpsPerso corps;
    private final int[] xp;
    private int specialite, spePrecedente;
    private ImageIcon icone;


    public AbstractPerso(CorpsPerso corps, int[] xp, Caracteristiques caract, InformationsPerso infos, AnimationPerso anim) {
	super(corps);
	this.caract = caract;
	this.infos = infos;
	this.anim = anim;
	this.corps = corps;
	this.xp = xp.length < TypeSpecialite.values().length ? Arrays.copyOf(xp, TypeSpecialite.values().length) : xp;
	specialites = Specialite.getSpecialites(this);
	specialite = -1;
    }

    public BufferedImage getIcone() {
	return anim.getTetePerso().getImage();
    }

    public ImageIcon getSmallIcone() {
	if(icone == null)
	    icone = new ImageIcon(anim.getTetePerso().getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	return icone;
    }

    public Specialite getSpecialite() {
	return specialite == -1 ? null : specialites[specialite];
    }

    public Specialite[] getSpecialites() {
	return specialites;
    }

    public int getIDSpecialite() {
	return specialite;
    }

    public int getXP(TypeSpecialite type) {
	return xp[type.ordinal()];
    }

    public void setXP(TypeSpecialite type, int xp) {
	this.xp[type.ordinal()] = xp;
    }

    public void incrXP(TypeSpecialite type, int xp) {
	setXP(type, xp + getXP(type));
    }

    public int getSpecialitePrincipale() {
	int max = 0;
	for(int i=1 ; i<xp.length ; i++)
	    if(xp[max] < xp[i])
		max = i;
	return max;
    }

    public int getSpecialitePrecedente() {
	return spePrecedente;
    }

    public void setSpecialite(int specialite) {
	spePrecedente = Math.max(0, this.specialite);
	Specialite ancienne = getSpecialite();
	if(ancienne != null)
	    ancienne.fermer();
	this.specialite = specialite;
	Specialite nouvelle = getSpecialite();
	nouvelle.lancer();
	for(final ChangeSpecialiteListener l : getListeners(ChangeSpecialiteListener.class))
	    l.changeSpecialite(this, ancienne, nouvelle);
    }

    public void setSpecialite(TypeSpecialite type) {
	setSpecialite(type.ordinal());
    }

    public void addChangeSpecialiteListener(ChangeSpecialiteListener l) {
	addListener(ChangeSpecialiteListener.class, l);
    }

    public void removeChangeSpecialiteListener(ChangeSpecialiteListener l) {
	removeListener(ChangeSpecialiteListener.class, l);
    }

    public int[] getXPCompetences() {
	return xp;
    }

    public InformationsPerso getInfos() {
	return infos;
    }

    public Caracteristiques getCaract() {
	return caract;
    }

    public CorpsPerso getCorps() {
	return corps;
    }

    public void dessineInfos(Graphics2D g, Rectangle zone, int equipe) {
	g.setFont(Style.POLICE);
	String s = getNom();
	int l = g.getFontMetrics().stringWidth(s), l2 = (int) (l * 1.1), h = g.getFontMetrics().getHeight();
	dessineFond(g, zone.x + (zone.width - l2)/2, (int) (zone.y - h * 1.75), l2, h, equipe);
	g.setColor(getCouleur());
	g.drawString(s, zone.x + (zone.width - l)/2, zone.y - h);
    }

    public void dessineFond(Graphics2D g, int x, int y, int w, int h, int equipe) {
	Composite tmp = g.getComposite();
	g.setComposite(Style.TRANSPARENCE);
	g.setColor(equipe == getEquipe() ? Color.LIGHT_GRAY : Color.RED);
	g.fillRoundRect(x, y, w, h, w/5, h/3);
	g.setComposite(tmp);
    }

    @Override
    public void meurt() {
	super.meurt();
	forceAction(new ActionMeurt(this));
    }

    @Override
    public int getLargeurVie(Graphics g, Rectangle zone) {
	return (int) (g.getFontMetrics(Style.POLICE).stringWidth(getNom()) * .9);
    }

    @Override
    public AnimationPerso getAnimation() {
	return anim;
    }

    @Override
    public int getVitalite() {
	return caract.getVitalite();
    }

    @Override
    public int getVitesse() {
	return caract.getVitesse() * 25;
    }

    @Override
    public String toString() {
	return "[PERSO " + infos + "]" + super.toString();
    }

    @Override
    public void setAngle(int angle) {
	super.setAngle(angle);
	if(anim != null && anim.getSequence() == null)
	    anim.getTetePerso().setAngleFutur(getAngleRad(), 5);
    }

    @Override
    public String getNom() {
	return infos.getNom();
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	dessineInfos(g, zone, equipe);
	super.dessiner(g, zone, equipe);
	if(anim == null)
	    g.drawImage(Images.get("divers/options.png", true), zone.x, zone.y, zone.width, zone.height, null);
	else anim.dessiner(g, zone, getDirection() == Direction.DROITE);
    }
}
