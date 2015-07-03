package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * 用于文件传输通信的类，主要用于建立socket
 * 
 * @author Troyal
 * 
 */
public class FileClient {
	/**
	 * 用于文件传输通信的socket
	 */
	private Socket server = null;
	/**
	 * 当前消息窗口的frame
	 */
	private ChatFrame window = null;

	public FileClient(ChatFrame window) {
		this.window = window;
	}

	/**
	 * 返回用于文件传输通信的socket
	 * 
	 * @return 用于文件传输通信的socket
	 */
	public Socket getFileSocket() {
		return this.server;
	}

	/**
	 * 创建用于文件传输通信的socket
	 */
	public void connect() {
		try {
			server = new Socket(window.getIPAddress(), window.getPort() + 1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
