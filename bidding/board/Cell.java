package board;

import utils.Position;

import java.util.HashMap;
import java.util.Map;

public class Cell {
    private Position position;
    Map<String, BoardObject> content;


    public Cell(Position position) {
        this.position = position;
        content = new HashMap<>();
    }
    public void addContent(BoardObject obj){
        content.put(obj.getId(),obj);
    }
    public void removeContent(BoardObject obj){
        content.remove(obj.getId());
    }
}
