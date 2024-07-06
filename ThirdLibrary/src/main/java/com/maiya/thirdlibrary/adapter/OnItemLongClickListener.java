package com.maiya.thirdlibrary.adapter;

import android.view.View;

/**
 * Created by WQ on 16.1.11.
 * item长按事件，请不要改成kotlin，不然就不能在用到它的地方用lambda了
 */
public interface OnItemLongClickListener {
    void onLongClick(View v, int position);
}