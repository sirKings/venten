package kingsleyjohn.com.ven10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CodedMessage extends AppCompatActivity {

    private ImageView imageView;
    private TextView date;
    private TextView time;
    private TextView codeMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coded_message);

        imageView = findViewById(R.id.codedImage);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        codeMsg = findViewById(R.id.codedMessage);

         String length;
         String width;
         String firstColor;
         String secondColor;

        String message = getIntent().getStringExtra("message");
        Log.e("message", "hf  "+message);

        //11/18/2016:7:37 AM
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy:H:mm a", Locale.UK);

        String firstLine = message.split("\\n")[0];
        String secondLine = message.split("\\n")[1];
        String thirdLine = message.split("\\n")[2];


        firstColor = thirdLine.split("/")[1].split("-")[0];
        secondColor = thirdLine.split("/")[1].split("-")[1];

        width = thirdLine.split("/")[0].split("Z")[1].split("w")[0];
        String lengthSection = thirdLine.split("/")[0].split("Z")[1].split("w")[1];
        length = lengthSection.substring(0, lengthSection.length()-2);



        try{
            Date msgDate = dateFormat.parse(firstLine.substring(3));
            date.setText(getDate(msgDate));
            time.setText(getTime(msgDate));
        }catch (Exception e){
            e.printStackTrace();
        }

        String imageUrl = "https://dummyimage.com/"+width.trim()+"x"+length.trim()+"/"+firstColor.trim()+"/"+secondColor.trim();

        codeMsg.setText(secondLine);
        Picasso.with(this).load(imageUrl).into(imageView);


    }

    private String getDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return "Date: "+c.get(Calendar.DATE)+getDayNumberSuffix(c.get(Calendar.DATE))+" "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK)+ " "+c.get(Calendar.YEAR);
    }

    private String getTime(Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.UK);
        return "Time: "+timeFormat.format(date).toUpperCase();
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
