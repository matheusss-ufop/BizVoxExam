package com.android.bizvoxexam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class GetAShotActivity extends Activity {

    private static GetAShotApiDribbble dribbbleGetAShot;

    private static TextView shot_title;
    private static ImageView shot_image;
    private static TextView shot_description;
    private static TextView shot_views_count;
    private static TextView shot_comments_count;
    private static TextView shot_created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_a_shot);

        dribbbleGetAShot = new GetAShotApiDribbble(this);

        int shotId = getIntent().getIntExtra("id", 0);

        dribbbleGetAShot.getShot(shotId);

        shot_title = (TextView) findViewById(R.id.shot_title);
        shot_image = (ImageView) findViewById(R.id.shot_image);
        shot_description = (TextView) findViewById(R.id.shot_description);
        shot_views_count = (TextView) findViewById(R.id.shot_views_count);
        shot_comments_count = (TextView) findViewById(R.id.shot_comments_count);
        shot_created_at = (TextView) findViewById(R.id.shot_created_at);
    }

    public static void loadShotData(){

        ShotItem shot = dribbbleGetAShot.getShot();

        shot_title.setText(shot.getTitle());
        shot_image.setImageBitmap(shot.getImage());
        shot_description.setText(shot.getDescription());
        shot_views_count.setText(shot.getViewsCount() + " views");
        shot_comments_count.setText(shot.getCommentsCount() + " comments");
        shot_created_at.setText(shot.getCreatedAt());


    }

}
