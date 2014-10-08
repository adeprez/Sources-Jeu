package reseau.paquets;

public class PaquetMessage extends Paquet {
    public static final int SERVEUR = 0, INFO = 1, PRIVE = 2, EQUIPE = 3, GENERAL = 4;


    public PaquetMessage(int type, String message) {
	super(TypePaquet.MESSAGE);
	addBytePositif(type);
	addShort(message);
    }

    public PaquetMessage(int type, String message, int expediteur) {
	this(type, message);
	addBytePositif(expediteur);
    }

}
