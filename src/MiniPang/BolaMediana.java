package MiniPang;

import java.io.Serializable;
import java.util.Random;

import Core.Field;
import Core.Sprite;

public class BolaMediana extends Bolas implements Serializable {

	BolaPequeña BolaPequeñaIzq;
	BolaPequeña BolaPequeñaDer;
	boolean cogerVida = false;

	public BolaMediana(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f,
			String direccion) {
		super(name, x1, y1, x2 - 30, y2 - 30, angle, path, f, direccion);
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
			this.setVelocity(this.velocity[0], -11);
		} else if (this.derecha) {
			this.setVelocity(this.velocity[0], -11);
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
		this.BolaPequeñaIzq = new BolaPequeña(super.name + " Pequeña", (int) this.x1 - 20, (int) this.y1 + 20,
				(int) this.x2 - 20, (int) this.y2 + 20, this.angle, this.path, this.f, "izquierda");
		this.BolaPequeñaDer = new BolaPequeña(super.name + " Pequeña", (int) this.x1 + 20, (int) this.y1 + 20,
				(int) this.x2 + 20, (int) this.y2 + 20, this.angle, this.path, this.f, "derecha");
		this.BolaPequeñaDer.derecha = true;
		this.BolaPequeñaDer.izquierda = false;
		Main.contadorBolas += 2;

		Random r = new Random();
		int numRandom = r.nextInt(0, 2);

		if (Vida.apareceVida == false && numRandom == 1) {
			Main.up = new Vida("Up", (int) this.x1-20, (int) this.y1-20, (int) this.x2+20, (int) this.y2+20, 0, "resources/Up.gif",
					this.f);
			Vida.apareceVida = true;

		}

	}

	@Override
	public String toString() {
		return "BolaMediana [BolaPequeñaIzq=" + BolaPequeñaIzq + ", BolaPequeñaDer=" + BolaPequeñaDer + "]";
	}

}
