package com.example.oop;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.*;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.*;

import java.io.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Card[] cardsOriginal = null;
    Card[] cardsFiltered = null;
    Dialog dialog;
    DrawerLayout drawerLayout;

    FilterParameters param = new FilterParameters(R.id.nationality_spinner, R.id.WB_spinner, R.id.gender_spinner);

    int page_num = 0;
    int max_page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            getWindow().getDecorView().getWindowInsetsController().hide(
//                    WindowInsets.Type.statusBars()
//            );
//        }
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.drawL);
        NavigationView navigationView = findViewById(R.id.navL);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton imgB = findViewById(R.id.menuBtn);

        imgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.filter_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        try {
            cardsOriginal = readFromJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cardsFiltered = cardsOriginal.clone();
        setFilter(MainActivity.this.getCurrentFocus());
        setPage();

        SpinnerMy spinnerN = new SpinnerMy(dialog, listOfN(), param, this, R.id.nationality_spinner);
        SpinnerMy spinnerG = new SpinnerMy(dialog, new String[]{"", "female", "male"}, param, this, R.id.gender_spinner);
        SpinnerMy spinnerWB = new SpinnerMy(dialog, listOfWB(), param, this, R.id.WB_spinner);
    }

    public void openFilter(View view) {
        dialog.show();
    }

    String readFile(int path) throws IOException {

        InputStream is = getResources().openRawResource(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }

    public Card[] readFromJson() throws IOException {

        int path = R.raw.man;
        Gson gson = new Gson();
        String fileContents = readFile(path);
        Card[] cards = gson.fromJson(fileContents, Card[].class);

        return cards;
    }

    public Card[] getByAge(int minAge, int maxAge, Card[] cards) {
        Card[] cardsByAge = new Card[cards.length];
        int count = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].age >= minAge && cards[i].age <= maxAge) {
                cardsByAge[count++] = cards[i];
            }
        }

        return Arrays.copyOfRange(cardsByAge, 0, count);
    }

    public Card[] getByGender(String gender, Card[] cards) {
        Card[] cardsByGender = new Card[cards.length];
        int count = 0;
        if (Objects.equals(gender, "all")) {
            cardsByGender = cards.clone();
            return cardsByGender;
        }
        for (int i = 0; i < cards.length; i++) {
            if (Objects.equals(cards[i].gender, gender)) {
                cardsByGender[count++] = cards[i];
            }
        }
        return Arrays.copyOfRange(cardsByGender, 0, count);
    }

    public Card[] getByNationality(String nationality, Card[] cards) {
        Card[] cardsByNationality = new Card[cards.length];
        int count = 0;
        if (nationality.equals("all")) {
            cardsByNationality = cards.clone();
            return cardsByNationality;
        }
        for (int i = 0; i < cards.length; i++) {
            if (Objects.equals(cards[i].nationality, nationality)) {
                cardsByNationality[count++] = cards[i];
            }
        }
        return Arrays.copyOfRange(cardsByNationality, 0, count);
    }

    public Card[] getByWB(String WB, Card[] cards) {
        Card[] cardsByWB = new Card[cards.length];
        int count = 0;
        if (WB.equals("all")) {
            cardsByWB = cards.clone();
            return cardsByWB;
        }
        for (int i = 0; i < cards.length; i++) {
            if (Objects.equals(cards[i].wantedBy, WB)) {
                cardsByWB[count++] = cards[i];
            }
        }
        return Arrays.copyOfRange(cardsByWB, 0, count);
    }

    public Card[] getByArchived(boolean Arch, Card[] cards) {
        Card[] cardsByArch = new Card[cards.length];
        int count = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i].archived == Arch) {
                cardsByArch[count++] = cards[i];
            }
        }
        return Arrays.copyOfRange(cardsByArch, 0, count);
    }

    public void setFilter(View view) {
        cardsFiltered = getFilterCards();
        page_num = 0;
        max_page = (int) Math.ceil((double) cardsFiltered.length / 4);
        setPage();
        dialog.dismiss();
    }

    public Card[] getFilterCards() {
        EditText minAgeT = dialog.findViewById(R.id.editminAge);
        EditText maxAgeT = dialog.findViewById(R.id.editmaxAge);

        String nationality = param.map.get(R.id.nationality_spinner);
        String gender = param.map.get(R.id.gender_spinner);
        String WB = param.map.get(R.id.WB_spinner);

        Switch switch1 = dialog.findViewById(R.id.switch1);
        boolean arch = switch1.isChecked();
        System.out.println(arch);

        int minAge = 0;
        int maxAge = 300;
        try {
            minAge = Integer.parseInt(minAgeT.getText().toString());
        } catch (Exception var) {
            minAge = 0;
        }
        try {
            maxAge = Integer.parseInt(maxAgeT.getText().toString());
        } catch (Exception var) {
            maxAge = 300;
        }

        if (gender.isEmpty()) gender = "all";
        if (nationality.isEmpty()) nationality = "all";
        if (WB.isEmpty()) WB = "all";

        System.out.println(gender);

        Card[] filterCards = getByAge(minAge, maxAge, cardsOriginal);
        filterCards = getByGender(gender, filterCards);
        filterCards = getByNationality(nationality, filterCards);
        filterCards = getByWB(WB, filterCards);
        filterCards = getByArchived(arch, filterCards);

        return filterCards;
    }

    public void setCard(int card_id, Card card) {
        CardView cardView = findViewById(card_id);
        String card_name = getResources().getResourceEntryName(card_id);
        TextView nameT = cardView.findViewById(this.getResources().getIdentifier(card_name + "_name", "id", this.getPackageName()));
        TextView nationalityT = cardView.findViewById(this.getResources().getIdentifier(card_name + "_nationality", "id", this.getPackageName()));
        TextView ageT = cardView.findViewById(this.getResources().getIdentifier(card_name + "_age", "id", this.getPackageName()));
        ImageView img = cardView.findViewById(this.getResources().getIdentifier(card_name + "_imageView", "id", this.getPackageName()));

        int resID = getResources().getIdentifier(card.photoPath, "drawable", getPackageName());
        img.setImageResource(resID);
        String name = card.lastName + " " + card.name;
        nameT.setText(name);
        nationalityT.setText(card.nationality);
        ageT.setText(String.valueOf(card.age + " years old"));
    }

    public void hideCard(int card_id) {
        CardView cardView = findViewById(card_id);
        cardView.setVisibility(View.GONE);
    }

    public void showCard(int card_id) {
        CardView cardView = findViewById(card_id);
        cardView.setVisibility(View.VISIBLE);
    }

    public void setPage() {
        int[] card_ids = {R.id.card1, R.id.card2, R.id.card3, R.id.card4};
        int cards_per_page = (cardsFiltered.length - page_num * 4);
        if (cards_per_page > 4) cards_per_page = 4;
        if (cards_per_page < 0) return;
        for (int i = 0; i < cards_per_page; i++) {
            setCard(card_ids[i], cardsFiltered[page_num * 4 + i]);
            showCard(card_ids[i]);
        }
        for (int i = 3; i >= cards_per_page; i--) {
            hideCard(card_ids[i]);
        }
        TextView page = findViewById(R.id.page_num);
        page.setText("Page " + (page_num + 1));

        ImageButton back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);

        ImageButton forward = findViewById(R.id.forward);
        forward.setVisibility(View.VISIBLE);
        if (page_num >= max_page - 1) {
            forward.setVisibility(View.GONE);
        }
        if (page_num == 0) {
            back.setVisibility(View.GONE);
        }
    }

    public void nextPage(View v) {
        page_num++;
        setPage();
    }

    public void previousPage(View v) {
        page_num--;
        setPage();
    }

    String[] listOfN() {
        List<String> nationalities = new ArrayList<>();
        nationalities.add("");
        for (int i = 0; i < cardsOriginal.length; i++) {
            if (!nationalities.contains(cardsOriginal[i].nationality)) {
                nationalities.add(cardsOriginal[i].nationality);
            }
        }
        return nationalities.toArray(new String[0]);
    }

    String[] listOfWB() {
        List<String> WB = new ArrayList<>();
        WB.add("");
        for (int i = 0; i < cardsOriginal.length; i++) {
            if (!WB.contains(cardsOriginal[i].wantedBy)) {
                WB.add(cardsOriginal[i].wantedBy);
            }
        }
        return WB.toArray(new String[0]);
    }

    public Card getCardById(int id) {
        int i;
        if (id == R.id.card1)
            i = 0;
        else if (id == R.id.card2)
            i = 1;
        else if (id == R.id.card3)
            i = 2;
        else if (id == R.id.card4)
            i = 3;
        else
            return null;

        return cardsFiltered[page_num * 4 + i];
    }

    public void onCardClick(View v) {
        Intent i = new Intent(this, CardActivity.class);
        i.putExtra("card", getCardById(v.getId()));
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.home) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.about) {
            Intent i = new Intent(this, About.class);
            startActivity(i);
        }
        return false;
    }
}