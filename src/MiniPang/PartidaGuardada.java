package MiniPang;

import java.io.Serializable;
import java.util.ArrayList;

public class PartidaGuardada implements Serializable {

	public PangRojo RedPang;
	public ArrayList<BolaGrande> bolas;
	public int puntsRed;
	public int contadorB;
	public Seagull seagull;
	public Plataforma pIzquierda;
	public Plataforma pDerecha;
	public boolean apareceVida;
	public Vida vida;
	public boolean aumento;

	public PartidaGuardada(PangRojo redPang, ArrayList<BolaGrande> bolas, int puntsRed, int contadorBolas, Seagull seaG,
			Plataforma plataformaIzquierda, Plataforma plataformaDerecha, boolean apareceVida, boolean aumento) {
		super();
		RedPang = redPang;
		this.bolas = bolas;
		this.puntsRed = puntsRed;
		this.contadorB = contadorBolas;
		this.seagull = seaG;
		this.pIzquierda = plataformaIzquierda;
		this.pDerecha = plataformaDerecha;
		this.apareceVida = apareceVida;
		this.vida = Main.up;
		this.aumento = aumento;
	}

	@Override
	public String toString() {
		return "PartidaGuardada [RedPang=" + RedPang + ", bolas=" + bolas + ", puntsRed=" + puntsRed + ", contadorB="
				+ contadorB + ", seagull=" + seagull + ", pIzquierda=" + pIzquierda + ", pDerecha=" + pDerecha
				+ ", vidaCogida=" + apareceVida + "]";
	}

}
