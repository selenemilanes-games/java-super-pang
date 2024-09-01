package MiniPang;

import java.io.Serializable;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class Vida extends PhysicBody implements Serializable {
	
	public static boolean apareceVida=false;

	public Vida(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.setConstantForce(0, 0.15);
		this.trigger=true;
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		if (sprite instanceof Suelo) {
			this.trigger=false;
		}

	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		// TODO Auto-generated method stub

	}

}
