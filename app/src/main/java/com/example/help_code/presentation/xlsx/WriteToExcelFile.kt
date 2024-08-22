package com.example.help_code.presentation.xlsx

import android.content.Context
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


fun Context.writeToExcel(array: Array<ToExelModel>) {

    val excelColumns = listOf("ID", "SKU", "Название", "К-во", "Закупочная цена")

    val excelWorkBook = XSSFWorkbook()
    val createHelper = excelWorkBook.creationHelper
    val sheet: Sheet = excelWorkBook.createSheet("Worksheet")

    val headerFont = excelWorkBook.createFont()
    headerFont.color = IndexedColors.BLUE.getIndex()
    headerFont.bold

    val headerCellStyle = excelWorkBook.createCellStyle()
    headerCellStyle.setFont(headerFont)

    val headerRow = sheet.createRow(0) //initialize 1st row

    //create 1st row
    for (col in excelColumns.indices) {
        val cell = headerRow.createCell(col)
        cell.setCellValue(excelColumns[col])
        cell.cellStyle = headerCellStyle
    }

    //cell style for age
    val countCellStyle = excelWorkBook.createCellStyle()
    countCellStyle.dataFormat = createHelper.createDataFormat().getFormat("#")
    val priceCellStyle = excelWorkBook.createCellStyle()
    priceCellStyle.dataFormat = createHelper.createDataFormat().getFormat("#,##")

    var rowIndex = 1
    for (customer in array) {
        val row = sheet.createRow(rowIndex++)
        row.createCell(0).setCellValue(customer.id)
        row.createCell(1).setCellValue(customer.sku)
        row.createCell(2).setCellValue(customer.name)
        row.createCell(3).apply {
            setCellValue(customer.count.toDouble() ?: 0.0)
            cellStyle = countCellStyle
        }
        row.createCell(4).apply {
            setCellValue(customer.price)
            cellStyle = priceCellStyle
        }
    }
    getFilesDir()
    val generatedExcelFile = openFileOutput("customer.xlsx", Context.MODE_PRIVATE)
    excelWorkBook.write(generatedExcelFile)
    excelWorkBook.close()
}

fun fileToZip(
    pathToFiles: Array<String> = arrayOf(
        "/home/matte/theres_no_place.png",
        "/home/matte/vladstudio_the_moon_and_the_ocean_1920x1440_signed.jpg"
    ),
    pathToZip: String = "/home/matte/Desktop/test.zip"
) {

    ZipOutputStream(BufferedOutputStream(FileOutputStream(pathToZip))).use { out ->
        for (file in pathToFiles) {
            FileInputStream(file).use { fi ->
                BufferedInputStream(fi).use { origin ->
                    val entry = ZipEntry(file.substring(file.lastIndexOf("/")))
                    out.putNextEntry(entry)
                    origin.copyTo(out, 1024)
                }
            }
        }
    }
}