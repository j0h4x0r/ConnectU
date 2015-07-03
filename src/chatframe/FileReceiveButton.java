package chatframe;

import javax.swing.*;
import java.awt.event.*;

/**
 * �˰�ť���ڿ�ʼ�����ļ�������ʱ������ѡ�񱣴�·���ĶԻ���
 * 
 * @author Troyal
 * 
 */
public class FileReceiveButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
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
				// ����ͼ���������
				window.getTfdFile().setEnabled(false);
				window.getBtnFileChoose().setEnabled(false);
				window.getBtnFileReceive().setVisible(false);
				window.setIsReceive(true);
				// �����ļ������̣߳������Ի����ڴ��߳��ж���
				Thread fileProcessThread = new Thread(window.getFileProcess());
				fileProcessThread.start();
			}
		});
	}
}
