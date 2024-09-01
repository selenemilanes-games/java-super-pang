package MiniPang;

import java.io.Serializable;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class Plataforma extends PhysicBody implements Disparable, Serializable {

	public Plataforma(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.solid=true;
	}

	@Override
	public void danyar() {
		this.delete();

	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		// TODO Auto-generated method stub

	}

}
