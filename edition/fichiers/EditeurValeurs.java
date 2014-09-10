package fichiers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import divers.Outil;

public class EditeurValeurs extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final EditeurFichier editeur;
	private final JComboBox<Valeur> types;
	private final JTextField entree;


	public EditeurValeurs(EditeurFichier editeur) {
		super(new BorderLayout());
		this.editeur = editeur;
		setBorder(new TitledBorder("Ajouter"));
		types = new JComboBox<Valeur>(Valeur.values());
		entree = new JTextField();
		entree.addActionListener(this);
		add(types, BorderLayout.WEST);
		add(entree, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(entree.getText() != null) try {
			switch((Valeur) types.getSelectedItem()) {
			case Boolean:
				boolean b1 = Boolean.valueOf(entree.getText());
				editeur.getChamps().add(b1);
				editeur.enregistrer();
				break;
			case Byte:
				byte b2 = Byte.valueOf(entree.getText());
				editeur.getChamps().add(b2);
				editeur.enregistrer();
				break;
			case Integer:
				int i = Integer.valueOf(entree.getText());
				editeur.getChamps().add(i);
				editeur.enregistrer();
				break;
			case Int_Positif:
				int si = Integer.valueOf(entree.getText());
				editeur.getChamps().addShort(si);
				editeur.enregistrer();
				break;
			case Long:
				long l = Long.valueOf(entree.getText());
				editeur.getChamps().add(l);
				editeur.enregistrer();
				break;
			case String:
				editeur.getChamps().add(entree.getText());
				editeur.enregistrer();
				break;
			case Short_String:
				editeur.getChamps().addShort(entree.getText());
				editeur.enregistrer();
				break;
			case String_Long:
				editeur.getChamps().addLong(entree.getText());
				editeur.enregistrer();
				break;
			case Byte_Positif:
				int pi = Integer.valueOf(entree.getText());
				editeur.getChamps().addBytePositif(pi);
				editeur.enregistrer();
				break;
			case Lire_Byte_Positif:
				Outil.message("Byte positif : " + editeur.getChamps().nextPositif() + " (index=" + editeur.getChamps().getIndex() + ")");
				break;
			case Lire_Int_Positif:
				Outil.message("Int positif : " + editeur.getChamps().nextShortInt() + " (index=" + editeur.getChamps().getIndex() + ")");
				break;
			case Lire_Int:
				Outil.message("Int : " + editeur.getChamps().nextInt() + " (index=" + editeur.getChamps().getIndex() + ")");
				break;
			case Lire_String:
				Outil.message("String : " + editeur.getChamps().nextString() + " (index=" + editeur.getChamps().getIndex() + ")");
				break;
			case Lire_Long:
				Outil.message("Long : " + editeur.getChamps().nextLong() + " (index=" + editeur.getChamps().getIndex() + ")");
				break;
			case Set_Index:
				int index = Integer.valueOf(entree.getText());
				editeur.getChamps().setIndex(index);
				Outil.message(" (index=" + editeur.getChamps().getIndex() + ")");
				break;
			default: 
				System.err.println("EditValeurs : pas de correspondance pour le type");
				break;
			}
		} catch(Exception err) {
			Outil.erreur("Entrez une valeur correcte.\n" + err.getMessage());
		}
	}

	private enum Valeur {
		Byte_Positif, Byte, Int_Positif, Integer, String, Short_String, String_Long, Boolean, Long, 
		Lire_Byte_Positif, Lire_Int_Positif, Lire_Int, Lire_String, Lire_Long, Set_Index;
	}

}
