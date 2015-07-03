package mainframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * 此按钮显示关于ConnectU作者的信息
 * 
 * @author Troyal
 * 
 */
class AboutButton extends JButton {
	AboutButton() {
		setText(Constant.ABOUT_BUTTON_NAME);
		setBounds(Constant.ABOUT_BUTTON_LOCATION_X,
				Constant.ABOUT_BUTTON_LOCATION_Y, Constant.BUTTON_WIDTH,
				Constant.BUTTON_HEIGHT);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						Constant.MESSAGE_ABOUT_INFORMATION,
						Constant.ABOUT_FRAME_TITLE,
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
}
