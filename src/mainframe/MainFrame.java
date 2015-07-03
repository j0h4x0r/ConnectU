package mainframe;

import javax.swing.*;
import java.net.*;

import process.mainframe.*;

/**
 * ConnectU�������棬�����û��б�ĳ�ʼ������ʼ��UDP�˿����ڹ㲥�Լ������Ự��ͨ��
 * 
 * @author Troyal
 * 
 */
public class MainFrame extends JFrame {
	// ������İ�ť���ı���GUIԪ�صĶ���
	private UserAddButton userAddButton = null;
	private UserRefreshButton userRefreshButton = null;
	private ChatButton chatButton = null;
	private ExitButton exitButton = null;
	private AboutButton aboutButton = null;
	UserCountLabel userCount = null;
	UserTable userTable = null;

	public MainFrame() {
		// �����������ö˿ڵ��б���ʼ��UDP�˿�
		IPPortBean ipPortBean = IPPortBean.getInstance();
		DatagramSocket datagramSocket = BroadcastProcess.getInstance()
				.getDatagramSocket();
		// ��������UDP�˿ڵ��̺߳ͼ������Ƿ��������߳�
		userCount = new UserCountLabel();
		userTable = new UserTable(userCount);
		BroadcastReceive broadcastReceive = new BroadcastReceive(
				datagramSocket, userTable);
		Thread receiveThread = new Thread(broadcastReceive);
		receiveThread.start();
		Thread runningCheck = new Thread(new CheckRunning());
		runningCheck.start();
		// �����û��б�
		JScrollPane userTablePane = new JScrollPane(userTable);
		userTablePane.setBounds(Constant.USER_TABLE_LOCATION_X,
				Constant.USER_TABLE_LOCATION_Y, Constant.USER_TABLE_WIDTH,
				Constant.USER_TABLE_HEIGHT);
		// ͨ��UDP�˿ڷ��͹㲥��Ϣ��֪ͨ�������Լ�������
		String message = process.mainframe.Constant.MESSAGE_BROADCAST_LOGIN
				+ ":" + Correspond.getLocalHostName() + ":"
				+ Correspond.getLocalAddress();
		Correspond.sendUDPMessage(process.mainframe.Constant.BROADCAST_ADDRESS,
				message);
		// ������ť
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
		// ����mainframe������
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
