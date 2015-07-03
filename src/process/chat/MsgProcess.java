package process.chat;

import java.io.*;
import java.net.*;
import javax.swing.*;

import process.mainframe.*;
import chatframe.ChatFrame;

/**
 * 消息收发的处理类，包括发送消息、处理接收消息等方法， 扩展了Runnable方法，独占一个线程
 * 
 * @author Troyal
 * 
 */
public class MsgProcess implements Runnable {
	/**
	 * 消息通信的套接字
	 */
	private Socket socket = null;
	/**
	 * 用于写入输出流
	 */
	private OutputStreamWriter outputStreamWriter = null;
	/**
	 * 用于从输入流读入
	 */
	private InputStreamReader inputStreamReader = null;
	/**
	 * 已经收发的所有消息，将显示在Chatframe的textReceived文本面板中
	 */
	private String message = "";
	/**
	 * 当前聊天的窗口框架
	 */
	private ChatFrame window = null;
	/**
	 * 收发消息的socket是否关闭的标志变量
	 */
	private boolean isSocketClosed = false;

	public MsgProcess(Socket socket, ChatFrame window) {
		try {
			this.socket = socket;
			outputStreamWriter = new OutputStreamWriter(
					socket.getOutputStream(), Constant.TEXT_CHARSET);
			inputStreamReader = new InputStreamReader(socket.getInputStream(),
					Constant.TEXT_CHARSET);
			this.window = window;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 获取对方的IP地址
	 * 
	 * @return 与本机建立会话的对方IP地址
	 */
	public String getRequestIP() {
		String requestIP = null;
		try {
			requestIP = socket.getInetAddress().getHostAddress();
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return requestIP;
	}

	/**
	 * 线程启动时调用receiveMsg方法
	 */
	public void run() {
		receiveMsg();
	}

	/**
	 * 发送消息给对方，包括建立、断开连接的消息
	 * 
	 * @param msg
	 *            要发送的消息
	 */
	public void sendMsg(String msg) {
		try {
			char[] buffer = new char[Constant.MAX_BUFFER];
			StringReader strReader = new StringReader(msg);
			int len = 0;
			// 将当前的msg读入缓存数组
			while ((len = strReader.read(buffer)) != -1) {
				outputStreamWriter.write(buffer, 0, len);
				outputStreamWriter.flush();
				if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_CONNECTED)) {
					// 若发送连接成功的消息
					message = message + "连接到"
							+ socket.getInetAddress().getHostName() + "成功！"
							+ Constant.ENTER;
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnMessageSend().setEnabled(true);
					window.getBtnExit().setEnabled(true);
				} else if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_DISCONNECT)) {
					// 若发送断开连接的消息
					message = message + "连接已断开" + Constant.ENTER;
				} else {
					// 普通会话消息
					message = message + "我说：" + msg + Constant.ENTER;
				}
				// 刷新显示消息的文本面板
				window.getTextReceived().setText(message);
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(window,
					Constant.ERROR_MESSAGE_CHATWINDOW_NOT_RUNNING, null,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 接收对方发送的消息，包括建立、断开连接的消息以及文件传输的消息
	 */
	public void receiveMsg() {
		try {
			char[] buffer = new char[Constant.MAX_BUFFER];
			int len = 0;
			// 将输入流信息读入缓存数组
			while (!isSocketClosed
					&& (len = inputStreamReader.read(buffer)) != -1) {
				String msg = new String(buffer, 0, len);
				if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_CONNECTED)) {
					// 若接收连接成功的消息
					message = message + "与"
							+ socket.getInetAddress().getHostName() + "连接成功！"
							+ Constant.ENTER;
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnMessageSend().setEnabled(true);
					window.getBtnExit().setEnabled(true);
				} else if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_CLOSED)) {
					// 若接收对方窗口关闭的消息
					message = message + socket.getInetAddress().getHostName()
							+ " 已经退出" + Constant.ENTER;
					window.getTextReceived().setText(message);
					window.setVisible(true);
					window.getBtnFileChoose().setEnabled(false);
					// 移除IPPortBean中二者的连接信息
					Integer port = Integer.valueOf(window.getPort());
					String localhostIP = Correspond.getLocalAddress();
					IPPortBean.removeIPPortBean(localhostIP, port);
					String requestIP = socket.getInetAddress().getHostAddress();
					Correspond.getChatRunningBean().remove(requestIP);
					// 断开与其的连接
					window.getMsgServer().closeMsgServer();
					window.getFileServer().closeFileServer();
					this.disconnect();
					window.getFileProcess().disconnect();
					break;
				} else if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_DISCONNECT)) {
					// 若接收对方断开连接的消息
					message = message + "与 "
							+ socket.getInetAddress().getHostName()
							+ " 的连接已经断开" + Constant.ENTER;
					window.getTextReceived().setText(message);
					window.setVisible(true);
					break; // 不再接收消息
				} else if (msg.equals(Constant.MESSAGE_CANCEL_SEND_FILE)) {
					// 若接收对方取消文件发送的消息
					window.getBtnFileChoose().setVisible(true);
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnFileSend().setVisible(true);
					window.getBtnFileSend().setEnabled(true);
					window.getTransferProcessBar().setVisible(false);
					window.getTransferProcessBar().setEnabled(false);
					window.getTfdFile().setVisible(true);
					window.getTfdFile().setEditable(true);
					window.getFileProcess().setSendingCancel(true);
					JOptionPane.showMessageDialog(window,
							Constant.ERROR_MESSAGE_FILESEND_CANCELED, null,
							JOptionPane.ERROR_MESSAGE);
					window.getFileProcess().setSendingCancel(false);
				} else if (msg.equals(Constant.MESSAGE_CANCEL_RECEIVE_FILE)) {
					// 若接收对方取消文件接收的消息
					window.getBtnFileChoose().setVisible(true);
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnFileSend().setVisible(true);
					window.getTransferProcessBar().setVisible(false);
					window.getTransferProcessBar().setEnabled(false);
					window.getTfdFile().setVisible(true);
					window.getTfdFile().setEditable(true);
					window.getFileProcess().setReceivingCancel(true);
					JOptionPane.showMessageDialog(window,
							Constant.ERROR_MESSAGE_FILERECEIVE_CANCELED, null,
							JOptionPane.ERROR_MESSAGE);
					window.getFileProcess().setReceivingCancel(false);
				} else {
					// 普通会话消息
					message = message + socket.getInetAddress().getHostName()
							+ " 说 ：" + msg + Constant.ENTER;
				}
				// 刷新显示消息的文本面板
				window.getTextReceived().setText(message);
				window.setVisible(true);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * 断开连接，关闭socket和输入输出流
	 */
	public void disconnect() {
		try {
			isSocketClosed = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStreamWriter.close();
				inputStreamReader.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
