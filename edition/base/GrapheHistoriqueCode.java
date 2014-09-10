package base;

import io.IO;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import ressources.Fichiers;
import statique.Style;
import divers.Outil;

public class GrapheHistoriqueCode extends Ecran implements ActionListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private static final int COEF_CONTOURS = 40;
	private final JCheckBox lignes, classes, moy;
	private final List<JourCode> codes;
	private int pX;


	public GrapheHistoriqueCode() {
		super("fond/parchemin.jpg");
		setLayout(new FlowLayout());
		setName("Historique");
		codes = new ArrayList<JourCode>();
		IO io = Fichiers.lire(HistoriqueCode.PATH);
		while(io.aBytes(16))
			codes.add(new JourCode(io));
		lignes = new JCheckBox("Lignes", true);
		lignes.addActionListener(this);
		classes = new JCheckBox("Classes", true);
		classes.addActionListener(this);
		moy = new JCheckBox("Moyenne", true);
		moy.addActionListener(this);
		add(lignes);
		add(classes);
		add(moy);
		setPreferredSize(new Dimension(300, 300));
		setBorder(BorderFactory.createLoweredBevelBorder());
		addMouseMotionListener(this);
	}

	public void dessiner(Graphics g, Rectangle zone, long minX, int minY, long maxX, int maxY, boolean l) {
		Graphics2D g2d = (Graphics2D) g;
		Point precedent = new Point(zone.x, zone.y + zone.height);
		g2d.setStroke(new BasicStroke(3f));
		Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f);
		int[] px = new int[codes.size() + 2], py = new int[px.length];
		for(int i=0 ; i<codes.size() ; i++) {
			JourCode j = codes.get(i);
			g.setColor(l ? Color.BLUE : Color.RED);
			int h = ((j.getVal(l) - minY) * zone.height)/Math.max(1, maxY);
			int x = (int) (((j.t - minX) * zone.width)/Math.max(1, maxX));
			Point p = new Point(zone.x + x, zone.y + zone.height - h);
			px[i] = p.x;
			py[i] = p.y;
			g.fillRect(p.x, p.y, 1, h);
			if((l || !lignes.isSelected()) && Math.max(p.x, pX) - Math.min(p.x, pX) < 2)
				dessine(g2d, j);
			Composite tmp = g2d.getComposite();
			g2d.setComposite(c);
			g.drawLine(precedent.x, precedent.y, p.x, p.y);
			g2d.setComposite(tmp);
			precedent = p;
		}
		px[px.length - 2] = zone.x + zone.width;
		py[px.length - 2] = zone.y + zone.height;
		px[px.length - 1] = zone.x;
		py[px.length - 1] = zone.y + zone.height;
		g.setColor(l ? Color.BLUE : Color.RED);
		Composite tmp = g2d.getComposite();
		g2d.setComposite(Style.TRANSPARENCE);
		g.fillPolygon(px, py, px.length);
		g2d.setComposite(tmp);
	}

	public void dessiner(Graphics g, boolean l) {
		int minY = Integer.MAX_VALUE, maxY = 0;
		long minX = Long.MAX_VALUE, maxX = 0;
		for(final JourCode j : codes) {
			if(j.getVal(l) < minY)
				minY = j.getVal(l);
			else if(j.getVal(l) > maxY)
				maxY = j.getVal(l);
			if(j.t < minX)
				minX = j.t;
			else if(j.t > maxX)
				maxX = j.t;
		}
		dessiner(g, getRect(), minX, minY, maxX - minX, maxY - minY, l);
	}

	public Rectangle getRect() {
		int w = getWidth(), h = getHeight();
		return new Rectangle(w/COEF_CONTOURS, h/COEF_CONTOURS, w - w/(COEF_CONTOURS/2), h - h/(COEF_CONTOURS/2) - 30);
	}

	public void dessineMois(Graphics g) {
		long min = Long.MAX_VALUE, max = 0;
		for(final JourCode j : codes)
			if(j.t < min)
				min = j.t;
			else if(j.t > max)
				max = j.t;
		Rectangle r = getRect();
		int d = (int) (r.width/getNombreMois(min, max));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(min);
		for(int i=r.x ; i<=r.x + r.width ; i+=d) {
			g.setColor(Color.WHITE);
			g.fillRect(i, 0, 1, getHeight());
			g.setColor(Color.BLACK);
			String s = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()),
					s2 = c.get(Calendar.YEAR) + "";
			g.drawString(s, i + (d - g.getFontMetrics().stringWidth(s))/2, getHeight() - 25);
			g.drawString(s2, i + (d - g.getFontMetrics().stringWidth(s2))/2, getHeight() - 8);
			c.add(Calendar.MONTH, 1);
		}
		g.setColor(Color.BLACK);
		g.drawString(codes.size() + " fois depuis " + (max - min)/1000/60/60/24 + " jours", 10, 20);
		g.fillRect(0, r.y + r.height - 1, getWidth(), 3);
	}

	private static void dessine(Graphics g, JourCode j) {
		Color tmp = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRoundRect(-10, 25, 130, 65, 25, 20);
		g.setColor(Color.WHITE);
		g.drawString(Outil.formatCourtJour(new Date(j.t)), 15, 40);
		g.drawString(j.classes + " classes", 15, 60);
		g.drawString(j.lignes + " lignes", 15, 80);
		g.setColor(tmp);
	}

	private static double getNombreMois(long min, long max) {
		Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
		double m = 0;
		c1.setTimeInMillis(min);
		c2.setTimeInMillis(max);
		while(c1.before(c2)) {
			c1.add(Calendar.MONTH, 1);
			m++;
		}
		return Math.max(1, m - 1 + c2.get(Calendar.DAY_OF_MONTH)/30.0);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(Style.POLICE);
		dessineMois(g);
		if(classes.isSelected())
			dessiner(g, false);
		if(lignes.isSelected())
			dessiner(g, true);
		if(moy.isSelected()) {
			g.setColor(new Color(255, 255, 0, 100));
			Rectangle r = getRect();
			g.drawLine(r.x, r.y + r.height, r.x + r.width, r.y);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		pX = e.getX();
		repaint(0, 25, 130, 65);
	}

	private class JourCode {
		private final int classes, lignes;
		private final long t;


		public JourCode(long t, int classes, int lignes) {
			this.t = t;
			this.classes = classes;
			this.lignes = lignes;
		}

		public JourCode(IO io) {
			this(io.nextLong(), io.nextInt(), io.nextInt());
		}

		public int getVal(boolean l) {
			return l ? lignes : classes;
		}

	}

}
