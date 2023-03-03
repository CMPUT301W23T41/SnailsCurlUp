package com.example.catchqr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.catchqr.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ProfileFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    binding.fragmentName.setText("My Profile");
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.search:
                    binding.fragmentName.setText("Search");
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.scan:
                    binding.fragmentName.setText("Scan");
                    replaceFragment(new ScanFragment());
                    break;
                case R.id.map:
                    binding.fragmentName.setText("Map");
                    replaceFragment(new MapFragment());
                    break;
                case R.id.leaderboard:
                    binding.fragmentName.setText("Leaderboard");
                    replaceFragment(new LeaderboardFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
