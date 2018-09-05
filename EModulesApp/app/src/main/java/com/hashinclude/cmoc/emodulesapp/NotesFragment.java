package com.hashinclude.cmoc.emodulesapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance(QuestionModel questionModel) {
        NotesFragment fragment = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("questionModel", questionModel);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View row = inflater.inflate(R.layout.fragment_notes, container, false);
        Bundle bundle = getArguments();
        final DatabaseAdapter databaseAdapter = new DatabaseAdapter(getContext());
        final QuestionModel questionModel = bundle.getParcelable("questionModel");
        final EditText notesEditText = row.findViewById(R.id.notesEditText);
        String notes = questionModel.getNotes();
        if (TextUtils.isEmpty(notes)) {
            notesEditText.setHint("Type notes here");
        } else {
            notesEditText.setText(notes);
        }
        notesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("TEXTCHANGE", "Before Called");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("TEXTCHANGE", "On Called");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                databaseAdapter.updateNotes(questionModel.getId(), newText);
            }
        });
        return row;
    }

}
