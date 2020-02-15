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
    private val n by lazy { findPreference<EditTextPreference>("n")!! }
    private val wss by lazy { findPreference<CheckBoxPreference>("wss")!! }
    private val wssPath by lazy { findPreference<EditTextPreference>("wss-path")!! }
    private val sv by lazy { findPreference<CheckBoxPreference>("sv")!! }
    private val mux by lazy { findPreference<CheckBoxPreference>("mux")!! }
    private val muxMaxStream by lazy { findPreference<EditTextPreference>("mux-max-stream")!! }

    //geek's
    private val timeout by lazy { findPreference<EditTextPreference>("timeout")!! }


    //debug
    private val receivedStr by lazy { findPreference<Preference>("receivedStr")!! }
    private val fallbackDNS by lazy { findPreference<EditTextPreference>("fallback-dns")!! }
    private val verbose by lazy { findPreference<CheckBoxPreference>("verbose")!! }


    override val options
        get() = PluginOptions().apply {
            this.id = "mostlstunnel"
            if (n.text.isNotBlank()) this["n"] = n.text

            if (wss.isChecked) this["wss"] = null
            if (wssPath.text.isNotBlank()) this["wss-path"] = wssPath.text
            if (sv.isChecked) this["sv"] = null
            if (mux.isChecked) this["mux"] = null
            if (muxMaxStream.text.isNotBlank()) this["mux-max-stream"] = muxMaxStream.text

            if (timeout.text.isNotBlank()) this["timeout"] = timeout.text

            if (fallbackDNS.text.isNotBlank()) this["fallback-dns"] = fallbackDNS.text
            if (verbose.isChecked) this["verbose"] = null
        }

    override fun onInitializePluginOptions(options: PluginOptions) {
        n.text = options["n"] ?: ""

        wss.isChecked = options.containsKey("wss")
        wssPath.isEnabled = wss.isChecked
        wssPath.text = options["wss-path"] ?: ""
        sv.isChecked = options.containsKey("sv")
        mux.isChecked = options.containsKey("mux")
        muxMaxStream.isEnabled = mux.isChecked
        muxMaxStream.text = options["mux-max-stream"] ?: ""

        timeout.text = options["timeout"] ?: ""

        fallbackDNS.text = options["fallback-dns"] ?: ""
        verbose.isChecked = options.containsKey("verbose")
        receivedStr.summary = options.toString()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.tls_tunnel_settings)
        n.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }

        wss.setOnPreferenceChangeListener { _, newValue ->
            wssPath.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }

        wssPath.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
        mux.setOnPreferenceChangeListener { _, newValue ->
            muxMaxStream.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }
        muxMaxStream.setOnBindEditTextListener { it.inputType = InputType.TYPE_CLASS_NUMBER }
        timeout.setOnBindEditTextListener { it.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL }

        fallbackDNS.setOnBindEditTextListener { it.inputType = InputType.TYPE_TEXT_VARIATION_URI }
    }
}
