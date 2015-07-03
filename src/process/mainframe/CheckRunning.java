package process.mainframe;

import java.io.*;
import java.net.*;

/**
 * ����һ������TCP��Ϣ���׽��֣�������Ӧ��鱾���Ƿ�����ConnectU������ �˶�����ռһ���߳�
 * 
 * @author Troyal
 * 
 */
public class CheckRunning implements Runnable {
	private ServerSocket sever = null;

	public CheckRunning() {
		try {
			sever = new ServerSocket(Constant.RUNNING_CHECK_PORT);
		} catch (IOException ioe) {
			try {
				sever.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ioe.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				sever.accept();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
