package com.example.root.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

/**
 * Created by felias on 5.12.16.
 */

public class HelpActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private float lastX;
    private ProgressBar progressBar;
    private int numberOfImages = 3;
    private int partialProgress = 100 / numberOfImages;
    private int currentImage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        viewFlipper = (ViewFlipper) findViewById(R.id.help_gallery);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(partialProgress);

        ImageButton previous = (ImageButton) findViewById(R.id.but_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextImage();
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.but_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevImage();
            }
        });
    }

    // Using the following method, we will handle all screen swaps.
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();

                // Handling left to right screen swap.
                if (lastX < currentX) {
                    showNextImage();

                }

                // Handling right to left screen swap.
                if (lastX > currentX) {

                    showPrevImage();
                }
                break;
        }
        return false;
    }

    private void showNextImage(){
        // If there aren't any other children, just break.
        if (viewFlipper.getDisplayedChild() == 0)
            return;

        // Next screen comes in from left.
        viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
        // Current screen goes out from right.
        viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

        // Display next screen.
        viewFlipper.showNext();
        if (currentImage > 1) {
            currentImage--;
            progressBar.setProgress(currentImage * partialProgress);
        }
    }

    private void showPrevImage() {
        // If there is a child (to the left), kust break.
        if (viewFlipper.getDisplayedChild() == 1)
            return;

        // Next screen comes in from right.
        viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
        // Current screen goes out from left.
        viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

        // Display previous screen.
        viewFlipper.showPrevious();
        if (currentImage < numberOfImages - 1) {
            currentImage++;
            progressBar.setProgress(currentImage* partialProgress);
        } else if (currentImage == numberOfImages - 1) {
            progressBar.setProgress(100);
            currentImage++;
        }
    }

}
