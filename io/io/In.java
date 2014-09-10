package io;

import java.io.IOException;
import java.io.InputStream;

import exception.HorsLigneException;

public class In extends InOut {
	
	public In(InputStream in) {
		setIn(in);
	}

	@Override
	public byte[] lire() throws IOException, HorsLigneException {
		return lire(lireShortInt());
	}

}
