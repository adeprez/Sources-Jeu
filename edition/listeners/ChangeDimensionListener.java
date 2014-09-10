package listeners;

import java.util.EventListener;

import physique.forme.Rect;

public interface ChangeDimensionListener extends EventListener {
	public void changeDimension(Rect taille);
}
