package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class BlockedUsers extends AppCompatActivity {

    RecyclerView recyclerView;

    Button Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        recyclerView = findViewById(R.id.recyclerview);

        ConnectionToBackend c = new ConnectionToBackend();
        List<Account> items = c.getAllInList(myAccount.getUserId(), 1);



        recyclerView.setLayoutManager
                (new LinearLayoutManager(this));
        recyclerView.setAdapter
                (new PersonAdapter(getApplicationContext(), items));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(BlockedUsers.this, recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent UnblockIntent =
                                new Intent(BlockedUsers.this, Unblock.class);
                        if(!items.isEmpty()){
                            Account Person = items.get(position);
                            UnblockIntent.putExtra
                                    ("PersonName", Person.getUsername());
                            UnblockIntent.putExtra
                                    ("PersonId", Person.getUserId());
                        }
                        startActivity(UnblockIntent);
                    }

                    @Override public void onLongItemClick
                            (View view, int position) {
                        // do whatever
                    }
                }));

        Cancel = findViewById(R.id.Cancel);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(BlockedUsers.this, PersonalProfileUsers.class);
                startActivity(HomeIntent);
            }
        });




    }
}