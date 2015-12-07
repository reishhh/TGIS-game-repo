import javax.swing.JLabel;
import java.awt.Rectangle;

class Tile extends JLabel{
	String type;
	int xVal,yVal;

	/**
	 * Constructor
	 * @param bounds
	 *
	 */
	public Tile(Rectangle bounds){
		this.setBounds(bounds);
	}

	public Tile(){

	}

	public void setType(String type){
		this.type = type;
	}
}
