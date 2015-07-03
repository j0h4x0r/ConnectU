package mainframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * �˰�ť��ʾ����ConnectU���ߵ���Ϣ
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
