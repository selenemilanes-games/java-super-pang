package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.ImageIcon;

/**
 * <h2>This class is the representation of a Sprite in a Pixel Field.</h2> It
 * measures as a rectangle, has collision functions. Every class in a Pixel
 * Field must extend from this class
 * 
 * @see <a href="https://gitlab.com/malbareda/GraphicBoard">Git Repository</a>
 * @version 0.8
 * @author
 *         <h1>Marc Albareda</h1> <img src=
 *         "https://yt3.ggpht.com/a-/AAuE7mAEQPhIx5GSxS4TWhnD_TltoW2pMDvvuveiOg=s288-mo-c-c0xffffffff-rj-k-no"
 *         alt="Smiley face" height="1200" width="1200">
 *
 * 
 */
public class Sprite implements Serializable, Comparable<Sprite> {

	
	transient Image img;
	/**
	 * angle passa a ser el radi del cercle TODO
	 */
	public boolean circle = false;
	
	/**
	 * Activates verbose mode
	 */
	boolean verbose = false;
	/**
	 * the name of the sprite
	 */
	public String name;

	/**
	 * The horizontal position in the field of the upper-left pixel in the sprite
	 */
	public float x1;
	/**
	 * The vertical position in the field of the upper-left pixel in the sprite
	 */
	public float y1;

	/**
	 * The horizontal position in the field of the bottom-right pixel in the sprite
	 */
	public float x2;
	/**
	 * The vertical position in the field of the bottom-right pixel in the sprite
	 */
	public float y2;

	/**
	 * The rotating angle in degrees
	 */
	public double angle;

	/**
	 * The path to the image to that Sprite
	 */
	public String path;

	/**
	 * Specifies whether this Sprite is solid. Non-solid sprites will not collide.
	 */
	public boolean solid = true;

	/**
	 * Specifies whether this sprite is unscrollable. Unscrollables won't be
	 * scrolled ever. Used for HUD or fixed objects
	 */
	public boolean unscrollable;
	/**
	 * Specifies whether this sprite is text. Text won't use an image path and will
	 * print the String specified as path.
	 */
	public boolean text;
	/**
	 * Font for the text if the Sprite is used as text
	 */
	public Font font = new Font("SansSerif", Font.PLAIN, 16);
	/**
	 * Color for the text if the Sprite is used as text
	 */
	public int textColor = 0x000000;
	/**
	 * checks if an img Array should be used instead of a single image
	 */
	public boolean useImgArray = false;
	/**
	 * position of the image on the img array that should be accessed currently
	 */
	private int currentImg = 0;
	/**
	 * array of images, needed if a single sprite has several images.
	 */
	public String[] imgArray;
	/**
	 * marks this Sprite to be deleted.
	 */

	public int drawingBoxExtraLeft;

	public int drawingBoxExtraRight;

	public int drawingBoxExtraUp;

	public int drawingBoxExtraDown;

	// TODO ENCAPSULARHO
	public boolean visible = true;

	/**
	 * checks whether this sprite has a collision box distinct to its drawing box
	 */
	public boolean collisionBox;

	/**
	 * marks this sprite to be deleted. Deleted sprites aren't drawn and they do not
	 * collide.
	 */
	public boolean delete;

	/**
	 * marks this sprite as one that is flipped. Physics works correctly tho
	 */
	public boolean flippedX;
	public boolean flippedY;

	// No tocar aquest boolean. Esta en package per alguna rao. El faig servir per
	// comprovacions internes del s Phantoms
	boolean internalDelete;

	/**
	 * marks this sprite as a sprite with a palleteSwap
	 */
	public boolean palleteSwap;
	/**
	 * List of the required swaps. The even numbers will be replaced by the
	 * inmediately after number
	 */
	public ArrayList<Color> swapList = new ArrayList<>();

	/**
	 * layer of the object. Objects can ignore certain layers and won't collide with
	 * them. Defaults to 0
	 */
	public int layer = 0;
	/**
	 * set of the ignored layers. Objects can ignore certain layers and won't
	 * collide with them
	 */
	public HashSet<Integer> ignoredLayers = new HashSet<>();

	/**
	 * determines if this sprite is a color Sprite. Color Sprites are just squares
	 * with one color
	 * 
	 */
	public boolean colorSprite = false;

	/**
	 * color of a colored sprite, in RGB-Alpha format.
	 */
	public Color color;

	protected transient Field f;

	
	/**
	 * Returns the field
	 * @return the field
	 */
	public Field getField() {
		return f;
	}

