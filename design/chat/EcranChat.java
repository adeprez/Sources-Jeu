package chat;

import io.IO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import perso.Perso;
import reseau.client.Client;
import reseau.listeners.MessageListener;
import reseau.paquets.PaquetMessage;
import statique.Style;
import base.Ecran;

public class EcranChat extends Ecran implements MessageListener {
    private static final long serialVersionUID = 1L;
    private final PanelChat[] chats;
    private final JTabbedPane tab;
    private final Client client;


    public EcranChat(Client client) {
	this.client = client;
	setLayout(new GridLayout());
	tab = new JTabbedPane(SwingConstants.BOTTOM);
	tab.setFont(Style.POLICE.deriveFont(12f));
	tab.setFocusable(false);
	chats = new PanelChat[3];
	for(int i=0 ; i<chats.length ; i++) {
	    chats[i] = getPanel(i);
	    tab.addTab(chats[i].getName(), chats[i]);
	    final int index = i;
	    chats[i].addChangeTitreListener((String titre) -> tab.setTitleAt(index, titre));
	}
	add(tab, BorderLayout.CENTER);
	client.addMessageListener(this);
    }

    @Override
    public boolean fermer() {
	if(super.fermer()) {
	    client.removeMessageListener(this);
	    return true;
	}
	return false;
    }

    @Override
    public void message(int type, String message, Perso expediteur, IO io) {
	boolean self = client.getPerso() == expediteur;
	switch(type) {
	case PaquetMessage.INFO:
	case PaquetMessage.GENERAL:
	    chats[0].addMessage(getPanelMessage(type, message, expediteur, self, null));
	    break;
	case PaquetMessage.EQUIPE:
	    chats[0].addMessage(getPanelMessage(type, message, expediteur, self, null));
	    chats[1].addMessage(getPanelMessage(type, message, expediteur, self, null));
	    break;
	case PaquetMessage.PRIVE:
	    Perso cible = self ? client.getPerso(io.nextPositif()) : null;
	    chats[0].addMessage(getPanelMessage(type, message, expediteur, self, cible));
	    chats[2].addMessage(getPanelMessage(type, message, expediteur, self, cible));
	    tab.setTitleAt(2, chats[2].getName());
	    break;
	}
    }

    private PanelMessage getPanelMessage(int type, String message, Perso expediteur, boolean self, Perso cible) {
	switch(type) {
	case PaquetMessage.INFO: return new PanelMessage(message, expediteur, Color.GREEN, new Color(0, 0, 0, 100), self, cible);
	case PaquetMessage.GENERAL: return new PanelMessage(message, expediteur, Color.BLACK, new Color(255, 255, 255, 100), self, cible);
	case PaquetMessage.EQUIPE: return new PanelMessage(message, expediteur, Color.WHITE, new Color(100, 100, 255, 100), self, cible);
	case PaquetMessage.PRIVE: return new PanelMessage(message, expediteur, Color.WHITE, new Color(0, 255, 0, 100), self, cible);
	default: return null;
	}
    }

    private PanelChat getPanel(int chat) {
	switch(chat) {
	case 0: return new PanelChat(client, "Tous", PaquetMessage.GENERAL);
	case 1: return new PanelChat(client, "Equipe", PaquetMessage.EQUIPE);
	case 2: return new PanelChatPrive(client);
	default: return null;
	}
    }

}
