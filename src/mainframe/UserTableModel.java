package mainframe;

import javax.swing.table.*;
import java.util.*;

/**
 * 为在线用户表格提供数据模型， 此类扩展自AbstractTableModel类
 * 
 * @author Troyal
 * 
 */
class UserTableModel extends AbstractTableModel {
	/**
	 * 项目名称，表格的第一行
	 */
	private Vector<String> itemNames = null;
	/**
	 * 数据区域，位于名称下方
	 */
	private Vector<Vector<Vector<String>>> itemData = new Vector<Vector<Vector<String>>>();

	UserTableModel(Vector<String> itemNames,
			Vector<Vector<Vector<String>>> itemData) {
		this.itemNames = itemNames;
		this.itemData = itemData;
	}

	/**
	 * 向表格中加入给定的条目
	 * 
	 * @param item
	 *            要加入的条目
	 */
	public void addColumn(Vector<Vector<String>> item) {
		itemData.add(item);
	}

	/**
	 * 删除表格中给定的条目
	 * 
	 * @param item
	 *            要删除的条目
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
