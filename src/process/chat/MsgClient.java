package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * ������Ϣͨ�ŵ��࣬��Ҫ���ڽ���socket
 * 
 * @author Troyal
 * 
 */
public class MsgClient {
	/**
	 * ������Ϣͨ�ŵ�socket
	 */
	private Socket server = null;
	/**
	 * ��ǰ��Ϣ���ڵ�frame
	 */
	private ChatFrame window = null;

	public MsgClient(ChatFrame window) {
		this.window = window;
	}

	/**
	 * ����������Ϣͨ�ŵ�socket
	 * 
	 * @return ������Ϣͨ�ŵ�socket
	 */
	public Socket getSocket() {
		return this.server;
	}

	/**
	 * ����������Ϣͨ�ŵ�socket
	 */
	public void connect() {
		try {
			server = new Socket(window.getIPAddress(), window.getPort());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
