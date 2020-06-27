package com.jumpdontdie.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorPinchos extends Actor {

    private Texture pinchos;

    public ActorPinchos(Texture pinchos){
        this.pinchos = pinchos;
        setSize(pinchos.getWidth(), pinchos.getHeight());
    }

    @Override
    public void act(float delta) {
        setX(getX()-700*delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(pinchos, getX(), getY());
    }
}
