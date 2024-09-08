package com.mv.attendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.style.Color
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.mv.attendance.Mode2QRCodeProperties.decodeScannedString
import kotlin.concurrent.fixedRateTimer
import kotlin.math.roundToInt

class Mode2TakeAttendanceShowQRCode : AppCompatActivity() {

    private lateinit var qrCodeIV: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode2_take_attendance_show_qrcode)
        /*window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )*/

        qrCodeIV = findViewById(R.id.idTVQRCodeMode2TakeAttendance)
        val titleOfLectureAttendanceSession:String = intent.getStringExtra("Title").toString()
        val teacherOfLectureAttendanceSession:String = intent.getStringExtra("Teacher_PRN").toString()
        val divisionOfLectureAttendanceSession:String = intent.getStringExtra("Division").toString()

        var currentTime = (System.currentTimeMillis() / 1000).toInt()
        var finalString = decodeScannedString("{}|$titleOfLectureAttendanceSession|$teacherOfLectureAttendanceSession|$divisionOfLectureAttendanceSession|$currentTime|", -5)
        val data: QrData = QrData.Text(finalString)
        var options = QrVectorOptions.Builder()
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

        var drawable = QrCodeDrawable(applicationContext, data, options)
        qrCodeIV.setImageDrawable(drawable)

        fixedRateTimer("timer", false, 0L, 1000) {
            runOnUiThread {
                //tvTime.text = SimpleDateFormat("dd MMM - HH:mm", Locale.US).format(Date())
                var currentTime = (System.currentTimeMillis() / 1000).toInt()
                finalString = decodeScannedString("{}|$titleOfLectureAttendanceSession|$teacherOfLectureAttendanceSession|$divisionOfLectureAttendanceSession|$currentTime|", -5)
                val data: QrData = QrData.Text(finalString)
                options = QrVectorOptions.Builder()
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

                drawable = QrCodeDrawable(applicationContext, data, options)
                qrCodeIV.setImageDrawable(drawable)

            }
        }



    }
}