package mainframe;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.Color;
import java.util.*;

/**
 * ��������ʾ�����û��ı�񣬲�ʵ���˶Ա������Ŀ����ӡ�ɾ���Ȼ�������
 * 
 * @author Troyal
 * 
 */
public class UserTable extends JTable {
	/**
	 * ��Ŀ���ƣ����ĵ�һ��
	 */
	private Vector<String> itemNames = new Vector<String>();
	/**
	 * ��������λ�������·�
	 */
	private Vector<Vector<Vector<String>>> itemData = new Vector<Vector<Vector<String>>>();
	/**
	 * ÿһ������Ϊ���������е�һ����Ŀ
	 */
	private Vector<Vector<String>> item = null;
	/**
	 * �����û����б�
	 */
	private List<String> userList = null;
	/**
	 * �����û�����
	 */
	private UserCountLabel userCount = null;

	UserTable(UserCountLabel userCount) {
		super();
		this.userCount = userCount;
		userCount.setText(Constant.USER_COUNT_LABEL_NAME + itemData.size());
		setBounds(Constant.USER_TABLE_LOCATION_X,
				Constant.USER_TABLE_LOCATION_Y, Constant.USER_TABLE_WIDTH,
				Constant.USER_TABLE_HEIGHT);
		// �趨����һ�е���Ŀ����
		itemNames.add(Constant.USERTABLE_ITEMNAME_HOST);
		itemNames.add(Constant.USERTABLE_ITEMNAME_IP);
	}

	/**
	 * �������û��ı���м����µ��û�
	 * 
	 * @param userName
	 *            Ҫ������û���
	 * @param userIP
	 *            Ҫ�����IP��ַ
	 * @return �Ƿ����ɹ���booleanֵ
	 */
	public boolean addUser(String userName, String userIP) {
		boolean ifSuccess;
		if (userList == null)
			userList = new ArrayList<String>();
		if (!userList.contains(userName + ":" + userIP)
				&& !userList.contains(Constant.UNKNOWN_USERNAME + ":" + userIP)) {
			userList.add(Constant.UNKNOWN_USERNAME + ":" + userIP);
			Vector<String> name = new Vector<String>();
			Vector<String> ip = new Vector<String>();
			name.add(userName);
			ip.add(userIP);
			item = new Vector<Vector<String>>();
			item.add(name);
			item.add(ip);
			itemData.add(item);

			UserTableModel model = new UserTableModel(itemNames, itemData);
			setModel(model);
			userCount.setText(Constant.USER_COUNT_LABEL_NAME + itemData.size());

			ifSuccess = true;
		} else {
			ifSuccess = false;
		}
		return ifSuccess;
	}

	/**
	 * ���������û��б�ˢ�������û����
	 * 
	 * @param userList
	 *            �����û��б�
	 */
	public void refreshUserTable(List<String> userList) {
		this.userList = userList;
		// ɾ������е���������
		if (itemData.size() != 0)
			itemData.removeAllElements();
		Vector<String> user = null;
		Vector<String> ip = null;
		// �������û��б��е���Ϣ������
		for (String message : userList) {
			user = new Vector<String>();
			ip = new Vector<String>();
			user.add(message.split(":")[0]);
			ip.add(message.split(":")[1]);
			item = new Vector<Vector<String>>();
			item.add(user);
			item.add(ip);
			itemData.add(item);
		}
		UserTableModel model = new UserTableModel(itemNames, itemData);
		setModel(model);
		userCount.setText(Constant.USER_COUNT_LABEL_NAME + itemData.size());
	}

	public int getUserCount() {
		return userList.size();
	}

	public void addItem(Vector<Vector<String>> item) {
		itemData.add(item);
	}

	public void removeItem(Vector<Vector<String>> item) {
		itemData.remove(item);
	}

	/**
	 * ���ݸ���������ֵ���������û������ѡ���û���IP��ַ
	 * 
	 * @param index
	 *            ѡ���û��ڱ���е�����ֵ
	 * @return ��ѡ�û���IP��ַ
	 */
	public String getSelectedUserIPAddress(int index) {
		if (index == -1) {
			JOptionPane.showMessageDialog(this,
					Constant.MESSAGE_NOT_SELECT_USER, null,
					JOptionPane.ERROR_MESSAGE);
			return null;
		} else {
			return userList.get(index).split(":")[1];
		}
	}

	/**
	 * ���ݸ���������ֵ���������û������ѡ���û���������
	 * 
	 * @param index
	 *            ѡ���û��ڱ���е�����ֵ
	 * @return ��ѡ�û���������
	 */
	public String getSelectedUserName(int index) {
		return userList.get(index).split(":")[0];
	}
}
