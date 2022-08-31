package com.example.test_t1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class button1Fragment extends Fragment
{
    private View view;
    private LineChart chart;//
    private button2Fragment btn2Fragment;
    private String fac_line1;
    private List csv_list;
    private Iterator iter_csv;
    private  List list_csv;

    ArrayList<issue_data> issueDataList;
    MainActivity mainActivity ;
    private boolean is_item= false;
    int i;
    int last_index;
    MyAdapter myAdapter;
    ListView listView;
    Handler handler;
    Thread data_run;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onStop()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data_list", issueDataList);
        bundle.putInt("cur_index",i);
        this.setArguments(bundle);
        data_run.interrupt();
        super.onStop();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("1번 프래그먼트실행");
        issueDataList = new ArrayList<issue_data>();
        i=0;
        if(getArguments() != null)
        {
            Bundle bundle = getArguments();
            issueDataList = bundle.getParcelableArrayList("data_list");
            i= bundle.getInt("cur_index");
        }
        view = inflater.inflate(R.layout.fragment_btn1,container,false);
        listView = (ListView) view.findViewById(R.id.listview);
        handler = new Handler();
        //이슈데이터리스트를 받아서 체크
        myAdapter = new MyAdapter(getContext(),issueDataList);
        listView.setAdapter(myAdapter);
        mainActivity = new MainActivity();
        Read_csv read_csv = new Read_csv();
        csv_list = read_csv.readCSV();
        list_csv = (List) csv_list.get(0);

        //전체 사이즈
        last_index = list_csv.size();
        iter_csv= list_csv.iterator();


        bottomNavigationView = view.findViewById(R.id.nav_view);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                //밑에 내비바클릭한것과 같이 동작 하면 댐
            Bundle bundle = new Bundle();
            bundle.putString("line", myAdapter.getItem(position).getFac_line());
            btn2Fragment = new button2Fragment();
            btn2Fragment.setArguments(bundle);
            Navigation.findNavController(view).navigate(R.id.action_navigation_btn1_to_navigation_btn2,bundle); //controller

                //navigate()는 호출되면 호출대상을 스택맨위에 올려놓음
                //navigate()로 이동되면 뒤로가기를 해야 NavController.navigateup(), NavController.popBackStack()메서드가 호출되어 스택최상단대상을 삭제 함.
                //navigate()말고 , 버튼누른작용
            }
        });

        //차트코드
        chart = (LineChart) view.findViewById(R.id.data_chart);
        chart.setDrawGridBackground(true);
        chart.setBackgroundColor(Color.BLACK);
        chart.setGridBackgroundColor(Color.BLACK);

// description text
        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();
        des.setEnabled(true);
        des.setText("Factory DATA");
        des.setTextSize(15f);
        des.setTextColor(Color.WHITE);

// touch gestures (false-비활성화)
        chart.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
//auto scale
        chart.setAutoScaleMinMaxEnabled(false);

// if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        //chart.setData(BarDataSet( ,"안녕"));
        //chart.setVisibleYRange(0.03f,0.03f, YAxis.AxisDependency.LEFT);//
//X축
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);

//Legend
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);

//Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.purple_200));
        leftAxis.setDrawGridLines(false); //단위선
        leftAxis.setGridColor(getResources().getColor(R.color.purple_700));
        leftAxis.setAxisMaximum(0.06f);
        leftAxis.setAxisMinimum(-0.06f);
        //leftAxis.setDrawLimitLinesBehindData(true);

        //상한선 조정
        LimitLine upper_limit = new LimitLine(0.03f, "Max Value");
        upper_limit.setLineWidth(2f);
        upper_limit.enableDashedLine(10f, 10f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(10f);

        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.addLimitLine(upper_limit);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

// don't forget to refresh the drawing
        chart.invalidate();

       //그래프 데이터표기부
        data_run =new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    //실시간 데이터 그래프 띄워줌
                    while (i<last_index)
                    {
                        String temp=list_csv.get(i).toString();
                        float num_f = Float.parseFloat(temp);
                        addEntry(num_f);
                        //표기하는 num_f값이 상한값을 넘는지 확인

                        if(num_f > 0.03f)
                        {
                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    add_Item(0,0);
                                    myAdapter.notifyDataSetChanged();
                                    listView.setAdapter(myAdapter);

                                }
                            }, 30);
                        }
                        i++;
                        Thread.sleep(30);
                    }
                } catch (Exception e)
                {
                }
            }
        });

        data_run.start();
        return view;
    }

    public void addEntry(double num) {

        LineData data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(30);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getXMax(), 50f, YAxis.AxisDependency.LEFT);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Real-time Line Data");
        set.setLineWidth(1f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.white));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }

    public void make_Item(int image, String issue ,String fac_line, String moter_name, String day_time ,String moter_state,String solution)
    {
        System.out.println("아이템 추가");
        issueDataList.add(new issue_data(image, issue, fac_line, moter_name, day_time, moter_state, solution));
    }



    public  void add_Item(int server_issue , int server_state)
    {
        //서버로 부터 화면에 띄워줄 아이템데이터를 받는다. 이슈,라인,모터명, 시기, 고장원인, 해결방안
        int s_issue = server_issue;
        int s_moter_state= server_state;

        //받은데이터의 int값을 배열에집어넣어 적합한 str으로 전환- 바로 집어넣어도대고

        //전환한str을 add에 전달
        String[]issues = {"정상", "고장","점검","교체","위험"};
        String[]fac_lines={"A1","A2","A3","A4","A5"};
        String[]moter_states={"정상","베어링불량","회전체불평형", "축정렬불량","벨트느슨함"};
        String[]solution={"부품교체","점검필요"};

        make_Item(R.drawable.ic_launcher_background,issues[s_issue], "A2","JHFF1122모터","2022-08-09", moter_states[s_moter_state],"모터교체필요");

    }




}