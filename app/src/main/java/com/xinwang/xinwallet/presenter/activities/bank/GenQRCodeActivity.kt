package com.xinwang.xinwallet.presenter.activities.bank

import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.xinwang.xinwallet.R
import kotlinx.android.synthetic.main.activity_gen_qrcode.*
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import com.google.zxing.EncodeHintType
import com.google.zxing.Result
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*
import me.dm7.barcodescanner.zxing.ZXingScannerView




class GenQRCodeActivity : AppCompatActivity(),ZXingScannerView.ResultHandler {


    private var zXingScannerView: ZXingScannerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_qrcode)

        val string = "{" +
                "type:黃" +
                "generator:陳" +
                "payer:df," +
                "payee:df," +
                "currency:32," +
                "amount:234," +
                "message:095r," +
                "payerOTP:erf" +
                "}"
        genQRcode(string)
    }

 fun scan(view: View){
     zXingScannerView=ZXingScannerView(this)
     setContentView(zXingScannerView)
     zXingScannerView!!.setResultHandler(this)
     zXingScannerView!!.startCamera()
 }

    override fun handleResult(result: Result?) {
        Toast.makeText(this,result!!.getText(),Toast.LENGTH_SHORT).show()
        zXingScannerView!!.resumeCameraPreview(this)

    }

    override fun onPause() {
        super.onPause()
        zXingScannerView!!.stopCamera()
    }
    private fun genQRcode(string: String) {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8")
        // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
        // 設定 QR code 容錯率為 H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
        val write = MultiFormatWriter()
        val matrax = write.encode(string, BarcodeFormat.QR_CODE, 600, 600,hints)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(matrax)
        qrCode.setImageBitmap(addLogo(bitmap))
    }

    private fun addLogo(bitmapCode: Bitmap): Bitmap {
        val bitmapLogo = BitmapFactory.decodeResource(resources, R.drawable.uwallet96)
        val qrCodeWidth = bitmapCode.width
        val qrCodeHeight = bitmapCode.height
        val logoWidth = bitmapLogo.width
        val logoHeight = bitmapLogo.height

        val blankBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(blankBitmap)

        canvas.drawBitmap(bitmapCode, 0f, 0f, null)
        canvas.save(Canvas.ALL_SAVE_FLAG)
        var scaleSize = 1.0f
        while (logoWidth / scaleSize > qrCodeWidth / 5 || logoHeight / scaleSize > qrCodeHeight / 5) {
            scaleSize *= 2f
        }
        val sx = 2f / scaleSize
        canvas.scale(sx, sx, (qrCodeWidth / 2).toFloat(), (qrCodeHeight / 2).toFloat())
        canvas.drawBitmap(bitmapLogo, (qrCodeWidth - logoWidth) / 2.toFloat(), (qrCodeHeight - logoHeight) / 2.toFloat(), null)

        canvas.restore()
        return blankBitmap
    }

}
