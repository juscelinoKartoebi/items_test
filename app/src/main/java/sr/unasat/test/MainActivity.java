package sr.unasat.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Model> modelList;
    RecyclerView recyclerView;
    OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating an arraylist

        modelList = new ArrayList<>();
        modelList.add(new Model("Komkommer", getString(R.string.komkommer), R.drawable.komkommer));
        modelList.add(new Model("pompoen", getString(R.string.pompoen), R.drawable.pompoen));
        modelList.add(new Model("Kouseband", getString(R.string.kouseband), R.drawable.kouseband));
        modelList.add(new Model("boulanger", getString(R.string.boulanger), R.drawable.boulanger));
        modelList.add(new Model("klaroen", getString(R.string.klaroen), R.drawable.klaroen));
        modelList.add(new Model("kool", getString(R.string.kool), R.drawable.kool));
        modelList.add(new Model("bitawiri", getString(R.string.bitawiri), R.drawable.bitawiri));
        modelList.add(new Model("antroewa", getString(R.string.antroewa), R.drawable.antroewa));

        // recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        // adapter
        mAdapter = new OrderAdapter(this, modelList);
        recyclerView.setAdapter(mAdapter);




    }
}