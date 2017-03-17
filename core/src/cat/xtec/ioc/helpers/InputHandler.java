package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.screens.GameScreen;

public class InputHandler implements InputProcessor {

    // Enter per a la gesitó del moviment d'arrastrar
    int previousY = 0;
    // Objectes necessaris
    private Spacecraft spacecraft;
    private GameScreen screen;
    private Vector2 stageCoord;

    private Stage stage;

    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        spacecraft = screen.getSpacecraft();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP && keycode != Input.Keys.DOWN)
        {
            spacecraft.goUp();
            return true;
        }
        else if (keycode == Input.Keys.DOWN && keycode != Input.Keys.UP)
        {
            spacecraft.goDown();
            return true;
        }
        else if (keycode == Input.Keys.RIGHT && keycode != Input.Keys.LEFT)
        {
            spacecraft.goStraight();
            return true;
        }
        else if (keycode == Input.Keys.LEFT && keycode != Input.Keys.RIGHT)
        {
            spacecraft.goBack();
            return true;
        }

        else
        {
            spacecraft.pause();
            return true;
        }



    }
    @Override
    public boolean keyUp(int keycode) {
        spacecraft.pause();
        return true;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {

            case READY:
                // Si fem clic comencem el joc
                //System.out.println(screenX+ "-" +screenY);
                boolean posicionH = 234<screenX && screenX<375;

                if(posicionH && 88<screenY && screenY<116){
                    //FACIL
                    System.out.println("FACIL");
                }else if(posicionH && 116<screenY && screenY<154){
                    //NORMAL
                    System.out.println("NORMAL");
                }else if(posicionH && 154<screenY && screenY<194){
                    //DIFICIL
                    System.out.println("DIFICIL");
                }
                screen.setCurrentState(GameScreen.GameState.RUNNING);

                break;
            case RUNNING:
                previousY = screenY;

                stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                if (actorHit != null)
                    Gdx.app.log("HIT", actorHit.getName());
                break;
            // Si l'estat és GameOver tornem a iniciar el joc
            case GAMEOVER:
                screen.reset();
                break;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // Quan deixem anar el dit acabem un moviment
        // i posem la nau en l'estat normal
        spacecraft.goStraight();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Posem un umbral per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousY - screenY) > 2)

            // Si la Y és major que la que tenim
            // guardada és que va cap avall
            if (previousY < screenY) {
                spacecraft.goDown();
            } else {
                // En cas contrari cap a dalt
                spacecraft.goUp();
            }
        // Guardem la posició de la Y
        previousY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
