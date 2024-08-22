package com.example.help_code.presentation.xlsx

import android.content.Context
import org.zeroturnaround.zip.ZipUtil
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun Context.fileToZip(
    nameZip: String = zipFileName,
    vararg nameFiles: String = arrayOf(exelFileName),
) {
    toZip(
        pathToFiles = nameFiles.map { name ->
            File(filesDir.absoluteFile, name).path
        },
        pathToZip = File(filesDir.absoluteFile, nameZip).path
    )
}

fun toZip(
    pathToFiles: List<String>,
    pathToZip: String
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

fun Context.packZip(){
    ZipUtil.packEntry( File(filesDir.absoluteFile, exelFileName), File(filesDir.absoluteFile,zipFileName))

}