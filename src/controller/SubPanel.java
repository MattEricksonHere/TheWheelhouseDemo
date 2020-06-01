package controller;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public abstract class SubPanel extends JPanel {
	private static final long serialVersionUID = -6836845790644938163L;

	public SubPanel(String title) {
		LineBorder lBorder = new LineBorder(Color.DARK_GRAY, 2);
		
		TitledBorder tBorder = new TitledBorder(lBorder, title);
		tBorder.setTitlePosition(TitledBorder.BELOW_TOP);
		tBorder.setTitleJustification(TitledBorder.LEFT);
		tBorder.setTitleColor(UIBot.FGColor1);
		setBorder(tBorder);
	}
	
}
