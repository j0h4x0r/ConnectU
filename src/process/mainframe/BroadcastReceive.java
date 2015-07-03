package process.mainframe;

import java.io.*;
import java.net.*;
import java.util.*;

import mainframe.UserTable;

/**
 * ����UDP��Ϣ����ռһ���̡߳� ������ȡ�������û����UDP��Ϣ�ȷ����� �йضԽ��յ���UDP��Ϣ�Ĵ���ķ�����Correspond����
 * 
 * @author Troyal
 * 
 */
public class BroadcastReceive implements Runnable {
	/**
	 * ������UDP�׽���
	 */
	private DatagramSocket socket = null;
	/**
	 * ���յ������ݱ�
	 */
	private static DatagramPacket receivedPacket = null;
	/**
	 * �յ�UDP��Ϣ���û��б�ͨ���������û��б�������ͬ
	 */
	private static List<String> receivedList = new ArrayList<String>();
	/**
	 * �����û��ı��
	 */
	private static UserTable userTable = null;
	/**
	 * �����ݱ��еõ�����Ҫ��UDP��Ϣ
	 */
	private static String udpMessage = null;

	public BroadcastReceive(DatagramSocket socket, UserTable userTable) {
		this.socket = socket;
		this.userTable = userTable;
		byte[] receivedBuffer = new byte[Constant.MAX_RECEIVED_BUFFER];
		receivedPacket = new DatagramPacket(receivedBuffer,
				receivedBuffer.length);
	}

	public void run() {
		while (true) {
			try {
				socket.receive(receivedPacket);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			String message = new String(receivedPacket.getData(), 0,
					receivedPacket.getLength());
			udpMessage = message;
			Correspond.udpMessageProcess();
		}
	}

	public static List<String> getReceivedList() {
		return receivedList;
	}

	public static String getReceivedUDPMessage() {
		return udpMessage;
	}

	public static DatagramPacket getReceivedPacket() {
		return receivedPacket;
	}

	public static UserTable getUserTable() {
		return userTable;
	}
}
