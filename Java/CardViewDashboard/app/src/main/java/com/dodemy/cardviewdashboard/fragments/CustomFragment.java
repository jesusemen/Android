package com.dodemy.cardviewdashboard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.dodemy.cardviewdashboard.adapters.PhrasesAdapter;
import com.dodemy.cardviewdashboard.R;
import com.dodemy.cardviewdashboard.classes.Phrases;

import java.util.ArrayList;
import java.util.Locale;
//import java.util.Map;

public class CustomFragment extends Fragment implements TextToSpeech.OnInitListener {
    private PhrasesAdapter adapter;
    private ListView listView;
    private FloatingActionButton addPhraseButton;
    private TextView phraseTitleTextView;
    public TextToSpeech mTextToSpeech;
    private ArrayList<String> phrases;
    private static final String PHRASE_LABEL = " Phrases";
    private static final String CATEGORY_KEY = "categories";
    public static String categories = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);


        //if we selecte a category sent from home fragment else we got here through menu
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(CATEGORY_KEY)) {
            categories = arguments.getString(CATEGORY_KEY).toLowerCase();
        } else {
            Resources res = view.getResources();
            categories = res.getStringArray(R.array.categories)[0].toLowerCase();
        }

        Phrases.fetchAllPhrases(view.getContext());
        phrases = Phrases.getAllPhrases().get(categories);

        phraseTitleTextView = view.findViewById(R.id.label_phrases_txtview);

        phraseTitleTextView.setText(categories.substring(0, 1).toUpperCase() +
                categories.substring(1) + PHRASE_LABEL);
        addPhraseButton = view.findViewById(R.id.add_phrases_btn);
        //

        // setting local for text to speech
        mTextToSpeech = new TextToSpeech(getActivity(), this);

//                new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    mTextToSpeech.setLanguage(Locale.US);
//                } else
//                    mTextToSpeech = null;
//                Log.e("CustomFragment", "Failed to initialize the TextToSpeech engine");
//            }
//        });
        //setting adapter and listview
        adapter = new PhrasesAdapter(getContext(), R.layout.entry_item, phrases);
        listView = view.findViewById(R.id.phrases_list);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);

        //activating text to speech when user selects item in listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
                String text = phrases.get(position);
                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        //button to display alert dialog box to add new phrase
        addPhraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhraseDialogBox();
            }
        });
        return view;
    }


    /**
     * alert dialogbox for add new item to listview
     */
    private void addPhraseDialogBox() {
        // Creating alert Dialog with one Button
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.dialogBox);
        alertDialog.setTitle("Add Phrase");
        final EditText input = new EditText(getContext());
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_comment_black_24dp);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String item = input.getText().toString();
                        Phrases.allPhrases.get(categories).add(item);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                    }
                }).create();

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeech.setLanguage(Locale.US);
        } else
            mTextToSpeech = null;
        Log.e("CustomFragment", "Failed to initialize the TextToSpeech engine");
    }
}
