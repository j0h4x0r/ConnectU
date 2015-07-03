package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * �˰�ť���ڽ��������Ϣ���͸��Է�
 * 
 * @author Troyal
 * 
 */
public class MessageSendButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
	 */
	ChatFrame window = null;

	MessageSendButton(ChatFrame window) {
		setText(Constant.JBUTTON_MESSAGE_SEND_NAME);
		setBounds(Constant.JBUTTON_MESSAGE_SEND_LOCATION_X,
				Constant.JBUTTON_MESSAGE_SEND_LOCATION_Y,
				Constant.JBUTTON_MESSAGE_SEND_WIDTH,
				Constant.JBUTTON_MESSAGE_SEND_HEIGHT);
		setEnabled(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = MessageSendButton.this.window;

			public void actionPerformed(ActionEvent e) {
				// ����MsgProcess��sendMsg����������Ϣ������Ϣ�������Ϊ��
				window.getMsgProcess().sendMsg(window.getTextSend().getText());
				window.getTextSend().setText("");
			}
		});
	}
}
