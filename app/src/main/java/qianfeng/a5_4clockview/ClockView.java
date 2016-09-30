package qianfeng.a5_4clockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class ClockView extends View {

    // 绘制表盘的画笔
    private Paint clockPaint;

    // 画圆心的点
    private Paint dotPaint;

    // 写字体的画笔
    private Paint numberPaint;

    //获取系统当前时间
    private Calendar calendar;

    // 时针的画笔
    private Paint hourPaint;

    // 分针的画笔
    private Paint minutePaint;

    // 秒针的画笔
    private Paint secondPaint;


    private static final int DEFAULTWIDTH = 250; //px
    private static final int DEFAULTHEIGHT = 250;

    // 动态创建时调用这个构造方法
    public ClockView(Context context) {
        this(context, null);
    }

    // 在xml中创建时调用这个构造方法
    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // 该方法由系统调用，用来给控件默认的属性，(看Button类)
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) { // 所有的构造方法经过修改后都会调用到这个构造方法，只需在这里初始化画笔就好了。android源码也是这样来写的
        super(context, attrs, defStyleAttr);
        init(); //

    }

    private void init() {
        clockPaint = new Paint();

        // 去锯齿
        clockPaint.setAntiAlias(true);

        // 设置画笔颜色
        clockPaint.setColor(Color.GREEN);
        // 设置画笔的风格为描边，还有另外两个是 充满(fill) 以及充满和描边()
        clockPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔宽度
        clockPaint.setStrokeWidth(4); // 注意画笔是有宽度的

        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(Color.RED);

        numberPaint = new Paint();
        numberPaint.setAntiAlias(true);
        // 设置文本的对齐方式
        numberPaint.setTextAlign(Paint.Align.CENTER);
        // 设置文本的字体大小
        numberPaint.setTextSize(20);
        numberPaint.setColor(Color.RED);

        // 时分秒动起来之准备画笔
        hourPaint = new Paint();
        hourPaint.setColor(Color.BLUE);
        hourPaint.setStrokeWidth(15);

        minutePaint = new Paint();
        minutePaint.setColor(Color.GREEN);
        minutePaint.setStrokeWidth(11);

        secondPaint = new Paint();
        secondPaint.setColor(Color.RED);
        secondPaint.setStrokeWidth(7);

    }

    /**
     * widthMeasureSpec 是一个32位的int类型数据，其中高2位表示测量模式，低30位表示测量值
     *
     * MeasureSpec.EXACTLY: match_parent 或者 指定具体的dp值，就是这种模式
     * MeasureSpec.AT_MOST: wrap_content 时，就是这种模式
     * MeasureSpec.UNSPECIFIED: 子控件可以无限大
     *              当父容器是ScrollView这些可以根据内容来拉长的父容器，子控件在里面就是无限大，而不受屏幕宽高影响

     * @param widthMeasureSpec 宽度测量规格
     * @param heightMeasureSpec  高度测量规格
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode)
        {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = DEFAULTWIDTH;
                    break;
            case MeasureSpec.EXACTLY:
                break;
        }
        switch (heightMode)
        {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = DEFAULTHEIGHT;
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        // 宽度和高度只取最小的那个
        widthSize = heightSize = widthSize < heightSize ?  widthSize : heightSize;
        // 指定最终确定的值，传递到onDraw方法里面的getMeasureWidth()方法中
        setMeasuredDimension(widthSize,heightSize);
    }

    // 控件的绘制 要调用onDraw()方法
    // super.onDraw(canvas);表示调用父类的绘制方法，因为View本身不具备显示效果，所以这里可以删掉
    // 但是父类如果是EditView之类的控件，就必须加上这行super.onDraw(canvas);否则就没有EditView的样式效果
    @Override
    protected void onDraw(Canvas canvas) { // canvas:是画布，系统只会在两个地方提供画布，其余地方都要自己new，注意自己new的时候要提供一个bitmap给它，它才根据你提供的bitmap来new一个canvas
        // 系统只在两个地方给你提供画布，自定义View以及自定义ServiceView里面给你提供画布，不用自己new
        int width = getWidth();
        int height = getHeight();
        //1.2表示表盘的圆心坐标
        //3.表盘半径
        //4.绘制表盘的画笔
        int radius = width / 2 - 2; // 为什么半径要额外减2？ 因为绘制的时候，是半径里外各占2，所以要减2，注意这个细节
        canvas.drawCircle(width / 2, height / 2, radius, clockPaint);

        canvas.drawCircle(width / 2, height / 2, 20, dotPaint);

        // 开始旋转圆来画直线和写文本啦！
        for (int i = 1; i < 13; i++) {
            canvas.save(); // 保存当前画布的状态
            //1.画布旋转的角度
            //2.3  画布旋转中心点
            canvas.rotate(i * 30, width / 2, height / 2);
            //画表盘上的短线
            canvas.drawLine(radius, 0, radius, 15, clockPaint);

            //画表盘上的数字
            //1.要绘制的文本
            //2.X的坐标是这样解析：如果TextAlign为left则表示文本绘制的起始位置，如果TextAlign为center则表示文本X轴的中心点
            //3.基线
            //4.画笔
            canvas.drawText(i + "", radius, 40, numberPaint);// 这个方法绘制文本内容是受numberPaint画笔里面的一个属性的影响，是setTextAlign()属性的影响
            // 恢复画布旋转前的状态,一般save()和restore()是一起使用的
            canvas.restore();
        }

        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // 时分秒动起来


        canvas.save();// 记录 画布旋转前的位置，   minute / 60f * 30---> 这是时针在当前这个小时到下个小时的偏移量，肯定取决于当前的分钟数，而且分钟数肯定是要除以一个小时的分钟数，得到比例再乘上30度.
        canvas.rotate(hour * 30 + minute / 60f * 30, radius, radius); // hour*30代表逆时针旋转30度倍数，再在12点(因为这个点的坐标最好找,drawCycle()时指定的坐标最好找)的那个位置画，
        // 画好之后再给你旋转到你原来的位置，再显示出来。
        canvas.drawLine(radius, radius + 45, radius, 150, hourPaint);
        canvas.restore();// 将刚才记录的画布旋转前的位置还原
//        LayoutInflater.from(getContext()).inflate()
        canvas.save();
        canvas.rotate(minute * 6 + second / 60f * 6, radius, radius);
        canvas.drawLine(radius, radius + 60, radius, 130, minutePaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(second * 6, radius, radius);
        canvas.drawLine(radius, radius + 75, radius, 100, secondPaint);
        canvas.restore();

        // 时分秒动起来，只需要一个方法，它会自己更新画布，相当于重新绘制一遍，但注意不要自己手动调用onDraw()方法，这样画布是不能给你调用的，它内部是会自己调用那张画布的。
        postInvalidateDelayed(1000); // 延迟1000毫秒就重新绘制一遍,这样时间是精确的，不会有执行代码消耗掉一点点时间之说。因为你获取时间来决定角度的时候，获取的那个时间就是你要显示出去的那一瞬间的时间。

//        invalidate();  这个方法重新绘制画布有个致命缺陷，就是会不断执行这行代码，而不是一秒后执行。会消耗点不必要的性能.
    }
}
