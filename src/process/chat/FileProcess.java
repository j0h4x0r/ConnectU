package process.chat;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import javax.swing.*;

import chatframe.ChatFrame;

/**
 * �ļ�����Ĵ����࣬��������������Ϣ��ȡ������ȷ����� ��չ��Runnable��������ռһ���߳�
 * 
 * @author Troyal
 * 
 */
public class FileProcess implements Runnable {
	/**
	 * �ļ�����ͨ�ŵ��׽���
	 */
	private Socket socket = null;
	/**
	 * ���ַ���д���������Writer������������Ϣ�ʹ���ʵʩ״����ͨ��
	 */
	private OutputStreamWriter outputStreamWriter = null;
	/**
	 * �������������ַ�����Reader������������Ϣ�ʹ���ʵʩ״����ͨ��
	 */
	private InputStreamReader inputStreamReader = null;
	/**
	 * ������д���ļ�������������ڴ��������ļ�
	 */
	private FileOutputStream fileOutputStream = null;
	/**
	 * ���ļ�ΪԴ����ֽ��������ڱ����ļ�����
	 */
	private FileInputStream fileInputStream = null;
	/**
	 * ������������д��������������ļ�����
	 */
	private DataOutputStream dataOutputStream = null;
	/**
	 * ������������д���������������ļ�����
	 */
	private DataInputStream dataInputStream = null;
	/**
	 * socket�������
	 */
	private OutputStream outputStream = null;
	/**
	 * socket��������
	 */
	private InputStream inputStream = null;
	/**
	 * ȡ�����͵ı�־����
	 */
	private boolean SendingCancel = false;
	/**
	 * ȡ�����յı�־����
	 */
	private boolean ReceivingCancel = false;
	/**
	 * ��ǰ����Ĵ��ڿ��
	 */
	private ChatFrame window = null;

	public FileProcess(Socket socket, ChatFrame window) {
		try {
			this.socket = socket;
			this.window = window;
			// ��socket�еõ�����������ļ�����
			outputStream = socket.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream,
					Constant.TEXT_CHARSET);
			dataOutputStream = new DataOutputStream(outputStream);
			// ��socket�еõ������������ļ�����
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream,
					Constant.TEXT_CHARSET);
			dataInputStream = new DataInputStream(inputStream);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// ��־������getter��setter
	public boolean getSendingCancel() {
		return SendingCancel;
	}

	public void setSendingCancel(boolean isSendingCancel) {
		this.SendingCancel = isSendingCancel;
	}

	public boolean getReceivingCancel() {
		return ReceivingCancel;
	}

	public void setReceivingCancel(boolean isReceivingCancel) {
		this.ReceivingCancel = isReceivingCancel;
	}

