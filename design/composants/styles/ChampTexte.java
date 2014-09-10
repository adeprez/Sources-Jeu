package composants.styles;

import io.IO;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import statique.Style;
import bordures.BordureImage;

public class ChampTexte extends JTextField {
	private static final long serialVersionUID = 1L;


	public ChampTexte(boolean titre, boolean centre) {
		setDocument(new Doc(this));
		setCursor(Style.curseur.texte());
		setBorder(new BordureImage("trait large.png"));
		setFont(titre ? Style.TITRE : Style.POLICE);
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		if(centre)
			setHorizontalAlignment(SwingConstants.CENTER);
	}

	public ChampTexte(String texte, boolean titre, boolean centre) {
		this(titre, centre);
		setText(texte);
	}

	public ChampTexte() {
		this(false, false);
	}

	public ChampTexte(String texte, boolean centre) {
		this(texte, false, centre);
	}

	public ChampTexte(String texte) {
		this(texte, false);
	}

	private class Doc extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private final JTextComponent txt;
		
		
		public Doc(JTextComponent txt) {
			this.txt = txt;
		}

		@Override
		public void insertString(int off, String s, AttributeSet a) throws BadLocationException {
			if(txt.getText().length() < IO.LIMITE_BYTE_POSITIF)
				super.insertString(off, s, a);
			else Toolkit.getDefaultToolkit().beep();
		}
	}
	
	
}
