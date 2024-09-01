package Core;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface Observer {
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);
	public void mousePressed(MouseEvent e);
	public void mouseWheelMoved(MouseWheelEvent e);
	public void mouseReleased(MouseEvent e);
	public void mouseDragged(MouseEvent e);
	public void mouseMoved(MouseEvent e);
}
