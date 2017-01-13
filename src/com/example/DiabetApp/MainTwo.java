package com.example.DiabetApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.DiabetApp.DB.FoodAdapter4;
import com.example.DiabetApp.com.artem.ViewContainer;
import provider.Buffer;
import provider.ImagesProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class MainTwo extends Activity implements TextWatcher {

    private static String LOG_TAG = "==DIABET_APPLICATION ==" ;
    public ArrayList tagsArray;
    private  GestureDetector gestureDetector;
    private  GestureDetector gestureDetectorOfExpListView;
    public static volatile boolean Clicked=false;
    public static Activity activity;
    private static AutoCompleteTextView autoTV;
    private static InputMethodManager inputMethodManager;

    public static FoodAdapter4 adapter4;
    private GridLayout leftListGridLayout;
    private Button button,plusButton;
    public static ScrollView blockLayout,leftListView,constructorBlock;
    private TextView finalResult,kkalTV;
    public static volatile boolean isCleared=false;
    public static ExpandableListView lv;
    public static View currentRow;
    private Pattern p = Pattern.compile("^[0-9]{1,6}$");
    private AssetManager assetManager ;
    private static Animation a;
    public static volatile boolean languageIsEng=true;
    private Product product1_choosen;
    private static Handler handler;
    private Bitmap bitmap=null;

    private static final int MILK_GROUP=1;
    private static final int BAKERY_GROUP=2;
    private static final int DRINKS_GROUP=3;
    private static final int SWEETS_GROUP=4;
    private static final int VEGGIES_GROUP=5;
    private static final int FRUITS_GROUP=6;
    private static final int MEAT_GROUP=7;
    private static final int ADDITIONAL_GROUP=8;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int ADD =1;
    private static final int REMOVING =0;
    static final int GALLERY_REQUEST = 1;
    static ViewContainer viewContainer =new ViewContainer();

    private int screeWidth=0;
    private int expandListWIDTH=0;
    private int leftListWIDTH=0;
    private int gridWIDTH=0;
    private int ruchkaWIDTH=0;

    private TextView XE_TV,result_xe_TV;

    private  static  TranslateAnimation hide_slider;
    private static TranslateAnimation show_slider;

    public static ExpandableListView expandableListView;
    private Float XE_CARB_VARIABLE =12.0f;
    private Button cancelButton,cancel_button;
    private static String allnames[]=null;
    private int rowHeight =0;
    private TextView xe_tv_block;

    private LinearLayout header, block_in_header,ruchka;
    private RelativeLayout rightPartLL;
    private TextView call_headerTV,XE_headerTV,XEhidden_headerTV;

    private GestureDetector.SimpleOnGestureListener expListListener;
    private static ArrayAdapter<String> autoTVadapter ;
    public static  Typeface font;
    public ImageView imageViewConstuctor;
    private ImagesProvider imagesProvider;
    static Product.Container adapterHolder;
    public static DBHelper dbHelper;




    static {
        //Adding order should be the same as the order of groups in the final list of products!
        new Product(BAKERY_GROUP,"black bread","черный хлеб",25,53);
        new Product(BAKERY_GROUP,"white bread","белый хлеб",20,52);
        new Product(BAKERY_GROUP,"crackers","сухари",15,50);
        new Product(BAKERY_GROUP,"flour","мука",15,49);
        new Product(BAKERY_GROUP,"pasta (raw)","мучные изделия (сырые)",15,56);
        new Product(BAKERY_GROUP,"pasta (boiled)","мучные изделия (готовые)",50,186);
        new Product(BAKERY_GROUP,"corn flakes","кукурузные хлопья",15,54);
        new Product(BAKERY_GROUP,"cereals (any)","хлопья",50,179);
        new Product(BAKERY_GROUP,"wheat bran","пшеничные отруби",50,168);


        new Product(MILK_GROUP,"milk","молоко",200,106);
        new Product(MILK_GROUP,"kefir","кефир",250,10);
        new Product(MILK_GROUP,"cream","сливки",200,418);
        new Product(MILK_GROUP,"ice-cream","мороженое",65,145);
        new Product(MILK_GROUP,"cheesecake","сырники",75,137);


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

        new Product(VEGGIES_GROUP,"potato(boiled)","картофель(вареный)",75,52);
        new Product(VEGGIES_GROUP,"carrot","морковь",200,82);
        new Product(VEGGIES_GROUP,"beetroot","свёкла",150,65);
        new Product(VEGGIES_GROUP,"pea","горох",100,81);
        new Product(VEGGIES_GROUP,"beans","фасоль",50,51);
        new Product(VEGGIES_GROUP,"tomato","томат",300,54);
        new Product(VEGGIES_GROUP,"french fries","картофель фри",35,110);
        new Product(VEGGIES_GROUP,"eggplant","баклажан",200,50);
        new Product(VEGGIES_GROUP,"zucchini","кабачок",190,33);
        new Product(VEGGIES_GROUP,"cucumber","огурец",400,64);
        new Product(VEGGIES_GROUP,"broccoli","брокколи",250,85);
        new Product(VEGGIES_GROUP,"cauliflower","цветная капуста",240,60);
        new Product(VEGGIES_GROUP,"radish","редис",300,48);
        new Product(VEGGIES_GROUP,"spinach","шпинат",580,133);
        new Product(VEGGIES_GROUP,"bell paper","болгарский перец",250,75);
        new Product(VEGGIES_GROUP,"pumpkin","тыква",200,52);

        new Product(FRUITS_GROUP,"apple","яблоко",105,50);
        new Product(FRUITS_GROUP,"apricot","абрикос",115,53);
        new Product(FRUITS_GROUP,"pineapple","ананас",100,48);
        new Product(FRUITS_GROUP,"orange","апельсин",143,55);
        new Product(FRUITS_GROUP,"banana","банан",54,60);
        new Product(FRUITS_GROUP,"cherry","вишня",106,52);
        new Product(FRUITS_GROUP,"pomegranate","гранат",102,53);
        new Product(FRUITS_GROUP,"grapefruit","грейпфрут",165,58);
        new Product(FRUITS_GROUP,"pear","груша",112,47);
        new Product(FRUITS_GROUP,"kiwi","киви",150,71);
        new Product(FRUITS_GROUP,"lemon","лимон",334,104);
        new Product(FRUITS_GROUP,"mango","манго",86,58);
        new Product(FRUITS_GROUP,"mandarin","мандарин",140,53);
        new Product(FRUITS_GROUP,"peach","персик",115,51);
        new Product(FRUITS_GROUP,"plum","слива",121,52);
        new Product(FRUITS_GROUP,"persimmon","хурма",75,47);
        new Product(FRUITS_GROUP,"grape","виноград",69,48);
        new Product(FRUITS_GROUP,"cranberry","клюква",250,70);
        new Product(FRUITS_GROUP,"raspberries","малина",133,55);
        new Product(FRUITS_GROUP,"sea buckthorn","облепиха",218,65);

        new Product(MEAT_GROUP,"meat","облепиха",218,65);

    }

    private SharedPreferences mSettings;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(block_in_header !=null) {
            makeHeaderSmaller();
        }
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_eng:
                if(!languageIsEng){
                    XE_CARB_VARIABLE =12.0f;
                    languageIsEng=true;
                    adapter4.notifyDataSetChanged();
                    result_xe_TV.setText("carbs");

                    XE_headerTV.setText(R.string.XeTV_block_eng);
                    XEhidden_headerTV.setText(R.string.XeTV_block_eng);

                    updateLeftList(leftListGridLayout);
                    updateBlockLayout(blockLayout,languageIsEng,product1_choosen);
                    updateConstructorBlock(constructorBlock,languageIsEng,XE_CARB_VARIABLE);
                    calculateResult(leftListGridLayout);

                }
                return true;


            case R.id.action_rus:
                if(languageIsEng){
                    languageIsEng=false;
                    adapter4.notifyDataSetChanged();
                    XE_CARB_VARIABLE =1.0f;
                    result_xe_TV.setText("XE");

                    XE_headerTV.setText(R.string.XeTV_block_rus);
                    XEhidden_headerTV.setText(R.string.XeTV_block_rus);

                    updateLeftList(leftListGridLayout);
                    updateBlockLayout(blockLayout,languageIsEng,product1_choosen);
                    updateConstructorBlock(constructorBlock,languageIsEng,XE_CARB_VARIABLE);
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
                    xe_row_tv.setText(convertFloat(newValue)+"");
                }
                if (!languageIsEng) {
                    b.setText(p.getAuxName());
                    newValue=pvalue/12;
                    xe_row_tv.setText(convertFloat(newValue)+"");
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(outState);
//        outState.putSerializable("p",adapter4);
//        viewContainer.addViewToContainer(leftListGridLayout);
//        outState.putSerializable("grid",viewContainer);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        adapter4  = (FoodAdapter4) savedInstanceState.getSerializable("p");
//        leftListGridLayout =(GridLayout)savedInstanceState.getSerializable("grid");
    }



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        activity=this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screeWidth = metrics.widthPixels;


        /**D A T A B A S E!!!*/
        dbHelper = new DBHelper(activity);
        final SQLiteDatabase database =  dbHelper.getReadableDatabase();
        if (database!=null) {
            Cursor c = database.rawQuery("select * from products", null);
            logCursor(c, "Table position");
            c.close();
        }



        font = Typeface.createFromAsset(getAssets(), "fonts/Lato-Bold.ttf");

        ruchkaWIDTH =(int)(15*Resources.getSystem().getDisplayMetrics().density);
        result_xe_TV  =(TextView)findViewById(R.id.result_xeTV);
        expandListWIDTH = (int )(140* Resources.getSystem().getDisplayMetrics().density);   // =210
        leftListWIDTH = screeWidth-expandListWIDTH;
        gridWIDTH=leftListWIDTH;

        ruchka=(LinearLayout)findViewById(R.id.ruchka);
        leftListView=(ScrollView)findViewById(R.id.myview);
        leftListView.layout(0,0,leftListWIDTH,leftListView.getHeight());
        assetManager=getAssets();
        tagsArray=new ArrayList();
        handler = new Handler();

        imagesProvider = new ImagesProvider(activity);

        plusButton = (Button)findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constructorBlock = (ScrollView) getLayoutInflater().inflate(R.layout.constructor_block, null);
                final TextView amountEditText = (TextView)constructorBlock.findViewById(R.id.amountEditText);
                final EditText calloriesEdtText = (EditText)constructorBlock.findViewById(R.id.caloriesEditText);
                final EditText nameEdit = (EditText)constructorBlock.findViewById(R.id.nameEditText);
                final String[] name=new String[1];

                imageViewConstuctor = (ImageView)constructorBlock.findViewById(R.id.imageView);

                View.OnClickListener imageCLickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                    }
                };
                imageViewConstuctor.setOnClickListener(imageCLickListener);


                updateConstructorBlock(constructorBlock, languageIsEng, XE_CARB_VARIABLE);
                ScrollView.OnClickListener cancelButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(constructorBlock!=null) {
                            constructorBlock.setVisibility(View.INVISIBLE);
                            constructorBlock = null;
                        }
                    }
                };

                ScrollView.OnClickListener okButtonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(constructorBlock!=null) {
                            name[0]= nameEdit.getText() + "";
                            if(!databaseListHasNo(name[0])){
                                nameEdit.requestFocus();
                                Toast.makeText(activity,"database already contains "+name[0],5000).show();
                            }else{

                                //если имя уникально,то  и ключ в Buffer тоже уникален будет
                                new Buffer(name[0],bitmap);
                                if(bitmap!=null) {
                                    new ImagesProvider(activity).setFileName(name[0] + ".png").setExternal(false).setDirectoryName("newimagesfolder").saveBitmap(bitmap);
                                }
                                String amount = amountEditText.getText() + "";
                                String callories = calloriesEdtText.getText() + "";

                                if(p.matcher(amount).matches()&&Integer.parseInt(amount)>0
                                         &&p.matcher(callories).matches()&&Integer.parseInt(callories)>0
                                         &&name[0].matches("^[a-z а-я]{2,30}$")) {

                                    //добавление нового продукта в новую группу "моя группа"
                                    Product.addProductInNewGroup(name[0],name[0], Integer.parseInt(amount), Integer.parseInt(callories),ImagesProvider.filePath);


                                    //Adding a new name into database of autocomplete textView
                                    String[] auxNamesArray = Product.setNamesMap(ADD, name[0]).keySet().toArray(new String[0]);

                                    autoTVadapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, auxNamesArray);
                                    autoTV.setAdapter(autoTVadapter);

                                    hideKeyboardOf(nameEdit);
                                    hideKeyboardOf(amountEditText);
                                    hideKeyboardOf(calloriesEdtText);

                                    constructorBlock.setVisibility(View.INVISIBLE);
                                    constructorBlock = null;

                                    logCursor(dbHelper.getReadableDatabase().rawQuery("select * from products", null),"Population");
                                }
                            }
                        }
                    }
                };

                Button okButton = (Button) constructorBlock.findViewById(R.id.okButton);
                okButton.setOnClickListener(okButtonListener);
                cancel_button = (Button) constructorBlock.findViewById(R.id.cancel_Button);
                cancel_button.setOnClickListener(cancelButtonListener);

                getWindow().addContentView(constructorBlock, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        if(leftListGridLayout==null)
            leftListGridLayout = (GridLayout)findViewById(R.id.grid);
        leftListGridLayout.layout(0,0,screeWidth-expandListWIDTH,leftListGridLayout.getHeight());

        rightPartLL = (RelativeLayout)findViewById(R.id.rightPartLL);
        header = (LinearLayout)findViewById(R.id.header);

        block_in_header =(LinearLayout)getLayoutInflater().inflate(R.layout.left_header_expand,null);
        XE_headerTV =(TextView) block_in_header.findViewById(R.id.xeTV);
        call_headerTV =(TextView) block_in_header.findViewById(R.id.calloriesTV);
        XEhidden_headerTV =(TextView) block_in_header.findViewById(R.id.xeTVhidden);
        makeHeaderSmaller();
        header.addView(block_in_header);
        rowHeight = (int)(60*Resources.getSystem().getDisplayMetrics().density);

    /**Animation of right list  & left list!!!*/
        expListListener= new GestureDetector.SimpleOnGestureListener(){

            int difference=100;

            @Override
            public  boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                expandableListView.invalidateViews();

                if(e1.getX() - e2.getX() < 0 && Math.abs(e2.getX() - e1.getX())>difference){
                    makeHeaderBigger();
                }
                return false;
            }

            @Override
            public   boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                expandableListView.invalidateViews();

                Thread closeSliderThread = new Thread(new closingSlider());
                Thread openingSliderThread = new Thread(new openingSlider());

                    if (e1.getX() - e2.getX() < 0 && Math.abs(e2.getX() - e1.getX()) > difference) {

                        makeHeaderBigger();

                        synchronized (rightPartLL) {
                            synchronized (closeSliderThread) {
                                closeSliderThread.start();
                            }

//                        for (int i = 0; i < expandableListView.getCount(); i++) expandableListView.collapseGroup(i);

                        }
                        return true;
                    }

                Toast.makeText(activity, (e1.getX() - e2.getX() > 0 && Math.abs(e1.getX() - e2.getX()) > difference) + "", 5000);
                    if (e2.getX() - e1.getX() > 0
//                            && Math.abs(e1.getX() - e2.getX()) > difference
                            )
                    {
                        makeHeaderSmaller();
                        synchronized (rightPartLL) {
                            synchronized (openingSliderThread){
                                openingSliderThread.start();
                            }

                        }
                        return true;
                    }
                    return false;
            }


            @Override
            public boolean onDown(MotionEvent e) {

                return true;
            }

             class closingSlider implements Runnable{
                 @Override
                 public void run() {
                     rightPartLL.post(new Runnable() {
                         @Override
                         public void run() {
                             synchronized (closingSlider.class) {
                                 rightPartLL.startAnimation(hide_slider);
                             }
                         }
                     });

                 }
             }
            class openingSlider implements Runnable{
                @Override
                public void run() {
                    rightPartLL.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (openingSlider.class) {
                                rightPartLL.startAnimation(show_slider);
                            }
                        }
                    });

                }
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                makeHeaderSmaller();
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }
        };


        gestureDetectorOfExpListView = new GestureDetector(activity,expListListener);

        TableRow rightPart = (TableRow)findViewById(R.id.rightPart);
        RelativeLayout rightLL = (RelativeLayout)rightPart.findViewById(R.id.rightPartLL);
        TableRow row = (TableRow)rightLL.findViewById(R.id.expRow);
        row.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return gestureDetectorOfExpListView.onTouchEvent(motionEvent);
            }
        });
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

        Set<String>set = Product.getNames().keySet();
        autoTVadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,set.toArray(new String[0]));
        autoTV.setAdapter(autoTVadapter);
        autoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(autoTV,0);

            }
        });

        ArrayList<ArrayList<Product>> productsList =Product.getProducts();
        if(adapter4==null) {
            adapter4 = new FoodAdapter4(this, productsList, expandableListView);
        }
        expandableListView.setAdapter(adapter4);

        updateAdapter();

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCleared = true;
                if (leftListGridLayout.getRowCount() > 0) {
                    leftListGridLayout.removeAllViews();
                    finalResult.setText("0.0");
                    kkalTV.setText(0 + "");
                }

            }
        });
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return gestureDetectorOfExpListView.onTouchEvent(motionEvent)|expandableListView.onTouchEvent(motionEvent);
            }

        });


        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            final String message = "Choose the action below:";

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    int indexAdditional=adapter4.getFoodList().size()-1;

                    if (adapter4.getFoodList().size()>5&& groupPosition==indexAdditional) {

                        TextView tv = (TextView) view.findViewById(R.id.textItem);

                        if (tv.isEnabled()) {
                            newDialogInstance(view, childPosition, tv.getText().toString(), message).show(getFragmentManager(), "dlg");
                            return true;
                        }
                    }
                }
                return false;
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

                try {
                    adapter4.setClicked(i, i1);
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


                        /**here we convert float number if it does not contain decimals*/
                        XE_TV.setText(convertFloat(XE_CARB_VARIABLE) + "");
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

                                    if (product1.getGroupName().equals("drinks") || product1.getName().matches("milk|kefir|cream")) {
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
                                    String[] pictNames;
                                    String productName = product1.getName();
                                    String pictureURL  = product1.getPicURL();

                                    if(pictureURL!=null){
                                        Bitmap b = BitmapFactory.decodeFile(pictureURL);
                                        image.setImageBitmap(b);
                                        Log.d(LOG_TAG,  itemName+ " absolute url is active");
                                    }else {

                                        try {
                                            //Проверяем,есть ли в буффере название добавленного ранее продукта
                                            //и если есть ,то тянем Картинку по названию из буффера
                                            if (Buffer.getPictures_map().containsKey(itemName)) {
                                                if (productName.equals(itemName)) {
                                                    Bitmap bitmap = new ImagesProvider(activity).setFileName(itemName + ".png").setDirectoryName("newimagesfolder").setExternal(false).load();
                                                    image.setImageBitmap(bitmap);
//                                                image.setImageBitmap(Buffer.getPictures_map().get(itemName));
                                                }
                                            }
                                            pictNames = assetManager.list(Product.getGroupsList().get(i));
                                            for (String pictName : pictNames) {
                                                String name = pictName.substring(0, pictName.length() - 4);

                                                if (productName.startsWith("potato")) {
                                                    productName = "potato";
                                                }
                                                if (productName.equals(name)) {
                                                    InputStream in = assetManager.open(Product.getGroupsList().get(i) + "/" + pictName);
                                                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                                                    image.setImageBitmap(bitmap);
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    String newName = Character.valueOf(itemName.charAt(0)).toString().toUpperCase() + itemName.substring(1, itemName.length());
                                    nameItem.setText(newName);

                                    final TextView caloriesTV = (TextView) blockLayout.findViewById(R.id.caloriesTV);
                                    caloriesTV.setText(callories + "");

                                    final TextView xeTVresult = (TextView) blockLayout.findViewById(R.id.xeTV_result);
                                    final EditText editAmount = (EditText) blockLayout.findViewById(R.id.amountEditText);

                                    if (XE_CARB_VARIABLE % 10 == 0) {
                                        if (XE_CARB_VARIABLE == 12.0) xeTVresult.setText(12 + "");
                                        else if (XE_CARB_VARIABLE == 1.0) xeTVresult.setText(1 + "");
                                    }


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
                                    final View finalView = view;

                                    animateRowToLeft(view);
                                    Runnable deleteRow = new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter4.setClicked(i, i1);
                                            leftListGridLayout.removeView(view);
                                            calculateResult(leftListGridLayout);
                                        }
                                    };

                                    Button b = (Button) view.findViewById(R.id.button);
                                    for (int k = 0; k < expandableListView.getChildCount(); k++) {

                                        LinearLayout itemView = getViewfromExpandableList(expandableListView, k);
                                        CheckedTextView tv1 = (CheckedTextView) itemView.findViewById(R.id.textItem);

                                        if (tv1 == null) {
                                            tv1 = new CheckedTextView(activity);
                                            tv1.setText(b.getText());
                                            tv1.setChecked(true);
                                            tv1.setEnabled(false);
                                            String buttontext = b.getText().toString();
                                            String tvtext = tv1.getText().toString();

                                            if (buttontext.equals(tvtext)) {
                                                handler.postDelayed(deleteRow, 350);
                                                return true;
                                            }
                                        }
                                    }

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
                            @SuppressLint("NewApi")
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

                } finally {
                    if (leftListGridLayout.getRowCount() != 0) {
                        isCleared = false;
                    }
                }
            }

            private synchronized void animateRowToLeft(View view1) {

                try {
                    a = new TranslateAnimation(view1.getX(), -300, 0, view1.getY());
                    a.setDuration(300);
                    a.setFillAfter(true);
                    view1.setAnimation(a);
                    view1.startAnimation(a);
                } finally {
                    a = null;
                }
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

                    for (String name : names.keySet()) {
                       if(patternName.equals(name)){
                           groupIndex = names.get(name);
                           expandableListView.expandGroup(groupIndex);
                           ArrayList <Product> productList = Product.getProducts().get(groupIndex);
                           for(int i=0;i<productList.size();i++){
                               Product product = productList.get(i);
                               String nameProduct = languageIsEng?product.getName():product.getAuxName();
                               if(nameProduct.equals(name)){
                                   int childIndex = i;
                                   adapter4.setChildFounded(groupIndex, childIndex);

                                   hideKeyboardOf(autoTV);
                               }
                           }
                       }
                    }
                }
                autoTV.setText("");
                isCleared=false;
            }
        });

    }    ///OnCreate end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GALLERY_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Uri imgUri = data.getData();
                    if(imgUri!=null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                            imageViewConstuctor.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    private boolean databaseListHasNo(String name) {
        for(String n:Product.getNames().keySet()){
            if(n.equalsIgnoreCase(name))
                return false;
        }
        return true;
    }

    public static MyAlertDialog newDialogInstance(View choosen ,int index,String title, String message){
        MyAlertDialog dialog = new MyAlertDialog(choosen,index);
        return dialog;
    }




    @SuppressLint("ValidFragment")
    public static class MyAlertDialog extends DialogFragment {

        private static View choosenView;
        private static int index=-1;
        @SuppressLint("ValidFragment")
        public  MyAlertDialog(View view,int indez){
          this.choosenView=view;
          this.index=indez;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title= ((TextView)choosenView.findViewById(R.id.textItem)).getText().toString();
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage("actions:")
                    .setPositiveButton("delete", deleteItemListener)
//                    .setNeutralButton("edit",editItemListener)
                    .setNegativeButton("cancel",cancelListener)
                    .create();
        }
    }
    static DialogInterface.OnClickListener deleteItemListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            synchronized (Product.class) {
                Log.d("REMOVAL: ", "main array is: " + adapter4.getFoodList().toString());

                TextView tv = (TextView) MyAlertDialog.choosenView.findViewById(R.id.textItem);

                removeFromAdditionalGroup(MyAlertDialog.index);

                ArrayList<ArrayList<Product>> groupsOfProducts = Product.getProducts();
                final int indexAdditional = groupsOfProducts.size() - 1;
                ArrayList<Product> additionalGroup = groupsOfProducts.remove(indexAdditional);
                Product removedProduct = additionalGroup.remove(MyAlertDialog.index);
                groupsOfProducts.add(indexAdditional, additionalGroup);
                adapter4.setFoodList(groupsOfProducts);

                Buffer.getPictures_map().remove(removedProduct.getName());

                deleteItemFromDatabase(dbHelper,removedProduct.getName());

                //If our addittional group has no children we remove it as well.
                if (additionalGroup.isEmpty()) {
                    adapter4.getFoodList().remove(indexAdditional);
                    dbHelper.getWritableDatabase().delete("products",null,null);
                }

                //Removing the product` name from adapter`s array
                String productName = tv.getText().toString();
                HashMap<String, Integer> map = Product.setNamesMap(REMOVING, productName);
                String[] arr = map.keySet().toArray(new String[0]);
                autoTVadapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, arr);
                autoTV.setAdapter(autoTVadapter);

                adapter4.notifyDataSetChanged();
            }

        }
    };

    private static void removeFromAdditionalGroup(int index) {

    }

    static DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    public void onBackPressed(){
        if(blockLayout==null&&constructorBlock==null) {
            super.onBackPressed();
        }
        if(autoTV.isFocusable()) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(autoTV.getWindowToken(), 0);
        }
        if(blockLayout!=null){
           cancelButton.callOnClick();
        }
        if(constructorBlock!=null){
           cancel_button.callOnClick();
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
        XE_TV.setText(convertFloat(res) + "");

        kall_TV.setText(resultCall+"");
        calculateResult(leftListGridLayout);

        hideKeyboardOf(editAmount);

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
            TextView gramTV = (TextView)blockLayout1.findViewById(R.id.gramTV);
            TextView kkalTV = (TextView)blockLayout1.findViewById(R.id.kkalTV);
            String XEprevious = xe_or_carbs_TV.getText().toString();
            float xePast = Float.parseFloat(XEprevious);

            if (isEnglish) {
                item_name = product.getName();
                String newName = Character.valueOf(item_name.charAt(0)).toString().toUpperCase() + item_name.substring(1, item_name.length());
                weightTV_block1.setText(R.string.weightTV_block_eng);
                XeTV_block1.setText(R.string.XeTV_block_eng);
                calloriesTV_block1.setText(R.string.calloriesTV_block_eng);
                gramTV.setText(R.string.gram_ml_ENG);
                kkalTV.setText(R.string.kkal_ENG);
                nameItem1.setText(newName);
                float res = xePast*12;
                xe_or_carbs_TV.setText(res+"");
                xe_tv_block.setText("carbs");
            }
            if (!isEnglish) {
                item_name = product.getAuxName();
                String newName = Character.valueOf(item_name.charAt(0)).toString().toUpperCase() + item_name.substring(1, item_name.length());
                weightTV_block1.setText(R.string.weightTV_block_rus);
                XeTV_block1.setText(R.string.XeTV_block_rus);
                calloriesTV_block1.setText(R.string.calloriesTV_block_rus);
                gramTV.setText(R.string.gram_ml_RUS);
                kkalTV.setText(R.string.kkal_RUS);
                nameItem1.setText(newName);
                float res = xePast/12;
                xe_or_carbs_TV.setText(res+"");
                xe_tv_block.setText("XE");
            }
        }
    }

    public void updateConstructorBlock(ScrollView constuctor,boolean isEnglish,float carbValue){

        if(constuctor!=null) {
            EditText name = (EditText) constuctor.findViewById(R.id.nameEditText);
            TextView weight = (TextView) constuctor.findViewById(R.id.weightTV_block);
            TextView gr_or_ml = (TextView) constuctor.findViewById(R.id.gramTV);
            TextView callories = (TextView) constuctor.findViewById(R.id.calloriesTV_block);
            TextView kkalTV = (TextView) constuctor.findViewById(R.id.kkalTV);
            TextView in = (TextView) constuctor.findViewById(R.id.inTV);
            TextView carbsLable = (TextView) constuctor.findViewById(R.id.XE_GRAM_lable);
            TextView gramsOfCarbs = (TextView) constuctor.findViewById(R.id.grOfCarb);
            if (isEnglish) {
                name.setHint(R.string.name_of_item_HINT_ENG);
                weight.setText(R.string.weightTV_block_eng);
                gr_or_ml.setText(R.string.gram_ml_ENG);
                callories.setText(R.string.calloriesTV_block_eng);
                kkalTV.setText(R.string.kkal_ENG);
                in.setText(R.string.in_ENG);
                carbsLable.setText(carbValue+"");
                gramsOfCarbs.setText(R.string.gramOfCarbsENG);
            }
            if (!isEnglish) {
                name.setHint(R.string.name_of_item_HINT_RUS);
                weight.setText(R.string.weightTV_block_rus);
                gr_or_ml.setText(R.string.gram_ml_RUS);
                callories.setText(R.string.calloriesTV_block_rus);
                kkalTV.setText(R.string.kkal_RUS);
                in.setText(R.string.in_RUS);
                carbsLable.setText(carbValue+"");
                gramsOfCarbs.setText(R.string.gramOfCarbsRUS);
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
           LinearLayout l = (LinearLayout) e.getChildAt(i);
           TextView tv = (TextView) l.findViewById(R.id.XeRowTV);
           res += Float.parseFloat(tv.getText().toString());
           TextView calltv = (TextView) l.findViewById(R.id.calloriesRowHiddenTV);
           res2 += Integer.parseInt(calltv.getText().toString());
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

    }

    void initAnimations(){


        hide_slider =new TranslateAnimation(activity,null) ;
        hide_slider.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                int ruchkaWidth= ruchkaWIDTH;
                ruchka.layout(screeWidth-ruchkaWidth,0,ruchkaWidth,(int)ruchka.getY());
                rightPartLL.layout(screeWidth-ruchkaWidth , 0, screeWidth + expandListWIDTH-ruchkaWidth, rightPartLL.getHeight());
                leftListView.layout(0, 0, screeWidth , leftListView.getHeight());
                leftListGridLayout.layout(0, 0, screeWidth , leftListGridLayout.getHeight());

                int height = rowHeight;
                int res = rowHeight;
                for (int i = 0; i < leftListGridLayout.getRowCount(); i++) {

                    LinearLayout v = (LinearLayout) leftListGridLayout.getChildAt(i);
                    v.layout(0, (int) v.getY(), screeWidth, res);
                    res += height;

                    final TextView xe_tv = (TextView) v.findViewById(R.id.XeRowTV);
                    xe_tv.layout(350, (int) xe_tv.getY(), 410, xe_tv.getHeight() + 18);

                    TextView callTV = (TextView) v.findViewById(R.id.calloriesRowHiddenTV);
                    callTV.layout(280, (int) callTV.getY(), 350, callTV.getHeight() + 16);
                    callTV.setVisibility(View.VISIBLE);


                }
                leftListGridLayout.invalidate();
                animation.setFillAfter(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        show_slider =new TranslateAnimation(activity,null);
        show_slider.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @SuppressWarnings("Unchecked")
            @Override
            public void onAnimationEnd(Animation animation) {
                int ruchkaWidth=1;
                ruchka.layout(screeWidth - expandListWIDTH,0,ruchkaWidth,(int)ruchka.getY());
                rightPartLL.layout(screeWidth - expandListWIDTH-ruchkaWidth, 0, screeWidth, rightPartLL.getHeight());
                leftListView.layout(0, 0, screeWidth-expandListWIDTH+ruchkaWidth, leftListView.getHeight());
                leftListGridLayout.layout(0, 0, screeWidth-expandListWIDTH+ruchkaWidth, leftListGridLayout.getHeight());


                int rowWidth = screeWidth-expandListWIDTH;
                int height = rowHeight;
                int res = rowHeight;

                for (int i = 0; i < leftListGridLayout.getRowCount(); i++) {
                    if(!(leftListGridLayout.getChildAt(i) instanceof ScrollView)) {
                        LinearLayout v1 = (LinearLayout) leftListGridLayout.getChildAt(i);
                        v1.layout((int) v1.getX(), (int) v1.getY(), rowWidth, res);
                        res += height;
                        TextView callTV = (TextView) v1.findViewById(R.id.calloriesRowHiddenTV);
                        callTV.setVisibility(View.INVISIBLE);
                    }
                }
                leftListGridLayout.invalidate();
                animation.setFillAfter(true);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static Number convertFloat(float num){
        if((num-(int)num)==0){
            return (int)num;
        }
        else{
            return num;
        }
    }
    public synchronized static void  hideKeyboardOf(View v){
        if(v!=null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }else{
            inputMethodManager.hideSoftInputFromWindow(autoTV.getWindowToken(), 0);
        }

    }


    public static LinearLayout getViewfromExpandableList(ExpandableListView expandableListView1,int indexOfView){
        LinearLayout v = (LinearLayout) expandableListView1.getChildAt(indexOfView);
        return v;
    }

    public void makeHeaderSmaller(){

        XEhidden_headerTV.setVisibility(View.VISIBLE);
        XE_headerTV.setVisibility(View.INVISIBLE);
        call_headerTV.setVisibility(View.INVISIBLE);
    }

    public void makeHeaderBigger(){

        XEhidden_headerTV.setVisibility(View.INVISIBLE);
        XE_headerTV.setVisibility(View.VISIBLE);
        call_headerTV.setVisibility(View.VISIBLE);
    }
    static void logCursor(Cursor c, String title) {
        if (c != null) {
            if (c.moveToFirst()) {
                Log.d(LOG_TAG, title + ". " + c.getCount() + " rows");
                StringBuilder sb = new StringBuilder();
                do {
                    sb.setLength(0);
                    for (String cn : c.getColumnNames()) {
                        sb.append(cn + " = "
                                + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, sb.toString());
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, title + ". Cursor is null");
    }

    public static void deleteItemFromDatabase(DBHelper helper,String itemName){

        SQLiteDatabase db = helper.getWritableDatabase();
//        if(databaseIsEmpty(db)){
            Log.d(LOG_TAG,  itemName+ " is deleted from database");

            db.execSQL("DELETE FROM products WHERE name='"+itemName+"'");


    }
    public static boolean databaseIsEmpty(){
        boolean isEmpty  = true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from products", null);
        int rowCount = c.getCount();
        c.close();
        if(rowCount>0){
            return false;
        }else if(rowCount==0){
            return true;
        }
        return false;
    }

    public static void updateAdapter(){
        if(!databaseIsEmpty()){
            SQLiteDatabase db =  dbHelper.getReadableDatabase();
            Cursor c  = db.query("products", null, null, null, null, null, null);
            if(c.moveToFirst()){
                int nameInd = c.getColumnIndex("name");
                int auxNameInd = c.getColumnIndex("aux_name");
                int amountInd = c.getColumnIndex("amount");
                int callInd = c.getColumnIndex("callories");
//                int urlInd = c.getColumnIndex("url");

                do{
                    String name = c.getString(nameInd);
                    String auxname = c.getString(auxNameInd);
                    int amount = c.getInt(amountInd);
                    int callories = c.getInt(callInd);
//                    String url = c.getString(urlInd);
                    if(databaseDoesntContain(name)) {
                        Product.addProductInNewGroup(name,auxname,amount,callories,null);
                    }else {
                        break;
                    }
                }while (c.moveToNext());
            }
            c.close();
        }else {
            Toast.makeText(activity, "database is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean databaseDoesntContain(String name) {
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        Cursor c  = db.query("products", null, null, null, null, null, null);
        if(c.moveToFirst()){
            int nameInd = c.getColumnIndex("name");
            String a_name = c.getString(nameInd);
            if(a_name.equals(name)){
                return false;
            }
        }
        c.close();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context,"myDB",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL("create table if not exists  products(" +
                 "id integer primary key autoincrement," +
                 "name text,aux_name text," +
                 "amount integer," +
                 "callories integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
