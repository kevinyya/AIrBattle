package com.example.airbattle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private Bitmap playerBitmap;
    private Bitmap bulletBitmap;
    private List<Bullet> bullets; // List to hold bullets
    private float x, y;
    private int health;
    private Game game;

    public Player(Bitmap playerBitmap, Bitmap bulletBitmap, Game game) {
        this.playerBitmap = playerBitmap;
        this.bulletBitmap = bulletBitmap;
        this.bullets = new CopyOnWriteArrayList<>();
        this.game = game; // Initialize the reference
    }

    public void update(long currentTime) {
        // Update bullets and remove off-screen bullets
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isOffScreen()) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove); // Remove marked bullets after iteration
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(playerBitmap, x, y, null);
        // Draw all bullets
        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }
    }

    public void shoot() {
        // Create a bullet at the player's position
        Bullet bullet = new Bullet(x + playerBitmap.getWidth()/2 -15 , y, bulletBitmap);
        bullets.add(bullet);
        game.addBullet(bullet); // Notify the Game class
    }

    public Rect getRect() {
        return new Rect((int)x, (int)y, (int)(x + playerBitmap.getWidth()), (int)(y + playerBitmap.getHeight()));
    }

    public void setPosition(float x, float y) {
        // Center the player aircraft under the touch point
        this.x = x - playerBitmap.getWidth() / 2; // Center horizontally
        this.y = y - playerBitmap.getHeight(); // Position above the touch point
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth() {
        health--;
    }

    // Getter methods for x and y positions
    public float getX() {
        return x; // Return current x position
    }

    public float getY() {
        return y; // Return current y position
    }
}