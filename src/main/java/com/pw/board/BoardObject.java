package com.pw.board;

import com.pw.utils.Position;

public interface BoardObject {
    void setPosition(Position position);
    Position getPosition();
    String getId();

}
