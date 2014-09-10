package tests;
import static org.junit.Assert.assertEquals;
import io.IO;
import map.Map;

import org.junit.Test;

import perso.Perso;
import reseau.paquets.jeu.PaquetPosition;
import ressources.compte.Compte;
import exceptions.HorsLimiteException;


@SuppressWarnings("static-method")
public class TestPerso {

	
	@Test
	public void testPerso() throws HorsLimiteException {
		Map m = Map.getAllMaps().get(0);
		Perso p = Compte.getComptes()[0].getPersos()[0];
		p.setEquipe(2);
		p.setMap(m);
		try {
			p.setPos(100, 1000);
		} catch(Exception e) {
			e.printStackTrace();
		}
		IO io = new IO();
		p.ecrireDonnees(io);
		PaquetPosition.ecrire(io, p);
		Perso p2 = new Perso(io.nextShortString(), io, io, io, io);
		p2.setEquipe(io.nextPositif());
		p2.setMap(m);
		PaquetPosition.effet(io, p2);
		
		assertEquals(p.getNomCompte(), p2.getNomCompte());
		assertEquals(p.getNom(), p2.getNom());
		assertEquals(p.getCaract().toString(), p.getCaract().toString());
		assertEquals(p.getCouleur(), p2.getCouleur());
		assertEquals(p.getDirection(), p2.getDirection());
		assertEquals(p.getAngle(), p2.getAngle());
		assertEquals(p.getEquipe(), p2.getEquipe());
		assertEquals(p.getInfos().toString(), p2.getInfos().toString());
		assertEquals(p.getForme().toString(), p2.getForme().toString());
		assertEquals(p.getX(), p2.getX());
		assertEquals(p.getY(), p2.getY());
	}
	
}
