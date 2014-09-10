package io;

import interfaces.Lancable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import reseau.listeners.ReceiveListener;
import reseau.paquets.TypePaquet;
import exception.HorsLigneException;
import exception.InvalideException;


public abstract class InOut extends Out implements Runnable, Lancable {
	private InputStream input;
	private boolean run;


	public InOut() {}

	public InOut(InputStream reader, OutputStream writer) {
		super(writer);
		setOut(writer);
	}

	public abstract byte[] lire() throws IOException, InvalideException, HorsLigneException;

	public void setIn(InputStream input) {
		this.input = input;
	}
	
	public InputStream getIn() {
		return input;
	}

	public void addReceiveListener(ReceiveListener l) {
		addListener(ReceiveListener.class, l);
	}

	public void removeReceiveListener(ReceiveListener l) {
		removeListener(ReceiveListener.class, l);
	}

	public byte lireByte() throws IOException, HorsLigneException {
		int i = input.read();
		if(i == -1)
			throw new HorsLigneException();
		return (byte) i;
	}

	public int lireShortInt() throws IOException, HorsLigneException {
		return IO.getInt(lireByte(), lireByte(), lireByte());
	}

	public byte[] lire(int nbr) throws IOException, HorsLigneException {
		byte[] b = new byte[nbr];
		for(int i=0 ; i<b.length ; i++) 
			b[i] = lireByte();
		return b;
	}

	protected void notifyReceiveListener(TypePaquet type, IO in) {
		for(final ReceiveListener l : getListeners(ReceiveListener.class)) {
			in.setIndex(1);
			try {
				l.recu(type, in);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isRunning() {
		return run;
	}

	@Override
	public void run() {
		while(isRunning()) try {
			IO io = new IO(lire());
			notifyReceiveListener(TypePaquet.get(io.nextPositif()), io);
		} catch(SocketTimeoutException e) {
			fermer();
		} catch(SocketException e) {
			fermer();
		} catch(HorsLigneException e) {
			fermer();
		} catch(InvalideException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean lancer() {
		run = true;
		new Thread(this).start();
		return run;
	}

	@Override
	public boolean fermer() {
		if(super.fermer()) try {
			input.close();
			run = false;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return !run;
	}


}
