package reseau;

import interfaces.IOable;
import io.InOut;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import exception.HorsLigneException;
import exception.InvalideException;

public class InOutCodeID extends InOut {
	private static final byte ID = 42;


	public InOutCodeID() {}

	public InOutCodeID(InputStream reader, OutputStream writer) {
		super(reader, writer);
	}

	@Override
	public synchronized boolean write(IOable write) {
		synchronized(getOut()) {
			try {
				writeByte(ID);
				return super.write(write);
			} catch(SocketException e) {
				return false;
			} catch(IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	@Override
	public byte[] lire() throws IOException, InvalideException, HorsLigneException {
		synchronized(getIn()) {
			if(lireByte() == ID) 
				return lire(lireShortInt());
			throw new InvalideException();
		}
	}

}
