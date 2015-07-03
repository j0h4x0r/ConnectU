package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * 创建并监听用于建立文件传输连接的ServerSocket，扩展了Runnable类，占一个线程
 * 
 * @author Troyal
 * 
 */
public class FileServer implements Runnable {
	/**
	 * 用于处理建立文件传输连接请求的ServerSocket
	 */
	private ServerSocket serverSocket = null;
	/**
	 * 在本机建立的与对方进行文件传输用的套接字，由ServerSocket的accept()方法得到
	 */
	private Socket client = null;
	/**
	 * 当前会话窗口的frame
	 */
	ChatFrame window = null;

	public FileServer(ChatFrame window) {
		try {
			serverSocket = new ServerSocket(window.getPort() + 1);
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
	public void closeFileServer() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返回用于文件传输的socket
	 * 
	 * @return 用于文件传输的socket
	 */
	public Socket getFileSocket() {
		return this.client;
	}

	/**
	 * 线程启动时，ServerSocket处于监听状态，处理文件传输连接请求， 并设置相关图形组件的属性
	 */
	public void run() {
		try {
			while (true) {
				client = serverSocket.accept();
				FileProcess fileProcess = new FileProcess(client, window);
				window.setFileProcess(fileProcess);
				window.getBtnFileSend().setVisible(false);
				window.getBtnFileChoose().setEnabled(false);
				window.getTfdFile().setEnabled(false);
				window.getTfdFile().setText("");
				window.getBtnSendCancel().setVisible(false);
				window.getBtnSendCancel().setEnabled(false);
				window.getBtnReceiveCancel().setVisible(false);
				window.getBtnReceiveCancel().setEnabled(false);
				window.getBtnFileReceive().setVisible(true);
				window.getBtnFileReceive().setEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
