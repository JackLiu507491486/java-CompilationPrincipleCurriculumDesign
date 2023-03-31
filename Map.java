import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
    ArrayList<Arrows> arrows = new ArrayList<>();
    ArrayList<Node> nodes = new ArrayList<>();
}
