package com.alok328raj.digitalcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.CustomDataEntry;
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

public class MealWiseVisualisation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal_wise_visualisation);

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
                .baseUrl("http://digitalcafe-stats.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient client = retrofit.create(ApiClient.class);
        client.getStudentsMealWise().enqueue(new Callback<List<MealWiseResponse>>() {
            @Override
            public void onResponse(Call<List<MealWiseResponse>> call, Response<List<MealWiseResponse>> response) {
                if(response.code() == 200) {

//                    for(MealWiseResponse c : response.body()){
//                        Log.d("testE", c.getDay() + " " + c.getConsumers());
//                    }



                    Cartesian cartesian = AnyChart.line();

                    cartesian.animation(true);

                    cartesian.padding(10d, 20d, 10d, 20d);

                    cartesian.crosshair().enabled(true);
                    cartesian.crosshair()
                            .yLabel(true)
                            // TODO ystroke
                            .yStroke((Stroke) null, null, null, (String) null, (String) null);

                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                    cartesian.title("Trend of food consumption meal wise for each day");

                    cartesian.yAxis(0).title("Number of students");
                    cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

                    List<DataEntry> dataEntries = new ArrayList<>();
                    List<MealWiseResponse> list = response.body();
                    int k=0, x=0;
                    int p1 = 0, p2=0, p3=0, p4=0;
                    for(int i=0; i<7; i++){
                        String day = list.get(x).getDay();
                        for(int j=0; j<4 && k<28 ; j++){
                            p1 = list.get(x+0).getConsumers();
                            p2 = list.get(x+1).getConsumers();
                            p3 = list.get(x+2).getConsumers();
                            p4 = list.get(x+3).getConsumers();
                        }
                        x+=4;
//                        Log.d("testE", day+" "+p1+" "+p2+" "+p3+" "+p4);
                        dataEntries.add(new CustomDataEntry(day, p1, p2, p3, p4));
                    }

                    Set set = Set.instantiate();
                    set.data(dataEntries);
                    Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                    Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
                    Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");
                    Mapping series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }");

                    Line series1 = cartesian.line(series1Mapping);
                    series1.name("Breakfast");
                    series1.hovered().markers().enabled(true);
                    series1.hovered().markers()
                            .type(MarkerType.CIRCLE)
                            .size(4d);
                    series1.tooltip()
                            .position("right")
                            .anchor(Anchor.LEFT_CENTER)
                            .offsetX(5d)
                            .offsetY(5d);

                    Line series2 = cartesian.line(series2Mapping);
                    series2.name("Lunch");
                    series2.hovered().markers().enabled(true);
                    series2.hovered().markers()
                            .type(MarkerType.CIRCLE)
                            .size(4d);
                    series2.tooltip()
                            .position("right")
                            .anchor(Anchor.LEFT_CENTER)
                            .offsetX(5d)
                            .offsetY(5d);

                    Line series3 = cartesian.line(series3Mapping);
                    series3.name("Snacks");
                    series3.hovered().markers().enabled(true);
                    series3.hovered().markers()
                            .type(MarkerType.CIRCLE)
                            .size(4d);
                    series3.tooltip()
                            .position("right")
                            .anchor(Anchor.LEFT_CENTER)
                            .offsetX(5d)
                            .offsetY(5d);

                    Line series4 = cartesian.line(series4Mapping);
                    series4.name("Dinner");
                    series4.hovered().markers().enabled(true);
                    series4.hovered().markers()
                            .type(MarkerType.CIRCLE)
                            .size(4d);
                    series4.tooltip()
                            .position("right")
                            .anchor(Anchor.LEFT_CENTER)
                            .offsetX(5d)
                            .offsetY(5d);


                    cartesian.legend().enabled(true);
                    cartesian.legend().fontSize(13d);
                    cartesian.legend().padding(0d, 0d, 10d, 0d);
                    anyChartView.setChart(cartesian);
                }else{
                    Toast.makeText(MealWiseVisualisation.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MealWiseResponse>> call, Throwable t) {
                Toast.makeText(MealWiseVisualisation.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
