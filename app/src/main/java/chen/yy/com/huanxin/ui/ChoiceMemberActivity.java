package chen.yy.com.huanxin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.adapter.ChoiceAdapter;
import chen.yy.com.huanxin.model.bean.CheckUser;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.model.db.DbManager;

public class ChoiceMemberActivity extends AppCompatActivity {

      @BindView(R.id.lv_choice)
      ListView lvChoice;
      @BindView(R.id.cb_all)
      CheckBox cbAll;
      @BindView(R.id.btn_save)
      Button btnSave;
      private ChoiceAdapter choiceAdapter;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_choice_member);
            ButterKnife.bind(this);
            initdata();
      }

      private void initdata() {
            List<UserInfo> contacts = DbManager.getDbManager().getContactImp().getContacts();
                  if (contacts != null && contacts.size() > 0) {
                        List<CheckUser> check=new ArrayList<>();
                        for (int i = 0; i < contacts.size(); i++) {
                              check.add(new CheckUser(contacts.get(i)));
                              choiceAdapter = new ChoiceAdapter(this, check,cbAll);
                              lvChoice.setAdapter(choiceAdapter);
                        }


            }
      }



      @OnClick({R.id.btn_save})
      public void onClick(View v) {
            if(v==btnSave){
                  if(choiceAdapter!=null){
                        setResult(RESULT_OK,getIntent().putParcelableArrayListExtra("choice",(ArrayList) choiceAdapter.getChoiceUser()));
                      finish();
                  }

            }
      }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            if(choiceAdapter!=null){
                  setResult(RESULT_OK,getIntent().putParcelableArrayListExtra("choice",(ArrayList) choiceAdapter.getChoiceUser()));
            }

      }
}
