package mainframe;

import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import process.mainframe.Correspond;
import process.mainframe.IPPortBean;

/**
 * �˰�ť���ڷ����ѡ���û��ĻỰ
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
				// ȡ��ѡ���û�����Ϣ
				int index = userTable.getSelectedRow();
				String selectedIPAddress = userTable
						.getSelectedUserIPAddress(index);
				String localIPAddress = Correspond.getLocalAddress();
				if (selectedIPAddress == null) {
					return;
				} else {
					// ���Է��Ƿ�������ConnectU
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
					// ��������Լ��Ի�
					JOptionPane.showMessageDialog(userTable,
							process.mainframe.Constant.MSEEAGE_IS_LOCALHOST,
							null, JOptionPane.ERROR_MESSAGE);
				} else if ((Correspond.getChatRunningBean().size() == 0)
						|| !(Correspond.getChatRunningBean()
								.containsKey(selectedIPAddress))) {
					// ��������TCP�˿ڵ�����
					int connectTCPPort = Correspond.getLocalSuitTCPPort();
					IPPortBean.addIPPortBean(localIPAddress,
							Integer.valueOf(connectTCPPort));
					Correspond.getChatRunningBean()
							.put(selectedIPAddress, true);
					String message = process.mainframe.Constant.UDP_MESSAGE_CONNECT_PORT
							+ String.valueOf(connectTCPPort);
					Correspond.sendUDPMessage(selectedIPAddress, message);
				} else {
					// �Ự�����Ѿ���
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
