package com.example.administrator.welcomedemo.friend_list_item;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import com.example.administrator.welcomedemo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
    public class ExpandableList_friend_item extends Activity {
        private List<Map<String,Object>> parentList=new ArrayList<Map<String,Object>>();
        private List<List<Map<String,Object>>> childList = new ArrayList<List<Map<String,Object>>>();
        ExpendAdapter adapter;
        ExpandableListView exList;
        private String[] listName = new String[]{
                "我的好友","高中同学","大学同学","移动开发","网站建设","普通朋友"
        };
        private String[] childTitle= new String[]{
                "丫宁","王八锐","小鸟","连超","董二丫"
        };
        private String[] childMood= new String[]{
                "aaaaa","bbbbb","eeeee","vvvvv","sssdfffffff"
        };
        private int[] headImage=new int[]{
                R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.focus,R.drawable.one
        };

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.expandable_friend_item);

            exList = (ExpandableListView) this.findViewById(R.id.expandableListView1);
            parentList =getParentList();
            childList = getChildList();
            adapter = new ExpendAdapter(ExpandableList_friend_item.this, parentList, childList);

            exList.setAdapter(adapter);
            exList.setGroupIndicator(null);
            exList.setDivider(null);

        }
        public List<Map<String,Object>> getParentList(){
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(int i=0;i<listName.length;i++){
                Map<String, Object> curGroupMap = new HashMap<String, Object>();
                list.add(curGroupMap);
                curGroupMap.put("List", listName[i]);
            }
            return list;
        }
        public List<List<Map<String,Object>>> getChildList(){
            List<List<Map<String,Object>>> list1 = new ArrayList<List<Map<String,Object>>>();
            for (int i = 0; i < listName.length; i++) {


                List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
                for (int j = 0; j <childTitle.length; j++) {
                    Map<String, Object> curChildMap = new HashMap<String, Object>();
                    children.add(curChildMap);
                    curChildMap.put("Title", childTitle[j]);
                    curChildMap.put("Mood", childMood[j]);
                    curChildMap.put("Head", headImage[j]);
                }
                list1.add(children);
            }
            return list1;

        }

    }
