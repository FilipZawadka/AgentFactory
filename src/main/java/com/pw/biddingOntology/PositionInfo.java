package com.pw.biddingOntology;

import com.pw.utils.Position;

import jade.content.Concept;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PositionInfo implements Concept {
	private Integer x, y;

	public PositionInfo(Position position) {
		this.x = position.getX();
		this.y = position.getY();
	}
}
