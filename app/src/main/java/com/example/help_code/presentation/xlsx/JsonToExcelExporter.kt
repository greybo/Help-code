package com.example.help_code.presentation.xlsx

import android.content.Context
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONArray
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class JsonToExcelExporter {


}

fun Context.jsonToExcelFetch() {
    // Provide the path to your JSON file
    val jsonFilePath = "country_state.json" //"path/to/your/data.json"

    // Load JSON data from file
//    val jsonArray = readJsonFile(jsonFilePath)
    val jsonArray = readJsonFileAssets(jsonFilePath)

    // Create Excel workbook and sheet
//    val excelWorkBook = SXSSFWorkbook(/*SXSSFWorkbook.DEFAULT_WINDOW_SIZE*/)
    val excelWorkBook = XSSFWorkbook()
    val createHelper = excelWorkBook.creationHelper
    val sheet: Sheet = excelWorkBook.createSheet("createSheet")

    // Create header row
    createHeaderRow(sheet, jsonArray)

    // Add data rows
    addDataRows(sheet, jsonArray)

    // Write the workbook content to a file
    try {
        val f = File("test.xlsx")
//        val outputStream = FileOutputStream("output.xlsx")
        val outputStream = openFileOutput("output.xlsx", Context.MODE_PRIVATE);

        excelWorkBook.write(outputStream)
        excelWorkBook.close()
        outputStream.close()

        println("Excel file exported successfully!")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun Context.readJsonFileAssets(jsonFilePath: String): JSONArray {
    try {
        assets.open(jsonFilePath).use { inputStream ->
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            val jsonData = String(buffer)
            return JSONArray(jsonData)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return JSONArray()
    }
}
private fun readJsonFile(jsonFilePath: String): JSONArray {
    try {
        FileInputStream(jsonFilePath).use { inputStream ->
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            val jsonData = String(buffer)
            return JSONArray(jsonData)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return JSONArray()
    }
}

private fun createHeaderRow(sheet: Sheet, jsonArray: JSONArray) {
    if (jsonArray.length() == 0) {
        return  // No data to create headers
    }

    val firstObject = jsonArray.getJSONObject(0)
    val headerRow = sheet.createRow(0)

    var cellIndex = 0
    for (key in firstObject.keys()) {
        val cell = headerRow.createCell(cellIndex++)
        cell.setCellValue(key)
    }
}

private fun addDataRows(sheet: Sheet, jsonArray: JSONArray) {
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val dataRow = sheet.createRow(i + 1)

        var cellIndex = 0
        for (key in jsonObject.keys()) {
            val cell = dataRow.createCell(cellIndex++)
            val value = jsonObject[key]

            if (value is String) {
                cell.setCellValue(value)
            } else if (value is Number) {
                cell.setCellValue(value.toDouble())
            } else {
                // Handle other data types as needed
                cell.setCellValue(value.toString())
            }
        }
    }
}