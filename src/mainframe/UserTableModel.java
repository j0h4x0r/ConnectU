package mainframe;

import javax.swing.table.*;
import java.util.*;

/**
 * Ϊ�����û�����ṩ����ģ�ͣ� ������չ��AbstractTableModel��
 * 
 * @author Troyal
 * 
 */
class UserTableModel extends AbstractTableModel {
	/**
	 * ��Ŀ���ƣ����ĵ�һ��
	 */
	private Vector<String> itemNames = null;
	/**
	 * ��������λ�������·�
	 */
	private Vector<Vector<Vector<String>>> itemData = new Vector<Vector<Vector<String>>>();

	UserTableModel(Vector<String> itemNames,
			Vector<Vector<Vector<String>>> itemData) {
		this.itemNames = itemNames;
		this.itemData = itemData;
	}

	/**
	 * �����м����������Ŀ
	 * 
	 * @param item
	 *            Ҫ�������Ŀ
	 */
	public void addColumn(Vector<Vector<String>> item) {
		itemData.add(item);
	}

	/**
	 * ɾ������и�������Ŀ
	 * 
	 * @param item
	 *            Ҫɾ������Ŀ
	 */
	public void removeColumn(Vector<Vector<String>> item) {
		itemData.remove(item);
	}

	public int getRowCount() {
		return itemData.size();
	}

	public int getColumnCount() {
		return itemNames.size();
	}

	public Object getValueAt(int row, int column) {
		return itemData.get(row).get(column);
	}

	public String getColumnName(int col) {
		return itemNames.get(col);
	}
}
