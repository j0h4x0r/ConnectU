package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * 此按钮用于将输入的消息发送给对方
 * 
 * @author Troyal
 * 
 */
public class MessageSendButton extends JButton {
	/**
	 * 当前会话窗口的frame
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
				// 调用MsgProcess的sendMsg方法发送消息并将消息输入框设为空
				window.getMsgProcess().sendMsg(window.getTextSend().getText());
				window.getTextSend().setText("");
			}
		});
	}
}
