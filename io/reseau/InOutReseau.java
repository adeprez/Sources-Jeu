package reseau;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import reseau.serveur.Serveur;

public class InOutReseau extends InOutCodeID {
    private final Socket socket;


    public InOutReseau() throws UnknownHostException, IOException {
	this(InetAddress.getLocalHost());
    }

    public InOutReseau(InetAddress adresse) throws UnknownHostException, IOException {
	this(adresse, Serveur.DEFAULT_PORT);
    }

    public InOutReseau(InetAddress adresse, int port) throws IOException {
	this(new Socket(adresse, port));
    }

    public InOutReseau(Socket socket) throws IOException {
	this.socket = socket;
	setOut(socket.getOutputStream());
	setIn(socket.getInputStream());
    }

    public InetAddress getAdresse() {
	return socket.getInetAddress();
    }

    public Socket getSocket() {
	return socket;
    }

    public int getPort() {
	return socket.getPort();
    }

    @Override
    public boolean fermer() {
	if(super.fermer()) try {
	    socket.close();
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

}
