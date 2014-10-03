package interfaces;

import java.awt.Graphics2D;

import vision.Camera;

public interface Localise3D extends LocaliseDessinable {
    public static final float PLANARITE_PLAN = 15f, PLAN_ARR_ARR = -.8f, PLAN_ARR_AV = -.5f, PLAN_AV_ARR = 1.5f, PLAN_AV_AV = 2f, PLAN_MILIEU = 0;


    public void dessine3D(Graphics2D arriere, Graphics2D centre, Graphics2D avant, Camera c);

}
