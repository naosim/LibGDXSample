package com.naosim.libgdxsample;

/**
* Created by fujitanao on 15/05/26.
*/
public class Axis2D {
    private float x;
    private float y;
    public Axis2D(float x, float y) {
        set(x, y);
    }
    void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    void set(Axis2D p) {
        set(p.getX(), p.getY());
    }
    void setX(float x) { this.x = x; }
    void setY(float y) { this.y = y; }
    float getX() { return x; }
    float getY() { return y; }
    void inverse() {
        this.x = -x;
        this.y = -y;
    }
    void inverseX() { this.x = -x; }
    void inverseY() { this.y = -y; }


    public void add(Axis2D addValue) {
        set(getX() + addValue.getX(), getY() + addValue.getY());
    }
    public Axis2D diff(Axis2D originValue) {
        return new Axis2D(getX() - originValue.getX(), getY() - originValue.getY());
    }

    public Axis2D scale(float scale) {
        return new Axis2D(getX() * scale, getY() * scale);
    }

    public float abs() {
        return (float)(Math.sqrt(getX() * getX() + getY() * getY()));
    }
}
