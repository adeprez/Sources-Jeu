package ressources.sprites.animation.sequence;

import interfaces.Sauvegardable;
import io.IO;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.Membre;
import divers.Outil;

public class EtapeSequence implements Sauvegardable {
	private static final int ANGLE_MAX = 360;
	private final float[] angles;
	private boolean inverseSens;
	private int dX, dY;

	
	public EtapeSequence(float[] angles) {
		this.angles = angles;
	}
	
	public EtapeSequence(int taille) {
		this(new float[taille]);
	}

	public EtapeSequence(int taille, IO io) {
		this(taille);
		for(int i=0 ; i<angles.length ; i++)
			angles[i] = (float) Math.toRadians(Outil.getValeur(io.nextPositif(), ANGLE_MAX, IO.LIMITE_BYTE_POSITIF));
		dX = io.next() * 2;
		dY = io.next() * 2;
		inverseSens = io.nextBoolean();
	}
	
	public EtapeSequence(EtapeSequence source) {
		this(source.angles.clone());
		dX = source.dX;
		dY = source.dY;
		inverseSens = source.inverseSens;
	}
	
	public EtapeSequence(EtapeSequence e1, EtapeSequence e2) {
		this(entre(e1, e2));
		dX = (e1.dX + e2.dX)/2;
		dY = (e1.dY + e2.dY)/2;
		inverseSens = e1.inverseSens || e2.inverseSens;
	}

	public void setInverseSens(boolean inverseSens) {
		this.inverseSens = inverseSens;
	}
	
	public boolean inverseSens() {
		return inverseSens;
	}
	
	public void decalage(Animation anim) {
		anim.setInverse(inverseSens);
		anim.setDecalageX(dX);
		anim.setDecalageY(dY);
	}

	public void effet(DetermineurAngle angle, Animation anim) {
		decalage(anim);
		Membre[] membres = anim.getMembres();
		for(int i=0 ; i<angles.length ; i++)
			membres[i].setAngle(angle.getAngle(i, angles));
	}
	
	public void effetPuisFutur(DetermineurAngle angle, Animation anim, int etapesToAngle, EtapeSequence suivant) {
		decalage(anim);
		Membre[] membres = anim.getMembres();
		for(int i=0 ; i<angles.length ; i++) {
			Membre m = membres[i];
			m.setAngle(angle.getAngle(i, angles));
			m.setAngleFutur(angle.getAngle(i, suivant.angles), etapesToAngle);
			m.versAngle();
		}
	}

	public int getdX() {
		return dX;
	}

	public void setdX(int dX) {
		this.dX = dX;
	}

	public int getdY() {
		return dY;
	}

	public void setdY(int dY) {
		this.dY = dY;
	}

	public float[] getAngles() {
		return angles;
	}

	public void incrAngle(int membre, int incr) {
		setAngle(membre, Math.toDegrees(angles[membre]) + incr);
	}

	public void setAngle(int rang, double angle) {
		while(angle < 0)
			angle += 360;
		angles[rang] = (float) Math.toRadians(angle % 360);
	}

	@Override
	public IO sauvegarder(IO io) {
		for(final float f : angles)
			io.addBytePositif((int) Outil.getPourcentage((float) Math.toDegrees(f), ANGLE_MAX, IO.LIMITE_BYTE_POSITIF));
		return io.addByte(dX/2).addByte(dY/2).add(inverseSens);
	}
	
	public static float[] entre(EtapeSequence e1, EtapeSequence e2) {
		float[] f = new float[e1.angles.length];
		for(int i=0 ; i<f.length ; i++) {
			float d =  e1.angles[i] - e2.angles[i];
			if(d > Math.PI)
				d -= Math.PI * 2;
			else if(d < -Math.PI)
				d += Math.PI * 2;
			f[i] = e1.angles[i] - Membre.angle(d/2);
		}
		return f;
	}
	

}
