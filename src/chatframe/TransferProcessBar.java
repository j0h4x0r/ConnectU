package chatframe;

import javax.swing.*;

/**
 * ����Ϊ�����ļ��Ľ�������ռһ���߳�
 * 
 * @author Troyal
 * 
 */
public class TransferProcessBar extends JProgressBar implements Runnable {
	/**
	 * ��ǰ�Ľ��ȱ仯��
	 */
	private int margin = 0;

	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * ���ϸ��½���������ʾ
	 */
	public void run() {
		int value = getValue();
		setValue(value + margin);
	}
}
