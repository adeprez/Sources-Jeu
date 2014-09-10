package fichiers;

import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import listeners.SelectionFichierListener;
import ressources.RessourcesLoader;

import composants.MenuContextuel;

public class ArbreFichiers extends JTree implements TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	private final File racine;
	private MenuContextuel menu;


	public ArbreFichiers(String lien) {
		this(RessourcesLoader.getFichier(lien, false));
	}

	public ArbreFichiers(File racine) {
		super(getTree(new DefaultMutableTreeNode(racine), racine));
		this.racine = racine;
		addTreeSelectionListener(this);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setToggleClickCount(1);
	}

	public void addSelectionFichierListener(SelectionFichierListener l) {
		listenerList.add(SelectionFichierListener.class, l);
	}

	public void removeSelectionFichierListener(SelectionFichierListener l) {
		listenerList.remove(SelectionFichierListener.class, l);
	}
	
	public void actualise() {
		setModel(new DefaultTreeModel(getTree(new DefaultMutableTreeNode(racine), racine)));
	}
	
	public MenuContextuel getMenu() {
		return menu;
	}
	
	public void setMenu(MenuContextuel menu) {
		this.menu = menu;
		setComponentPopupMenu(menu);
	}

	private void notifySelectionFichierListener(File f) {
		if(f.isFile())
			for(final SelectionFichierListener l : listenerList.getListeners(SelectionFichierListener.class))
				l.selection(f);
	}

	private static DefaultMutableTreeNode getTree(DefaultMutableTreeNode d, File fichier) {
		if(fichier.isFile())
			return new DefaultMutableTreeNode(fichier, true);
		for(final File f : fichier.listFiles()) {
			DefaultMutableTreeNode sousNoeud;
			if(f.isDirectory()) {
				sousNoeud = new DefaultMutableTreeNode(f.getName(), true);
				d.add(getTree(sousNoeud, f));
			} 
			else sousNoeud = new DefaultMutableTreeNode(f.getName(), false);
			d.add(sousNoeud);
		}
		return d;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath tp = e.getNewLeadSelectionPath();
		if(tp != null && tp.getPathCount() > 1) {
			Object[] o = tp.getPath();
			String nom = o[1].toString();
			for(int i=2 ; i<o.length ; i++)
				nom += '/' + o[i].toString();
			notifySelectionFichierListener(RessourcesLoader.getFichier(nom, false));
		}
	}

}
