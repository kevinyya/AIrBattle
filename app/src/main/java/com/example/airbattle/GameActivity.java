package com.example.airbattle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameThread gameThread;
    private SurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Load your Bitmap objects from resources
        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_aircraft);
        Bitmap bulletBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        Bitmap[] enemyBitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.enemy_aircraft_small),
                BitmapFactory.decodeResource(getResources(), R.drawable.enemy_aircraft_medium),
                BitmapFactory.decodeResource(getResources(), R.drawable.enemy_aircraft_large)
        };
        Bitmap heartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background); // Load background
        Bitmap explosionBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);

        gameView = findViewById(R.id.gameSurfaceView); // Ensure your SurfaceView has this ID
        SurfaceHolder surfaceHolder = gameView.getHolder(); // Get the SurfaceHolder

        // Get screen dimensions
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();

        // Initialize the game and the thread
        game = new Game(playerBitmap, bulletBitmap, enemyBitmaps, heartBitmap, backgroundBitmap, explosionBitmap, screenWidth, screenHeight, this); // Pass 'this' as context
        gameThread = new GameThread(surfaceHolder, game);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the game thread if it's not already running
        if (!gameThread.isAlive()) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the game thread
        gameThread.setRunning(false);
        try {
            gameThread.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Update player position based on touch input
        game.handleTouch(x, y); // Call handleTouch with only x and y
        return true; // Indicate that the touch event was handled
    }
}