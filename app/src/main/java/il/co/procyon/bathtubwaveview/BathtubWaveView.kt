package il.co.procyon.bathtubwaveview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Shader
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


class BathtubWaveView : View {

    private var mOneThirdCircle: Path? = null
    private var mTwoThirdCircle: Path? = null
    private var mCirclePaint: Paint? = null
    //    Point mCenterPoint;
    internal var mOneThidPoint: Point = Point()
    internal var mTwoThirdPoint: Point = Point()

    private var wave: Path? = null

    //    Point mTrackingPoint;
    private var mTrackingPaint: Paint? = null

    private var mOneThirdTrackingPoint: Point? = null
    private var mTwoThirdTrackingPoint: Point? = null
    private var mTrigPointPaint: Paint? = null


    private val tempPoint = FloatArray(2)
    private var mRadius: Float = 0.toFloat()
    private var mWaveGradientPaint: Paint? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()

    }


    private fun init() {

        mRadius = Utils.convertDpToPixel(60f, context)
        mOneThirdCircle = Path()
        mTwoThirdCircle = Path()
        wave = Path()
        mOneThidPoint = Point()
        mTwoThirdPoint = Point()

        mOneThirdTrackingPoint = Point()
        mTwoThirdTrackingPoint = Point()


        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint!!.color = Color.GRAY
        mCirclePaint!!.style = Paint.Style.STROKE
        mCirclePaint!!.strokeWidth = Utils.convertDpToPixel(1f, context)

        mTrackingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTrackingPaint!!.color = Color.RED
        mTrackingPaint!!.style = Paint.Style.FILL_AND_STROKE

        mTrigPointPaint = Paint(mTrackingPaint)
        mTrigPointPaint!!.color = Color.GREEN


        mWaveGradientPaint = Paint()
        mWaveGradientPaint!!.style = Paint.Style.FILL


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //mCenterPoint.set(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        mOneThidPoint.set((measuredWidth * (1f / 3f)).toInt(), measuredHeight / 2)
        mTwoThirdPoint.set((measuredWidth * (2f / 3f)).toInt(), measuredHeight / 2)

        mWaveGradientPaint!!.shader = LinearGradient(
                0f, 0f,
                measuredWidth.toFloat(), measuredHeight.toFloat(),
                Color.parseColor("#66efc9"), Color.parseColor("#ff8f77"), Shader.TileMode.CLAMP)

        createAnimator()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        wave!!.reset()


        wave!!.moveTo(0f, (height / 2).toFloat())
        wave!!.cubicTo(mOneThirdTrackingPoint!!.x.toFloat(), mOneThirdTrackingPoint!!.y.toFloat(),
                mTwoThirdTrackingPoint!!.x.toFloat(), mTwoThirdTrackingPoint!!.y.toFloat(),
                width.toFloat(), (height / 2).toFloat())

        wave!!.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
        wave!!.lineTo(0f, measuredHeight.toFloat())
        wave!!.close()


        //        mOneThirdCircle.reset();
        //        mOneThirdCircle.addCircle(mOneThidPoint.x,
        //                mOneThidPoint.y,
        //                mRadius,
        //                Path.Direction.CW);
        //        mOneThirdCircle.addCircle(mTwoThirdPoint.x,
        //                mTwoThirdPoint.y,
        //                mRadius,
        //                Path.Direction.CCW);
        //
        //
        canvas.drawPath(wave!!, mWaveGradientPaint!!)
        //
        //        canvas.drawPath(mOneThirdCircle, mCirclePaint);
        //
        //
        //        canvas.drawCircle(mOneThirdTrackingPoint.x, mOneThirdTrackingPoint.y, Utils.convertDpToPixel(5, getContext()), mTrigPointPaint);
        //        canvas.drawCircle(mTwoThirdTrackingPoint.x, mTwoThirdTrackingPoint.y, Utils.convertDpToPixel(5, getContext()), mTrigPointPaint);


    }

    private fun createAnimator() {
        val circleAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)

        circleAnimator.addUpdateListener { valueAnimator ->
            val location = valueAnimator.animatedFraction


            mOneThirdTrackingPoint!!.x = (mOneThidPoint.x + mRadius * Math.sin(2.0 * Math.PI * location.toDouble() + Math.toDegrees(90.0))).toInt()
            mOneThirdTrackingPoint!!.y = (mOneThidPoint.y + mRadius * Math.cos(2.0 * Math.PI * location.toDouble() + Math.toDegrees(90.0))).toInt()

            mTwoThirdTrackingPoint!!.x = (mTwoThirdPoint.x + mRadius * Math.sin(2.0 * Math.PI * location.toDouble())).toInt()
            mTwoThirdTrackingPoint!!.y = (mTwoThirdPoint.y + mRadius * Math.cos(2.0 * Math.PI * location.toDouble())).toInt()

            invalidate()
        }
        circleAnimator.duration = 4000
        circleAnimator.interpolator = LinearInterpolator()
        circleAnimator.repeatCount = ValueAnimator.INFINITE

        circleAnimator.start()
    }
}
