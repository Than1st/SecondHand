package com.group4.secondhand.ui

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.group4.secondhand.R
import com.group4.secondhand.data.model.ResponseNotification
import java.io.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val listCategory : MutableList<String> = ArrayList()
val listCategoryId : MutableList<Int> = ArrayList()

fun loadingBar(context: Context): ProgressDialog {
    return ProgressDialog(context)
}

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

fun showToastSuccess(view: View, message: String, color: Int) {
    val snackBarView =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val layoutParams = ActionBar.LayoutParams(snackBarView.view.layoutParams)
    snackBarView.setAction(" ") {
        snackBarView.dismiss()
    }
    val textView =
        snackBarView.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_close, 0)
    textView.compoundDrawablePadding = 16
    layoutParams.gravity = Gravity.TOP
    layoutParams.setMargins(32, 150, 32, 0)
    snackBarView.view.setPadding(24, 16, 0, 16)
    snackBarView.view.setBackgroundColor(color)
    snackBarView.view.layoutParams = layoutParams
    snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBarView.show()
}

