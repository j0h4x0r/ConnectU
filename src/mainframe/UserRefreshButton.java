package mainframe;

import javax.swing.*;
import java.awt.event.*;

import process.mainframe.Correspond;

/**
 * �˰�ť����ˢ�µ�ǰ�û��б�
 * 
 * @author Troyal
 * 
 */
class UserRefreshButton extends JButton {
	UserRefreshButton() {
		setText(Constant.USERREFRESH_BUTTON_NAME);
		setBounds(Constant.USERREFRESH_BUTTON_LOCATION_X,
				Constant.USERREFRESH_BUTTON_LOCATION_Y, Constant.BUTTON_WIDTH,
				Constant.BUTTON_HEIGHT);

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �����������˹㲥�Լ�����ConnectU����Ϣ
				String localHostName = Correspond.getLocalHostName();
				String localHostAddress = Correspond.getLocalAddress();
				String message = process.mainframe.Constant.MESSAGE_BROADCAST_LOGIN
						+ ":" + localHostName + ":" + localHostAddress;
				Correspond.sendUDPMessage(
						process.mainframe.Constant.BROADCAST_ADDRESS, message);
			}
		});
	}
}
