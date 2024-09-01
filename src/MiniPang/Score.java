package MiniPang;

import java.awt.Font;

import Core.Field;
import Core.Sprite;

public class Score extends Sprite {

	public Score(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.solid = false;
		this.text = true;
		this.textColor = 0xFFFF00;
		this.font = new Font("Source Code Pro", Font.ITALIC, 20);
		this.path = "SCORE";
	}

}
