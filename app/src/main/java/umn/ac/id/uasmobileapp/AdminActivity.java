package umn.ac.id.uasmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {
    public static String businessId;
    ImageButton btnBarangAdmin,btnHome,btnPesananAdmin;
    TextView tVnamaBisnisAdmin;
    Session session;
    String businessName;

    //public String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tVnamaBisnisAdmin = findViewById(R.id.namaBisnisAdmin);

        session = new Session(getApplicationContext());
        String key = session.getKey();
        System.out.println("----------------------------- KEY : " + key);

        DatabaseReference reference = FirebaseDatabase.getInstance("https://final-project-mobile-app-98d46-default-rtdb.firebaseio.com/").getReference("business");
        Query query = reference.orderByChild("Employee/" + key).equalTo(true);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    System.out.println(dataSnapshot);
                    for(DataSnapshot businessSnapshot: dataSnapshot.getChildren()){
                        businessName = businessSnapshot.child("business_name").getValue(String.class);
                        businessId = businessSnapshot.getKey();
                    } tVnamaBisnisAdmin.setText(businessName);
                }
                else{
                    System.out.println("Nothing");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminContainerFragment, new AdminHomeFragment()).commit();
        }

        btnHome = findViewById(R.id.btnHome);
        btnPesananAdmin = findViewById(R.id.btnPesanan);
        btnBarangAdmin = findViewById(R.id.btnBarang);
        btnBarangAdmin = findViewById(R.id.btnBarang);

        btnHome.setSelected(true);btnHome.setOnClickListener(view ->{
            Fragment AdminHomeFragment = new AdminHomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.adminContainerFragment, AdminHomeFragment,null);
            transaction.commit();
            btnPesananAdmin.setSelected(false);
            btnBarangAdmin.setSelected(false);
            btnHome.setSelected(true);
        });


        btnPesananAdmin.setOnClickListener(view -> {
            Fragment AdminPesananFragment = new AdminPesananFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.adminContainerFragment, AdminPesananFragment, null);
            transaction.commit();
            btnHome.setSelected(false);
            btnBarangAdmin.setSelected(false);
            btnPesananAdmin.setSelected(true);
        });

        btnBarangAdmin.setOnClickListener(view->{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.adminContainerFragment, new AdminBarangFragment(),null);
            transaction.commit();
            btnHome.setSelected(false);
            btnBarangAdmin.setSelected(true);
            btnPesananAdmin.setSelected(false);
        });

    }
    public void logout(View view){
        session.logout();
        Intent intent = new Intent(AdminActivity.this,Login.class);
        startActivity(intent);
        finish();
    }
}