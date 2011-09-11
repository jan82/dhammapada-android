
package com.appamatto.dhammapada;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Translation {
    public static final Translation[] translations = new Translation[] {
            new Translation("Acharya Buddharakkhita", "buddharakkhita"),
            new Translation("Thanissaro Bhikkhu", "thanissaro"),
    };

    private static final String TRANSLATION_ID = "TRANSLATION_ID";

    public final String name;
    public final String id;

    public Translation(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public static int getIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String id = prefs.getString(TRANSLATION_ID, null);
        for (int i = 0; i < translations.length; i++) {
            if (translations[i].id.equals(id)) {
                return i;
            }
        }
        return 0;
    }

    public static Translation get(Context context) {
        return translations[getIndex(context)];
    }

    public static void set(Context context, Translation translation) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(TRANSLATION_ID, translation.id).commit();
    }
}
