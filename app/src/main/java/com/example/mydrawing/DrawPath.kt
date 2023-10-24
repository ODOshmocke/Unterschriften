package com.example.mydrawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs

class DrawPath @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {



    private var paint : Paint?=null
    private var path : Path?=null
    private var mPath : Path?=null
    private var pathList = ArrayList<PaintPath>()
    private var undoPathList = ArrayList<PaintPath>()
    private var mX: Float? = null
    private var mY: Float? = null
    private var TouchTolerance : Float = 4f


    init {
        paint = Paint()
        paint!!.color = Color.BLACK
        paint!!.strokeWidth = 10f
        paint!!.style=Paint.Style.STROKE
        paint!!.isAntiAlias = true
        setBackgroundColor(Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        if (pathList.size > 0) {
            for(path in pathList){
                canvas.drawPath(path.path, paint!!)
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val xPos : Float = event!!.x
        val yPos : Float = event.y


        when(event.action){
            MotionEvent.ACTION_DOWN->{
                touchStart(xPos, yPos)
                invalidate()
            }
            MotionEvent.ACTION_MOVE->{
                touchMove(xPos, yPos)
                invalidate()
            }
            MotionEvent.ACTION_UP->{
                touchUp()
                invalidate()
            }
            else->{

            }
        }
        invalidate()
        return true
    }

    private fun touchStart(xPos: Float, yPos: Float) {
        // explain the code below
        // if the pathList has more than 0 items, then remove the last item from the pathList and add it to the undoPathList
    mPath = Path()
        val paintPath = PaintPath(mPath!!)
        pathList.add(paintPath)
        println(pathList)
        mPath!!.reset()
        mPath!!.moveTo(xPos, yPos)
        mX = xPos
        mY = yPos
    }

    private fun touchMove(xPos: Float, yPos: Float) {
        // explain the code below
        // if the distance between the current xPos and the last xPos is greater than the touch tolerance
        val dX: Float = abs(xPos - mX!!)
        val dY: Float = abs(yPos - mY!!)
        if (dX >= TouchTolerance || dY >= TouchTolerance) {
            mPath!!.quadTo(mX!!, mY!!, (xPos + mX!!) / 2, (yPos + mY!!) / 2)
            mX = xPos
            mY = yPos
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX!!, mY!!)
    }

    fun setUndo(){

        // if the pathList has more than 0 items, then remove the last item from the pathList and add it to the undoPathList
        if(pathList.size > 0){
            undoPathList.add(pathList.removeAt(pathList.size - 1))
            invalidate()
        }
    }
    fun setRedo(){
        if(undoPathList.size > 0){
            pathList.add(undoPathList.removeAt(undoPathList.size - 1))
            invalidate()
        }
    }

    fun setdelete() {
        if (pathList.size > 0) {
            pathList.clear()
            undoPathList.clear()
            invalidate()
        }
    }
    // Add a function to capture and save the drawn image
    fun setSaveSend() {
        // Create a bitmap with the same dimensions as the view
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a canvas to draw on the bitmap
        val canvas = Canvas(bitmap)

        // Capture the drawing by drawing the view onto the canvas
        draw(canvas)

        // Now, 'bitmap' contains the captured drawing

        // Send the image to your server or desired destination via a network request
        sendImageToServer(bitmap)
    }

    private fun sendImageToServer(bitmap: Bitmap) {
        // Use a library like Retrofit or HttpURLConnection to send the image to your server.
        // Here's a simplified example using HttpURLConnection:

        val url = "http://localhost:8000/unterschrift/"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"

        // Set up the connection properties and headers (if needed)
        // ...

        // Convert the Bitmap to a byte array
        val outputStream = connection.outputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Write the image data to the output stream
        outputStream.write(byteArray)
        outputStream.close()

        // Handle the server response or errors here
        // ...

        // Disconnect the connection
        connection.disconnect()
    }

}