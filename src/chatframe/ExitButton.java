package chatframe;

import javax.swing.*;
import java.awt.event.*;

import process.chat.MsgProcess;
import process.mainframe.*;

/**
 * �˰�ť�����˳����촰��
 * 
 * @author Troyal
 * 
 */
public class ExitButton extends JButton {
	/**
	 * ��ǰ�Ự���ڵ�frame
	 */
	ChatFrame window = null;

	ExitButton(ChatFrame window) {
		setText(Constant.JBUTTON_CHAT_WINDOW_EXIT_NAME);
		setBounds(Constant.JBUTTON_CHAT_WINDOW_EXIT_LOCATION_X,
				Constant.JBUTTON_CHAT_WINDOW_EXIT_LOCATION_Y,
				Constant.JBUTTON_CHAT_WINDOW_EXIT_WIDTH,
				Constant.JBUTTON_CHAT_WINDOW_EXIT_HEIGHT);
		setEnabled(false);
		this.window = window;
		addActionListener(new ActionListener() {
			ChatFrame window = ExitButton.this.window;

			public void actionPerformed(ActionEvent e) {
				Integer port = Integer.valueOf(window.getPort());
				String localHostIP = Correspond.getLocalAddress();
				// ��IPPortBean��ɾ�����ߵĻỰ��ϵ
				IPPortBean.removeIPPortBean(localHostIP, port);

				String ip = null;
				if (window.getRequestIP() != null)
					ip = window.getRequestIP();
				else
					ip = window.getIPAddress();
				// ��ChatRunningBean��ɾ���Լ�����Ϣ
				if (Correspond.getChatRunningBean().containsKey(ip)) {
					window.getMsgProcess().sendMsg(
							process.chat.Constant.MESSAGE_CHAT_WINDOW_CLOSED);
					Correspond.getChatRunningBean().remove(ip);
				}
				// �رմ��ڼ������׽���
				window.dispose();
				window.getMsgServer().closeMsgServer();
				window.getFileServer().closeFileServer();
				window.getMsgProcess().disconnect();
				window.getFileProcess().disconnect();
			}
		});
	}

}
