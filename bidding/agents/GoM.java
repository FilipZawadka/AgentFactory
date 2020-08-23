package agents;

import board.Board;
import board.BoardObject;
import jade.core.AID;
import jade.core.Agent;
import utils.Position;

public class GoM extends Agent implements BoardObject {
    private String id;
    private AID gomId;
    private Position position;

    @Override
    protected void setup() {
        super.setup();

        Object[] args = getArguments();

        this.id = args[0].toString();
        this.position = new Position(Integer.parseInt((String) args[1]), Integer.parseInt((String) args[2]));
    }

    public AID getGomId() {
        return gomId;
    }

    public void setGomId(AID gomId) {
        this.gomId = gomId;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
