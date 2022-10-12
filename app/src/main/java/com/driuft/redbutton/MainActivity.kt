package com.driuft.redbutton

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.driuft.redbutton.databinding.ActivityMainBinding
import com.driuft.redbutton.ui.FinalActivity
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val redButton get() = binding.redButton as Button
    private val cyanButton get() = binding.cyanButton
    private val tinyYellowButton get() = binding.tinyYellowButton
    private val superSecretView get() = binding.SUPERSECRETPOWERLEVELVIEW
    private val sharedPref: SharedPreferences by lazy {
        getPreferences(MODE_PRIVATE)
    }

    private var superSecretCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureUI()
        configureToasty()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        redButton.setOnClickListener { congratulations() }
        cyanButton.setOnClickListener { otherButtonClicked() }
        tinyYellowButton.setOnClickListener { tinyButtonClicked() }
        tinyYellowButton.setOnLongClickListener { tinyButtonLongClicked() }
        superSecretView.setOnClickListener { unclickableButton() }
    }

    /**
     * Show a clue when the Cyan button is clicked.
     */
    private fun otherButtonClicked() {
        Toasty.info(
            this,
            getString(R.string.toast_wrong_button_error),
            Toasty.LENGTH_LONG
        ).show()
    }

    /**
     * Shows a clue when the tiny Yellow button is clicked.
     */
    private fun tinyButtonClicked() {
        Toasty.warning(
            this,
            getString(R.string.toast_do_not_press_and_hold),
            Toasty.LENGTH_LONG
        ).show()
    }

    /**
     * Tiny yellow button was held
     * I wonder what will happen next...
     */
    private fun tinyButtonLongClicked(): Boolean {
        // Save state
        with (sharedPref.edit()) {
            putBoolean(getString(R.string.red_button_shown_key), true)
            apply()
        }

        // Update visibility
        configureUI()

        // TODO: MAKE THE RED BUTTON CLICKABLE
        redButton.isClickable = false

        return true
    }

    /**
     * Something is preventing me from clicking...
     * Maybe I should check out [R.layout.activity_main] to look for anything suspicious.
     */
    private fun unclickableButton() {
        superSecretCounter += 1
        // Show a clue after three (3) taps
        if (superSecretCounter > 3) {
            Toasty.warning(
                this,
                getString(R.string.toast_unclickable_warning),
                Toasty.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Congratulations!!!
     * You DID it! The [redButton] seems pleased.
     * Creepy, but cool.
     */
    private fun congratulations() {
        val partyEmoji = String(Character.toChars(0x1F389))
        Toasty.success(
            this,
            getString(R.string.toast_congrats, partyEmoji),
            Toasty.LENGTH_LONG
        ).show()

        // Navigate to new activity to CELEBRATE!
        val i = Intent(this, FinalActivity::class.java)
        startActivity(i)
        finish()
    }

    /**
     * Configures visibility if red button has already been shown.
     */
    private fun configureUI() {
        val defaultValue = false
        val redButtonShown = sharedPref.getBoolean(getString(R.string.red_button_shown_key), defaultValue)

        if (redButtonShown) {
            tinyYellowButton.visibility = View.GONE
            redButton.visibility = View.VISIBLE
        }
    }

    /**
     * Configures [Toasty] to not allow multiple Toasts to be queued.
     */
    private fun configureToasty() {
        Toasty.Config.getInstance()
            .allowQueue(false)
            .apply()
    }
}