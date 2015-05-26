package com.naosim.libgdxsample;

/**
* Created by fujitanao on 15/05/26.
*/
public class EntityModel {
    public final Physics2D physics = new Physics2D();
    public final Axis2D scale = new Axis2D(1, 1);
    public final Axis2D targetPosition = new Axis2D(1, 1);
    public final Axis2D targetScale = new Axis2D(1, 1);
    public float alpha = 1;

    public void step() {
        Axis2D diffPosition = targetPosition.diff(physics.position);
        Axis2D forceToTarget = diffPosition.scale(0.1f);
        Axis2D forceResistance = physics.velocity.scale(0.07f);
        physics.accel.set(forceToTarget.diff(forceResistance));

        // 0 -> targetScale
        // r -> 0.5
        float diff = diffPosition.abs();
        float scaleX = targetScale.getX() - (diff / 600);
        float scaleY = targetScale.getY() - (diff / 600);
        scale.set(Math.max(scaleX, 0), Math.max(scaleY, 0));


        physics.step();
    }
}
