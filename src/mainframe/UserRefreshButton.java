package mainframe;

import javax.swing.*;
import java.awt.event.*;

import process.mainframe.Correspond;

/**
 * 此按钮用于刷新当前用户列表
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
				// 重新向所有人广播自己启动ConnectU的消息
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
