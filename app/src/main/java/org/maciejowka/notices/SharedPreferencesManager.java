package org.maciejowka.notices;

import android.content.SharedPreferences;

interface SharedPreferencesManager {

    void saveToSharedPreferences(SharedPreferences sharedPreferences);

    void loadFromSharedPreferences(SharedPreferences sharedPreferences);
}
