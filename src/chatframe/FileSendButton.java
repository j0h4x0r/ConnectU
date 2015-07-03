package chatframe;

import javax.swing.*;
import java.awt.event.*;

import process.chat.*;

/**
 * �˰�ť���ڿ�ʼ�����ļ�
 * 
 * @author Troyal
 * 
 */
public class FileSendButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
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
				// ���tfdFile�е�·��Ϊ�գ�����ʾ����
				if (window.getTfdFile().getText() == null
						|| window.getTfdFile().getText().equals("")) {
					JOptionPane.showMessageDialog(window,
							Constant.ERROR_MESSAGE_FILE_NULL, null,
							JOptionPane.ERROR_MESSAGE);
				} else {
					// ����ͼ���������
					window.getBtnFileSend().setVisible(false);
					window.getTfdFile().setEnabled(false);
					window.getBtnSendCancel().setVisible(true);
					window.getBtnSendCancel().setEnabled(true);
					window.getBtnFileChoose().setEnabled(false);
					// ������������
					window.getFileClient().connect();
					window.setFileProcess(new FileProcess(window
							.getFileClient().getFileSocket(), window));
					window.setIsReceive(false);
					// ���������߳�
					Thread fileProcessThread = new Thread(
							window.getFileProcess());
					fileProcessThread.start();
				}
			}
		});
	}
}
