package com.example.index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.index.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import constantes.Constants;
import fragments.*;
import fragments.calls.CallsFragment;
import fragments.pools.PoolsFragment;
import fragments.redes.RedeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private SharedPreferences sharedPreferences;

    Toolbar toolbar;
    LinearLayout title_id;
    TextView dynamicTitle;
    RelativeLayout typesFeed;
    ImageView chev_ic;

    TextView user_name;

    View overlayView;

    /**
     * * A função abaixo cria parametros e definições na criação da tela
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = CommomUtils.getSharedPreference(this);
        super.onCreate(savedInstanceState);
        dynamicTitle = findViewById(R.id.dinamicTittle);
        user_name = findViewById(R.id.username_campo);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Redirecionar para LoginActivity se o usuário não estiver logado
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chev_ic = findViewById(R.id.ic_chevron);
        drawerLayout = findViewById(R.id.drawer_layout);
        /*String user_name1 = "nada";*/
       // user_name.setText(sharedPreferences.getString(Constants.USERNAME, " ").toString())


        // Carregando as SharedPreferences

        String username = sharedPreferences.getString(Constants.USERNAME, "");
        String mail_ = sharedPreferences.getString(Constants.MAIL, "");



        NavigationView navigationView = findViewById(R.id.nav_perfil);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.username_campo);
        TextView mailTextView = headerView.findViewById(R.id.email_campo);
        // Atualizando o TextView com o valor carregado das SharedPreferences
        usernameTextView.setText(username);
        mailTextView.setText(mail_);
        ImageButton menu_left = findViewById(R.id.menu_left);
        ImageButton profileButton = findViewById(R.id.profile_btn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });
        menu_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        typesFeed = findViewById(R.id.typesFedd);
        title_id = findViewById(R.id.title_id);
        title_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chev_ic.getRotation() == 90){
                    chev_ic.setRotation(270);
                    slideDown(typesFeed);
                }
                else{
                    chev_ic.setRotation(90);
                    slideUp(typesFeed);
                }

                Log.d("LinearL Title", "Tittulo clicado ");
            }
        });

/*        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();


                String toolbarTitle = "";
                if (itemId == R.id.home) {
                    selectedFragment = new HomeFragment();
                    toolbarTitle = "Index";
                    chev_ic.setVisibility(View.GONE);
                }
                if (itemId == R.id.web) {
                    selectedFragment = new RedeFragment();
                    toolbarTitle = "Web";
                    chev_ic.setVisibility(View.GONE);
                }
                if (itemId == R.id.chat) {
                    selectedFragment = new CallsFragment();
                    toolbarTitle = "Chats";
                    chev_ic.setVisibility(View.GONE);
                }
                if (itemId == R.id.polls) {
                    selectedFragment = new PoolsFragment();
                    toolbarTitle = "Polls";
                    chev_ic.setVisibility(View.GONE);
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    dynamicTitle.setText(toolbarTitle);
                    adjustToolbarTitle();
                }
                return true;
            }
        });

        // Defina o HomeFragment como o fragmento inicial
        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
            dynamicTitle = findViewById(R.id.dinamicTittle);
            dynamicTitle.setText("Index");
            chev_ic.setVisibility(View.GONE);
        }

    }

    /**
     * A Função abaixo executa as transições disponivies pelas opções dos menus laterais
    * **/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Log.d("MainActivity", "Selected item ID: " + itemId); // Log para depuração

        if (itemId == R.id.nav_perfil) {
            dynamicTitle.setText("Perfil");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if (itemId == R.id.nav_saved) {
            dynamicTitle.setText("Salvos");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SavedFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.END);
        }else if (itemId == R.id.nav_premium) {
            dynamicTitle.setText("Index Premium");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PremiumFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if (itemId == R.id.nav_history) {
            dynamicTitle.setText("Histórico");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if (itemId == R.id.nav_config) {
            dynamicTitle.setText("Configurações");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            drawerLayout.closeDrawer(GravityCompat.END);
        } else if (itemId == R.id.logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            redirectLogin();
        } else {
            throw new IllegalStateException("Unexpected value: " + itemId);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void adjustToolbarTitle() {
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < toolbar.getChildCount(); i++) {
                    if (toolbar.getChildAt(i) instanceof TextView) {
                        TextView toolbarTitle = (TextView) toolbar.getChildAt(i);
                        Toolbar.LayoutParams params = (Toolbar.LayoutParams) toolbarTitle.getLayoutParams();
                        params.setMarginEnd(0); // Ajuste conforme necessário
                        toolbarTitle.setLayoutParams(params);
                        break;
                    }
                }
            }
        });
    }
    // Método para animar o deslizar para baixo (mostrar)
    private void slideDown(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

        // Animação de desvanecimento
        view.animate()
                .alpha(1.0f)
                .setDuration(300)
                .setListener(null);

/*        // Animação de deslize
        view.setTranslationY(-view.getHeight());
        view.animate()
                .translationY(0)
                .setDuration(300)
                .setListener(null);*/
    }

    // Método para animar o deslizar para cima (esconder)
    private void slideUp(final View view) {

        // Animação de desvanecimento
        view.animate()
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });


    }

    public void redirectLogin(){
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();

        // Iniciar a LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        // Encerrar a atividade atual
        finish();
    }

    public void callAPIToGetUserInfo(){

    }
/*    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }*/
}
