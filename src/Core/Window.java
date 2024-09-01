package Core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Window representation of the GUI interface. Manages every part of the GUI
 * which is not the board or field proper.
 * 
 * @author Marc Albareda
 * @version 1.42
 */
public class Window extends JFrame {

	/*
	 * TODO Posar opci� de carregarli un jPanel de pantalla d'intro?
	 */

	/**
	 * Activates verbose mode
	 */
	boolean verbose = false;
	/**
	 * Pixel Field used
	 */
	private Field field;
	/**
	 * Horizontal size of the window
	 */
	private boolean secondBoard = false;
	/**
	 * Checks whether labels should appear at the right of the screen
	 */
	private boolean actLabels = false;
	/**
	 * Checks whether the debug label, a label with mouse and keyboard input, should
	 * appear
	 */
	private boolean debugLabel = false;
	/**
	 * Content of the labels
	 */
	private String[] labels = { "" };
	/**
	 * Panel containing all the labels
	 */
	private JPanel labelpanel = new JPanel(new GridLayout(0, 1, 5, 5));
	/**
	 * tpanel used as container for both boards if double board mode is activated
	 */
	private JPanel tpanel = new JPanel(new GridLayout(0, 2, 5, 5));
	/**
	 * Last keyboard char pressed, even if it's not currently pressed
	 */
	private char keyPressed;
	/**
	 * Last keyboard char pressed, resets every time on check
	 * 
	 * @deprecated
	 */
	private char currentKeyPressed;
	/**
	 * set with every key CURRENTLY pressed.
	 */
	private Set<Character> pressedKeys = new HashSet<Character>();
	/**
	 * set that registers every key pressed event. Deleted on polling.
	 */
	private Set<Character> keysDown = new HashSet<Character>();
	/**
	 * set that registers every key released event. Deleted on polling.
	 */
	private Set<Character> keysUp = new HashSet<Character>();
	/**
	 * current Music being played
	 */
	String currentMusic;
	/**
	 * music clip to be played
	 */
	Clip musicClip;
	/**
	 * SFX clip to be played
	 */
	/**
	 * volume of the music, in a scale from 0 to 100
	 */
	public int musicVolume=60;
	/**
	 * volume of the sfx, in a scale from 0 to 100
	 */
	public int sfxVolume=60;
	Clip sfxClip;
	/**
	 * audio player
	 */
	AudioInputStream audioInputStream;

	/**
	 * list of observers for observer pattern
	 */
	private ArrayList<Observer> observers;

	public Window(Field f) {
		observers = new ArrayList<Observer>();
		field = f;
		initField();

		setExtendedState(JFrame.MAXIMIZED_BOTH);

	}
	
	public Window(Field f, int width, int height) {
		observers = new ArrayList<Observer>();
		field = f;
		
		setSize(width, height);
		initField();


	}
	
	/**
	 * subscribes an observer to the observer list
	 * @param o observer to subscribe
	 */
	public void subscribe(Observer o) {
		observers.add(o);
		System.out.println(observers);
	}
	
	
	/**
	 * Changes the field of an already opened window
	 * @param f  the new field
	 */
	public void changeField(Field f) {
		this.remove(this.field);
		this.field=f;
		initField();
	}


	/**
	 * Initializes the window on PixelField mode
	 */
	private void initField() {
		if(field.verbose)this.verbose=true;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//setLocationRelativeTo(null);
		
		setResizable(true);
		add(field);

		if (actLabels) {
			for (String s : labels) {
				JLabel etiq = new JLabel(s);
				labelpanel.add(etiq);
			}
		}
		add(labelpanel, BorderLayout.LINE_END);
		labelpanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		setVisible(true);
		field.addComponentListener(field.cl);

		field.setWindow(this);
		addKeyListener(ka);
		
		this.validate();
		this.repaint();

	}

	/**
	 * renews the window in case the label changes.
	 */
	private void renew() {
		labelpanel.removeAll();
		if (actLabels) {
			for (String s : labels) {
				JLabel etiq = new JLabel(s);
				labelpanel.add(etiq);
			}

		}

		labelpanel.repaint();
		labelpanel.revalidate();
		// every cell in board is reset as a side effect.

	}

