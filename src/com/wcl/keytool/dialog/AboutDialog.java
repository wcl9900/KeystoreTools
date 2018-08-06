package com.wcl.keytool.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Describe:
 * <p>Author:王春龙
 * <p>CreateTime:2017/8/28
 */
public class AboutDialog extends JDialog{
    public AboutDialog(Frame owner) {
        super(owner);
        init();
    }

    private void init(){
        setTitle("关于");
        int width = 300;
        int height = 200;
        setSize(width, height);
        Point locationOwner = getOwner().getLocation();
        Dimension sizeOwner = getOwner().getSize();
        setLocation(locationOwner.x + (sizeOwner.width - width)/2, locationOwner.y + (sizeOwner.height - height)/2);

        setResizable(false);

        JLabel jTextArea = new JLabel();
        jTextArea.setText("制作者:王春龙");

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(jTextArea, FlowLayout.LEFT);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel, BorderLayout.CENTER);
    }
}
