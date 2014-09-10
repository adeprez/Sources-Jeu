package vision;

import java.util.HashSet;
import java.util.Set;

import listeners.ChangeCaseListener;
import map.Map;
import map.objets.Objet;
import exceptions.HorsLimiteException;

public class RevelateurObjets implements ChangeCaseListener {
	private final Map map;
	private Set<Objet> visibles;


	public RevelateurObjets(Map map) {
		this.map = map;
	}

	public void setOpaque(boolean opaque) {
		if(visibles != null)
			for(final Objet o : visibles)
				o.setOpaque(opaque);
	}

	public boolean dansZone(int x, int y) {
		if(visibles == null)
			return false;
		for(final Objet o : visibles)
			if(o.getXMap() == x && o.getYMap() == y)
				return true;
		return false;
	}

	public boolean expension(int x, int y) {
		try {
			Objet o = map.getObjet(x, y);
			if(o.aFond() && visibles.add(o)) {
				o.setOpaque(false);
				expension(x + 1, y);
				expension(x - 1, y);
				expension(x, y + 1);
				expension(x, y - 1);
				return true;
			}
		} catch(HorsLimiteException e) {}
		return false;
	}
	
	public void changeZone(int x, int y) {
		setOpaque(true);
		visibles = new HashSet<Objet>();
		if(!expension(x, y))
			expension(x, y + 1);
	}

	@Override
	public void changeCase(int x, int y, int nX, int nY) {
		if(!dansZone(nX, nY + 1))
			changeZone(nX, nY);
	}

}
