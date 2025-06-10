package com.example.xml_parser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "XMLParser";
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentLayout = findViewById(R.id.contentLayout);
        Button parseButton = findViewById(R.id.parseButton);

        parseButton.setOnClickListener(v -> parseXML());
    }

    private void parseXML() {
        List<Item> items = new ArrayList<>();
        try {
            InputStream is = getAssets().open("data.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);

            String text = "";
            Item currentItem = null;
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("item".equalsIgnoreCase(tagName)) {
                            currentItem = new Item();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (currentItem != null) {
                            if ("name".equalsIgnoreCase(tagName)) {
                                currentItem.setName(text);
                            } else if ("price".equalsIgnoreCase(tagName)) {
                                currentItem.setPrice(text);
                            } else if ("description".equalsIgnoreCase(tagName)) {
                                currentItem.setDescription(text);
                            } else if ("item".equalsIgnoreCase(tagName)) {
                                items.add(currentItem);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }


            displayContent(items);

        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing XML", e);
            Toast.makeText(this, "Error parsing XML: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void displayContent(List<Item> items) {
        contentLayout.removeAllViews();

        TextView headerView = new TextView(this);
        headerView.setText("DATA XML");
        headerView.setTextSize(18);
        headerView.setTextColor(Color.WHITE);
        headerView.setPadding(16, 16, 16, 16);
        headerView.setBackgroundColor(Color.rgb(63, 81, 181));
        headerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        contentLayout.addView(headerView);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);


            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(16, 16, 16, 16);


            if (i % 2 == 0) {
                itemLayout.setBackgroundColor(Color.rgb(238, 238, 238));
            } else {
                itemLayout.setBackgroundColor(Color.WHITE);
            }


            TextView nameTextView = new TextView(this);
            nameTextView.setText("Name: " + item.getName());
            nameTextView.setTextSize(16);
            nameTextView.setTextColor(Color.BLACK);
            nameTextView.setPadding(0, 0, 0, 8);
            itemLayout.addView(nameTextView);


            TextView priceTextView = new TextView(this);
            priceTextView.setText("Price: " + item.getPrice());
            priceTextView.setTextSize(14);
            priceTextView.setTextColor(Color.rgb(33, 33, 33));
            priceTextView.setPadding(0, 0, 0, 8);
            itemLayout.addView(priceTextView);


            TextView descTextView = new TextView(this);
            descTextView.setText("Description: " + item.getDescription());
            descTextView.setTextSize(14);
            descTextView.setTextColor(Color.rgb(66, 66, 66));
            itemLayout.addView(descTextView);


            View divider = new View(this);
            divider.setBackgroundColor(Color.LTGRAY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            divider.setLayoutParams(params);


            contentLayout.addView(itemLayout);
            if (i < items.size() - 1) {
                contentLayout.addView(divider);
            }
        }
    }


    private static class Item {
        private String name;
        private String price;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}