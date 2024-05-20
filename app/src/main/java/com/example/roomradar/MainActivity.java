package com.example.roomradar;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomradar.Entities.BoardingHouse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final NotificationsFragment notificationsFragment = new NotificationsFragment();
    private final MapFragment mapFragment = new MapFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initializeView();

    }

    private void initializeView(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavContainer);

        bottomNavigationView.setSelectedItemId(R.id.homeItem);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.homeItem){
                    System.out.println("home ni");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, homeFragment).addToBackStack(null).commit();
                }else if(item.getItemId() == R.id.mapsItem){
                    System.out.println("maps ni");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, mapFragment).addToBackStack(null).commit();
                }else if(item.getItemId() == R.id.notificationsItem){
                    System.out.println("notifs ni");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, notificationsFragment).addToBackStack(null).commit();
                }else if(item.getItemId() == R.id.profileItem){
                    System.out.println("profile ni");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentsContainer, profileFragment).addToBackStack(null).commit();
                }

                return true;
            }
        });
    }
}