/*
 * 2011 September 2
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;

public class StyleEditor extends DhammapadaActivity implements StyleAdapter {
    private static StyleField[] fields = new StyleField[] {
        new BooleanField("chapters", "Show Chapters"),
        new BooleanField("ribbon", "Show Bookmark Ribbon"),
    };

    private SQLiteDatabase db;
    private Style style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        long id = getIntent().getLongExtra("id", -1);
        db = new DBHelper(this).getWritableDatabase();
        style = Style.get(db, id);
        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        for (StyleField field : fields) {
            linear.addView(field.inflate(this, linear, inflater));
            inflater.inflate(R.layout.divider, linear);
        }
        ScrollView scroller = new ScrollView(this);
        scroller.addView(linear);
        setContentView(scroller);
    }

    /* use reflection to set fields */
    public void setField(String field, boolean value) {
    }
}

interface StyleAdapter {
    public void setField(String field, boolean value);
}

abstract class StyleField {
    public final String field;

    public StyleField(String field) {
        this.field = field;
    }

    public abstract View inflate(Context context, ViewGroup parent, LayoutInflater inflater);
}

class BooleanField extends StyleField {
    public final String text;

    public BooleanField(String field, String text) {
        super(field);
        this.text = text;
    }

    public View inflate(final Context context, ViewGroup parent, LayoutInflater inflater) {
        CheckBox check = (CheckBox) inflater.inflate(R.layout.boolean_field, parent, false);
        check.setText(text);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((StyleAdapter) context).setField(field, isChecked);
            }
        });
        return check;
    }
}
