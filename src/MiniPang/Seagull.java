package MiniPang;

import java.io.Serializable;
import java.util.ArrayList;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class Seagull extends PhysicBody implements Serializable, Disparable {
	
	boolean muerto = false;

	public Seagull(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.setVelocity(2, 0);
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		if (sprite instanceof Disparable) {
			Disparable d = (Disparable) sprite;
			d.danyar();
		}
		if (this.flippedX) {
			this.setVelocity(-3, 0);
		} else if (!this.flippedX) {
			this.setVelocity(3, 0);
		}
		
	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		
	}

	@Override
	public void update() {
		super.update();
//		System.out.println(this.x1);
		if (this.x1 == 1600) {
			this.setVelocity(-3, 0);
			this.flippedX = true;
		}
		if (this.x1 == 200) {
			this.setVelocity(3, 0);
			this.flippedX = false;
		}
//		if (this.y1==400) {
//			movimientoArriba();
//		}
//		if (this.y1==300) {
//			movimientoAbajo();
//		}
	}

//	private void movimientoAbajo() {
//		if (this.flippedX) {
//			this.setVelocity(this.velocity[0], 4);
//		} else if (!this.flippedX) {
//			this.setVelocity(this.velocity[0], 4);
//		}
//		
//	}
//
//	private void movimientoArriba() {
//		if (this.flippedX) {
//			this.setVelocity(this.velocity[0], -4);
//		} else if (!this.flippedX) {
//			this.setVelocity(this.velocity[0], -4);
//		}
//		
//	}

	@Override
	public void danyar() {
		for (int i = 0; i < 30000; i++) {
			this.changeImage("resources/SeagullHurted.gif");
		}
		Main.w.playSFX("resources/SeagullHurted.wav");
		muerto=true;
		this.delete();
	}

}
