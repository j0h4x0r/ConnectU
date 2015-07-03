package mainframe;

import javax.swing.*;

/**
 * 此标签用于显示在线人数， 此标签的内容设置被放在userTable类中
 * 
 * @author Troyal
 * 
 */
class UserCountLabel extends JLabel {
	UserCountLabel() {
		setBounds(Constant.USER_COUNT_LABEL_LOCATION_X,
				Constant.USER_COUNT_LABEL_LOCATION_Y,
				Constant.USER_COUNT_LABEL_WIDTH,
				Constant.USER_COUNT_LABEL_HEIGHT);
	}
}
