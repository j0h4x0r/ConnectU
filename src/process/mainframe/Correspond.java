package process.mainframe;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

import chatframe.*;

/**
 * 此类包含与其他主机通信的有关方法，包括取得本地主机的参数、处理UDP消息等
 * 
 * @author Troyal
 * 
 */
public class Correspond {
	/**
	 * 存储已建立会话的用户
	 */
	private static Map<String, Boolean> chatRunningBean = null;

	/**
	 * 取得本机的IP地址
	 * 
	 * @return 本机IP地址的String值
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
	 * 取得本地主机名
	 * 
	 * @return 主机名的String值
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
	 * 取得已建立会话的用户的Map，并保证只有一个这样的实例存在
	 * 
	 * @return
	 */
	public static synchronized Map<String, Boolean> getChatRunningBean() {
		if (chatRunningBean == null)
			chatRunningBean = new HashMap<String, Boolean>();
		return chatRunningBean;
	}

	/**
	 * 判断与给定的用户是否已经建立TCP连接
	 * 
	 * @param ip
	 *            给定用户的IP地址
	 * @return 若已经建立连接，则返回true，否则返回false
	 */
	public static boolean isChatWindowsRunning(String ip) {
		return chatRunningBean.get(ip);
	}

	/**
	 * 判断请求的TCP端口是否允许建立会话
	 * 
	 * @param port
	 *            请求的TCP端口
	 * @return 若允许建立，则返回true，否则返回false
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
	 * 取得本机的空闲端口用以建立新的TCP连接
	 * 
	 * @return 本机的空闲端口
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
			// 选取已占用的最大端口号加固定增量作为选定的空闲端口
			suitPort = ports[portsCount - 1] + Constant.CHAT_PORT_INCREASEMENT;
		} else {
			suitPort = Constant.DEFAULT_CHAT_PORT;
		}
		return suitPort;
	}

	/**
	 * 判断给定的IP地址是否合法
	 * 
	 * @param ip
	 *            IP地址的String值
	 * @return 若给定的IP地址合法，则返回true，否则弹出错误提示并返回false
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
	 * 发送UDP消息
	 * 
	 * @param ip
	 *            数据报的目标IP地址的String值
	 * @param udpMessage
	 *            要发送的消息，以String形式给出
	 */
	public static void sendUDPMessage(String ip, String udpMessage) {
		try {
			InetAddress address = InetAddress.getByName(ip);
			byte[] message = udpMessage.getBytes();
			// 根据需要发送的消息创建数据报包
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
	 * 处理收到的UDP消息，包括广播消息和建立TCP连接的握手消息
	 */
	public static void udpMessageProcess() {
		// 将得到的数据报包的信息提取出来
		String udpMessage = BroadcastReceive.getReceivedUDPMessage();
		DatagramPacket receivedPacket = BroadcastReceive.getReceivedPacket();
		String requestIP = receivedPacket.getAddress().getHostAddress();
		String requestUserName = receivedPacket.getAddress().getHostName();
		if (udpMessage != null) {
			if (udpMessage.indexOf(Constant.UDP_MESSAGE_CONNECT_PORT) > -1) {
				// 建立会话的请求，请求连接本机的TCP端口
				int tcpPort = Integer.valueOf(udpMessage.split(":")[1])
						.intValue();
				// 检查请求的端口是否空闲
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
				// 建立会话的请求，请求打开本机的TCP端口
				getChatRunningBean().put(requestIP, true);
				// BroadcastReceive.getUserTable().addUser(requestUserName,
				// requestIP);
				String port = udpMessage.split(":")[1];
				// 打开会话窗口
				ChatFrame window = new ChatFrame(requestUserName);
				window.setPort(Integer.valueOf(port).intValue());
				window.setIPAddress(requestIP);
				window.autoStart();
				String localIPAddress = getLocalAddress();
				IPPortBean.addIPPortBean(localIPAddress, Integer.valueOf(port));
				// 返回打开成功的信息
				String message = Constant.UDP_MESSAGE_PORT_OPENED
						+ String.valueOf(port);
				sendUDPMessage(requestIP, message);
				System.out.println(" **************** : " + requestIP);
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_PORT_OPENED) > -1) {
				// 对方的TCP端口已打开
				String port = udpMessage.split(":")[1];
				// 打开会话窗口
				ChatFrame window = new ChatFrame(requestUserName);
				window.setPort(Integer.valueOf(port).intValue());
				window.setIPAddress(requestIP);
				window.autoStart();
				window.autoConnect();
				System.out.println("Connected to ---- " + requestIP + " : "
						+ port);
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_PORT_CHECK_OK) > -1) {
				// 请求的TCP端口允许连接
				int tcpPort = Integer.valueOf(udpMessage.split(":")[1])
						.intValue();
				String message = Constant.UDP_MESSAGE_OPEN_PORT
						+ String.valueOf(tcpPort);
				sendUDPMessage(requestIP, message);
			} else if (udpMessage.indexOf(Constant.MESSAGE_BROADCAST_LOGIN) > -1) {
				// 广播消息，有用户上线
				System.out.println(udpMessage);
				String userMessage = udpMessage.split(":")[1] + ":"
						+ udpMessage.split(":")[2];
				// 向在线用户列表中添加该用户
				List<String> receivedList = BroadcastReceive.getReceivedList();
				if (!receivedList.contains(userMessage)) {
					receivedList.add(userMessage);
					BroadcastReceive.getUserTable().refreshUserTable(
							receivedList);
					String localHostName = getLocalHostName();
					String localHostAddress = getLocalAddress();
					// 向其发送自己在线的消息，帮助其建立在线用户列表
					String message = Constant.UDP_MESSAGE_USER_LOGINNED
							+ localHostName + ":" + localHostAddress;
					sendUDPMessage(Constant.BROADCAST_ADDRESS, message);
				}
			} else if (udpMessage.indexOf(Constant.MESSAGE_BROADCAST_LOGOFF) > -1) {
				// 广播消息，有用户下线
				List<String> receivedList = BroadcastReceive.getReceivedList();
				String userMessage = udpMessage.split(":")[1] + ":"
						+ udpMessage.split(":")[2];
				// 从在线用户列表和在线用户表格中移除该用户
				if (receivedList.contains(userMessage)) {
					receivedList.remove(userMessage);
					BroadcastReceive.getUserTable().refreshUserTable(
							receivedList);
				}
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_USER_LOGINNED) > -1) {
				// 响应广播消息，告知发送方当前在线
				String userMessage = udpMessage.split(":")[1] + ":"
						+ udpMessage.split(":")[2];
				// 将发送方加入在线用户列表
				List<String> receivedList = BroadcastReceive.getReceivedList();
				if (!receivedList.contains(userMessage)) {
					receivedList.add(userMessage);
					BroadcastReceive.getUserTable().refreshUserTable(
							receivedList);
				}
			} else if (udpMessage.indexOf(Constant.UDP_MESSAGE_SUIT_PORT) > -1) {
				// 请求的TCP端口得到对方允许
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
