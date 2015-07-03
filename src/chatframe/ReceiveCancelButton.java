package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * �˰�ť����ȡ���ļ��Ľ���
 * @author Troyal
 *
 */
public class ReceiveCancelButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
	 */
	ChatFrame window = null;
	ReceiveCancelButton(ChatFrame window) {
		setText(Constant.JBUTTON_FILE_RECEIVE_CANCEL_NAME);
		setBounds(Constant.JBUTTON_FILE_RECEIVE_CANCEL_LOCATION_X,
				Constant.JBUTTON_FILE_CHOOSE_LOCATION_Y,
				Constant.JBUTTON_FILE_CHOOSE_WIDTH,
				Constant.JBUTTON_FILE_CHOOSE_HEIGHT);
		setVisible(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = ReceiveCancelButton.this.window;
			public void actionPerformed(ActionEvent e) {
				if (window.getFileProcess().cancelReceiveFile()) {
					//����ȡ���ļ����յ���Ϣ������Ĳ�����MsgProcess����ʵ��
					window.getMsgProcess().sendMsg(
							process.chat.Constant.MESSAGE_CANCEL_RECEIVE_FILE);
				}
			}
		});
	}
}
