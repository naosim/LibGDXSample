package com.naosim.libgdxsample;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;


public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        ((ViewGroup)findViewById(R.id.gameContainer)).addView(initializeForView(new MyGdxApp(), cfg));
    }

    class MyGdxApp implements ApplicationListener {

        private static final int NUM_OF_CIRCLE = 20;
        private final Axis2D DISPLAY_CENTER = new Axis2D(480 / 2, 900 / 2);
        private final float GRAVITY = 0.8f;
        private final long CREATE_INTERVAL = 30;
        private final long COMMIT_FRAME = NUM_OF_CIRCLE * CREATE_INTERVAL + 100;

        ShapeRenderer shapeRenderer;
        Random random = new Random();

        private EntityModel[] entityModels;
        private CircleStamp[] entities;
        @Override
        public void create() {
            shapeRenderer = new ShapeRenderer();

            entityModels = new EntityModel[NUM_OF_CIRCLE];
            for(int i = 0; i < entityModels.length; i++) {
                entityModels[i] = new EntityModel();
                float scale = random.nextFloat() / 2 + 0.1f;
                float x = random.nextFloat()* 320 + 80;
                entityModels[i].targetScale.set(scale, scale);
                entityModels[i].targetPosition.set(x, i % 2 == 0 ? 900 : -entityModels[i].getRadius());
            }
//            {
//                int i = 0;
//                entityModels[i] = new EntityModel();
//                entityModels[i].targetScale.set(0.5f, 0.5f);
//                entityModels[i].targetPosition.set(200, 200);
//
//                i++;
//                entityModels[i] = new EntityModel();
//                entityModels[i].targetScale.set(0.4f, 0.4f);
//                entityModels[i].targetPosition.set(200, 380);
//
//                i++;
//                entityModels[i] = new EntityModel();
//                entityModels[i].targetScale.set(0.3f, 0.3f);
//                entityModels[i].targetPosition.set(360, 200);
//
//                i++;
//                entityModels[i] = new EntityModel();
//                entityModels[i].targetScale.set(0.2f, 0.2f);
//                entityModels[i].targetPosition.set(100, 100);
//            }

            float x = 0;
            for(EntityModel em : entityModels) {
                float r = em.targetScale.getX() * 200;
                double randomRad = random.nextDouble() * Math.PI * 2;
                em.physics.position.set(em.targetPosition.getX() + (float)Math.cos(randomRad) * r, em.targetPosition.getY() + (float)Math.sin(randomRad) * r);
            }
            Color[] colors = {
                    new Color(0.5686f, 0.76862f, 0.10980f, 1.0f),
                    new Color(0.8627f, 0.11764f, 0.40784f, 1.0f),
                    new Color(0.9098f, 0.65882f, 0.15294f, 1.0f),
                    new Color(0.1647f, 0.68624f, 0.90196f, 1.0f),
            };
            Color[] colors2 = {
                    new Color(0.5686f, 0.76862f, 0.10980f, 0.4f),
                    new Color(0.8627f, 0.11764f, 0.40784f, 0.4f),
                    new Color(0.9098f, 0.65882f, 0.15294f, 0.4f),
                    new Color(0.1647f, 0.68624f, 0.90196f, 0.4f),
            };


            entities = new CircleStamp[entityModels.length];
            for(int j = 0; j < entityModels.length; j++) {
                CircleStamp e = new CircleStamp(70, 100, 200, colors[j % 4].r, colors[j % 4].g, colors[j % 4].b);
                adoptToEntity(entityModels[j], e);
                entities[j] = e;

            }


        }

        private void adoptToEntity(EntityModel em, CircleStamp m) {
            m.setCenterX(em.physics.position.getX());
            m.setCenterY(em.physics.position.getY());
//            m.setScale(em.scale.getX(), em.scale.getY());
            m.setScale(em.targetScale.getX(), em.targetScale.getY());
        }

        @Override
        public void dispose() {
        }

        @Override
        public void pause() {
        }

        long timeFrame = 0;
        @Override
        public void render() {
            // 赤で塗りつぶす
            Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            if(timeFrame > COMMIT_FRAME) {
                for(int i = 0; i < entityModels.length; i++) {
                    EntityModel em = entityModels[i];
                    if(!em.visible) continue;
                    adoptToEntity(em, entities[i]);
                    entities[i].draw(shapeRenderer);
                }
                Gdx.gl.glDisable(GL20.GL_BLEND);
                return;
            }


            for(int i = 0; i < entityModels.length; i++) {
                EntityModel em = entityModels[i];
                em.visible = (timeFrame > i * CREATE_INTERVAL);
                if(!em.visible) continue;
                em.physics.accel.set(DISPLAY_CENTER.diff(em.physics.position).unit().scale(GRAVITY));// 重力
                // 下
//                if(em.physics.position.getY() - em.getRadius() < 0) {
//                    em.physics.velocity.inverseY();
//                    em.physics.velocity.setY(Math.abs(em.physics.velocity.getY()) * 0.9f);
//                    em.physics.position.setY(em.getRadius());
//                }

                if(em.physics.position.getX() - em.getRadius() < 0) {
                    em.physics.velocity.setX(Math.abs(em.physics.velocity.getX()) * 0.6f);
                    em.physics.position.setX(em.getRadius());
                }
                float width = 480;
                if(em.physics.position.getX()+ em.getRadius() > width) {
                    em.physics.velocity.setX(Math.abs(em.physics.velocity.getX()) * -0.6f);
                    em.physics.position.setX(width - em.getRadius());
                }
            }

            for(int i = 0; i < entityModels.length; i++) {
                EntityModel em1 = entityModels[i];
                if(!em1.visible) continue;
                for(int j = i + 1; j < entityModels.length; j++) {
                    EntityModel em2 = entityModels[j];
                    if(!em2.visible) continue;
                    if(em1.isHit(em2)) {
                        Log.e("HIT", "HIT");
                        Axis2D unitEm2ToEm1 = em1.physics.position.diff(em2.physics.position).unit();
                        em1.physics.accel.add(unitEm2ToEm1.scale(1.3f * (float)Math.pow(em2.getRadius() / em1.getRadius(), 2)));
                        em2.physics.accel.add(unitEm2ToEm1.scale(-1.3f * (float)Math.pow(em1.getRadius() / em2.getRadius(), 2)));
                    }
                }
            }

            for(int i = 0; i < entityModels.length; i++) {
                EntityModel em = entityModels[i];
                if(!em.visible) continue;
                em.step();
                adoptToEntity(em, entities[i]);
                entities[i].draw(shapeRenderer);
            }

            int i = 0;



//            for(EntityModel em : entityModels) {
//                if(i * 10 < timeFrame) {
//                    em.step();
//                    adoptToEntity(em, entities[i]);
//                    entities[i].draw(shapeRenderer);
//                }
//
//                i++;
//            }
            Gdx.gl.glDisable(GL20.GL_BLEND);

            timeFrame++;
        }

        @Override
        public void resize(int width, int height) {
        }

        @Override
        public void resume() {
        }
    }

    public static class CircleStamp {

        private  float x;
        private  float y;
        private  float r;
        private  float cr;
        private  float cg;
        private  float cb;

        public CircleStamp(float x, float y, float r, float cr, float cg, float cb) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.cr = cr;
            this.cg = cg;
            this.cb = cb;
        }
        public void draw(ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(cr, cg, cb, 0.5f);
            shapeRenderer.circle(x, y, r * scale - 4);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(cr, cg, cb, 1f);
            shapeRenderer.circle(x, y, r * scale - 5);
            shapeRenderer.end();
        }

        public void setCenterX(float x) {
            this.x = x;
        }
        public void setCenterY(float y) {
            this.y = y;
        }

        private float scale = 1;
        public void setScale(float x, float y) {
            scale = x;
        }
    }
}
