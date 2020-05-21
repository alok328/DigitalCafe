package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.CustomDataEntry;
import com.alok328raj.digitalcafe.API.Model.DayWiseResponse;
import com.alok328raj.digitalcafe.API.Model.MealWiseResponse;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DayWiseVisualisation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_wise_visualisation);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        }

        final AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://digitalcafe-stats.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient client = retrofit.create(ApiClient.class);
        client.getStudentsDayWise().enqueue(new Callback<List<DayWiseResponse>>() {
            @Override
            public void onResponse(Call<List<DayWiseResponse>> call, Response<List<DayWiseResponse>> response) {
                if(response.code() == 200){

                    Cartesian cartesian = AnyChart.line();

                    cartesian.animation(true);

                    cartesian.padding(10d, 20d, 10d, 20d);

                    cartesian.crosshair().enabled(true);
                    cartesian.crosshair()
                            .yLabel(true)
                            // TODO ystroke
                            .yStroke((Stroke) null, null, null, (String) null, (String) null);

                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                    cartesian.title("Trend of food consumption day wise for each day");

                    cartesian.yAxis(0).title("Number of students");
                    cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

                    List<DataEntry> dataEntries = new ArrayList<>();
                    List<DayWiseResponse> list = response.body();
                    for(int i=0; i<7; i++){
                        String day = list.get(i).getDay();
                        int p1 = list.get(i).getStudents();
                        dataEntries.add(new CustomDataEntry(day, p1));
                    }
                    Set set = Set.instantiate();
                    set.data(dataEntries);
                    Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

                    Line series1 = cartesian.line(series1Mapping);
                    series1.name("Students");
                    series1.hovered().markers().enabled(true);
                    series1.hovered().markers()
                            .type(MarkerType.CIRCLE)
                            .size(4d);
                    series1.tooltip()
                            .position("right")
                            .anchor(Anchor.LEFT_CENTER)
                            .offsetX(5d)
                            .offsetY(5d);
                    cartesian.legend().enabled(true);
                    cartesian.legend().fontSize(13d);
                    cartesian.legend().padding(0d, 0d, 10d, 0d);

                    anyChartView.setChart(cartesian);

                }else{
                    Toast.makeText(DayWiseVisualisation.this, (CharSequence) response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DayWiseResponse>> call, Throwable t) {
                Log.d("testT", t.getMessage());
            }
        });

    }
}
