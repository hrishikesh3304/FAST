package com.mv.attendance

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.style.Color
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import se.simbio.encryption.Encryption
import java.lang.Math.abs
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class GiveAttendance : AppCompatActivity() {
    private lateinit var qrCodeIV: ImageView
        //private val qrgEncoder: QRGEncoder? = null
    private val bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_give_attendance)
        qrCodeIV = findViewById(R.id.idTVQRCode)


        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        /*editTextName.setText(sh.getString("Name", ""))
        editTextRollNo.setText(sh.getString("Roll No", ""))
        editTextDivision.setText(sh.getString("Div", ""))*/
        val current_time = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted_date = current_time.format(formatter)

        var finalString = "{}|" + formatted_date + "|" + sh.getString("Roll No", "") + "|" + sh.getString("Div", "") + "|" + sh.getString("Name", "")
        val key = formatted_date
        val salt = "Mith"
        val iv = ByteArray(16)
        val encryption = Encryption.getDefault(key, salt, iv)
        finalString = encryption.encryptOrNull(finalString)
        val data: QrData = QrData.Text(finalString)
        val options = QrVectorOptions.Builder()
            .padding(.3f)
            /*.logo(
                QrVectorLogo(
                    drawable = DrawableSource
                        .Resource(R.drawable.ic_baseline_sim_card_24),
                    size = .3f,
                    padding = QrVectorLogoPadding.Natural(.05f),
                    shape = QrVectorLogoShape
                        .Circle
                )
            )*/
            .colors(
                QrVectorColors(
                    dark = QrVectorColor
                        .Solid(Color(0xff111111)),
                    ball = QrVectorColor.Solid(
                        ContextCompat.getColor(applicationContext, R.color.black_shade_1)
                    )
                )
            )
            .shapes(
                QrVectorShapes(
                    darkPixel = QrVectorPixelShape
                        .RoundCorners(.5f),
                    ball = QrVectorBallShape
                        .RoundCorners(.25f),
                    frame = QrVectorFrameShape
                        .RoundCorners(.25f),
                )
            )
            .build()

        val drawable = QrCodeDrawable(applicationContext, data, options)


        if(kotlin.math.abs(sh.getLong("savedTime", 167666442) - getTime()) > 20) {     ////////Change time here!!
            qrCodeIV.setImageDrawable(drawable)
        }
        Log.d("QWERT", "Saved - " + sh.getLong("savedTime", 167666442).toString() +"   Current - " + getTime())

    }


    @Throws(Exception::class)
    private fun getTime(): Long {
        /*val url = "https://time.is/Unix_time"
        val doc: Document = Jsoup.parse(URL(url).openStream(), "UTF-8", url)
        val tags = arrayOf(
            "div[id=time_section]",
            "div[id=clock0_bg]"
        )
        var elements: Elements = doc.select(tags[0])
        for (i in tags.indices) {
            elements = elements.select(tags[i])
        }
        return elements.text().toLong()*/
        return System.currentTimeMillis() / 1000L
    }
}