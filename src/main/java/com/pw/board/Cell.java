package com.pw.board;

import com.pw.utils.Position;

import java.util.Hashtable;

public class Cell {
    private Position position;
    Hashtable<String, BoardObject> content;


    public Cell(Position position) {
        this.position = position;
        content = new Hashtable<>();
    }

    public void addContent(BoardObject obj) {
        content.put(obj.getId(), obj);
    }

    public void removeContent(BoardObject obj) {
        content.remove(obj.getId());
    }
}
