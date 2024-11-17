package com.example.airbattle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    private Player player;
    private List<Enemy> enemies; // Thread-safe list for enemies
    private List<Bullet> bullets; // Thread-safe list for bullets
    private int score;
    private Paint paint;
    private boolean isGameOver;
    private Bitmap[] enemyBitmaps;
    private Bitmap heartBitmap;
    private Bitmap backgroundBitmap; // Background bitmap
    private Bitmap explosionBitmap; // Bitmap for explosion effect
    private float lastDestroyedEnemyX; // X position of last destroyed enemy
    private float lastDestroyedEnemyY; // Y position of last destroyed enemy
    private long explosionStartTime; // Time when explosion starts
    private final long explosionDuration = 500; // Duration of explosion in milliseconds

    private final int MAX_HEALTH = 5;
    private int playerHealth; // Track player health
    private int screenWidth;
    private int screenHeight;
    private Context context; // Context for dialogs

    private long lastSpawnTime;
    private final long spawnInterval = 2000; // Spawn an enemy every 2 seconds
    private long lastBulletTime; // Track last bullet spawn time
    private final long bulletInterval = 1000; // Shoot a bullet every 1 second

    private long elapsedTime = 0; // Track elapsed time in milliseconds
    private final long increaseSpawnRateInterval = 60000; // 1 minute in milliseconds
    private float spawnRateMultiplier = 0.5f; // Initial spawn rate multiplier

    public Game(Bitmap playerBitmap, Bitmap bulletBitmap, Bitmap[] enemyBitmaps, Bitmap heartBitmap, Bitmap backgroundBitmap, Bitmap explosionBitmap, int screenWidth, int screenHeight, Context context) {
        this.player = new Player(playerBitmap, bulletBitmap, this);
        this.enemies = new CopyOnWriteArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.score = 0;
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        this.isGameOver = false;
        this.enemyBitmaps = enemyBitmaps;
        this.heartBitmap = heartBitmap;
        this.backgroundBitmap = backgroundBitmap; // Initialize the background bitmap
        this.explosionBitmap = explosionBitmap; // Initialize the explosion bitmap
        this.playerHealth = MAX_HEALTH; // Set player's initial health
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.context = context; // Save the context for showing dialogs
        this.lastSpawnTime = System.currentTimeMillis(); // Initialize last spawn time
        this.lastBulletTime = System.currentTimeMillis(); // Initialize last bullet time

        initializePlayerPosition();
    }

    private void initializePlayerPosition() {
        float playerWidth = player.getRect().width();
        float playerHeight = player.getRect().height();
        player.setPosition((screenWidth) / 2, screenHeight - playerHeight - 150);
    }

    private void spawnEnemies(long currentTime) {
        // Update elapsed time
        elapsedTime += (currentTime - lastSpawnTime);

        // Check if one minute has passed to increase the spawn rate
        if (elapsedTime >= increaseSpawnRateInterval) {
            spawnRateMultiplier += 0.3f; // Increase the spawn rate by 30%
            elapsedTime = 0; // Reset elapsed time
            Log.d("SpawnRate", "Spawn rate increased to: " + spawnRateMultiplier);
        }

        // Calculate adjusted spawn interval based on the multiplier
        long adjustedSpawnInterval = (long)(spawnInterval / spawnRateMultiplier);

        if (currentTime - lastSpawnTime >= adjustedSpawnInterval) {
            float x = (float) (Math.random() * (screenWidth - 100));
            int enemyType = getRandomEnemyType(); // Use weighted random selection
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(enemyBitmaps[enemyType],
                    enemyBitmaps[enemyType].getWidth() / 2,
                    enemyBitmaps[enemyType].getHeight() / 2,
                    false);
            enemies.add(new Enemy(x, 0, scaledBitmap, enemyType)); // Add new enemy
            lastSpawnTime = currentTime; // Update last spawn time
        }
    }

    private int getRandomEnemyType() {
        // Weighted random selection
        int randomValue = (int) (Math.random() * 100); // Random number from 0 to 99
        if (randomValue < 70) {
            return 0; // Small enemy (70% chance)
        } else if (randomValue < 90) {
            return 1; // Medium enemy (20% chance)
        } else {
            return 2; // Large enemy (10% chance)
        }
    }

    public void update(long currentTime) {
        if (isGameOver) return;

        spawnEnemies(currentTime); // Spawn enemies based on time

        // Automatic bullet shooting
        if (currentTime - lastBulletTime >= bulletInterval) {
            player.shoot(); // Automatically shoot a bullet
            lastBulletTime = currentTime; // Update last bullet time
        }

        player.update(currentTime); // Update player position and bullets

        // Update enemies and check for collisions
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(); // Update enemy position

            // Check collision with player
            if (checkCollision(player, enemy)) {
                handlePlayerCollision(); // Handle player collision with enemy
                enemies.remove(i); // Remove enemy on collision
                continue;
            }

            // Check for bullet collisions
            checkBulletCollisions(i, enemy);
        }

        // Remove off-screen bullets
        bullets.removeIf(Bullet::isOffScreen); // Use lambda for cleaner syntax
    }

    private void handlePlayerCollision() {
        playerHealth--; // Decrease player health on collision
        if (playerHealth <= 0) {
            isGameOver = true; // End the game if health is zero
            showGameOverDialog(); // Show game over dialog
        }
    }

    private void checkBulletCollisions(int enemyIndex, Enemy enemy) {
        for (int j = bullets.size() - 1; j >= 0; j--) {
            Bullet bullet = bullets.get(j);

            if (checkCollision(bullet, enemy)) {
                enemy.takeDamage(1);
                bullet.setActive(false);
                bullets.remove(j);
                if (!enemy.isAlive()) {
                    // Save enemy position for explosion
                    lastDestroyedEnemyX = enemy.getX(); // Get the enemy's X position
                    lastDestroyedEnemyY = enemy.getY(); // Get the enemy's Y position
                    explosionStartTime = System.currentTimeMillis(); // Start explosion timer
                    explosionBitmap = getScaledExplosionBitmap(enemy.getType());
                    increaseScore(getEnemyPoints(enemy));
                    enemies.remove(enemyIndex);
                }
                break; // Exit the bullet loop after hit
            }
        }
    }

    public void draw(Canvas canvas) {
        if (canvas == null) return;

        // Draw the background first
        canvas.drawBitmap(backgroundBitmap, 0, 0, null); // Draw the background bitmap
        player.draw(canvas); // Draw the player aircraft
        for (Enemy enemy : enemies) {
            enemy.draw(canvas); // Draw all enemies
        }
        for (Bullet bullet : bullets) {
            bullet.draw(canvas); // Draw all bullets
        }
        drawScore(canvas); // Draw the score
        drawHealth(canvas); // Draw player health

        // Draw explosion if active
        if (System.currentTimeMillis() - explosionStartTime < explosionDuration) {
            canvas.drawBitmap(explosionBitmap, lastDestroyedEnemyX, lastDestroyedEnemyY, null);
        }

        if (isGameOver) {
            drawGameOver(canvas); // Draw game over screen if applicable
        }
    }

    private void drawScore(Canvas canvas) {
        String scoreText = "Score: " + score;
        float textWidth = paint.measureText(scoreText); // Measure the width of the text
        float xPosition = (canvas.getWidth() - textWidth) / 2; // Center the text
        canvas.drawText(scoreText, xPosition, 100, paint); // Draw the text at the calculated position
    }

    private void drawHealth(Canvas canvas) {
        for (int i = 0; i < playerHealth; i++) {
            canvas.drawBitmap(heartBitmap, canvas.getWidth() - 1100 + i * 100, canvas.getHeight() - 150, null);
        }
    }

    private void drawGameOver(Canvas canvas) {
        paint.setTextSize(100);
        paint.setColor(Color.RED);
        canvas.drawText("Game Over", 250, 500, paint);
    }

    private boolean checkCollision(Player player, Enemy enemy) {
        return Rect.intersects(player.getRect(), enemy.getRect());
    }

    private boolean checkCollision(Bullet bullet, Enemy enemy) {
        return Rect.intersects(bullet.getRect(), enemy.getRect());
    }

    public void handleTouch(float x, float y) {
        if (isGameOver) {
            resetGame(); // Restart game on touch if game over
            return;
        }
        player.setPosition(x, y); // Update player position based on touch input
    }

    public void increaseScore(int amount) {
        score += amount; // Increase score
    }

    public void resetGame() {
        score = 0;
        enemies.clear();
        bullets.clear(); // Clear bullets on reset
        playerHealth = MAX_HEALTH; // Reset player health
        isGameOver = false;
        initializePlayerPosition();
    }

    private int getEnemyPoints(Enemy enemy) {
        if (enemy.isSmall()) return 10;
        if (enemy.isMedium()) return 30;
        if (enemy.isLarge()) return 100;
        return 0;
    }

    public void showGameOverDialog() {
        ((Activity) context).runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Game Over");
            builder.setMessage("Score: " + score);

            builder.setPositiveButton("Restart", (dialog, which) -> resetGame()); // Restart the game
            builder.setNegativeButton("Main Menu", (dialog, which) -> {
                // Logic to go back to the main menu
                Intent intent = new Intent(context, MenuActivity.class);
                context.startActivity(intent);
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    private float getExplosionScale(int enemyType) {
        switch (enemyType) {
            case 0: // Small enemy
                return 3.0f; // Scale down to 50%
            case 1: // Medium enemy
                return 5.0f; // Original size
            case 2: // Large enemy
                return 10.0f; // Scale up to 150%
            default:
                return 1.0f; // Default scale
        }
    }
    private Bitmap getScaledExplosionBitmap(int enemyType) {
        float scale = getExplosionScale(enemyType);
        int explosionWidth = (int) (59 * scale); // Original width
        int explosionHeight = (int) (59 * scale); // Original height

        return Bitmap.createScaledBitmap(explosionBitmap, explosionWidth, explosionHeight, false);
    }
}