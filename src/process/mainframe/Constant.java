package process.mainframe;

/**
 * 定义process.mainframe中用到的常量，包括某些指定的端口和UDP消息等
 * 
 * @author Troyal
 * 
 */
public class Constant {
	public static final int UDP_PORT = 1623;
	public static final int RUNNING_CHECK_PORT = 1978;
	public static final int DEFAULT_CHAT_PORT = 8824;
	public static final int CHAT_PORT_INCREASEMENT = 2;

	public static final String MESSAGE_IP_NOT_EXIST = "所要操作的IP不存在";
	public static final String MESSAGE_NOT_RUNNING = "您选择的用户没有启动ConnectU";
	public static final String MSEEAGE_IS_LOCALHOST = "不能和自己聊天";
	public static final String MESSAGE_CHATWINDOW_IS_RUNNING = "您与选择用户的聊天窗口已启动";
	public static final String MESSAGE_IP_ERROR = "您输入的IP地址不正确";
	public static final String MESSAGE_NETWORK_ERROR = "网络错误";
	public static final String BROADCAST_ADDRESS = "255.255.255.255";
	public static final String REGEX_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	public static final String MESSAGE_BROADCAST_LOGIN = "Login";
	public static final String MESSAGE_BROADCAST_LOGOFF = "Logoff";
	public static final String UDP_MESSAGE_CONNECT_PORT = "Connect TCP Port to:";
	public static final String UDP_MESSAGE_SUIT_PORT = "This TCP Port is OK:";
	public static final String UDP_MESSAGE_PORT_CHECK_OK = "TCP Port check OK:";
	public static final String UDP_MESSAGE_OPEN_PORT = "Open the port:";
	public static final String UDP_MESSAGE_PORT_OPENED = "TCP port has opened:";
	public static final String UDP_MESSAGE_USER_LOGINNED = "Has login:";
	public static final int MAX_RECEIVED_BUFFER = 1024;
}
