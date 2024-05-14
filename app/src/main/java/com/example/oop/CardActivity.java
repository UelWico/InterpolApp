package com.example.oop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class CardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton imgB = findViewById(R.id.menuBtn);
        drawerLayout = findViewById(R.id.drawL);

        NavigationView navigationView = findViewById(R.id.navL);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        imgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Card card = (Card) getIntent().getSerializableExtra("card");
        System.out.println(card.iPtoString());

        TextView textIP = findViewById(R.id.textIP);
        TextView name = findViewById(R.id.page_name);
        TextView WB = findViewById(R.id.WB_page);

        textIP.setText(card.iPtoString());
        name.setText(card.nameXlastName());
        WB.setText("Wanted by\n" + card.wantedBy);

        if (card.isPD()) {
            TextView PD = findViewById(R.id.PD);
            TextView textPD = findViewById(R.id.textPD);

            PD.setVisibility(View.VISIBLE);
            textPD.setVisibility(View.VISIBLE);
            textPD.setText(card.physicalDescription);
        }
        if (card.isDet()) {
            TextView Det = findViewById(R.id.Det);
            TextView textDet = findViewById(R.id.textDet);

            Det.setVisibility(View.VISIBLE);
            textDet.setVisibility(View.VISIBLE);
            textDet.setText(card.details);
        }

        TextView textCh = findViewById(R.id.textCh);
        textCh.setText(card.charges);
        ImageView img = findViewById(R.id.imageView);
        int resID = getResources().getIdentifier(card.photoPath, "drawable", getPackageName());
        img.setImageResource(resID);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.about) {
            Intent i = new Intent(this, About.class);
            startActivity(i);
        }
        return false;
    }
}