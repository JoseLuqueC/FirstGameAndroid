package com.jumpdontdie.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jumpdontdie.BaseScreen;
import com.jumpdontdie.MainGame;

public class Scene2DScreen extends BaseScreen {

    Scene2DScreen(MainGame game){
        super(game);
        texturaJugador = new Texture("duende.png");
        texturaPinchos = new Texture("pinchos.png");
    }

    private Stage stage;

    private ActorJugador jugador;
    private ActorPinchos pinchos;

    private Texture texturaJugador, texturaPinchos;

    @Override
    public void show() {

        stage = new Stage();
        stage.setDebugAll(true);

        jugador = new ActorJugador(texturaJugador);
        pinchos = new ActorPinchos(texturaPinchos);
        stage.addActor(jugador);
        stage.addActor(pinchos);

        jugador.setPosition(20, 100);
        pinchos.setPosition(1600, 100);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f,0.5f,0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        comprobarColisiones();

        stage.draw();
    }

    private void comprobarColisiones(){
        if(jugador.isAlive() &&
                (jugador.getX()+jugador.getWidth()<pinchos.getX())){
            jugador.setAlive(false);
        }
    }

    @Override
    public void dispose() {
        texturaJugador.dispose();
        texturaPinchos.dispose();
    }
}
