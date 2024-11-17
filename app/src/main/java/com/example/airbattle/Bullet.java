package com.example.airbattle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bullet {
    private Bitmap bulletBitmap; // Bitmap for the bullet image
    private float x, y;          // Position coordinates of the bullet
    private final float speed = 10; // Speed at which the bullet moves
    private boolean active = true; // Add an active flag

    // Constructor to initialize bullet position and bitmap
    public Bullet(float x, float y, Bitmap bulletBitmap) {
        this.x = x;
        this.y = y;
        this.bulletBitmap = bulletBitmap;
    }

    // Update the bullet's position
    public void update() {
        y -= speed; // Move the bullet upwards
        if (y < 0) {
            active = false; // Mark bullet as inactive if it goes off-screen
        }
    }

    // Draw the bullet on the canvas
    public void draw(Canvas canvas) {
        if (active) { // Only draw if the bullet is active
            canvas.drawBitmap(bulletBitmap, x, y, null);
        }
    }

    // Get the rectangular bounds of the bullet for collision detection
    public Rect getRect() {
        return new Rect((int)x, (int)y, (int)(x + bulletBitmap.getWidth()), (int)(y + bulletBitmap.getHeight()));
    }

    // Check if the bullet is off the screen
    public boolean isOffScreen() {
        return y < 0; // Returns true if the bullet has moved above the screen
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}