package statique;

import java.awt.AlphaComposite;
import java.awt.Font;

import ressources.Proprietes;
import divers.Curseur;

public final class Style {
	public static final AlphaComposite TRANSPARENCE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f),
			SEMI_OPAQUE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .50f);
	public static Font POLICE = setPolice(), TITRE;
	public static final Curseur curseur = new Curseur();

	
	private Style() {}
	
	public static Font setPolice() {
		POLICE = new Font(Proprietes.getInstance().getPolice(), Font.PLAIN, 20);
		TITRE = POLICE.deriveFont(Font.BOLD).deriveFont(26f);
		return POLICE;
	}
}
