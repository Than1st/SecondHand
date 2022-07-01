package com.group4.secondhand.ui

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.view.Window
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import java.io.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

val listCategory : MutableList<String> = ArrayList()
val listCategoryId : MutableList<Int> = ArrayList()

fun setFullScreen(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun lightStatusBar(window: Window, light: Boolean = true) {
    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = light
}

fun currency(angka: Int): String {
    val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRp = DecimalFormatSymbols()

    formatRp.currencySymbol = "Rp "
    formatRp.monetaryDecimalSeparator = ','
    formatRp.groupingSeparator = '.'

    kursIndonesia.decimalFormatSymbols = formatRp
    return kursIndonesia.format(angka).dropLast(3)
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

private fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun uriToFile(
    selectedImage: Uri,
    context: Context,
): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) {
        outputStream.write(buf, 0, len)
    }
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun convertDate(date: String): String {
    var kotlin = date
    kotlin = kotlin.drop(5)
    var bulan = kotlin.take(2)
    kotlin = kotlin.drop(3)
    val tanggal = kotlin.take(2)
    kotlin = kotlin.drop(3)
    val jam = kotlin.take(2)
    kotlin = kotlin.drop(3)
    val menit = kotlin.take(2)

    when (bulan) {
        "01" -> {
            bulan = "Jan"
        }
        "02" -> {
            bulan = "Feb"
        }
        "03" -> {
            bulan = "Mar"
        }
        "04" -> {
            bulan = "Apr"
        }
        "05" -> {
            bulan = "Mei"
        }
        "06" -> {
            bulan = "Jun"
        }
        "07" -> {
            bulan = "Jul"
        }
        "08" -> {
            bulan = "Agu"
        }
        "09" -> {
            bulan = "Sep"
        }
        "10" -> {
            bulan = "Okt"
        }
        "11" -> {
            bulan = "Nov"
        }
        "12" -> {
            bulan = "Des"
        }
    }

    return "$tanggal $bulan, $jam:$menit"


}

fun striketroughtText(tv: TextView, textChange: String): String {
    tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    return textChange
}

