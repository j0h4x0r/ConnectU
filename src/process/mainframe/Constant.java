package process.mainframe;

/**
 * ����process.mainframe���õ��ĳ���������ĳЩָ���Ķ˿ں�UDP��Ϣ��
 * 
 * @author Troyal
 * 
 */
public class Constant {
	public static final int UDP_PORT = 1623;
	public static final int RUNNING_CHECK_PORT = 1978;
	public static final int DEFAULT_CHAT_PORT = 8824;
	public static final int CHAT_PORT_INCREASEMENT = 2;

	public static final String MESSAGE_IP_NOT_EXIST = "��Ҫ������IP������";
	public static final String MESSAGE_NOT_RUNNING = "��ѡ����û�û������ConnectU";
	public static final String MSEEAGE_IS_LOCALHOST = "���ܺ��Լ�����";
	public static final String MESSAGE_CHATWINDOW_IS_RUNNING = "����ѡ���û������촰��������";
	public static final String MESSAGE_IP_ERROR = "�������IP��ַ����ȷ";
	public static final String MESSAGE_NETWORK_ERROR = "�������";
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
