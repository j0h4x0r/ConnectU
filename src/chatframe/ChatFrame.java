package chatframe;

import javax.swing.*;
import java.awt.event.*;

import process.chat.FileClient;
import process.chat.FileProcess;
import process.chat.FileServer;
import process.chat.MsgClient;
import process.chat.MsgProcess;
import process.chat.MsgServer;
import process.mainframe.Correspond;

/**
 * ConnectU������Ự����
 * 
 * @author Troyal
 * 
 */
public class ChatFrame extends JFrame {
	private JPanel panel = new JPanel();
	// ��¼�Է���IP�ͽ���TCP���ӵĶ˿ڣ�������ʾ����
	private JLabel labelIP = null;
	private JTextField tfdIP = null;
	private JLabel labelPort = null;
	private JTextField tfdPort = null;
	// �ļ�·�����ı��򣬷��͡���ʾ��Ϣ���ı����
	private JLabel labelFile = null;
	private JTextField tfdFile = null;
	private JTextPane textSend = null;
	private JScrollPane paneSendMsg = null;
	private JTextPane textReceived = null;
	private JScrollPane paneReceivedMsg = null;
	// ��ťԪ�صĶ���
	private MessageSendButton btnMessageSend = null;
	private ExitButton btnExit = null;
	private FileChooseButton btnFileChoose = null;
	private FileSendButton btnFileSend = null;
	private SendCancelButton btnSendCancel = null;
	private FileReceiveButton btnFileReceive = null;
	private ReceiveCancelButton btnReceiveCancel = null;
	// ��Ϣ���䡢�ļ�������߳�
	private MsgClient msgClient = null;
	private MsgServer msgServer = null;
	private MsgProcess msgProcess = null;
	private FileClient fileClient = null;
	private FileServer fileServer = null;
	private FileProcess fileProcess = null;
	// ��������
	private boolean isReceive = false;
	private String requestIP = null;
	private TransferProcessBar transferProcessBar = null;

