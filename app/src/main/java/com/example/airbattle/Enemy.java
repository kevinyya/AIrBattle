package com.example.airbattle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy {
    private Bitmap enemyBitmap;
    private float x, y;
    private int health;
    private int enemyType;
    private float speed;
    private float radius; // Radius for circle collision detection

    public Enemy(float x, float y, Bitmap enemyBitmap, int enemyType) {
        if (enemyType < 0 || enemyType > 2) {
            throw new IllegalArgumentException("Invalid enemy type: " + enemyType);
        }

        this.x = x;
        this.y = y;
        this.enemyBitmap = enemyBitmap;
        this.health = getInitialHealth(enemyType);
        this.enemyType = enemyType;
        this.speed = getInitialSpeed(enemyType);
        this.radius = Math.max(enemyBitmap.getWidth(), enemyBitmap.getHeight()) / 2; // Set radius based on the bitmap size
    }

    private int getInitialHealth(int enemyType) {
        switch (enemyType) {
            case 0: return 1; // Small enemy
            case 1: return 3; // Medium enemy
            case 2: return 5; // Large enemy
            default: return 1;
        }
    }

    private float getInitialSpeed(int enemyType) {
        switch (enemyType) {
            case 0: return 5; // Small enemy speed
            case 1: return 3; // Medium enemy speed
            case 2: return 1; // Large enemy speed
            default: return 2;
        }
    }

    public void update() {
        if (health > 0) {
            y += speed; // Move the enemy down
        }
    }

    public void draw(Canvas canvas) {
        if (health > 0) {
            canvas.drawBitmap(enemyBitmap, x, y, null);
        }
    }

    public Rect getRect() {
        return new Rect((int)x, (int)y, (int)(x + enemyBitmap.getWidth()), (int)(y + enemyBitmap.getHeight()));
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0; // Ensure health doesn't go below zero
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isSmall() {
        return enemyType == 0;
    }

    public boolean isMedium() {
        return enemyType == 1;
    }

    public boolean isLarge() {
        return enemyType == 2;
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight; // Check if the enemy is off the bottom of the screen
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getType() {
        return enemyType;
    }
}