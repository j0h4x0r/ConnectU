package process.chat;

/**
 * 定义process.chat中用到的常量，主要包括握手消息和一些提示信息
 * @author Troyal
 *
 */
public class Constant {
	public static final int MAX_BUFFER = 10240;
	public static final int MAX_FILE_BUFFER = 102400;
	public static final int MAX_FLAG_BUFFER = 128;
	public static final String ENTER = "\r\n";
	public static final String TEXT_CHARSET = "UTF-8";
	
	public static final String MESSAGE_CHAT_WINDOW_CONNECTED = "ConnectU Conneted!";
	public static final String MESSAGE_CHAT_WINDOW_DISCONNECT = "ConnectU Disconnected!";
	public static final String MESSAGE_CHAT_WINDOW_CLOSED = "ConnectU Closed!";
	public static final String MESSAGE_CANCEL_RECEIVE_FILE = "File receiving Cancel!";
	public static final String MESSAGE_CANCEL_SEND_FILE = "File sending Cancel!";
	public static final String MESSAGE_FILE_SEND_FLG = "******File transfer Flag******";
	public static final String MESSAGE_FILE_RECEIVE_FLG = "File transfer Flag Received!";
	
	public static final String ERROR_MESSAGE_CHATWINDOW_NOT_RUNNING = "对方已经关闭了消息窗口，请重新连接！";
	public static final String ERROR_MESSAGE_FILESEND_CANCELED = "对方取消了文件传送";
	public static final String ERROR_MESSAGE_FILERECEIVE_CANCELED = "对方取消了文件接收";
	public static final String CONFIRM_MESSAGE_CANCEL_SENDFILE = "文件传送中，确定取消？";
	public static final String CONFIRM_MESSAGE_CANCEL_RECEIVEFILE = "文件接收中，确定取消？";
	public static final String CONFIRM_MESSAGE_FILE_RECEIVED = "文件接收完毕";
	public static final String CONFIRM_MESSAGE_FILE_SENT = "文件传送完毕";
}