	/**
	 * �ر�IO�����Ͽ��ļ���������
	 */
	public void disconnect() {
		try {
			if (socket != null && !socket.isClosed()) {
				outputStream.close();
				outputStreamWriter.close();
				dataOutputStream.close();

				inputStream.close();
				inputStreamReader.close();
				dataInputStream.close();

				socket.close();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * �����Ի����ý��շ�ѡ���ļ�����·��
	 * 
	 * @return �ļ����յı���·��
	 */
	public String getReceivePath() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(window);
		return chooser.getSelectedFile().getPath();
	}

	/**
	 * ȡ���ļ�����
	 * 
	 * @return �Ƿ�ȡ���ɹ�������ȷȡ��������true�����򷵻�false
	 */
	public boolean cancelSendFile() {
		boolean flag;
		try {
			// ����ȷ�Ͽ�
			int confirm = JOptionPane.showConfirmDialog(null,
					Constant.CONFIRM_MESSAGE_CANCEL_SENDFILE, null,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.OK_OPTION) {
				// ����ͼ�����������
				window.getBtnSendCancel().setVisible(false);
				window.getBtnFileSend().setVisible(true);
				window.getTfdFile().setVisible(true);
				window.getTfdFile().setEnabled(true);
				window.getBtnFileChoose().setEnabled(true);
				setSendingCancel(true);
				setReceivingCancel(false);
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception ioe) {
			flag = true;
		}
		return flag;
	}

	/**
	 * ȡ���ļ�����
	 * 
	 * @return �Ƿ�ȡ���ɹ�������ȷȡ��������true�����򷵻�false
	 */
	public boolean cancelReceiveFile() {
		boolean flag;
		try {
			// ����ȷ�Ͽ�
			int confirm = JOptionPane.showConfirmDialog(null,
					Constant.CONFIRM_MESSAGE_CANCEL_RECEIVEFILE, null,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.OK_OPTION) {
				// ����ͼ�����������
				window.getBtnReceiveCancel().setVisible(false);
				window.getBtnFileReceive().setVisible(true);
				window.getBtnFileSend().setVisible(true);
				window.getTfdFile().setEnabled(true);
				window.getBtnFileChoose().setEnabled(true);
				setReceivingCancel(true);
				setSendingCancel(false);
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception ioe) {
			flag = true;
		}
		return flag;
	}

	/**
	 * �߳�����ʱ���ж��Ƿ����ļ����ǽ����ļ�����������Ӧ�ķ���
	 */
	public void run() {
		if (window.isReceive())
			receiveFile();
		else
			sendFile(window.getSendPath());
	}

	/**
	 * ����������Ϣ
	 */
	private void sendTransferFlg() {
		try {
			StringReader flgReader = new StringReader(
					Constant.MESSAGE_FILE_SEND_FLG);
			char[] flgBuffer = new char[128];
			int flgLen = 0;
			while ((flgLen = flgReader.read(flgBuffer)) != -1) {
				outputStreamWriter.write(flgBuffer, 0, flgLen);
				outputStreamWriter.flush();
				break;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Ӧ��������Ϣ
	 */
	private void replyTransferFlg() {
		try {
			char[] flgBuffer = new char[Constant.MAX_FLAG_BUFFER];
			int flgLen = 0;
			String fileFlg = null;
			while ((flgLen = inputStreamReader.read(flgBuffer)) != -1) {
				fileFlg = new String(flgBuffer, 0, flgLen);
				break;
			}
			if (fileFlg.equals(Constant.MESSAGE_FILE_SEND_FLG)) {
				StringReader flgReply = new StringReader(
						Constant.MESSAGE_FILE_RECEIVE_FLG);
				char[] replyBuffer = new char[Constant.MAX_FLAG_BUFFER];
				int replyLen = 0;
				while ((replyLen = flgReply.read(replyBuffer)) != -1) {
					outputStreamWriter.write(replyBuffer, 0, replyLen);
					outputStreamWriter.flush();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �˷������ڽ����ļ�����ͼ�����������Ӧ������
	 */
	public void receiveFile() {
		try {
			String receivePath = getReceivePath();
			window.getTransferProcessBar().setValue(0);
			window.getBtnFileReceive().setEnabled(false);
			replyTransferFlg();
			// �����ļ�����ʾʵʱ�ļ��������
			char[] buffer = new char[1024];
			int len = 0;
			String name = null;
			int maxLength = 0;
			while ((len = inputStreamReader.read(buffer)) != -1) {
				name = new String(buffer, 0, len).split(";")[0];
				maxLength = Integer.valueOf(new String(buffer, 0, len)
						.split(";")[1]);
				System.out.println("Received byte : " + len);
				break;
			}
			// ������Ӧ��ͼ�����
			window.getTfdFile().setVisible(false);
			window.getTransferProcessBar().setVisible(true);
			window.getTransferProcessBar().setStringPainted(true);
			window.getBtnFileReceive().setVisible(false);
			window.getBtnReceiveCancel().setVisible(true);
			window.getBtnReceiveCancel().setEnabled(true);
			window.getTransferProcessBar().setMinimum(0);
			window.getTransferProcessBar().setMaximum(maxLength * 100);
			setReceivingCancel(false);
			setSendingCancel(false);
			// �����ļ������յ����ļ�д��Ӳ��
			File file = new File(receivePath + File.separator + name);
			fileOutputStream = new FileOutputStream(file);
			int flen = 0;
			byte[] fbuffer = new byte[Constant.MAX_FILE_BUFFER];
			while (!getReceivingCancel() && !getSendingCancel()
					&& (flen = dataInputStream.read(fbuffer)) != -1) {
				if (flen == 1) {
					if (fbuffer[0] == -1) {
						System.out.println("File Receive end.");
						break;
					}
				}
				fileOutputStream.write(fbuffer, 0, flen);
				fileOutputStream.flush();
				// ���ý�������ʵʱ��ʾ
				int margin = new Float((flen * 100) / Constant.MAX_FILE_BUFFER)
						.intValue();
				if (margin == 0) {
					margin = 1;
				}
				window.getTransferProcessBar().setMargin(margin);
				SwingUtilities.invokeAndWait(window.getTransferProcessBar());
				System.out.println("File Receiving byte : " + flen);
			}

			window.getTransferProcessBar().setValue(0);
			window.getTransferProcessBar().setVisible(false);
			window.getTransferProcessBar().setEnabled(false);

			window.getBtnFileReceive().setVisible(false);
			window.getBtnFileReceive().setEnabled(false);
			window.getBtnFileSend().setVisible(true);
			window.getBtnFileSend().setEnabled(false);

			window.getTfdFile().setVisible(true);
			window.getTfdFile().setEnabled(true);
			window.getTfdFile().setEditable(true);

			window.getBtnFileChoose().setVisible(true);
			window.getBtnFileChoose().setEnabled(true);
			// ���ȡ���ļ����ͻ����
			if (!getReceivingCancel() && !getSendingCancel()) {
				fileOutputStream.close();
				window.getTransferProcessBar().setMargin(maxLength * 100);
				SwingUtilities.invokeAndWait(window.getTransferProcessBar());
				JOptionPane.showMessageDialog(window,
						Constant.CONFIRM_MESSAGE_FILE_RECEIVED, null,
						JOptionPane.INFORMATION_MESSAGE);
			}
			setReceivingCancel(false);
			setSendingCancel(false);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �˷������ڷ����ļ�����ͼ�����������Ӧ������
	 * 
	 * @param path
	 *            Ҫ�����ļ��ı���·��
	 */
	public void sendFile(String path) {
		try {
			sendTransferFlg();
			window.getTransferProcessBar().setValue(0);
			setReceivingCancel(false);
			setSendingCancel(false);
			// ����������Ϣ
			char[] flgBuffer = new char[128];
			int flgLen = 0;
			String fileFlg = null;
			while ((flgLen = inputStreamReader.read(flgBuffer)) != -1) {
				fileFlg = new String(flgBuffer, 0, flgLen);
				System.out.println(fileFlg);
				break;
			}
			// �����ļ�����ʾʵʱ�ļ����ͽ���
			if (fileFlg.equals(Constant.MESSAGE_FILE_RECEIVE_FLG)) {
				File file = new File(path);
				int maxLength = new Float(file.length()
						/ Constant.MAX_FILE_BUFFER).intValue();
				String strMaxLength = String.valueOf(maxLength);
				StringReader stringReader = new StringReader(file.getName()
						+ ";" + strMaxLength);
				char[] buffer = new char[file.getName().length() + 1
						+ strMaxLength.length()];
				int flen = 0;
				while ((flen = stringReader.read(buffer)) != -1) {
					outputStreamWriter.write(buffer, 0, flen);
					outputStreamWriter.flush();
					break;
				}
				// ����ͼ�����������
				window.getTfdFile().setVisible(false);
				window.getTransferProcessBar().setVisible(true);
				window.getTransferProcessBar().setStringPainted(true);
				window.getTransferProcessBar().setMargin(1);
				window.getTransferProcessBar().setMinimum(0);
				window.getTransferProcessBar().setMaximum(maxLength);
				// ��Ӳ�̶�ȡ�ļ���д�����������
				fileInputStream = new FileInputStream(file);
				byte[] fileBuffer = new byte[Constant.MAX_FILE_BUFFER];
				int read = 0;
				while (!getReceivingCancel() && !getSendingCancel()
						&& (read = fileInputStream.read(fileBuffer)) != -1) {
					dataOutputStream.write(fileBuffer, 0, read);
					dataOutputStream.flush();

					SwingUtilities
							.invokeAndWait(window.getTransferProcessBar());
					System.out.println("Sent byte : " + read);
				}
				// ����ͼ���������
				window.getTransferProcessBar().setVisible(false);
				window.getTfdFile().setText("");
				window.getTfdFile().setVisible(true);
				window.getTfdFile().setEnabled(true);
				window.getBtnSendCancel().setVisible(false);
				window.getBtnFileSend().setVisible(true);
				window.getBtnFileSend().setEnabled(false);
				window.getBtnFileChoose().setEnabled(true);
				// ���ȡ���ļ����ͻ����
				if (!getReceivingCancel() && !getSendingCancel()) {
					Thread.sleep(10);
					dataOutputStream.write(-1);
					dataOutputStream.flush();
					fileInputStream.close();
					dataOutputStream.close();
					setReceivingCancel(false);
					setSendingCancel(false);
					JOptionPane.showMessageDialog(window,
							Constant.CONFIRM_MESSAGE_FILE_SENT, null,
							JOptionPane.INFORMATION_MESSAGE);
				}
				setReceivingCancel(false);
				setSendingCancel(false);
			}

		} catch (Exception ffe) {
			ffe.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
