package com.up.lhm.recharge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.widget.TextView;

import com.up.lhm.recharge.adapter.AmountAdapter;
import com.up.lhm.recharge.adapter.AmountAdapter.MoneyInputListener;
import com.up.lhm.recharge.data.AmountRespone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lianghaimiao
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 金额列表
     */
    private RecyclerView mRvRechargeMoney;
    /**
     * 选中的金额
     */
    private TextView mTvSelectMoney;
    /**
     * 被选择的条目的下标
     */
    private int mSelectItem = 0;

    private AmountAdapter mAmountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }


    private void initView() {
        mRvRechargeMoney = findViewById(R.id.rv_recharge_money);
        mTvSelectMoney = findViewById(R.id.tv_select_money);
    }


    private void initData() {
        List<AmountRespone> amountList = getAmountList();
        if (amountList == null || amountList.size() <= 0) { return; }

        ((SimpleItemAnimator) mRvRechargeMoney.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mAmountAdapter = new AmountAdapter(amountList, this);
        mAmountAdapter.setDefaultItem(mSelectItem);
        mTvSelectMoney.setText(amountList.get(mSelectItem).amount + "元");
        mRvRechargeMoney.setLayoutManager(manager);
        mRvRechargeMoney.setAdapter(mAmountAdapter);

    }


    public List<AmountRespone> getAmountList() {
        List<AmountRespone> mList = new ArrayList<>();
        String[] strings = {"10", "20", "30", "100"};
        String defaultSelect = "10";
        for (int i = 0; i < strings.length; i++) {
            AmountRespone ListBean1 = new AmountRespone();
            ListBean1.amount = strings[i];
            ListBean1.itemType = 0;
            ListBean1.isSelect = TextUtils.equals(strings[i], defaultSelect);

            if (ListBean1.isSelect) {
                mSelectItem = i;
            }
            mList.add(ListBean1);
        }

        //其他金额
        AmountRespone ListBean2 = new AmountRespone();
        ListBean2.amount = "0";
        ListBean2.itemType = 1;
        ListBean2.isSelect = false;
        mList.add(ListBean2);

        return mList;
    }

    private void setListener() {
        mAmountAdapter.setMoneyInputListener(new MoneyInputListener() {
            @Override
            public void onGetMoneyInput(String money) {
                mTvSelectMoney.setText(money + "元");
            }
        });

    }
}
