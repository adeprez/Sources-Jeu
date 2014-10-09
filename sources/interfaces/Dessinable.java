package interfaces;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public interface Dessinable {
    public void predessiner(Graphics2D g, Rectangle zone, int equipe);
    public void dessiner(Graphics2D g, Rectangle zone, int equipe);
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe);
}
