package mainframe;

import javax.swing.*;

import process.mainframe.Correspond;

import java.awt.event.*;

/**
 * 此按钮用于退出ConnectU
 * 
 * @author Troyal
 * 
 */
class ExitButton extends JButton {
	ExitButton() {
		setText(Constant.EXIT_BUTTON_NAME);
		setBounds(Constant.EXIT_BUTTON_LOCATION_X,
				Constant.EXIT_BUTTON_LOCATION_Y, Constant.BUTTON_WIDTH,
				Constant.BUTTON_HEIGHT);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 广播UDP消息，告知所有人自己已退出
				String localHostIP = Correspond.getLocalAddress();
				String localHostName = Correspond.getLocalHostName();
				String message = process.mainframe.Constant.MESSAGE_BROADCAST_LOGOFF
						+ ":" + localHostName + ":" + localHostIP;
				Correspond.sendUDPMessage(
						process.mainframe.Constant.BROADCAST_ADDRESS, message);
				System.exit(0);
			}
		});
	}
}
