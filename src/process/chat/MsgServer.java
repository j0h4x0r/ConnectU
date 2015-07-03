package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * �������������ڽ����Ự���ӵ�ServerSocket����չ��Runnable�࣬ռһ���߳�
 * 
 * @author Troyal
 * 
 */
public class MsgServer implements Runnable {
	/**
	 * ���ڴ������Ự���������ServerSocket
	 */
	private ServerSocket serverSocket = null;
	/**
	 * �ڱ�����������Է����лỰ�õ��׽��֣���ServerSocket��accept()�����õ�
	 */
	private Socket client = null;
	/**
	 * ��ǰ�Ự���ڵ�frame
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
	 * �رմ�ServerSocket
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
	 * ����������Ϣͨ�ŵ�socket
	 * 
	 * @return ���ڻỰ��socket
	 */
	public Socket getSocket() {
		return this.client;
	}

	/**
	 * �߳�����ʱ��ServerSocket���ڼ���״̬������Ự��������
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
