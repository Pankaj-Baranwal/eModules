package com.hashinclude.cmoc.emodulesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

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
    public ArrayList<String> arrayList;
    TopicListViewAdapter topicListViewAdapter;
    SlidingUpPanelLayout slidingUpPanelLayout;

    TextView correctTextView, incorrectTextView, unattemptedTextView;
    int countCorrect, countIncorrect, countUnattempted;

    LinearLayout normalToolbar, dragView, searchView;
    EditText searchStringEditText;
    ImageView searchIconImageView, searchToolbarBackArrow;
    TextView searchTextView;
    ListView topicsListView;
    FrameLayout rootListViewForSearch;

    String textSearchedFor;
    int[] optionSelected;

    LinearLayout afterSearchToolbar;
    ImageView afterSearchBackArrow;
    QuestionModel currentModelBeforeClicked, afterClickQuestionModel;
    TextView afterSearchTextView;

    //    FOR CHARTS :
    BarChart barChart, stackedBarChart;
    PieChart pieChart;
    int[] colorArray = {
            Color.parseColor("#26C6DA"),
            Color.parseColor("#FFF176"),
            Color.parseColor("#FF7043"),
            Color.parseColor("#9CCC65")};
    float pieCorrect = 0, pieIncorrect = 0, pieUnattempted = 0, pieUnanswered = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        context = this;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        optionSelected = new int[10];
        for (int i = 0; i < 10; i++) {
            optionSelected[i] = 0;
        }
        arrayList = new ArrayList<>();
        arrayList.add("Flagged Questions");
        arrayList.add("Correct Questions");
        arrayList.add("Incorrect Questions");
        arrayList.add("Unattempted Questions");
        arrayList.add("Quant Problem Solving (QPS)");
        arrayList.add("Quant Data Sufficiency (QDS)");
        arrayList.add("Verbal Reading Comprehension (VRC)");
        arrayList.add("Verbal Critical Reasoning (VCR)");
        arrayList.add("Verbal Sentence Correction (VSC)");

        databaseAdapter = new DatabaseAdapter(context);
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
        searchTextView = findViewById(R.id.searchTextButton);
        topicsListView = findViewById(R.id.listView);
        rootListViewForSearch = findViewById(R.id.rootListView);

        afterSearchToolbar = findViewById(R.id.afterSearchToolBar);
        afterSearchBackArrow = findViewById(R.id.afterSearchToolBarBackArrow);
        afterSearchTextView = findViewById(R.id.afterSearchTextView);

