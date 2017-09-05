package com.example.android.booklists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {


    public String userQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText queryView = (EditText) findViewById(R.id.search_query);
        final String LOG_TAG = SearchActivity.class.getName();

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Intent to open BookListActivity
                Intent BookListIntent = new Intent(SearchActivity.this, BookListActivity.class);
                String Data= queryView.getText().toString();
                if (!Data.isEmpty()) {
                    BookListIntent.putExtra("query", Data);
                    startActivity(BookListIntent);
                }
                else {
                    Toast.makeText(getBaseContext(),R.string.ToastMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

