package JSP行为.plugin;

import javax.swing.*;
import java.awt.*;

public class MyApplet extends JApplet {
    String img;

    @Override
    public void paint(Graphics g) {
        Image image = getImage(getCodeBase(), img);
        //绘制一张图片
        g.drawImage(image, 0, 0, 400, 400, this);
        g.setColor(Color.blue);
        g.setFont(new Font("宋体", 2, 24));
        //绘制一个字符串
        g.drawString("jsp:plugin-test", 40, 170);
        g.setColor(Color.pink);
        g.setFont(new Font("NewsRoman", 2, 10));
        //绘制一个日期字符串
        g.drawString(new java.util.Date().toString(), 10, 109);
    }

    @Override
    public void init() {
        //获取plugin指令中的参数
        img = getParameter("image");
    }
}