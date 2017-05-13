package cn.ilell.manage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.veryfit.multi.entity.BleDevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ilell.manage.R;

/**
 * Created by WSL
 * 扫描手环监听器
 */
public class BandScanAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<BleDevice> entities;
    private int selectItem = -1;

    public BandScanAdapter(Context context, List<BleDevice> refCourse) {
        setEntities(refCourse);
        this.inflater = LayoutInflater.from(context);
    }

    public void setEntities(List<BleDevice> entities) {
        if (entities != null) {
            this.entities = entities;
        } else {
            this.entities = new ArrayList<BleDevice>();
        }
    }

    public void upDada(BleDevice device) {
        this.entities.add(device);
        Collections.sort(entities);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.entities.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    public void setSelectItem(int item) {
        this.selectItem = item;
        notifyDataSetChanged();
    }

    @Override
    public BleDevice getItem(int index) {
        return entities.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_band_device, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_basic_name);
            holder.tvMac = (TextView) convertView.findViewById(R.id.tv_basic_mac);
            holder.view = (View) convertView.findViewById(R.id.view_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BleDevice course = entities.get(position);
        holder.tvName.setText(course.mDeviceName);
        holder.tvMac.setText(course.mDeviceAddress);
        if (selectItem == position) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tvName;
        private TextView tvMac;
        private View view;
    }
}
