package generator.window;

import java.awt.*;

/**
 * 
 * @author sunyu
 *
 */
public class FrameTool {
    /**
     * 获得本地屏幕的大小。
     *
     * @return Dimension对象
     */
    public static Dimension getScreenDimension() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenDimension = kit.getScreenSize();
        return screenDimension;
    }

    /**
     * 将窗口居中显示
     * @param parent
     * @param child
     */
    public static void centerContainer(Container parent, Container child) {
        //取屏幕大小
        Dimension screenSize = getScreenDimension();
        int x = 0;
        int y = 0;
        int height = screenSize.height;
        int width = screenSize.width;
        
        if (parent != null && parent.isVisible()) {
            x = parent.getX();
            y = parent.getY();
            height = parent.getHeight();
            width = parent.getWidth();
        }
        child.setLocation(x + (width - child.getWidth()) / 2,
                y + (height - child.getHeight()) / 2);
    }
}
