package com.hashinclude.cmoc.emodulesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harsh on 2/7/18.
 */

public class QuestionModel implements Parcelable{
    private int id;
    private String query, solution, correct, topic, notes, marked, timeTaken;
    private int flagged;

    public QuestionModel() {

    }

    protected QuestionModel(Parcel in) {
        id = in.readInt();
        query = in.readString();
        solution = in.readString();
        correct = in.readString();
        topic = in.readString();
        notes = in.readString();
        marked = in.readString();
        timeTaken = in.readString();
        flagged = in.readInt();
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }


    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getMarked() {
        return marked;
    }

    public void setMarked(String marked) {
        this.marked = marked;
    }


    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getFlagged() {
        return flagged;
    }

    public void setFlagged(int flagged) {
        this.flagged = flagged;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(query);
        parcel.writeString(solution);
        parcel.writeString(correct);
        parcel.writeString(topic);
        parcel.writeString(notes);
        parcel.writeString(marked);
        parcel.writeString(timeTaken);
        parcel.writeInt(flagged);
    }
}
