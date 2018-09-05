package com.hashinclude.cmoc.emodulesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by harsh on 2/5/18.
 */

//The Adapter for the RecyclerView used in MainActivity
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolder> {

    LayoutInflater layoutInflater;
    Context context;
    ArrayList<QuestionModel> questionModelArrayList;

    public MainRecyclerViewAdapter(Context context, ArrayList<QuestionModel> questionModelArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.questionModelArrayList = questionModelArrayList;

    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.main_custom_row, parent, false);
        MainViewHolder holder = new MainViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        TextView mainTextView = holder.mainTextView;
        TextView timeTextView = holder.timeTextView;
        ImageView questionStatus = holder.questionStatusImageView;
        ImageView flaggedImageView = holder.flagStatusImageView;

        int questionNumber = position + 1;

        String markedAnswer = questionModelArrayList.get(position).getMarked();
        String correctAnswer = questionModelArrayList.get(position).getCorrect();
        int flagged = questionModelArrayList.get(position).getFlagged();
        String timeTaken = questionModelArrayList.get(position).getTimeTaken();

        String mainText = questionModelArrayList.get(position).getId() + ". ";
        //if the user hasn't opened the question, time taken will be NULL, given in the documentation

        //FOR STATUS OF QUESTION AND MAIN TEXT
        //if the user hasn't marked an answer, and hasn't even clicked the question(timeTaken = 0), so show unattempted sign
        if (TextUtils.isEmpty(markedAnswer) && TextUtils.isEmpty(timeTaken)) {
            GlideApp.with(context)
                    .load(R.drawable.normal_status)
                    .into(questionStatus);
            mainText += "Unattempted";
        }
        //if the user hasn't marked the answer and time is not null, meaning he opened the question, toh set a warning sign
        else if (TextUtils.isEmpty(markedAnswer) && !TextUtils.isEmpty(timeTaken)) {
            GlideApp.with(context)
                    .load(R.drawable.warning)
                    .into(questionStatus);
            mainText += "Unanswered";
        }
        //timeWon't be null if we are are, because won't update markedAnswer without updating timer
        else if (markedAnswer.equals(correctAnswer)) {
            GlideApp.with(context)
                    .load(R.drawable.success)
                    .into(questionStatus);
            //Time is surely not NULL, and marked answer is correct, so will show "Correct"
            mainText += "Correct";
        } else {
            GlideApp.with(context)
                    .load(R.drawable.error)
                    .into(questionStatus);
            //Time is surely not NULL, and marked answer is incorrect, so will show "Incorrect"
            mainText += "Incorrect";
        }

//        TIME TEXT VIEW
        if (TextUtils.isEmpty(timeTaken)) {
            timeTextView.setText("--:--");
        } else {
            timeTextView.setText(timeTaken);
        }

        mainTextView.setText(mainText);

        //FOR FLAG
        if (flagged == 0) {
            GlideApp.with(context)
                    .load(R.drawable.unflagged)
                    .into(flaggedImageView);
        } else {
            GlideApp.with(context)
                    .load(R.drawable.flagged)
                    .into(flaggedImageView);
        }

        GlideApp.with(context)
                .load(R.drawable.arrowrightcircle)
                .into(holder.rightArrowImageView);
    }

    public void updateList(ArrayList<QuestionModel> arrayList) {
        this.questionModelArrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return questionModelArrayList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView mainTextView, timeTextView;
        ImageView questionStatusImageView, flagStatusImageView, rightArrowImageView;
        LinearLayout rootOfCustomRow;

        public MainViewHolder(View itemView) {
            super(itemView);
            mainTextView = itemView.findViewById(R.id.tempTextView);
            rightArrowImageView = itemView.findViewById(R.id.rightArrow);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            questionStatusImageView = itemView.findViewById(R.id.questionStatus);
            flagStatusImageView = itemView.findViewById(R.id.flagQuestion);
            rootOfCustomRow = itemView.findViewById(R.id.rootCustomRow);
        }
    }
}
