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


        clearButton?.setOnClickListener { doodleView?.clearCanvas() }


        brushSizeSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val brushSize = (progress + 1).toFloat()
                doodleView?.setBrushSize(brushSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        opacitySeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val opacity = progress / 100f
                doodleView?.setOpacity(opacity)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        colorButton?.setOnClickListener {
            showColorPickerDialog()
        }

        saveButton?.setOnClickListener {
            doodleView?.saveDrawing()
        }

        loadButton?.setOnClickListener {
            doodleView?.loadDrawing()
        }

        undoButton?.setOnClickListener {
            doodleView?.undo()
        }

        redoButton?.setOnClickListener {
            doodleView?.redo()
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
