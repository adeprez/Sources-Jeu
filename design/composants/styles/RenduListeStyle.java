package composants.styles;

import interfaces.StyleListe;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import bordures.BordureImage;

public class RenduListeStyle implements ListCellRenderer<StyleListe> {
	
	
	@Override
	public Component getListCellRendererComponent(JList<? extends StyleListe> list, StyleListe value, int index, boolean isSelected, boolean cellHasFocus) {
		JComponent p = value.creerVue();
		p.setOpaque(false);
		if(isSelected)
			p.setBorder(new BordureImage("trait large.png"));
		return p;
	}
	
}
