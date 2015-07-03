package process.mainframe;

import java.util.*;

/**
 * 此类中包含一个HashMap以及对其的操作，其key为用户的IP，值为其他用户与该用户建立TCP连接所使用的端口
 * 
 * @author Troyal
 * 
 */
public class IPPortBean {
	private static HashMap<String, Vector<Integer>> ipPortMap = new HashMap<String, Vector<Integer>>();
	private static IPPortBean instance = null;

	/**
	 * 向哈希表中加入一个新的TCP连接信息
	 * 
	 * @param ip
	 *            进行通信的用户的IP
	 * @param port
	 *            进行通信的TCP端口
	 */
	public static void addIPPortBean(String ip, Integer port) {
		if (ipPortMap.containsKey(ip)) {
			if (!ipPortMap.get(ip).contains(port))
				ipPortMap.get(ip).add(port);
		} else {
			Vector<Integer> portVector = new Vector<Integer>();
			portVector.add(port);
			ipPortMap.put(ip, portVector);
		}
	}

	/**
	 * 向哈希表中删除一个TCP连接信息
	 * 
	 * @param ip
	 *            进行通信的用户的IP
	 * @param port
	 *            进行通信的TCP端口
	 */
	public static void removeIPPortBean(String ip, Integer port) {
		if (ipPortMap.containsKey(ip)) {
			if (ipPortMap.get(ip).size() == 1)
				// 若该用户只有建立了一个TCP连接，则直接删除该用户
				ipPortMap.remove(ip);
			else
				ipPortMap.get(ip).remove(port);
		} else {
			System.out.println(Constant.MESSAGE_IP_NOT_EXIST);
		}
	}

	/**
	 * 取得所有用户TCP连接信息的哈希表
	 * 
	 * @return 包含所有用户TCP连接信息的哈希表
	 */
	public static HashMap<String, Vector<Integer>> getIPPortMap() {
		return ipPortMap;
	}

	/**
	 * 取得IPPortBean类的一个实例
	 * 
	 * @return IPPortBean类的一个实例
	 */
	public static synchronized IPPortBean getInstance() {
		if (instance == null)
			instance = new IPPortBean();
		return instance;
	}

	/**
	 * 判断给定的本机端口是否已经与某用户建立了TCP连接
	 * 
	 * @param port
	 *            本机端口
	 * @return 若端口已被占用，返回true，否则返回false
	 */
	public static boolean containsTCPPort(int port) {
		boolean flag;
		if (ipPortMap.size() != 0)
			flag = ipPortMap.get(Correspond.getLocalAddress()).contains(port);
		else
			flag = false;
		return flag;
	}
}
