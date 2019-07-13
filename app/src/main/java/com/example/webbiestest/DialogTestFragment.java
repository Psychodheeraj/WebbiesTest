package com.example.webbiestest;


import android.app.Dialog;
import android.os.Bundle;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webbiestest.R;
import com.example.webbiestest.databinding.FragmentDialogTestBinding;
import com.example.webbiestest.databinding.LayoutCheckTestBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogTestFragment extends DialogFragment {

     private static final String TAG = "DialogTestFragment";
     private View dialogTestFragmentView;

     private List<String> arrayList = new ArrayList<>();
     private FragmentDialogTestBinding binding;

     private LayoutInflater mInflater;
     private LinearLayout linearLayout;

     private ArrayList<String> checkecList=new ArrayList<>();
     TextView textView;

     private LayoutCheckTestBinding layoutBinding;



     /*
      * For displaying Dialog on full Screen
      * Dheeraj
      * */
     @Override
     public void onStart() {
          super.onStart();
          Dialog dialog = getDialog();
          if (dialog != null) {
               int width = ViewGroup.LayoutParams.MATCH_PARENT;
               int height = ViewGroup.LayoutParams.MATCH_PARENT;
               dialog.getWindow().setLayout(width, height);
               // dialog.getWindow().setWindowAnimations(R.style.dialog_slide);
          }
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

          binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_dialog_test, container, false);
          // dialogTestFragmentView = inflater.inflate(R.layout.fragment_dialog_test, container, false);
          dialogTestFragmentView = binding.getRoot();
          binding.getCheckStatus();

          linearLayout = (LinearLayout) dialogTestFragmentView.findViewById(R.id.linearLayout);

          Button button=(Button)dialogTestFragmentView.findViewById(R.id.button);
          button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    Toast.makeText(getContext(), checkecList.size(), Toast.LENGTH_SHORT).show();
               }
          });
          //inflater=getLayoutInflater();


          for (int i = 0; i <= 5; i++) {
               arrayList.add("Test" + i);
          }

          init();

          return dialogTestFragmentView;
     }

     public void init() {
          for (String list : arrayList) {


               layoutBinding=DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_check_test, null,false);
               View child =layoutBinding.getRoot();
               textView  = (TextView) child.findViewById(R.id.textView2);
               textView.setText(list);

               linearLayout.addView(child);

          }
     }

     @BindingAdapter("switchClick")
     public void onApplyClick()
     {
          Bundle bundle=new Bundle();
          bundle.putStringArrayList("checked Data",checkecList);
          Navigation.findNavController(dialogTestFragmentView).navigate(R.id.homeFragment,bundle);


     }

     //TOdo data binding on toggle button is not working
     @BindingAdapter("switchClick")
     public void onSwitchChanged(Switch clickSwitch )
     {
         clickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked)
                   {
                      checkecList.add( textView.getText().toString());
                   }
                   else
                   {
                        checkecList.remove(textView.getText().toString());
                   }
              }
         });

     }

}
