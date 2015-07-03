package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * 用于消息通信的类，主要用于建立socket
 * 
 * @author Troyal
 * 
 */
public class MsgClient {
	/**
	 * 用于消息通信的socket
	 */
	private Socket server = null;
	/**
	 * 当前消息窗口的frame
	 */
	private ChatFrame window = null;

	public MsgClient(ChatFrame window) {
		this.window = window;
	}

	/**
	 * 返回用于消息通信的socket
	 * 
	 * @return 用于消息通信的socket
	 */
	public Socket getSocket() {
		return this.server;
	}

	/**
	 * 创建用于消息通信的socket
	 */
	public void connect() {
		try {
			server = new Socket(window.getIPAddress(), window.getPort());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
