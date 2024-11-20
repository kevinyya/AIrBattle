package com.example.airbattle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private Game game;
    private GameThread gameThread;
    private SurfaceView gameView;

    private SurfaceHolder surfaceHolder;

    private MediaPlayer mediaPlayer;


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
        Bitmap pauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);

        gameView = findViewById(R.id.gameSurfaceView); // Ensure your SurfaceView has this ID
        surfaceHolder = gameView.getHolder(); // Get the SurfaceHolder

        mediaPlayer = MediaPlayer.create(this, R.raw.battle_bgm);
        mediaPlayer.setLooping(true); // Loop the music

        // Get screen dimensions
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics); // Use getMetrics to get screen dimensions
        int screenWidth = metrics.widthPixels; // Get the width of the screen
        int screenHeight = metrics.heightPixels; // Get the height of the screen


        boolean isHardMode = getIntent().getBooleanExtra("HARD_MODE", false);

        // Initialize the game and the thread
        game = new Game(playerBitmap, bulletBitmap, enemyBitmaps, heartBitmap, backgroundBitmap, explosionBitmap, pauseBitmap, screenWidth, screenHeight, this); // Pass 'this' as context
        game.setGameMode(isHardMode);
        gameThread = new GameThread(surfaceHolder, game);

        // Set up the pause button
        ImageButton pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Game", "Pause button clicked");
                game.pauseGame(); // Call the pause method in the Game class
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySavedVolumes();

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        // Resume the game state if it was paused
        if (game != null && game.isPaused()) {
            game.resumeGame();
        }

        // Check if the game thread is already running
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new GameThread(surfaceHolder, game); // Create a new instance
            gameThread.setRunning(true); // Set the thread to running
            gameThread.start(); // Start the thread
        } else {
            Log.d("GameActivity", "Game thread is already running.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
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
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Call the handleTouch method continuously as the finger moves
                game.handleTouch(touchX, touchY);
                break;
            case MotionEvent.ACTION_DOWN:
                // Update position on touch down
                game.handleTouch(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                // Handle touch release if necessary (optional)
                break;
        }
        return true; // Return true to indicate the event was handled
    }

    private void applySavedVolumes() {
        SharedPreferences preferences = getSharedPreferences("game_settings", MODE_PRIVATE);
        float musicVolume = preferences.getFloat("music_volume", 1.0f); // Default to 100% volume
        float effectVolume = preferences.getFloat("effect_volume", 1.0f);

        setMusicVolume(musicVolume);
    }

    private void setMusicVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }
}