package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * 此按钮用于开始接收文件，按下时将弹出选择保存路径的对话框
 * 
 * @author Troyal
 * 
 */
public class FileReceiveButton extends JButton {
	/**
	 * 当前会话窗口的frame
	 */
	ChatFrame window = null;

	FileReceiveButton(ChatFrame window) {
		setText(Constant.JBUTTON_FILE_RECEIVE_NAME);
		setBounds(Constant.JBUTTON_FILE_RECEIVE_LOCATION_X,
				Constant.JBUTTON_FILE_RECEIVE_LOCATION_Y,
				Constant.JBUTTON_FILE_RECEIVE_WIDTH,
				Constant.JBUTTON_FILE_RECEIVE_HEIGHT);
		setVisible(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = FileReceiveButton.this.window;

			public void actionPerformed(ActionEvent e) {
				// 设置图形组件属性
				window.getTfdFile().setEnabled(false);
				window.getBtnFileChoose().setEnabled(false);
				window.getBtnFileReceive().setVisible(false);
				window.setIsReceive(true);
				// 创建文件传输线程，弹出对话框将在此线程中定义
				Thread fileProcessThread = new Thread(window.getFileProcess());
				fileProcessThread.start();
			}
		});
	}
}
