package asandersa.gdx;

import asandersa.gdx.emitter.Emitter;
import asandersa.gdx.emitter.Particle;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Наследуемся от ApplicationAdapter - базовая обертка для любой gdx-игры.
 * ApplicationAdapter позволяет запустить приложение и предоставляет нам 3 базовых метода:
 * create () - запускается вначале и инициализирует всю инфраструктуру,
 * render () - вызывается на каждый изображаемый кадр: начало рендера, рендер, окончание,
 * dispose () - вызывается в завершении, для освобождения ресурсов.
 */
public class Starter extends ApplicationAdapter {
	SpriteBatch batch;

	private String meId;
	private ObjectMap<String,Ship> ships = new ObjectMap<>();

	private List<Ship> enemies = new ArrayList<>();

	private final KeyboardAdapter inputProcessor;
	private MessageSender messageSender;

	private Texture bulletTexture;

	public Starter(InputState inputState) {
		this.inputProcessor = new KeyboardAdapter(inputState);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(inputProcessor); //регистрируем адаптер
		batch = new SpriteBatch();

		Ship me = new Ship(100, 100);
		ships.put(meId, me);
		bulletTexture = new Texture("bullet.png");


	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		ScreenUtils.clear(0, 0, 0, 0);
		batch.begin();
		for (String key : ships.keys()) {
			Ship ship = ships.get(key);
			InputState inputState = inputProcessor.getInputState();
			Emitter emitter = ship.emitter;
			emitter.setAngle(inputState.getAngle());
			emitter.getPosition().set(ship.getOrigin());
			if (inputState.isFirePressed()) {
				emitter.start(delta);
			}
			emitter.act(delta);
			for (Particle particle : emitter.getParticles()) {
				Vector2 position = particle.getPosition();
				batch.draw(bulletTexture, position.x - 8, position.y - 8);
			}
			ship.render(batch);

		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (Ship value : ships.values()) {
			value.dispose();
		}
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void handleTimer() {
		if(inputProcessor != null && !ships.isEmpty()) {
			Ship me = ships.get(meId);
			InputState playerState = inputProcessor.updateAndGetInputState(me.getOrigin());
			messageSender.sendMessage(playerState);
		}
	}

    public void setMeId(String meId) {
		this.meId = meId;
    }

	public void evict(String idToEvict) {
		ships.remove(idToEvict);
	}

	public void updateShip(String id, float x, float y, float angle) {
		if (ships.isEmpty()) {
			return;
		}

		Ship ship = ships.get(id);
		if (ship == null) {
			ship = new Ship(x, y,"shipEnemy.png");
			ships.put(id, ship);
		} else {
			ship.moveTo(x, y);
		}

		ship.rotateTo(angle);


	}
}
