package com.naosim.libgdxsample;

/**
* Created by fujitanao on 15/05/26.
*/
public class Physics2D {
    public final Axis2D accel = new Axis2D(0f, 0f);
    public final Axis2D velocity = new Axis2D(0f, 0f);
    public final Axis2D position = new Axis2D(0f, 0f);

    public void step() {
        velocity.add(accel);
        position.add(velocity);
    }
}
