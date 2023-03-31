import java.util.ArrayList;
/*
    定义确定有穷自动机
 */
class DFA {
    ArrayList<String> K;//结点集合
    ArrayList<String> A;//关系集合
    String[][] f;//关系投影
    String S;//初态结点
    ArrayList<String> Z;//终态节点

    DFA(){
        K = new ArrayList<String>();
        A = new ArrayList<String>();
        f = new String[50][50];
        S = "";
        Z = new ArrayList<String>();
    }

}

