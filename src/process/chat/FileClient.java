package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * �����ļ�����ͨ�ŵ��࣬��Ҫ���ڽ���socket
 * 
 * @author Troyal
 * 
 */
public class FileClient {
	/**
	 * �����ļ�����ͨ�ŵ�socket
	 */
	private Socket server = null;
	/**
	 * ��ǰ��Ϣ���ڵ�frame
	 */
	private ChatFrame window = null;

	public FileClient(ChatFrame window) {
		this.window = window;
	}

	/**
	 * ���������ļ�����ͨ�ŵ�socket
	 * 
	 * @return �����ļ�����ͨ�ŵ�socket
	 */
	public Socket getFileSocket() {
		return this.server;
	}

	/**
	 * ���������ļ�����ͨ�ŵ�socket
	 */
	public void connect() {
		try {
			server = new Socket(window.getIPAddress(), window.getPort() + 1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
