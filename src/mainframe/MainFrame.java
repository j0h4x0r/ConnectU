package mainframe;

import javax.swing.*;
import java.net.*;

import process.mainframe.*;

/**
 * ConnectU的主界面，在线用户列表的初始化，初始化UDP端口用于广播以及建立会话的通信
 * 
 * @author Troyal
 * 
 */
public class MainFrame extends JFrame {
	// 主界面的按钮、文本等GUI元素的定义
	private UserAddButton userAddButton = null;
	private UserRefreshButton userRefreshButton = null;
	private ChatButton chatButton = null;
	private ExitButton exitButton = null;
	private AboutButton aboutButton = null;
	UserCountLabel userCount = null;
	UserTable userTable = null;

	public MainFrame() {
		// 创建本机可用端口的列表，初始化UDP端口
		IPPortBean ipPortBean = IPPortBean.getInstance();
		DatagramSocket datagramSocket = BroadcastProcess.getInstance()
				.getDatagramSocket();
		// 创建监听UDP端口的线程和检查软件是否启动的线程
		userCount = new UserCountLabel();
		userTable = new UserTable(userCount);
		BroadcastReceive broadcastReceive = new BroadcastReceive(
				datagramSocket, userTable);
		Thread receiveThread = new Thread(broadcastReceive);
		receiveThread.start();
		Thread runningCheck = new Thread(new CheckRunning());
		runningCheck.start();
		// 创建用户列表
		JScrollPane userTablePane = new JScrollPane(userTable);
		userTablePane.setBounds(Constant.USER_TABLE_LOCATION_X,
				Constant.USER_TABLE_LOCATION_Y, Constant.USER_TABLE_WIDTH,
				Constant.USER_TABLE_HEIGHT);
		// 通过UDP端口发送广播消息，通知所有人自己上线了
		String message = process.mainframe.Constant.MESSAGE_BROADCAST_LOGIN
				+ ":" + Correspond.getLocalHostName() + ":"
				+ Correspond.getLocalAddress();
		Correspond.sendUDPMessage(process.mainframe.Constant.BROADCAST_ADDRESS,
				message);
		// 创建按钮
		userAddButton = new UserAddButton(userTable);
		userRefreshButton = new UserRefreshButton();
		chatButton = new ChatButton(userTable);
		exitButton = new ExitButton();
		aboutButton = new AboutButton();
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(userTablePane);
		buttonPanel.add(userCount);
		buttonPanel.add(exitButton);
		buttonPanel.add(chatButton);
		buttonPanel.add(aboutButton);
		buttonPanel.add(userAddButton);
		buttonPanel.add(userRefreshButton);
		buttonPanel
				.setSize(Constant.MAINFRAME_WIDTH, Constant.MAINFRAME_HEIGHT);
		buttonPanel.setLayout(null);
		add(buttonPanel);
		// 对于mainframe的设置
		setLayout(null);
		setTitle(Constant.MAINFRAME_TITLE);
		setSize(Constant.MAINFRAME_WIDTH, Constant.MAINFRAME_HEIGHT);
		setResizable(false);
		setLocationByPlatform(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
	}
}
