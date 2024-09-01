package MiniPang;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Core.Field;
import Core.Sprite;
import Core.Window;

public class Main implements Serializable {

	static Field fMenu = new Field(); // field del menu
	static Window w = new Window(fMenu); // windows
	static Field fOne = new Field(); // field del joc
	static Field fTwo = new Field(); // field del joc
	static Field fNewGame = new Field(); // field de les opcions
	static Field fCredits = new Field(); // field dels credits
	static public int contadorBolas = -1;
	static Timer timer = new Timer();
	static boolean salirPlay = false;
	static boolean salirPlayTwo = false;
	static PartidaGuardada PG = new PartidaGuardada(null, null, PuntsRed.puntsRed, contadorBolas, null, null, null,
			Vida.apareceVida, false);
	static PartidaGuardadaTwo PG2 = new PartidaGuardadaTwo(null, null, null, PuntsRed.puntsRed, PuntsBlue.puntsBlue,
			contadorBolas, null, null, null, Vida.apareceVida, false, false);
	static boolean HayPG = false;
	static boolean pausado = false;
	static Vida up = null;

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
//		int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
//		int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
//		w.setBounds((ancho / 2) - (w.getWidth() / 2), (alto / 2) - (w.getHeight() / 2), 1920, 1080);
		w.musicVolume = 85;
		w.sfxVolume = 100;

