package chatframe;

import javax.swing.*;

/**
 * 此类为传输文件的进度条，占一个线程
 * 
 * @author Troyal
 * 
 */
public class TransferProcessBar extends JProgressBar implements Runnable {
	/**
	 * 当前的进度变化量
	 */
	private int margin = 0;

	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * 不断更新进度条的显示
	 */
	public void run() {
		int value = getValue();
		setValue(value + margin);
	}
}
