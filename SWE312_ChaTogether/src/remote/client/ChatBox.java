package remote.client;

import remote.server.ServerInterface;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;


public class ChatBox extends javax.swing.JFrame implements Runnable{
    private Client client;
    private ServerInterface server;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private Vector<String> listClients;
    private String name;
    private GroupLayout groupLayout;
    
    //constructor
    public ChatBox(final String name,String autorization,final ServerInterface server) {
        initComponents();
        
        this.server = server;
        this.name = name;
        
        //detect the client group: simple user or admin to grant the admin permissions (activate,block,delete) clients
        if(autorization.equals("Administrator")){
            System.out.print(autorization);
            listConnect.setComponentPopupMenu(jPopupMenu1);
        }
        
        this.setLocationRelativeTo(null);
        this.setTitle("Chat (" + name + ")");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("chat.png")));
        
        groupLayout = new GroupLayout(jPanel1);
        jPanel1.setLayout(new GridLayout(100,1));
        jPanel1.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        //ask the customer before closing chat, if yes we delete it in the list of customers
        this.addWindowListener(new java.awt.event.WindowAdapter() {    
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(new JFrame(), 
                    "Are you sure to close this chat ?", "Close chat?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    try {
                        server.removeClient(name);
                        server.release();
                    } catch (RemoteException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    System.exit(0);
                }else{
                   
                }
            }
        });
        
        //a placeholder on the textfield to send message
        inputMsg.setForeground(Color.GRAY);
        inputMsg.setText("Enter your Message ...");
        inputMsg.addFocusListener(new FocusListener() {
        @Override
         public void focusGained(FocusEvent e) {
            if (inputMsg.getText().equals("Enter your Message ...")) {
                inputMsg.setText("");
                inputMsg.setForeground(Color.BLACK);
            }
        }
        @Override
         public void focusLost(FocusEvent e) {
            if (inputMsg.getText().isEmpty()) {
                inputMsg.setForeground(Color.GRAY);
                inputMsg.setText("Enter your Message ...");
            }
        }
        });
        
        //a list that contains the names of the connected clients
        listClients = new Vector<>();
        listConnect.setListData(listClients);
        
        try{
            client = new Client(name,server,inputMsg,listMessage,jPanel1);
        } catch (RemoteException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        //every 20s will refresh the list of connected clients
        Timer minuteur = new Timer();
        TimerTask tache = new TimerTask() {
            @Override
            public void run() {
                try {
                    int[] indices = listConnect.getSelectedIndices();
                    model.clear();
                    listClients = server.getListClientByName(name);
                    int i=0;
                    while(i<listClients.size()){
                        model.addElement(listClients.get(i));
                        i++;
                    }
                    listConnect.setModel(model);
                    listConnect.setSelectedIndices(indices);
                } catch (RemoteException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        };
        minuteur.schedule(tache,0,20000);
    }
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        listConnect = new javax.swing.JList<String>();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputMsg = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listMessage = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jMenuItem2.setText("Block Users");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("Reactive Users");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        listConnect.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        listConnect.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listConnect.setToolTipText("");
        jScrollPane1.setViewportView(listConnect);

        inputMsg.setColumns(20);
        inputMsg.setRows(5);
        inputMsg.setToolTipText("Enter your Message ...");
        inputMsg.setMargin(new java.awt.Insets(6, 0, 0, 16));
        jScrollPane2.setViewportView(inputMsg);
        inputMsg.getAccessibleContext().setAccessibleName("Enter your Message ...");

        btnSend.setBackground(new java.awt.Color(255, 255, 255));
        btnSend.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/remote/client/send.png"))); // NOI18N
        btnSend.setBorderPainted(false);
        btnSend.setContentAreaFilled(false);
        btnSend.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        listMessage.setEditable(false);
        listMessage.setColumns(20);
        listMessage.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        listMessage.setRows(5);
        listMessage.setRequestFocusEnabled(false);
        jScrollPane3.setViewportView(listMessage);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setText("Friends List");

        jButton1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/remote/client/refresh.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/remote/client/upload.png"))); // NOI18N
        jButton3.setToolTipText("upload File");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton3.setDefaultCapable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(jPanel1);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 204));
        jLabel1.setText("Uploaded");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        jLabel3.setText(" Messages");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 204));
        jLabel4.setText(" Input Pane");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane4))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //Click on the "send" button to send a message. Check whether the message is empty before sending
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        if(!inputMsg.getText().equals("")){
            if(!inputMsg.getText().equals("Enter your Message ...")){
                client.sendMessage(listConnect.getSelectedValuesList());
                inputMsg.setText("");
            }else{
            JOptionPane.showMessageDialog(this,"Please insert something to set your message","Alert",JOptionPane.WARNING_MESSAGE);
        }
        }else{
            JOptionPane.showMessageDialog(this,"Please insert something to send your message","Alert",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnSendActionPerformed

    // client list refresh button (refresh button) (used by threads)
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Thread thread = new Thread(this);
        thread.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    //Action on the popup menu "blocker clients"
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            server.blockClient(listConnect.getSelectedValuesList());
        } catch (RemoteException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    //action on the popup menu ""activer clients"
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try {
            server.reactiveClient(listConnect.getSelectedValuesList());
        } catch (RemoteException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    //action on the button "send file", first verified is what this file verified the extensions available before sending
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            String[] extension = file.getName().split("\\.");
            System.out.println(extension.length);
            if(extension[extension.length - 1].equals("txt")||
                extension[extension.length - 1].equals("java")||
                extension[extension.length - 1].equals("php")||
                extension[extension.length - 1].equals("c")||
                extension[extension.length - 1].equals("cpp")||
                extension[extension.length - 1].equals("xml")||
                extension[extension.length - 1].equals("exe")||
                extension[extension.length - 1].equals("png")||
                extension[extension.length - 1].equals("jpg")||
                extension[extension.length - 1].equals("jpeg")||
                extension[extension.length - 1].equals("pdf")||
                extension[extension.length - 1].equals("jar")||
                extension[extension.length - 1].equals("rar")||
                extension[extension.length - 1].equals("zip")
            ){
                try {
                    ArrayList<Integer> inc;
                    try (FileInputStream in = new FileInputStream(file)) {
                        inc = new ArrayList<>();
                        int c=0;
                        while((c=in.read()) != -1) {
                            inc.add(c);
                        }
                        in.close();
                    }
                    server.broadcastMessage(inc, listClients,file.getName());
                    String replicaFileName;
                    if(extension[extension.length - 1].equals("txt")||
                       extension[extension.length - 1].equals("php")||
                       extension[extension.length - 1].equals("cpp")||
                       extension[extension.length - 1].equals("xml")||
                       extension[extension.length - 1].equals("exe")||
                       extension[extension.length - 1].equals("png")||
                       extension[extension.length - 1].equals("jpg")||
                       extension[extension.length - 1].equals("pdf")||
                       extension[extension.length - 1].equals("jar")||
                       extension[extension.length - 1].equals("rar")||
                       extension[extension.length - 1].equals("zip")
                      ){
                        replicaFileName=file.getName().substring(0, file.getName().length()-4)+"_replica."+extension[extension.length - 1];
                        server.backUp(inc,replicaFileName);
                    }
                    if(extension[extension.length - 1].equals("c")){
                        replicaFileName=file.getName().substring(0, file.getName().length()-2)+"_replica."+extension[extension.length - 1];
                        server.backUp(inc,replicaFileName);
                    }
                    if(extension[extension.length - 1].equals("jpeg")||extension[extension.length - 1].equals("java")){
                        replicaFileName=file.getName().substring(0, file.getName().length()-5)+"_replica."+extension[extension.length - 1];
                        server.backUp(inc,replicaFileName);
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (RemoteException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

                JLabel jfile = new JLabel(file.getName() + " Uploaded ...");
                jPanel1.add(jfile);
                jPanel1.repaint();
                jPanel1.revalidate();
            }else{
                JOptionPane.showMessageDialog(this,"You can only upload files with these formats: xml,exe,jpg,png,jpeg,pdf,c,cpp,jar,java,txt,php ","Alert",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JTextArea inputMsg;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> listConnect;
    private javax.swing.JTextArea listMessage;
    // End of variables declaration//GEN-END:variables

    //the thread function for the "refresh" button
    @Override
    public void run() {
        try {
            //System.out.println(server.getListClientByName(nom+3).size());
            model.clear();
            listClients = server.getListClientByName(name);
            int i=0;
            while(i<listClients.size()){
                model.addElement(listClients.get(i));
                i++;
            }
            listConnect.setModel(model);
        } catch (RemoteException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}