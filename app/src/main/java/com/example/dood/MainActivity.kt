package com.example.dood

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var doodleView: DoodleView? = null
    private var brushSizeSeekBar: SeekBar? = null
    private var opacitySeekBar: SeekBar? = null
    private var clearButton: Button? = null
    private var colorButton: Button? = null
    private var saveButton: Button? = null
    private var loadButton: Button? = null
    private var undoButton: Button? = null
    private var redoButton: Button? = null

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
        saveButton = findViewById(R.id.btnSave)
        loadButton = findViewById(R.id.btnLoad)
        undoButton = findViewById(R.id.undoButton)
        redoButton = findViewById(R.id.redoButton)

        // Set up Clear Button functionality
        clearButton?.setOnClickListener { doodleView?.clearCanvas() }

        // Set up Brush Size SeekBar
        brushSizeSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val brushSize = (progress + 1).toFloat()
                doodleView?.setBrushSize(brushSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Set up Opacity SeekBar
        opacitySeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val opacity = progress / 100f
                doodleView?.setOpacity(opacity)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Set up Color Picker Dialog
        colorButton?.setOnClickListener {
            showColorPickerDialog()
        }

        // Set up Save Button functionality
        saveButton?.setOnClickListener {
            doodleView?.saveDrawing()
        }

        // Set up Load Button functionality
        loadButton?.setOnClickListener {
            doodleView?.loadDrawing()
        }

        // Set up Undo Button functionality
        undoButton?.setOnClickListener {
            doodleView?.undo() // Call the undo method in DoodleView
        }

        // Set up Redo Button functionality
        redoButton?.setOnClickListener {
            doodleView?.redo() // Call the redo method in DoodleView
        }
    }

    private fun showColorPickerDialog() {
        val colors = arrayOf(
            "Red", "Green", "Blue", "Yellow", "Black"
        )

        val colorValues = intArrayOf(
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick a color")
        builder.setItems(colors) { dialog: DialogInterface, which: Int ->
            val selectedColor = colorValues[which]
            doodleView?.setBrushColor(selectedColor)
        }

        builder.create().show()
    }
}
