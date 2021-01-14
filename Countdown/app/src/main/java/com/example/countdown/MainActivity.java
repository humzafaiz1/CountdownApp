package com.example.countdown;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText countdownHours, countdownMinutes, countdownSeconds;
    Button startCountdown;
    boolean isPressed = true;
    CountDownTimer countDownTimer;
    long totalMil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inputfilter was used so that I could set a min and a max to each number

        countdownHours = findViewById(R.id.timerText1);
        countdownHours.setFilters(new InputFilter[]{ new InputFilterMinMax(0,99)});
        countdownMinutes = findViewById(R.id.timerText2);
        countdownMinutes.setFilters(new InputFilter[]{ new InputFilterMinMax(0,59)});
        countdownSeconds = findViewById(R.id.timerText3);
        countdownSeconds.setFilters(new InputFilter[]{ new InputFilterMinMax(0,59)});
        startCountdown = findViewById(R.id.button);

    }

    public void startStopTimer(View view) {
        // Checking if the button has been pressed or not
        if (isPressed) {
            //Checking to make sure there is a number somewhere, otherwise it prompts user with a toast
            if (countdownSeconds.getText().toString().equals("") &&
                    countdownMinutes.getText().toString().equals("") &&
                    countdownHours.getText().toString().equals("")) {

                Toast.makeText(this, "Enter a time!", Toast.LENGTH_SHORT).show();
            } else {
                /*
                    adding up all the times and summing it into milliseconds
                    have to check if each are empty so that we don't crash the app
                    (can't store an empty string into an int value)
                    Can do this differently if I set each EditText to zero instead

                 */
                int minutes = 0;
                int hours = 0;
                int seconds = 0;
                if(!countdownSeconds.getText().toString().equals("")) {
                    seconds = Integer.parseInt(countdownSeconds.getText().toString());
                }
                if(!countdownMinutes.getText().toString().equals("")){
                    minutes = Integer.parseInt(countdownMinutes.getText().toString());
                }
                if(!countdownHours.getText().toString().equals("")){
                    hours = Integer.parseInt(countdownHours.getText().toString());
                }
                totalMil = convertTimeMilliseconds(seconds, minutes, hours);
                startTimer();
                //Switching button text and changing flag to false
                startCountdown.setText("Stop Countdown");
                isPressed = false;
            }
        } else {
            //Calling this function so that the actual timer stops (countdownTimer)
            stopTimer();
            //Switching the button text back
            startCountdown.setText("Start Countdown");
            //setting flag back to true so that on next press it will start timer
            isPressed = true;
        }
    }

    /*
    Starts the timer with an interval of 1000 (1 second), had to use a global variable for total
    milliseconds for this method. Call the updatetimer method so that the text within
    each edittext changes every second as well.
     */

    public void startTimer(){
        countDownTimer = new CountDownTimer(totalMil, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                totalMil = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                //Changes the button back when the timer is completely finished and changes the flag as well
                startCountdown.setText("Start Countdown");
                isPressed = true;
            }
        }.start();

    }
    // Stops the timer
    public void stopTimer(){
        countDownTimer.cancel();
    }

    // Finds the total milliseconds between the hours minutes and seconds

    public int convertTimeMilliseconds(int s, int m, int h){
        int total = 0;
        total += s * 1000;
        total += m * 60_000;
        total += h * 3.6 * Math.pow(10, 6);
        return total;
    }

    /*
    Changes the text of each EditText to it's respective time
    The total milliseconds are divided by an hour in milliseconds (3.6 * 10^6)
    Afterwards we find the remainder and divide that by a minute in milliseconds (60,000)
    Lastly we find the remainder from the minutes and the hours and get the seconds divide by 1000
    Add a zero to each string in case they are single digits (looks nicer)
    change each edittext to the string so that it changes each second within the startimer method
     */

    public void updateTimer(){
        int hours = (int) totalMil / (int) (3.6 * Math.pow(10, 6));
        int minutes = (int) totalMil % (int)(3.6 * Math.pow(10, 6)) / 60_000;
        int seconds = (int) totalMil % (int)(3.6 * Math.pow(10,6)) % 60_000 / 1_000;

        String hoursText = "";
        String minutesText = "";
        String secondsText = "";

        if(hours < 10) hoursText += "0";
        hoursText += hours;
        if(minutes < 10) minutesText += "0";
        minutesText += minutes;
        if(seconds < 10) secondsText += "0";
        secondsText += seconds;

        countdownHours.setText(hoursText);
        countdownMinutes.setText(minutesText);
        countdownSeconds.setText(secondsText);

    }

}