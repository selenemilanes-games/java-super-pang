package MiniPang;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class ColaProjectilBlue extends PhysicBody {

	boolean colisionColaBlue = false;

	public ColaProjectilBlue(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.trigger = true;
		this.setVelocity(0, -10);
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		if (sprite instanceof Disparable) { // es pot fer un instanceof a l'interficie, no nomes la classe
			Disparable d = (Disparable) sprite; // casteig a Disparable
			d.danyar(); // cridem a danyar
			if (sprite instanceof BolaGrande) {
				PuntsBlue.puntsBlue += 250;
			} else if (sprite instanceof BolaMediana) {
				PuntsBlue.puntsBlue += 500;
			} else if (sprite instanceof BolaPequeña) {
				PuntsBlue.puntsBlue += 1000;
			}
//			System.out.println("pum");
			colisionColaBlue = true;
		}
	}

	@Override
	public void onCollisionExit(Sprite sprite) {

	}

}
