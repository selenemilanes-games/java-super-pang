package MiniPang;

import java.io.Serializable;

import Core.Field;
import Core.Sprite;

public class BolaGrande extends Bolas implements Serializable {

	BolaMediana BolaMedianaIzq;
	BolaMediana BolaMedianaDer;

	public BolaGrande(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f,
			String direccion) {
		super(name, x1, y1, x2, y2, angle, path, f, direccion);
		this.circle = super.circle;
		this.setVelocity(super.velocity[0], super.velocity[1]);
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
		this.BolaMedianaIzq = new BolaMediana(super.name + " Mediana", (int) this.x1 - 40, (int) this.y1 + 20,
				(int) this.x2 - 40, (int) this.y2 + 20, this.angle, this.path, this.f, "izquierda");
		this.BolaMedianaDer = new BolaMediana(super.name + " Mediana", (int) this.x1 + 40, (int) this.y1 + 20,
				(int) this.x2 + 40, (int) this.y2 + 20, this.angle, this.path, this.f, "derecha");
		this.BolaMedianaDer.derecha = true;
		this.BolaMedianaDer.izquierda = false;
		Main.contadorBolas += 2;

		// con esto hacemos dos bolas medianas

	}

	@Override
	public String toString() {
		return "BolaGrande [BolaMedianaIzq=" + BolaMedianaIzq + ", BolaMedianaDer=" + BolaMedianaDer + "]";
	}
}
