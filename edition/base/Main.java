package base;

import javax.swing.UIManager;

import reseau.client.TentativeConnexion;

public class Main {
    public static void main(String... args) {
	apparence("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	TentativeConnexion.testLocal(true, 0);
	//				EcranAdministration.ouvrir(false);
	//		Fenetre.getInstance().changer(new EcranEditeurLieu(new Compte("test"), new Lieu("comptes/test/territoire/lieux/test2"))).setVisible(true);
	//		Fenetre.getInstance().changer(new EcranChoixPartie(new Compte("test"))).setVisible(true);
	//		try {
	//			Serveur.main();
	//		} catch(IOException e) {
	//			e.printStackTrace();
	//		}
	//Fenetre.getInstance().changer(new EcranEditeurLieu(Compte.getComptes()[0], new Lieu("comptes/test/territoire/lieux/test2"))).setVisible(true);
	//		Fenetre.getInstance().setVisible(true);
    }

    public static boolean apparence(String nom) {
	try {
	    UIManager.setLookAndFeel(nom);
	    return true;
	} catch(Exception err) {
	    err.printStackTrace();
	    return false;
	}
    }

}
