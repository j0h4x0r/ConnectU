package process.mainframe;

import java.util.*;

/**
 * �����а���һ��HashMap�Լ�����Ĳ�������keyΪ�û���IP��ֵΪ�����û�����û�����TCP������ʹ�õĶ˿�
 * 
 * @author Troyal
 * 
 */
public class IPPortBean {
	private static HashMap<String, Vector<Integer>> ipPortMap = new HashMap<String, Vector<Integer>>();
	private static IPPortBean instance = null;

	/**
	 * ���ϣ���м���һ���µ�TCP������Ϣ
	 * 
	 * @param ip
	 *            ����ͨ�ŵ��û���IP
	 * @param port
	 *            ����ͨ�ŵ�TCP�˿�
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
	 * ���ϣ����ɾ��һ��TCP������Ϣ
	 * 
	 * @param ip
	 *            ����ͨ�ŵ��û���IP
	 * @param port
	 *            ����ͨ�ŵ�TCP�˿�
	 */
	public static void removeIPPortBean(String ip, Integer port) {
		if (ipPortMap.containsKey(ip)) {
			if (ipPortMap.get(ip).size() == 1)
				// �����û�ֻ�н�����һ��TCP���ӣ���ֱ��ɾ�����û�
				ipPortMap.remove(ip);
			else
				ipPortMap.get(ip).remove(port);
		} else {
			System.out.println(Constant.MESSAGE_IP_NOT_EXIST);
		}
	}

	/**
	 * ȡ�������û�TCP������Ϣ�Ĺ�ϣ��
	 * 
	 * @return ���������û�TCP������Ϣ�Ĺ�ϣ��
	 */
	public static HashMap<String, Vector<Integer>> getIPPortMap() {
		return ipPortMap;
	}

	/**
	 * ȡ��IPPortBean���һ��ʵ��
	 * 
	 * @return IPPortBean���һ��ʵ��
	 */
	public static synchronized IPPortBean getInstance() {
		if (instance == null)
			instance = new IPPortBean();
		return instance;
	}

	/**
	 * �жϸ����ı����˿��Ƿ��Ѿ���ĳ�û�������TCP����
	 * 
	 * @param port
	 *            �����˿�
	 * @return ���˿��ѱ�ռ�ã�����true�����򷵻�false
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
