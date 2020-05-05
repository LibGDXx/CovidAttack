package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class Enemy {
    private static final int BOX_SIZE = 32;
    private static final float ENEMY_DENSITY = 1.0f;
    public static final float ENEMY_JUMP_FORCE = 250f;
    public static final float ENEMY_RUN_FORCE = 5f;
    public static final String ENEMY_IMG_PATH = "enemy1.png";
    private static final float ENEMY_START_X = 17f;
    private static final float ENEMY_START_Y = 18f;
    private Body body;
    private boolean isJumping = false;
    private boolean isDead = false;

    public Enemy(World world) {
        createBoxBody1(world, ENEMY_START_X, ENEMY_START_Y);
    }
    private void createBoxBody1(World world, float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BOX_SIZE / CovidAttack.PIXEL_PER_METER / 2, BOX_SIZE / CovidAttack.PIXEL_PER_METER / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = ENEMY_DENSITY;
        body = world.createBody(bdef);
        body.createFixture(fixtureDef).setUserData(this);
    }
    public Body getBody1() {
        return body;
    }
    public void hit1() {
        isDead = true;
    }
    public void setJumping1(boolean jumping) {
        isJumping = jumping;
    }
    public boolean isJumping1() {
        return isJumping;
    }
    public boolean isDead1() {
        return isDead;
    }
}
