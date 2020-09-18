package com.pw.agents;

import com.pw.board.Board;
import com.pw.utils.Distance;
import com.pw.utils.NeighborPosition;
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

    public GOTr(Position position, String _id, Board board, List<TrAgent> agents) {
        this.position = position;
        id = _id;
        this.board = board;
        trlist = new ArrayList<>(agents);
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

    public boolean isPositionFree(Position position){
        for (GomAgent a: board.GomList){
            if(Distance.isEqual(a.getPosition(),position)){
                return true;
            }
        }
        for (TrAgent a: board.TrList){
            if(Distance.isEqual(a.getPosition(),position)){
                return false;
            }
        }
        for (GOTr a: board.GOTrList){
            if(Distance.isEqual(a.getPosition(),position)){
                return false;
            }
        }
        return true;
    }

    public void goTo(Position dest) {
        while(!Distance.isEqual(position,dest)) {
            boolean blocked = true;
            while (position.getX() < dest.getX() && isPositionFree(NeighborPosition.getRightPosition(position))) {
                moveRight();
                blocked = false;
            }
            while (position.getX() > dest.getX() && isPositionFree(NeighborPosition.getLeftPosition(position))) {
                moveLeft();
                blocked = false;
            }
            while (position.getY() < dest.getY() && isPositionFree(NeighborPosition.getUpPosition(position))) {
                moveUp();
                blocked = false;
            }
            while (position.getY() > dest.getY() && isPositionFree(NeighborPosition.getDownPosition(position))) {
                moveDown();
                blocked = false;
            }
            if (blocked){
                switch((int)(Math.random()*4)){
                    case 0:
                        if(isPositionFree(NeighborPosition.getRightPosition(position))){
                            moveRight();
                            break;
                        }
                    case 1:
                        if(isPositionFree(NeighborPosition.getLeftPosition(position))){
                            moveLeft();
                            break;
                        }
                    case 2:
                        if(isPositionFree(NeighborPosition.getUpPosition(position))){
                            moveUp();
                            break;
                        }
                    default:
                        if(isPositionFree(NeighborPosition.getDownPosition(position))){
                            moveDown();
                            break;
                        }
                }
            }
        }
        dispose();
    }
}
