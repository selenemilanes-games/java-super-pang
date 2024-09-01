package MiniPang;

import java.awt.Font;

import Core.Field;
import Core.Sprite;

public class PuntsRed extends Sprite {

	static int puntsRed = 0;

	public PuntsRed(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.solid = false;
		this.text = true;
		this.textColor = 0xefca03;
		this.font = new Font("Bahnschrift Condensed", Font.PLAIN, 40);
		this.path = "" + puntsRed;
	}

	@Override
	public void update() {
		super.update();
		this.path = "" + puntsRed;
	}
}
