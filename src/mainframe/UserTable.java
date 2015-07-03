package mainframe;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.Color;
import java.util.*;

/**
 * 定义了显示在线用户的表格，并实现了对表格中条目的添加、删除等基本操作
 * 
 * @author Troyal
 * 
 */
public class UserTable extends JTable {
	/**
	 * 项目名称，表格的第一行
	 */
	private Vector<String> itemNames = new Vector<String>();
	/**
	 * 数据区域，位于名称下方
	 */
	private Vector<Vector<Vector<String>>> itemData = new Vector<Vector<Vector<String>>>();
	/**
	 * 每一个主机为数据区域中的一个条目
	 */
	private Vector<Vector<String>> item = null;
	/**
	 * 在线用户的列表
	 */
	private List<String> userList = null;
	/**
	 * 在线用户人数
	 */
	private UserCountLabel userCount = null;

	UserTable(UserCountLabel userCount) {
		super();
		this.userCount = userCount;
		userCount.setText(Constant.USER_COUNT_LABEL_NAME + itemData.size());
		setBounds(Constant.USER_TABLE_LOCATION_X,
				Constant.USER_TABLE_LOCATION_Y, Constant.USER_TABLE_WIDTH,
				Constant.USER_TABLE_HEIGHT);
		// 设定表格第一行的项目名称
		itemNames.add(Constant.USERTABLE_ITEMNAME_HOST);
		itemNames.add(Constant.USERTABLE_ITEMNAME_IP);
	}

	/**
	 * 向在线用户的表格中加入新的用户
	 * 
	 * @param userName
	 *            要加入的用户名
	 * @param userIP
	 *            要加入的IP地址
	 * @return 是否加入成功的boolean值
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
	 * 根据在线用户列表刷新在线用户表格
	 * 
	 * @param userList
	 *            在线用户列表
	 */
	public void refreshUserTable(List<String> userList) {
		this.userList = userList;
		// 删除表格中的所有数据
		if (itemData.size() != 0)
			itemData.removeAllElements();
		Vector<String> user = null;
		Vector<String> ip = null;
		// 将在线用户列表中的信息加入表格
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
	 * 依据给定的索引值返回在线用户表格中选中用户的IP地址
	 * 
	 * @param index
	 *            选中用户在表格中的索引值
	 * @return 所选用户的IP地址
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
	 * 依据给定的索引值返回在线用户表格中选中用户的主机名
	 * 
	 * @param index
	 *            选中用户在表格中的索引值
	 * @return 所选用户的主机名
	 */
	public String getSelectedUserName(int index) {
		return userList.get(index).split(":")[0];
	}
}
