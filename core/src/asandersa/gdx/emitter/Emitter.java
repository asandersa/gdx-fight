package asandersa.gdx.emitter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Pool;

/**
 * что-то плюющееся
 */
public class Emitter {
    private String owner;
    private final Vector2 position = new Vector2();
    private float spead = 500; //скорость частицы
    private float angle = 0; //угол
    private float rate = 20; //частота
    private float distance = 600; //расстояние pix
    private float size = 16;
    private float lastParticleEmit;

    /**
     * Ипользуем встроенную коллекцию libgdx, когда мы удаляем элементы из этой коллекции
     * они удаляются не сразу, а после определенной команды в один заход, что
     * минимизирует кол-во итераций через цикл и дает экономию.
     */
    private final DelayedRemovalArray<Particle> particles = new DelayedRemovalArray<>();
    private final Pool<Particle> particlePool = new Pool<Particle>() {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };


    public void start(float delta) {
        lastParticleEmit += delta;
        if(lastParticleEmit <= 1/rate) {
            return;
        }
        lastParticleEmit = 0;

        Particle particle = particlePool.obtain();//запрашиваем частичку из пула
        particle.fill(owner, position, angle, size, spead, distance);
        particles.add(particle);

    }

    public void act(float delta) {
        particles.begin(); //даем понять, что начинаем удалять элементы
        for (Particle particle : particles) {
            particle.act(delta);
            if (particle.isFinished()) {
                particles.removeValue(particle, true);
                particlePool.free(particle);
            }
        }
        particles.end(); //даем понять, что нужно все удалять.

    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public DelayedRemovalArray<Particle> getParticles() {
        return particles;
    }
}
