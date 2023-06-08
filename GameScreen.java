import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen{

	private Application game;
	OrthographicCamera cam;
	
	public GameScreen(Application game)
	{
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 1000, 505);
	}
	
	public void dispose() {
	}

	
	public void hide() {		
	}

	
	public void pause() {		
	}

	
	public void render(float arg0) {	
		cam.update();
	}

	
	public void resize(int arg0, int arg1) {		
	}

	
	public void resume() {		
	}

	
	public void show() {		
	}
	
}
