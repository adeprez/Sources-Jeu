package reseau.serveur;

import interfaces.Fermable;
import interfaces.FiltreEnvoi;
import interfaces.IOable;
import interfaces.Joignable;
import interfaces.Lancable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import reseau.AbstractClient;
import reseau.donnees.MessageServeur;
import reseau.listeners.ConnexionListener;
import reseau.listeners.DeconnexionListener;
import reseau.paquets.Paquet;
import reseau.ressources.RessourcesReseau;
import reseau.serveur.filtreEnvoi.DefaultFiltreEnvoi;
import divers.Listenable;
import exception.ServeurFullException;

@SuppressWarnings("unchecked")
public abstract class AbstractServeur<E extends AbstractClient> extends Listenable 
implements Runnable, Lancable, Fermable, DeconnexionListener<AbstractClient>, Joignable<E> {
	private final HashMap<Integer, E> clients;
	private final List<Exception> erreurs;
	private final ServerSocket socket;
	private final Thread thread;
	private long temps;


	public AbstractServeur(int port) throws IOException {
		socket = new ServerSocket(port);
		clients = new HashMap<Integer, E>();
		erreurs = new ArrayList<Exception>();
		thread = new Thread(this);
	}

	public abstract RessourcesReseau getRessources();
	public abstract Paquet getPaquetConnexion();
	public abstract int getMaxClients();
	public abstract E creerClient(Socket socket) throws IOException;

	public int getPort() {
		return socket.getLocalPort();
	}

	public InetAddress getAdresse() {
		return socket.getInetAddress();
	}

	public Collection<E> getClients() {
		return clients.values();
	}

	public E getClient(int id) {
		return clients.get(id);
	}

	public void deconnecterClients(MessageServeur cause) {
		for(final AbstractClient c : getClients())
			c.fermer(cause);
	}

	public void addConnexionListener(ConnexionListener<E> l) {
		addListener(ConnexionListener.class, l);
	}

	public void removeConnexionListener(ConnexionListener<E> l) {
		removeListener(ConnexionListener.class, l);
	}

	public void addDeconnexionListener(DeconnexionListener<E> l) {
		addListener(DeconnexionListener.class, l);
	}

	public void removeDeconnexionListener(DeconnexionListener<E> l) {
		removeListener(DeconnexionListener.class, l);
	}

	private void notifierConnexionListener(E client) {
		for(final ConnexionListener<E> l : getListeners(ConnexionListener.class))
			l.connexion(client);
	}

	private void notifyDeconnexionListener(E e) {
		for(final DeconnexionListener<E> l : getListeners(DeconnexionListener.class))
			l.deconnexion(e);
	}

	private int getIDLibre() throws ServeurFullException {
		if(getClients().size() >= getMaxClients())
			throw new ServeurFullException();
		for(byte i=0 ; i<getMaxClients() ; i++) 
			if(getClient(i) == null)
				return i;
		throw new ServeurFullException();
	}
	
	public void envoyer(int id, IOable io) {
		getClient(id).write(io);
	}
	
	public void envoyerTous(IOable io) {
		envoyerFiltre(io, new DefaultFiltreEnvoi());
	}
	
	public void envoyerFiltre(IOable io, FiltreEnvoi filtre) {
		for(final int id : clients.keySet())
			if(filtre.doitEnvoyer(id, io))
				envoyer(id, io);
	}

	@Override
	public void run() {
		temps = System.currentTimeMillis();
		while(!socket.isClosed()) {
			try {
				AbstractClient c = creerClient(socket.accept());;
				c.lancer();
				c.write(getPaquetConnexion());
			} catch(SocketException e) {
			} catch(Exception e) {
				e.printStackTrace();
				erreurs.add(e);
			}
		}
		System.out.println("Fin du serveur. " + ((System.currentTimeMillis() - temps)/1000) 
				+ " secondes en ligne");
		if(!erreurs.isEmpty())
			System.err.println(erreurs.size() + " erreurs");
	}

	@Override
	public boolean rejoindre(E c) {
		try {
			c.setID(getIDLibre());
			clients.put(c.getID(), c);
			c.addDeconnexionListener(this);
			notifierConnexionListener(c);
			return true;
		} catch(ServeurFullException e) {
			c.fermer(new MessageServeur(MessageServeur.ERREUR, e.getMessage()));
			return false;
		}
	}

	@Override
	public void deconnexion(AbstractClient client) {
		E c = clients.remove(client.getID());
		if(c != null) {
			c.removeDeconnexionListener(this);
			notifyDeconnexionListener(c);
		}
	}

	@Override
	public boolean fermer() {
		try {
			deconnecterClients(new MessageServeur(MessageServeur.INFO, "Le serveur est hors ligne"));
			socket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean lancer() {
		if(!thread.isAlive()) {
			thread.start();
			return true;
		}
		return false;
	}

}
