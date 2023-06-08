import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
public class Launcher {

	public static void main(String[] args) {
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Mario1.0";
		cfg.width = 1000;
		cfg.height = 505;
		cfg.resizable = false;
		
		new LwjglApplication(new Application(), cfg);
	}

}
