package asandersa.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Ship {
    private final float size = 100;
    private final float halfSize = size / 2; //для экономии памяти процессора


    private final Vector2 position = new Vector2(); //определяет положение в пространстве
    private final Vector2 angel = new Vector2();

    private final Vector2 origin = new Vector2();

    private final Texture texture;

    private final TextureRegion textureRegion;
    /**
     * Инициализируем текстуру в конструкторе, передаем координаты объекта в вектор (x, y)
     */
    public Ship(float x, float y) {
        this(x,y, "ship.png");
    }

    public Ship(float x, float y, String textureName) {
        texture = new Texture(textureName);
        textureRegion = new TextureRegion(texture);
        position.set(x,y);
        origin.set(position).add(halfSize, halfSize);
    }

    /**
     * Отрисовываем изображение
     */
    public void render(Batch batch) {
        /**
         * У метода draw есть филды, которые позволяют указывать наклон нашего изображения-текстуры.
         * Но с классом Texture это не работает, по этому оборачиваем текстуру в TextureRegion.
         */
        batch.draw(textureRegion,
                    position.x, position.y,
                    halfSize, halfSize,
                    size, size, //ширина, высота
                    1, 1, //масштабирование
                    angel.angleDeg()); //угол на который нужно повернуть картинку, в градусах

    }

    /**
     * Освобождаем текстуры, для корректного завершения приложения.
     */
    public void dispose() {
        texture.dispose();
    }

    public void moveTo(Vector2 direction) {
        position.add(direction); //прибавляет к положению вектор перемещения
        origin.set(position).add(halfSize, halfSize);
    }

    public void rotateTo(Vector2 mousePos) {
        /**
         * Вектор mousePos - координаты мыши, вектор position - координаты персонажа
         * Разница векторов mousePose - position = координаты курсора относительно персонажа (описывают угол наклона)
         * upd origin = position + (halfSize, halfSize)
         */
        angel.set(mousePos).sub(origin);

    }


    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getOrigin() {
        return origin;
    }

}
