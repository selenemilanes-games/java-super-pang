package MiniPang;

import java.io.Serializable;
import java.util.Arrays;

import Core.Field;
import Core.Sprite;

public class BolaPequeña extends Bolas implements Serializable {

	public BolaPequeña(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f,
			String direccion) {
		super(name, x1, y1, x2 - 50, y2 - 50, angle, path, f, direccion);
		this.circle = super.circle;
		if (direccion.equals("izquierda")) {
			this.izquierda = true;
			this.derecha = false;
		} else {
			this.derecha = true;
			this.izquierda = false;
		}
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		super.onCollisionEnter(sprite);
	}

	@Override
	public void movimientoArriba() {
		super.movimientoArriba();
		if (this.izquierda) {
			this.setVelocity(this.velocity[0], -9);
		} else if (this.derecha) {
			this.setVelocity(this.velocity[0], -9);
		}
	}

	@Override
	public void movimientoAbajo() {
		super.movimientoAbajo();
	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		super.onCollisionExit(sprite);
	}

	@Override
	public void danyar() {
		super.danyar();
		Main.contadorBolas -= 1;
	}

	@Override
	public String toString() {
		return "BolaPequeña [izquierda=" + izquierda + ", derecha=" + derecha + ", velocity="
				+ Arrays.toString(velocity) + ", x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + "]";
	}

}