//        FOR CHARTS
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        stackedBarChart = findViewById(R.id.stackedBarChart);


        topicListViewAdapter = new TopicListViewAdapter();
        topicsListView.setAdapter(topicListViewAdapter);

        //HANDLE THE CLICK JUST OUTSIDE LISTVIEW TO AVOID OPENING OF QUESTION UNKNOWINGLY
        rootListViewForSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    pieChart.animateY(1500);
                    pieChart.invalidate();
                    stackedBarChart.animateY(2000);
                    stackedBarChart.invalidate();
                    barChart.animateY(2500);
                    barChart.invalidate();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(panel.getApplicationWindowToken(), 0);
            }
        });


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
                currentModelBeforeClicked = questionModelArrayList.get(position);
                intent.putExtra("questionModel", currentModelBeforeClicked);
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
        afterSearchTextView.setText(questionModelArrayList.size() + " Result(s)");
        afterSearchBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(30);
                normalToolbar.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                rootListViewForSearch.setVisibility(View.GONE);
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
                rootListViewForSearch.setVisibility(View.GONE);

                for (int i = 0; i < 10; i++) {
                    optionSelected[i] = 0;
                }
                topicListViewAdapter = new TopicListViewAdapter();
                topicsListView.setAdapter(topicListViewAdapter);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                vibrator.vibrate(40);
            }
        });

        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView checkIcon = view.findViewById(R.id.checkIconImageView);
                if (optionSelected[i] != 1) {
                    checkIcon.setVisibility(View.VISIBLE);
                    optionSelected[i] = 1;
                } else {
                    optionSelected[i] = 0;
                    checkIcon.setVisibility(View.INVISIBLE);
                }

            }
        });

        searchIconImageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(50);
                normalToolbar.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                rootListViewForSearch.setVisibility(View.VISIBLE);
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(20);
                boolean somethingSelected = false;
                try {
                    String temp = searchStringEditText.getText().toString();
                    if (!TextUtils.isEmpty(temp)) {
                        String toSearch = temp.toLowerCase();
                        textSearchedFor = toSearch;
                        somethingSelected = true;
                    }
                    for (int i = 0; i < 10; i++) {
                        if (optionSelected[i] == 1) {
                            somethingSelected = true;
                            break;
                        }
                    }
                    ArrayList<QuestionModel> questionModels = getDataForCurrentSearch();
                    questionModelArrayList = questionModels;
                    adapter.updateList(questionModels);
                } catch (Exception e) {
                    Log.d("EXCEPTION", e.toString());

                }
                if (somethingSelected) {
                    normalToolbar.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    rootListViewForSearch.setVisibility(View.GONE);
                    afterSearchToolbar.setVisibility(View.VISIBLE);
                    afterSearchTextView.setText(questionModelArrayList.size() + " Result(s)");

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);
                }
            }
        });
        searchStringEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    boolean somethingSelected = false;
                    try {
                        String temp = searchStringEditText.getText().toString();
                        if (!TextUtils.isEmpty(temp)) {
                            String toSearch = temp.toLowerCase();
                            textSearchedFor = toSearch;
                        }
                        for (int i = 0; i < 10; i++) {
                            if (optionSelected[i] == 1) {
                                somethingSelected = true;
                                break;
                            }
                        }
                        ArrayList<QuestionModel> questionModels = getDataForCurrentSearch();
                        questionModelArrayList = questionModels;
                        adapter.updateList(questionModelArrayList);
                    } catch (Exception e) {
                        Log.d("EXCEPTION", e.toString());

                    }
                    if (somethingSelected) {
                        normalToolbar.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        rootListViewForSearch.setVisibility(View.GONE);
                        afterSearchToolbar.setVisibility(View.VISIBLE);
                        afterSearchTextView.setText(questionModelArrayList.size() + " Result(s)");
                        for (int i = 0; i < 10; i++) {
                            optionSelected[i] = 0;
                        }
                        topicListViewAdapter = new TopicListViewAdapter();
                        topicsListView.setAdapter(topicListViewAdapter);

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
            rootListViewForSearch.setVisibility(View.GONE);
            afterSearchToolbar.setVisibility(View.GONE);

            for (int i = 0; i < 10; i++) {
                optionSelected[i] = 0;
            }
            topicListViewAdapter = new TopicListViewAdapter();
            topicsListView.setAdapter(topicListViewAdapter);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0);
        } else if (afterSearchToolbar.getVisibility() == View.VISIBLE) {
            normalToolbar.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            rootListViewForSearch.setVisibility(View.GONE);
            afterSearchToolbar.setVisibility(View.GONE);
            ArrayList<QuestionModel> questionModels = databaseAdapter.getAllData();
            questionModelArrayList = questionModels;

            for (int i = 0; i < 10; i++) {
                optionSelected[i] = 0;
            }
            topicListViewAdapter = new TopicListViewAdapter();
            topicsListView.setAdapter(topicListViewAdapter);

            adapter.updateList(questionModels);
        } else {
            super.onBackPressed();
        }
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
                afterClickQuestionModel = databaseAdapter.getDataForASingleRow(idOfQuestion);
                questionModelArrayList.set(position, afterClickQuestionModel);
                if (TextUtils.isEmpty(currentModelBeforeClicked.getMarked()) &&
                        !TextUtils.isEmpty(afterClickQuestionModel.getMarked())) {
                    if (afterClickQuestionModel.getMarked().equals(afterClickQuestionModel.getCorrect())) {
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
                resetCharts();
                currentModelBeforeClicked = afterClickQuestionModel;
            }
        }
    }

    public void resetCharts() {
        List<PieEntry> pieEntries = new ArrayList<>();
        if (!TextUtils.isEmpty(currentModelBeforeClicked.getMarked())) {

        } else {
            if (!TextUtils.isEmpty(afterClickQuestionModel.getMarked())) {
                if (afterClickQuestionModel.getMarked().equals(afterClickQuestionModel.getCorrect())) {
                    if (TextUtils.isEmpty(currentModelBeforeClicked.getTimeTaken())) {
                        pieUnattempted--;
                        pieCorrect++;
                    } else {
                        pieUnanswered--;
                        pieCorrect++;
                    }
                } else {
                    if (TextUtils.isEmpty(currentModelBeforeClicked.getTimeTaken())) {
                        pieUnattempted--;
                        pieIncorrect++;
                    } else {
                        pieUnanswered--;
                        pieIncorrect++;
                    }
                }
            } else {
                if (TextUtils.isEmpty(currentModelBeforeClicked.getTimeTaken()))
                    pieUnattempted--;
                pieUnanswered++;
            }
        }

        pieEntries.add(new PieEntry(pieUnattempted, "Unattempted"));
        pieEntries.add(new PieEntry(pieUnanswered, "Unanswered"));
        pieEntries.add(new PieEntry(pieIncorrect, "Incorrect"));
        pieEntries.add(new PieEntry(pieCorrect, "Correct"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setXValuePosition(null);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setColors(colorArray);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setEntryLabelColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

        //BAR CHART
        BarChartTask task = new BarChartTask();
        task.execute();

//        STACKED BAR CHART
        StackedBarChartTask stackedBarChartTask=new StackedBarChartTask();
        stackedBarChartTask.execute();
    }

    public void setUpCharts() {
        setUpPieChart();
        setUpStackedBarChart();
        setUpBarChart();
    }


    public void setUpPieChart() {

//        PIE CHART
        List<PieEntry> pieEntries = new ArrayList<>();

        ArrayList<QuestionModel> temp = databaseAdapter.getAllData();
        for (int i = 0; i < temp.size(); i++) {
            if (TextUtils.isEmpty(temp.get(i).getMarked()) && TextUtils.isEmpty(temp.get(i).getTimeTaken())) {
                pieUnattempted++;
            } else if (TextUtils.isEmpty(temp.get(i).getMarked()) && !TextUtils.isEmpty(temp.get(i).getTimeTaken())) {
                pieUnanswered++;
            } else if (!TextUtils.isEmpty(temp.get(i).getMarked()) && temp.get(i).getMarked().equals(temp.get(i).getCorrect())) {
                pieCorrect++;
            } else {
                pieIncorrect++;
            }
        }

        pieEntries.add(new PieEntry(pieUnattempted, "Unattempted"));
        pieEntries.add(new PieEntry(pieUnanswered, "Unanswered"));
        pieEntries.add(new PieEntry(pieIncorrect, "Incorrect"));
        pieEntries.add(new PieEntry(pieCorrect, "Correct"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setXValuePosition(null);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setColors(colorArray);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setEntryLabelColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.invalidate();
    }

    public void setUpBarChart() {

        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            barEntries.add(new BarEntry((float) i, databaseAdapter.averageTimeTaken(i)));
        }

        BarDataSet set = new BarDataSet(barEntries, "Average time per topic (sec.)");
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

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) value;
                if (v == 0) {
                    return "QPS";
                }
                if (v == 1) {
                    return "QDS";
                }
                if (v == 2) {
                    return "VRC";
                }
                if (v == 3) {
                    return "VCR";
                }
                if (v == 4) {
                    return "VSC";
                }
                return "";
            }
        });
        barChart.invalidate();
    }

    public void setUpStackedBarChart() {

//        STACKED BAR CHART
        ArrayList<BarEntry> stackedBarEntry = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            float[] status = databaseAdapter.getFromTopic(i);

            stackedBarEntry.add(new BarEntry(
                    i, status));
        }

        BarDataSet set1;

        set1 = new BarDataSet(stackedBarEntry, "");
        set1.setDrawIcons(false);
        set1.setColors(colorArray);
        set1.setStackLabels(new String[]{"Unattempted", "Unanswered", "Incorrect", "Correct"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData stackedBarData = new BarData(dataSets);
        stackedBarData.setValueTextColor(Color.BLACK);

        stackedBarChart.setData(stackedBarData);
        stackedBarChart.getDescription().setEnabled(false);
        stackedBarChart.setFitBars(true);

        stackedBarChart.invalidate();
        XAxis stackerBarChartXAxis = stackedBarChart.getXAxis();
        stackerBarChartXAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) value;
                if (v == 0) {
                    return "QPS";
                }
                if (v == 1) {
                    return "QDS";
                }
                if (v == 2) {
                    return "VRC";
                }
                if (v == 3) {
                    return "VCR";
                }
                if (v == 4) {
                    return "VSC";
                }
                return "";
            }
        });
    }

    public ArrayList<QuestionModel> getDataForCurrentSearch() {
        ArrayList<QuestionModel> questionModels;

        questionModels = databaseAdapter.getAllMatching(textSearchedFor, optionSelected);

        for (int i = 0; i < 10; i++) {
            optionSelected[i] = 0;
        }
        topicListViewAdapter = new TopicListViewAdapter();
        topicsListView.setAdapter(topicListViewAdapter);

        return questionModels;
    }

    class TopicListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = getLayoutInflater().inflate(R.layout.list_custom_row, null);
            TextView textView = row.findViewById(R.id.textViewTopic);
            ImageView imageView = row.findViewById(R.id.checkIconImageView);

            imageView.setVisibility(View.INVISIBLE);
            textView.setText(arrayList.get(i));
            return row;
        }


    }

    class StackedBarChartTask extends AsyncTask<Void, Void, Void> {
        ArrayList<BarEntry> stackedBarEntry;
        BarDataSet set1;
        ArrayList<IBarDataSet> dataSets;
        BarData stackedBarData;

        @Override
        protected Void doInBackground(Void... voids) {
            stackedBarEntry = new ArrayList<>();
            dataSets = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                float[] status = databaseAdapter.getFromTopic(i);
                stackedBarEntry.add(new BarEntry(
                        i, status));
            }

            set1 = new BarDataSet(stackedBarEntry, "");
            set1.setDrawIcons(false);
            set1.setColors(colorArray);
            set1.setStackLabels(new String[]{"Unattempted", "Unanswered", "Incorrect", "Correct"});
            dataSets.add(set1);

            stackedBarData = new BarData(dataSets);
            stackedBarData.setValueTextColor(Color.BLACK);
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            stackedBarChart.setData(stackedBarData);
            stackedBarChart.notifyDataSetChanged();
            stackedBarChart.invalidate();

        }
    }

    class BarChartTask extends AsyncTask<Void, Void, Void> {
        List<BarEntry> barEntries = new ArrayList<>();
        BarDataSet set;
        BarData data;

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < 5; i++) {
                barEntries.add(new BarEntry((float) i, databaseAdapter.averageTimeTaken(i)));
            }
            set = new BarDataSet(barEntries, "Average time per topic (sec.)");
            data = new BarData(set);
            data.setBarWidth(0.9f); // set custom bar width

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            barChart.setData(data);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
    }
}
