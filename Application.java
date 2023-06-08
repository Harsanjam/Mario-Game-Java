import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

public class Application extends Game {

	private GameScreen gameScreen;

	private Stage mainMenuStage;
	private Stage gameStage;
	private Character backgroundL;
	private Character backgroundR;
	private Character mario;
	private Character groundL;
	private Character groundR;
	private Enemy goomba;
	private HUD hud;

	private float moveSpeed = 400f;
	private float boundaryCoordinate = 720f;
	private int i = 0;

	private final float gravity = -0.8f;
	private final float terminalVelocity = -50;

	private boolean play;

	private Character platform1;
	private Character platform2;
	private Character platform3;
	private Character platform4;

	private int distanceX;
	private int distanceY;

	private Character[] platform = new Character[4];

	
	private void init() {
		hud = new HUD();	
	}
	
	public void create() {

		play = true;

		gameScreen = new GameScreen(this);

		gameStage = new Stage();
		mainMenuStage = new Stage();

		backgroundR = new Character(hud);
		backgroundR.setTexture(new Texture(Gdx.files.internal("assets/Background.jpg")));
		backgroundR.setPosition(0, 0);
		gameStage.addActor(backgroundR);

		backgroundL = new Character(hud);
		backgroundL.setTexture(new Texture(Gdx.files.internal("assets/Background.jpg")));
		backgroundL.setPosition((backgroundL.getX() - backgroundR.getWidth()), 0);
		gameStage.addActor(backgroundL);

		groundR = new Character(hud);
		groundR.setTexture(new Texture(Gdx.files.internal("assets/ground.jpg")));
		groundR.setPosition(0, -80);
		gameStage.addActor(groundR);

		groundL = new Character(hud);
		groundL.setTexture(new Texture(Gdx.files.internal("assets/ground.jpg")));
		groundL.setPosition((groundL.getX() - groundR.getWidth()), -80);
		gameStage.addActor(groundL);

		mario = new Character(hud);
		mario.setTexture(new Texture(Gdx.files.internal("assets/Mario.png")));
		mario.setPosition(100, 100);
		gameStage.addActor(mario);
		
		goomba = new Enemy();
		goomba.setTexture(new Texture(Gdx.files.internal("assets/Goomba.png")));
		goomba.setPosition(200, 100);
		gameStage.addActor(goomba);

		platform1 = new Character(hud);
		platform2 = new Character(hud);
		platform3 = new Character(hud);
		platform4 = new Character(hud);

		platform1.setTexture(new Texture(Gdx.files.internal("assets/platform.jpg")));
		platform2.setTexture(new Texture(Gdx.files.internal("assets/platform.jpg")));
		platform3.setTexture(new Texture(Gdx.files.internal("assets/platform.jpg")));
		platform4.setTexture(new Texture(Gdx.files.internal("assets/platform.jpg")));
		platform[0] = platform1;
		platform[1] = platform2;
		platform[2] = platform3;
		platform[3] = platform4;
		
		for (int i = 0; i < platform.length; i++) {
			platform[i].setPosition(-2000, 120);
			gameStage.addActor(platform[i]);
		}
	}

	public void render() {
		if (play) {
			Gravity();
			EnemyCollision();
			mario.velocityX = 0;
			mario.velocityY = 0;
			Camera cam = gameStage.getCamera();

			if (mario.getX() > boundaryCoordinate && i == 0) {
				backgroundL.setPosition(backgroundL.getX() + (2 * backgroundL.getWidth()), 0);
				groundL.setPosition(groundL.getX() + (2 * groundL.getWidth()), -80);
				
				platform[0].setPosition(mario.getX() + 500, 120);
				FollowUpPlatforms();
				
				boundaryCoordinate += (backgroundL.getWidth());
				i = 1;
			}
			
			else if (mario.getX() > boundaryCoordinate && i == 1) {
				backgroundR.setPosition(backgroundR.getX() + (2 * backgroundR.getWidth()), 0);
				groundR.setPosition(groundR.getX() + (2 * groundR.getWidth()), -80);
				goomba.setPosition(mario.getX() + 500, goomba.getY());
				boundaryCoordinate += (backgroundR.getWidth());
				i = 0;
			}

			if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				mario.velocityX += moveSpeed;
				if(mario.getX() >= cam.position.x)
				cam.position.lerp(new Vector3(mario.getX(), 253, 0), 0.1f);
			}
			else if (Gdx.input.isKeyPressed(Keys.LEFT) && mario.getX() > cam.position.x - (cam.viewportWidth/2)) {
				mario.velocityX -= moveSpeed;
			}

			Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			float dt = Gdx.graphics.getDeltaTime();
			gameStage.act(dt);
			gameStage.draw();
		}
	}

	private void Gravity() {
		mario.verticalVelocity = mario.verticalVelocity + gravity;
		if (mario.verticalVelocity < terminalVelocity) {
			mario.verticalVelocity = terminalVelocity;
		}
		if (groundL.getBoundingRectangle().overlaps(mario.getBoundingRectangle())
				|| groundR.getBoundingRectangle().overlaps(mario.getBoundingRectangle())) {

			mario.verticalVelocity = 0f;
		}
		for(int i = 0; i < platform.length; i++)
		{
			if(platform[i].getBoundingRectangle().overlaps(mario.getBoundingRectangle())
					&& mario.getY() > platform[i].getY() && mario.getX() > platform[i].getX() - 35)
			{
				mario.verticalVelocity = 0f;
			}
			if(platform[i].getBoundingRectangle().overlaps(mario.getBoundingRectangle())
					&& mario.verticalVelocity > 0 && mario.getY() < platform[i].getY())
			{
				mario.verticalVelocity = -10f;
			}
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && mario.verticalVelocity == 0) {
			mario.verticalVelocity = 17;
		}
		Jump(mario);
		Jump(goomba);
	}
	
	private void FollowUpPlatforms()
	{
		platform[1].setPosition(platform[0].getX() + platform[0].getWidth() + 100, platform[0].getY());
		platform[2].setPosition(platform[0].getX() + (platform[0].getWidth()/2), platform[0].getY() + 150);
		platform[3].setPosition(platform[0].getX() + (2 * platform[0].getWidth()), platform[0].getY() + 200);
	}

	private int getRandomInt(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min or vice versa...");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	private void EnemyCollision()
	{
		if(mario.getBoundingRectangle().overlaps(goomba.getBoundingRectangle()))
		{
			mario.verticalVelocity = 17f;
			Jump(mario);
		}
	}
	
	private void Jump(Character character)
	{
		character.verticalPosition = character.verticalPosition + character.verticalVelocity;
		character.setPosition(character.getX(), character.verticalPosition);
	}
	
	private void Jump(Enemy enemy)
	{
		enemy.verticalPosition = enemy.verticalPosition + enemy.verticalVelocity;
		enemy.setPosition(enemy.getX(), enemy.verticalPosition);
	}
}
