/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allintask.lingdao.ui.activity.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.helper.EMChatHelper;
import com.allintask.lingdao.R;
import com.allintask.lingdao.domain.EaseUser;
import com.allintask.lingdao.ui.adapter.message.EaseContactAdapter;
import com.allintask.lingdao.widget.EaseSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SuppressLint("Registered")
public class PickContactNoCheckboxActivity extends BaseEMChatActivity {

	protected EaseContactAdapter contactAdapter;
	private List<EaseUser> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_pick_contact_no_checkbox);
		ListView listView = (ListView) findViewById(R.id.list);
		EaseSidebar sidebar = (EaseSidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		contactList = new ArrayList<EaseUser>();
		// get contactlist
		getContactList();
		// set adapter
		contactAdapter = new EaseContactAdapter(this, R.layout.ease_row_contact, contactList);
		listView.setAdapter(contactAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick(position);
			}
		});

	}

	protected void onListItemClick(int position) {
		setResult(RESULT_OK, new Intent().putExtra("username", contactAdapter.getItem(position)
				.getUsername()));
		finish();
	}

	public void back(View view) {
		finish();
	}

	private void getContactList() {
		contactList.clear();
		Map<String, EaseUser> users = EMChatHelper.getInstance().getContactList();
		for (Map.Entry<String, EaseUser> entry : users.entrySet()) {
			if (!entry.getKey().equals(CommonConstant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(CommonConstant.GROUP_USERNAME) && !entry.getKey().equals(CommonConstant.CHAT_ROOM) && !entry.getKey().equals(CommonConstant.CHAT_ROBOT))
				contactList.add(entry.getValue());
		}
		// sort
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }
                
            }
        });
	}

}
