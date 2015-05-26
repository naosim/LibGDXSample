package com.naosim.libgdxsample;

import android.os.Bundle;

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
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

        initialize(new MyGdxApp(), cfg);
    }

    class MyGdxApp implements ApplicationListener {

        ShapeRenderer shapeRenderer;
        CircleStamp circleStamp;
        Random random = new Random();

        private EntityModel[] entityModels;
        private CircleStamp[] entities;
        @Override
        public void create() {
            shapeRenderer = new ShapeRenderer();

            entityModels = new EntityModel[4];
            {
                int i = 0;
                entityModels[i] = new EntityModel();
                entityModels[i].targetScale.set(0.5f, 0.5f);
                entityModels[i].targetPosition.set(200, 200);

                i++;
                entityModels[i] = new EntityModel();
                entityModels[i].targetScale.set(0.4f, 0.4f);
                entityModels[i].targetPosition.set(200, 380);

                i++;
                entityModels[i] = new EntityModel();
                entityModels[i].targetScale.set(0.3f, 0.3f);
                entityModels[i].targetPosition.set(360, 200);

                i++;
                entityModels[i] = new EntityModel();
                entityModels[i].targetScale.set(0.2f, 0.2f);
                entityModels[i].targetPosition.set(100, 100);
            }

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
            m.setScale(em.scale.getX(), em.scale.getY());
        }

        @Override
        public void dispose() {
        }

        @Override
        public void pause() {
        }

        @Override
        public void render() {
            // 赤で塗りつぶす
            Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            int i = 0;
            for(EntityModel em : entityModels) {
                em.step();
                adoptToEntity(em, entities[i]);
                entities[i].draw(shapeRenderer);
                i++;
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
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
            shapeRenderer.circle(x, y, r * scale + 1);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(cr, cg, cb, 1f);
            shapeRenderer.circle(x, y, r * scale);
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
