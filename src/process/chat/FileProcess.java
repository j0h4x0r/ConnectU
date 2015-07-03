package process.chat;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import javax.swing.*;

import chatframe.ChatFrame;

/**
 * 文件传输的处理类，包括处理握手消息、取消传输等方法， 扩展了Runnable方法，独占一个线程
 * 
 * @author Troyal
 * 
 */
public class FileProcess implements Runnable {
	/**
	 * 文件传输通信的套接字
	 */
	private Socket socket = null;
	/**
	 * 将字符流写入输出流的Writer，用于握手消息和传输实施状况的通信
	 */
	private OutputStreamWriter outputStreamWriter = null;
	/**
	 * 将输入流读入字符流的Reader，用于握手消息和传输实施状况的通信
	 */
	private InputStreamReader inputStreamReader = null;
	/**
	 * 将数据写入文件的输出流，用于创建本地文件
	 */
	private FileOutputStream fileOutputStream = null;
	/**
	 * 以文件为源获得字节流，用于本地文件读入
	 */
	private FileInputStream fileInputStream = null;
	/**
	 * 将数据输入流写入输出流，用于文件传输
	 */
	private DataOutputStream dataOutputStream = null;
	/**
	 * 从输入流读入写入数据流，用于文件传输
	 */
	private DataInputStream dataInputStream = null;
	/**
	 * socket的输出流
	 */
	private OutputStream outputStream = null;
	/**
	 * socket的输入流
	 */
	private InputStream inputStream = null;
	/**
	 * 取消传送的标志变量
	 */
	private boolean SendingCancel = false;
	/**
	 * 取消接收的标志变量
	 */
	private boolean ReceivingCancel = false;
	/**
	 * 当前聊天的窗口框架
	 */
	private ChatFrame window = null;

	public FileProcess(Socket socket, ChatFrame window) {
		try {
			this.socket = socket;
			this.window = window;
			// 从socket中得到输出流用于文件传送
			outputStream = socket.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream,
					Constant.TEXT_CHARSET);
			dataOutputStream = new DataOutputStream(outputStream);
			// 从socket中得到输入流用于文件接收
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream,
					Constant.TEXT_CHARSET);
			dataInputStream = new DataInputStream(inputStream);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// 标志变量的getter和setter
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
	 * 关闭IO流，断开文件传输连接
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
	 * 弹出对话框让接收方选择文件接收路径
	 * 
	 * @return 文件接收的本地路径
	 */
	public String getReceivePath() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(window);
		return chooser.getSelectedFile().getPath();
	}

	/**
	 * 取消文件发送
	 * 
	 * @return 是否取消成功，若的确取消，返回true，否则返回false
	 */
	public boolean cancelSendFile() {
		boolean flag;
		try {
			// 弹出确认框
			int confirm = JOptionPane.showConfirmDialog(null,
					Constant.CONFIRM_MESSAGE_CANCEL_SENDFILE, null,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.OK_OPTION) {
				// 设置图形组件的属性
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
	 * 取消文件接收
	 * 
	 * @return 是否取消成功，若的确取消，返回true，否则返回false
	 */
	public boolean cancelReceiveFile() {
		boolean flag;
		try {
			// 弹出确认框
			int confirm = JOptionPane.showConfirmDialog(null,
					Constant.CONFIRM_MESSAGE_CANCEL_RECEIVEFILE, null,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.OK_OPTION) {
				// 设置图形组件的属性
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
	 * 线程启动时，判断是发送文件还是接收文件，并调用相应的方法
	 */
	public void run() {
		if (window.isReceive())
			receiveFile();
		else
			sendFile(window.getSendPath());
	}

	/**
	 * 发送握手消息
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
	 * 应答握手消息
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
	 * 此方法用于接收文件并对图形组件进行相应的设置
	 */
	public void receiveFile() {
		try {
			String receivePath = getReceivePath();
			window.getTransferProcessBar().setValue(0);
			window.getBtnFileReceive().setEnabled(false);
			replyTransferFlg();
			// 接收文件并显示实时文件传输进度
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
			// 设置相应的图形组件
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
			// 接收文件并将收到的文件写入硬盘
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
				// 设置进度条的实时显示
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
			// 如果取消文件传送或接收
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
	 * 此方法用于发送文件并对图形组件进行相应的设置
	 * 
	 * @param path
	 *            要发送文件的本地路径
	 */
	public void sendFile(String path) {
		try {
			sendTransferFlg();
			window.getTransferProcessBar().setValue(0);
			setReceivingCancel(false);
			setSendingCancel(false);
			// 处理握手消息
			char[] flgBuffer = new char[128];
			int flgLen = 0;
			String fileFlg = null;
			while ((flgLen = inputStreamReader.read(flgBuffer)) != -1) {
				fileFlg = new String(flgBuffer, 0, flgLen);
				System.out.println(fileFlg);
				break;
			}
			// 发送文件并显示实时文件发送进度
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
				// 设置图形组件的属性
				window.getTfdFile().setVisible(false);
				window.getTransferProcessBar().setVisible(true);
				window.getTransferProcessBar().setStringPainted(true);
				window.getTransferProcessBar().setMargin(1);
				window.getTransferProcessBar().setMinimum(0);
				window.getTransferProcessBar().setMaximum(maxLength);
				// 从硬盘读取文件并写入输出流传送
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
				// 设置图形组件属性
				window.getTransferProcessBar().setVisible(false);
				window.getTfdFile().setText("");
				window.getTfdFile().setVisible(true);
				window.getTfdFile().setEnabled(true);
				window.getBtnSendCancel().setVisible(false);
				window.getBtnFileSend().setVisible(true);
				window.getBtnFileSend().setEnabled(false);
				window.getBtnFileChoose().setEnabled(true);
				// 如果取消文件传送或接收
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
