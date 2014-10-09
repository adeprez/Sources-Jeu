package chat;

import io.IO;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import layouts.LayoutLignes;
import listeners.AjoutListener;
import reseau.AbstractClient;
import reseau.paquets.PaquetMessage;

import composants.styles.ScrollPaneTransparent;

import divers.Outil;

public class PanelChat extends JPanel implements ActionListener, DocumentListener {
    private static final int TAILLE_MESSAGE_MAX = 500;
    private static final long serialVersionUID = 1L;
    private final ScrollPaneTransparent scroll;
    private final AbstractClient client;
    private final JTextField texte;
    private final JPanel messages;
    private final int type;


    public PanelChat(AbstractClient client, String nom, int type) {
	this.client = client;
	this.type = type;
	setName(nom);
	setLayout(new BorderLayout());
	setOpaque(false);
	messages = new JPanel(new LayoutLignes());
	texte = new JTextField();
	texte.setDragEnabled(true);
	texte.getDocument().addDocumentListener(this);
	add(texte, BorderLayout.SOUTH);
	add(scroll = new ScrollPaneTransparent(messages), BorderLayout.CENTER);
	texte.addActionListener(this);
    }

    public void addChangeTitreListener(AjoutListener<String> l) {
	listenerList.add(AjoutListener.class, l);
    }

    public void setTitre() {
	setTitre(getName());
    }

    @SuppressWarnings("unchecked")
    public void setTitre(String titre) {
	for(final AjoutListener<String> l : getListeners(AjoutListener.class))
	    l.ajout(titre);
    }

    public JPanel getMessages() {
	return messages;
    }

    public void addMessage(PanelMessage msg) {
	messages.add(msg);
	scroll.doLayout();
	scroll.validate();
	scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    public AbstractClient getClient() {
	return client;
    }

    public IO getPaquet() {
	return new PaquetMessage(type, texte.getText().trim());
    }

    public void removeFocus() {
	texte.setFocusable(false);
	texte.setFocusable(true);
    }

    public void checkFocus() {
	if(texte.getText().isEmpty())
	    removeFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if(texte.getText().trim().isEmpty())
	    removeFocus();
	else if(texte.getText().trim().length() > TAILLE_MESSAGE_MAX)
	    Outil.erreur("Les messages ne doivent pas dépasser " + TAILLE_MESSAGE_MAX + " caractères");
	else {
	    client.write(getPaquet());
	    texte.setText("");
	}
    }

    @Override
    public void insertUpdate(DocumentEvent e) {}

    @Override
    public void removeUpdate(DocumentEvent e) {
	checkFocus();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
	checkFocus();
    }

}
