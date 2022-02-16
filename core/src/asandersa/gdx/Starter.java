package asandersa.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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

	private Ship me;
	private List<Ship> enemies = new ArrayList<>();

	private final KeyboardAdapter inputProcessor;
	private MessageSender messageSender;
	private InputState inputState;

	public Starter(InputState inputState) {
		this.inputProcessor = new KeyboardAdapter(inputState);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(inputProcessor); //регистрируем адаптер
		batch = new SpriteBatch();

		me = new Ship(100, 100);

		List<Ship> newEnemies = IntStream.range(0, 5)
				.mapToObj(i -> {
					int x = MathUtils.random(Gdx.graphics.getWidth());
					int y = MathUtils.random(Gdx.graphics.getHeight());

					return new Ship(x, y, "shipEnemy.png");
				})
				.collect(Collectors.toList());
		enemies.addAll(newEnemies);
	}

	@Override
	public void render () {
		me.moveTo(inputProcessor.getDirection());
		me.rotateTo(inputProcessor.getMousePos());

		ScreenUtils.clear(0, 0, 0, 0);
		batch.begin();
		me.render(batch);
		enemies.forEach(enemy -> {
			enemy.render(batch);
			enemy.rotateTo(me.getPosition());
		});

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		me.dispose();
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void handleTimer() {
		if(inputProcessor != null) {
			InputState playerState = inputProcessor.updateAndGetInputState(me.getOrigin());
			messageSender.sendMessage(playerState);
		}
	}
}
