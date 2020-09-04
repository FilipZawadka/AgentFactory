package board;

import utils.Position;

public interface BoardObject {
    void setPosition(Position position);
    Position getPosition();
    String getId();

}
