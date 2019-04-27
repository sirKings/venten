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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String message = getIntent().getStringExtra("message");
        Log.e("message", "hf  "+message);

         String length;
         String width;
         String firstColor;
         String secondColor;
         String timeString;
         String dateString;
         String codedMessage;

        final String secondLineRegex = "(?<=\\n|\\r)(.*)(?=\\n|\\r)";
        final String timeRegex = "([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])\\s*([AaPp][Mm])";
        final String dateRegex = "([0-9]{2}[/]{1}[0-9]{2}[/]{1}[0-9]{4})";
        final String lengthRegex = "(?<=w)(.*)(?=l)";
        final String widthRegex = "(?<=SZ )(.*)(?=w)";
        final String firstColorRegex = "(?<=/)(.*)(?=-)";
        final String secondColorRegex = "(?<=-)(.*)(?=)";


        final Pattern secondLinePattern = Pattern.compile(secondLineRegex, Pattern.MULTILINE);
        final Pattern timePattern = Pattern.compile(timeRegex, Pattern.MULTILINE);
        final Pattern datePattern = Pattern.compile(dateRegex, Pattern.MULTILINE);
        final Pattern lengthPattern = Pattern.compile(lengthRegex, Pattern.MULTILINE);
        final Pattern widthPattern = Pattern.compile(widthRegex, Pattern.MULTILINE);
        final Pattern firstColorPattern = Pattern.compile(firstColorRegex, Pattern.MULTILINE);
        final Pattern secondColorPattern = Pattern.compile(secondColorRegex, Pattern.MULTILINE);

        firstColor = decodeMessage(firstColorPattern, message);
        secondColor = decodeMessage(secondColorPattern, message);
        width = decodeMessage(widthPattern, message);
        length = decodeMessage(lengthPattern, message);
        timeString = decodeMessage(timePattern, message);
        dateString = decodeMessage(datePattern, message);
        codedMessage = decodeMessage(secondLinePattern, message);



        //11/18/2016:7:37 AM
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.UK);


        try{
            Date msgDate = dateFormat.parse(dateString);
            date.setText(getDate(msgDate));
        }catch (Exception e){
            e.printStackTrace();
        }

        String imageUrl = "https://dummyimage.com/"+width.trim()+"x"+length.trim()+"/"+firstColor.trim()+"/"+secondColor.trim();

        codeMsg.setText(codedMessage);
        Picasso.with(this).load(imageUrl).placeholder(getResources().getDrawable(R.drawable.placeholder)).into(imageView);
        time.setText(timeString);

        Log.e("image", imageUrl);

    }

    private String getDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return "Date: "+c.get(Calendar.DATE)+getDayNumberSuffix(c.get(Calendar.DATE))+" "+c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK)+ " "+c.get(Calendar.YEAR);
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

    private String decodeMessage(Pattern pattern, String message){
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()){
            return matcher.group(0);
        }else{
            return "Not found";
        }
    }
}
