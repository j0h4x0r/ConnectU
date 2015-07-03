package process.mainframe;

import java.net.*;

/**
 * 创建UDP套接字用于广播上线、下线等消息以及TCP握手消息的传送。 提供了取得UDP套接字和取得该类实例的方法
 * 
 * @author Troyal
 * 
 */
public class BroadcastProcess {
	private static BroadcastProcess instance = null;
	private DatagramSocket socket = null;

	private BroadcastProcess() {
		try {
			// 创建UDP套接字并将其绑定到指定端口
			socket = new DatagramSocket(Constant.UDP_PORT);
		} catch (SocketException se) {
			socket.close();
			se.printStackTrace();
		}
	}

	/**
	 * 取得当前本机的UDP套接字
	 * 
	 * @return 本机的UDP套接字
	 */
	public DatagramSocket getDatagramSocket() {
		return this.socket;
	}

	/**
	 * 取得BroadcastProcess类的一个实例， 并保证只有一个这样的实例存在
	 * 
	 * @return BroadcastProcess类的一个实例
	 */
	public static synchronized BroadcastProcess getInstance() {
		if (instance == null)
			instance = new BroadcastProcess();
		return instance;
	}
}
