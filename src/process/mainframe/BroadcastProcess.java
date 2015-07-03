package process.mainframe;

import java.net.*;

/**
 * ����UDP�׽������ڹ㲥���ߡ����ߵ���Ϣ�Լ�TCP������Ϣ�Ĵ��͡� �ṩ��ȡ��UDP�׽��ֺ�ȡ�ø���ʵ���ķ���
 * 
 * @author Troyal
 * 
 */
public class BroadcastProcess {
	private static BroadcastProcess instance = null;
	private DatagramSocket socket = null;

	private BroadcastProcess() {
		try {
			// ����UDP�׽��ֲ�����󶨵�ָ���˿�
			socket = new DatagramSocket(Constant.UDP_PORT);
		} catch (SocketException se) {
			socket.close();
			se.printStackTrace();
		}
	}

	/**
	 * ȡ�õ�ǰ������UDP�׽���
	 * 
	 * @return ������UDP�׽���
	 */
	public DatagramSocket getDatagramSocket() {
		return this.socket;
	}

	/**
	 * ȡ��BroadcastProcess���һ��ʵ���� ����ֻ֤��һ��������ʵ������
	 * 
	 * @return BroadcastProcess���һ��ʵ��
	 */
	public static synchronized BroadcastProcess getInstance() {
		if (instance == null)
			instance = new BroadcastProcess();
		return instance;
	}
}
