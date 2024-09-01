package MiniPang;

import java.io.Serializable;
import java.util.ArrayList;

public class PartidaGuardadaTwo implements Serializable {

	public PangRojo RedPang;
	public PangAzul BluePang;
	public ArrayList<BolaGrande> bolas;
	public int puntsRed;
	public int puntsBlue;
	public int contadorB;
	public Seagull seagull;
	public Plataforma pIzquierda;
	public Plataforma pDerecha;
	public boolean apareceVida;
	public Vida vida;
	public boolean aumentoRed;
	public boolean aumentoBlue;

	public PartidaGuardadaTwo(PangRojo redPang, PangAzul bluePang, ArrayList<BolaGrande> bolas, int puntsRed,
			int puntsBlue, int contadorBolas, Seagull seaG, Plataforma plataformaIzquierda,
			Plataforma plataformaDerecha, boolean apareceVida, boolean aumentoRed, boolean aumentoBlue) {
		super();
		RedPang = redPang;
		BluePang = bluePang;
		this.bolas = bolas;
		this.puntsRed = puntsRed;
		this.puntsBlue = puntsBlue;
		this.contadorB = contadorBolas;
		this.seagull = seaG;
		this.pIzquierda = plataformaIzquierda;
		this.pDerecha = plataformaDerecha;
		this.apareceVida = apareceVida;
		this.vida = Main.up;
		this.aumentoRed=aumentoRed;
		this.aumentoBlue=aumentoBlue;

	}

	@Override
	public String toString() {
		return "PartidaGuardadaTwo [RedPang=" + RedPang + ", BluePang=" + BluePang + ", bolas=" + bolas + ", puntsRed="
				+ puntsRed + ", puntsBlue=" + puntsBlue + ", contadorB=" + contadorB + ", seagull=" + seagull
				+ ", pIzquierda=" + pIzquierda + ", pDerecha=" + pDerecha + ", vidaCogida=" + apareceVida + "]";
	}

}
