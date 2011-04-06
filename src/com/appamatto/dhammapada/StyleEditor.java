/*
 * 2011 April 5
 *
 * The author disclaims copyright to this source code.
 */
package com.appamatto.dhammapada;

import android.view.View;
import android.os.Bundle;

public class StyleEditor extends DhammapadaActivity {
    /* use custom preference activity to show a live preview */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
    }
}
