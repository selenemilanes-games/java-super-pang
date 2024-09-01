package MiniPang;

import Core.Field;
import Core.Sprite;

public class Suelo extends Sprite {

	public Suelo(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.solid=true;
	}

}
