package MiniPang;

import java.awt.Font;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class TextoPB extends PhysicBody {

	public TextoPB(String name, int x1, int y1, int x2, int y2, String path, Field f) {
		super(name, x1, y1, x2, y2, 0, path, f);
		this.text = true;
		this.textColor = 0x4B82EC;
		this.font = new Font("Consolas", Font.PLAIN, 24);
		this.setVelocity(0, -3);
		this.trigger=true;
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update() {
		super.update();
		if (this.y1 <= 0) {
			this.delete();
		}
	}

}
