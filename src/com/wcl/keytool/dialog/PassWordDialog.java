package com.wcl.keytool.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * <p>Describe:
 * <p>Author:王春龙
 * <p>CreateTime:2017/8/28
 */
public class PassWordDialog extends JDialog implements ActionListener{

    private JTextField jTextFieldPassWord;
    private JTextField jTextFieldAlias;
    private JTextField jTextFieldAliasPassWord;
    private int width;
    private int height;
    private JButton jButtonSure;
    private JButton jButtonCancel;

    boolean canEnable = false;

    public PassWordDialog(Frame owner) {
        super(owner);
        setModal(true);
        init();
    }

    private void init(){
        setTitle("密码");
        width = 240;
        height = 180;
        setSize(width, height);
        setResizable(false);
        Point locationOwner = getOwner().getLocation();
        Dimension sizeOwner = getOwner().getSize();
        setLocation(locationOwner.x + (sizeOwner.width - width)/2, locationOwner.y + (sizeOwner.height - height)/2);

        JPanel frame = new JPanel(new BorderLayout());

        JPanel jPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        jPanel.setLayout(gridBagLayout);

        gridBagConstraints.insets = new Insets(2,2,2,2);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.fill = GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0;
        JLabel jLabelPassword = new JLabel("密码:");
        gridBagLayout.setConstraints(jLabelPassword, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        jTextFieldPassWord = new JTextField();
        gridBagLayout.setConstraints(jTextFieldPassWord, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.fill = GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0;
        JLabel jLabelAlias = new JLabel("别名:");
        gridBagLayout.setConstraints(jLabelAlias, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        jTextFieldAlias = new JTextField();
        gridBagLayout.setConstraints(jTextFieldAlias, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.fill = GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0;
        JLabel jLabelAliasPassword = new JLabel("别名密码:");
        gridBagLayout.setConstraints(jLabelAliasPassword, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        jTextFieldAliasPassWord = new JTextField();
        gridBagLayout.setConstraints(jTextFieldAliasPassWord, gridBagConstraints);

        jPanel.add(jLabelAlias);
        jPanel.add(jTextFieldAlias);

        jPanel.add(jLabelPassword);
        jPanel.add(jTextFieldPassWord);

//        jPanel.add(jLabelAliasPassword);
//        jPanel.add(jTextFieldAliasPassWord);


        JPanel jPanelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jButtonSure = new JButton("确定");
        jButtonCancel = new JButton("取消");
        jPanelButton.add(jButtonSure);
        jPanelButton.add(jButtonCancel);

        jButtonSure.addActionListener(this);
        jButtonCancel.addActionListener(this);

        frame.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        frame.add(jPanel, BorderLayout.CENTER);
        frame.add(jPanelButton, BorderLayout.SOUTH);
        add(frame);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    canEnable = true;
                    setVisible(false);
                }
            }
        });
    }

    public String getPassWord(){
        return jTextFieldPassWord.getText();
    }

    public String getAlias(){
        return jTextFieldAlias.getText();
    }

    public String getAliasPassWord(){
        return jTextFieldAliasPassWord.getText();
    }

    public boolean isCanEnable() {
        return canEnable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jButtonSure){
            canEnable = true;
        }
        else
        if(e.getSource() == jButtonCancel){
            canEnable = false;
        }
        setVisible(false);
    }
}
