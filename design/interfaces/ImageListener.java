package interfaces;

import java.awt.image.BufferedImage;
import java.util.EventListener;

public interface ImageListener extends EventListener {
	public void change(int id, BufferedImage image);
}
