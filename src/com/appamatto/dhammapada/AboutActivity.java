/*
 * 2011 September 2
 * 
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal);
        setTitle("Dhammapada Reader: About");

        TextView aboutText = (TextView) findViewById(R.id.legal_text);
        BufferedReader stream;
        try {
            stream = new BufferedReader(new InputStreamReader(getAssets().open(
                            "about.txt")));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = stream.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            aboutText.setText(text.toString());
        } catch (IOException e) {
        }
    }
}
