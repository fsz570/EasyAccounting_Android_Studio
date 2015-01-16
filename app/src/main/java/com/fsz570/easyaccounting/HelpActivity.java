package com.fsz570.easyaccounting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fsz570.easyaccounting.util.Utils;

public class HelpActivity extends Activity {

    View help1;
    View help2;
    View help3;
    View help4;
    View help5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        initUi();

    }

    private void initUi(){
        help1 = findViewById(R.id.help_1);
        help2 = findViewById(R.id.help_2);
        help3 = findViewById(R.id.help_3);
        help4 = findViewById(R.id.help_4);
        help5 = findViewById(R.id.help_5);

        help1.setOnClickListener(clickListener);
        help2.setOnClickListener(clickListener);
        help3.setOnClickListener(clickListener);
        help4.setOnClickListener(clickListener);
        help5.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.help_1:
                    setCollapseAndExpandParam(help1, help2, 5);
                    break;
                case R.id.help_2:
                    setCollapseAndExpandParam(help2, help3, 5);
                    break;
                case R.id.help_3:
                    setCollapseAndExpandParam(help3, help4, 5);
                    break;
                case R.id.help_4:
                    setCollapseAndExpandParam(help4, help5, 5);
                    break;
                case R.id.help_5:
                    setCollapseAndExpandParam(help5, help1, 5);
                    break;
            }
        }
    };

    private void setCollapseAndExpandParam(View collapseView, View ExpandView, int slowRate){
        RelativeLayout.LayoutParams paramsCollapse;
        RelativeLayout.LayoutParams paramsExpand;

        //paramsCollapse = (RelativeLayout.LayoutParams)collapseView.getLayoutParams();
        paramsCollapse = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsCollapse.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        collapseView.setLayoutParams(paramsCollapse);

        //paramsExpand = (RelativeLayout.LayoutParams)ExpandView.getLayoutParams();
        paramsExpand = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        paramsExpand.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ExpandView.setLayoutParams(paramsExpand);

//        Utils.collapseWidth(collapseView, slowRate);
//        Utils.expandWidth(ExpandView, slowRate);
        Utils.fadeOut(collapseView, slowRate);
        Utils.fadeIn(ExpandView, slowRate);
    }
}
