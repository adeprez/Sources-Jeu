package base;

import interfaces.EcranChangeable;
import interfaces.Fermable;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import ressources.Images;
import ressources.Proprietes;
import statique.Style;

public class Fenetre extends JFrame implements EcranChangeable, Fermable {
    private static final long serialVersionUID = 1L;
    private static Fenetre instance;
    private final GlassPane glassPane;
    private Ecran ecran;


    public static Fenetre getInstance() {
	synchronized(Fenetre.class) {
	    if(instance == null) {
		instance = new Fenetre();
		IconeTaches.getInstance();
	    }
	    return instance;
	}
    }

    public static boolean aInstance() {
	return instance != null;
    }

    public static Fenetre newFrame(Ecran e) {
	Fenetre f = newFrame((Container) e);
	if(e != null)
	    f.changer(e);
	f.setVisible(true);
	return f;
    }

    public static Fenetre newFrame(Container contentPane) {
	Fenetre f = new Fenetre();
	if(contentPane != null)
	    f.setContentPane(contentPane);
	return f;
    }

    @Override
    public GlassPane getGlassPane() {
	return glassPane;
    }

    private Fenetre() {
	setCursor(Style.curseur.defaut());
	glassPane = new GlassPane();
	setIconImage(Images.get("divers/icone.png", false));
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	setSize(Math.min(700, (int) (d.width/1.2)), Math.min(600, (int) (d.height/1.2)));
	setMinimumSize(new Dimension(450, 425));
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLocationRelativeTo(getRootPane());
	setFocusable(true);
	setGlassPane(glassPane);
	requestFocus();
    }

    @Override
    public Fenetre changer(Ecran nouveau) {
	if(ecran != null)
	    ecran.fermer();
	nouveau.setFenetre(this);
	setContentPane(nouveau);
	setTitle(nouveau.getName()
		+ (Proprietes.getInstance().estAdmin() ? " - ADMIN" : "")
		+ (Proprietes.getInstance().estDebug() ? " - DEBUG" : ""));
	ecran = nouveau;
	validate();
	repaint();
	return this;
    }

    @Override
    public boolean fermer() {
	boolean ok = ecran == null || ecran.fermer();
	dispose();
	return ok;
    }

}
