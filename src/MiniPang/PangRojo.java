package MiniPang;

import java.io.Serializable;
import java.util.ArrayList;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class PangRojo extends PhysicBody implements Serializable {

	public ProjectilPangRojo projectil;
	public ProjectilPangRojo colaprojectil;
	public boolean muerto = false;
	public boolean aumento = false;
	public int vida = 3;

	public PangRojo(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f,
			ProjectilPangRojo proyectil) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.projectil = proyectil;
		this.circle = true;
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		if (sprite instanceof Bolas) {
//			System.out.println("Pang muerto");
			this.vida--;
			Main.w.playSFX("resources/Ouch.wav");
			if (this.vida==0) {
				this.muerto = true;
				this.setVelocity(0, 0);
			}
			
		}

		if (sprite instanceof PangAzul) {
			this.trigger = true;
		}
		
		if (sprite instanceof Vida) {
			if (this.vida<=2) {
				this.vida++;
			}
			Vida.apareceVida=true;
			Main.w.playSFX("resources/upItem.wav");
			sprite.delete();
		}
	}

	@Override
	public void onCollisionExit(Sprite sprite) {
		if (sprite instanceof PangAzul) {
			this.trigger = false;
		}
	}

	public void moviment(Inputs in) {
		if (in == Inputs.ESQUERRA) { // Per moure's a esquerra/dreta es fa servir la VELOCITAT
			this.setVelocity(-15, this.velocity[1]); // Solo modificamos la "x", no la "y"
			this.flippedX = true;
			this.changeImage("resources/RedWalking.gif");
			if (!aumento) {
				this.x2 += 90;
				this.y1 -= 20;
			}
			aumento = true;
		}
		if (in == Inputs.DRETA) {
			this.setVelocity(15, this.velocity[1]);
			this.flippedX = false;
			this.changeImage("resources/RedWalking.gif");
			if (!aumento) {
				this.x2 += 90;
				this.y1 -= 20;
			}
			aumento = true;
		}
		if (in == Inputs.QUIET) {
			setVelocity(0, this.velocity[1]);
			this.changeImage("resources/RedPang.gif");
			if (aumento) {
				this.x2 -= 90;
				this.y1 += 20;
			}
			aumento = false;
		}
		if (in == Inputs.DISPARAR) {
			disparar();
			this.changeImage("resources/RedPangShoot.png");
		}
	}

	private void disparar() {
		ArrayList<ColaProjectilRed> colaprojectilPangRojo = new ArrayList<ColaProjectilRed>();
		colaprojectilPangRojo
				.add(new ColaProjectilRed("Cola Projectil PangAzul", 0, 0, 0, 0, 0, "resources/weaponColaRed.png", f));

		if (!aumento) {
			this.projectil = new ProjectilPangRojo(this.projectil, (int) this.x1 + 35, (int) this.y1 + 105,
					(int) this.x2 - 35, (int) this.y2, colaprojectilPangRojo);
//			System.out.println(this.y2);
		} else if (aumento) {
			this.projectil = new ProjectilPangRojo(this.projectil, (int) this.x1 + 80, (int) this.y1 + 125,
					(int) this.x2 - 80, (int) this.y2, colaprojectilPangRojo);
		}
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public String toString() {
		return "PangRojo [projectil=" + projectil + ", colaprojectil=" + colaprojectil + ", muerto=" + muerto
				+ ", aumento=" + aumento + "]";
	}

}
