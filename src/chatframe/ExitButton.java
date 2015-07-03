package chatframe;

import javax.swing.*;
import java.awt.event.*;

import process.chat.MsgProcess;
import process.mainframe.*;

/**
 * 此按钮用于退出聊天窗口
 * 
 * @author Troyal
 * 
 */
public class ExitButton extends JButton {
	/**
	 * 当前会话窗口的frame
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
				// 从IPPortBean中删除二者的会话关系
				IPPortBean.removeIPPortBean(localHostIP, port);

				String ip = null;
				if (window.getRequestIP() != null)
					ip = window.getRequestIP();
				else
					ip = window.getIPAddress();
				// 从ChatRunningBean中删除自己的信息
				if (Correspond.getChatRunningBean().containsKey(ip)) {
					window.getMsgProcess().sendMsg(
							process.chat.Constant.MESSAGE_CHAT_WINDOW_CLOSED);
					Correspond.getChatRunningBean().remove(ip);
				}
				// 关闭窗口及所有套接字
				window.dispose();
				window.getMsgServer().closeMsgServer();
				window.getFileServer().closeFileServer();
				window.getMsgProcess().disconnect();
				window.getFileProcess().disconnect();
			}
		});
	}

}
