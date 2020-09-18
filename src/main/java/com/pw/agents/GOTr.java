package com.pw.agents;

import com.pw.board.Board;
import com.pw.utils.Position;
import jade.core.AID;

import java.util.ArrayList;
import java.util.List;

public class GOTr {
    private Position position;
    private List<TrAgent> trlist;
    private String id;
    public Board board;
    public Integer TRnum;

    public GOTr(Position position, String _id, Board board, TrAgent firstAgent) {
        this.position = position;
        id = _id;
        this.board = board;
        trlist = new ArrayList<>();
        trlist.add(firstAgent);
        board.GOTrList.add(this);
    }

    public TrAgent getTrByAID(AID trAID){
        for (TrAgent a: trlist){
            if(a.getAID()==trAID){
                return a;
            }
        }
        return null;
    }

    public void dispose(){
        board.GOTrList.remove(this);
    }

    public void setPosition(Position _position) {
        position = _position;
    }


    public Position getPosition() {
        return position;

    }


    public String getId() {
        return id;
    }

    private void updateTRs() {
        for (int i = 0; i < trlist.size(); i++) {
            trlist.get(i).setPosition(position);
        }

    }

    public void moveUp() {
        if (position.getY() < board.height) {
            position.setY(position.getY() + 1);
            updateTRs();
        }

    }

    public void moveDown() {
        if (position.getY() > 0) {
            position.setY(position.getY() - 1);
            updateTRs();
        }
    }

    public void moveLeft() {
        if (position.getX() > 0) {
            position.setX(position.getX() - 1);
            updateTRs();
        }
    }

    public void moveRight() {
        if (position.getX() < board.width) {
            position.setX(position.getX() + 1);
            updateTRs();
        }
    }

    public void goTo(Position dest) {
        while (position.getX() < dest.getX()) {
            moveRight();
        }
        while (position.getX() > dest.getX()) {
            moveLeft();
        }
        while (position.getY() < dest.getY()) {
            moveUp();
        }
        while (position.getY() > dest.getY()) {
            moveDown();
        }
        dispose();
    }
}
