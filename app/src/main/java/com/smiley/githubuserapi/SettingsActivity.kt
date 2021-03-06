package com.smiley.githubuserapi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var reminder: String
        private lateinit var reminderPreference: SwitchPreference
        private lateinit var language: String
        private lateinit var languagePreference: Preference
        private lateinit var reminderReceiver: ReminderReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            reminderReceiver = ReminderReceiver()
            init()
            setSummaries()
        }

        private fun init() {
            reminder = resources.getString(R.string.reminder)
            language = getString(R.string.language)
            reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference
            languagePreference = findPreference<Preference>(language) as Preference
        }
        private fun setSummaries() {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            languagePreference.intent = mIntent
            val sh = preferenceManager.sharedPreferences
            if (reminderPreference.isChecked) {
                sh.getBoolean(reminder, false)
                val title = resources.getString(R.string.title_reminder)
                val repeatTime = resources.getString(R.string.set_time)
                val repeatMessage = resources.getString(R.string.mess_reminder)
                reminderReceiver.setRepeatingAlarm(context, title, repeatTime, repeatMessage)
            }else{
                sh.getBoolean(reminder, true)
                reminderReceiver.cancelAlarm(context)
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }
        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            if (key == reminder) {
                reminderPreference.isChecked = sharedPreferences.getBoolean(reminder, false)
            }
        }

    }
}