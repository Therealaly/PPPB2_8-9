package com.example.customview1a;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.res.ResourcesCompat;

public class EditTextWithClear extends AppCompatEditText {
    //untuk extend perlu generate constructor agar dapat diapply
    Drawable mClearButtonImage;
    Boolean rtlActive;

    private void init(){
       // rtlActive = getResources().getBoolean(R.bool.rtlActive);
        mClearButtonImage = ResourcesCompat.getDrawable
                (getResources(), R.drawable.ic_clear_opaque_24dp, null);
        // mengambil drawable opaque
        // dimasukkan ke setiap method yang digenerate

        // memberikan listener untuk setiap perubahan pada edit text yg menyangkut teks
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showClearButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // digunakan untuk memastikan apaila posisi trtentu diklik (lokasi tanda silang)
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(getCompoundDrawablesRelative()[2] != null) {
                    float lft_clearButtonStart =
                                (getWidth() - getPaddingEnd() - mClearButtonImage.getIntrinsicWidth());
                    float rgt_clearButtonStart =
                                (getPaddingEnd() + mClearButtonImage.getIntrinsicWidth());
                        // mendapatkan letak dari clear button
                    boolean isClearButtonClicked = false;
                    // letak dibatasi pada batas-batas gambar clear button
                    // apabila daerah clear button disentuh, dianggap edit text sedang disentuh

                    if (ViewUtils.isLayoutRtl(getRootView())) {
                        if (motionEvent.getX() < rgt_clearButtonStart) {
                            isClearButtonClicked = true;
                        }
                    } else {
                        if (motionEvent.getX() > lft_clearButtonStart) {
                            isClearButtonClicked = true;
                        }
                    }

                    if(isClearButtonClicked){
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            mClearButtonImage = ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_clear_black_24dp, null);
                            showClearButton();
                            // misal lagi down diganti jadi hitam (posisi awal)
                        }
                        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                            mClearButtonImage = ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_clear_opaque_24dp, null);
                            // apabila posisi up (posisi selain awal)
                            getText().clear();
                            hideClearButton();
                            return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public EditTextWithClear(@NonNull Context context) {
        super(context);
        init();
    }

    public EditTextWithClear(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithClear(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // membuat method show clear button dan hide clear button
    private void showClearButton(){
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mClearButtonImage, null);
        // digunakan untuk menempelkan komponen. mau ditempel di sebelah top, left, right, bottom
        // misal mau ditempatkan di sebelah kanan, maka diisi bagian right
    }

    private void hideClearButton(){
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        // untuk menghilangkan komponen drawable maka diset null semua
    }
}
