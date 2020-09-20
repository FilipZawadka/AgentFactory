package com.pw.utils;

import com.pw.biddingOntology.PositionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private Integer x, y;

    public Position(PositionInfo positionInfo) {
        this.x = positionInfo.getX();
        this.y = positionInfo.getY();
    }

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
    }
}
