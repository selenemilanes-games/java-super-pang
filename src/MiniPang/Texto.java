package MiniPang;

import java.awt.Font;

import Core.Field;
import Core.Sprite;

public class Texto extends Sprite {

	public Texto(String name, int x1, int y1, int x2, int y2, String path, Field f) {
		super(name, x1, y1, x2, y2, 0, path, f);
		this.text = true;
		this.textColor = 0xFFFFFF;
		this.font = new Font("Consolas", Font.PLAIN, 24);
	}

}
