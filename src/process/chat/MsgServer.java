package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * 创建并监听用于建立会话连接的ServerSocket，扩展了Runnable类，占一个线程
 * 
 * @author Troyal
 * 
 */
public class MsgServer implements Runnable {
	/**
	 * 用于处理建立会话连接请求的ServerSocket
	 */
	private ServerSocket serverSocket = null;
	/**
	 * 在本机建立的与对方进行会话用的套接字，由ServerSocket的accept()方法得到
	 */
	private Socket client = null;
	/**
	 * 当前会话窗口的frame
	 */
	private ChatFrame window = null;

	public MsgServer(ChatFrame window) {
		try {
			serverSocket = new ServerSocket(window.getPort());
			this.window = window;
		} catch (Exception e) {
			try {
				serverSocket.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 关闭此ServerSocket
	 */
	public void closeMsgServer() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * 返回用于消息通信的socket
	 * 
	 * @return 用于会话的socket
	 */
	public Socket getSocket() {
		return this.client;
	}

	/**
	 * 线程启动时，ServerSocket处于监听状态，处理会话连接请求
	 */
	public void run() {
		try {
			while (true) {
				client = serverSocket.accept();
				MsgProcess msgProcess = new MsgProcess(client, window);
				window.setMsgProcess(msgProcess);
				msgProcess.receiveMsg();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
