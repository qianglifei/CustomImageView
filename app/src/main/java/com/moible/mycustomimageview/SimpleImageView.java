package com.moible.mycustomimageview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义ImageView,用于显示图片
 *
 * 1.继承View 创建自定义控件
 * 2.如果有需要自定义View属性，也就是在Values/attrs.xml中定义属性集
 * 3.在Xml 中引用控件，设置属性
 * 4.在代码中读取xml中的属性，初始化视图
 * 5.测量视图大小
 * 6.绘制视图内容
 */
public class SimpleImageView extends View {
    //画笔
    private Paint mBitmapPaint;
    //图片的Drawable ：可绘制物，表示一些可以绘制在Canvas的对象
    private Drawable mDrawable;
    //View 的宽度
    private int mWidth;
    //View 的高度
    private int mHeight;

    public SimpleImageView(Context context) {
        super(context, null);
    }

    public SimpleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //根据属性初始化
        initAttrs(attrs);
    }

    /**
     * 初始化控件属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            //无类型数组
            TypedArray array = null;
            try {
                array = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleImageView);
                //根据图片id，获取到Drawable对象
                mDrawable = array.getDrawable(R.styleable.SimpleImageView_src);
                //测量Drawable对象的宽，高
                measureDrawable();
            } finally {
                if (array != null) {
                    array.recycle();
                }
            }

        }
    }

    private void measureDrawable() {
        if (mDrawable == null) {
            throw new RuntimeException("drawable 不能为空!");
        }
        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置View的宽和高为图片的宽高
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mDrawable){
            return;
        }
        //绘制图片
        canvas.drawBitmap(drawable2Bitmap(mDrawable),getLeft(),getTop(),mBitmapPaint);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
}
