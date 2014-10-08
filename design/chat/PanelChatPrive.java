package chat;

import interfaces.Nomme;
import io.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import listeners.AjoutPersoListener;
import listeners.RemovePersoListener;
import perso.Perso;
import reseau.client.Client;
import reseau.paquets.PaquetMessage;
import divers.Outil;

public class PanelChatPrive extends PanelChat implements ListCellRenderer<Perso>, AjoutPersoListener, RemovePersoListener {
    private static final long serialVersionUID = 1L;
    private final Map<String, Integer> count;
    private final DefaultComboBoxModel<Perso> modele;
    private final JComboBox<Perso> dest;


    public PanelChatPrive(Client client) {
	super(client, "Privé", PaquetMessage.PRIVE);
	modele = new DefaultComboBoxModel<Perso>();
	count = new HashMap<String, Integer>();
	for(final Perso p : client.getPartie().getMap().getPersos())
	    modele.addElement(p);
	client.getPartie().addAjoutPersoListener(this);
	client.getPartie().addRemovePersoListener(this);
	dest = new JComboBox<Perso>(modele);
	dest.setRenderer(this);
	dest.setFocusable(false);
	dest.addActionListener((ActionEvent e) -> update());
	add(dest, BorderLayout.NORTH);
    }

    public void update() {
	String nom = ((Nomme) modele.getSelectedItem()).getNom();
	count.remove(nom);
	for(final Component c : getMessages().getComponents())
	    c.setVisible(nom.equals(c.getName()));
	setTitre();
    }

    @Override
    public void setTitre() {
	int n = 0;
	for (final Integer i : count.values())
	    n += i;
	setName("Privé" + (n > 0 ? "(" + n + ")" : ""));
	super.setTitre();
    }

    @Override
    public void addMessage(PanelMessage msg) {
	super.addMessage(msg);
	if(msg.estOrigine())
	    msg.setName(msg.getCible().getNom());
	boolean visible = msg.getName().equals(((Nomme) modele.getSelectedItem()).getNom());
	msg.setVisible(visible);
	if(!visible) {
	    Integer val = count.get(msg.getName());
	    count.put(msg.getName(), val == null ? 1 : val + 1);
	}
	setTitre();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Perso> list, Perso value, int index, boolean isSelected, boolean cellHasFocus) {
	JPanel p = new JPanel(new BorderLayout());
	p.setPreferredSize(new Dimension(50, 25));
	if(cellHasFocus)
	    p.setBackground(Color.LIGHT_GRAY);
	else if(isSelected)
	    p.setBackground(Color.GRAY);
	else if(count.containsKey(value.getNom()))
	    p.setBackground(Color.ORANGE);
	else p.setOpaque(false);
	p.add(new JLabel(value.getSmallIcone()), BorderLayout.WEST);
	p.add(Outil.getTexte(value.getNom(), false), BorderLayout.CENTER);
	Integer val = count.get(value.getNom());
	if(val != null)
	    p.add(Outil.getTexte("(" + val + ")", false), BorderLayout.EAST);
	return p;
    }

    @Override
    public IO getPaquet() {
	return super.getPaquet().addBytePositif(getClient().getRessources().getIDPerso(modele.getSelectedItem()));
    }

    @Override
    public void remove(Perso perso) {
	if(perso != getClient().getPerso()) {
	    modele.removeElement(perso);
	    for(final Component c : getMessages().getComponents())
		if(c.getName().equals(perso.getNom()))
		    remove(c);
	    count.remove(perso);
	    setTitre();
	}
    }

    @Override
    public void ajout(Perso perso) {
	if(perso != getClient().getPerso())
	    modele.addElement(perso);
    }

}
