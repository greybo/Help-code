package com.example.help_code.presentation.xlsx

import android.content.Context
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONArray


private val excelColumns = arrayOf("Id", "Name" , "Address" , "Age")
fun Context.writeToExcel(customerArray:Array<CustomerModel>) {

    val excelWorkBook = XSSFWorkbook()
    val createHelper = excelWorkBook.creationHelper
    val sheet: Sheet = excelWorkBook.createSheet()

    val headerFont = excelWorkBook.createFont()
    headerFont.color = IndexedColors.BLUE.getIndex()
    headerFont.bold

    val  headerCellStyle = excelWorkBook.createCellStyle()
    headerCellStyle.setFont(headerFont)

    val headerRow= sheet.createRow(0) //initialize 1st row

    //create 1st row
    for (col in excelColumns.indices){
        val cell = headerRow.createCell(col)
        cell.setCellValue(excelColumns[col])
        cell.cellStyle = headerCellStyle
    }

    //cell style for age
    val ageCellStyle= excelWorkBook.createCellStyle()
    ageCellStyle.dataFormat = createHelper.createDataFormat().getFormat("#")

    var rowIndex = 1
    for (customer in customerArray){
        val row = sheet.createRow(rowIndex++)
        row.createCell(0).setCellValue(customer.id)
        row.createCell(1).setCellValue(customer.name)
        row.createCell(2).setCellValue(customer.address)
        val ageCell = row.createCell(3)
        customer.age?.toDouble()?.let { ageCell.setCellValue(it) }
        ageCell.cellStyle = ageCellStyle
    }

//        val generatedExcelFile = FileOutputStream("customer.xlsx")
    val generatedExcelFile = openFileOutput("customer.xlsx", Context.MODE_PRIVATE);
    excelWorkBook.write(generatedExcelFile)
    excelWorkBook.close()
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