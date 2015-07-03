package process.mainframe;

import java.io.*;
import java.net.*;

/**
 * 创建一个接收TCP消息的套接字，用于响应检查本机是否启动ConnectU的请求， 此动作独占一个线程
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
