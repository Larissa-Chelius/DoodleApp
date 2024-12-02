package com.example.dood

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var doodleView: DoodleView? = null
    private var brushSizeSeekBar: SeekBar? = null
    private var opacitySeekBar: SeekBar? = null
    private var clearButton: Button? = null
    private var colorButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Find views by ID
        doodleView = findViewById(R.id.doodleCanvas)
        brushSizeSeekBar = findViewById(R.id.seekBarBrushSize)
        opacitySeekBar = findViewById(R.id.seekBarOpacity)
        clearButton = findViewById(R.id.btnClear)
        colorButton = findViewById(R.id.btnColor)

        // Set up Clear Button functionality
        clearButton?.setOnClickListener { doodleView?.clearCanvas() }

        // Set up Brush Size SeekBar
        brushSizeSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val brushSize = (progress + 1).toFloat() // Add 1 to avoid 0 width
                doodleView?.setBrushSize(brushSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Set up Opacity SeekBar
        opacitySeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val opacity = progress / 100f // Convert to 0-1 range
                doodleView?.setOpacity(opacity)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Set up Color Button (you can implement a color picker later)
        colorButton?.setOnClickListener {
            doodleView?.setBrushColor(-0x10000) // Example: Red
        }
    }
}
