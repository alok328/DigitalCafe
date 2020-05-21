package com.alok328raj.digitalcafe.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alok328raj.digitalcafe.API.ApiClient;
import com.alok328raj.digitalcafe.API.Model.CustomDataEntry;
import com.alok328raj.digitalcafe.API.Model.MealWiseResponse;
import com.alok328raj.digitalcafe.MealWiseVisualisation;
import com.alok328raj.digitalcafe.R;
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

public class MealWiseFragment extends Fragment {

    View v;

    public MealWiseFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
//            fun();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.meal_wise_stats_fragment, container,false);
        return v;
    }

    private void fun() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.103:5000")
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

                    AnyChartView anyChartView = getView().findViewById(R.id.any_chart);
                    anyChartView.setProgressBar(getView().findViewById(R.id.progress));
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
                    Toast.makeText(getContext(), "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MealWiseResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
