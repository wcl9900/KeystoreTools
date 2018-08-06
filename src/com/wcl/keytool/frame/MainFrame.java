package com.wcl.keytool.frame;

import com.wcl.keytool.dialog.AboutDialog;
import com.wcl.keytool.dialog.PassWordDialog;
import com.wcl.keytool.entity.FileEntity;
import com.wcl.keytool.utils.CacheOpenUtils;
import com.wcl.keytool.utils.FileMDFive;
import sun.security.util.Debug;
import sun.security.x509.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>Describe:
 * <p>Author:王春龙
 * <p>CreateTime:2017/8/28
 */
public class MainFrame extends JFrame{

    private JPanel jPanelContent;
    private GridBagLayout gridBagLayout;
    private GridBagConstraints gridBagConstraints;
    private JLabel jLabelFilePath;

    public MainFrame(){
        init();
    }

    private void init(){
        URL resource = getClass().getResource("/com/wcl/keytool/icon/key-icon.png");
        setIconImage(getToolkit().createImage(resource));

        setTitle("KeystoreTools");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 580;
        int height = 440;
        setSize(width, height);
        setResizable(false);
        setLocation((getToolkit().getScreenSize().width - width)/2, (getToolkit().getScreenSize().height - height)/2);

        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jMenuBar.setBorderPainted(true);
        jMenuBar.setBorder(new MetalBorders.MenuBarBorder());
        jMenuBar.setUI(new BasicMenuBarUI());
        jMenuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JMenu jMenuFile = new JMenu("文件");
        JMenuItem jMenuItemOpenFile = new JMenuItem("打开文件");
        jMenuFile.add(jMenuItemOpenFile);

        jMenuItemOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
                jfc.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if(f.isDirectory()) return true;
                        String[] split = f.getAbsolutePath().split(".");
                        if(split == null || split.length == 0) return false;
                        String fileExName = split[split.length - 1];
                        if(fileExName.equals("jks") || fileExName.equals("keystore")) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "签名文件";
                    }
                });
                jfc.showOpenDialog(MainFrame.this);
                File file=jfc.getSelectedFile();
                if(file == null) return;

                if(file.isFile()){
                    showPassWordDialog(file);
                }
            }
        });

        JMenuItem jMenuItemExit = new JMenuItem("退出程序");
        jMenuFile.add(jMenuItemExit);
        jMenuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu jMenuHelp = new JMenu("帮助");
        JMenuItem jMenuItemAbout = new JMenuItem("关于");
        jMenuHelp.add(jMenuItemAbout);

        jMenuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog(MainFrame.this).setVisible(true);
            }
        });

        jMenuBar.add(jMenuFile);
        jMenuBar.add(jMenuHelp);

        jPanelContent = new JPanel();
        initContentJPanel(jPanelContent);

        JPanel jPanel = new JPanel(new BorderLayout());
