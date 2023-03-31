import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


/*
 * 界面UI
 */
public class UI {
    JFrame jf;
    JPanel jp, jp_main;
    JButton jb_CreateNote, jb_CreateLine, jb_Delete, jb_O, jb_I, jb_matrix, jb_CreateChart;
    JFrame jf_createNode, jf_createLine;
    DFA dfa;
    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Arrows> arrows = new ArrayList<>();
    Map map = new Map();

    UI() {

        dfa = new DFA();
        jf = new JFrame("自动机的状态转换图");//创建窗口
        jf.setLayout(new BorderLayout());
        jp = new JPanel();
        jp.setBackground(Color.GRAY);
        jp_main = new JPanel();
        jp_main.setBackground(Color.WHITE);
        jf.add(BorderLayout.SOUTH, jp);
        jf.add(jp_main);
        jb_CreateNote = new JButton("添加节点");

        jb_CreateNote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNode();
            }
        });

        jb_CreateLine = new JButton("添加边");

        jb_CreateLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createLine();
            }
        });
        jb_Delete = new JButton("清屏");
        jb_Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jp_main.repaint();
                dfa = new DFA();
                map.nodes.clear();
                nodes.clear();
                arrows.clear();
            }
        });
        jb_O = new JButton("导出文件");
        jb_O.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createFileOut(jf);
            }
        });
        jb_I = new JButton("导入文件");
        jb_I.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jp_main.repaint();
                createFileIn(jf);
                drawMap(jp_main.getGraphics());
            }
        });
        jb_matrix = new JButton("状态转换矩阵");
        jb_matrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getMatrix();
            }
        });
        jb_CreateChart = new JButton("生成状态转换图");
        jb_CreateChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jp_main.repaint();
                createMapUI();
            }
        });
        jp.add(jb_CreateNote);
        jp.add(jb_CreateLine);
        jp.add(jb_Delete);
        jp.add(jb_O);
        jp.add(jb_I);
        jp.add(jb_matrix);
        jp.add(jb_CreateChart);
        //jf.setExtendedState(JFrame.MAXIMIZED_BOTH); //全屏显示
        jf.setSize(1300, 800);//设置窗口大小
        jf.setResizable(false); //最大化窗口无效，即不能缩放
        jf.setLocationRelativeTo(null); //窗口居中
        jf.setVisible(true);  //窗口可视化
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //关闭有效
    }

    /*
        创建结点UI
     */
    void createNode() {
        jf_createNode = new JFrame("创建结点");

        JLabel jl_name = new JLabel("结点名");
        JLabel jl_location = new JLabel("坐标");
        JLabel jl_temp = new JLabel("点击获取坐标!");
        JTextField jtf_name = new JTextField(22);
        JTextField jtf_x = new JTextField("x", 10);
        JTextField jtf_y = new JTextField("y", 10);
        jtf_x.setEditable(false);
        jtf_y.setEditable(false);
        JButton jb_ok = new JButton("确定");
        JCheckBox jcb_first = new JCheckBox("初态结点");
        JCheckBox jcb_last = new JCheckBox("终态结点");
        /*
            获取鼠标点击坐标
         */
        this.jp_main.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    jtf_x.setText(String.valueOf(e.getX()));
                    jtf_y.setText(String.valueOf(e.getY()));
                }
            }
        });
        /*
            确定键的触发器
         */
        jb_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = jp_main.getGraphics();
                g.setColor(Color.BLACK);
                if (!jtf_x.getText().equals("x")) {
                    if (!jtf_name.getText().equals("")) {
                        if (!dfa.K.contains(jtf_name.getText())) {
                            if (!(!dfa.S.equals("") && jcb_first.isSelected())) {
                                dfa.K.add(jtf_name.getText());
                                if (!jcb_first.isSelected() && !jcb_last.isSelected()) {
                                    Node node = new Node(Integer.parseInt(jtf_x.getText()), Integer.parseInt(jtf_y.getText()), jtf_name.getText(), 0);
                                    nodes.add(node);
                                    map.nodes.add(node);
                                    drawNode(g, Integer.parseInt(jtf_x.getText()), Integer.parseInt(jtf_y.getText()), jtf_name.getText());
                                }
                                if (jcb_first.isSelected()) {
                                    dfa.S = jtf_name.getText();
                                    Node node = new Node(Integer.parseInt(jtf_x.getText()), Integer.parseInt(jtf_y.getText()), jtf_name.getText(), 1);
                                    nodes.add(node);
                                    map.nodes.add(node);
                                    drawFirstNode(g, Integer.parseInt(jtf_x.getText()), Integer.parseInt(jtf_y.getText()), jtf_name.getText());
                                }
                                if (jcb_last.isSelected()) {
                                    dfa.Z.add(jtf_name.getText());
                                    Node node = new Node(Integer.parseInt(jtf_x.getText()), Integer.parseInt(jtf_y.getText()), jtf_name.getText(), 2);
                                    nodes.add(node);
                                    map.nodes.add(node);
                                    drawLastNode(g, Integer.parseInt(jtf_x.getText()), Integer.parseInt(jtf_y.getText()), jtf_name.getText());
                                }
                                jf_createNode.setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(jf_createNode, "已有初始结点", "错误提示", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(jf_createNode, "结点重复", "错误提示", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(jf_createNode, "未命名结点", "错误提示", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(jf_createNode, "未选择坐标", "错误提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        jf_createNode.add(jl_name);
        jf_createNode.add(jtf_name);
        jf_createNode.add(jl_location);
        jf_createNode.add(jtf_x);
        jf_createNode.add(jtf_y);
        jf_createNode.add(jcb_first);
        jf_createNode.add(jcb_last);
        jf_createNode.add(jb_ok);
        jf_createNode.add(jl_temp);

        jf_createNode.setSize(300, 200);
        jf_createNode.setLocation(jf.getLocation());
        jf_createNode.setVisible(true);
        jf_createNode.setResizable(false);
        jf_createNode.setAlwaysOnTop(true);
        jf_createNode.setLayout(new FlowLayout());
    }

    /*
        画普通结点
     */
    void drawNode(Graphics g, int x, int y, String name) {
        g.setColor(Color.BLACK);
        g.drawOval(x, y, 50, 50);
        g.setFont(new Font("宋体", Font.BOLD, 25));
        g.drawString(name, x + 19, y + 32);
    }

    /*
        画初始节点
     */
    void drawFirstNode(Graphics g, int x, int y, String name) {
        g.setColor(Color.GRAY);
        g.fillOval(x, y, 50, 50);
        g.setColor(Color.BLACK);
        g.setFont(new Font("宋体", Font.BOLD, 25));
        g.drawString(name, x + 19, y + 32);
    }

    /*
        画终态结点
     */
    void drawLastNode(Graphics g, int x, int y, String name) {
        drawNode(g, x, y, name);
        g.drawOval(x + 5, y + 5, 40, 40);
    }

    /*
        创建边UI
     */
    void createLine() {
        jf_createLine = new JFrame("创建边");
        JLabel jl_First = new JLabel("起点");
        JLabel jl_Last = new JLabel("终点");
        JLabel jl_LineName = new JLabel("输入符号");
        JTextField jtf_FirstNodeName = new JTextField(10);
        JTextField jtf_LastNodeName = new JTextField(10);
        JTextField jtf_LineName = new JTextField(20);
        JButton jb_ok = new JButton("确定");

        jb_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphics g = jp_main.getGraphics();
                if(dfa.K.contains(jtf_FirstNodeName.getText()) && dfa.K.contains(jtf_LastNodeName.getText())){
                    if(!jtf_LineName.getText().equals("")){
                        String fNode = jtf_FirstNodeName.getText();//起始结点名
                        String nNode = jtf_LastNodeName.getText();//终态结点名
                        Point fn = getPoint(fNode);//起始结点坐标
                        Point nn = getPoint(nNode);//终态结点坐标
                        /*将LineName切割获取内容*/
                        String[] lName = jtf_LineName.getText().split(",");//符号名
                        /*将边的信息保存到DFA和Arrows中*/
                        for (int i = 0; i < lName.length; i++) {
                            arrows.add(new Arrows(fNode,nNode,lName[i]));
                            map.arrows.add(new Arrows(fNode,nNode,lName[i]));
                            if(!dfa.A.contains(lName[i])) dfa.A.add(lName[i]);
                            int Node_num = dfa.K.indexOf(fNode);
                            int arrow_num = dfa.A.indexOf(lName[i]);
                            dfa.f[Node_num][arrow_num] = nNode;
                        }
                            drawArrows(fn.x,fn.y,nn.x,nn.y,g,jtf_LineName.getText());
                            jf_createLine.setVisible(false);
                    }else {
                        JOptionPane.showMessageDialog(jf_createNode, "边未添加字符", "错误提示", JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(jf_createNode, "初态/终态结点不存在", "错误提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        jf_createLine.add(jl_First);
        jf_createLine.add(jtf_FirstNodeName);
        jf_createLine.add(jl_Last);
        jf_createLine.add(jtf_LastNodeName);
        jf_createLine.add(jl_LineName);
        jf_createLine.add(jtf_LineName);
        jf_createLine.add(jb_ok);

        jf_createLine.setSize(300, 200);
        jf_createLine.setLocation(jf.getLocation());
        jf_createLine.setVisible(true);
        jf_createLine.setResizable(false);
        jf_createLine.setAlwaysOnTop(true);
        jf_createLine.setLayout(new FlowLayout());
    }

    /*
        画箭头
     */
    public static void drawAL(int sx, int sy, int ex, int ey,Graphics g,int temp,String s)

    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.black);
        double  H  =   6 ;  // 箭头高度
        double  L  =   6 ; // 底边的一半
        int  x3  =   0 ;
        int  y3  =   0 ;
        int  x4  =   0 ;
        int  y4  =   0 ;
        double  awrad  =  Math.atan(L  /  H);  // 箭头角度
        double  arraow_len  =  Math.sqrt(L  *  L  +  H  *  H); // 箭头的长度
        double [] arrXY_1  =  rotateVec(ex  -  sx, ey  -  sy, awrad,  true , arraow_len);
        double [] arrXY_2  =  rotateVec(ex  -  sx, ey  -  sy,  - awrad,  true , arraow_len);
        double  x_3  =  ex  -  arrXY_1[ 0 ];  // (x3,y3)是第一端点
        double  y_3  =  ey  -  arrXY_1[ 1 ];
        double  x_4  =  ex  -  arrXY_2[ 0 ]; // (x4,y4)是第二端点
        double  y_4  =  ey  -  arrXY_2[ 1 ];

        Double X3  =   x_3;
        x3  =  X3.intValue();
        Double Y3  =   y_3;
        y3  =  Y3.intValue();
        Double X4  =   x_4;
        x4  =  X4.intValue();
        Double Y4  =  y_4;
        y4  =  Y4.intValue();
        // g.setColor(SWT.COLOR_WHITE);
        // 画线
        g.setFont(new Font("宋体", Font.BOLD, 15));
        if(temp == 0){
            g2.drawLine(sx, sy, ex, ey);
            g2.drawString(s,(sx+ex)/2,(sy+ey)/2);
        } else {
            g2.drawArc(sx,sy,50,50,0,270);
            g2.drawString(s,sx,sy);
        }
        // 画箭头的一半
        g2.drawLine(ex, ey, x3, y3);
        // 画箭头的另一半
        g2.drawLine(ex, ey, x4, y4);

    }

    //计算
    public  static double [] rotateVec( int  px,  int  py,  double  ang,  boolean  isChLen,
                                        double  newLen)   {

        double  mathstr[]  =   new   double [ 2 ];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double  vx  =  px  *  Math.cos(ang)  -  py  *  Math.sin(ang);
        double  vy  =  px  *  Math.sin(ang)  +  py  *  Math.cos(ang);
        if  (isChLen)   {
            double  d  =  Math.sqrt(vx  *  vx  +  vy  *  vy);
            vx  =  vx  /  d  *  newLen;
            vy  =  vy  /  d  *  newLen;
            mathstr[ 0 ]  =  vx;
            mathstr[ 1 ]  =  vy;
        }
        return  mathstr;
    }

    /*
        画不同位置画箭头方法
     */
    public void drawArrows(int x1,int y1,int x2,int y2,Graphics g,String s){
        Point p1_up = new Point(x1 + 25,y1);
        Point p1_down = new Point(x1 + 25,y1 + 50);
        Point p1_left = new Point(x1,y1 + 25);
        Point p1_right = new Point(x1 + 50,y1 + 25);

        Point p2_up = new Point(x2 + 25,y2);
        Point p2_down = new Point(x2 + 25,y2 + 50);
        Point p2_left = new Point(x2,y2 + 25);
        Point p2_right = new Point(x2 + 50,y2 + 25);

        if (p1_left.getX() == p2_left.getX() && p1_left.getY() == p2_left.getY()){
            drawAL(p1_up.x-50,p1_up.y-25, p1_left.x, p1_left.y, g,1,s);
        }else if(p1_left.getX() <= p2_left.getX() && p1_right.getY() >= p2_left.getY()){
            drawAL(p1_up.x, p1_up.y, p2_left.x, p2_left.y, g,0,s);
        }else if(p1_right.getX() > p2_right.getX() && p1_left.getY() >= p2_right.getY()){
            drawAL(p1_up.x, p1_up.y, p2_right.x,p2_right.y,g,0,s);
        }else if(p1_left.getX() <= p2_left.getX() && p1_right.getY() < p2_left.getY()){
            drawAL(p1_down.x, p1_down.y,p2_left.x,p2_left.y,g,0,s);
        }else if(p1_right.getX() > p2_right.getX() && p1_left.getY() < p2_right.getY()){
            drawAL(p1_down.x,p1_down.y,p2_right.x,p2_right.y,g,0,s);
        }


    }




    /*
        已知结点名ArrayListNode的坐标获取
     */
    Point getPoint(String s){
        for (int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).ns.equals(s)){
               return new Point(nodes.get(i).x,nodes.get(i).y);
            }
        }
        return null;
    }




    /*
        创建保存文件的UI
     */
    void createFileOut(JFrame jf){
        File OFile = showFile(jf,1);
        try {
            FileOutputStream fos = new FileOutputStream(OFile);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(map);
                oos.close();
                fos.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(jf_createNode, "保存失败", "错误提示", JOptionPane.ERROR_MESSAGE);;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(jf_createNode, "未找到文件", "错误提示", JOptionPane.ERROR_MESSAGE);
        }
    }


    /*
        创建导出文件的UI
     */
    void createFileIn(JFrame jf){
       File IFile = showFile(jf,0);
        try {
            FileInputStream fis = new FileInputStream(IFile);
            try {
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    map = (Map) ois.readObject();
                    nodes.addAll(map.nodes);
                    arrows.addAll(map.arrows);
                    dfa = getDfa();
                    ois.close();
                    fis.close();
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(jf_createNode, "读取失败1", "错误提示", JOptionPane.ERROR_MESSAGE);;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(jf_createNode, "读取失败2", "错误提示", JOptionPane.ERROR_MESSAGE);;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(jf_createNode, "未找到文件", "错误提示", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
        将文件地址返回
     */
    File showFile(Component parent,int temp){
        File file = null;
        //创建一个文件存储器
        JFileChooser jfc = new JFileChooser();
        //默认显示文件夹为初始
        jfc.setCurrentDirectory(new File("."));
        //模式,只可选择文件
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //是否可以多选,否
        jfc.setMultiSelectionEnabled(false);
        //文件过滤，只保留txt文档
        jfc.setFileFilter(new FileNameExtensionFilter("txt(*.txt)","txt"));

        int result;
        if(temp == 0)
            result = jfc.showOpenDialog(parent);
        else
            result = jfc.showSaveDialog(parent);

        if(result == JFileChooser.APPROVE_OPTION){
            file = jfc.getSelectedFile();
        }
        return file;
    }

    /*
        根据Map画图
     */
    void drawMap(Graphics g){
        for (int i = 0;i < map.nodes.size();i++){
            switch (map.nodes.get(i).temp){
                case 0:{
                    drawNode(g,map.nodes.get(i).x,map.nodes.get(i).y,map.nodes.get(i).ns);
                    break;
                } case 1:{
                    drawFirstNode(g,map.nodes.get(i).x,map.nodes.get(i).y,map.nodes.get(i).ns);
                    break;
                } case 2:{
                    drawLastNode(g,map.nodes.get(i).x,map.nodes.get(i).y,map.nodes.get(i).ns);
                    break;
                }
            }
        }

        for (int i = 0; i < map.arrows.size(); i++) {
            String s = "";
            Point fn = getPoint(map.arrows.get(i).fNode);
            Point nn = getPoint(map.arrows.get(i).nNode);
            drawArrows(fn.x, fn.y, nn.x, nn.y, g, getArrowsName(map.arrows.get(i).fNode,map.arrows.get(i).nNode));
        }
    }

    /*
        判断是否线是否有重复的
     */
    String getArrowsName(String fn,String nn){
        String s = "";
        for (int i = 0; i < map.arrows.size(); i++) {
            if(map.arrows.get(i).fNode.equals(fn) && map.arrows.get(i).nNode.equals(nn)){
                if(s.equals("")) s += map.arrows.get(i).name;
                else s += "," + map.arrows.get(i).name;
            }
        }
        return s;
    }

    /*
        Node,Arrows转成DFA
     */
    DFA getDfa(){
        DFA dfa = new DFA();
        for (int i = 0; i < nodes.size(); i++) {
            dfa.K.add(nodes.get(i).ns);
            if(nodes.get(i).temp == 1){
                dfa.S = nodes.get(i).ns;
            }else if(nodes.get(i).temp == 2){
                dfa.Z.add(nodes.get(i).ns);
            }
        }

        for (int i = 0; i < arrows.size(); i++) {
            if (!dfa.A.contains(arrows.get(i).name)) {
                dfa.A.add(arrows.get(i).name);
            }
            dfa.f[dfa.K.indexOf(arrows.get(i).fNode)][dfa.A.indexOf(arrows.get(i).name)] = arrows.get(i).nNode;
        }

        return dfa;
    }

    /*
        状态转换矩阵
     */
    void getMatrix(){
        JFrame jf_getMatrix = new JFrame("状态转换矩阵");

        Object[] tableHead = new Object[dfa.A.size() + 2];
        tableHead[0] = " ";
        tableHead[dfa.A.size() + 1] = " ";
        for (int i = 1; i < tableHead.length - 1; i++) {
            tableHead[i] = dfa.A.get(i - 1);
        }


        Object[][] table = new Object[dfa.K.size()][tableHead.length];

        for (int i = 0; i < nodes.size(); i++) {
            table[i][0] = nodes.get(i).ns;
            if(nodes.get(i).temp == 2)
            table[i][tableHead.length - 1] = 1;
            else if(nodes.get(i).temp == 1)
                table[i][tableHead.length - 1] = -1;
            else table[i][tableHead.length - 1] = 0;
        }

        for (int i = 0; i < dfa.K.size(); i++) {
            for (int j = 0; j < dfa.A.size(); j++) {
                table[i][j+1] = dfa.f[i][j];
            }
        }


        JTable jt = new JTable(table,tableHead);
        jt.setGridColor(Color.BLACK);
        jf_getMatrix.add(jt.getTableHeader(),BorderLayout.NORTH);
        jf_getMatrix.add(jt,BorderLayout.CENTER);

        jf_getMatrix.setSize(700, 600);
        jf_getMatrix.setLocationRelativeTo(jf);
        jf_getMatrix.setVisible(true);
        jf_getMatrix.setResizable(false);
        jf_getMatrix.setAlwaysOnTop(true);
        jf_getMatrix.setLayout(new BorderLayout());
    }

    /*
        随机生成DFA图

    void createDFAMap(){
        Random r = new Random();
        for (int i = 0; i < map.nodes.size(); i++) {
            map.nodes.get(i).x = r.nextInt(jp_main.getWidth() - 50);
            map.nodes.get(i).y = r.nextInt(jp_main.getHeight() - 50);
            nodes.get(i).x = map.nodes.get(i).x;
            nodes.get(i).y = map.nodes.get(i).y;
        }
    }
*/

    /*
         生成DFA的UI
     */
    void createMapUI(){
        JFrame createNewMap = new JFrame("生成新状态转换图页面");
        JPanel jp1 = new JPanel();
        createNewMap.setLayout(new BorderLayout());
        JRadioButton jrb_sj = new JRadioButton("随机生成",false);
        JRadioButton jrb_zd = new JRadioButton("指定",false);
        jp1.add(jrb_sj);
        jp1.add(jrb_zd);
        ButtonGroup group = new ButtonGroup();
        group.add(jrb_sj);
        group.add(jrb_zd);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton[] nos = new JRadioButton[nodes.size()];
        JTextField[] xs = new JTextField[nodes.size()];
        JTextField[] ys = new JTextField[nodes.size()];
        JPanel jpm = new JPanel();
        jpm.setLayout(new GridLayout(0,3));
        for (int i = 0; i < nos.length; i++) {
            nos[i] = new JRadioButton();
            nos[i].setText(nodes.get(i).ns);
            nos[i].setEnabled(false);
            xs[i] = new JTextField();
            xs[i].setText(String.valueOf(nodes.get(i).x));
            xs[i].setEditable(false);
            ys[i] = new JTextField();
            ys[i].setText(String.valueOf(nodes.get(i).y));
            ys[i].setEditable(false);
            bg.add(nos[i]);
            jpm.add(nos[i]);
            jpm.add(xs[i]);
            jpm.add(ys[i]);
        }

        createNewMap.add(jpm);


        /*
            随机生成触发器
         */
        jrb_sj.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(jrb_sj.isSelected()){
                    Random r = new Random();
                    for (int i = 0; i < nodes.size(); i++) {
                        xs[i].setText(String.valueOf(r.nextInt(jp_main.getWidth() - 50)));
                        ys[i].setText(String.valueOf(r.nextInt(jp_main.getHeight() - 50)));
                    }
                }
            }

        });


        class RadioListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton jrb = (JRadioButton) e.getSource();
                jp_main.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getButton() == MouseEvent.BUTTON1){
                            for (int i = 0; i < nos.length; i++) {
                                if (jrb == nos[i] && nos[i].isSelected()){
                                    xs[i].setText(String.valueOf(e.getX()));
                                    ys[i].setText(String.valueOf(e.getY()));
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }

        for (int i = 0; i < nos.length; i++) {
            nos[i].addActionListener(new RadioListener());
        }

        /*
            指定生成的触发器
         */
        jrb_zd.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (jrb_zd.isSelected()){
                    for (int i = 0; i < nos.length; i++) {
                        nos[i].setEnabled(true);
                    }
                }
            }
        });


        JPanel jp2 = new JPanel();
        JButton jb = new JButton("确定");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(map.nodes.size());
                for (int i = 0; i < nos.length; i++) {
                    nodes.get(i).x = Integer.parseInt(xs[i].getText());
                    nodes.get(i).y = Integer.parseInt(ys[i].getText());
                    map.nodes.get(i).x = nodes.get(i).x;
                    map.nodes.get(i).y = nodes.get(i).y;
                }
                drawMap(jp_main.getGraphics());
                createNewMap.setVisible(false);
            }
        });
        jp2.add(jb);
        createNewMap.add(jp2,BorderLayout.SOUTH);
        createNewMap.add(jp1,BorderLayout.NORTH);
        createNewMap.setSize(500, 400);
        createNewMap.setVisible(true);
        createNewMap.setResizable(false);
        createNewMap.setAlwaysOnTop(true);
        createNewMap.setLocationRelativeTo(jf);
        createNewMap.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }


}

