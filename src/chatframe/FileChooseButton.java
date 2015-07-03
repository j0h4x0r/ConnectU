package chatframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * �˰�ť����ѡ��Ҫ���͵��ļ�������ʱ������ѡ���ļ��Ի���
 * 
 * @author Troyal
 * 
 */
public class FileChooseButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
	 */
	ChatFrame window = null;

	FileChooseButton(ChatFrame window) {
		setText(Constant.JBUTTON_FILE_CHOOSE_FILE);
		setBounds(Constant.JBUTTON_FILE_CHOOSE_LOCATION_X,
				Constant.JBUTTON_FILE_CHOOSE_LOCATION_Y,
				Constant.JBUTTON_FILE_CHOOSE_WIDTH,
				Constant.JBUTTON_FILE_CHOOSE_HEIGHT);
		setEnabled(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = FileChooseButton.this.window;

			public void actionPerformed(ActionEvent e) {
				window.getTfdFile().setText("");
				// ����ѡ��Ի��򲢽��ļ�·������tfdFile�ı���
				FileDialog fileDialog = new FileDialog(window,
						Constant.CONFIRM_MESSAGE_FILE_SEND, FileDialog.LOAD);
				fileDialog.setVisible(true);
				String filePath = fileDialog.getDirectory();
				String fileName = fileDialog.getFile();
				if ((filePath != null) && (fileName != null)
						&& !(filePath.indexOf(File.separator) == -1)) {
					window.getTfdFile().setText(
							filePath + File.separator + fileName);
					window.getBtnFileSend().setEnabled(true);
				} else {
					window.getTfdFile().setText("");
				}
			}
		});
	}
}
