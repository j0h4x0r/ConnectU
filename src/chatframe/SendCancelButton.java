package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * �˰�ť����ȡ���ļ��Ĵ���
 * 
 * @author Troyal
 * 
 */
public class SendCancelButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
	 */
	ChatFrame window;

	SendCancelButton(ChatFrame window) {
		setText(Constant.JBUTTON_FILE_SEND_CANCEL_NAME);
		setBounds(Constant.JBUTTON_FILE_SEND_CANCEL_LOCATION_X,
				Constant.JBUTTON_FILE_SEND_CANCEL_LOCATION_Y,
				Constant.JBUTTON_FILE_SEND_CANCEL_WIDTH,
				Constant.JBUTTON_FILE_SEND_CANCEL_HEIGHT);
		setVisible(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = SendCancelButton.this.window;

			public void actionPerformed(ActionEvent e) {
				if (window.getFileProcess().cancelSendFile())
					// ����ȡ���ļ����͵���Ϣ������Ĳ�����MsgProcess����ʵ��
					window.getMsgProcess().sendMsg(
							process.chat.Constant.MESSAGE_CANCEL_SEND_FILE);
			}
		});
	}
}
