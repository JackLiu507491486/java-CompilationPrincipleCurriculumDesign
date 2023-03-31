import java.io.Serializable;

public class Arrows implements Serializable {
    String name,fNode,nNode;
    Arrows(String fNode,String nNode,String name){
        this.fNode = fNode;
        this.nNode = nNode;
        this.name = name;
    }

}
