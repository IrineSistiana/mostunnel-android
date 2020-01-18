/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2019-2020 by IrineSistiana                                        *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.github.IrineSistiana.plugin.mostunnel.tls

import android.os.Bundle
import android.text.InputType
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import com.github.IrineSistiana.plugin.mostunnel.R
import com.github.IrineSistiana.plugin.mostunnel.general.GeneralConfigFragment
import com.github.shadowsocks.plugin.PluginOptions
import kotlin.collections.set

class ConfigFragment : GeneralConfigFragment() {
    //required
    private val serverName by lazy { findPreference<EditTextPreference>("serverName")!! }

    //optional
    private val wssMode by lazy { findPreference<CheckBoxPreference>("wssMode")!! }
    private val path by lazy { findPreference<EditTextPreference>("path")!! }
    private val insecureSkipVerify by lazy { findPreference<CheckBoxPreference>("insecureSkipVerify")!! }
    private val mux by lazy { findPreference<CheckBoxPreference>("mux")!! }
    private val maxStream by lazy { findPreference<EditTextPreference>("maxStream")!! }

    //geek's
    private val buffSize by lazy { findPreference<EditTextPreference>("buffSize")!! }
    private val noDelay by lazy { findPreference<CheckBoxPreference>("noDelay")!! }
    private val mss by lazy { findPreference<EditTextPreference>("mss")!! }
    private val idleTimeout by lazy { findPreference<EditTextPreference>("idleTimeout")!! }


    //debug
    private val receivedStr by lazy { findPreference<Preference>("receivedStr")!! }
    private val fallbackDNS by lazy { findPreference<EditTextPreference>("fallbackDNS")!! }
    private val verbose by lazy { findPreference<CheckBoxPreference>("verbose")!! }


    override val options
        get() = PluginOptions().apply {
            this.id = "mostlstunnel"
            if (serverName.text.isNotBlank()) this["n"] = serverName.text

            if (wssMode.isChecked) this["wss"] = null
            if (path.text.isNotBlank()) this["path"] = path.text
            if (insecureSkipVerify.isChecked) this["sv"] = null
            if (mux.isChecked) this["mux"] = null
            if (maxStream.text.isNotBlank()) this["max-stream"] = maxStream.text

            if (buffSize.text.isNotBlank()) this["buff"] = buffSize.text
            if (noDelay.isChecked) this["no-delay"] = null
            if (mss.text.isNotBlank()) this["mss"] = mss.text

            if (idleTimeout.text.isNotBlank()) this["timeout"] = idleTimeout.text

            if (fallbackDNS.text.isNotBlank()) this["fallback-dns"] = fallbackDNS.text
            if (verbose.isChecked) this["verbose"] = null
        }

    override fun onInitializePluginOptions(options: PluginOptions) {
        serverName.text = options["n"] ?: ""

        wssMode.isChecked = options.containsKey("wss")
        path.isEnabled = wssMode.isChecked
        path.text = options["path"] ?: ""
        insecureSkipVerify.isChecked = options.containsKey("sv")
        mux.isChecked = options.containsKey("mux")
        maxStream.isEnabled = mux.isChecked
        maxStream.text = options["max-stream"] ?: ""

        buffSize.text = options["buff"] ?: ""
        noDelay.isChecked = options.containsKey("no-delay")
        mss.text = options["mss"] ?: ""
        idleTimeout.text = options["timeout"] ?: ""

        fallbackDNS.text = options["fallback-dns"] ?: ""
        verbose.isChecked = options.containsKey("verbose")
        receivedStr.summary = options.toString()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.tls_tunnel_settings)
        serverName.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }

        wssMode.setOnPreferenceChangeListener { _, newValue ->
            path.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }

        path.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        mux.setOnPreferenceChangeListener { _, newValue ->
            maxStream.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }
        maxStream.setOnBindEditTextListener { it.inputType = InputType.TYPE_CLASS_NUMBER }

        buffSize.setOnBindEditTextListener { it.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL }
        idleTimeout.setOnBindEditTextListener { it.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL }

        fallbackDNS.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
    }
}
