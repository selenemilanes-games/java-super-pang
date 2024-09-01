package MiniPang;

import java.util.ArrayList;
import java.util.Iterator;

import Core.Field;
import Core.PhysicBody;
import Core.Sprite;

public class ProjectilPangAzul extends PhysicBody {

	ArrayList<ColaProjectilBlue> cola = new ArrayList<ColaProjectilBlue>();
	int contador = 0;
	int yGriega = 775;

	public ProjectilPangAzul(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f,
			ArrayList<ColaProjectilBlue> cola) {
		super(name, x1, y1, x2, y2, angle, path, f);
		this.trigger = true;
		this.cola = cola;
		this.setVelocity(0, -10);
	}

	public ProjectilPangAzul(ProjectilPangAzul p, int x1, int y1, int x2, int y2, ArrayList<ColaProjectilBlue> cola) {
		super(p.name, x1, y1, x2, y2, p.angle, p.path, p.f);
		this.trigger = true; // marca el sprite como un trigger. Los triggers se mueven segun las fisicas
								// pero no paran al colisionar con otro physicbody
		this.cola = cola;
		this.setVelocity(0, -10);
	}

	@Override
	public void onCollisionEnter(Sprite sprite) {
		// System.out.println("Disparo a " + " " + sprite.name); // imprimeix objecte si
		// ha colisionat
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
			this.delete(); // esborrem el projectil
			for (int i = 0; i < this.cola.size(); i++) {
				this.cola.get(i).delete();
//				System.out.println("Aquí no entra");
			}
		}
	}

	@Override
	public void onCollisionExit(Sprite sprite) {

	}
	
	@Override
	public void onTriggerEnter(Sprite sprite) {
		super.onTriggerEnter(sprite);
		if (sprite instanceof Vida) {
			Vida.apareceVida=true;
			Main.w.playSFX("resources/DestroyItem.wav");
			sprite.delete();
		}
	}

	@Override
	public void update() {
		super.update();

//		System.out.println("Ha entrado en " + this.y1);

		if (this.y1 < 5) {
			this.delete();
			for (int i = 0; i < this.cola.size(); i++) {

				this.cola.get(i).delete();
//					System.out.println("Cola desaparece de pantalla");

			}
//			System.out.println("Proyectil desaparece de pantalla");
		}

		if (this.delete) {
//			System.out.println("Se ha deleteado");

			for (int i = 0; i < this.cola.size(); i++) {
				if (this.cola.get(i).y1 < 5) {
					this.cola.get(i).delete();
//					System.out.println("Cola desaparece de pantalla");
				}
			}
			return;
		}

		for (int i = 0; i < this.cola.size(); i++) {
			if (this.cola.get(i).colisionColaBlue) { // Si alguna parte de la cola colisiona con una bola, eliminamos toda
													// la cola
				for (int j = 0; j < this.cola.size(); j++) {
					this.cola.get(j).delete();
//					System.out.println("Se elimina cola por colision con bola");
				}
				this.delete();
			}
		}
		if (!this.delete) {
			for (int i = 0; i < 775; i++) {
				if (this.y1 == yGriega) {
					generarCola();
//					System.out.println("Ha entrado en " + this.y1 + " y se ha generado la cola");
					yGriega -= 10;
				}
			}
		}
	}

	public void generarCola() {
		if (contador == 0) {
			this.cola.add(new ColaProjectilBlue("Cola Projectil PangAzul", (int) this.x1 + 10, (int) this.y1 + 30,
					(int) this.x2 - 10, (int) this.y2 + 30, 0, "resources/weaponColaBlue.png", f));
		}
		if (contador > 0) {
			this.cola.add(new ColaProjectilBlue("Cola Projectil PangAzul", (int) this.x1 + 10,
					(int) this.cola.get(contador - 1).y1 + 20, (int) this.x2 - 10,
					(int) this.cola.get(contador - 1).y2 + 20, 0, "resources/weaponColaBlue.png", f));
		}
//		System.out.println("contador es " + contador);
		contador += 1;

	}

}
