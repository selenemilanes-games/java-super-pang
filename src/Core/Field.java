package Core;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.script.ScriptContext;
import javax.swing.*;


/**
 * Graphical User Interface that converts the provided Sprites to a graphical
 * representation as a Pixel Field. This class also provides methods to
 * manipulate that board and set how the graphics are displayed.
 * 
 * @author Marc Albareda
 * @version 0.4
 *
 */
public class Field extends JPanel {

	/**
	 * Activates verbose mode
	 */
	boolean verbose = false;
	/**
	 * List of Sprites.
	 */
	ArrayList<Sprite> sprites = new ArrayList<>();
	
	ArrayList<Sprite> newSprites = new ArrayList<>();
	/**
	 * flag set on a draw.
	 */
	private boolean recentDraw = false;
	/**
	 * Path to Background. Null for no background.
	 */
	public String background = null;

	/**
	 * Checks whether the objects out of scrolls should continue moving Caution:
	 * enabling this will destroy the performance
	 */
	public boolean moveOutOfScroll = false;
	/**
	 * Checks whether the objects out of scrolls should continue updating. False will improve performacne
	 */
	public boolean updateOutOfScroll = false;
	
	/**
	 * Horizontal scroll applied
	 */
	private int scrollx = 0;
	/**
	 * Vertical scroll applied
	 */
	private int scrolly = 0;
	/**
	 * Checks whether the scroll is locked to an object
	 */
	public boolean lockScroll = false;
	/**
	 * Checks whether the scroll is locked to an object on the Y axis
	 */
	public boolean lockScrollY = false;
	/**
	 * Checks whether the scroll is locked to an object on the X axis
	 */
	public boolean lockScrollX = false;
	/**
	 * Object to which the scroll is locked
	 */
	private Sprite scrollSprite;
	/**
	 * X of last mouse click
	 */
	private int mouseX = -1;
	/**
	 * y of last mouse click
	 */
	private int mouseY = -1;
	/**
	 * X of last mouse click. Will reset after each check.
	 */
	private int currentMouseX = -1;
	/**
	 * Y of last mouse click. Will reset after each check.
	 */
	private int currentMouseY = -1;
	/**
	 * X of last mouse click
	 */
	private int rmouseX = -1;
	/**
	 * Y of last mouse click
	 */
	private int rmouseY = -1;
	/**
	 * X of last mouse click. Will reset after each check.
	 */
	private int rcurrentMouseX = -1;
	/**
	 * Y of last mouse click. Will reset after each check.
	 */
	private int rcurrentMouseY = -1;
	/**
	 * Rotation of the mouseWheel Up is -1 and Down is 1
	 */
	private int mouseWheelRotation = 0;
	/**
	 * Amount scrolled with the mouseWheel
	 */
	private int mouseWheelScroll = 0;

	/**
	 * Width of the field
	 */
	private int fieldWidth = 1;
	/**
	 * Height of the field
	 */
	private int fieldHeight = 2;
	/**
	 * determines whether the field should be autoresized when the window size is
	 * changed
	 */
	private boolean autoresize = false;

	public Window w = null;

	/**
	 * list of observers for observer pattern
	 */
	private ArrayList<Observer> observers;
	
	/**
	 * subscribes an observer to the observer list
	 * @param o observer to subscribe
	 */
	public void subscribe(Observer o) {
		observers.add(o);
		System.out.println(observers);
	}
	