	/**
	 * Sets the field and adds this sprite to its spriteList
	 * @param f the field that should be the new Sprite Field
	 */
	public void setField(Field f) {
		this.f = f;
		f.addSprite(this);
	}

	/**
	 * Determines the order in the field. The higher the value the more in front
	 * will be. The highest value will be on the forefront and the lowest on the
	 * background.
	 */
	public Integer orderInLayer = 0;

	// Fer tots els canvis al constructor privat de Sprite. Si no m'oblidaré algun
	// dia de fer el canvi a algun dels 40000 constructors i no trobaré mai el
	// problema
	private Sprite(String name, int x1, int y1, int x2, int y2, double angle, Field f) {
		super();
		this.name = name;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.angle = angle;
		this.f = f;
		if (x2 < x1 || y2 < y1) {
			System.err.println("ERROR. SPRITE INITIALIZED WITH NEGATIVE LENGHT/HEIGHT");
		}
		this.layer = 0;
		if (f != null)
			f.newSprites.add(this);
		if (f != null && f.verbose)
			this.verbose = true;
	}

	/**
	 * Default sprite constructor, for sprites that are rotated from their original
	 * image
	 * 
	 * @param name  Name of the sprite, for identification purposes
	 * @param x1    x position (horizontal) of the upper left corner
	 * @param y1    y position (vertical) of the upper left corner
	 * @param x2    x position (horizontal) of the lower right corner
	 * @param y2    y position (vertical) of the lower right corner
	 * @param angle angle (in degrees) of rotation of the sprite
	 * @param path  path to the image of that sprite, as a relative path
	 */
	public Sprite(String name, int x1, int y1, int x2, int y2, double angle, String path, Field f) {
		this(name, x1, y1, x2, y2, angle, f);
		useImgArray = false;
		this.path = path;
		File fil = new File(path);
		if (!fil.exists()) {
			System.err.println("No hi ha cap imatge en " + path + " ignorar si és un text");
		}
		img = new ImageIcon(	path).getImage(); 
	}

	/**
	 * Default sprite constructor, for sprites with multiple images that are rotated
	 * from their original image
	 * 
	 * @param name  Name of the sprite, for identification purposes
	 * @param x1    x position (horizontal) of the upper left corner
	 * @param y1    y position (vertical) of the upper left corner
	 * @param x2    x position (horizontal) of the lower right corner
	 * @param y2    y position (vertical) of the lower right corner
	 * @param angle angle (in degrees) of rotation of the sprite
	 * @param path  path to the array of images of that sprite, as a relative path
	 */
	public Sprite(String name, int x1, int y1, int x2, int y2, double angle, String[] path, Field f) {
		this(name, x1, y1, x2, y2, angle, f);
		imgArray = path;
		useImgArray = true;
		File fil = new File(path[0]);
		if (!fil.exists()) {
			System.err.println("No hi ha cap imatge en " + path[0]);
		}
		 
	}

	/**
	 * Default sprite constructor, for sprites that are simple squares of a single
	 * color
	 * 
	 * @param name  Name of the sprite, for identification purposes
	 * @param x1    x position (horizontal) of the upper left corner
	 * @param y1    y position (vertical) of the upper left corner
	 * @param x2    x position (horizontal) of the lower right corner
	 * @param y2    y position (vertical) of the lower right corner
	 * @param angle angle (in degrees) of rotation of the sprite
	 * @param color color of that sprite.
	 */
	public Sprite(String name, int x1, int y1, int x2, int y2, double angle, Color color, Field f) {
		this(name, x1, y1, x2, y2, angle, f);
		this.colorSprite = true;
		this.color = color;
	}

	/**
	 * Default sprite constructor, for sprites that are rotated from their original
	 * image
	 * 
	 * @param name  Name of the sprite, for identification purposes
	 * @param x1    x position (horizontal) of the upper left corner
	 * @param y1    y position (vertical) of the upper left corner
	 * @param x2    x position (horizontal) of the lower right corner
	 * @param y2    y position (vertical) of the lower right corner
	 * @param angle angle (in degrees) of rotation of the sprite
	 * @param path  path to the image of that sprite, as a relative path
	 */
	Sprite(String name, int x1, int y1, int x2, int y2, double angle, String path) {
		this(name, x1, y1, x2, y2, angle, (Field) null);
		this.path = path;
		useImgArray = false;
	}

	/**
	 * changes the image
	 * 
	 * @param path of the new image
	 */
	public void changeImage(String path) {
		// TODO eventualment anira al setPath quan encapsuli tot...
		this.path = path;
		img = new ImageIcon(	path).getImage(); 

	}

