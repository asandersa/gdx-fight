package asandersa.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class KeyboardAdapter extends InputAdapter {

    private final Vector2 mousePos = new Vector2();
    private final Vector2 direction = new Vector2(); //для экономии памяти
    private final Vector2 angle = new Vector2();

    private final InputState inputState; //инкапсуляция, легко обновлять без генерирования новых сущностей

    public KeyboardAdapter(InputState inputState) {
        this.inputState = inputState;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        inputState.setFirePressed(true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputState.setFirePressed(false);
        return false;
    }

    /**
     * При нажатии клавиши выставляем флажки true
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) inputState.setLeftPressed(true);
        if (keycode == Input.Keys.W) inputState.setUpPressed(true);
        if (keycode == Input.Keys.D) inputState.setRightPressed(true);
        if (keycode == Input.Keys.S) inputState.setDownPressed(true);
        return false;
    }

    /**
     * При отпускании клавиши выставляем флажки false
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) inputState.setLeftPressed(false);
        if (keycode == Input.Keys.W) inputState.setUpPressed(false);
        if (keycode == Input.Keys.D) inputState.setRightPressed(false);
        if (keycode == Input.Keys.S) inputState.setDownPressed(false);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePos.set(screenX, Gdx.graphics.getHeight() - screenY); //теперь координаты мыши отсчитываются от низа экрана.
        return false;
    }

    /**
     * Т.к. положение мыши при зажатой кнопке не отслеживается мы определяем
     * метод для опроса машины об изменениях положения мыши.
     * Переходим с модели подписки на модель опроса устройства.
     */
    public void updateMousePos() {
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        mousePos.set(x, y);

    }

    public Vector2 getDirection() {
        direction.set(0, 0); //перезаписывает полностью значение вектора

        if(inputState.isLeftPressed()) direction.add(-5, 0); //сложение значений вектора
        if(inputState.isUpPressed()) direction.add(0, 5);
        if(inputState.isRightPressed()) direction.add(5, 0);
        if(inputState.isDownPressed()) direction.add(0, -5);

        return direction;
    }

    public Vector2 getMousePos() {
        updateMousePos();
        return mousePos;
    }

    public InputState updateAndGetInputState(Vector2 playerOrigin) {
        updateMousePos();
        angle.set(mousePos).sub(playerOrigin);
        inputState.setAngle(angle.angleDeg());
        return inputState;
    }

    public InputState getInputState() {
        return inputState;
    }
}