	/**
	 * Change Size of the window
	 * 
	 * @param x: Width
	 * @param y: Height
	 */
	public void changeSize(int x, int y) {
		setSize(x, y);
	}
	
	/**
	 * Shows an input popup
	 * @param text Content of the input popup
	 * @return Answer the user wrote on the input field
	 */
	public String showInputPopup(String text) {
		 return JOptionPane.showInputDialog(null,
                 text, null);
		 
	}
	
	/**
	 * Shows a message alert
	 * @param text Content of the message popup
	 */
	public void showPopup(String text) {
		 JOptionPane.showMessageDialog(null, text);
	}



	/**
	 * Stops all music
	 */
	public void stopMusic() {

		try {
			musicClip.stop();
			musicClip.close();
			if(verbose)System.out.println("music stopped");

		} catch (Exception e) {
			System.err.println("Error trying to play sound ");
		}
	}

	/**
	 * resets AudioStream so songs can be changed
	 */
	private void resetAudioStream(String filePath)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		musicClip = AudioSystem.getClip();
		musicClip.open(audioInputStream);
	}

	/**
	 * Plays a music song continously. Stops the current music if there was one. Does nothing if the music to be played is the same as the one being currently played
	 * 
	 * @param Path to a compatible files. .wav and .au are compatible files. You can
	 *             convert audio to wav here
	 *             https://audio.online-convert.com/convert-to-wav
	 */
	public void playMusic(String filePath) {

		if (!filePath.equals(this.currentMusic)) {
			if(verbose)System.out.println("playing music " + filePath);
			this.currentMusic=filePath;
			try {
				if (musicClip != null && musicClip.isOpen())
					stopMusic();
				resetAudioStream(filePath);

				musicClip.loop(Clip.LOOP_CONTINUOUSLY);
				musicClip.setFramePosition(0);
				
				FloatControl control = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
		        float range = control.getMinimum();
		        float result = range * (1 - musicVolume / 100.0f);
		        control.setValue(result);
		        
		        
				musicClip.start();
			} catch (Exception e) {
				System.err.println("Error trying to play sound " + filePath);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Plays a music song just once. Stops the current music if there was one
	 * 
	 * @param Path to a compatible files. .wav and .au are compatible files. .ogg
	 *             perhaps
	 */
	public void playMusicOnce(String filePath) {

		if (!filePath.equals(this.currentMusic)) {
			if(verbose)System.out.println("playing music " + filePath);
			this.currentMusic = filePath;
			try {
				if (musicClip != null && musicClip.isOpen())
					stopMusic();
				resetAudioStream(filePath);

				musicClip.loop(0);
				musicClip.setFramePosition(0);
				
				
				FloatControl control = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
		        float range = control.getMinimum();
		        float result = range * (1 - musicVolume / 100.0f);
		        control.setValue(result);
		        
		        
				musicClip.start();
			} catch (Exception e) {
				System.err.println("Error trying to play sound " + filePath);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Plays a sfx sound once. SFX goes in a different channel than music, so it can
	 * be played at the same time as music. Several SFX can sound at once
	 * 
	 * @param Path to a compatible files. .wav and .au are compatible files. .ogg
	 *             perhaps
	 */
	public void playSFX(String filePath) {

		if(verbose)System.out.println("playing sfx" + filePath);
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

			// create clip reference
			sfxClip = AudioSystem.getClip();
			
		
			// open audioInputStream to the clip
			sfxClip.open(audioInputStream);
			// will not loop
			sfxClip.loop(0);
			
			
			FloatControl control = (FloatControl) sfxClip.getControl(FloatControl.Type.MASTER_GAIN);
	        float range = control.getMinimum();
	        float result = range * (1 - sfxVolume / 100.0f);
	        control.setValue(result);


		} catch (Exception e) {
			System.err.println("Error trying to play sound " + filePath);
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new, custom font, from a ttf or otf file. This font can be used on both
	 * Board and Field
	 * 
	 * @param path Path to a ttf or otf file
	 * @param size font size
	 * @return a Font object with the font properties.
	 */
	public static Font customFont(String path, int size) {
		try {
			float f = size;
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(path)));
			return customFont;

		} catch (IOException | FontFormatException e) {
			return null;
		}
	}

	/**
	 * Closes the window. Ends execution of the window options including music
	 */
	public void close() {
		setVisible(false); //you can't see me!
		dispose();
		System.out.println("Closing...");
	}

	public boolean isActLabels() {
		return actLabels;
	}

	/**
	 * activates labels. Labels will appear as text to the right of the screen. Use
	 * it for info or debugging
	 */
	public void setActLabels(boolean actetiquetes) {
		this.actLabels = actetiquetes;
		if (!actetiquetes)
			renew();
	}

	public boolean isDebugLabel() {
		return debugLabel;
	}

	/**
	 * activates debug label. A label with keyboard and mouse input info
	 */
	public void setDebugLabel(boolean etiquetadebug) {
		this.debugLabel = etiquetadebug;
		renew();
	}

	/**
	 * gets all the labels as a String array
	 */
	public String[] getLabels() {
		return labels;
	}

	/**
	 * sets the labels. Each String on the array will be one line. Lines will be
	 * automatically adjusted to height.
	 */
	public void setLabels(String[] etiquetes) {
		this.labels = etiquetes;
		renew();
	}

	/**
	 * Returns the last key pressed, even if it's not being pressed currently. Used
	 * for Pac-man style games (direction is kept until changed)
	 */
	public char getKeyPressed() {
		return keyPressed;
	}

	/**
	 * Returns the current key pressed. Works correctly only if there is only one
	 * key pressed. For multiple keys use getPressedKeys
	 * 
	 * @return The current pressed key. The last pressed if there are multiple. 0
	 *         (null character, not '0') if there is none
	 * @deprecated
	 */
	public char getCurrentKeyPressed() {
		if (pressedKeys.toArray().length == 0) {
			return 0;
		} else
			return (char) pressedKeys.toArray()[0];
	}

	/**
	 * Returns a set of Character with all the currently pressed chars. Check
	 * whether your character is included with contains
	 */
	public Set<Character> getPressedKeys() {
		return pressedKeys;
	}
	
	/**
	 * Returns a set of Character with all the events of a pressed key. Check
	 * whether your character is included with contains. The key will only be registered once, Even if it keeps getting pressed 
	 */
	public Set<Character> getKeysDown() {
		HashSet<Character> tmp = new HashSet<>();
		tmp.addAll(keysDown);
		return tmp;
	}
	
	/**
	 * Returns a set of Character with all the events of a released key. Check
	 * whether your character is included with contains. The key will only be registered once, Even if it keeps getting pressed 
	 */
	public Set<Character> getKeysUp() {
		HashSet<Character> tmp = new HashSet<>();
		tmp.addAll(keysUp);
		return tmp;
	}

	/**
	 * Keyboard integration. Detects pressed keys and returns them as a set.
	 */
	private KeyAdapter ka = new KeyAdapter() {
		
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			for(Observer o : observers) {
				o.keyPressed(e);
			}
			if(pressedKeys.add(e.getKeyChar())){
				keysDown.add(e.getKeyChar());
			}
			keysUp.remove(e.getKeyChar());
			char char1 = e.getKeyChar();
			keyPressed = char1;
			currentKeyPressed = char1;
			if (debugLabel)
				renew();
		}

		@Override
		public synchronized void keyReleased(KeyEvent e) {
			for(Observer o : observers) {
				o.keyReleased(e);
			}
			pressedKeys.remove(e.getKeyChar());
			keysDown.remove(e.getKeyChar());
			keysUp.add(e.getKeyChar());
			currentKeyPressed = '0';
			if (debugLabel)
				renew();
		}
		
		

	};

	/**
	 * called on window every time its field updates
	 */
	public void windowUpdate() {
		// TODO Auto-generated method stub
		this.keysDown.clear();
		this.keysUp.clear();
	}

}