	/**
	 * Swaps the two colors
	 * 
	 * @param c1 the color that will be swapped out
	 * @param c2 the color that will be swapped in
	 */
	public void palleteSwap(int c1, int c2) {
		this.palleteSwap = true;
		swapList.add(new Color(c1));
		swapList.add(new Color(c2));
	}

	/**
	 * Defines this sprite as having a drawing box different from the collision box
	 * 
	 * @param x1 extra width of the drawing box (left side)
	 * @param x2 extra width of the drawing box (right side)
	 * @param y1 extra height of the drawing box (upper side)
	 * @param y2 extra height of the drawing box (lower side)
	 */
	public void collisionBox(int x1, int x2, int y1, int y2) {
		this.drawingBoxExtraLeft = x1;
		this.drawingBoxExtraRight = x2;
		this.drawingBoxExtraUp = y1;
		this.drawingBoxExtraDown = y2;
		this.collisionBox = true;
	}

	/**
	 * removes the collision box
	 */
	public void removeCollisionBox() {
		this.collisionBox = false;
	}

	protected Shape getRect() {
		Rectangle myRect;

		myRect = new Rectangle((int) x1, (int) y1, (int) x2 - (int) x1, (int) y2 - (int) y1);

		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), (x1 + x2) / 2, (y1 + y2) / 2);
		Shape rotatedRect = at.createTransformedShape(myRect);

		return rotatedRect;
	}
	
	protected Ellipse2D getEllipse() {
		
		
		Ellipse2D ellipse = new Ellipse2D.Double(x1, y1, x2-x1, y2-y1);

		return ellipse;
	}


	protected Rectangle getNonRotatingRect() {
		Rectangle myRect;

		myRect = new Rectangle((int) x1, (int) y1, (int) x2 - (int) x1, (int) y2 - (int) y1);

		return myRect;
	}

	/**
	 * Returns the all the Sprites in the provided list that collide with this
	 * Sprite
	 * 
	 * @param others A list of Sprites or classes that extends Sprite
	 * @return A list with all the sprites in the sprites list in the List that
	 *         collides. <br>
	 *         Returns an Empty list if there is no collision with any Sprite
	 */
	public ArrayList<Sprite> collidesWithList(ArrayList<? extends Sprite> others) {
		ArrayList<Sprite> list = new ArrayList<>();
		for (Sprite s : others) {
			if (this.collidesWith(s))
				list.add(s);
		}
		return list;

	}

	/**
	 * Checks if this sprite collides with the provided sprite. It won't collide
	 * with itself, against non-solid sprites, or sprites marked as deleted
	 * 
	 * @param other The other sprite
	 */
	public boolean collidesWith(Sprite other) {

		if (!this.equals(other) && !this.ignoredLayers.contains(other.layer)
				&& !other.ignoredLayers.contains(this.layer) && !this.delete && !this.internalDelete && !other.delete
				&& !other.internalDelete && other.solid && this.solid) {
			if(this.circle && other.circle) {
				float xMid = (x1+x2)/2;
				float yMid = (y1+y2)/2;
				
				float xMidO = (other.x1+other.x2)/2;
				float yMidO = (other.y1+other.y2)/2;
				double magnitud = Math.sqrt(Math.abs(xMid-xMidO)*Math.abs(xMid-xMidO) + Math.abs(yMid-yMidO)*Math.abs(yMid-yMidO)); 
				return (magnitud <= (this.x2-this.x1)/2+(other.x2-other.x1)/2);
				
			}else if(this.circle && !other.circle) {
				return this.getEllipse().intersects(other.x1,other.y1,other.x2-other.x1,other.y2-other.y1);
			}else if(!this.circle && other.circle) {
				return other.getEllipse().intersects(this.x1,this.y1,this.x2-this.x1,this.y2-this.y1);
			}
			else if (this.angle % 180 == 0 && other.angle % 180 == 0) {
				// els dos son rectangles, es pot fer servir un rectangle intersect sense cap
				// rotacio
				return this.getNonRotatingRect().intersects(other.getNonRotatingRect());
			}
			// TODO INVESTIGAR COM ES POT RETRANSFORMAR A RECTANGLE UN RECTANGLE ROTAT AMB
			// AFFINETRANSFORM
			if (this.angle % 90 == 0 && other.angle % 180 == 0) {
				// els dos son rectangles pero un esta rotat 90 o 270 .
				return this.getRect().intersects(other.getNonRotatingRect());
			} else if (this.angle % 180 == 0 && other.angle % 90 == 0) {
				// els dos son rectangles pero un esta rotat 90 o 270 .
				return other.getRect().intersects(this.getNonRotatingRect());
			} else {
				// hi han rotacions de no %90 o els dos estan rotats %90
				Area areaA = new Area(this.getRect());
				areaA.intersect(new Area(other.getRect()));
				return (!areaA.isEmpty());
			}

		} else {
			return false;
		}

	}

	/**
	 * Checks if this sprite collides with the provided sprite. It won't collide
	 * against non-solid sprites, or sprites marked as deleted
	 * 
	 * @param other The other sprite
	 */
	public double collidesWithPercent(Sprite other) {
		Area areaStart = new Area(this.getRect());
		Area areaA = new Area(this.getRect());
		areaA.intersect(new Area(other.getRect()));
		double sizestart = areaStart.getBounds2D().getWidth() * areaStart.getBounds2D().getHeight();
		double sizeA = areaA.getBounds2D().getWidth() * areaA.getBounds2D().getHeight();
		return sizeA / sizestart * 100.0;
	}

	/**
	 * Checks if this sprite collides with the point in coordinates x,y *
	 * 
	 * @param other The other sprite
	 */
	public boolean collidesWithPoint(int x, int y) {
		return this.getRect().contains(x, y);
	}

	/**
	 * Returns the first Sprite in the provided filed that collides with this Sprite
	 * 
	 * @param fOne A Pixel Field
	 * @return The first Sprite in the sprites list in the Pixel Field that
	 *         collides. <br>
	 *         The Pixel Field is ordered by insertion in the code <br>
	 *         Returns null if there is no collision with any Sprite
	 */
	public Sprite firstCollidesWithField() {
		return this.firstCollidesWithList(f.sprites);
	}

	/**
	 * Returns the all the Sprites in the provided filed that collide with this
	 * Sprite
	 * 
	 * @param fOne A Pixel Field
	 * @return A list with all the sprites in the sprites list in the Pixel Field
	 *         that collides. <br>
	 *         The Pixel Field is ordered by insertion in the code <br>
	 *         Returns an Empty list if there is no collision with any Sprite
	 */
	public ArrayList<Sprite> collidesWithField() {
		return this.collidesWithField(this.f);
	}

	/**
	 * Returns the all the Sprites in the provided filed that collide with this
	 * Sprite
	 * 
	 * @param f A Pixel Field
	 * @return A list with all the sprites in the sprites list in the Pixel Field
	 *         that collides. <br>
	 *         The Pixel Field is ordered by insertion in the code <br>
	 *         Returns an Empty list if there is no collision with any Sprite
	 */
	public ArrayList<Sprite> collidesWithField(Field f) {
		return this.collidesWithList(f.sprites);
	}

	/**
	 * Returns the first Sprite in the provided list that collides with this Sprite
	 * 
	 * @param others A list of Sprites or classes that extends Sprite
	 * @return The first Sprite in the llist that collides <br>
	 *         Returns null if there is no collision with any Sprite
	 */
	public Sprite firstCollidesWithList(ArrayList<? extends Sprite> others) {
		for (Sprite s : others) {
			if (this.collidesWith(s))
				return s;
		}
		return null;

	}

	/**
	 * marks this sprite to be deleted. Sprites marked for deletion won't collide or
	 * be drawn.
	 */
	public void delete() {
		delete = true;
		// f.sprites.remove(this);
	}

	public void undelete() {
		if (delete == true && !this.f.containsSprite(this)) {
			delete = false;
			f.newSprites.add(this);
		}
	}

	public String toString() {
		return "Sprite " + this.name + " , from (" + this.x1 + ", " + this.y1 + ") to (" + this.x2 + ", " + this.y2
				+ ") " + " url: " + this.path;

	}

	/**
	 * each turn the update function will be called for every non-deleted sprite in
	 * a field.
	 */
	public void update() {
		// buida
	}

	/**
	 * CompareTo orders the list according to the orderInLayer. This ensures that
	 * the higher sprites are drawn last
	 */
	@Override
	public int compareTo(Sprite other) {
		return this.orderInLayer.compareTo(other.orderInLayer);
	}

	public int getCurrentImg() {
		return currentImg;
	}

	public void setCurrentImg(int currentImg) {
		this.currentImg = currentImg;
		img = new ImageIcon(imgArray[currentImg]).getImage(); 

	}

}
