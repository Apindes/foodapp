package com.example.DiabetApp;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.DiabetApp.DB.FoodAdapter3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class MainTwo extends Activity implements TextWatcher {

    public ArrayList tagsArray;
    private  GestureDetector gestureDetector;
    private  GestureDetector gestureDetectorOfExpListView;
    public static volatile boolean Clicked=false;
    public static Activity activity;
    private AutoCompleteTextView autoTV;

    private FoodAdapter3 adapter3;
    private GridLayout leftListGridLayout;
    private Button button;
    public static ScrollView blockLayout,leftListView;
    private TextView finalResult,kkalTV;
    public static volatile boolean isCleared=false;
    public static ExpandableListView lv;
    public static View currentRow;
    public Pattern p = Pattern.compile("^[0-9]{1,6}$");
    private AssetManager assetManager ;
    private static Animation a;
    private String [] groups = new String[]{"milk","drinks","bakery","sweets"};
    public static volatile boolean languageIsEng=true;
    Product product1_choosen;



    private static final int MILK_GROUP=1;
    private static final int BAKERY_GROUP=2;
    private static final int DRINKS_GROUP=3;
    private static final int SWEETS_GROUP=4;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    float prevVelocity=0;

    static volatile boolean isSliderOpened =true;
    int screeWidth=0;
    int screenHeight=0;
    int expandListWIDTH=0;
    int leftListWIDTH=0;
    int gridWIDTH=0;
    private int xeTVwidth;
    private final int VUSTYP_WIDTH=40;
    private TextView XE_TV,result_xe_TV;
//    private TextView xeTV;
    private  static  TranslateAnimation hide_slider;
    private ExpandableListView expandableListView;
    private Float XE_CARB_VARIABLE =12.0f;
    private Button cancelButton;
    private static String allnames[]=null;

    private int plateHeight=0;
    private TextView xe_tv_block;

    private LinearLayout header,collapsed_LL,rightPartLL,expanded_LL;
    private TranslateAnimation show_slider;
     GestureDetector.SimpleOnGestureListener expListListener;

    static {

        new Product(MILK_GROUP,"milk","молоко",200,106);
        new Product(MILK_GROUP,"kefir","кефир",250,10);
        new Product(MILK_GROUP,"cream","сливки",200,418);
        new Product(MILK_GROUP,"ice-cream","мороженое",65,145);
        new Product(MILK_GROUP,"cheesecake","сырники",75,137);

        new Product(BAKERY_GROUP,"black bread","черный хлеб",25,53);
        new Product(BAKERY_GROUP,"white bread","белый хлеб",20,52);
        new Product(BAKERY_GROUP,"crackers","сухари",15,50);
        new Product(BAKERY_GROUP,"flour","мука",15,49);
        new Product(BAKERY_GROUP,"pasta (raw)","мучные изделия (сырые)",15,56);
        new Product(BAKERY_GROUP,"pasta (boiled)","мучные изделия (готовые)",50,186);
        new Product(BAKERY_GROUP,"corn flakes","кукурузные хлопья",15,54);
        new Product(BAKERY_GROUP,"cereals (any)","хлопья",50,179);
        new Product(BAKERY_GROUP,"wheat bran","пшеничные отруби",50,168);

        new Product(DRINKS_GROUP,"cola","кола",100,38);
        new Product(DRINKS_GROUP,"apple juice(fresh)","яблочный фреш",100,46);
        new Product(DRINKS_GROUP,"orange juice(fresh)","апельсиновый фреш",110,50);
        new Product(DRINKS_GROUP,"carrot juice(fresh)","морковный фреш",125,35);
        new Product(DRINKS_GROUP,"tomato juice(fresh)","томатный фреш",300,63);
        new Product(DRINKS_GROUP,"grapefruit juice(fresh)","грейпфрутовый фреш",140,42);
        new Product(DRINKS_GROUP,"beetroot juice(fresh)","буряковый фреш",125,53);

        new Product(SWEETS_GROUP,"sugar","сахар",10,38);
        new Product(SWEETS_GROUP,"chocolate","шоколад",20,110);
        new Product(SWEETS_GROUP,"honey","мёд",12,37);

        HashMap<String,Integer> names = Product.getNames();
        allnames=new String[names.size()];
        names.keySet().toArray(allnames);

    }

    private MotionEvent currentMotionEvent;
    public static AlphaAnimation fadeIn,fadeOut;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        isSliderOpened =true;
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_eng:
                if(!languageIsEng){
                    XE_CARB_VARIABLE =12.0f;
                    languageIsEng=true;
                    result_xe_TV.setText("carbs");
                    lv.setAdapter(adapter3);
                    updateLeftList(leftListGridLayout);
                    updateBlockLayout(blockLayout,languageIsEng,product1_choosen);
                    if(blockLayout!=null){
                        xe_tv_block.setText("carbs");
                    }

                    calculateResult(leftListGridLayout);

                }
                return true;

            case R.id.action_rus:
                if(languageIsEng){
                    languageIsEng=false;
                    XE_CARB_VARIABLE =1.0f;
                    lv.setAdapter(adapter3);
                    result_xe_TV.setText("XE");
                    updateLeftList(leftListGridLayout);
                    updateBlockLayout(blockLayout,languageIsEng,product1_choosen);
                    if(blockLayout!=null){
                        xe_tv_block.setText("XE");
                    }
                    calculateResult(leftListGridLayout);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateLeftList(GridLayout gridLayout) {
        int rowCount = gridLayout.getRowCount();
        if(rowCount==0) return;
        for(int i=0;i<rowCount;i++){
            if(!(gridLayout.getChildAt(0)instanceof ScrollView)) {
                LinearLayout ll = (LinearLayout) gridLayout.getChildAt(i);

                Product p = (Product) ll.getTag();
                Button b = (Button) ll.findViewById(R.id.button);
                TextView xe_row_tv  = (TextView)ll.findViewById(R.id.XeRowTV);
                String pastValue = xe_row_tv.getText().toString();
                float pvalue = Float.parseFloat(pastValue);
                float newValue = 0;
                if (languageIsEng) {
                    b.setText(p.getName());
                    newValue=pvalue*12;
                    xe_row_tv.setText(newValue+"");
                }
                if (!languageIsEng) {
                    b.setText(p.getAuxName());
                    newValue=pvalue/12;
                    xe_row_tv.setText(newValue+"");
                }
            }
        }
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        activity=this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screeWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        result_xe_TV  =(TextView)findViewById(R.id.result_xeTV);
        expandListWIDTH = (int )(140* Resources.getSystem().getDisplayMetrics().density);   // =210
        leftListWIDTH = screeWidth-expandListWIDTH;
        gridWIDTH=leftListWIDTH;
        xeTVwidth = (int)(33* Resources.getSystem().getDisplayMetrics().density);
        leftListView=(ScrollView)findViewById(R.id.myview);
        leftListView.layout(0,0,leftListWIDTH,leftListView.getHeight());
        assetManager=getAssets();
        tagsArray=new ArrayList();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        leftListGridLayout = (GridLayout)findViewById(R.id.grid);

        collapsed_LL  = (LinearLayout)getLayoutInflater().inflate(R.layout.left_header_collapsed,null);
        rightPartLL = (LinearLayout)findViewById(R.id.rightPartLL);
        header = (LinearLayout)findViewById(R.id.header);

        expanded_LL=(LinearLayout)getLayoutInflater().inflate(R.layout.left_header_expand,null);
        header.addView(expanded_LL);
        plateHeight = (int)(60*Resources.getSystem().getDisplayMetrics().density);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if(!isSliderOpened){
                    isSliderOpened =true;
                    header.removeView(header.getChildAt(0));
                    header.addView(collapsed_LL);
                    return true;
                }

                return false;
            }
        });

    /**Animation of right list  & left list!!!*/
        expListListener= new GestureDetector.SimpleOnGestureListener(){

            @Override
            public synchronized boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                expandableListView.invalidateViews();

                return false;
            }

            @Override
            public synchronized boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


                if(velocityX>0 ) {
                    if(isSliderOpened) {

                        header.removeView(header.getChildAt(0));
                        header.addView(expanded_LL);
                        rightPartLL.startAnimation(hide_slider);

                        Log.d("==DIABET_APPLICATION ==","removed ll_collapsed();");
                        Toast.makeText(activity,"isSliderOpened= "+isSliderOpened,5000).show();
//                        isSliderOpened=false;
                        for(int i=0;i<expandableListView.getCount();i++) expandableListView.collapseGroup(i);
                        return true;
                    }

                }
                if(velocityX<0){
                    if(!isSliderOpened) {
                        header.removeView(header.getChildAt(0));
                        header.addView(collapsed_LL);
                        rightPartLL.startAnimation(show_slider);

                        Toast.makeText(activity,"isSliderOpened= "+isSliderOpened,5000).show();
                        Log.d("==DIABET_APPLICATION ==","removed ll_expanded();");

//                        isSliderOpened=true;
                        return true;
                    }

                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                currentMotionEvent=e;
                return true;
            }

        };


        gestureDetectorOfExpListView = new GestureDetector(activity,expListListener);

        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return velocityX<0;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

        } ;

        gestureDetector = new GestureDetector(activity,listener);


        lv=expandableListView;

        kkalTV = (TextView)findViewById(R.id.kkalTV);
        kkalTV.setText(0+"");
        Button clearButton = (Button) findViewById(R.id.clearButton);
        finalResult = (TextView)findViewById(R.id.finalResult);



        autoTV = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoTV.addTextChangedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,allnames);
        autoTV.setAdapter(adapter);
        autoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                im.showSoftInputFromInputMethod(autoTV.getWindowToken(),0);
                im.showSoftInput(autoTV,0);

            }
        });

        ArrayList<Product>[]arr=Product.getProducts();
        adapter3=new FoodAdapter3(this,arr,expandableListView);
        expandableListView.setAdapter(adapter3);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCleared = true;
                if (leftListGridLayout.getRowCount() > 0) {
                    leftListGridLayout.removeAllViews();
                    finalResult.setText("0.0");
                    kkalTV.setText(0 + "");
                }
                isSliderOpened =true;

            }
        });
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return (gestureDetectorOfExpListView.onTouchEvent(motionEvent));

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {


            @Override
            public boolean onChildClick(final ExpandableListView expandableLV, final View view1, final int i, final int i1, long l) {
                if (!Clicked) {
                    Clicked = true;
                } else {
                    Clicked = false;
                }
            try{
                int color_default  = Color.parseColor("#72000000");
//                view1.setBackgroundColor(color_default);



                adapter3.setClicked(i, i1);

                final CheckedTextView tv = (CheckedTextView) view1.findViewById(R.id.textItem);

                if (tv.isEnabled()) {

                    final Product product = (Product) tv.getTag();
                    final String itemText = product.getName();
                    final LinearLayout rowItem = (LinearLayout) getLayoutInflater().inflate(R.layout.item_button, null);

                    rowItem.setTag(product);

                    final String callories_CONSTANT = product.getCalloriess() + "";
                    currentRow = rowItem;

                    final TextView gramTV = (TextView) rowItem.findViewById(R.id.gramRowTV);
                    XE_TV = (TextView) rowItem.findViewById(R.id.XeRowTV);
                    final TextView callories_LeftListTV = (TextView) rowItem.findViewById(R.id.calloriesRowHiddenTV);

                    XE_TV.setText(XE_CARB_VARIABLE + "");

                    gramTV.setText(product.getAmount() + "");


                    /**delete probably this!*/
                    callories_LeftListTV.setText(callories_CONSTANT);
                    callories_LeftListTV.setTag(callories_CONSTANT);
                    /***/

                    View.OnClickListener displayRowListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.isEnabled()) {

                                blockLayout = (ScrollView) getLayoutInflater().inflate(R.layout.detail_block, null);
                                Product product1 = (Product) view.getTag();
                                product1_choosen = product1;

                                TextView gramsTV = (TextView) blockLayout.findViewById(R.id.gramTV);
                                TextView nameItem = (TextView) blockLayout.findViewById(R.id.itemTextView);
                                xe_tv_block = (TextView) blockLayout.findViewById(R.id.XeTV_block);

                                if (product1.getIsGroup().equals("drinks") || product1.getName().matches("milk|kefir|cream")) {
                                    gramsTV.setText("ml");
                                } else gramsTV.setText("grams");

                                /**grams  in 1XE*/
                                final int productAmount = product1.getAmount();
                                /**Callories in 1XE*/
                                final int callories = product1.getCalloriess();

                                Button nameButton = (Button) view.findViewById(R.id.button);

                                String itemName = languageIsEng ? product1.getName() : product1.getAuxName();

                                updateBlockLayout(blockLayout, languageIsEng, product1);

                                ImageView image = (ImageView) blockLayout.findViewById(R.id.imageView);
                                String[] pictName;

                                try {
                                    pictName = assetManager.list(groups[i]);
                                    for (String n : pictName) {
                                        String name = n.substring(0, n.length() - 4);
                                        if (product1.getName().equals(name)) {
                                            InputStream in = assetManager.open(groups[i] + "/" + n);
                                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                                            image.setImageBitmap(bitmap);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String newName = Character.valueOf(itemName.charAt(0)).toString().toUpperCase() + itemName.substring(1, itemName.length());
                                nameItem.setText(newName);

                                final TextView caloriesTV = (TextView) blockLayout.findViewById(R.id.caloriesTV);
                                caloriesTV.setText(callories + "");

                                final TextView xeTVresult = (TextView) blockLayout.findViewById(R.id.xeTV_result);
                                final EditText editAmount = (EditText) blockLayout.findViewById(R.id.amountEditText);

                                xeTVresult.setText(XE_CARB_VARIABLE + "");
//                                XE_CARB_VARIABLE = Float.parseFloat(XE_TV.getText().toString());

                                TableLayout display = (TableLayout) blockLayout.findViewById(R.id.display);
                                /**Происходит пересчет во вьюхах   blockLayout(display)   ХЕ и калорий*/
                                display.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view1) {

                                        int calloriesOfOneXE = Integer.parseInt(callories_CONSTANT);

                                        calculateXEandCalloriesInBlockLayout(editAmount, xeTVresult, caloriesTV, productAmount, calloriesOfOneXE);
                                    }
                                });


                                /**Происходит занесение данных в левый ListView*/

                                Button appoveButton = (Button) display.findViewById(R.id.approveButton);
                                appoveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String nAmount = editAmount.getText().toString();
                                        if (p.matcher(nAmount).matches()) {

                                            setDataInLeftListView(itemText, nAmount, productAmount, callories_CONSTANT, editAmount, xeTVresult);

                                            isSliderOpened = true;

                                        }
                                    }
                                });


                                cancelButton = (Button) display.findViewById(R.id.cancelButton);

                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        blockLayout.setVisibility(View.INVISIBLE);
                                        blockLayout = null;
                                        for (int i = 0; i < leftListGridLayout.getRowCount(); i++) {
                                            View mv = leftListGridLayout.getChildAt(i);
                                            if (!mv.isEnabled()) mv.setEnabled(true);
                                        }
                                    }
                                });
                                /**Calculate XE amount in the fields of block Layout !  */

                                editAmount.setText(nameButton.getTag().toString());

                                float res1 = (Float.valueOf(editAmount.getText().toString()) / productAmount) * XE_CARB_VARIABLE;
                                xeTVresult.setText(res1 + "");

                                getWindow().addContentView(blockLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                view.setEnabled(false);
                            }
                        }
                    };

                    rowItem.setOnClickListener(displayRowListener);

                    rowItem.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View view, MotionEvent motionEvent) {

                            if (gestureDetector.onTouchEvent(motionEvent)) {

                                Button b = (Button) view.findViewById(R.id.button);

                                for (int k = 0; k < expandableListView.getChildCount(); k++) {

                                    LinearLayout v = (LinearLayout) expandableListView.getChildAt(k);
                                    CheckedTextView tv1 = (CheckedTextView) v.findViewById(R.id.textItem);

                                    if (tv1 == null) {
                                        tv1 = new CheckedTextView(activity);
                                        tv1.setText(b.getText());
                                        tv1.setChecked(true);
                                        tv1.setEnabled(false);
                                        String buttontext = b.getText().toString();
                                        String tvtext = tv1.getText().toString();

                                        if (buttontext.equals(tvtext)) {
                                            adapter3.setClicked(i, i1);
                                            leftListGridLayout.removeView(view);
                                            calculateResult(leftListGridLayout);
                                            return true;
                                        }
                                    }
                                }
                                removeRowFromLeftListView(view);

                                return true;

                            }
                            return false;
                        }

                    });


                    button = (Button) rowItem.findViewById(R.id.button);
                    String buttonName = languageIsEng ? itemText : product.getAuxName();
                    button.setText(buttonName);
                    button.setTag(product.getAmount());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rowItem.callOnClick();

                        }
                    });

                    leftListGridLayout.addView(rowItem);
                    calculateResult(leftListGridLayout);
                    return false;
                } else {
                    String itemText = ((Product) tv.getTag()).getName();
                    leftListGridLayout.removeView(findView(itemText));
                    calculateResult(leftListGridLayout);
                    return false;
                }

            }finally{
                    if(leftListGridLayout.getRowCount()!=0){
                        isCleared=false;
                    }
                }
            }

            private void removeRowFromLeftListView(View view) {
                a =new TranslateAnimation(view.getX(),-300,view.getY(),view.getY()) ;
                a.setDuration(250);a.setFillAfter(true);
            }

        });
        initAnimations();
        final Button okAutoBoxButton = (Button) findViewById(R.id.okAutoBoxButton);
        okAutoBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int groupIndex=-1;

                String patternName = autoTV.getText().toString();
                if(patternName!=null&&patternName.trim()!=""){
                    HashMap<String,Integer> names = Product.getNames();
                    allnames=new String[names.size()];
                    names.keySet().toArray(allnames);

                    for (String name : allnames) {
                       if(patternName.equals(name)){
                           groupIndex = names.get(name);
                           expandableListView.expandGroup(groupIndex);
                           ArrayList <Product> productList = Product.getProducts()[groupIndex];
                           for(int i=0;i<productList.size();i++){
                               Product product = productList.get(i);
                               String nameProduct = languageIsEng?product.getName():product.getAuxName();
                               if(nameProduct.equals(name)){
                                   int childIndex = i;
                                   adapter3.setChildFounded(groupIndex, childIndex);
                                   Toast.makeText(activity,"group: "+groupIndex+"child: "+childIndex,5000).show();
//                                   return;
//                                   break;
                               }
                           }

//

                       }
                    }
                }
                autoTV.setText("");
                isCleared=false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(blockLayout==null) {
            super.onBackPressed();
        }
        if(autoTV.isFocusable()) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(autoTV.getWindowToken(), 0);
        }
        if(blockLayout!=null){
           cancelButton.callOnClick();
        }
    }

    public void calculateXEandCalloriesInBlockLayout(EditText editAmount, TextView xeTV,TextView callTV,int gramsOfOneXE,int calloriesOfOneXE){
        String weight = editAmount.getText().toString();

        if (p.matcher(weight).matches()) {
            Float amount_edited = Float.parseFloat(weight);

            String result = String.valueOf(((amount_edited / gramsOfOneXE)* XE_CARB_VARIABLE));
            xeTV.setText(result);
            String resultCall = String.valueOf((int)((amount_edited / gramsOfOneXE)*calloriesOfOneXE));
            callTV.setText(resultCall);
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(editAmount.getWindowToken(), 0);
        }
    }

    public void setDataInLeftListView(String nameOfProduct,String changedWeight,int weightOfOneXE,String calloriesOfOneXE,EditText editAmount,TextView xeTV){
        Float amount_edited = Float.parseFloat(changedWeight);
        float res = (amount_edited / weightOfOneXE)* XE_CARB_VARIABLE;
        xeTV.setText(res + "");
        String resultCall = String.valueOf((int)((amount_edited / weightOfOneXE)*Float.parseFloat(calloriesOfOneXE)));

        View view = findView(nameOfProduct);
        TextView gramTV = (TextView) view.findViewById(R.id.gramRowTV);
        TextView XE_TV = (TextView) view.findViewById(R.id.XeRowTV);
        TextView kall_TV = (TextView) view.findViewById(R.id.calloriesRowHiddenTV);

        gramTV.setText(changedWeight);
        XE_TV.setText(res + "");

        kall_TV.setText(resultCall+"");
        calculateResult(leftListGridLayout);

        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(editAmount.getWindowToken(), 0);

        blockLayout.setVisibility(View.INVISIBLE);
        blockLayout = null;
        for(int i=0;i< leftListGridLayout.getRowCount();i++){
            View mv = leftListGridLayout.getChildAt(i);
            if(!mv.isEnabled()) mv.setEnabled(true);
        }
    }

    public void updateBlockLayout(ScrollView blockLayout1,boolean isEnglish,Product product){
        if(blockLayout1!=null) {

            TextView weightTV_block1 = (TextView) blockLayout1.findViewById(R.id.weightTV_block);
            TextView XeTV_block1 = (TextView) blockLayout1.findViewById(R.id.XeTV_block);
            TextView calloriesTV_block1 = (TextView) blockLayout1.findViewById(R.id.calloriesTV_block);
            TextView nameItem1 = (TextView) blockLayout1.findViewById(R.id.itemTextView);
            String item_name;
            TextView xe_or_carbs_TV = (TextView)blockLayout1.findViewById(R.id.xeTV_result);
            String XEprevious = xe_or_carbs_TV.getText().toString();
            float xePast = Float.parseFloat(XEprevious);

            if (isEnglish) {
                item_name = product.getName();
                String newName = Character.valueOf(item_name.charAt(0)).toString().toUpperCase() + item_name.substring(1, item_name.length());
                weightTV_block1.setText(R.string.weightTV_block_eng);
                XeTV_block1.setText(R.string.XeTV_block_eng);
                calloriesTV_block1.setText(R.string.calloriesTV_block_eng);
                nameItem1.setText(newName);
                float res = xePast*12;
                xe_or_carbs_TV.setText(res+"");
            }
            if (!isEnglish) {
                item_name = product.getAuxName();
                String newName = Character.valueOf(item_name.charAt(0)).toString().toUpperCase() + item_name.substring(1, item_name.length());
                weightTV_block1.setText(R.string.weightTV_block_rus);
                XeTV_block1.setText(R.string.XeTV_block_rus);
                calloriesTV_block1.setText(R.string.calloriesTV_block_rus);
                nameItem1.setText(newName);
                float res = xePast/12;
                xe_or_carbs_TV.setText(res+"");
            }
        }
    }



    public View findView(String text){
        for(int i=0;i< leftListGridLayout.getChildCount();i++){
            View v = leftListGridLayout.getChildAt(i);
            LinearLayout ll = (LinearLayout)v;
            Product p = (Product)ll.getTag();
            if( p.getName().equals(text)){
                return v;
            }
        }
        return null;
    }

    public void calculateResult(GridLayout e ){
        float res=0;
        int res2=0;
       for(int i=0;i<e.getRowCount();i++){
           if(!(e.getChildAt(i) instanceof ScrollView)) {
               LinearLayout l = (LinearLayout) e.getChildAt(i);
               TextView tv = (TextView) l.findViewById(R.id.XeRowTV);
               res += Float.parseFloat(tv.getText().toString());
               TextView calltv = (TextView) l.findViewById(R.id.calloriesRowHiddenTV);
               res2 += Integer.parseInt(calltv.getText().toString());
           }
       }
        finalResult.setText(res+"");
        kkalTV.setText(res2+"");
    }




    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screeWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        if(hasFocus){
            isSliderOpened =false;
            TranslateAnimation t1 = new TranslateAnimation(activity,null);
            t1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rightPartLL.layout(480 - 40, 0, 480 - 40 + expandListWIDTH, rightPartLL.getHeight());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            rightPartLL.startAnimation(t1);
            rightPartLL.invalidate();
        }
    }

    void initAnimations(){


        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        hide_slider =new TranslateAnimation(activity,null) ;
        hide_slider.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
//                isSliderOpened=true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSliderOpened=false;
                rightPartLL.layout(screeWidth - VUSTYP_WIDTH, 0, screeWidth - VUSTYP_WIDTH + expandListWIDTH, rightPartLL.getHeight());
                leftListView.layout(0, 0, screeWidth - VUSTYP_WIDTH, leftListView.getHeight());
                leftListGridLayout.layout(0, 0, screeWidth - VUSTYP_WIDTH, leftListGridLayout.getHeight());

                int height = plateHeight;
                int res = plateHeight;
                for (int i = 0; i < leftListGridLayout.getRowCount(); i++) {
                    if(!(leftListGridLayout.getChildAt(i) instanceof ScrollView)) {
                        LinearLayout v = (LinearLayout) leftListGridLayout.getChildAt(i);
                        v.layout(0, (int) v.getY(), 425, res);
                        res += height;

                        final TextView xe_tv = (TextView) v.findViewById(R.id.XeRowTV);
                        xe_tv.layout(350, (int) xe_tv.getY(), 410, xe_tv.getHeight() + 18);

                        TextView callTV = (TextView) v.findViewById(R.id.calloriesRowHiddenTV);
                        callTV.layout(280, (int) callTV.getY(), 350, callTV.getHeight() + 16);
                        callTV.setVisibility(View.VISIBLE);
                    }

                }

                leftListGridLayout.invalidate();
                animation.setFillAfter(true);
                rightPartLL.invalidate();

                Log.d("==DIABET_APPLICATION ==","hideSlider();");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        show_slider =new TranslateAnimation(activity,null);
        show_slider.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                isSliderOpened=false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSliderOpened=true;
                rightPartLL.layout(screeWidth - expandListWIDTH, 0, screeWidth, rightPartLL.getHeight());
                leftListView.layout(0, 0, leftListWIDTH, leftListView.getHeight());
                leftListGridLayout.layout(0, 0, gridWIDTH, leftListGridLayout.getHeight());

                int height = plateHeight;
                int res = plateHeight;
                for (int i = 0; i < leftListGridLayout.getRowCount(); i++) {
                    if(!(leftListGridLayout.getChildAt(i) instanceof ScrollView)) {
                        LinearLayout v1 = (LinearLayout) leftListGridLayout.getChildAt(i);
                        v1.layout((int) v1.getX(), (int) v1.getY(), 267, res);
                        res += height;
                        TextView callTV = (TextView) v1.findViewById(R.id.calloriesRowHiddenTV);
                        callTV.setVisibility(View.INVISIBLE);
                    }
                }
                expListListener.onDown(currentMotionEvent);
                animation.setFillAfter(true);
                rightPartLL.invalidate();

                Log.d("==DIABET_APPLICATION ==","showSlider();");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
