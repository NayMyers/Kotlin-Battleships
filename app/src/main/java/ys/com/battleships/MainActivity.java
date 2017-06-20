package ys.com.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FriendlyBattleGridView FBGV = ((FriendlyBattleGridView)findViewById(R.id.grid));
    }
}
