package com.hashinclude.cmoc.emodulesapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class MainActivity extends AppCompatActivity {

    //main RecyclerView will hold the data to show on the MainScreen
    RecyclerView mainRecyclerView;
    VerticalRecyclerViewFastScroller fastScroller;
    DatabaseAdapter databaseAdapter;
    ArrayList<QuestionModel> questionModelArrayList;
    Context context;
    MainRecyclerViewAdapter adapter;
    Vibrator vibrator;
    public static int REQUEST_CODE = 100;
    SlidingUpPanelLayout slidingUpPanelLayout;

    TextView correctTextView, incorrectTextView, unattemptedTextView;
    int countCorrect, countIncorrect, countUnattempted;

    LinearLayout normalToolbar, dragView, searchView;
    EditText searchStringEditText;
    ImageView searchIconImageView, searchToolbarBackArrow;

    LinearLayout afterSearchToolbar;
    ImageView afterSearchBackArrow;
    String textSearchedFor;
    TextView afterSearchTextView;

    //    FOR CHARTS :
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        context = this;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        databaseAdapter = new DatabaseAdapter(this);
        questionModelArrayList = databaseAdapter.getAllData();

        correctTextView = findViewById(R.id.numberOfCorrect);
        incorrectTextView = findViewById(R.id.numberOfIncorrect);
        unattemptedTextView = findViewById(R.id.numberOfUnattempted);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        searchIconImageView = findViewById(R.id.searchIconImageView);
        normalToolbar = findViewById(R.id.normalToolbar);
        dragView = findViewById(R.id.dragView);
        searchView = findViewById(R.id.searchToolbar);
        searchToolbarBackArrow = findViewById(R.id.searcToolbarBackArrow);
        searchStringEditText = findViewById(R.id.searchStringEditText);

        afterSearchToolbar = findViewById(R.id.afterSearchToolBar);
        afterSearchBackArrow = findViewById(R.id.afterSearchToolBarBackArrow);
        afterSearchTextView = findViewById(R.id.afterSearchTextView);


        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED) {

                } else if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    barChart.animateY(2000);
                    barChart.invalidate();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(panel.getApplicationWindowToken(), 0);

                normalToolbar.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);
            }
        });


//        FOR CHARTS
        barChart = findViewById(R.id.barChart);
        adapter = new MainRecyclerViewAdapter(this, questionModelArrayList);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(adapter);
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        fastScroller = findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(mainRecyclerView);

        countCorrect = 0;
        countIncorrect = 0;
        countUnattempted = 0;
        for (int i = 0; i < questionModelArrayList.size(); i++) {
            if (TextUtils.isEmpty(questionModelArrayList.get(i).getMarked())) {
                countUnattempted++;
            } else if (questionModelArrayList.get(i).getMarked().equals(questionModelArrayList.get(i).getCorrect())) {
                countCorrect++;
            } else {
                countIncorrect++;
            }
        }

        correctTextView.setText(String.format("%03d", countCorrect));
        incorrectTextView.setText(String.format("%03d", countIncorrect));
        unattemptedTextView.setText(String.format("%03d", countUnattempted));


        mainRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

        //Added the OnTouchListener
        mainRecyclerView.addOnItemTouchListener(new RowClickedListener(this, mainRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                vibrator.vibrate(30);
//                Sent the intent to SingleQuestionActivity
                Intent intent = new Intent(context, SingleQuestionActivity.class);
                intent.putExtra("positionInRecyclerView", position);
                intent.putExtra("questionModel", questionModelArrayList.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }

            //flag question on long click
            @Override
            public void onLongItemClick(View view, int position) {
                vibrator.vibrate(70);
                if (questionModelArrayList.get(position).getFlagged() == 0) {
                    questionModelArrayList.get(position).setFlagged(1);
                    //id is 1 index based, but position is 0 based
                    databaseAdapter.updateFlagged(position + 1, 1);
                    Toast.makeText(context, "Flagged Question No." + questionModelArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                } else {
                    questionModelArrayList.get(position).setFlagged(0);
                    //id is 1 index based, but position is 0 based
                    databaseAdapter.updateFlagged(position + 1, 0);
                    Toast.makeText(context, "Unflagged Question No." + questionModelArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        }));


        setUpSearchBar();
        setUpCharts();
        setUpAfterSearchBar();
    }

    public void setUpAfterSearchBar() {
        afterSearchTextView.setText(textSearchedFor);
        afterSearchBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(30);
                normalToolbar.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                afterSearchToolbar.setVisibility(View.GONE);
                ArrayList<QuestionModel> questionModels = databaseAdapter.getAllData();
                questionModelArrayList = questionModels;
                adapter.updateList(questionModels);
            }
        });
    }

    public void setUpSearchBar() {

        searchToolbarBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalToolbar.setVisibility(View.VISIBLE);
                afterSearchToolbar.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                vibrator.vibrate(30);
            }
        });

        searchIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalToolbar.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                vibrator.vibrate(30);
            }
        });

        searchStringEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        textSearchedFor = v.getText().toString();
                        String stringToSearch = textSearchedFor.toLowerCase();
                        ArrayList<QuestionModel> questionModels = databaseAdapter.getAllData(stringToSearch);
                        questionModelArrayList = questionModels;
                        adapter.updateList(questionModels);
//                        Log.d("VIEW", questionModels.size() + "");
                    } catch (Exception e) {
                        //We get an exception when nothing matches. So better to handle it here
//                        Log.d("VIEW", "Caught Exception");

                    } finally {
                        normalToolbar.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);

                        //Update text view when making it visible
                        afterSearchToolbar.setVisibility(View.VISIBLE);
                        afterSearchTextView.setText(textSearchedFor);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (searchView.getVisibility() == View.VISIBLE) {
            normalToolbar.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            afterSearchToolbar.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);
        } else if (afterSearchToolbar.getVisibility() == View.VISIBLE) {
            normalToolbar.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            afterSearchToolbar.setVisibility(View.GONE);
            ArrayList<QuestionModel> questionModels = databaseAdapter.getAllData();
            questionModelArrayList = questionModels;
            adapter.updateList(questionModels);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        barChart.animateY(2000);
        barChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainRecyclerView.getApplicationWindowToken(), 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("recyclerViewPosition", 0);
                int idOfQuestion = data.getIntExtra("idOfQuestion", 1);
                QuestionModel questionModel = databaseAdapter.getDataForASingleRow(idOfQuestion);
                questionModelArrayList.set(position, questionModel);
                if (questionModel.getMarked() != null) {
                    if (questionModel.getMarked() == questionModel.getCorrect()) {
                        countUnattempted--;
                        countCorrect++;
                    } else {
                        countUnattempted--;
                        countIncorrect++;
                    }
                }
                correctTextView.setText(String.format("%03d", countCorrect));
                incorrectTextView.setText(String.format("%03d", countIncorrect));
                unattemptedTextView.setText(String.format("%03d", countUnattempted));
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setUpCharts() {

        List<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, 10f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "Number of Questions");


        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars


        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000);
        barChart.invalidate();
    }
}
