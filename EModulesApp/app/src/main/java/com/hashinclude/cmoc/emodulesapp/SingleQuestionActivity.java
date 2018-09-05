package com.hashinclude.cmoc.emodulesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//Will be used to load a single question from the db
public class SingleQuestionActivity extends AppCompatActivity {

    ViewPager questionViewPager;
    Toolbar toolbar;
    QuestionViewPagerAdapter questionViewPagerAdapter;
    DatabaseAdapter databaseAdapter;
    TextView toolBarTextView, timerTextView;
    QuestionModel questionModel;
    int position;
    ImageView flagQuestionToolbarImageView, backArrowIcon;
    SmartTabLayout indicator;
    Vibrator vibrator;
    EventBus eventBus;
    Context context;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_question);

        eventBus = EventBus.getDefault();
        eventBus.register(this);
        timer = new Timer();
        context = this;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent = getIntent();
        position = intent.getIntExtra("positionInRecyclerView", 0);
        questionModel = intent.getParcelableExtra("questionModel");
        databaseAdapter = new DatabaseAdapter(this);
        questionModel = databaseAdapter.getDataForASingleRow(questionModel.getId());

        questionViewPager = findViewById(R.id.questionViewPager);
        toolBarTextView = findViewById(R.id.toolbarTextView);
        toolbar = findViewById(R.id.questionActivityToolbar);
        timerTextView = findViewById(R.id.toolbarTimerTextView);
        indicator = findViewById(R.id.viewpagertab);
        flagQuestionToolbarImageView = findViewById(R.id.flagQuestionTooolbar);
        backArrowIcon = findViewById(R.id.backArrowIcon);

        backArrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(30);
                onBackPressed();
            }
        });

        toolBarTextView.setText("Question " + questionModel.getId());


        questionViewPagerAdapter = new QuestionViewPagerAdapter(getSupportFragmentManager(), questionModel);
        questionViewPager.setAdapter(questionViewPagerAdapter);
        indicator.setViewPager(questionViewPager);
        questionViewPager.setCurrentItem(1);


        if (questionModel.getFlagged() == 0) {
            GlideApp.with(this)
                    .load(R.drawable.flag_white_border)
                    .into(flagQuestionToolbarImageView);
        } else {
            GlideApp.with(this)
                    .load(R.drawable.flagged_white_border)
                    .into(flagQuestionToolbarImageView);
        }
        flagQuestionToolbarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(70);
                if (questionModel.getFlagged() == 0) {
                    GlideApp.with(context)
                            .load(R.drawable.flagged_white_border)
                            .into((ImageView) view);
                    questionModel.setFlagged(1);
                    databaseAdapter.updateFlagged(questionModel.getId(), 1);
                } else {
                    GlideApp.with(context)
                            .load(R.drawable.flag_white_border)
                            .into((ImageView) view);
                    questionModel.setFlagged(0);
                    databaseAdapter.updateFlagged(questionModel.getId(), 0);
                }
            }
        });

        questionViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(questionViewPager.getApplicationWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final int[] currentTimeInSeconds = new int[10];


        if (TextUtils.isEmpty(questionModel.getTimeTaken())) {
            questionModel.setTimeTaken("00:00");
            databaseAdapter.updateTime(questionModel.getId(), "00:00");
            timerTextView.setText("00:00");
            currentTimeInSeconds[0] = 0;
        } else {
            String[] timeValue = questionModel.getTimeTaken().split(":");
            int min = Integer.parseInt(timeValue[0]) * 60;
            int sec = Integer.parseInt(timeValue[1]);
            currentTimeInSeconds[0] = min + sec;
            timerTextView.setText(questionModel.getTimeTaken());
        }

        //If nothing marked, then only run the timer
        if (TextUtils.isEmpty(questionModel.getMarked())) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int minutes = currentTimeInSeconds[0] / 60;
                    int seconds = currentTimeInSeconds[0] % 60;
                    if (minutes >= 100) {
                        databaseAdapter.updateTime(questionModel.getId(), "99:59");
                        updateTextView("99", "59");
                        this.cancel();
                    }
                    String min = String.valueOf(minutes), sec = String.valueOf(seconds);
                    String minToShow = "", secToShow = "";
                    if (minutes / 10 == 0) {
                        minToShow = "0";
                    }
                    minToShow += min;
                    if (seconds / 10 == 0) {
                        secToShow = "0";
                    }
                    secToShow += sec;
                    final String finalMinToShow = minToShow;
                    final String finalSecToShow = secToShow;
                    databaseAdapter.updateTime(questionModel.getId(), finalMinToShow + ":" + finalSecToShow);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTextView(finalMinToShow, finalSecToShow);
                        }
                    });
                    currentTimeInSeconds[0]++;
                }
            }, 500, 1000);
        }
    }

    public void updateTextView(String minToShow, String secToShow) {
        timerTextView.setText(minToShow + ":" + secToShow);
    }


    //catch Event from fragment A
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SubmitButtonClickedEvent event) {
        TextView textView = (TextView) indicator.getTabAt(0);
        textView.setText(Html.fromHtml("Solution " + "\uD83D\uDD13").toString());

        timer.cancel();
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("recyclerViewPosition", position);
        returnIntent.putExtra("idOfQuestion", questionModel.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        timer.cancel();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(questionViewPager.getApplicationWindowToken(), 0);

        finish();
    }


    class QuestionViewPagerAdapter extends FragmentPagerAdapter {
        QuestionModel questionModel;
        ArrayList<String> arrayList = new ArrayList<>();
        String locked = "\uD83D\uDD12";
        String unlocked = "\uD83D\uDD13";

        public QuestionViewPagerAdapter(FragmentManager fm, QuestionModel questionModel) {
            super(fm);
            this.questionModel = questionModel;
            if (TextUtils.isEmpty(questionModel.getMarked())) {
                arrayList.add(Html.fromHtml("Solution " + locked).toString());
            } else {
                arrayList.add(Html.fromHtml("Solution " + unlocked).toString());
            }
            arrayList.add("Question");
            arrayList.add("Notes");
        }

        @Override
        public Fragment getItem(int position) {
            Fragment myFragment = null;
            if (position == 0) {
                SolutionFragment solutionFragment = SolutionFragment.newInstance(questionModel);
                myFragment = solutionFragment;
            }
            if (position == 1) {
                QuestionFragment questionFragment = QuestionFragment.newInstance(questionModel);
                myFragment = questionFragment;
            }
            if (position == 2) {
                NotesFragment notesFragment = NotesFragment.newInstance(questionModel);
                myFragment = notesFragment;
            }
            return myFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }

}
