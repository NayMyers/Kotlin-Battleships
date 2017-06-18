package ys.com.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BattleGridView BGV = ((BattleGridView)findViewById(R.id.grid));

        BGV.setState(0, 0, GridState.MISS);
        BGV.setState(3, 1, GridState.HIT);

        BGV.setBattleGridViewListener(new BattleGridViewListener() {
            @Override
            public void gridClicked(int x, int y) {
                Log.i("Grid", "Grid pressed at (" + x + ", " + y + ")");

                BGV.setState(x, y, GridState.HIT);
                BGV.invalidate();
            }
        });
    }
}
