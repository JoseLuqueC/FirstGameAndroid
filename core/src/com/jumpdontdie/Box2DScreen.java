package com.jumpdontdie;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DScreen extends BaseScreen {


    public Box2DScreen(MainGame game){
        super(game);
    }

    private World world;

    private Box2DDebugRenderer renderer;

    private OrthographicCamera camera;

    private Body duendeBody, sueloBody, pinchoBody;

    private Fixture duendeFixture, sueloFixture, pinchoFixture;

    private boolean debeSaltar, duendeSaltando, duendeVivo = true, tocaSuelo = false;

    @Override
    public void show() {
        world = new World(new Vector2(0, -10), true);
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16,9);
        camera.translate(0,1);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

               if(fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor")
                       ||
                   fixtureB.getUserData().equals("player") && fixtureA.getUserData().equals("floor")){

                   tocaSuelo = true;


                   if(Gdx.input.isTouched()){
                       debeSaltar = true;
                   }
                   duendeSaltando = false;
               }
                if(fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("spike")
                        ||
                        fixtureB.getUserData().equals("player") && fixtureA.getUserData().equals("spike")){
                    duendeVivo = false;
                }

                }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();
                if(fixtureA == duendeFixture && fixtureB == sueloFixture){
                    duendeSaltando = true;
                }
                if(fixtureA == sueloFixture && fixtureB == duendeFixture){
                    duendeSaltando = true;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        duendeBody = world.createBody(createDuendeBodyDef());
        sueloBody = world.createBody(createSueloBodyDef());
        pinchoBody = world.createBody(createPinchoBodyDef(6f));



        PolygonShape duendeShape = new PolygonShape();
        duendeShape.setAsBox(0.5f,0.5f);
        duendeFixture = duendeBody.createFixture(duendeShape, 1);
        duendeShape.dispose();

        PolygonShape sueloShape = new PolygonShape();
        sueloShape.setAsBox(500, 1);
        sueloFixture = sueloBody.createFixture(sueloShape, 1);
        sueloShape.dispose();

        pinchoFixture = createPinchoFixture(pinchoBody);

        duendeFixture.setUserData("player");
        sueloFixture.setUserData("floor");
        pinchoFixture.setUserData("spike");
    }

    private BodyDef createPinchoBodyDef(float x) {
        BodyDef def = new BodyDef();
        def.position.set(x,0.5f);
        return def;
    }

    private BodyDef createSueloBodyDef() {
        BodyDef def = new BodyDef();
        def.position.set(0,-1);
        return def;
    }

    private BodyDef createDuendeBodyDef(){
        BodyDef def = new BodyDef();
        def.position.set(0,10);
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    private Fixture createPinchoFixture(Body pinchoBody){
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(-0.5f, -0.5f);
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        Fixture fix = pinchoBody.createFixture(shape, 1);
        shape.dispose();
        return fix;
    }

    @Override
    public void dispose() {
        duendeBody.destroyFixture(duendeFixture);
        sueloBody.destroyFixture(sueloFixture);
        pinchoBody.destroyFixture(pinchoFixture);
        world.destroyBody(duendeBody);
        world.destroyBody(sueloBody);
        world.destroyBody(pinchoBody);
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(debeSaltar){
            debeSaltar = false;
            saltar();
        }

        if(Gdx.input.justTouched() && !duendeSaltando){
            debeSaltar = true;
        }

        if(duendeVivo && tocaSuelo) {
            float velocidadY = duendeBody.getLinearVelocity().y;
            duendeBody.setLinearVelocity(8, velocidadY);
        }

        world.step(delta, 6, 2);

        camera.update();
        renderer.render(world, camera.combined);
    }

    private void saltar(){
        Vector2 position = duendeBody.getPosition();
        duendeBody.applyLinearImpulse(0,6, position.x, position.y, true);
    }
}
