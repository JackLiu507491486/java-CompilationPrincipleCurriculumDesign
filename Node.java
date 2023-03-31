import java.io.Serializable;

public class Node implements Serializable {
    int x,y,temp;
    String ns;
    Node(int x,int y,String s,int temp) {
        this.x = x;
        this.y = y;
        this.ns = s;
        this.temp = temp;
    }
}
