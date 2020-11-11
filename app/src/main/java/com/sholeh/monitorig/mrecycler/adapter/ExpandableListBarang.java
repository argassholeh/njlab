package com.sholeh.monitorig.mrecycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sholeh.monitorig.R;
import com.sholeh.monitorig.mrecycler.model.ChildModel;
import com.sholeh.monitorig.mrecycler.model.ParentModel;

import java.util.HashMap;
import java.util.List;

public class ExpandableListBarang extends BaseExpandableListAdapter {

    Context context;
    List<ParentModel> listParen;
    HashMap<ParentModel, List<ChildModel>> listChild;


    public ExpandableListBarang(Context context,
                                List<ParentModel> listParen,
                                HashMap<ParentModel, List<ChildModel>> listChild) {
        this.context = context;
        this.listParen = listParen;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return this.listParen.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listParen.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listParen.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChild.get(this.listParen.get(groupPosition)).get(childPosition);
}

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ParentModel parentModel = (ParentModel) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.list_parenttotal,null);
        }

        TextView tvnamabarang = convertView.findViewById(R.id.tv_parentnamabarang);
        TextView tvtotalbarang = convertView.findViewById(R.id.tv_parenttotal);
        TextView tvtotalnormal = convertView.findViewById(R.id.tv_parenttotalbaik);
        TextView tvtotalrusak = convertView.findViewById(R.id.tv_parenttotalrusak);
        ImageView img = convertView.findViewById(R.id.imgpanah);
        tvnamabarang.setText(parentModel.getNama_barang());
        tvtotalbarang.setText(parentModel.getTotal_barang());
        tvtotalnormal.setText(parentModel.getTotal_normal());
        tvtotalrusak.setText(parentModel.getTotal_rusak());

        if (isExpanded){
            img.setImageResource(R.drawable.ic_keyboard_arrow_up_grey_24dp);
        } else {
            img.setImageResource(R.drawable.ic_keyboard_arrow_down_grey_24dp);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildModel childModel = (ChildModel)getChild(groupPosition,childPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_childtotal,null);
        }
        TextView tvno= convertView.findViewById(R.id.txNoChild);
        TextView tvbarangrusak = convertView.findViewById(R.id.tv_barangrusak);
        ImageView imgiconx = convertView.findViewById(R.id.img_iconx);
        tvbarangrusak.setText(childModel.getNama_barang());
        tvno.setText(childPosition+1+". ");
        if (childModel.getStatus().equalsIgnoreCase("baik")){
            tvbarangrusak.setTextColor(context.getResources().getColor(R.color.black_trasnparent));
            imgiconx.setVisibility(View.GONE);
        } else {
            tvbarangrusak.setTextColor(context.getResources().getColor(R.color.colorAccent));
            imgiconx.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
