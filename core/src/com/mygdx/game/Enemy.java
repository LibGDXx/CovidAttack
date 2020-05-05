package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class Enemy {
    private static final int BOX_SIZE = 32; //speed of enemy. lower = faster, higher = can't move
    private static final float ENEMY_DENSITY = 1.0f; //gravity
    public static final String ENEMY_IMG_PATH = "enemy1.png"; //path to the sprite of the first enemy character
    private static final float ENEMY_START_X = 17f; //where enemy starts x-coordinate. doesn't update
    private static final float ENEMY_START_Y = 18f; //where enemy starts y-coordinate. doesn't update
    private Body body; //the body of the enemy where physics is applied to

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
    //allows the CovidAttack() method to obtain the full enemy body that has physics, size, and starting coordinates.
    public Body getBody1() {
        return body;
    }
}
