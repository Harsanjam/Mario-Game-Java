import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;


public class HUD {

	private float health = 100;
	private float greenValue = 255;
	
	public void tick() {
		greenValue = health*2;
		if (health  <= 0 ) {
			health = 0;
		}
		
	}
	
	public void render (Graphics g) {
		g.setColor(Color.red);
		g.fillRect(7,1001,200,30);
		g.setColor(new Color(90,(int)greenValue,0));
		g.fillRect(7,1001, (int) health*2, 30);
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
}
