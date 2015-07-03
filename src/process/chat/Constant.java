package process.chat;

/**
 * ����process.chat���õ��ĳ�������Ҫ����������Ϣ��һЩ��ʾ��Ϣ
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
	
	public static final String ERROR_MESSAGE_CHATWINDOW_NOT_RUNNING = "�Է��Ѿ��ر�����Ϣ���ڣ����������ӣ�";
	public static final String ERROR_MESSAGE_FILESEND_CANCELED = "�Է�ȡ�����ļ�����";
	public static final String ERROR_MESSAGE_FILERECEIVE_CANCELED = "�Է�ȡ�����ļ�����";
	public static final String CONFIRM_MESSAGE_CANCEL_SENDFILE = "�ļ������У�ȷ��ȡ����";
	public static final String CONFIRM_MESSAGE_CANCEL_RECEIVEFILE = "�ļ������У�ȷ��ȡ����";
	public static final String CONFIRM_MESSAGE_FILE_RECEIVED = "�ļ��������";
	public static final String CONFIRM_MESSAGE_FILE_SENT = "�ļ��������";
}
