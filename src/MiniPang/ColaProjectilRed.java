package MiniPang;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class ColaProjectilRed extends PhysicBody {

	boolean colisionColaRed = false;

	public ColaProjectilRed(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
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
				PuntsRed.puntsRed += 250;
			} else if (sprite instanceof BolaMediana) {
				PuntsRed.puntsRed += 500;
			} else if (sprite instanceof BolaPequeña) {
				PuntsRed.puntsRed += 1000;
			}
//			System.out.println("pum");
			colisionColaRed = true;
		}
	}

	@Override
	public void onCollisionExit(Sprite sprite) {

	}

}
