package interfaces;

import reseau.AbstractClient;

public interface Joignable<E extends AbstractClient> {
	public boolean rejoindre(E client);
}