	/**
	 * Event handler every time mouse is clicked.
	 */
	private MouseMotionListener mml = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {
			//System.out.println("drag?");
			for(Observer o : observers) {
				o.mouseDragged(e);
			}
		};
		public void mouseMoved(MouseEvent e) {
			for(Observer o : observers) {
				o.mouseMoved(e);
			}
		}
	};
	private MouseListener ml = new MouseAdapter() {

		public void mouseReleased(MouseEvent e) {
			for(Observer o : observers) {
				o.mouseReleased(e);
			}
		};
		
		public void mouseDragged(MouseEvent e) {
			System.out.println("drag?");
			for(Observer o : observers) {
				o.mouseDragged(e);
			}
		};
		
		public void mousePressed(MouseEvent e) {
			for(Observer o : observers) {
				o.mousePressed(e);
			}
			int button = e.getButton();
			Point p = e.getPoint();
			if (button == 1) {
				if(verbose)System.out.println("button 1 pressed on "+p.x+", "+p.y);
				mouseX = p.x;
				mouseY = p.y;
				currentMouseX = p.x;
				currentMouseY = p.y;
			} else if (button == 3) {
				if(verbose)System.out.println("button 3 pressed on "+p.x+", "+p.y);
				rmouseX = p.x;
				rmouseY = p.y;
				rcurrentMouseX = p.x;
				rcurrentMouseY = p.y;
			}

		}

	};
	
	public void verbose() {
		this.verbose=true;
		this.w.verbose=true;
		for (Sprite sprite : sprites) {
			sprite.verbose=true;
		}
	}

	private MouseWheelListener mwl = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			for(Observer o : observers) {
				o.mouseWheelMoved(e);
			}
			mouseWheelRotation = e.getWheelRotation();
			mouseWheelScroll = e.getScrollAmount();

			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				// System.out.println("MouseWheelEvent.WHEEL_UNIT_SCROLL");
			}

			if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
				// System.out.println("MouseWheelEvent.WHEEL_BLOCK_SCROLL");
			}
		}
	};
	/**
	 * Redraws field if window size is changed.
	 */
	public ComponentListener cl = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			if (autoresize) {
				resize();
				repaint();
			}

		}

	};

	/**
	 * sets a Window to this field. It is supposed to be the window that contains
	 * this field
	 * 
	 * @param w
	 */
	public void setWindow(Window w) {
		this.w = w;
	}

	public void resize() {
		double widthQ = (double) this.getWidth() / (double) this.fieldWidth;
		double heightQ = (double) this.getHeight() / (double) this.fieldHeight;
//		System.out.println(this.getWidth() + " " + this.fieldWidth);
//		System.out.println(widthQ + " " + heightQ);
		for (Sprite s : sprites) {
			s.x1 *= widthQ;
			s.y1 *= heightQ;
			s.x2 *= widthQ;
			s.y2 *= heightQ;
		}

		this.fieldWidth = this.getWidth();
		this.fieldHeight = this.getHeight();
	}

	public Field() {
		// setFocusable(true);
		observers = new ArrayList<Observer>();
		addMouseListener(ml);
		addMouseWheelListener(mwl);
		addMouseMotionListener(mml);
	}
	
	
	/**
	 * Returns whether the Sprite is contained in the field.
	 * @param s
	 */
	public boolean containsSprite(Sprite s) {
		return (newSprites.contains(s)||sprites.contains(s));
	}
	/**
	 * Manually adds a new Sprite to the Sprite list. Used to "undelete sprites"
	 * @param s
	 */
	public void addSprite(Sprite s) {
		newSprites.add(s);
	}
	
	/**
	 * Manually removes a Sprite. Used when changing Field or when duplicating sprites.
	 * @param c
	 */
	public void removeSprite(Sprite c) {
		// TODO Auto-generated method stub
		newSprites.remove(c);
		sprites.remove(c);
	}

	/**
	 * Clears the sprite list
	 */
	public void clear() {
		sprites.clear();
	}
	
	/**
	 * Clears only the sprites in the given layer
	 */
	public void clearInLayer(int layer) {
		for (Sprite sprite : sprites) {
			if(sprite.orderInLayer==layer)sprite.delete();
		}
		for (Sprite sprite : newSprites) {
			if(sprite.orderInLayer==layer)sprite.delete();
		}
	}


	public void draw() {

		for (Iterator iterator = sprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			if(updateOutOfScroll || (sprite.x1 < -this.scrollx + w.getWidth() && sprite.x2 > -this.scrollx)) {
				sprite.update();
			}
			
			if(sprite.delete) iterator.remove();
		}
		//esborrem el getkeysdown i getkeysup de la window
		w.windowUpdate();
		if(newSprites.size()>0) {
			sprites.addAll(newSprites);
		}
		
		newSprites.clear();
		Collections.sort(sprites);
		repaint();
		for (Iterator iterator = sprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			if (sprite instanceof PhysicBody && !sprite.delete) {
				if(updateOutOfScroll || (sprite.x1 < -this.scrollx + w.getWidth() && sprite.x2 > -this.scrollx)) {
					((PhysicBody) sprite).applyPhysics();
				}
			}
		}
	}
	
	

	/**
	 * Draws the provided sprite list, and nothing else
	 */
	public void draw(ArrayList<Sprite> ssprites) {
		
		
		for (Iterator iterator = ssprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			sprite.update();
            if (sprite.delete) {
                iterator.remove();
                Image img = new ImageIcon((sprite.path)).getImage();
                img.flush();
            }
		}
		sprites.clear();
		newSprites.addAll(ssprites);
		draw();
		
	}

	/**
	 * Scrolls the whole field by the specified ammount
	 * 
	 * @param x The ammount of pixels it should be horizontally scrolled (negative
	 *          left, positive right)
	 * @param y The ammount of pixels it should be vertically scrolled (negative up,
	 *          positive down)
	 */
	public void scroll(int x, int y) {
		scrollx += x;
		scrolly += y;
		if(verbose)System.out.println("current scroll "+scrollx+", "+scrolly);
	}

	/**
	 * Resets the scroll, being returned at 0,0
	 */
	public void resetScroll() {
		scrollx = 0;
		scrolly = 0;
	}

	/**
	 * locks the scroll to a sprite
	 * 
	 * @param s
	 */
	public void lockScroll(Sprite s, Window w) {
		lockScroll(s, w, true, true);
	}

	/**
	 * locks the scroll to a sprite on the selected axis (X or Y)
	 * 
	 * @param s Sprite on which the scroll will be locked
	 * @param w Window that will be locked
	 * @param x whether the X axis will be locked or not
	 * @param y whether the Y axis will be locked or not
	 */
	public void lockScroll(Sprite s, Window w, boolean x, boolean y) {
		this.scrollSprite = s;
		this.lockScroll = false;
		if (x)
			this.lockScrollX = true;
		if (y)
			this.lockScrollY = true;
	}

	/**
	 * Throws a linecast from the origin point to the destination point. 
	 * The Linecast draws a line from those two points and returns the first sprite in the path of that line
	 * @param xO  x coordinate of the Origin point
	 * @param yO  y coordinate of the Origin point
	 * @param xD  x coordinate of the Destination point. Also xD
	 * @param yD  y coordinate of the Destination point. 
	 * @param ignoredLayers HashSet containing the integers of every layer that should be ignored
	 * @return the first sprite that the ray collides with. Null if it reaches the destination point without colliding with anyone
	 */
	public Sprite lineCast(int xO, int yO, int xD, int yD, HashSet<Integer> ignoredLayers) {
        //TODO versio cutre�
        while(xO!=xD || yO!=yD) {
            int distX = Math.abs(xD-xO);
            int distY = Math.abs(yD-yO);
            if(distX>distY && xD>xO) {
                xO++;

            }else if(distX>distY && xO>xD) {
                xO--;
            }else if(distY>=distX && yD>yO) {
                yO++;
            }else if(distY>=distX && yD<yO) {
                yO--;
            }
            for(Sprite s : sprites) {
                if(!ignoredLayers.contains(s.layer)&&s.collidesWithPoint(xO, yO)) {
                    return s;
                }
            }
        }
        return null;

    }
	
	/**
	 * Throws a linecast from center of the sprite to the destination point. 
	 * The Linecast draws a line from those two points and returns the first sprite in the path of that line, 
	 * excluding the ones on the layer of the sprite that has thrown it
	 * @param origin Sprite from where the line is cast
	 * @param xD  x coordinate of the Destination point. Also xD
	 * @param yD  y coordinate of the Destination point. 
	 * @return the first sprite (excluding the ones in the same layer as the origin sprite) that the ray collides with. 
	 * Null if it reaches the destination point without colliding with anyone
	 */
	public Sprite lineCast(Sprite origin, int xD, int yD) {
        //TODO versio cutre�
		int xO = (int) ((origin.x1+origin.x2)/2);
		int yO = (int) ((origin.y1+origin.y2)/2);
		HashSet<Integer> il = new HashSet<>();
		il.add(origin.layer);
		return lineCast(xO, yO, xD, yD, il);

    }
	
	
	/**
	 * swaps colors
	 * 
	 * @param img
	 * @param an  array of colors. Even numbers would be the ones being replaced by
	 *            the inmediately successive numbers
	 * @return
	 */
	public BufferedImage swapColors(BufferedImage img, Color... mapping) {
		int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < mapping.length / 2; i++) {
			map.put(mapping[2 * i].getRGB(), mapping[2 * i + 1].getRGB());
		}

		for (int i = 0; i < pixels.length; i++) {
			if (map.containsKey(pixels[i]))
				pixels[i] = map.get(pixels[i]);
		}

		img.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		return img;
	}

	public BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	@Override
	public void paintComponent(Graphics g) {
		
		ArrayList<Sprite> currentSprites = new ArrayList<Sprite>(sprites);
		
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		if (background != null) {
			Image img = null;
			try {
				img = new ImageIcon(background).getImage();
			} catch (Exception e) {
				System.out.println("Error on background img");
			}
			g2d.drawImage(img, (int) 0, (int) 0, (int) (getWidth()), (int) (getHeight()), 0, 0, img.getWidth(this),
					img.getHeight(this), this);

		}
		// System.out.println("painting "+sprites);
		for (Iterator iterator = currentSprites.iterator(); iterator.hasNext();) {


		
			try {

				Sprite sprite = (Sprite) iterator.next();
				if (!sprite.delete) {
					if (w == null || sprite.unscrollable
							//TODO EN ALGUNS ORDINADORS SEMBVLA QUE EL GETWIDTH RETORNA VALORS INFERIORS ALS ESPERATS
							|| (sprite.x1 < -this.scrollx + w.getWidth() && sprite.x2 > -this.scrollx)
							|| moveOutOfScroll) {
						drawSprite(sprite, g2d);
					}
				}else {
					
				}

			} catch (IndexOutOfBoundsException e) {
				// e.printStackTrace();
				System.out.println("CONCURRENCY ERROR. DID YOU FORGET TO USE A TIMER OR A THREAD.SLEEP?");
			} catch (NullPointerException e) {
				// e.printStackTrace();
				System.out.println("CONCURRENCY ERROR. DID YOU FORGET TO USE A TIMER OR A THREAD.SLEEP?");
			} catch (Exception e) {
				System.out.println(sprites.size()+ " " + newSprites.size());
				System.out.println("Excepcio no controlada en Field. Avisa al Marc pls.");
				e.printStackTrace();
			}
		}
		Toolkit.getDefaultToolkit().sync();
	}

	private void drawSprite(Sprite sprite, Graphics2D g2d) {

		float x, y; 
		int width, height;

		if (sprite == null) {
			System.err.println("Sprite null. Has intentat dibuixar un null? Vols fer explotar el meu motor?");
		} else if (sprite.visible){

			if (!sprite.collisionBox) {
				x = sprite.x1;
				y = sprite.y1;
				width = (int) (sprite.x2 - sprite.x1);
				height = (int) (sprite.y2 - sprite.y1);
			} else {
				x = sprite.x1 - sprite.drawingBoxExtraLeft;
				y = sprite.y1 - sprite.drawingBoxExtraUp;
				width = (int) (sprite.x2 + sprite.drawingBoxExtraRight - x);
				height = (int) (sprite.y2 + sprite.drawingBoxExtraDown - y);
			}
			
			if(sprite.flippedX) {
				x = x+width;
				width = width*-1;
			}
			if(sprite.flippedY) {
				y = y+height;
				height = height*-1;
			}

			try {
				if (sprite.text) {
					// Color inside = new Color(colorlletres[value]);
					// g2d.setPaint(inside);
					// int padding = 5;
					Color inside = new Color(sprite.textColor);
					g2d.setPaint(inside);
					g2d.setFont(sprite.font);
					if (!sprite.unscrollable) {
						x += scrollx;
						y += scrolly;
					}
					AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(sprite.angle), x + width / 2,
							y + height / 2);
					g2d.setTransform(a);
					g2d.drawString(sprite.path, sprite.x1, sprite.y2);
					// On drawString the starting point is not the upper left, but the bottom left
				} else {

					Image img;
					if (sprite.useImgArray) {
						img = new ImageIcon((sprite.imgArray[sprite.getCurrentImg()])).getImage();
					} else {
						//java.net.URL imgURL = getClass().getResource(sprite.path);
						img = sprite.img;

						//img = new ImageIcon((sprite.path)).getImage();
					}
					if (this.lockScroll) {
						scrollx = (int) (-this.scrollSprite.x1 + this.getWidth() / 2);
						scrolly = (int) (-this.scrollSprite.y1 + this.getHeight() / 2);
					} else {
						if (this.lockScrollX) {
							scrollx = (int) (-this.scrollSprite.x1 + this.getWidth() / 2);
						}
						if (this.lockScrollY) {
							scrolly = (int) (-this.scrollSprite.y1 + this.getHeight() / 2);
						}
					}
					if (!sprite.unscrollable) {
						x += scrollx;
						y += scrolly;
					}
					AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(sprite.angle), x + width / 2,
							y + height / 2);
					g2d.setTransform(a);

					if (sprite.colorSprite) {
						g2d.setColor(sprite.color);
						g2d.drawRect((int)x, (int)y, width, height);
						g2d.setColor(sprite.color);
						g2d.fillRect((int)x, (int)y, width, height);
					} else {
						if (sprite.palleteSwap) {
							BufferedImage bimg = this.toBufferedImage(img);
							bimg = swapColors(bimg, sprite.swapList.toArray(new Color[sprite.swapList.size()]));
							img = bimg;
						}
						//System.out.println(img);

						g2d.drawImage(img, (int)x, (int)y, width, height, this);
					}

				}

			} catch (Exception e) {
				System.out.println("Error on image " + sprite.path + " object: " + sprite.name);
				e.printStackTrace();
			}
		}
	}

	/**
	 * get the X of the pixel last clicked.
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * get the Y of the pixel last clicked.
	 */
	public int getMouseY() {
		return mouseY;
	}
	
	
	public ArrayList<Sprite> getMouseSprite() {
		ArrayList<Sprite> ret = new ArrayList<Sprite>();
		for (Iterator iterator = sprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			
			if(sprite.collidesWithPoint(mouseX, mouseY)) {
				ret.add(sprite);
			}
			
		}
		return ret;
	}

	/**
	 * get the X of the pixel last clicked. Returns -1 if no pixel was clicked since
	 * last check.
	 */
	public int getCurrentMouseX() {
		int tmp = currentMouseX;
		currentMouseX = -1;
		return tmp;
	}

	/**
	 * get the Y of the pixel last clicked. Returns -1 if no pixel was clicked since
	 * last check.
	 */
	public int getCurrentMouseY() {
		int tmp = currentMouseY;
		currentMouseY = -1;
		return tmp;
	}
	
	
	public ArrayList<Sprite> getCurrentMouseSprite() {
		ArrayList<Sprite> ret = new ArrayList<Sprite>();
		int gcmx = getCurrentMouseX();
		int gcmy = getCurrentMouseY();
		for (Iterator iterator = sprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			
			if(sprite.collidesWithPoint(gcmx, gcmy)) {
				ret.add(sprite);
			}
			
		}
		return ret;
	}

	/**
	 * get the X of the pixel last clicked.
	 */
	public int getRightMouseX() {
		return rmouseX;
	}

	/**
	 * get the Y of the pixel last clicked.
	 */
	public int getRightMouseY() {
		return rmouseY;
	}

	/**
	 * get the X of the pixel last clicked. Returns -1 if no pixel was clicked since
	 * last check.
	 */
	public int getCurrentRightMouseX() {
		int tmp = rcurrentMouseX;
		rcurrentMouseX = -1;
		return tmp;
	}

	/**
	 * get the Y of the pixel last clicked. Returns -1 if no pixel was clicked since
	 * last check.
	 */
	public int getCurrentRightMouseY() {
		int tmp = rcurrentMouseY;
		rcurrentMouseY = -1;
		return tmp;
	}
	
	public ArrayList<Sprite> getRightMouseSprite() {
		ArrayList<Sprite> ret = new ArrayList<Sprite>();
		for (Iterator iterator = sprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			
			if(sprite.collidesWithPoint(rmouseX, rmouseY)) {
				ret.add(sprite);
			}
			
		}
		return ret;
	}

	/**
	 * get the X of the pixel which the mouse is currently hovering.
	 */
	public int getMouseOverX() {
		if (this.getMousePosition() != null)
			return this.getMousePosition().x;
		else
			return -1;
	}

	/**
	 * get the Y of the pixel which the mouse is currently hovering.
	 */
	public int getMouseOverY() {
		if (this.getMousePosition() != null)
			return this.getMousePosition().y;
		else
			return -1;
	}
	
	public ArrayList<Sprite> getMouseOverSprite() {
		ArrayList<Sprite> ret = new ArrayList<Sprite>();
		for (Iterator iterator = sprites.iterator(); iterator.hasNext();) {
			Sprite sprite = (Sprite) iterator.next();
			
			if(sprite.collidesWithPoint(this.getMouseOverX(), this.getMouseOverY())) {
				ret.add(sprite);
			}
			
		}
		return ret;
	}

	public int getScrollx() {
		return scrollx;
	}

	public void setScrollx(int scrollx) {
		this.scrollx = scrollx;
	}

	public int getScrolly() {
		return scrolly;
	}

	public void setScrolly(int scrolly) {
		this.scrolly = scrolly;
	}

	public int getMouseWheelRotation() {
		return mouseWheelRotation;
	}

	public int getMouseWheelScroll() {
		return mouseWheelScroll;
	}



}
