package com.javalive09.codebag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Created by peter on 16/9/21.
 *
 */
public class ListAdapter extends BaseAdapter {

    private static final int NO_ENTRY = 0; // 不含有入口方法
    private static final int ENTRY = 1; // 含有入口方法

    private CodeBagActivity mActivity;
    private LayoutInflater factory;
    private List<Node> mList;

    ListAdapter(CodeBagActivity context, ArrayList<Node> list) {
        factory = LayoutInflater.from(context);
        mActivity = context;
        mList = list;
        Collections.sort(mList);
    }

    @Override
    public int getCount() {
        if(mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Node getItem(int position) {
        if(mList != null) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.main_list_item, parent, false);
            holder = new Holder();
            holder.text = (TextView) convertView.findViewById(R.id.list_item_text);
            holder.icon = (ImageView) convertView.findViewById(R.id.list_item_icon);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Node node = getItem(position);
        switch (node.type) {
            case Node.CLASS:
                holder.icon.setImageResource(R.drawable.file);
                String txt = String.format(mActivity.getString(R.string.item_string_java), node.name);
                holder.text.setText(txt);
                break;
            case Node.DIR:
                holder.icon.setImageResource(R.drawable.folder);
                holder.text.setText(node.name);
                break;
            case Node.METHOD:
                holder.icon.setImageResource(R.drawable.run);
                txt = String.format(mActivity.getString(R.string.item_string_method), node.name);
                holder.text.setText(txt);
                break;
            default:
                break;
        }

        if(getItemViewType(position) != ENTRY) {
            convertView.setBackgroundResource(android.R.color.darker_gray);
        }else {
            convertView.setBackgroundResource(R.drawable.list_item_bg);
        }
        convertView.setOnClickListener(mActivity);
        convertView.setOnLongClickListener(mActivity);
        convertView.setTag(R.id.main_item_pos, position);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        Node node = getItem(position);
        if (node.type == Node.CLASS) {
            String className = node.className;
            try {
                Class<?> cls = Class.forName(className);
                if(cls != null) {
                    Class<?> superCls = cls.getSuperclass();
                    if(superCls != null) {
                        String superClassName = superCls.getSimpleName();
                        if (superClassName.equals("Entry")) {
                            Method[] methods = cls.getDeclaredMethods();
                            for (Method m : methods) {
                                if (Modifier.PUBLIC == m.getModifiers()) {
                                    return ENTRY;
                                }
                            }
                        }
                    }
                }
                return NO_ENTRY;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ENTRY;
    }

    private class Holder {
        ImageView icon;
        TextView text;
    }
}