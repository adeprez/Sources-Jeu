package interfaces;

import java.awt.Dimension;

import physique.Collision;
import exceptions.HorsLimiteException;

public interface Localise {
    public static final Dimension UNITE = new Dimension(100, 100);

    public int getX();
    public int getY();
    public Collision setX(int x) throws HorsLimiteException;
    public Collision setY(int y) throws HorsLimiteException;
    public Collision setPos(int x, int y) throws HorsLimiteException;
    public int getLargeur();
    public int getHauteur();
    public Collision setHauteur(int hauteur) throws HorsLimiteException;
    public Collision setLargeur(int largeur) throws HorsLimiteException;

}
