package com.up.lhm.recharge.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.up.lhm.recharge.R;
import com.up.lhm.recharge.data.AmountRespone;

import java.util.ArrayList;
import java.util.List;

import static com.up.lhm.recharge.data.AmountRespone.NORMAL_NUM;
import static com.up.lhm.recharge.data.AmountRespone.OTHER_NUM;

/**
 * @author lianghaimiao
 * @date 2019/1/22
 * @function
 */

public class AmountAdapter extends RecyclerView.Adapter {
    private Context mContext;
    /**
     * 上次选中的oisition
     */
    private int oldPostion = 0;
    /**
     * 金额数据
     */
    private List<AmountRespone> mAmountList = new ArrayList<>();
    /**
     * 默认选中的标签
     */
    private int mSelectItem;
    private EditText mEditText;

    public AmountAdapter(List<AmountRespone> amountList, Context context) {
        mAmountList.addAll(amountList);
        this.mContext = context;
    }

    /**
     * 基于项不同的类型来获得不同的viewholder,关联对应的布局
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        switch (viewType) {
            case NORMAL_NUM:
                holder = new NorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_normal_amount, null));
                break;
            case OTHER_NUM:
                holder = new OtherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_other_amount, null));
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        int itemType = mAmountList.get(position).itemType;
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.onGetMoneyInput(s.toString());
                String temp = s.toString();
                int length = temp.length();
                if (length >= 1 && temp.startsWith("0")) {
                    //不能以0开头
                    s.replace(0, 1, "");
                    return;
                }
                int d = temp.indexOf(".");
                if (d < 0) { return; }
                if (temp.length() - d - 1 > 2) {
                    s.delete(d + 3, d + 4);
                } else if (d == 0) {
                    s.delete(d, d + 1);
                }

            }
        };
        final AmountRespone amountRespone = mAmountList.get(position);
        switch (itemType) {
            case NORMAL_NUM:
                final TextView textView = ((NorViewHolder) viewHolder).mTvNormalAmount;
                boolean isSelect = amountRespone.isSelect;
                textView.setTypeface(isSelect ? Typeface.defaultFromStyle(Typeface.BOLD)
                                              : Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setText(amountRespone.amount);
                textView.setSelected(isSelect);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setSelected(true);
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        if (position != oldPostion) {
                            notifyItem(position);
                        }

                        //先清空输入框金额，再更新选中金额
                        mEditText.setText("");
                        listener.onGetMoneyInput(amountRespone.amount);
                        InputMethodManager imm = (InputMethodManager) mContext
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(
                                    ((Activity) mContext).getWindow().getDecorView().getWindowToken(), 0);
                        }

                        mEditText.clearFocus();
                        mEditText.setSelected(false);

                    }
                });

                break;
            case OTHER_NUM:
                mEditText = ((OtherViewHolder) viewHolder).mTvOtherAmount;

                //当选择项目从TextView转移到EditText，editText重新获取焦点
                mEditText.addTextChangedListener(textWatcher);

                mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            notifyItem(oldPostion);
                            mEditText.setSelected(true);
                            listener.onGetMoneyInput(mEditText.getText().toString());
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void notifyItem(int posiont) {
        if (oldPostion >= 0) {
            mAmountList.get(oldPostion).setSelect(false);
            notifyItemChanged(oldPostion);
        }
        oldPostion = posiont;
    }

    @Override
    public int getItemCount() {
        return mAmountList.size();
    }

    /**
     * 默认选中的金额
     */
    public void setDefaultItem(int selectItem) {
        this.mSelectItem = selectItem;
    }

    @Override
    public int getItemViewType(int position) {
        return mAmountList.get(position).itemType;
    }

    class NorViewHolder extends ViewHolder {
        /**
         * 固定金额
         */
        TextView mTvNormalAmount;

        public NorViewHolder(View inflate) {
            super(inflate);
            mTvNormalAmount = inflate.findViewById(R.id.tv_normal_amount);
        }
    }

    class OtherViewHolder extends ViewHolder {
        /**
         * 其他金额输入框
         */
        EditText mTvOtherAmount;

        public OtherViewHolder(View inflate) {
            super(inflate);
            mTvOtherAmount = inflate.findViewById(R.id.tv_other_amount);
        }
    }

    private MoneyInputListener listener;

    public interface MoneyInputListener {
        /**
         * 选中/输入的金额
         */
        void onGetMoneyInput(String money);
    }

    public void setMoneyInputListener(MoneyInputListener listener) {
        this.listener = listener;
    }
}
