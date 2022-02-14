package asandersa.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class KeyboardAdapter extends InputAdapter {
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;

    private final Vector2 mousePos = new Vector2();
    private final Vector2 direction = new Vector2(); //для экономии памяти

    /**
     * При нажатии клавиши выставляем флажки true
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) leftPressed = true;
        if (keycode == Input.Keys.W) upPressed = true;
        if (keycode == Input.Keys.D) rightPressed = true;
        if (keycode == Input.Keys.S) downPressed = true;
        return false;
    }

    /**
     * При отпускании клавиши выставляем флажки false
     */
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) leftPressed = false;
        if (keycode == Input.Keys.W) upPressed = false;
        if (keycode == Input.Keys.D) rightPressed = false;
        if (keycode == Input.Keys.S) downPressed = false;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePos.set(screenX, Gdx.graphics.getHeight() - screenY); //теперь координаты мыши отсчитываются от низа экрана.
        return false;
    }

    public Vector2 getDirection() {
        direction.set(0, 0); //перезаписывает полностью значение вектора

        if(leftPressed) direction.add(-5, 0); //сложение значений вектора
        if(upPressed) direction.add(0, 5);
        if(rightPressed) direction.add(5, 0);
        if(downPressed) direction.add(0, -5);

        return direction;
    }

    public Vector2 getMousePos() {
        return mousePos;
    }
}
