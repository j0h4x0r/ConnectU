package mainframe;

import javax.swing.*;

import process.mainframe.Correspond;

import java.awt.*;
import java.awt.event.*;

/**
 * 此按钮用于添加指定的主机，按下可弹出输入IP的对话框
 * 
 * @author Troyal
 * 
 */
class UserAddButton extends JButton {
	private UserTable userTable = null;

	UserAddButton(UserTable userTable) {
		this.userTable = userTable;
		setText(Constant.USERADD_BUTTON_NAME);
		setBounds(Constant.USERADD_BUTTON_LOCATION_X,
				Constant.USERADD_BUTTON_LOCATION_Y, Constant.BUTTON_WIDTH,
				Constant.BUTTON_HEIGHT);
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 绘制输入IP的对话框
				JPanel addPanel = new JPanel();
				GridLayout layout = new GridLayout(2, 1);
				addPanel.setLayout(layout);
				JLabel labelIP = new JLabel(Constant.ADD_USER_LABEL);
				JTextField inputIP = new JTextField(10);
				addPanel.add(labelIP);
				addPanel.add(inputIP);
				// 将输入的主机添加到用户列表中，并不检查输入的用户是否启动了ConnectU
				int result = JOptionPane.showConfirmDialog(null, addPanel,
						Constant.ADD_USER_TITLE, JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					String userIP = inputIP.getText();
					String userName = Constant.UNKNOWN_USERNAME;
					if (Correspond.isIPRight(userIP))
						if (!UserAddButton.this.userTable.addUser(userName,
								userIP))
							JOptionPane.showMessageDialog(null,
									Constant.MESSAGE_USER_EXIST, null,
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
