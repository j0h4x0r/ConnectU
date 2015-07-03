package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * 此按钮用于取消文件的传送
 * 
 * @author Troyal
 * 
 */
public class SendCancelButton extends JButton {
	/**
	 * 当前会话窗口的frame
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
					// 发送取消文件传送的消息，具体的操作在MsgProcess类中实现
					window.getMsgProcess().sendMsg(
							process.chat.Constant.MESSAGE_CANCEL_SEND_FILE);
			}
		});
	}
}