	public ChatFrame(String requestUserName) {
		// ���ÿ���е����
		showChatWindow();
		setTitle(requestUserName + " - " + mainframe.Constant.MAINFRAME_TITLE);
		// ����chatframe������
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(Constant.CHAT_WINDOW_WIDTH, Constant.CHAT_WINDOW_HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		// ����msgClient��fileClient��ʵ��
		msgClient = new MsgClient(this);
		fileClient = new FileClient(this);
	}

	// �Ƿ����ڽ����ļ���
	public boolean isReceive() {
		return isReceive;
	}

	public void setIsReceive(boolean isReceive) {
		this.isReceive = isReceive;
	}

	/**
	 * ����chatframe����е������Ӳ�����Ӧ����
	 */
	public void showChatWindow() {
		// ��Ӳ��������ڼ�¼�Է���IP�ͽ���TCP���ӵĶ˿ڣ�������ʾ����
		labelIP = new JLabel(Constant.JLABEL_IP_TEXT);
		tfdIP = new JTextField(Constant.JTEXTFIELD_IP_TEXT);
		labelPort = new JLabel(Constant.JLABEL_PORT_TEXT);
		tfdPort = new JTextField(Constant.JTEXTFIELD_PORT_TEXT);
		labelIP.setVisible(false);
		tfdIP.setVisible(false);
		labelPort.setVisible(false);
		tfdPort.setVisible(false);
		panel.add(labelIP);
		panel.add(tfdIP);
		panel.add(labelPort);
		panel.add(tfdPort);
		// ��Ӳ������ļ����͵ĵ�ַ��
		labelFile = new JLabel(Constant.JLABEL_FILE_NAME);
		labelFile.setBounds(Constant.JLABEL_FILE_LOCATION_X,
				Constant.JLABEL_FILE_LOCATION_Y, Constant.JLABEL_FILE_WIDTH,
				Constant.JLABEL_FILE_HEIGHT);
		tfdFile = new JTextField(Constant.JTEXTFIELD_FILE_TEXT);
		tfdFile.setBounds(Constant.JTEXTFIELD_FILE_LOCATION_X,
				Constant.JTEXTFIELD_FILE_LOCATION_Y,
				Constant.JTEXTFIELD_FILE_WIDTH, Constant.JTEXTFIELD_FILE_HEIGHT);
		panel.add(labelFile);
		panel.add(tfdFile);
		// ��Ӳ�������Ϣ��ʾ���ı����
		textReceived = new JTextPane();
		paneReceivedMsg = new JScrollPane(textReceived);
		paneReceivedMsg.setBounds(
				Constant.JSCROLLPANE_RECEIVE_MESSAGE_LOCATION_X,
				Constant.JSCROLLPANE_RECEIVE_MESSAGE_LOCATION_Y,
				Constant.JSCROLLPANE_RECEIVE_MESSAGE_WIDTH,
				Constant.JSCROLLPANE_RECEIVE_MESSAGE_HEIGHT);
		panel.add(paneReceivedMsg);
		// ��Ӳ�������Ϣ������ı����
		textSend = new JTextPane();
		textSend.setFocusable(true);
		textSend.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// ����ctrl+Enter��ݼ�
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
					msgProcess.sendMsg(textSend.getText());
					textSend.setText("");
				}
			}
		});
		paneSendMsg = new JScrollPane(textSend);
		paneSendMsg.setBounds(Constant.JSCROLLPANE_SEND_MESSAGE_LOCATION_X,
				Constant.JSCROLLPANE_SEND_MESSAGE_LOCATION_Y,
				Constant.JSCROLLPANE_SEND_MESSAGE_WIDTH,
				Constant.JSCROLLPANE_SEND_MESSAGE_HEIGHT);
		panel.add(paneSendMsg);
		// ��Ӳ��������а�ť
		btnMessageSend = new MessageSendButton(this);
		btnExit = new ExitButton(this);
		btnFileSend = new FileSendButton(this);
		btnFileChoose = new FileChooseButton(this);
		btnSendCancel = new SendCancelButton(this);
		btnFileReceive = new FileReceiveButton(this);
		btnReceiveCancel = new ReceiveCancelButton(this);
		panel.add(btnMessageSend);
		panel.add(btnExit);
		panel.add(btnFileSend);
		panel.add(btnFileChoose);
		panel.add(btnSendCancel);
		panel.add(btnFileReceive);
		panel.add(btnReceiveCancel);
		// ��Ӳ������ļ����������
		transferProcessBar = new TransferProcessBar();
		transferProcessBar.setBounds(Constant.JPROCESSBAR_LOCATION_X,
				Constant.JPROCESSBAR_LOCATION_Y, Constant.JPROCESSBAR_WIDTH,
				Constant.JPROCESSBAR_HEIGHT);
		panel.add(transferProcessBar);
		transferProcessBar.setVisible(false);
		// ����panel����ӵ�chatframe��
		panel.setSize(Constant.CHAT_WINDOW_WIDTH, Constant.CHAT_WINDOW_HEIGHT);
		panel.setLayout(null);
		this.add(panel);
	}

	// ���ڸ������һϵ��getter��setter
	public String getIPAddress() {
		return tfdIP.getText().trim();
	}

	public void setIPAddress(String IPAddress) {
		this.tfdIP.setText(IPAddress);
	}

	public int getPort() {
		return Integer.parseInt(tfdPort.getText().trim());
	}

	public void setPort(int port) {
		this.tfdPort.setText(String.valueOf(port));
	}

	public MsgProcess getMsgProcess() {
		return this.msgProcess;
	}

	public void setMsgProcess(MsgProcess msgProcess) {
		this.msgProcess = msgProcess;
	}

	public FileProcess getFileProcess() {
		return this.fileProcess;
	}

	public void setFileProcess(FileProcess fileProcess) {
		this.fileProcess = fileProcess;
	}

	public MessageSendButton getBtnMessageSend() {
		return btnMessageSend;
	}

	public ExitButton getBtnExit() {
		return btnExit;
	}

	public FileChooseButton getBtnFileChoose() {
		return btnFileChoose;
	}

	public FileSendButton getBtnFileSend() {
		return btnFileSend;
	}

	public SendCancelButton getBtnSendCancel() {
		return btnSendCancel;
	}

	public FileReceiveButton getBtnFileReceive() {
		return btnFileReceive;
	}

	public ReceiveCancelButton getBtnReceiveCancel() {
		return btnReceiveCancel;
	}

	public JTextField getTfdFile() {
		return this.tfdFile;
	}

	public JTextPane getTextReceived() {
		return this.textReceived;
	}

	public JTextPane getTextSend() {
		return textSend;
	}

	public MsgServer getMsgServer() {
		return this.msgServer;
	}

	public FileServer getFileServer() {
		return this.fileServer;
	}

	public MsgClient getMsgClient() {
		return msgClient;
	}

	public FileClient getFileClient() {
		return fileClient;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public String getSendPath() {
		return this.tfdFile.getText();
	}

	public TransferProcessBar getTransferProcessBar() {
		return this.transferProcessBar;
	}

	/**
	 * ���������ʹ�����Ϣ���Ӻ��ļ����������ServerSocket���߳�
	 */
	public void autoStart() {
		msgServer = new MsgServer(this);
		Thread msgServerThread = new Thread(msgServer);
		msgServerThread.start();

		fileServer = new FileServer(this);
		Thread fileServerThread = new Thread(fileServer);
		fileServerThread.start();
	}

	/**
	 * �Ự�����ڶԷ��Ѵ򿪻Ự���ں��Լ�Ҳ�Ѿ��򿪴��ڵ�����£� ������Ϣͨ�ŵ��׽��ֲ��󶨶˿ڣ�ͬʱ��Է��������ӳɹ�����Ϣ��
	 * ��Ϣ�շ���ռһ���߳�
	 */
	public void autoConnect() {
		if (Correspond.isIPRight(getIPAddress())) {
			msgClient.connect();
			msgProcess = new MsgProcess(msgClient.getSocket(), this);
			msgProcess
					.sendMsg(process.chat.Constant.MESSAGE_CHAT_WINDOW_CONNECTED);
			this.requestIP = msgProcess.getRequestIP();
			Thread msgProcessThread = new Thread(msgProcess);
			msgProcessThread.start();
		}
	}
}
