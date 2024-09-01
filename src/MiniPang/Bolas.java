package MiniPang;

import java.io.Serializable;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class Bolas extends PhysicBody implements Disparable, Serializable {

	int vida = 1;
	boolean izquierda = false;
	boolean derecha = false;

	public Bolas(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f, String direccion) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.circle = true;
		this.setConstantForce(0, 0.15);
		this.setVelocity(-1, this.velocity[1]);
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
		movimientoIzDer();
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		if (sprite instanceof Suelo || sprite instanceof Plataforma) {
			movimientoArriba();
		}
		if (sprite instanceof Pared || sprite instanceof Barrera) { /// CUANDO TOQUE LA PARED SE CAMBIA LA DIRECCIÓN DE LA BOLA
			if (this.izquierda == true) {
				this.izquierda = false;
				this.derecha = true;
			} else {
				this.izquierda = true;
			}
		}
		if (sprite instanceof Bolas || sprite instanceof Vida) {
			this.trigger = true;
		}
	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		if (sprite instanceof Bolas || sprite instanceof BolaGrande || sprite instanceof BolaMediana
				|| sprite instanceof BolaPequeña || sprite instanceof Vida) {
			this.trigger = false;
		}
	}
	
	

	@Override
	public void onCollisionStay(Sprite sprite) {
		if (sprite instanceof Bolas || sprite instanceof BolaGrande || sprite instanceof BolaMediana
				|| sprite instanceof BolaPequeña || sprite instanceof Vida) {
			this.trigger = true;
		}
	}

	public void movimientoArriba() {
		if (this.izquierda) {
			this.setVelocity(this.velocity[0], -13);
		} else if (this.derecha) {
			this.setVelocity(this.velocity[0], -13);
		}
	}

	public void movimientoAbajo() {
		if (this.izquierda && this.y1 == 100) {
			this.setVelocity(this.velocity[0], 10);
		} else if (this.derecha && this.y1 == 100) {
			this.setVelocity(this.velocity[0], 10);
		}
	}

	public void movimientoIzDer() {
		if (this.izquierda) {
			this.setVelocity(-2, this.velocity[1]);
		} else if (this.derecha) {
			this.setVelocity(2, this.velocity[1]);
		}
	}

	@Override
	public void danyar() {
		this.delete();
		Main.w.playSFX("resources/Pop.wav");
	}

	@Override
	public String toString() {
		return "Bolas [vida=" + vida + ", izquierda=" + izquierda + ", derecha=" + derecha + "]";
	}

}
