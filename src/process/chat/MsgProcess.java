package process.chat;

import java.io.*;
import java.net.*;
import javax.swing.*;

import process.mainframe.*;
import chatframe.ChatFrame;

/**
 * ��Ϣ�շ��Ĵ����࣬����������Ϣ�����������Ϣ�ȷ����� ��չ��Runnable��������ռһ���߳�
 * 
 * @author Troyal
 * 
 */
public class MsgProcess implements Runnable {
	/**
	 * ��Ϣͨ�ŵ��׽���
	 */
	private Socket socket = null;
	/**
	 * ����д�������
	 */
	private OutputStreamWriter outputStreamWriter = null;
	/**
	 * ���ڴ�����������
	 */
	private InputStreamReader inputStreamReader = null;
	/**
	 * �Ѿ��շ���������Ϣ������ʾ��Chatframe��textReceived�ı������
	 */
	private String message = "";
	/**
	 * ��ǰ����Ĵ��ڿ��
	 */
	private ChatFrame window = null;
	/**
	 * �շ���Ϣ��socket�Ƿ�رյı�־����
	 */
	private boolean isSocketClosed = false;

	public MsgProcess(Socket socket, ChatFrame window) {
		try {
			this.socket = socket;
			outputStreamWriter = new OutputStreamWriter(
					socket.getOutputStream(), Constant.TEXT_CHARSET);
			inputStreamReader = new InputStreamReader(socket.getInputStream(),
					Constant.TEXT_CHARSET);
			this.window = window;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * ��ȡ�Է���IP��ַ
	 * 
	 * @return �뱾�������Ự�ĶԷ�IP��ַ
	 */
	public String getRequestIP() {
		String requestIP = null;
		try {
			requestIP = socket.getInetAddress().getHostAddress();
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return requestIP;
	}

	/**
	 * �߳�����ʱ����receiveMsg����
	 */
	public void run() {
		receiveMsg();
	}

	/**
	 * ������Ϣ���Է��������������Ͽ����ӵ���Ϣ
	 * 
	 * @param msg
	 *            Ҫ���͵���Ϣ
	 */
	public void sendMsg(String msg) {
		try {
			char[] buffer = new char[Constant.MAX_BUFFER];
			StringReader strReader = new StringReader(msg);
			int len = 0;
			// ����ǰ��msg���뻺������
			while ((len = strReader.read(buffer)) != -1) {
				outputStreamWriter.write(buffer, 0, len);
				outputStreamWriter.flush();
				if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_CONNECTED)) {
					// ���������ӳɹ�����Ϣ
					message = message + "���ӵ�"
							+ socket.getInetAddress().getHostName() + "�ɹ���"
							+ Constant.ENTER;
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnMessageSend().setEnabled(true);
					window.getBtnExit().setEnabled(true);
				} else if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_DISCONNECT)) {
					// �����ͶϿ����ӵ���Ϣ
					message = message + "�����ѶϿ�" + Constant.ENTER;
				} else {
					// ��ͨ�Ự��Ϣ
					message = message + "��˵��" + msg + Constant.ENTER;
				}
				// ˢ����ʾ��Ϣ���ı����
				window.getTextReceived().setText(message);
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(window,
					Constant.ERROR_MESSAGE_CHATWINDOW_NOT_RUNNING, null,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * ���նԷ����͵���Ϣ�������������Ͽ����ӵ���Ϣ�Լ��ļ��������Ϣ
	 */
	public void receiveMsg() {
		try {
			char[] buffer = new char[Constant.MAX_BUFFER];
			int len = 0;
			// ����������Ϣ���뻺������
			while (!isSocketClosed
					&& (len = inputStreamReader.read(buffer)) != -1) {
				String msg = new String(buffer, 0, len);
				if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_CONNECTED)) {
					// ���������ӳɹ�����Ϣ
					message = message + "��"
							+ socket.getInetAddress().getHostName() + "���ӳɹ���"
							+ Constant.ENTER;
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnMessageSend().setEnabled(true);
					window.getBtnExit().setEnabled(true);
				} else if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_CLOSED)) {
					// �����նԷ����ڹرյ���Ϣ
					message = message + socket.getInetAddress().getHostName()
							+ " �Ѿ��˳�" + Constant.ENTER;
					window.getTextReceived().setText(message);
					window.setVisible(true);
					window.getBtnFileChoose().setEnabled(false);
					// �Ƴ�IPPortBean�ж��ߵ�������Ϣ
					Integer port = Integer.valueOf(window.getPort());
					String localhostIP = Correspond.getLocalAddress();
					IPPortBean.removeIPPortBean(localhostIP, port);
					String requestIP = socket.getInetAddress().getHostAddress();
					Correspond.getChatRunningBean().remove(requestIP);
					// �Ͽ����������
					window.getMsgServer().closeMsgServer();
					window.getFileServer().closeFileServer();
					this.disconnect();
					window.getFileProcess().disconnect();
					break;
				} else if (msg.equals(Constant.MESSAGE_CHAT_WINDOW_DISCONNECT)) {
					// �����նԷ��Ͽ����ӵ���Ϣ
					message = message + "�� "
							+ socket.getInetAddress().getHostName()
							+ " �������Ѿ��Ͽ�" + Constant.ENTER;
					window.getTextReceived().setText(message);
					window.setVisible(true);
					break; // ���ٽ�����Ϣ
				} else if (msg.equals(Constant.MESSAGE_CANCEL_SEND_FILE)) {
					// �����նԷ�ȡ���ļ����͵���Ϣ
					window.getBtnFileChoose().setVisible(true);
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnFileSend().setVisible(true);
					window.getBtnFileSend().setEnabled(true);
					window.getTransferProcessBar().setVisible(false);
					window.getTransferProcessBar().setEnabled(false);
					window.getTfdFile().setVisible(true);
					window.getTfdFile().setEditable(true);
					window.getFileProcess().setSendingCancel(true);
					JOptionPane.showMessageDialog(window,
							Constant.ERROR_MESSAGE_FILESEND_CANCELED, null,
							JOptionPane.ERROR_MESSAGE);
					window.getFileProcess().setSendingCancel(false);
				} else if (msg.equals(Constant.MESSAGE_CANCEL_RECEIVE_FILE)) {
					// �����նԷ�ȡ���ļ����յ���Ϣ
					window.getBtnFileChoose().setVisible(true);
					window.getBtnFileChoose().setEnabled(true);
					window.getBtnFileSend().setVisible(true);
					window.getTransferProcessBar().setVisible(false);
					window.getTransferProcessBar().setEnabled(false);
					window.getTfdFile().setVisible(true);
					window.getTfdFile().setEditable(true);
					window.getFileProcess().setReceivingCancel(true);
					JOptionPane.showMessageDialog(window,
							Constant.ERROR_MESSAGE_FILERECEIVE_CANCELED, null,
							JOptionPane.ERROR_MESSAGE);
					window.getFileProcess().setReceivingCancel(false);
				} else {
					// ��ͨ�Ự��Ϣ
					message = message + socket.getInetAddress().getHostName()
							+ " ˵ ��" + msg + Constant.ENTER;
				}
				// ˢ����ʾ��Ϣ���ı����
				window.getTextReceived().setText(message);
				window.setVisible(true);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * �Ͽ����ӣ��ر�socket�����������
	 */
	public void disconnect() {
		try {
			isSocketClosed = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStreamWriter.close();
				inputStreamReader.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
