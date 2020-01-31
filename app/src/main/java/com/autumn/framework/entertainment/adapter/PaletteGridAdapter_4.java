package com.autumn.framework.entertainment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autumn.framework.entertainment.manager.Data_manager_4;
import com.autumn.reptile.R;

/**
 * @创建者 CSDN_LQR
 * @描述 调色板网格适配器
 */
public class PaletteGridAdapter_4 extends RecyclerView.Adapter<PaletteGridAdapter_4.PaletteGridViewHolder> {

    final String[] content = Data_manager_4.getContent();
    /*final int[] picResId = new int[]{R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R.mipmap.p4, R.mipmap.p5,
            R.mipmap.p6, R.mipmap.p43, R.mipmap.p44};*/

    @Override
    public PaletteGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_palette_content, parent, false);
        return new PaletteGridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaletteGridViewHolder holder, final int position) {
        holder.mTvTitle.setText(content[position]);
    }

    private int generateTransparentColor(float percent, int rgb) {
        int red = Color.red(rgb);
        int green = Color.green(rgb);
        int blue = Color.blue(rgb);
        int alpha = Color.alpha(rgb);
        alpha = (int) (percent * alpha);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public int getItemCount() {
        return content.length;
    }

    class PaletteGridViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        private Context mContext;

        public PaletteGridViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
