package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounter, tvMessage, tvScore;
    private Button btnStart, btnStop;

    private int currentDigit = 0;
    private int score = 0;
    private boolean isRunning = false;
    private long delayMillis = 500; // Początkowy interwał: 500 ms

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable counterRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvCounter = findViewById(R.id.tvCounter);
        tvMessage = findViewById(R.id.tvMessage);
        tvScore = findViewById(R.id.tvScore);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        btnStop.setEnabled(false);

        counterRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    currentDigit = (currentDigit + 1) % 10;
                    tvCounter.setText(String.valueOf(currentDigit));

                    handler.postDelayed(this, delayMillis);
                }
            }
        };

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = true;
                currentDigit = 0;
                tvCounter.setText(String.valueOf(currentDigit));
                tvMessage.setText("");


                btnStart.setEnabled(false);
                btnStop.setEnabled(true);


                handler.postDelayed(counterRunnable, delayMillis);
            }
        });


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });
    }

    private void stopGame() {
        if (!isRunning) return;

        isRunning = false;
        handler.removeCallbacks(counterRunnable);

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        if (currentDigit == 7) {
            tvMessage.setText("Trafienie!");
            score++;
            tvScore.setText("Trafienia: " + score);

            if (delayMillis > 200) {
                delayMillis -= 50;
            }
        } else {
            tvMessage.setText("Pudło! Zatrzymano na " + currentDigit);
        }
    }
}