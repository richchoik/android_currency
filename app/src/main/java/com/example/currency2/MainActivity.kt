package com.example.currency2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    lateinit var editTextSource: EditText
    lateinit var editTextDestination: EditText
    lateinit var spinnerSource: Spinner
    lateinit var spinnerDestination: Spinner
    var isSourceUpdating = false
    var isDestinationUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSource = findViewById(R.id.editTextSource)
        editTextDestination = findViewById(R.id.editTextDestination)
        spinnerSource = findViewById(R.id.spinnerSource)
        spinnerDestination = findViewById(R.id.spinnerDestination)

        val currencies = arrayOf("USD", "EUR", "VND")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSource.adapter = adapter
        spinnerDestination.adapter = adapter

        editTextSource.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isDestinationUpdating) {
                    isSourceUpdating = true
                    updateConversion(isSource = true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isSourceUpdating = false
            }
        })

        editTextDestination.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isSourceUpdating) {
                    isDestinationUpdating = true
                    updateConversion(isSource = false)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isDestinationUpdating = false
            }
        })

        spinnerSource.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (!isDestinationUpdating) {
                    isSourceUpdating = true
                    updateConversion(isSource = true)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerDestination.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (!isSourceUpdating) {
                    isDestinationUpdating = true
                    updateConversion(isSource = false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateConversion(isSource: Boolean) {
        if (isSource) {
            val sourceAmount = editTextSource.text.toString().toDoubleOrNull() ?: return
            val sourceCurrency = spinnerSource.selectedItem.toString()
            val destinationCurrency = spinnerDestination.selectedItem.toString()
            val conversionRate = getConversionRate(sourceCurrency, destinationCurrency)
            val destinationAmount = sourceAmount * conversionRate
            editTextDestination.setText(destinationAmount.toString())
        } else {
            val destinationAmount = editTextDestination.text.toString().toDoubleOrNull() ?: return
            val destinationCurrency = spinnerDestination.selectedItem.toString()
            val sourceCurrency = spinnerSource.selectedItem.toString()
            val conversionRate = getConversionRate(destinationCurrency, sourceCurrency)
            val sourceAmount = destinationAmount * conversionRate
            editTextSource.setText(sourceAmount.toString())
        }
    }

    private fun getConversionRate(source: String, destination: String): Double {
        // Giả lập tỷ giá chuyển đổi. Thay thế bằng dữ liệu thực hoặc gọi API
        return when (source) {
            "USD" -> when (destination) {
                "EUR" -> 0.85
                "VND" -> 23000.0
                else -> 1.0
            }
            "EUR" -> when (destination) {
                "USD" -> 1.18
                "VND" -> 27000.0
                else -> 1.0
            }
            "VND" -> when (destination) {
                "USD" -> 0.000043
                "EUR" -> 0.000037
                else -> 1.0
            }
            else -> 1.0
        }
    }
}
