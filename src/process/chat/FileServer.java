package process.chat;

import java.io.*;
import java.net.*;

import chatframe.ChatFrame;

/**
 * �������������ڽ����ļ��������ӵ�ServerSocket����չ��Runnable�࣬ռһ���߳�
 * 
 * @author Troyal
 * 
 */
public class FileServer implements Runnable {
	/**
	 * ���ڴ������ļ��������������ServerSocket
	 */
	private ServerSocket serverSocket = null;
	/**
	 * �ڱ�����������Է������ļ������õ��׽��֣���ServerSocket��accept()�����õ�
	 */
	private Socket client = null;
	/**
	 * ��ǰ�Ự���ڵ�frame
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
	 * �رմ�ServerSocket
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
	 * ���������ļ������socket
	 * 
	 * @return �����ļ������socket
	 */
	public Socket getFileSocket() {
		return this.client;
	}

	/**
	 * �߳�����ʱ��ServerSocket���ڼ���״̬�������ļ������������� ���������ͼ�����������
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