//        jPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
//        jPanel.add(jMenuBar, BorderLayout.NORTH);
        jPanel.add(jPanelContent, BorderLayout.CENTER);

        jLabelFilePath = new JLabel();
        jPanel.add(jLabelFilePath, BorderLayout.NORTH);

        setLayout(new BorderLayout());
        add(jMenuBar, BorderLayout.NORTH);
        add(jPanel, BorderLayout.CENTER);
        setVisible(true);
        drag();
    }

    private void showPassWordDialog(File file) {
        FileEntity fileEntity = CacheOpenUtils.get(FileMDFive.getFileMD5(file));
        String password;
        String alias;
        String aliasPassWord = null;
        if(fileEntity == null) {
            PassWordDialog passWordDialog = new PassWordDialog(MainFrame.this);
            passWordDialog.setVisible(true);
            if (!passWordDialog.isCanEnable()) {
                return;
            }
            password = passWordDialog.getPassWord();
            alias = passWordDialog.getAlias();
            aliasPassWord = passWordDialog.getAliasPassWord();
        }
        else {
            alias = fileEntity.getAlias();
            password = fileEntity.getPassword();
        }
        showInfo(file.getAbsolutePath(), password, alias, aliasPassWord);
    }

    private void showInfo(String filePath, String password, String alias, String aliasPassWord){
        jLabelFilePath.setText(filePath);

        jPanelContent.removeAll();

        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, password.toCharArray());

            String aliasTemp = alias;
            String aliasPassWordTemp = aliasPassWord;
            X509CertImpl certificate = (X509CertImpl) keyStore.getCertificate(aliasTemp);

            addInfoComponent("Version", certificate.getVersion()+"", 0);

            Field infoField = X509CertImpl.class.getDeclaredField("info");
            infoField.setAccessible(true);
            X509CertInfo info = (X509CertInfo) infoField.get(certificate);

            addInfoComponent("Subject", info.get("subject").toString(), 1);
            addInfoComponent("Issuer", info.get("issuer").toString(), 2);
            Field serialNumField = X509CertInfo.class.getDeclaredField("serialNum");
            serialNumField.setAccessible(true);
            CertificateSerialNumber serialNumber = (CertificateSerialNumber) serialNumField.get(info);
            addInfoComponent("Serial Number", Debug.toHexString(serialNumber.get("number").getNumber()).replace(" ", ""), 3);

            Field intervalField = X509CertInfo.class.getDeclaredField("interval");
            intervalField.setAccessible(true);
            CertificateValidity certificateValidity = (CertificateValidity) intervalField.get(info);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
            Date notBefore = certificateValidity.get("notBefore");
            Date notAfter = certificateValidity.get("notAfter");
            addInfoComponent("Valid From", simpleDateFormat.format(notBefore), 4);
            addInfoComponent("Valid Until", simpleDateFormat.format(notAfter), 5);

            Field pubKeyField = X509CertInfo.class.getDeclaredField("pubKey");
            pubKeyField.setAccessible(true);
            CertificateX509Key pubKey = (CertificateX509Key) pubKeyField.get(info);
            String pubKeyStr = pubKey.toString().split("\n")[0];
            addInfoComponent("Public Key", pubKeyStr, 6);

            Field algIdField = X509CertImpl.class.getDeclaredField("algId");
            algIdField.setAccessible(true);
            AlgorithmId algorithmId = (AlgorithmId) algIdField.get(certificate);
            addInfoComponent("Signature Algorithm", algorithmId.toString(), 7);
            addInfoComponent("MD5 FingerPrint", md5Stand(certificate.getFingerprint("MD5")), 8);
            addInfoComponent("", md5Lower(certificate.getFingerprint("MD5")), 9);
            addInfoComponent("SHA1 FingerPrint", md5Stand(certificate.getFingerprint("SHA1")), 10);

            jPanelContent.updateUI();
            CacheOpenUtils.put(FileMDFive.getFileMD5(new File(filePath)), alias, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "密码输入错误！");
            showPassWordDialog(new File(filePath));
        }
    }

    private String md5Lower(String md5){
        return md5.replace(":","").toLowerCase();
    }
    private String md5Stand(String md5){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < md5.length(); i++){
            stringBuilder.append(md5.charAt(i));
            if(i % 2 != 0 && i < md5.length() - 1){
                stringBuilder.append(":");
            }
        }
        return stringBuilder.toString().toUpperCase();
    }

    private void initContentJPanel(JPanel jPanelContent) {
        gridBagLayout = new GridBagLayout();
        gridBagConstraints = new GridBagConstraints();
        jPanelContent.setLayout(gridBagLayout);
    }

    private void addInfoComponent(String key, String value, int row){
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = row;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(4,0,4,0);
        String keyString = key != null && key.length() != 0 ? key + "：" :"";
        JLabel jLabelKey = new JLabel(keyString);
        gridBagLayout.setConstraints(jLabelKey, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = row;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        JTextField jTextFieldValue = new JTextField(value);
        jTextFieldValue.setEditable(false);
        gridBagLayout.setConstraints(jTextFieldValue, gridBagConstraints);

        jPanelContent.add(jLabelKey);
        jPanelContent.add(jTextFieldValue);
    }

    public void drag()//定义的拖拽方法
    {
        //panel表示要接受拖拽的控件
        new DropTarget(jPanelContent, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde)//重写适配器的drop方法
            {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
                        List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        if(list.size() != 0) {
                            showPassWordDialog(list.get(0));
                        }
                        dtde.dropComplete(true);//指示拖拽操作已完成
                    } else {
                        dtde.rejectDrop();//否则拒绝拖拽来的数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }
}
