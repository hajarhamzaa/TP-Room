package com.example.roomwordsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.roomwordsample.adapter.WordListAdapter;
import com.example.roomwordsample.databinding.ActivityMainBinding;
import com.example.roomwordsample.databinding.ContentMainBinding;
import com.example.roomwordsample.model.Word;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WordViewModel mWordViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ajout du RecyclerView
        WordListAdapter mAdapter = new WordListAdapter();
        binding.contentMain.recyclerview.setAdapter(mAdapter);
        binding.contentMain.recyclerview.setHasFixedSize(true);

        setSupportActionBar(binding.toolbar);

        // Obtenir le ViewModel
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        // Observer les données LiveData et mettre à jour l'adaptateur
        mWordViewModel.getAllWords().observe(this, words -> {
            mAdapter.setWords(words);
        });

        // 🔴 Ajout du clic long pour suppression
        mAdapter.setOnItemLongClickListener(word -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Supprimer ce mot ?")
                    .setMessage("Voulez-vous vraiment supprimer \"" + word.getWord() + "\" ?")
                    .setPositiveButton("Supprimer", (dialog, which) -> {
                        mWordViewModel.delete(word);
                        Toast.makeText(this, "Mot supprimé", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        // 🔵 Ajout du clic simple pour modification
        mAdapter.setOnItemClickListener(word -> {
            final EditText input = new EditText(this);
            input.setText(word.getWord());

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Modifier le mot")
                    .setView(input)
                    .setPositiveButton("Enregistrer", (dialog, which) -> {
                        String newWordText = input.getText().toString().trim();
                        if (!newWordText.isEmpty()) {
                            word.setWord(newWordText); // Nécessite un setWord() dans Word.java
                            mWordViewModel.update(word);
                            Toast.makeText(this, "Mot modifié", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
