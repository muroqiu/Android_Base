package com.kevin.vension.demo.v_custom.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.vension.demo.R;
import com.kevin.vension.demo.v_custom.models.SaleEntity;
import com.vension.customview.bulletinview.BulletinAdapter;

import java.util.List;


/**
 * SaleAdapter
 * Created by bakumon on 17-3-31.
 */

public class SaleAdapter extends BulletinAdapter<SaleEntity> {


    public SaleAdapter(Context context, List<SaleEntity> data) {
        super(context, data);
    }

    @Override
    public View getView(int position) {
        View view = getRootView(R.layout.item_sale);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_sale);
        TextView tVSaleTitle = (TextView) view.findViewById(R.id.tv_sale_title);
        TextView tVSalePrice = (TextView) view.findViewById(R.id.tv_sale_price);
        TextView tVSaleTag = (TextView) view.findViewById(R.id.tv_sale_tag);

        SaleEntity saleEntity = mData.get(position);
        imageView.setImageResource(R.mipmap.sale0);
        tVSaleTitle.setText(saleEntity.saleTitle);
        tVSalePrice.setText(saleEntity.salePrice);
        tVSaleTag.setText(saleEntity.saleTag);
        return view;
    }
}
