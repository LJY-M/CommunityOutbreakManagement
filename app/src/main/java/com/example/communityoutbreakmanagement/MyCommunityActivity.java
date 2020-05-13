package com.example.communityoutbreakmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyCommunityActivity extends AppCompatActivity {

    private String[] identityInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_community);

        Intent intentThatStartedMultiFunctionActivity = getIntent();

        if (intentThatStartedMultiFunctionActivity != null) {
            if (intentThatStartedMultiFunctionActivity.hasExtra("identityAuthentication")) {
                identityInformation = intentThatStartedMultiFunctionActivity.getStringArrayExtra("identityAuthentication");
                Toast.makeText(MyCommunityActivity.this,identityInformation[0] + identityInformation[1],Toast.LENGTH_SHORT).show();
                System.out.println(identityInformation[0] + identityInformation[1]);
            }
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.my_community_floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resident residentAB = new Resident("","","");
                Intent addBlogIntent = new Intent(MyCommunityActivity.this,AddBlogActivity.class);
                addBlogIntent.putExtra(residentAB.identityAuthentication, identityInformation);
                startActivity(addBlogIntent);
            }
        });
    }
}
