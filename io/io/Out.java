package io;

import interfaces.Fermable;
import interfaces.IOable;
import interfaces.Outer;

import java.io.IOException;
import java.io.OutputStream;

import divers.Listenable;

public class Out extends Listenable implements Outer, Fermable {
	private OutputStream out;


	public Out() {}

	public Out(OutputStream out) {
		setOut(out);
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	public OutputStream getOut() {
		return out;
	}

	protected void writeByte(byte b) throws IOException {
		out.write(b);
	}

	@Override
	public boolean fermer() {
		if(out == null)
			return true;
		try {
			out.flush();
			out.close();
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean write(IOable write) {
		try {
			byte[] b = write.getBytes();
			out.write(IO.getBytes(b.length));
			out.write(b);
			out.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}



}
