package asandersa.gdx;

public interface InputState {
    boolean isLeftPressed();

    boolean isRightPressed();

    boolean isUpPressed();

    boolean isDownPressed();

    boolean isFirePressed();

    float getAngle();

    void setLeftPressed(boolean leftPressed);

    void setRightPressed(boolean rightPressed);

    void setUpPressed(boolean upPressed);

    void setDownPressed(boolean downPressed);

    void setFirePressed(boolean firePressed);

    void setAngle(float angle);
}
