package physique;



public class Collision {
    private final Physique source, cible;


    public Collision(Physique source, Physique cible) {
	this.source = source;
	this.cible = cible;
    }

    public Physique getSource() {
	return source;
    }

    public Physique getCible() {
	return cible;
    }

    @Override
    public String toString() {
	return "Collision entre " + source + " et " + cible;
    }
}
