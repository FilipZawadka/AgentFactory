package com.pw.agents;

import java.util.ArrayList;

import com.pw.board.Board;
import com.pw.board.BoardObject;
import com.pw.utils.Position;

public class GOTr implements BoardObject {
    private Position position;
    ArrayList<BoardObject> Trlist;
    private String id;
    public Board board;
    public Integer TRnum;

    public GOTr(Position position, String _id, Board board, BoardObject firstAgent) {
        this.position = position;
        id = _id;
        this.board = board;
        Trlist = new ArrayList<>();
        Trlist.add(firstAgent);
    }


    @Override
    public void setPosition(Position _position) {
        position = _position;
    }

    @Override
    public Position getPosition() {
        return position;

    }

    @Override
    public String getId() {
        return id;
    }
    private void updateTRs(){
        for(int i =0;i<Trlist.size();i++){
            Trlist.get(i).setPosition(position);
        }

    }

    public void moveUp() {
        if(position.getY()<board.height){
            position.setY(position.getY()+1);
            updateTRs();
        }

    }

    public void moveDown() {
        if(position.getY()>0){
            position.setY(position.getY()-1);
            updateTRs();
        }
    }

    public void moveLeft() {
        if(position.getX()>0){
            position.setX(position.getX()-1);
            updateTRs();
        }
    }

    public void moveRight() {
        if(position.getX()<board.width){
            position.setX(position.getX()+1);
            updateTRs();
        }
    }
    public void goTo(Position dest){
        while(position.getX()<dest.getX()){
            moveRight();
        }
        while(position.getX()>dest.getX()){
            moveLeft();
        }
        while(position.getY()<dest.getY()){
            moveUp();
        }
        while(position.getY()>dest.getY()){
            moveDown();
        }
    }
}
