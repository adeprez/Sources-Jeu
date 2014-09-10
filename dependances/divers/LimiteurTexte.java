package divers;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimiteurTexte extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private final int max;


	public LimiteurTexte(int max){
		this.max = max;
	}
	
	@Override
	public void insertString(int offset, String str, AttributeSet attrs) throws BadLocationException {
		if(str != null && getLength() + str.length() > max)
			Toolkit.getDefaultToolkit().beep();
		else super.insertString(offset, str, attrs);
	}

	@Override
	public void replace(int offset, int length, String str, AttributeSet attrs)	throws BadLocationException {
		if(str != null && getLength() + str.length() - length > max)
			Toolkit.getDefaultToolkit().beep();
		else super.replace(offset, length, str, attrs);
	}
	
}
