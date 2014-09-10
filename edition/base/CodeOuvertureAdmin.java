package base;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CodeOuvertureAdmin extends KeyAdapter {
	private final char[] code;
	private int index;
	
	
	public CodeOuvertureAdmin() {
		code = new char[] {'a', 'z', 'e', 'r', 't', 'y'};
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(index >= code.length) {
			index = 0;
			EcranAdministration.ouvrir(true);
		}
		if(code[index] == e.getKeyChar())
			index++;
		else index = 0;
	}

	
	
}
