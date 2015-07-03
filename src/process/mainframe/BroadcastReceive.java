package process.mainframe;

import java.io.*;
import java.net.*;
import java.util.*;

import mainframe.UserTable;

/**
 * 接收UDP消息，独占一个线程。 定义了取得在线用户表格、UDP消息等方法。 有关对接收到的UDP消息的处理的方法在Correspond类中
 * 
 * @author Troyal
 * 
 */
public class BroadcastReceive implements Runnable {
	/**
	 * 本机的UDP套接字
	 */
	private DatagramSocket socket = null;
	/**
	 * 接收到的数据报
	 */
	private static DatagramPacket receivedPacket = null;
	/**
	 * 收到UDP消息的用户列表，通常与在线用户列表内容相同
	 */
	private static List<String> receivedList = new ArrayList<String>();
	/**
	 * 在线用户的表格
	 */
	private static UserTable userTable = null;
	/**
	 * 从数据报中得到的需要的UDP消息
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
