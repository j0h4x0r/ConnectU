package process.mainframe;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

import chatframe.*;

/**
 * �����������������ͨ�ŵ��йط���������ȡ�ñ��������Ĳ���������UDP��Ϣ��
 * 
 * @author Troyal
 * 
 */
public class Correspond {
	/**
	 * �洢�ѽ����Ự���û�
	 */
	private static Map<String, Boolean> chatRunningBean = null;

	/**
	 * ȡ�ñ�����IP��ַ
	 * 
	 * @return ����IP��ַ��Stringֵ
	 */
	public static String getLocalAddress() {
		String localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		return localAddress;
	}

	/**
	 * ȡ�ñ���������
	 * 
	 * @return ��������Stringֵ
	 */
	public static String getLocalHostName() {
		String localHostName = null;
		try {
			localHostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		return localHostName;
	}

	/**
	 * ȡ���ѽ����Ự���û���Map������ֻ֤��һ��������ʵ������
	 * 
	 * @return
	 */
	public static synchronized Map<String, Boolean> getChatRunningBean() {
		if (chatRunningBean == null)
			chatRunningBean = new HashMap<String, Boolean>();
		return chatRunningBean;
	}

	/**
	 * �ж���������û��Ƿ��Ѿ�����TCP����
	 * 
	 * @param ip
	 *            �����û���IP��ַ
	 * @return ���Ѿ��������ӣ��򷵻�true�����򷵻�false
	 */
	public static boolean isChatWindowsRunning(String ip) {
		return chatRunningBean.get(ip);
	}

	/**
	 * �ж������TCP�˿��Ƿ��������Ự
	 * 
	 * @param port
	 *            �����TCP�˿�
	 * @return �����������򷵻�true�����򷵻�false
	 */
	private static boolean isSuitTCPPort(int port) {
		int localSuitTCPPort = getLocalSuitTCPPort();
		boolean flag;
		if (port == localSuitTCPPort)
			flag = true;
		else if (!IPPortBean.containsTCPPort(port))
			flag = true;
		else
			flag = false;
		return flag;
	}

	/**
	 * ȡ�ñ����Ŀ��ж˿����Խ����µ�TCP����
	 * 
	 * @return �����Ŀ��ж˿�
	 */
	public static int getLocalSuitTCPPort() {
		int suitPort;
		String localHostIP = getLocalAddress();
		HashMap<String, Vector<Integer>> ipPortMap = IPPortBean.getIPPortMap();
		if (ipPortMap.size() == 0) {
			suitPort = Constant.DEFAULT_CHAT_PORT;
		} else if (ipPortMap.containsKey(localHostIP)) {
			int portsCount = ipPortMap.get(localHostIP).size();
			int[] ports = new int[portsCount];
			int index = 0;
			for (int port : ipPortMap.get(localHostIP))
				ports[index++] = port;
			Arrays.sort(ports);
			// ѡȡ��ռ�õ����˿ںżӹ̶�������Ϊѡ���Ŀ��ж˿�
			suitPort = ports[portsCount - 1] + Constant.CHAT_PORT_INCREASEMENT;
		} else {
			suitPort = Constant.DEFAULT_CHAT_PORT;
		}
		return suitPort;
	}

	/**
	 * �жϸ�����IP��ַ�Ƿ�Ϸ�
	 * 
	 * @param ip
	 *            IP��ַ��Stringֵ
	 * @return ��������IP��ַ�Ϸ����򷵻�true�����򵯳�������ʾ������false
	 */
	public static boolean isIPRight(String ip) {
		boolean flag;
		String regex = Constant.REGEX_IP;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(ip);
		if (m.matches()) {
			flag = true;
		} else {
			JOptionPane.showMessageDialog(null, Constant.MESSAGE_IP_ERROR,
					null, JOptionPane.ERROR_MESSAGE);
			flag = false;
		}
		return flag;
	}

	/**
	 * ����UDP��Ϣ
	 * 
	 * @param ip
	 *            ���ݱ���Ŀ��IP��ַ��Stringֵ
	 * @param udpMessage
	 *            Ҫ���͵���Ϣ����String��ʽ����
	 */
	public static void sendUDPMessage(String ip, String udpMessage) {
		try {
			InetAddress address = InetAddress.getByName(ip);
			byte[] message = udpMessage.getBytes();
			// ������Ҫ���͵���Ϣ�������ݱ���
			DatagramPacket packet = new DatagramPacket(message, 0,
					message.length, address, Constant.UDP_PORT);
			BroadcastProcess.getInstance().getDatagramSocket().send(packet);
			System.out.println(udpMessage);
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			JOptionPane.showMessageDialog(null, Constant.MESSAGE_NETWORK_ERROR,
					null, JOptionPane.ERROR_MESSAGE);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, Constant.MESSAGE_NETWORK_ERROR,
					null, JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * �����յ���UDP��Ϣ�������㲥��Ϣ�ͽ���TCP���ӵ�������Ϣ
	 */
	public static void udpMessageProcess() {
		// ���õ������ݱ�������Ϣ��ȡ����
		String udpMessage = BroadcastReceive.getReceivedUDPMessage();
		DatagramPacket receivedPacket = BroadcastReceive.getReceivedPacket();
		String requestIP = receivedPacket.getAddress().getHostAddress();
		String requestUserName = receivedPacket.getAddress().getHostName();
		if (udpMessage != null) {
			if (udpMessage.indexOf(Constant.UDP_MESSAGE_CONNECT_PORT) > -1) {
				// �����Ự�������������ӱ�����TCP�˿�
				int tcpPort = Integer.valueOf(udpMessage.split(":")[1])
						.intValue();
				// �������Ķ˿��Ƿ����
				if (!isSuitTCPPort(tcpPort)) {
					int localSuitTCPPort = getLocalSuitTCPPort();
					System.out.println(localSuitTCPPort);
					String message = Constant.UDP_MESSAGE_SUIT_PORT
							+ String.valueOf(localSuitTCPPort);
					sendUDPMessage(requestIP, message);
				} else {
					String message = Constant.UDP_MESSAGE_PORT_CHECK_OK
							+ String.valueOf(tcpPort);
					sendUDPMessage(requestIP, message);
				}
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_OPEN_PORT) > -1) {
				// �����Ự����������򿪱�����TCP�˿�
				getChatRunningBean().put(requestIP, true);
				// BroadcastReceive.getUserTable().addUser(requestUserName,
				// requestIP);
				String port = udpMessage.split(":")[1];
				// �򿪻Ự����
				ChatFrame window = new ChatFrame(requestUserName);
				window.setPort(Integer.valueOf(port).intValue());
				window.setIPAddress(requestIP);
				window.autoStart();
				String localIPAddress = getLocalAddress();
				IPPortBean.addIPPortBean(localIPAddress, Integer.valueOf(port));
				// ���ش򿪳ɹ�����Ϣ
				String message = Constant.UDP_MESSAGE_PORT_OPENED
						+ String.valueOf(port);
				sendUDPMessage(requestIP, message);
				System.out.println(" **************** : " + requestIP);
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_PORT_OPENED) > -1) {
				// �Է���TCP�˿��Ѵ�
				String port = udpMessage.split(":")[1];
				// �򿪻Ự����
				ChatFrame window = new ChatFrame(requestUserName);
				window.setPort(Integer.valueOf(port).intValue());
				window.setIPAddress(requestIP);
				window.autoStart();
				window.autoConnect();
				System.out.println("Connected to ---- " + requestIP + " : "
						+ port);
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_PORT_CHECK_OK) > -1) {
				// �����TCP�˿���������
				int tcpPort = Integer.valueOf(udpMessage.split(":")[1])
						.intValue();
				String message = Constant.UDP_MESSAGE_OPEN_PORT
						+ String.valueOf(tcpPort);
				sendUDPMessage(requestIP, message);
			} else if (udpMessage.indexOf(Constant.MESSAGE_BROADCAST_LOGIN) > -1) {
				// �㲥��Ϣ�����û�����
				System.out.println(udpMessage);
				String userMessage = udpMessage.split(":")[1] + ":"
						+ udpMessage.split(":")[2];
				// �������û��б�����Ӹ��û�
				List<String> receivedList = BroadcastReceive.getReceivedList();
				if (!receivedList.contains(userMessage)) {
					receivedList.add(userMessage);
					BroadcastReceive.getUserTable().refreshUserTable(
							receivedList);
					String localHostName = getLocalHostName();
					String localHostAddress = getLocalAddress();
					// ���䷢���Լ����ߵ���Ϣ�������佨�������û��б�
					String message = Constant.UDP_MESSAGE_USER_LOGINNED
							+ localHostName + ":" + localHostAddress;
					sendUDPMessage(Constant.BROADCAST_ADDRESS, message);
				}
			} else if (udpMessage.indexOf(Constant.MESSAGE_BROADCAST_LOGOFF) > -1) {
				// �㲥��Ϣ�����û�����
				List<String> receivedList = BroadcastReceive.getReceivedList();
				String userMessage = udpMessage.split(":")[1] + ":"
						+ udpMessage.split(":")[2];
				// �������û��б�������û�������Ƴ����û�
				if (receivedList.contains(userMessage)) {
					receivedList.remove(userMessage);
					BroadcastReceive.getUserTable().refreshUserTable(
							receivedList);
				}
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_USER_LOGINNED) > -1) {
				// ��Ӧ�㲥��Ϣ����֪���ͷ���ǰ����
				String userMessage = udpMessage.split(":")[1] + ":"
						+ udpMessage.split(":")[2];
				// �����ͷ����������û��б�
				List<String> receivedList = BroadcastReceive.getReceivedList();
				if (!receivedList.contains(userMessage)) {
					receivedList.add(userMessage);
					BroadcastReceive.getUserTable().refreshUserTable(
							receivedList);
				}
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_SUIT_PORT) > -1) {
				// �����TCP�˿ڵõ��Է�����
				int tcpPort = Integer.valueOf(udpMessage.split(":")[1])
						.intValue();
				if (!isSuitTCPPort(tcpPort)) {
					int localSuitTCPPort = getLocalSuitTCPPort();
					String message = Constant.UDP_MESSAGE_CONNECT_PORT
							+ String.valueOf(localSuitTCPPort);
					sendUDPMessage(requestIP, message);
				} else {
					String message = Constant.UDP_MESSAGE_OPEN_PORT
							+ String.valueOf(tcpPort);
					sendUDPMessage(requestIP, message);
				}
			}
		}
	}
}
