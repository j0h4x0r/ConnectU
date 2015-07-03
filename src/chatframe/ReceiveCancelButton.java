package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * 此按钮用于取消文件的接收
 * @author Troyal
 *
 */
public class ReceiveCancelButton extends JButton {
	/**
	 * 当前会话窗口的frame
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
					//发送取消文件接收的消息，具体的操作在MsgProcess类中实现
					window.getMsgProcess().sendMsg(
							process.chat.Constant.MESSAGE_CANCEL_RECEIVE_FILE);
				}
			}
		});
	}
}
