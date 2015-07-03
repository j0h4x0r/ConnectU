package mainframe;

import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import process.mainframe.Correspond;
import process.mainframe.IPPortBean;

/**
 * 此按钮用于发起和选定用户的会话
 * 
 * @author Troyal
 * 
 */
class ChatButton extends JButton {
	private UserTable userTable = null;
	private Socket check = null;

	ChatButton(UserTable userTable) {
		this.userTable = userTable;
		setText(Constant.CHAT_BUTTON_NAME);
		setBounds(Constant.CHAT_BUTTON_LOCATION_X,
				Constant.CHAT_BUTTON_LOCATION_Y, Constant.BUTTON_WIDTH,
				Constant.BUTTON_HEIGHT);
		addActionListener(new ActionListener() {
			UserTable userTable = ChatButton.this.userTable;

			public void actionPerformed(ActionEvent e) {
				// 取得选定用户的信息
				int index = userTable.getSelectedRow();
				String selectedIPAddress = userTable
						.getSelectedUserIPAddress(index);
				String localIPAddress = Correspond.getLocalAddress();
				if (selectedIPAddress == null) {
					return;
				} else {
					// 检查对方是否启动了ConnectU
					try {
						check = new Socket(selectedIPAddress,
								process.mainframe.Constant.RUNNING_CHECK_PORT);
					} catch (IOException ioe) {
						JOptionPane.showMessageDialog(userTable,
								process.mainframe.Constant.MESSAGE_NOT_RUNNING,
								null, JOptionPane.ERROR_MESSAGE);
						return;
					} finally {
						try {
							if (check != null)
								check.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				}

				if (selectedIPAddress.equals(localIPAddress)) {
					// 不允许和自己对话
					JOptionPane.showMessageDialog(userTable,
							process.mainframe.Constant.MSEEAGE_IS_LOCALHOST,
							null, JOptionPane.ERROR_MESSAGE);
				} else if ((Correspond.getChatRunningBean().size() == 0)
						|| !(Correspond.getChatRunningBean()
								.containsKey(selectedIPAddress))) {
					// 发送连接TCP端口的请求
					int connectTCPPort = Correspond.getLocalSuitTCPPort();
					IPPortBean.addIPPortBean(localIPAddress,
							Integer.valueOf(connectTCPPort));
					Correspond.getChatRunningBean()
							.put(selectedIPAddress, true);
					String message = process.mainframe.Constant.UDP_MESSAGE_CONNECT_PORT
							+ String.valueOf(connectTCPPort);
					Correspond.sendUDPMessage(selectedIPAddress, message);
				} else {
					// 会话窗口已经打开
					JOptionPane
							.showMessageDialog(
									userTable,
									process.mainframe.Constant.MESSAGE_CHATWINDOW_IS_RUNNING,
									null, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
