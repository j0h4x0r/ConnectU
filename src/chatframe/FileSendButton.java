package chatframe;

import javax.swing.*;
import java.awt.event.*;

import process.chat.*;

/**
 * 此按钮用于开始传送文件
 * 
 * @author Troyal
 * 
 */
public class FileSendButton extends JButton {
	/**
	 * 当前会话窗口的frame
	 */
	ChatFrame window = null;

	FileSendButton(ChatFrame window) {
		setText(Constant.JBUTTON_FILE_NAME);
		setBounds(Constant.JBUTTON_FILE_SEND_LOCATION_X,
				Constant.JBUTTON_FILE_SEND_LOCATION_Y,
				Constant.JBUTTON_FILE_SEND_WIDTH,
				Constant.JBUTTON_FILE_SEND_HEIGHT);
		setEnabled(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = FileSendButton.this.window;

			public void actionPerformed(ActionEvent e) {
				// 如果tfdFile中的路径为空，则提示错误
				if (window.getTfdFile().getText() == null
						|| window.getTfdFile().getText().equals("")) {
					JOptionPane.showMessageDialog(window,
							Constant.ERROR_MESSAGE_FILE_NULL, null,
							JOptionPane.ERROR_MESSAGE);
				} else {
					// 设置图形组件属性
					window.getBtnFileSend().setVisible(false);
					window.getTfdFile().setEnabled(false);
					window.getBtnSendCancel().setVisible(true);
					window.getBtnSendCancel().setEnabled(true);
					window.getBtnFileChoose().setEnabled(false);
					// 发出传输请求
					window.getFileClient().connect();
					window.setFileProcess(new FileProcess(window
							.getFileClient().getFileSocket(), window));
					window.setIsReceive(false);
					// 启动传输线程
					Thread fileProcessThread = new Thread(
							window.getFileProcess());
					fileProcessThread.start();
				}
			}
		});
	}
}