		menu();

	}

	private static void menu() throws InterruptedException, ClassNotFoundException {
		fOne.clear();
		fTwo.clear();
		boolean aumento = false;

		BotonMenu Continue = new BotonMenu("Continue", 1100, 180, 1700, 250, 0, "resources/MenuContinueA.png", fMenu);
		BotonMenu NewGame = new BotonMenu("New Game", 1100, 270, 1700, 340, 0, "resources/MenuNewGameA.png", fMenu);
		BotonMenu Credits = new BotonMenu("Credits", 1100, 360, 1700, 430, 0, "resources/MenuCreditsA.png", fMenu);
		BotonMenu Quit = new BotonMenu("Quit", 1100, 450, 1700, 520, 0, "resources/MenuQuitA.png", fMenu);

		Sprite ManoContinue = new Sprite("Mano Continue", 1000, 180, 1100, 250, 0, "resources/MenuMano.png", fMenu);
		ManoContinue.visible = false;
		Sprite ManoNewGame = new Sprite("Mano New Game", 1000, 270, 1100, 340, 0, "resources/MenuMano.png", fMenu);
		ManoNewGame.visible = false;
		Sprite ManoCredits = new Sprite("Mano Credits", 1000, 360, 1100, 430, 0, "resources/MenuMano.png", fMenu);
		ManoCredits.visible = false;
		Sprite ManoQuit = new Sprite("Mano Quit", 1000, 450, 1100, 520, 0, "resources/MenuMano.png", fMenu);
		ManoQuit.visible = false;

		boolean sortir = false;

		while (!sortir) {
			w.changeField(fMenu);
			w.playMusic("resources/ModeSelect.wav");
			fMenu.resize();
			fMenu.background = "resources/MainMenu.jpg";
			fMenu.draw();
			Thread.sleep(18);

			ArrayList<Sprite> listaSpritesHover = fMenu.getMouseOverSprite();
			boolean butContinue = false;
			boolean butNewGame = false;
			boolean butCredits = false;
			boolean butQuit = false;

			for (Sprite s : listaSpritesHover) {
				butContinue = false;
				butNewGame = false;
				butCredits = false;
				butQuit = false;
				switch (s.name) {
				case "Continue":
					butContinue = true;
					s.changeImage("resources/MenuContinueC.png");
					break;
				case "New Game":
					butNewGame = true;
					s.changeImage("resources/MenuNewGameC.png");
					break;
				case "Credits":
					butCredits = true;
					s.changeImage("resources/MenuCreditsC.png");
					break;
				case "Quit":
					butQuit = true;
					s.changeImage("resources/MenuQuitC.png");
					break;
				}
			}
			if (butContinue) {
				ManoContinue.visible = true;
				ManoNewGame.visible = false;
				ManoCredits.visible = false;
				ManoQuit.visible = false;
				NewGame.changeImage("resources/MenuNewGameA.png");
				Credits.changeImage("resources/MenuCreditsA.png");
				Quit.changeImage("resources/MenuQuitA.png");
			} else if (butNewGame) {
				ManoContinue.visible = false;
				ManoNewGame.visible = true;
				ManoCredits.visible = false;
				ManoQuit.visible = false;
				Continue.changeImage("resources/MenuContinueA.png");
				Credits.changeImage("resources/MenuCreditsA.png");
				Quit.changeImage("resources/MenuQuitA.png");
			} else if (butCredits) {
				ManoContinue.visible = false;
				ManoNewGame.visible = false;
				ManoCredits.visible = true;
				ManoQuit.visible = false;
				Continue.changeImage("resources/MenuContinueA.png");
				NewGame.changeImage("resources/MenuNewGameA.png");
				Quit.changeImage("resources/MenuQuitA.png");
			} else if (butQuit) {
				ManoContinue.visible = false;
				ManoNewGame.visible = false;
				ManoCredits.visible = false;
				ManoQuit.visible = true;
				Continue.changeImage("resources/MenuContinueA.png");
				NewGame.changeImage("resources/MenuNewGameA.png");
				Credits.changeImage("resources/MenuCreditsA.png");
			}

			ArrayList<Sprite> listaSpritesClickados = fMenu.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Continue":
					cargarPartida();
					if (HayPG) {
						fOne.clear();
						continueGame();
					} else {
						fTwo.clear();
						continueGameTwo();
					}
					break;
				case "New Game":
					fOne.clear();
					fTwo.clear();
					newGame();
					break;
				case "Credits":
					credits();
					break;
				case "Quit":
					w.stopMusic();
					w.close();
					sortir = true;
					break;
				}
			}
		}

	}

	private static void continueGameTwo() throws InterruptedException {
		fTwo.clear();
		Vida.apareceVida = PG2.apareceVida;
		PuntsRed.puntsRed = PG2.puntsRed;
		salirPlayTwo = false;
		w.playMusic("resources/Stage01(HongKong).wav");
		int contadorTimer = 0;
		contadorBolas = -1;
		boolean aumentoRed = PG2.aumentoRed;
		boolean aumentoBlue = PG2.aumentoBlue;

		BotonMenu Back = new BotonMenu("Back", 1350, 900, 1650, 950, 0, "resources/MenuBackA.png", fTwo);

		Suelo Suelo = new Suelo("Suelo", 150, 820, 1750, 853, 0, "resources/suelo.png", fTwo);

		// PANG ROJO

		ArrayList<ColaProjectilRed> colaprojectilPangRojo = new ArrayList<ColaProjectilRed>();
		colaprojectilPangRojo.add(
				new ColaProjectilRed("Cola Projectil PangRojo", 0, 0, 0, 0, 0, "resources/weaponColaRed.png", fTwo));

		ProjectilPangRojo projectilPangRojo = new ProjectilPangRojo("Projectil PangRojo", 0, 0, 0, 0, 0,
				"resources/weaponRed.png", fTwo, colaprojectilPangRojo);

		PangRojo PangRojo = PG2.RedPang;
		PangRojo.setField(fTwo);
		PangRojo.projectil = projectilPangRojo;
		PangRojo.muerto = PG2.RedPang.muerto;
		PangRojo.trigger = PG2.RedPang.trigger;

		Pared RedPannel = new Pared("UI Red Pannel", 0, 0, 200, 1050, 0, "resources/UIRedpannel.png", fTwo);
		Pared RedPannelNormal = new Pared("Red Pannel Normal", 18, 40, 200, 190, 0, "resources/RedPannelNormal.png",
				fTwo);
		Sprite P1 = new Sprite("P1", 15, 153, 50, 183, 0, "resources/P1Pannel.png", fTwo);

		Sprite RedScore = new Sprite("Red Score", 5, 235, 70, 270, 0, "resources/Score.png", fTwo);
		PuntsRed pRed = new PuntsRed("Punts", 15, 300, 70, 320, 0, "0", fTwo);

		Sprite VidasDeRojo = new Sprite("Vidas de Rojo", 15, 480, 155, 550, 0, "resources/UpRojo3.png", fTwo);

		// PANG AZUL
		ArrayList<ColaProjectilBlue> colaprojectilPangAzul = new ArrayList<ColaProjectilBlue>();
		colaprojectilPangAzul.add(
				new ColaProjectilBlue("Cola Projectil PangAzul", 0, 0, 0, 0, 0, "resources/weaponColaBlue.png", fTwo));

		ProjectilPangAzul projectilPangAzul = new ProjectilPangAzul("Projectil PangAzul", 0, 0, 0, 0, 0,
				"resources/weaponBlue.png", fTwo, colaprojectilPangAzul);

		PangAzul PangAzul = PG2.BluePang;
		PangAzul.setField(fTwo);
		PangAzul.muerto = PG2.BluePang.muerto;
		PangAzul.projectil = projectilPangAzul;
		PangAzul.flippedX = PG2.BluePang.flippedX;
		PangAzul.trigger = PG2.BluePang.trigger;

		Pared BluePannel = new Pared("UI Blue Pannel", 1700, 0, 1900, 1050, 0, "resources/UIBluepannel.png", fTwo);
		Pared BluePannelNormal = new Pared("Blue Pannel Normal", 1700, 40, 1880, 190, 0,
				"resources/BluePannelNormal.png", fTwo);
		BluePannelNormal.flippedX = true;
		Sprite P2 = new Sprite("P2", 1850, 153, 1885, 183, 0, "resources/P2Pannel.png", fTwo);

		Sprite BlueScore = new Sprite("Blue Score", 1825, 235, 1900, 270, 0, "resources/Score.png", fTwo);
		PuntsBlue pBlue = new PuntsBlue("Blue Punts", 1745, 300, 1810, 320, 0, "0", fTwo);

		Sprite VidasDeAzul = new Sprite("Vidas de Azul", 1745, 480, 1885, 550, 0, "resources/UpAzul3.png", fTwo);

		Seagull seagull = PG2.seagull;
		seagull.setField(fTwo);
		seagull.changeImage("resources/Seagull.gif");

		Barrera BarreraIzquierda1 = new Barrera("Barrera izquierda 1", 405, 0, 440, 170, 0,
				"resources/BarreraVertical.png", fTwo);
		Barrera BarreraDerecha1 = new Barrera("Barrera derecha 1", 590, 0, 625, 170, 0, "resources/BarreraVertical.png",
				fTwo);

		Barrera BarreraHorizontal1 = new Barrera("Barrera horizontal 1", 440, 0, 590, 35, 0,
				"resources/BarreraHorizontal.png", fTwo);
		Plataforma PlataformaIzquierda = PG2.pIzquierda;
		PlataformaIzquierda.setField(fTwo);
		PlataformaIzquierda.changeImage("resources/PlataformaGrisHorizontal.png");

		Barrera BarreraIzquierda2 = new Barrera("Barrera izquierda 2", 1205, 0, 1240, 170, 0,
				"resources/BarreraVertical.png", fTwo);
		Barrera BarreraDerecha2 = new Barrera("Barrera derecha 2", 1390, 0, 1425, 170, 0,
				"resources/BarreraVertical.png", fTwo);

		Barrera BarreraHorizontal2 = new Barrera("Barrera horizontal 2", 1240, 0, 1390, 35, 0,
				"resources/BarreraHorizontal.png", fTwo);
		Plataforma PlataformaDerecha = PG2.pDerecha;
		PlataformaDerecha.setField(fTwo);
		PlataformaDerecha.changeImage("resources/PlataformaGrisHorizontal.png");

		if (PG2.vida != null) {
			up = PG2.vida;
			up.setField(fTwo);
			up.changeImage("resources/Up.gif");
		}

		for (int i = 0; i < PG2.bolas.size(); i++) {
			PG2.bolas.get(i).setField(fTwo);
			if (PG2.bolas.get(i).delete) {
				if (PG2.bolas.get(i).BolaMedianaDer.delete) {
					if (!PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.delete) {
						PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.setField(fTwo);
						if (PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.name.contains("Azul")) {
							PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.changeImage("resources/BolaAzul.png");
						} else {
							PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.changeImage("resources/BolaRoja.png");
						}
					}
					if (!PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.delete) {
						PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.setField(fTwo);
						if (PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.name.contains("Azul")) {
							PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.changeImage("resources/BolaAzul.png");
						} else {
							PG2.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.changeImage("resources/BolaRoja.png");
						}
					}
				} else {
					PG2.bolas.get(i).BolaMedianaDer.setField(fTwo);
					if (PG2.bolas.get(i).BolaMedianaDer.name.contains("Azul")) {
						PG2.bolas.get(i).BolaMedianaDer.changeImage("resources/BolaAzul.png");
					} else {
						PG2.bolas.get(i).BolaMedianaDer.changeImage("resources/BolaRoja.png");
					}
				}
				if (PG2.bolas.get(i).BolaMedianaIzq.delete) {

					if (!PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.delete) {
						PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.setField(fTwo);
						if (PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.name.contains("Azul")) {
							PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.changeImage("resources/BolaAzul.png");
						} else {
							PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.changeImage("resources/BolaRoja.png");
						}
					}
					if (!PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.delete) {
						PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.setField(fTwo);
						if (PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.name.contains("Azul")) {
							PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.changeImage("resources/BolaAzul.png");
						} else {
							PG2.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.changeImage("resources/BolaRoja.png");
						}
					}
				} else {
					PG2.bolas.get(i).BolaMedianaIzq.setField(fTwo);
					if (PG2.bolas.get(i).BolaMedianaIzq.name.contains("Azul")) {
						PG2.bolas.get(i).BolaMedianaIzq.changeImage("resources/BolaAzul.png");
					} else {
						PG2.bolas.get(i).BolaMedianaIzq.changeImage("resources/BolaRoja.png");
					}
				}
			} else {
				if (PG2.bolas.get(i).name.contains("Azul")) {
					PG2.bolas.get(i).changeImage("resources/BolaAzul.png");
				} else {
					PG2.bolas.get(i).changeImage("resources/BolaRoja.png");
				}
			}
		}

		contadorBolas = PG2.contadorB;

		while (!salirPlayTwo) {
			w.changeField(fTwo);
			fTwo.resize();
			fTwo.background = "resources/BackgroundTropical.png";
			fTwo.draw();
			Thread.sleep(18);
			inputTwo(PangRojo, PangAzul);
//			System.out.println("Contador Bolas = " + contadorBolas);

			if (w.getPressedKeys().contains('p')) {
				pausado = true;
				while (pausado) {
					if (w.getPressedKeys().contains('r')) {
						pausado = false;
					}
				}
			}

			PG2 = new PartidaGuardadaTwo(PangRojo, PangAzul, PG2.bolas, PuntsRed.puntsRed, PuntsBlue.puntsBlue,
					contadorBolas, seagull, PlataformaIzquierda, PlataformaDerecha, Vida.apareceVida, aumentoRed,
					aumentoBlue);

			ArrayList<Sprite> listaSpritesClickados = fTwo.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Back":
					back();
					fTwo.clear();
					salirPlayTwo = true;
					break;
				}
			}

			if (PangRojo.vida == 3) {
				VidasDeRojo.changeImage("resources/UpRojo3.png");
			} else if (PangRojo.vida == 2) {
				VidasDeRojo.changeImage("resources/UpRojo2.png");
			} else if (PangRojo.vida == 1) {
				VidasDeRojo.changeImage("resources/UpRojo1.png");
			}

			if (PangRojo.muerto) {
				VidasDeRojo.changeImage("resources/UpRojo0.png");
				PangRojo.trigger = true;
				RedPannelNormal.changeImage("resources/RedPannelHurted.png");
				PangRojo.changeImage("resources/RedLoose.gif");
//				System.out.println("Se cambia imagen del Pang");

				if (!aumentoRed) {
					PangRojo.x2 += 10;
					PangRojo.y1 -= 30;
					PangRojo.y2 += 10;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoRed = true;
			}

			if (PangAzul.vida == 3) {
				VidasDeAzul.changeImage("resources/UpAzul3.png");
			} else if (PangAzul.vida == 2) {
				VidasDeAzul.changeImage("resources/UpAzul2.png");
			} else if (PangAzul.vida == 1) {
				VidasDeAzul.changeImage("resources/UpAzul1.png");
			}

			if (PangAzul.muerto) {
				VidasDeAzul.changeImage("resources/UpAzul0.png");
				PangAzul.trigger = true;
				BluePannelNormal.changeImage("resources/BluePannelHurted.png");
				BluePannelNormal.flippedX = true;
				PangAzul.changeImage("resources/BlueLoose.gif");
//				System.out.println("Se cambia imagen del Pang");

				if (!aumentoBlue) {
					PangAzul.x2 += 10;
					PangAzul.y1 -= 30;
					PangAzul.y2 += 10;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoBlue = true;
			}

			if (PangRojo.muerto && PangAzul.muerto) {
//				System.out.println("Se imprime Game Over");
				TextoJuego gameOver = new TextoJuego("Game Over", 420, 250, 1420, 400, 0, "resources/GameOver.png",
						fTwo);
				w.playMusic("resources/GameOver.wav");
//				System.out.println("Se ejecuta la música");

				if (contadorTimer == 0) {
					TimerTask taskPangMuertoTwo = new TimerTask() {
						@Override
						public void run() {
							pangMuertoTwo(PangRojo, PangAzul);
							fTwo.clear();
							salirPlayTwo = true;
						}
					};
					timer.schedule(taskPangMuertoTwo, 4000);
//					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}

//				System.out.println("Sortir");
			}
			if (contadorBolas == 0 && seagull.muerto) {
				TextoJuego allClear = new TextoJuego("All Clear", 420, 250, 1420, 400, 0, "resources/AllClear.png",
						fTwo);
				RedPannelNormal.changeImage("resources/RedPannelHappy.png");
				BluePannelNormal.changeImage("resources/BluePannelHappy.png");
				BluePannelNormal.flippedX = true;
				PangRojo.changeImage("resources/RedWin.gif");
				PangRojo.flippedX = true;
				PangAzul.changeImage("resources/BlueWin.gif");
				PangAzul.flippedX = false;
				w.playMusic("resources/StageCleared.wav");

				if (PangAzul.muerto) {
					PangAzul.muerto = false;
					PangAzul.x2 += 140;
					PangAzul.y1 -= 80;
					PangAzul.y2 += 10;
				}
				if (PangRojo.muerto) {
					PangRojo.muerto = false;
					PangRojo.x2 += 140;
					PangRojo.y1 -= 80;
					PangRojo.y2 += 10;
				}

				if (!aumentoRed) {
					PangRojo.x2 += 150;
					PangRojo.y1 -= 110;
					PangRojo.y2 += 20;

//					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoRed = true;

				if (!aumentoBlue) {
					PangAzul.x2 += 150;
					PangAzul.y1 -= 110;
					PangAzul.y2 += 20;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoBlue = true;

				if (contadorTimer == 0) {
					TimerTask taskStageClearTwo = new TimerTask() {
						@Override
						public void run() {
							stageClearTwo();
							salirPlayTwo = true;
						}

					};
					timer.schedule(taskStageClearTwo, 5000);
//					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}
			}
		}

	}

	private static void playOne(boolean aumento) throws InterruptedException {
		fNewGame.clear();
		Vida.apareceVida = false;
		PuntsRed.puntsRed = 0;
		salirPlay = false;
		contadorBolas = -1;
		w.playMusic("resources/Stage01(HongKong).wav");
		int contadorTimer = 0;

		BotonMenu Back = new BotonMenu("Back", 1350, 900, 1650, 950, 0, "resources/MenuBackA.png", fOne);

		ArrayList<ColaProjectilRed> colaprojectilPangRojo = new ArrayList<ColaProjectilRed>();
		colaprojectilPangRojo.add(
				new ColaProjectilRed("Cola Projectil PangRojo", 0, 0, 0, 0, 0, "resources/weaponColaRed.png", fOne));

		ProjectilPangRojo projectilPangRojo = new ProjectilPangRojo("Projectil PangRojo", 0, 0, 0, 0, 0,
				"resources/weaponRed.png", fOne, colaprojectilPangRojo);

		PangRojo PangRojo = new PangRojo("Pang azul", 650, 680, 750, 820, 0, "resources/RedPang.gif", fOne,
				projectilPangRojo);
		PangRojo.muerto = false;

		Suelo Suelo = new Suelo("Suelo", 150, 820, 1750, 853, 0, "resources/suelo.png", fOne);

		Pared RedPannel = new Pared("UI Red Pannel", 0, 0, 200, 1050, 0, "resources/UIRedpannel.png", fOne);
		Pared RedPannelNormal = new Pared("Red Pannel Normal", 18, 40, 200, 190, 0, "resources/RedPannelNormal.png",
				fOne);
		Pared BluePannel = new Pared("UI Blue Pannel", 1700, 0, 1900, 1050, 0, "resources/UIBluepannel.png", fOne);
		Sprite P1 = new Sprite("P1", 15, 153, 50, 183, 0, "resources/P1Pannel.png", fOne);

		Sprite RedScore = new Sprite("Red Score", 5, 235, 70, 270, 0, "resources/Score.png", fOne);
		PuntsRed p = new PuntsRed("Punts", 15, 300, 70, 320, 0, "0", fOne);

		Sprite VidasDeRojo = new Sprite("Vidas de Rojo", 15, 480, 155, 550, 0, "resources/UpRojo3.png", fOne);

		Seagull seagull = new Seagull("Seagull", 505, 300, 605, 400, 0, "resources/Seagull.gif", fOne);

		Barrera BarreraIzquierda1 = new Barrera("Barrera izquierda 1", 405, 0, 440, 170, 0,
				"resources/BarreraVertical.png", fOne);
		Barrera BarreraDerecha1 = new Barrera("Barrera derecha 1", 590, 0, 625, 170, 0, "resources/BarreraVertical.png",
				fOne);

		Barrera BarreraHorizontal1 = new Barrera("Barrera horizontal 1", 440, 0, 590, 35, 0,
				"resources/BarreraHorizontal.png", fOne);
		Plataforma PlataformaIzquierda = new Plataforma("Plataforma Izquierda", 440, 140, 590, 170, 0,
				"resources/PlataformaGrisHorizontal.png", fOne);

		BolaGrande BolaGrandeRojaDer2 = new BolaGrande("Bola Grande Roja Der2", 470, 35, 570, 135, 0,
				"resources/BolaRoja.png", fOne, "derecha");

		Barrera BarreraIzquierda2 = new Barrera("Barrera izquierda 2", 1205, 0, 1240, 170, 0,
				"resources/BarreraVertical.png", fOne);
		Barrera BarreraDerecha2 = new Barrera("Barrera derecha 2", 1390, 0, 1425, 170, 0,
				"resources/BarreraVertical.png", fOne);

		Barrera BarreraHorizontal2 = new Barrera("Barrera horizontal 2", 1240, 0, 1390, 35, 0,
				"resources/BarreraHorizontal.png", fOne);
		Plataforma PlataformaDerecha = new Plataforma("Plataforma Derecha", 1240, 140, 1390, 170, 0,
				"resources/PlataformaGrisHorizontal.png", fOne);

		BolaGrande BolaGrandeAzulIzq2 = new BolaGrande("Bola Grande Azul Izq2", 1270, 35, 1370, 135, 0,
				"resources/BolaAzul.png", fOne, "izquierda");

		up = new Vida("Up", 0, 0, 0, 0, 0, "resources/Up.gif", fTwo);

		BolaGrande BolaGrandeRojaIzq = new BolaGrande("Bola Grande Roja Izq", 220, 200, 320, 300, 0,
				"resources/BolaRoja.png", fOne, "derecha");
		BolaGrande BolaGrandeAzulIzq = new BolaGrande("Bola Grande Azul Izq", 340, 300, 440, 400, 0,
				"resources/BolaAzul.png", fOne, "derecha");

		BolaGrande BolaGrandeRojaDer = new BolaGrande("Bola Grande Roja Der", 1580, 200, 1680, 300, 0,
				"resources/BolaRoja.png", fOne, "izquierda");
		BolaGrande BolaGrandeAzulDer = new BolaGrande("Bola Grande Azul Der", 1460, 300, 1560, 400, 0,
				"resources/BolaAzul.png", fOne, "izquierda");

		contadorBolas = 6;

		ArrayList<BolaGrande> listaBolas = new ArrayList<BolaGrande>();
		listaBolas.add(BolaGrandeRojaDer);
		listaBolas.add(BolaGrandeRojaIzq);
		listaBolas.add(BolaGrandeAzulDer);
		listaBolas.add(BolaGrandeAzulIzq);
		listaBolas.add(BolaGrandeRojaDer2);
		listaBolas.add(BolaGrandeAzulIzq2);

		while (!salirPlay) {
			w.changeField(fOne);
			fOne.resize();
			fOne.background = "resources/BackgroundTropical.png";
			fOne.draw();
			Thread.sleep(18);
			input(PangRojo);
//			System.out.println("Contador Bolas = " + contadorBolas);

			if (w.getPressedKeys().contains('p')) {
				pausado = true;
				while (pausado) {
					if (w.getPressedKeys().contains('r')) {
						pausado = false;
					}
				}
			}

			PG = new PartidaGuardada(PangRojo, listaBolas, PuntsRed.puntsRed, contadorBolas, seagull,
					PlataformaIzquierda, PlataformaDerecha, Vida.apareceVida, aumento);

			ArrayList<Sprite> listaSpritesClickados = fOne.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Back":
					back();
					fOne.clear();
					salirPlay = true;
					break;
				}
			}

			if (PangRojo.vida == 3) {
				VidasDeRojo.changeImage("resources/UpRojo3.png");
			} else if (PangRojo.vida == 2) {
				VidasDeRojo.changeImage("resources/UpRojo2.png");
			} else if (PangRojo.vida == 1) {
				VidasDeRojo.changeImage("resources/UpRojo1.png");
			}

			if (PangRojo.muerto) {
				VidasDeRojo.changeImage("resources/UpRojo0.png");
				PangRojo.trigger = true;
//				System.out.println("Se imprime Game Over");
				TextoJuego gameOver = new TextoJuego("Game Over", 420, 250, 1420, 400, 0, "resources/GameOver.png",
						fOne);
				RedPannelNormal.changeImage("resources/RedPannelHurted.png");
				PangRojo.changeImage("resources/RedLoose.gif");
//				System.out.println("Se cambia imagen del Pang");
				w.playMusic("resources/GameOver.wav");
//				System.out.println("Se ejecuta la música");

				if (!aumento) {
					PangRojo.x2 += 10;
					PangRojo.y1 -= 30;
					PangRojo.y2 += 10;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumento = true;

				if (contadorTimer == 0) {
					TimerTask taskPangMuerto = new TimerTask() {
						@Override
						public void run() {
							pangMuerto(PangRojo);
							salirPlay = true;
							fOne.clear();
						}
					};
					timer.schedule(taskPangMuerto, 4000);
					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}

//				System.out.println("Sortir");
			}
			if (contadorBolas == 0 && seagull.muerto) {
				TextoJuego allClear = new TextoJuego("All Clear", 420, 250, 1420, 400, 0, "resources/AllClear.png",
						fOne);
				RedPannelNormal.changeImage("resources/RedPannelHappy.png");
				PangRojo.changeImage("resources/RedWin.gif");
				w.playMusic("resources/StageCleared.wav");

				if (!aumento) {
					PangRojo.x2 += 150;
					PangRojo.y1 -= 110;
					PangRojo.y2 += 20;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumento = true;

				if (contadorTimer == 0) {
					TimerTask taskStageClear = new TimerTask() {
						@Override
						public void run() {
							stageClear();
							salirPlay = true;
							fOne.clear();
						}

					};
					timer.schedule(taskStageClear, 5000);
//					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}
			}
		}
	}

	private static void guardarPartida() {
		try {
			File f = new File("save.dat");
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(PG);
			oos.flush();
			oos.close();

//			System.out.println(PG);
			System.out.println("Se ha guardado la partida");

		} catch (FileNotFoundException e) {
			System.out.println("No existeix el fitxer");
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void guardarPartidaTwo() {
		try {
			File f = new File("save.dat");
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(PG2);
			oos.flush();
			oos.close();

			System.out.println(PG2);
			System.out.println("Se ha guardado la partida");

		} catch (FileNotFoundException e) {
			System.out.println("No existeix el fitxer");
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void cargarPartida() throws ClassNotFoundException, InterruptedException {
		try {
			File f = new File("save.dat");
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);

			Object o = ois.readObject();

			if (o instanceof PartidaGuardada) {
				PG = (PartidaGuardada) o;
//				System.out.println("Hay PG");
				HayPG = true;
			} else if (o instanceof PartidaGuardadaTwo) {
				PG2 = (PartidaGuardadaTwo) o;
//				System.out.println("No hay PG");
				HayPG = false;
			}

			fis.close();
			ois.close();

//			System.out.println("Se ha cargado la partida");

		} catch (FileNotFoundException e) {
			System.out.println("No existeix el fitxer");
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void input(PangRojo PangRojo) {
		if (!PangRojo.muerto) {
			if (w.getKeysDown().contains(' ')) {
				PangRojo.moviment(Inputs.DISPARAR);
			}
			if (w.getPressedKeys().contains('d')) {
				PangRojo.moviment(Inputs.DRETA);
			} else if (w.getPressedKeys().contains('a')) {
				PangRojo.moviment(Inputs.ESQUERRA);
			} else {
				PangRojo.moviment(Inputs.QUIET);
			}
		}

		if (w.getPressedKeys().contains('u')) {
			guardarPartida();
			HayPG = true;
		}
	}

	private static void inputTwo(PangRojo PangRojo, PangAzul PangAzul) {
		if (!PangRojo.muerto) {
			if (w.getKeysDown().contains(' ')) {
				PangRojo.moviment(Inputs.DISPARAR);
			}
			if (w.getPressedKeys().contains('d')) {
				PangRojo.moviment(Inputs.DRETA);
			} else if (w.getPressedKeys().contains('a')) {
				PangRojo.moviment(Inputs.ESQUERRA);
			} else {
				PangRojo.moviment(Inputs.QUIET);
			}
		}

		if (!PangAzul.muerto) {
			if (w.getKeysDown().contains('8')) {
				PangAzul.moviment(Inputs.DISPARAR);
			}
			if (w.getPressedKeys().contains('6')) {
				PangAzul.moviment(Inputs.DRETA);
			} else if (w.getPressedKeys().contains('4')) {
				PangAzul.moviment(Inputs.ESQUERRA);
			} else {
				PangAzul.moviment(Inputs.QUIET);
			}
		}

		if (w.getPressedKeys().contains('u')) {
			guardarPartidaTwo();
			HayPG = false;
		}

	}

	private static void pangMuerto(PangRojo PangRojo) {
		System.out.println("Se ejecuta task -pangMuerto-");
		w.stopMusic();
		w.changeField(fMenu);
		fOne.clear();
		PangRojo.muerto = false;
		PangRojo.trigger = false;
		timer.purge();
		System.out.println("Se termina task -pangMuerto-");
	}

	private static void pangMuertoTwo(PangRojo PangRojo, PangAzul PangAzul) {
		System.out.println("Se ejecuta task -pangMuertoTwo-");
		w.stopMusic();
		w.changeField(fMenu);
//		fTwo.clear();
		PangRojo.muerto = false;
		PangAzul.muerto = false;
		PangRojo.trigger = false;
		PangAzul.trigger = false;
		timer.purge();
		System.out.println("Se termina task -pangMuertoTwo-");
	}

	private static void stageClear() {
		System.out.println("Se ejecuta task -stageClear-");
		w.stopMusic();
		w.changeField(fMenu);
		fOne.clear();
		contadorBolas = -1;
		timer.purge();
		System.out.println("Se termina task -stageClear-");

	}

	private static void stageClearTwo() {
		System.out.println("Se ejecuta task -stageClearTwo-");
		w.stopMusic();
		fTwo.clear();
		w.changeField(fMenu);
		contadorBolas = -1;
		timer.purge();
		System.out.println("Se termina task -stageClearTwo-");

	}

	private static void newGame() throws InterruptedException {
		boolean aumentoRed = false;
		boolean aumentoBlue = false;

		BotonMenu Player1 = new BotonMenu("Player 1", 1170, 230, 1670, 300, 0, "resources/Menu1PA.png", fNewGame);
		BotonMenu Player2 = new BotonMenu("Player 2", 1170, 350, 1670, 420, 0, "resources/Menu2PA.png", fNewGame);
		BotonMenu Back = new BotonMenu("Back", 1350, 500, 1650, 550, 0, "resources/MenuBackA.png", fNewGame);

		Sprite P1 = new Sprite("P1", 1020, 230, 1120, 300, 0, "resources/Menu1Player.png", fNewGame);
		P1.visible = false;
		Sprite P12 = new Sprite("P12", 970, 350, 1070, 420, 0, "resources/Menu1Player.png", fNewGame);
		P12.visible = false;
		Sprite P2 = new Sprite("P2", 1070, 350, 1170, 420, 0, "resources/Menu2Player.png", fNewGame);
		P2.visible = false;

		boolean sortir = false;

		while (!sortir) {
			w.changeField(fNewGame);
			fNewGame.resize();
			fNewGame.background = "resources/MainMenu.jpg";
			fNewGame.draw();

			ArrayList<Sprite> listaSpritesHover = fNewGame.getMouseOverSprite();
			boolean butPlayer1 = false;
			boolean butPlayer2 = false;
			boolean butBack = false;

			for (Sprite s : listaSpritesHover) {
				butPlayer1 = false;
				butPlayer2 = false;

				switch (s.name) {
				case "Player 1":
					butPlayer1 = true;
					s.changeImage("resources/Menu1PC.png");
					break;
				case "Player 2":
					butPlayer2 = true;
					s.changeImage("resources/Menu2PC.png");
					break;
				case "Back":
					butBack = true;
					s.changeImage("resources/MenuBackC.png");
					break;
				}
			}
			if (butPlayer1) {
				P1.visible = true;
				P2.visible = false;
				P12.visible = false;
				Player2.changeImage("resources/Menu2PA.png");
				Back.changeImage("resources/MenuBackA.png");
			} else if (butPlayer2) {
				P1.visible = false;
				P2.visible = true;
				P12.visible = true;
				Player1.changeImage("resources/Menu1PA.png");
				Back.changeImage("resources/MenuBackA.png");
			} else if (butBack) {
				P1.visible = false;
				P2.visible = false;
				P12.visible = false;
				Player1.changeImage("resources/Menu1PA.png");
				Player2.changeImage("resources/Menu2PA.png");
			}

			ArrayList<Sprite> listaSpritesClickados = fNewGame.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Player 1":
					playOne(aumentoRed);
					sortir = true;
					break;
				case "Player 2":
					playTwo(aumentoRed, aumentoBlue);
					sortir = true;
					break;
				case "Back":
					back();
					sortir = true;
					break;
				}
			}
		}

	}

	private static void playTwo(boolean aumentoRed, boolean aumentoBlue) throws InterruptedException {
		fNewGame.clear();
		fTwo.clear();
		Vida.apareceVida = false;
		PuntsRed.puntsRed = 0;
		PuntsBlue.puntsBlue = 0;
		salirPlayTwo = false;
		contadorBolas = -1;
		w.playMusic("resources/Stage01(HongKong).wav");
		int contadorTimer = 0;

		BotonMenu Back = new BotonMenu("Back", 1350, 900, 1650, 950, 0, "resources/MenuBackA.png", fTwo);

		Suelo Suelo = new Suelo("Suelo", 150, 820, 1750, 853, 0, "resources/suelo.png", fTwo);

		// PANG ROJO
		ArrayList<ColaProjectilRed> colaprojectilPangRojo = new ArrayList<ColaProjectilRed>();
		colaprojectilPangRojo.add(
				new ColaProjectilRed("Cola Projectil PangRojo", 0, 0, 0, 0, 0, "resources/weaponColaRed.png", fTwo));

		ProjectilPangRojo projectilPangRojo = new ProjectilPangRojo("Projectil PangRojo", 0, 0, 0, 0, 0,
				"resources/weaponRed.png", fTwo, colaprojectilPangRojo);

		PangRojo PangRojo = new PangRojo("Pang azul", 750, 680, 850, 820, 0, "resources/RedPang.gif", fTwo,
				projectilPangRojo);
		PangRojo.muerto = false;

		Pared RedPannel = new Pared("UI Red Pannel", 0, 0, 200, 1050, 0, "resources/UIRedpannel.png", fTwo);
		Pared RedPannelNormal = new Pared("Red Pannel Normal", 18, 40, 200, 190, 0, "resources/RedPannelNormal.png",
				fTwo);
		Sprite P1 = new Sprite("P1", 15, 153, 50, 183, 0, "resources/P1Pannel.png", fTwo);

		Sprite RedScore = new Sprite("Red Score", 5, 235, 70, 270, 0, "resources/Score.png", fTwo);
		PuntsRed pRed = new PuntsRed("Red Punts", 15, 300, 70, 320, 0, "0", fTwo);

		Sprite VidasDeRojo = new Sprite("Vidas de Rojo", 15, 480, 155, 550, 0, "resources/UpRojo3.png", fTwo);

		// PANG AZUL
		ArrayList<ColaProjectilBlue> colaprojectilPangAzul = new ArrayList<ColaProjectilBlue>();
		colaprojectilPangAzul.add(
				new ColaProjectilBlue("Cola Projectil PangAzul", 0, 0, 0, 0, 0, "resources/weaponColaBlue.png", fTwo));

		ProjectilPangAzul projectilPangAzul = new ProjectilPangAzul("Projectil PangAzul", 0, 0, 0, 0, 0,
				"resources/weaponBlue.png", fTwo, colaprojectilPangAzul);

		PangAzul PangAzul = new PangAzul("Pang Azul", 1050, 680, 1150, 820, 0, "resources/BluePang.gif", fTwo,
				projectilPangAzul);
		PangAzul.muerto = false;
		PangAzul.flippedX = true;

		Pared BluePannel = new Pared("UI Blue Pannel", 1700, 0, 1900, 1050, 0, "resources/UIBluepannel.png", fTwo);
		Pared BluePannelNormal = new Pared("Blue Pannel Normal", 1700, 40, 1880, 190, 0,
				"resources/BluePannelNormal.png", fTwo);
		BluePannelNormal.flippedX = true;
		Sprite P2 = new Sprite("P2", 1850, 153, 1885, 183, 0, "resources/P2Pannel.png", fTwo);

		Sprite BlueScore = new Sprite("Blue Score", 1825, 235, 1900, 270, 0, "resources/Score.png", fTwo);
		PuntsBlue pBlue = new PuntsBlue("Blue Punts", 1745, 300, 1810, 320, 0, "0", fTwo);

		Sprite VidasDeAzul = new Sprite("Vidas de Azul", 1745, 480, 1885, 550, 0, "resources/UpAzul3.png", fTwo);

		Seagull seagull = new Seagull("Seagull", 505, 300, 605, 400, 0, "resources/Seagull.gif", fTwo);

		Barrera BarreraIzquierda1 = new Barrera("Barrera izquierda 1", 405, 0, 440, 170, 0,
				"resources/BarreraVertical.png", fTwo);
		Barrera BarreraDerecha1 = new Barrera("Barrera derecha 1", 590, 0, 625, 170, 0, "resources/BarreraVertical.png",
				fTwo);

		Barrera BarreraHorizontal1 = new Barrera("Barrera horizontal 1", 440, 0, 590, 35, 0,
				"resources/BarreraHorizontal.png", fTwo);
		Plataforma PlataformaIzquierda = new Plataforma("Plataforma Izquierda", 440, 140, 590, 170, 0,
				"resources/PlataformaGrisHorizontal.png", fTwo);

		BolaGrande BolaGrandeRojaDer2 = new BolaGrande("Bola Grande Roja Der2", 470, 35, 570, 135, 0,
				"resources/BolaRoja.png", fTwo, "derecha");

		Barrera BarreraIzquierda2 = new Barrera("Barrera izquierda 2", 1205, 0, 1240, 170, 0,
				"resources/BarreraVertical.png", fTwo);
		Barrera BarreraDerecha2 = new Barrera("Barrera derecha 2", 1390, 0, 1425, 170, 0,
				"resources/BarreraVertical.png", fTwo);

		Barrera BarreraHorizontal2 = new Barrera("Barrera horizontal 2", 1240, 0, 1390, 35, 0,
				"resources/BarreraHorizontal.png", fTwo);
		Plataforma PlataformaDerecha = new Plataforma("Plataforma Derecha", 1240, 140, 1390, 170, 0,
				"resources/PlataformaGrisHorizontal.png", fTwo);

		BolaGrande BolaGrandeAzulIzq2 = new BolaGrande("Bola Grande Azul Izq2", 1270, 35, 1370, 135, 0,
				"resources/BolaAzul.png", fTwo, "izquierda");

		up = new Vida("Up", 0, 0, 0, 0, 0, "resources/Up.gif", fTwo);

		BolaGrande BolaGrandeRojaIzq = new BolaGrande("Bola Grande Roja Izq", 220, 200, 320, 300, 0,
				"resources/BolaRoja.png", fTwo, "derecha");
		BolaGrande BolaGrandeAzulIzq = new BolaGrande("Bola Grande Azul Izq", 340, 300, 440, 400, 0,
				"resources/BolaAzul.png", fTwo, "derecha");

		BolaGrande BolaGrandeRojaDer = new BolaGrande("Bola Grande Roja Der", 1580, 200, 1680, 300, 0,
				"resources/BolaRoja.png", fTwo, "izquierda");
		BolaGrande BolaGrandeAzulDer = new BolaGrande("Bola Grande Azul Der", 1460, 300, 1560, 400, 0,
				"resources/BolaAzul.png", fTwo, "izquierda");

		contadorBolas = 6;

		ArrayList<BolaGrande> listaBolas = new ArrayList<BolaGrande>();
		listaBolas.add(BolaGrandeRojaDer);
		listaBolas.add(BolaGrandeRojaIzq);
		listaBolas.add(BolaGrandeAzulDer);
		listaBolas.add(BolaGrandeAzulIzq);
		listaBolas.add(BolaGrandeRojaDer2);
		listaBolas.add(BolaGrandeAzulIzq2);

		while (!salirPlayTwo) {
			w.changeField(fTwo);
			fTwo.resize();
			fTwo.background = "resources/BackgroundTropical.png";
			fTwo.draw();
			Thread.sleep(18);
			inputTwo(PangRojo, PangAzul);
//			System.out.println("Contador Bolas = " + contadorBolas);

			if (w.getPressedKeys().contains('p')) {
				pausado = true;
				while (pausado) {
					if (w.getPressedKeys().contains('r')) {
						pausado = false;
					}
				}
			}

			PG2 = new PartidaGuardadaTwo(PangRojo, PangAzul, listaBolas, PuntsRed.puntsRed, PuntsBlue.puntsBlue,
					contadorBolas, seagull, PlataformaIzquierda, PlataformaDerecha, Vida.apareceVida, aumentoRed,
					aumentoBlue);

			ArrayList<Sprite> listaSpritesClickados = fTwo.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Back":
					back();
					fTwo.clear();
					salirPlayTwo = true;
					break;
				}
			}

			if (PangRojo.vida == 3) {
				VidasDeRojo.changeImage("resources/UpRojo3.png");
			} else if (PangRojo.vida == 2) {
				VidasDeRojo.changeImage("resources/UpRojo2.png");
			} else if (PangRojo.vida == 1) {
				VidasDeRojo.changeImage("resources/UpRojo1.png");
			}

			if (PangRojo.muerto) {
				VidasDeRojo.changeImage("resources/UpRojo0.png");
				PangRojo.trigger = true;
				RedPannelNormal.changeImage("resources/RedPannelHurted.png");
				PangRojo.changeImage("resources/RedLoose.gif");
				System.out.println("Se cambia imagen del Pang");

				if (!aumentoRed) {
					PangRojo.x2 += 10;
					PangRojo.y1 -= 30;
					PangRojo.y2 += 10;
					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoRed = true;
			}

			if (PangAzul.vida == 3) {
				VidasDeAzul.changeImage("resources/UpAzul3.png");
			} else if (PangAzul.vida == 2) {
				VidasDeAzul.changeImage("resources/UpAzul2.png");
			} else if (PangAzul.vida == 1) {
				VidasDeAzul.changeImage("resources/UpAzul1.png");
			}

			if (PangAzul.muerto) {
				VidasDeAzul.changeImage("resources/UpAzul0.png");
				PangAzul.trigger = true;
				BluePannelNormal.changeImage("resources/BluePannelHurted.png");
				BluePannelNormal.flippedX = true;
				PangAzul.changeImage("resources/BlueLoose.gif");
				System.out.println("Se cambia imagen del Pang");

				if (!aumentoBlue) {
					PangAzul.x2 += 10;
					PangAzul.y1 -= 30;
					PangAzul.y2 += 10;
					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoBlue = true;
			}

			if (PangRojo.muerto && PangAzul.muerto) {
				System.out.println("Se imprime Game Over");
				TextoJuego gameOver = new TextoJuego("Game Over", 420, 250, 1420, 400, 0, "resources/GameOver.png",
						fTwo);
				w.playMusic("resources/GameOver.wav");
				System.out.println("Se ejecuta la música");

				if (contadorTimer == 0) {
					TimerTask taskPangMuertoTwo = new TimerTask() {
						@Override
						public void run() {
							pangMuertoTwo(PangRojo, PangAzul);
							salirPlayTwo = true;
							fTwo.clear();
						}
					};
					timer.schedule(taskPangMuertoTwo, 4000);
					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}

				System.out.println("Sortir");
			}
			if (contadorBolas == 0 && seagull.muerto) {
				TextoJuego allClear = new TextoJuego("All Clear", 420, 250, 1420, 400, 0, "resources/AllClear.png",
						fTwo);
				RedPannelNormal.changeImage("resources/RedPannelHappy.png");
				BluePannelNormal.changeImage("resources/BluePannelHappy.png");
				BluePannelNormal.flippedX = true;
				PangRojo.changeImage("resources/RedWin.gif");
				PangRojo.flippedX = true;
				PangAzul.changeImage("resources/BlueWin.gif");
				PangAzul.flippedX = false;
				w.playMusic("resources/StageCleared.wav");

				if (PangAzul.muerto) {
					PangAzul.muerto = false;
					PangAzul.x2 += 140;
					PangAzul.y1 -= 80;
					PangAzul.y2 += 10;
				}
				if (PangRojo.muerto) {
					PangRojo.muerto = false;
					PangRojo.x2 += 140;
					PangRojo.y1 -= 80;
					PangRojo.y2 += 10;
				}

				if (!aumentoRed) {
					PangRojo.x2 += 150;
					PangRojo.y1 -= 110;
					PangRojo.y2 += 20;

//					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoRed = true;

				if (!aumentoBlue) {
					PangAzul.x2 += 150;
					PangAzul.y1 -= 110;
					PangAzul.y2 += 20;
					System.out.println("Se aumenta imagen del Pang");
				}
				aumentoBlue = true;

				if (contadorTimer == 0) {
					TimerTask taskStageClearTwo = new TimerTask() {
						@Override
						public void run() {
							stageClearTwo();
							salirPlayTwo = true;
							fTwo.clear();
						}

					};
					timer.schedule(taskStageClearTwo, 5000);
					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}
			}
		}

	}

	private static void continueGame() throws InterruptedException {
		fOne.clear();
		Vida.apareceVida = PG.apareceVida;
		PuntsRed.puntsRed = PG.puntsRed;
		salirPlay = false;
		w.playMusic("resources/Stage01(HongKong).wav");
		contadorBolas = -1;
		int contadorTimer = 0;
		boolean aumento = false;

		BotonMenu Back = new BotonMenu("Back", 1350, 900, 1650, 950, 0, "resources/MenuBackA.png", fOne);

		ArrayList<ColaProjectilRed> colaprojectilPangRojo = new ArrayList<ColaProjectilRed>();
		colaprojectilPangRojo.add(
				new ColaProjectilRed("Cola Projectil PangRojo", 0, 0, 0, 0, 0, "resources/weaponColaRed.png", fOne));

		ProjectilPangRojo projectilPangRojo = new ProjectilPangRojo("Projectil PangRojo", 0, 0, 0, 0, 0,
				"resources/weaponRed.png", fOne, colaprojectilPangRojo);

		PangRojo PangRojo = PG.RedPang;
		PangRojo.setField(fOne);
		PangRojo.projectil = projectilPangRojo;
		PangRojo.muerto = false;

		Suelo Suelo = new Suelo("Suelo", 150, 820, 1750, 853, 0, "resources/suelo.png", fOne);

		Pared RedPannel = new Pared("UI Red Pannel", 0, 0, 200, 1050, 0, "resources/UIRedpannel.png", fOne);
		Pared RedPannelNormal = new Pared("Red Pannel Normal", 18, 40, 200, 190, 0, "resources/RedPannelNormal.png",
				fOne);
		Pared BluePannel = new Pared("UI Blue Pannel", 1700, 0, 1900, 1050, 0, "resources/UIBluepannel.png", fOne);
		Sprite P1 = new Sprite("P1", 15, 153, 50, 183, 0, "resources/P1Pannel.png", fOne);

		Sprite RedScore = new Sprite("Red Score", 5, 235, 70, 270, 0, "resources/Score.png", fOne);
		PuntsRed pRed = new PuntsRed("Punts", 15, 300, 70, 320, 0, "0", fOne);

		Sprite VidasDeRojo = new Sprite("Vidas de Rojo", 15, 480, 155, 550, 0, "resources/UpRojo3.png", fOne);

		Seagull seagull = PG.seagull;
		seagull.setField(fOne);
		seagull.changeImage("resources/Seagull.gif");

		Barrera BarreraIzquierda1 = new Barrera("Barrera izquierda 1", 405, 0, 440, 170, 0,
				"resources/BarreraVertical.png", fOne);
		Barrera BarreraDerecha1 = new Barrera("Barrera derecha 1", 590, 0, 625, 170, 0, "resources/BarreraVertical.png",
				fOne);

		Barrera BarreraHorizontal1 = new Barrera("Barrera horizontal 1", 440, 0, 590, 35, 0,
				"resources/BarreraHorizontal.png", fOne);
		Plataforma PlataformaIzquierda = PG.pIzquierda;
		PlataformaIzquierda.setField(fOne);
		PlataformaIzquierda.changeImage("resources/PlataformaGrisHorizontal.png");

		Barrera BarreraIzquierda2 = new Barrera("Barrera izquierda 2", 1205, 0, 1240, 170, 0,
				"resources/BarreraVertical.png", fOne);
		Barrera BarreraDerecha2 = new Barrera("Barrera derecha 2", 1390, 0, 1425, 170, 0,
				"resources/BarreraVertical.png", fOne);

		Barrera BarreraHorizontal2 = new Barrera("Barrera horizontal 2", 1240, 0, 1390, 35, 0,
				"resources/BarreraHorizontal.png", fOne);
		Plataforma PlataformaDerecha = PG.pDerecha;
		PlataformaDerecha.setField(fOne);
		PlataformaDerecha.changeImage("resources/PlataformaGrisHorizontal.png");

		if (PG.vida != null) {
			up = PG.vida;
			up.setField(fOne);
			up.changeImage("resources/Up.gif");
		}

		for (int i = 0; i < PG.bolas.size(); i++) {
			PG.bolas.get(i).setField(fOne);
			if (PG.bolas.get(i).delete) {
				if (PG.bolas.get(i).BolaMedianaDer.delete) {
					if (!PG.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.delete) {
						PG.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.setField(fOne);
						if (PG.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.name.contains("Azul")) {
							PG.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.changeImage("resources/BolaAzul.png");
						} else {
							PG.bolas.get(i).BolaMedianaDer.BolaPequeñaDer.changeImage("resources/BolaRoja.png");
						}
					}
					if (!PG.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.delete) {
						PG.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.setField(fOne);
						if (PG.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.name.contains("Azul")) {
							PG.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.changeImage("resources/BolaAzul.png");
						} else {
							PG.bolas.get(i).BolaMedianaDer.BolaPequeñaIzq.changeImage("resources/BolaRoja.png");
						}
					}
				} else {
					PG.bolas.get(i).BolaMedianaDer.setField(fOne);
					if (PG.bolas.get(i).BolaMedianaDer.name.contains("Azul")) {
						PG.bolas.get(i).BolaMedianaDer.changeImage("resources/BolaAzul.png");
					} else {
						PG.bolas.get(i).BolaMedianaDer.changeImage("resources/BolaRoja.png");
					}
				}
				if (PG.bolas.get(i).BolaMedianaIzq.delete) {

					if (!PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.delete) {
						PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.setField(fOne);
						if (PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.name.contains("Azul")) {
							PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.changeImage("resources/BolaAzul.png");
						} else {
							PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaDer.changeImage("resources/BolaRoja.png");
						}
					}
					if (!PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.delete) {
						PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.setField(fOne);
						if (PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.name.contains("Azul")) {
							PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.changeImage("resources/BolaAzul.png");
						} else {
							PG.bolas.get(i).BolaMedianaIzq.BolaPequeñaIzq.changeImage("resources/BolaRoja.png");
						}
					}
				} else {
					PG.bolas.get(i).BolaMedianaIzq.setField(fOne);
					if (PG.bolas.get(i).BolaMedianaIzq.name.contains("Azul")) {
						PG.bolas.get(i).BolaMedianaIzq.changeImage("resources/BolaAzul.png");
					} else {
						PG.bolas.get(i).BolaMedianaIzq.changeImage("resources/BolaRoja.png");
					}
				}
			} else {
				if (PG.bolas.get(i).name.contains("Azul")) {
					PG.bolas.get(i).changeImage("resources/BolaAzul.png");
				} else {
					PG.bolas.get(i).changeImage("resources/BolaRoja.png");
				}
			}
		}

		contadorBolas = PG.contadorB;

		while (!salirPlay) {
			w.changeField(fOne);
			fOne.resize();
			fOne.background = "resources/BackgroundTropical.png";
			fOne.draw();
			Thread.sleep(18);
			input(PangRojo);
//			System.out.println("Contador Bolas = " + contadorBolas);

			if (w.getPressedKeys().contains('p')) {
				pausado = true;
				while (pausado) {
					if (w.getPressedKeys().contains('r')) {
						pausado = false;
					}
				}
			}

			PG = new PartidaGuardada(PangRojo, PG.bolas, PuntsRed.puntsRed, contadorBolas, seagull, PlataformaIzquierda,
					PlataformaDerecha, Vida.apareceVida, aumento);

			ArrayList<Sprite> listaSpritesClickados = fOne.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Back":
					back();
					fOne.clear();
					salirPlay = true;
					break;
				}
			}

			if (PangRojo.vida == 3) {
				VidasDeRojo.changeImage("resources/UpRojo3.png");
			} else if (PangRojo.vida == 2) {
				VidasDeRojo.changeImage("resources/UpRojo2.png");
			} else if (PangRojo.vida == 1) {
				VidasDeRojo.changeImage("resources/UpRojo1.png");
			}

			if (PangRojo.muerto) {
				VidasDeRojo.changeImage("resources/UpRojo0.png");
				PangRojo.trigger = true;
//				System.out.println("Se imprime Game Over");
				TextoJuego gameOver = new TextoJuego("Game Over", 420, 250, 1420, 400, 0, "resources/GameOver.png",
						fOne);
				RedPannelNormal.changeImage("resources/RedPannelHurted.png");
				PangRojo.changeImage("resources/RedLoose.gif");
//				System.out.println("Se cambia imagen del Pang");
				w.playMusic("resources/GameOver.wav");
//				System.out.println("Se ejecuta la música");

				if (!aumento) {
					PangRojo.x2 += 10;
					PangRojo.y1 -= 30;
					PangRojo.y2 += 10;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumento = true;

				if (contadorTimer == 0) {
					TimerTask taskPangMuerto = new TimerTask() {
						@Override
						public void run() {
							pangMuerto(PangRojo);
							fOne.clear();
							salirPlay = true;
						}
					};
					timer.schedule(taskPangMuerto, 4000);
//					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}
			}
			if (contadorBolas == 0 && seagull.muerto) {
				TextoJuego allClear = new TextoJuego("All Clear", 420, 250, 1420, 400, 0, "resources/AllClear.png",
						fOne);
				RedPannelNormal.changeImage("resources/RedPannelHappy.png");
				PangRojo.changeImage("resources/RedWin.gif");
				w.playMusic("resources/StageCleared.wav");

				if (!aumento) {
					PangRojo.x2 += 150;
					PangRojo.y1 -= 110;
					PangRojo.y2 += 20;
//					System.out.println("Se aumenta imagen del Pang");
				}
				aumento = true;

				if (contadorTimer == 0) {
					TimerTask taskStageClear = new TimerTask() {
						@Override
						public void run() {
							stageClear();
							fOne.clear();
							salirPlay = true;
						}

					};
					timer.schedule(taskStageClear, 5000);
					System.out.println("Se ejecuta timer");
					contadorTimer = 1;
				}
			}
		}

	}

	private static void back() throws InterruptedException {
		w.changeField(fMenu);
	}

	private static void credits() throws InterruptedException {
		fCredits.resize();
		w.playMusic("resources/StaffRollEnding.wav");

		Texto QuitCredits = new Texto("Skip Credits", 1600, 800, 1700, 900, "Skip credits", fCredits);

		TextoPB creditos = new TextoPB("Creditos", 790, 680, 890, 820, "CRÉDITOS", fCredits);
		creditos.font = new Font("Consolas", Font.PLAIN, 68);
		creditos.textColor = 0xFFFF00;

		TextoPB desarrollador = new TextoPB("Desarrollador", 750, 850, 890, 990, "DESARROLLADOR ORIGINAL", fCredits);
		desarrollador.font = new Font("Consolas", Font.PLAIN, 32);
		TextoPB mitchel = new TextoPB("Mitchel", 760, 890, 860, 1030, "MITCHELL CORPPORATION (1990)", fCredits);
		mitchel.textColor = 0xFFFFFF;

		TextoPB motor = new TextoPB("Motor", 880, 1000, 1020, 1140, "MOTOR", fCredits);
		motor.font = new Font("Consolas", Font.PLAIN, 32);
		TextoPB marc = new TextoPB("Marc", 840, 1040, 940, 1180, "MARC ALBAREDA", fCredits);
		marc.textColor = 0xFFFFFF;

		TextoPB realizacion = new TextoPB("Realizacion", 830, 1150, 970, 1290, "REALIZACIÓN", fCredits);
		realizacion.font = new Font("Consolas", Font.PLAIN, 32);
		TextoPB selene = new TextoPB("Selene", 835, 1190, 935, 1330, "SELENE MILANÉS", fCredits);
		selene.textColor = 0xFFFFFF;

		TextoPB like = new TextoPB("Like", 600, 1600, 650, 1940, "HOPE YOU ENJOYED IT", fCredits);
		like.font = new Font("Consolas", Font.PLAIN, 68);
		like.textColor = 0xFFFF00;

		Sprite RedPangWin = new Sprite("Red Pang Win", 700, 480, 920, 680, 0, "resources/RedWin.gif", fCredits);
		RedPangWin.visible = false;
		Sprite BluePangWin = new Sprite("Blue Pang Win", 900, 480, 1120, 680, 0, "resources/BlueWin.gif", fCredits);
		BluePangWin.flippedX = true;
		BluePangWin.visible = false;

		Cocodrilo Crocodile = new Cocodrilo("Crocodile", 200, 850, 320, 950, 0, "resources/Crocodile.gif", fCredits);
		Crocodile.flippedX = true;
		Crocodile.visible = false;

		boolean sortir = false;
		boolean hover = false;

		while (!sortir) {
			w.changeField(fCredits);
			fCredits.resize();
			fCredits.background = "resources/BackgroundBlack.jpg";
			fCredits.draw();
			Thread.sleep(18);

			ArrayList<Sprite> listaSpritesHover = fCredits.getMouseOverSprite();
			hover = false;
			for (Sprite s : listaSpritesHover) {
				if (s.name.equals("Skip Credits")) {
					hover = true;
					QuitCredits.textColor = 0xFFFF00;
				}
			}

			if (!hover) {
				QuitCredits.textColor = 0xFFFFFF;
			}

			ArrayList<Sprite> listaSpritesClickados = fCredits.getCurrentMouseSprite();

			for (Sprite s : listaSpritesClickados) {
				switch (s.name) {
				case "Skip Credits":
					w.changeField(fMenu);
					fCredits.clear();
					sortir = true;
					break;
				}
			}

			if (like.y1 == 4.0) {
				like.setVelocity(0, 0);
				Crocodile.visible = true;
				Crocodile.setVelocity(2, Crocodile.velocity[1]);
				RedPangWin.visible = true;
				BluePangWin.visible = true;
			}
		}
	}

}
